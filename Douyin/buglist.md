# bug-1

```
04-20 18:08:16.925 15677-15704/com.zy.douyin2 E/DouYin: DoyYinRender onSurfaceChanged
04-20 18:08:17.130 15677-15704/com.zy.douyin2 E/DouYin: native_lib run create ...
04-20 18:08:17.130 15677-15704/com.zy.douyin2 E/DouYin: native_lib build FaceTrack ...
04-20 18:08:17.130 15677-15704/com.zy.douyin2 E/DouYin: FaceTrack run init... 
04-20 18:08:17.131 15677-15704/com.zy.douyin2 E/DouYin: 人脸识别定位的级联分类器 init... 
04-20 18:08:17.138 4259-4647/? E/WifiConfigStore: updateConfiguration freq=5765 BSSID=7c:a1:77:e4:de:2c RSSI=-39 "HwSuv"WPA_PSK
04-20 18:08:17.239 15677-15704/com.zy.douyin2 E/DouYin: 创建一个定位人脸使用的适配器 init... 
04-20 18:08:17.239 15677-15704/com.zy.douyin2 E/DouYin: 人脸追踪的级联分类器 init... 
04-20 18:08:17.240 15677-15704/com.zy.douyin2 E/cv::error(): OpenCV(3.4.3) Error: Unknown error code -49 (Input file is empty) in CvFileStorage* cvOpenFileStorage(const char*, CvMemStorage*, int, const char*), file /build/3_4_pack-android/opencv/modules/core/src/persistence_c.cpp, line 388
04-20 18:08:17.361 15339-15591/? E/linker: readlink('/proc/self/fd/157') failed: Permission denied [fd=157]
04-20 18:08:17.361 15339-15591/? E/linker: warning: unable to get realpath for the library "/data/data/com.tencent.mm/app_tbs/core_share/tbs_jsapi_plugin.dex". Will use given name.
04-20 18:08:17.365 15677-15704/com.zy.douyin2 A/libc: Fatal signal 6 (SIGABRT), code -6 in tid 15704 (GLThread 10747)
04-20 18:08:17.434 3706-3706/? A/DEBUG: *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
04-20 18:08:17.435 3706-3706/? A/DEBUG: Build fingerprint: 'Huawei/PE-TL10/hwPE:6.0/HuaweiPE-TL10/C00B959:user/release-keys'
04-20 18:08:17.435 3706-3706/? A/DEBUG: Revision: '0'
04-20 18:08:17.435 3706-3706/? A/DEBUG: ABI: 'arm'
04-20 18:08:17.435 3706-3706/? A/DEBUG: pid: 15677, tid: 15704, name: GLThread 10747  >>> com.zy.douyin2 <<<
04-20 18:08:17.435 3706-3706/? A/DEBUG: signal 6 (SIGABRT), code -6 (SI_TKILL), fault addr --------
04-20 18:08:17.465 3706-3706/? A/DEBUG:     r0 00000000  r1 00003d58  r2 00000006  r3 b2f11978
04-20 18:08:17.465 3706-3706/? A/DEBUG:     r4 b2f11980  r5 b2f11930  r6 00000000  r7 0000010c
04-20 18:08:17.465 3706-3706/? A/DEBUG:     r8 00000047  r9 00000001  sl 00000000  fp b80d7294
04-20 18:08:17.465 3706-3706/? A/DEBUG:     ip 00000006  sp b2f10bd0  lr b6c91e49  pc b6c9371c  cpsr 400f0010
04-20 18:08:17.591 3706-3706/? A/DEBUG: backtrace:
04-20 18:08:17.591 3706-3706/? A/DEBUG:     #00 pc 0004371c  /system/lib/libc.so (tgkill+12)
04-20 18:08:17.591 3706-3706/? A/DEBUG:     #01 pc 00041e45  /system/lib/libc.so (pthread_kill+32)
04-20 18:08:17.592 3706-3706/? A/DEBUG:     #02 pc 0001bb53  /system/lib/libc.so (raise+10)
04-20 18:08:17.592 3706-3706/? A/DEBUG:     #03 pc 00018db1  /system/lib/libc.so (__libc_android_abort+34)
04-20 18:08:17.592 3706-3706/? A/DEBUG:     #04 pc 0001696c  /system/lib/libc.so (abort+4)
04-20 18:08:17.592 3706-3706/? A/DEBUG:     #05 pc 00853ffc  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (_ZN9__gnu_cxx27__verbose_terminate_handlerEv+344)
04-20 18:08:17.592 3706-3706/? A/DEBUG:     #06 pc 0082a4cc  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (_ZN10__cxxabiv111__terminateEPFvvE+4)
04-20 18:08:17.592 3706-3706/? A/DEBUG:     #07 pc 0082a50c  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (_ZSt9terminatev+16)
04-20 18:08:17.592 3706-3706/? A/DEBUG:     #08 pc 00829ee8  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (__cxa_throw+168)
04-20 18:08:17.592 3706-3706/? A/DEBUG:     #09 pc 001d603f  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (_ZN2cv5errorERKNS_9ExceptionE+238)
04-20 18:08:17.592 3706-3706/? A/DEBUG:     #10 pc 001d67a9  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (_ZN2cv5errorEiRKNS_6StringEPKcS4_i+96)
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #11 pc 00194aaf  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #12 pc 00197381  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (cvOpenFileStorage+1996)
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #13 pc 001bd4cb  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (_ZN2cv11FileStorage4openERKNS_6StringEiS3_+58)
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #14 pc 001bd655  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (_ZN2cv11FileStorageC1ERKNS_6StringEiS3_+32)
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #15 pc 0042ca99  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #16 pc 00423d6d  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (_ZN2cv17CascadeClassifier4loadERKNS_6StringE+144)
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #17 pc 00423de3  /data/app/com.zy.douyin2-1/lib/arm/libopencv_java3.so (_ZN2cv17CascadeClassifierC1ERKNS_6StringE+10)
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #18 pc 00011f55  /data/app/com.zy.douyin2-1/lib/arm/libnative-lib.so (_ZN2cv7makePtrINS_17CascadeClassifierEPKcEENS_3PtrIT_EERKT0_+64)
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #19 pc 00011cb5  /data/app/com.zy.douyin2-1/lib/arm/libnative-lib.so (_ZN9FaceTrackC1EPKcS1_+212)
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #20 pc 0000f9e1  /data/app/com.zy.douyin2-1/lib/arm/libnative-lib.so (Java_com_zy_douyin2_face_FaceTracker_native_1create+148)
04-20 18:08:17.593 3706-3706/? A/DEBUG:     #21 pc 005650e1  /data/app/com.zy.douyin2-1/oat/arm/base.odex (offset 0x340000) (long com.zy.douyin2.face.FaceTracker.native_create(java.lang.String, java.lang.String)+116)
04-20 18:08:17.594 3706-3706/? A/DEBUG:     #22 pc 00564cbd  /data/app/com.zy.douyin2-1/oat/arm/base.odex (offset 0x340000) (void com.zy.douyin2.face.FaceTracker.<init>(java.lang.String, java.lang.String, com.zy.douyin2.helper.CameraHelper)+392)
04-20 18:08:17.594 3706-3706/? A/DEBUG:     #23 pc 0056df57  /data/app/com.zy.douyin2-1/oat/arm/base.odex (offset 0x340000) (void com.zy.douyin2.render.DoyYinRender.onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)+282)
04-20 18:08:17.594 3706-3706/? A/DEBUG:     #24 pc 743496f5  /data/dalvik-cache/arm/system@framework@boot.oat (offset 0x23e5000)
04-20 18:08:17.861 24656-25248/? E/ActivityThread: Failed to find provider info for com.android.badge
```



