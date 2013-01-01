/*
 * NativeGed.h
 *
 *  Created on: Dec 30, 2012
 *      Author: gleb
 */

#ifndef NATIVEGED_H_
#define NATIVEGED_H_

#include "jni.h"
#include <stdint.h>
#include <android/log.h>

#define STORE_MAX_CAPACITY 10
#define TAG "ged.jni.nativeged"
#define LOGD(x) __android_log_print(ANDROID_LOG_DEBUG, TAG, x)


typedef enum {
	StoreType_Double, // for max edit distance
	StoreType_Integer, // for result count
	StoreType_String, // for search term

	StoreType_BooleanArray, // for flags


	StoreType_StringArray,
// for dict
//StoreType_Color,  // for custom classes etc
    StoreType_ResultArray
} StoreType;

typedef union {
	uint8_t mBoolean;

	double mDouble;

	int32_t mInteger;
	int64_t mLong;
	char* mString;
//  jobject  mColor;

	uint8_t* mBooleanArray;
	char** mStringArray;
	jobject*  mResultArray;
} StoreValue;

typedef struct {
	char* mKey;
	StoreType mType;
	StoreValue mValue;
	int32_t mLength; // Used only for arrays.
} StoreEntry;

typedef struct {
	StoreEntry mEntries[STORE_MAX_CAPACITY];
	int32_t mLength;
} Store;

int32_t isEntryValid(JNIEnv* pEnv, StoreEntry* pEntry, StoreType pType);

StoreEntry* allocateEntry(JNIEnv* pEnv, Store* pStore, char* pKey);

StoreEntry* findEntry(JNIEnv* pEnv, Store* pStore, char* pKey,
		int32_t* pError);

void releaseEntryValue(JNIEnv* pEnv, StoreEntry* pEntry);

void throwInvalidTypeException(JNIEnv* pEnv);

void throwNotExistingKeyException(JNIEnv* pEnv);

void throwStoreFullException(JNIEnv* pEnv);

#endif /* NATIVEGED_H_ */
