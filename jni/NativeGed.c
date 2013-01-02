/*
 * NativeGed.c
 *
 *  Created on: Dec 30, 2012
 *      Author: gleb
 */

#include "NativeGed.h"
#include <string.h>
#include <stdio.h>

int32_t isEntryValid(JNIEnv* pEnv, StoreEntry* pEntry,
    StoreType pType) {
    if (pEntry == NULL) {
        throwNotExistingKeyException(pEnv);
    } else if (pEntry->mType != pType) {
        throwInvalidTypeException(pEnv);
    } else {
        return 1;
    }
    // We arrive here if an exception is raised (raising an
    // exception does not stop the flow of code like in Java).
    return 0;
}

StoreEntry* findEntry(JNIEnv* pEnv, Store* pStore, char* pKey,
                      int32_t* pError) {
	char buff[100];
	sprintf(buff, "find: %s", pKey);
	LOGD(buff);
    StoreEntry* lEntry = pStore->mEntries;
    StoreEntry* lEntryEnd = lEntry + pStore->mLength;

    while ((lEntry < lEntryEnd)
        && (strcmp(lEntry->mKey, pKey) != 0)) {
        ++lEntry;
    }
if(lEntry == lEntryEnd){
	LOGD("find: null");

}else {
	LOGD("Found");
}
    return (lEntry == lEntryEnd) ? NULL : lEntry;
}


StoreEntry* allocateEntry(JNIEnv* pEnv, Store* pStore, char* pKey){
char buff[100];
sprintf(buff, "Value stored for %s ", pKey);

    int32_t lError = 0;
    StoreEntry* lEntry = findEntry(pEnv, pStore, pKey, &lError);
    if (lEntry != NULL) {
        releaseEntryValue(pEnv, lEntry);
    }
    // If entry does not exist, create a new entry right after
    // already allocated entries.
    else if (!lError) {
        // Checks store can accept a new entry.
        if (pStore->mLength >= STORE_MAX_CAPACITY) {
            throwStoreFullException(pEnv);
            return NULL;
        }
        lEntry = pStore->mEntries + pStore->mLength;

        lEntry->mKey = (char*) malloc(strlen(pKey));
           strcpy(lEntry->mKey, pKey);
           LOGD("Allocator: after key malloc");

        ++pStore->mLength;
        LOGD(buff);

    }
    return lEntry;
}

void releaseEntryValue(JNIEnv* pEnv, StoreEntry* pEntry) {
    int32_t i;
    switch (pEntry->mType) {
        case StoreType_String:
            free(pEntry->mValue.mString);
            break;
  /*      case StoreType_Color:
            // Unreferences the Id object for garbage collection.
            (*pEnv)->DeleteGlobalRef(pEnv, pEntry->mValue.mColor);
            break; */
        case StoreType_BooleanArray:
            free(pEntry->mValue.mBooleanArray);
            break;
        case StoreType_StringArray:
            // Destroys every C string pointed by the String array
            // before releasing it.
            for (i = 0; i < pEntry->mLength; ++i) {
                free(pEntry->mValue.mStringArray[i]);
            }
            free(pEntry->mValue.mStringArray);
            break;
        case StoreType_ResultArray:
            // Unreferences every Id before releasing the Id array.
            for (i = 0; i < pEntry->mLength; ++i) {
                (*pEnv)->DeleteGlobalRef(pEnv, pEntry->mValue.mResultArray[i]);
            }
            free(pEntry->mValue.mResultArray);
            break;
    }
}

void throwNotExistingKeyException(JNIEnv* pEnv) {
    jclass lClass = (*pEnv)->FindClass(pEnv,
        "ee/ut/ta/search/ged/exceptions/NotExistingKeyException");
    if (lClass != NULL) {
        (*pEnv)->ThrowNew(pEnv, lClass, "Key does not exist.");
    }
    (*pEnv)->DeleteLocalRef(pEnv, lClass);
}

void throwInvalidTypeException(JNIEnv* pEnv) {
    jclass lClass = (*pEnv)->FindClass(pEnv,
        "ee/ut/ta/search/ged/exception/InvalidTypeException");
    if (lClass != NULL) {
        (*pEnv)->ThrowNew(pEnv, lClass, "Type is invalid.");
    }
    (*pEnv)->DeleteLocalRef(pEnv, lClass);
}

void throwStoreFullException(JNIEnv* pEnv) {
    jclass lClass = (*pEnv)->FindClass(pEnv,
        "ee/ut/ta/search/ged/exception/StoreFullException");
    if (lClass != NULL) {
        (*pEnv)->ThrowNew(pEnv, lClass, "Store is full.");
    }
    (*pEnv)->DeleteLocalRef(pEnv, lClass);
}
