LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := ged
LOCAL_SRC_FILES := ee_ut_ta_search_ged_NativeGed.c NativeGed.c GenEditDist.c ARTrie.c Trie.c List.c FindEditDistanceMod.c FileToTrie.c
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)