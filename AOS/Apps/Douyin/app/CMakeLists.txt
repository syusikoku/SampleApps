cmake_minimum_required(VERSION 3.4.1)

add_library(
        native-lib

        SHARED

        src/main/cpp/native-lib.cpp
        src/main/cpp/FaceTrack.cpp
)

find_library(
        log-lib

        log)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}")

#添加子工程
add_subdirectory(src/main/cpp/FaceAlignment)

#导入opencv库文件
include_directories(src/main/cpp/include)

include_directories(src/main/cpp/FaceAlignment/include)


#动态库链接
target_link_libraries( # Specifies the target library.
        native-lib

        # included in the NDK.
        ${log-lib}
        opencv_java3 #opencv
        seeta_fa_lib #seeta五官定位
        android
        )