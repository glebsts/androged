LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := ged
LOCAL_SRC_FILES := ged.c
LOCAL_LDLIBS := -llog


include $(BUILD_SHARED_LIBRARY)