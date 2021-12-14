package com.zy.zygifloader;

import android.graphics.Bitmap;

import com.zy.zygifloader.task.GifFrame;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import cn.charles.kasa.framework.utils.LogUtil;

/**
 * Handler for read & extract Bitmap from *.gif
 * 参考资料 @url: https://www.jianshu.com/p/38743ef278ac
 */
public class MyGifHelper {
    private static final int MAX_STACK_SIZE = 4096;

    // to define some error type
    public static final int STATUS_OK = 0;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OPEN_ERROR = 2;

    // 状态
    protected int status;

    protected InputStream in;

    protected int width;
    protected int height;
    // global color table used
    protected boolean gctFlag;
    // size of global color table
    protected int gctSize;

    // global color table
    protected int[] gct;
    // local color table
    protected int[] lct;
    // active color table
    protected int[] act;

    // local color table flag
    protected boolean lctFlag;
    // interface flag
    protected boolean interlace;
    // local color table size
    protected int lctSize;

    // background color index
    protected int bgIndex;
    // background color
    private int bgColor;
    // previous bg color
    private int lastBgColor;

    // pixel aspect ratio
    protected int pixelAspect;

    // frames and from current file
    protected Vector<GifFrame> frames;
    protected int frameCount;

    // current image rectangle
    protected int ix, iy, iw, ih;
    protected int lrx, lry, lrw, lrh;

    // use transparent color
    protected boolean transparency = false;
    // delay in milliseconds
    protected int delay = 0;
    // transparent color index
    protected int transIndex;

    // current data block
    protected byte[] block = new byte[256];
    // block size
    protected int blockSize = 0;

    // LZW decoder working arrays
    // 前缀
    protected short[] prefix;
    // 后缀
    protected byte[] suffix;
    protected byte[] pixelStack;
    protected byte[] pixels;
    // current frame
    private Bitmap image;
    // previous frame
    private Bitmap lastImage;

    // last graphic control extension info
    private int dispose = 0;
    //     0=no action; 1=leave in place; 2=restore to bg; 3=restore to prev
    private int lastDispose = 0;
    // iterations; 0 = repeat forever
    private int loopCount = 1;

    public Bitmap getImage() {
        return getFrame(0);
    }

    public Bitmap getFrame(int _n) {
        Bitmap im = null;
        if ((_n >= 0) && (_n < frameCount)) {
            im = ((GifFrame) frames.elementAt(_n)).image;
        }
        return im;
    }