解决:
      模型传错导致

```
// 智能指针: 人脸追踪的级联分类器
LOGE("人脸追踪的级联分类器 init... ");
Ptr<CascadeClassifier> classifier1 = makePtr<CascadeClassifier>(faceModel);
// 创建一个跟踪人脸使用的适配器
LOGE("创建一个跟踪人脸使用的适配器 init... ");
Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(classifier1);
```



# bug-2

```
04-20 18:17:42.215 21714-21765/com.zy.douyin2 E/DouYin: DoyYinRender onDrawFrame
04-20 18:17:43.224 21714-21714/com.zy.douyin2 E/DouYin: DoyYinRender onFrameAvailable
04-20 18:17:43.224 21714-21714/com.zy.douyin2 E/DouYin: DoyYinRender mView requestRender
04-20 18:17:43.291 21714-21726/com.zy.douyin2 E/Camera-JNI: Callback buffer was too small! Expected 1382400 bytes, but got 460800 bytes!
04-20 18:17:43.291 21714-21726/com.zy.douyin2 E/Camera-JNI: Couldn't allocate byte array for JPEG data
04-20 18:17:43.292 21714-21714/com.zy.douyin2 E/DouYin: onPreviewFrame ... 
04-20 18:17:43.292 21714-21714/com.zy.douyin2 E/Camera-JNI: Null byte array!
04-20 18:17:43.293 21714-21818/com.zy.douyin2 E/DouYin: native_lib run detector ...
04-20 18:17:43.295 21714-21726/com.zy.douyin2 E/Camera-JNI: Callback buffer was too small! Expected 1382400 bytes, but got 460800 bytes!
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410] JNI DETECTED ERROR IN APPLICATION: jarray was NULL
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]     in call to GetByteArrayElements
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]     from com.zy.douyin2.face.Face com.zy.douyin2.face.FaceTracker.native_detector(long, byte[], int, int, int)
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410] "face_track" prio=5 tid=14 Runnable
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   | group="main" sCount=0 dsCount=0 obj=0x12e5f6a0 self=0xb826a048
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   | sysTid=21818 nice=0 cgrp=top_visible sched=0/0 handle=0x9ad2f930
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   | state=R schedstat=( 3263312 689936 8 ) utm=0 stm=0 core=0 HZ=100
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   | stack=0x9ac2d000-0x9ac2f000 stackSize=1038KB
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   | held mutexes= "mutator lock"(shared held)
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   at com.zy.douyin2.face.FaceTracker.native_detector(Native method)
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   at com.zy.douyin2.face.FaceTracker$1.handleMessage(FaceTracker.java:41)
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   - locked <0x052afab7> (a com.zy.douyin2.face.FaceTracker)
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   at android.os.Handler.dispatchMessage(Handler.java:102)
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   at android.os.Looper.loop(Looper.java:150)
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410]   at android.os.HandlerThread.run(HandlerThread.java:61)
04-20 18:17:43.329 21714-21818/com.zy.douyin2 A/art: art/runtime/java_vm_ext.cc:410] 
04-20 18:17:43.330 21714-21818/com.zy.douyin2 A/art: art/runtime/runtime.cc:368] Runtime aborting...
04-20 18:17:43.330 21714-21818/com.zy.douyin2 A/art: art/runtime/runtime.cc:368]
```



