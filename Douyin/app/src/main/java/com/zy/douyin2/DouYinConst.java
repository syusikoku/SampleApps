package com.zy.douyin2;

import android.hardware.Camera;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DouYinConst {
    /**
     * 要播放的视频文件的路径
     */
    public static final String DATA_VIDEO_PATH = "video_path";
    public static final String MIME_VIDEO = "video/";
    public static final String AUDIO_AUDIO = "audio/";
    private static File _sStorageDirectory;

    static {
        _sStorageDirectory = Environment.getExternalStorageDirectory();
    }

    /**
     * 视频文件名称
     */
    public static String VIDEO_FILE_NAME = _sStorageDirectory + "/Video_" + new SimpleDateFormat(
            "yyyyMMddHHmmssss").format(new Date()) + ".mp4";

    /**
     * 视频文件路径
     */
    public static String MP4_PATH = new File(VIDEO_FILE_NAME).getAbsolutePath();

    /**
     * 人脸定位的模型
     */
    public static String FACE_DECT_MODEL = "lbpcascade_frontalface.xml";

    /**
     * 五官检测的模型
     */
    public static String SEETA_FA_MODEL = "seeta_fa_v1.1.bin";

    /**
     * 人脸定位的模型-路径
     */
    public static String FACE_DECT_MODEL_PATH = new File(_sStorageDirectory, FACE_DECT_MODEL).getAbsolutePath();

    /**
     * 五官检测的模型-路径
     */
    public static String SEETA_FA_MODEL_PATH = new File(_sStorageDirectory, SEETA_FA_MODEL).getAbsolutePath();

    /**
     * 摄像头id
     */
    public static int MAIN_CAM_ID = Camera.CameraInfo.CAMERA_FACING_FRONT;


}