    private void setPixels() {
        int[] dest = new int[width * height];
        // fill in starting image contents based on last image's dispose code
        if (lastDispose > 0) {
            if (lastDispose == 3) {
                // use image before last
                int n = frameCount - 2;
                if (n > 0) {
                    lastImage = getFrame(n - 1);
                } else {
                    lastImage = null;
                }
            }
            if (lastImage != null) {
                lastImage.getPixels(dest, 0, width, 0, 0, width, height);
                // copy pixels
                if (lastDispose == 2) {
                    // fill last image rect area with background color
                    int c = 0;
                    if (!transparency) {
                        c = lastBgColor;
                    }
                    for (int i = 0; i < lrh; i++) {
                        int n1 = (lry + i) * width + lrx;
                        int n2 = n1 + lrw;
                        for (int k = n1; k < n2; k++) {
                            dest[k] = c;
                        }
                    }
                }
            }
        }

        // copy each source line to the appropriate place in the destinatioin
        int pass = 1;
        int inc = 8;
        int iline = 0;
        for (int i = 0; i < ih; i++) {
            int line = i;
            if (interlace) {
                if (iline >= ih) {
                    pass++;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            inc = 4;
                            break;
                        case 4:
                            iline = 1;
                            inc = 2;
//                            break;
                    }
                }
                line = iline;
                iline += inc;
            }
            line += iy;
            if (line < height) {
                int k = line * width;
                // start of line in dest
                int dx = k + ix;
                // end of dest line
                int dlim = dx + iw;
                if ((k + width) < dlim) {
                    // past dest edge
                    dlim = k + width;
                }
                // start of line in source
                int sx = i * iw;
                while (dx < dlim) {
                    // map color and insert in destination
                    int index = ((int) pixels[sx++]) & 0xff;
                    int c = act[index];
                    if (c != 0) {
                        dest[dx] = c;
                    }
                    dx++;
                }
            }
        }
        image = Bitmap.createBitmap(dest, width, height, Bitmap.Config.RGB_565);
    }

    public GifFrame[] getFrames() {
        if (null != frames) return frames.toArray(new GifFrame[0]);
        return null;
    }

    // to read & parse all *.gif stream
    public int read(InputStream _is) {
        LogUtil.loge("read run ...");
        LogUtil.loge("pre run init ...");
        init();
        if (_is != null) {
            in = _is;
            LogUtil.loge("pre run readHeader ...");
            readHeader();
            if (!err()) {
                LogUtil.loge("pre run readContents ...");
                readContents();
                if (frameCount < 0) {
                    status = STATUS_FORMAT_ERROR;
                }
            }
        } else {
            status = STATUS_OPEN_ERROR;
        }
        try {
            _is.close();
        } catch (IOException _e) {
            _e.printStackTrace();
        }
        LogUtil.loge("read run over ...");
        return status;
    }

    private void decodeImageData() {
        LogUtil.loge("decodeImageData run ...");
        int NullCode = -1;
        int npix = iw * ih;
        int data_size, clear, end_of_information, available, in_code, old_code, code_size, code_mask, code, i,
                datum, bits, count, first, top, pi, bi;
        if ((pixels == null) || (pixels.length < npix)) {
            // allocate new pixel array
            pixels = new byte[npix];
        }
        if (prefix == null) {
            prefix = new short[MAX_STACK_SIZE];
        }
        if (suffix == null) {
            suffix = new byte[MAX_STACK_SIZE];
        }
        if (pixelStack == null) {
            pixelStack = new byte[MAX_STACK_SIZE + 1];
        }

        // Initialize GIF data stream decoder.
        data_size = read();
        clear = 1 << data_size;
        end_of_information = clear + 1;
        available = clear + 2;
        old_code = NullCode;
        code_size = data_size + 1;
        code_mask = (1 << code_size) - 1;
        LogUtil.loge("预置数据 ...");
        for (code = 0; code < clear; code++) {
            prefix[code] = 0;
            suffix[code] = (byte) code;
        }

        // Decode GIF pixel stream.
        datum = bits = count = first = top = pi = bi = 0;
        for (i = 0; i < npix; ) {
            if (top == 0) {
                if (bits < code_size) {
                    // Load bytes until there are enough bits for a code.
                    if (count == 0) {
                        // Read a new data block
                        count = readBlock();
                        if (count <= 0) {
                            break;
                        }
                        bi = 0;
                    }

                    datum += (((int) block[bi]) & 0xff) << bits;
                    bits += 8;
                    bi++;
                    count--;
                    continue;
                }

                // Get the next code.
                code = datum & code_mask;
                datum >>= code_size;
                bits -= code_size;

                // Interpret the code
                if ((code > available) || (code == end_of_information)) {
                    break;
                }
                if (code == clear) {
                    // Reset decoder
                    code_size = data_size + 1;
                    code_mask = (1 << code_size) - 1;
                    available = clear + 2;
                    old_code = NullCode;
                    continue;
                }
                if (old_code == NullCode) {
                    pixelStack[top++] = suffix[code];
                    old_code = code;
                    first = code;
                    continue;
                }
                in_code = code;
                if (code == available) {
                    pixelStack[top++] = (byte) first;
                    code = old_code;
                }
                while (code > clear) {
                    pixelStack[top++] = suffix[code];
                    code = prefix[code];
                }
                first = ((int) suffix[code]) & 0xff;
                // Add a new string to the string table
                if (available >= MAX_STACK_SIZE) {
                    break;
                }
                pixelStack[top++] = (byte) first;
                prefix[available] = (short) old_code;
                suffix[available] = (byte) first;
                available++;
                if (((available & code_mask) == 0) && (available < MAX_STACK_SIZE)) {
                    code_size++;
                    code_mask += available;
                }
                old_code = in_code;
            }
            // Pop a pixel off the pixel stack
            top--;
            pixels[pi++] = pixelStack[top];
            i++;
        }
        for (i = pi; i < npix; i++) {
            // clear missing pixels
            pixels[i] = 0;
        }
    }


    private boolean err() {
        return status != STATUS_OK;
    }

    // to initia variable
    private void init() {
        LogUtil.loge("init");
        status = STATUS_OK;
        frameCount = 0;
        frames = new Vector<GifFrame>();
        gct = null;
        lct = null;
    }

    // to initia variable
    protected int read() {
        int curByte = 0;
        try {
            curByte = in.read();
        } catch (IOException _e) {
            _e.printStackTrace();
            status = STATUS_FORMAT_ERROR;
        }
        return curByte;
    }

    private int readBlock() {
        LogUtil.loge("run readBlock...");
        blockSize = read();
        int n = 0;
        if (blockSize > 0) {
            try {
                int count = 0;
                while (n < blockSize) {
                    count = in.read(block, n, blockSize - n);
                    if (count == -1) {
                        // 读到了末尾就返回
                        break;
                    }
                    n += count;
                }
            } catch (Exception _e) {
                _e.printStackTrace();
            }
            if (n < blockSize) {
                status = STATUS_FORMAT_ERROR;
            }
        }
        return n;
    }

    // Global Color Table
    private int[] readColorTable(int _ncolors) {
        LogUtil.loge("readColorTable");
        int nbytes = 3 * _ncolors;
        int[] tab = null;
        byte[] c = new byte[nbytes];
        int n = 0;
        try {
            n = in.read(c);
        } catch (IOException _e) {
            _e.printStackTrace();
        }
        LogUtil.loge("n = " + n);
        if (n < nbytes) {
            status = STATUS_FORMAT_ERROR;
        } else {
            // max size to avoid bounds checks
            tab = new int[256];
            int i = 0;
            int j = 0;
            while (i < _ncolors) {
                int r = ((int) c[j++]) & 0xff;
                int g = ((int) c[j++]) & 0xff;
                int b = ((int) c[j++]) & 0xff;
                tab[i++] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }
        return tab;
    }

    // Image Descriptor
    private void readContents() {
        LogUtil.loge("readContents run ...");
        // read GIF file content blocks
        boolean done = false;
        while (!(done || err())) {
            LogUtil.loge("解析数据");
            int code = read();
            LogUtil.loge("code = " + code);
            switch (code) {
                // image spearator
                case 0x2C:
                    LogUtil.loge("process 0x2C readImage");
                    readImage();
                    break;
                // extension
                case 0x21:
                    LogUtil.loge("process 0x21");
                    code = read();
                    switch (code) {
                        case 0xf9:
                            LogUtil.loge("process 0xf9 readGraphicControlExt");
                            // graphics control extension
                            readGraphicControlExt();
                            break;
                        case 0xff:
                            LogUtil.loge("process 0xff readBlock");
                            // application extension
                            readBlock();
                            String app = "";
                            for (int i = 0; i < 11; i++) {
                                app += (char) block[i];
                            }
                            if (app.equals("NETSCAPE2.0")) {
                                readNetscapeExt();
                            } else {
                                // don't care
                                skip();
                            }
                            break;
                        default:
                            // uniteresting extension
                            skip();
//                            break;
                    }
                    break;
                // terminator
                case 0x3b:
                    done = true;
                    break;
                // bad byte, but keep going and see what happens
                case 0x00:
                    break;
                default:
                    status = STATUS_FORMAT_ERROR;
//                    break;
            }
        }
    }

    private void readGraphicControlExt() {
        LogUtil.loge("readGraphicControlExt run ...");
        // block size
        read();
        // packed fields
        int packed = read();
        // disposal method
        dispose = (packed & 0x1c) >> 2;
        if (dispose == 0) {
            // elect to keep old image if discretionary
            dispose = 1;
        }
        transparency = (packed & 1) != 0;
        // delay in milliseconds
        delay = readShort() * 10;
        // transparent color index
        transIndex = read();
        // block terminator
        read();
    }

    // to get Stream - Head
    // 获取Gif文件的头信息
    private void readHeader() {
        LogUtil.loge("readHeader run ... ");
        String id = "";
        for (int i = 0; i < 6; i++) {
            id += (char) read();
        }
        LogUtil.loge("readHeader id = " + id);
        // 如果不是GIF文件，直接返回
        if (!id.toUpperCase().startsWith("GIF")) {
            status = STATUS_FORMAT_ERROR;
            LogUtil.loge("readHeader status-1 code = " + status);
            return;
        }
        LogUtil.loge("pre run readLSD ... ");
        readLSD();
        if (gctFlag && !err()) {
            gct = readColorTable(gctSize);
            bgColor = gct[bgIndex];
        }
    }

    private void readImage() {
        LogUtil.loge("readImage run ...");
        // offset of X
        ix = readShort();
        // (sub) image position & size
        iy = readShort();
        // with of bitmap
        iw = readShort();
        // height of bitmap
        ih = readShort();

        // Local Color Table Flag
        int packed = read();
        // 1 - local color table flag
        lctFlag = (packed & 0x80) != 0;
        // Interlace Flag,to array with interwoven if ENABLE,with order otherwise
        interlace = (packed & 0x40) != 0;
        // 3- sort flag
        // 4-5 - reserved
        // 6-8 - local color table size
        lctSize = 2 << (packed & 7);
        if (lctFlag) {
            // read table
            LogUtil.loge("readColorTable run ...");
            lct = readColorTable(lctSize);
            // make local table active
            act = lct;
        } else {
            // make global table active
            act = gct;
            if (bgIndex == transIndex) {
                bgColor = 0;
            }
        }
        int save = 0;
        if (transparency) {
            save = act[transIndex];
            // set transparent color if specified
            act[transIndex] = 0;
        }
        if (act == null) {
            // no color table defined
            status = STATUS_FORMAT_ERROR;
        }
        if (err()) {
            LogUtil.loge("readImage status-1 code = " + status);
            return;
        }
        LogUtil.loge("decodeImageData run ...");
        // decode pixel data
        decodeImageData();
        LogUtil.loge("skip run ...");
        // decode pixel data
        skip();
        if (err()) {
            LogUtil.loge("readImage status-2 code = " + status);
            return;
        }
        frameCount++;
        // create new image to receive frame data
        image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        LogUtil.loge("setPixels run ...");
        // createImage(width,height);
        // transfer pixel data to image
        setPixels();
        LogUtil.loge("add image to frame ...");
        // add image to frame
        frames.addElement(new GifFrame(delay, image));
        // list
        if (transparency) {
            act[transIndex] = save;
        }
        LogUtil.loge("resetFrame run ...");
        resetFrame();
    }

    // Logical Screen Descriptor
    private void readLSD() {
        LogUtil.loge("run readLSD ... ");
        // logical screen size
        width = readShort();
        height = readShort();
        // packed fields
        int packed = read();
        // 1: global color table flag
        // 2-4: color resolution
        // 5: gct sort flag
        gctFlag = (packed & 0x80) != 0;
        // 6-8: gct size
        gctSize = 2 << (packed & 7);
        // background color index
        bgIndex = read();
        // pixel aspect ratio
        pixelAspect = read();
    }

    private void readNetscapeExt() {
        do {
            readBlock();
            if (block[0] == 1) {
                // loop count stub-block
                int b1 = ((int) block[1]) & 0xff;
                int b2 = ((int) block[2]) & 0xff;
                loopCount = (b2 << 8) | b1;
            }
        } while ((blockSize > 0) && !err());
    }

    // read 8 bit data
    private int readShort() {
        // read 16 bit value, LSB first
        return read() | (read() << 8);
    }

    private void resetFrame() {
        lastDispose = dispose;
        lrx = ix;
        lry = iy;
        lrw = iw;
        lrh = ih;
        lastImage = image;
        lastBgColor = bgColor;
        dispose = 0;
        transparency = false;
        delay = 0;
        lct = null;
    }

    /**
     * Skips variable length blocks up to and including next zero length block
     */
    private void skip() {
        do {
            // 至少会执行一次
            readBlock();
        } while ((blockSize > 0) && !err());
    }
}
