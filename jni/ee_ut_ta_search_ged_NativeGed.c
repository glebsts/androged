/*
 * ee_ut_ta_search_ged_NativeGed.c
 *
 *  Created on: Dec 30, 2012
 *      Author: gleb
 */

#include "ee_ut_ta_search_ged_NativeGed.h"
#include <stdint.h>
#include <string.h>
#include <android/log.h>
#include "NativeGed.h"
#include "ARTrie.h"
#include "Trie.h"


#define SEARCHTERM_KEY "SearchTerm"
#define SEARCHOPTIONS_KEY "SearchOptions"
#define WORDS_KEY "Words"
#define TRANS_KEY "Transformations"
#define TAG "ged.jni.nativewrapper"
#define LOGD(x) __android_log_print(ANDROID_LOG_DEBUG, TAG, x)
#define LOGE(x) __android_log_print(ANDROID_LOG_ERROR, TAG, x)

static Store mStore = { { }, 0 };

/*Trie *t;
ARTrie *addT;
ARTrie *remT; */

/**
 * Initialization/Finalization.
 */
JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_initializeStore(
		JNIEnv* pEnv, jobject pThis) {
	mStore.mLength = 0;
	LOGD("Initialized store");
}

JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_finalizeStore(
		JNIEnv* pEnv, jobject pThis) {
	StoreEntry* lEntry = mStore.mEntries;
	StoreEntry* lEntryEnd = lEntry + mStore.mLength;

	// Releases every entry in the store.
	while (lEntry < lEntryEnd) {
		free(lEntry->mKey);
		releaseEntryValue(pEnv, lEntry);

		++lEntry;
	}
	mStore.mLength = 0;
	LOGD("Finalized store");
}

JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_setSearchTerm(
		JNIEnv* pEnv, jobject pThis, jstring pSearchTerm) {
	// Turns the Java string into a temporary C string.
	// GetStringUTFChars() is used here as an example but
	// Here, GetStringUTFChars() to show
	// the way it works. But as what we want is only a copy,
	// GetBooleanArrayRegion() would be be more efficient.

	const char* lStringTmp =
			(*pEnv)->GetStringUTFChars(pEnv, pSearchTerm, NULL);
	if (lStringTmp == NULL) {
		LOGD("NULL on getChars");
		return;
	}
	//LOGD(printf("Value %s received.", lStringTmp));
	StoreEntry* lEntry = allocateEntry(pEnv, &mStore, SEARCHTERM_KEY);
	if (lEntry != NULL) {
		lEntry->mType = StoreType_String;
		// Copy the temporary C string into its dynamically allocated
		// final location. Then releases the temporary string.
		// Malloc return value should theoretically be checked...
		jsize lStringLength = (*pEnv)->GetStringUTFLength(pEnv, pSearchTerm);
		lEntry->mValue.mString = (char*) malloc(
				sizeof(char) * (lStringLength + 1));
		strcpy(lEntry->mValue.mString, lStringTmp);
	}
	(*pEnv)->ReleaseStringUTFChars(pEnv, pSearchTerm, lStringTmp);
	LOGD("Set term");
}

JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_setSearchOptions(
		JNIEnv* pEnv, jobject pThis, jbooleanArray pOptions) {
	jboolean* lArrayTmp = (*pEnv)->GetBooleanArrayElements(pEnv, pOptions,
			NULL);
	if (lArrayTmp == NULL) {
		return;
	}
	// Finds/creates an entry in the store and fills its content.
	StoreEntry* lEntry = allocateEntry(pEnv, &mStore, SEARCHOPTIONS_KEY);
	if (lEntry != NULL) {
		lEntry->mType = StoreType_BooleanArray;
		// Allocates a new C buffer which is going to hold a copy of
		// the Java array.
		lEntry->mLength = (*pEnv)->GetArrayLength(pEnv, pOptions);
		size_t lBufferLength = lEntry->mLength * sizeof(uint8_t);
		// Malloc return value should theoretically be checked...
		lEntry->mValue.mBooleanArray = (uint8_t*) malloc(lBufferLength);
		memcpy(lEntry->mValue.mBooleanArray, lArrayTmp, lBufferLength);
	}
	// We have performed any modification on the array and thus do
	// not plan to send any modified data back to Java. So uses
	// JNI_ABORT flag for efficiency purpose.
	(*pEnv)->ReleaseBooleanArrayElements(pEnv, pOptions, lArrayTmp,
			JNI_ABORT);
	LOGD("Set search options");
}


JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_setDictionaryContent
  (JNIEnv* pEnv, jobject pThis, jobjectArray pWords){

	jsize lLength = (*pEnv)->GetArrayLength(pEnv, pWords);
	    char** lArray = (char**) malloc(lLength * sizeof(char*));
	    // Fills the C array with a copy of each input Java string.
	    int32_t i, j;
	    for (i = 0; i < lLength; ++i) {
	    	//if(i % 2000 == 0){

	    //	}
	        // Gets the current Java String from the input Java array.
	        // Object arrays can be accessed element by element only.
	        jstring lString = (*pEnv)->GetObjectArrayElement(pEnv, pWords, i);
	        if ((*pEnv)->ExceptionCheck(pEnv)) {
	            for (j = 0; j < i; ++j) {
	                free(lArray[j]);
	            }
	            free(lArray);
	            return;
	        }

	        jsize lStringLength = (*pEnv)->GetStringLength(pEnv, lString);
	        // Malloc return value should theoretically be checked...
	        lArray[i] = (char*) malloc(sizeof(char) * (lStringLength + 1));

	        (*pEnv)->GetStringUTFRegion(pEnv, lString, 0, lStringLength, lArray[i]);

	        if ((*pEnv)->ExceptionCheck(pEnv)) {
	        	char buff[100];
	        	sprintf(buff, "Exception check returns true for item %d", i);
	        	LOGE(buff);
	            for (j = 0; j < i; ++j) {
	                free(lArray[j]);
	            }
	            free(lArray);
	            return;
	        }

	        // No need to keep a reference to the Java string anymore.
	        (*pEnv)->DeleteLocalRef(pEnv, lString);
	    }
	    // Creates a new entry with the new String array.
	    StoreEntry* lEntry = allocateEntry(pEnv, &mStore, WORDS_KEY);
	    if (lEntry != NULL) {
	        lEntry->mType = StoreType_StringArray;
	        lEntry->mLength = lLength;
	        lEntry->mValue.mStringArray = lArray;
	    } else {
	        for (j = 0; j < lLength; ++j) {
	            free(lArray[j]);
	        }
	        free(lArray);
	        return;
	    }
	    LOGD("Set dictionary content");


}

JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_setTransformationContent
  (JNIEnv* pEnv, jobject pThis, jobjectArray pWords){

	jsize lLength = (*pEnv)->GetArrayLength(pEnv, pWords);
	    char** lArray = (char**) malloc(lLength * sizeof(char*));
	    // Fills the C array with a copy of each input Java string.
	    int32_t i, j;
	    for (i = 0; i < lLength; ++i) {
	    	//if(i % 2000 == 0){

	    //	}
	        // Gets the current Java String from the input Java array.
	        // Object arrays can be accessed element by element only.
	        jstring lString = (*pEnv)->GetObjectArrayElement(pEnv, pWords, i);
	        if ((*pEnv)->ExceptionCheck(pEnv)) {
	            for (j = 0; j < i; ++j) {
	                free(lArray[j]);
	            }
	            free(lArray);
	            return;
	        }

	        jsize lStringLength = (*pEnv)->GetStringLength(pEnv, lString);
	        // Malloc return value should theoretically be checked...
	        lArray[i] = (char*) malloc(sizeof(char) * (lStringLength + 1));

	        (*pEnv)->GetStringUTFRegion(pEnv, lString, 0, lStringLength, lArray[i]);

	        if ((*pEnv)->ExceptionCheck(pEnv)) {
	        	char buff[100];
	        	sprintf(buff, "Exception check returns true for item %d", i);
	        	LOGE(buff);
	            for (j = 0; j < i; ++j) {
	                free(lArray[j]);
	            }
	            free(lArray);
	            return;
	        }

	        // No need to keep a reference to the Java string anymore.
	        (*pEnv)->DeleteLocalRef(pEnv, lString);
	    }
	    // Creates a new entry with the new String array.
	    StoreEntry* lEntry = allocateEntry(pEnv, &mStore, TRANS_KEY);
	    if (lEntry != NULL) {
	        lEntry->mType = StoreType_StringArray;
	        lEntry->mLength = lLength;
	        lEntry->mValue.mStringArray = lArray;
	    } else {
	        for (j = 0; j < lLength; ++j) {
	            free(lArray[j]);
	        }
	        free(lArray);
	        return;
	    }
	    LOGD("Set trans content");



}


JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_createTrie
(JNIEnv* pEnv, jobject pThis){

	doAll();

/*
	t = createTrie();
    addT = createARTrie();
    remT = createARTrie();

    StoreEntry* lEntry = findEntry(pEnv, &mStore, TRANS_KEY, NULL);
    if (isEntryValid(pEnv, lEntry, StoreType_StringArray)) {
    	char buff[100];
    	sprintf(buff, "Entry valid, len: %d", lEntry->mLength);
    	LOGD(buff);

    	char *data;
    	data = (char *)readFile("/mnt/sdcard/ged/en-et-merli-markko-lt.txt");
    	sprintf(buff, "tf file len: %d", strlen(data));
    	LOGD(buff);
    	trieFromFile(data);
    	LOGD("Trie created");

    	data = (char *)readFile("/mnt/sdcard/ged/en_et_03_01_2007_EN_utf8.vp");
    	    	sprintf(buff, "wrd file len: %d", strlen(data));
    	    	LOGD(buff);


    	if (t != NULL){
    	     freeTrie(t);
    	     LOGD("Trie free");
    	  }

    }else {
    	LOGE("Entry is invalid!!");
    }
    LOGD("exit createTrie");
*/
}