问题点:

```
  // data 数据为空导致的
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        LogUtil.loge("onPreviewFrame ... ");
        // data数据是颠倒的
        if (null != mPreviewCallback) {
            mPreviewCallback.onPreviewFrame(data, camera);
        }
        mCamera.addCallbackBuffer(data);
    } 
```


解决办法

```
    // buffer = new byte[WIDTH * HEIGHT * 3 / 2];
    buffer = new byte[reqW * reqH * 3 / 2];
```

​     

------



# bug-3

```text
  Process: com.zy.douyin2, PID: 7698
    java.lang.IllegalStateException: 片元着色器编译失败: 0:102: S0001: No matching overload for function 'min' found    
at com.zy.douyin2.utils.OpenGLUtils.loadProgram(OpenGLUtils.java:99)
    at com.zy.douyin2.filter.AbsFilter.initilize(AbsFilter.java:100)
    at com.zy.douyin2.filter.AbsFilter.<init>(AbsFilter.java:47)
    at com.zy.douyin2.filter.AbsFrameFilter.<init>(AbsFrameFilter.java:20)
    at com.zy.douyin2.filter.BeautyFilter.<init>(BeautyFilter.java:17)
    at com.zy.douyin2.render.DoyYinRender$1.run(DoyYinRender.java:230)
    at android.opengl.GLSurfaceView$GLThread.guardedRun(GLSurfaceView.java:1472)
    at android.opengl.GLSurfaceView$GLThread.run(GLSurfaceView.java:1249)    
```