//
// Created by zzg on 2019/12/20.
//

#ifndef CSDNJNITHREAD_ANDROIDLOG_H
#define CSDNJNITHREAD_ANDROIDLOG_H


#endif //CSDNJNITHREAD_ANDROIDLOG_H

#include <android/log.h>

extern "C" {
#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"DouYin",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"DouYin",FORMAT,##__VA_ARGS__);
};

