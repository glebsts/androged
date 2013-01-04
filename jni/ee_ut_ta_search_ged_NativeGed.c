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
#include "GenEditDist.h"

#define SEARCHTERM_KEY "SearchTerm"
#define SEARCHOPTIONS_KEY "SearchOptions"
#define WORDS_KEY "Words"
#define TRANS_KEY "Transformations"
#define LETTERS_KEY "Letters"
#define RESULT_KEY "Results"
#define DISTANCE_KEY "MaxDistance"
#define BEST_KEY "Best"

#define SEARCHTERM_IDX 1
#define WORDS_IDX 2
#define TRANS_IDX 3
#define LETTERS_IDX 4

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

	const char* lStringTmp =
			(*pEnv)->GetStringUTFChars(pEnv, pSearchTerm, NULL);
	if (lStringTmp == NULL) {
		LOGD("NULL on getChars");
		return;
	}

	StoreEntry* lEntry = allocateEntry(pEnv, &mStore, SEARCHTERM_KEY);
	if (lEntry != NULL) {
		lEntry->mType = StoreType_String;

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
	jboolean* lArrayTmp =
			(*pEnv)->GetBooleanArrayElements(pEnv, pOptions, NULL);
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
	(*pEnv)->ReleaseBooleanArrayElements(pEnv, pOptions, lArrayTmp, JNI_ABORT);
	LOGD("Set search options");
}

JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_setDictionaryContent(
		JNIEnv* pEnv, jobject pThis, jobjectArray pWords) {

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

JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_setTransformationContent(
		JNIEnv* pEnv, jobject pThis, jobjectArray pWords) {

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

extern SRES *resultArray;
extern int num_elements;
extern int num_allocated;

JNIEXPORT jobjectArray JNICALL Java_ee_ut_ta_search_ged_NativeGed_process(
		JNIEnv* pEnv, jobject pThis) {

	doAll(pEnv, &mStore);

	jobjectArray lJavaArray;
	char buff[100];
	if (resultArray != NULL && num_elements>0) {

	/*	int ci1 = 0;
		for (ci1 = 0; ci1 < num_elements; ci1++) {
			sprintf(buff, "%d: %s %1.2f %d", ci1, resultArray[ci1].result,
					resultArray[ci1].distance, resultArray[ci1].type);
			LOGD(buff);
		}*/

		///////------------------------------------------------------

		jclass lStringClass = (*pEnv)->FindClass(pEnv, "java/lang/String");
		if (lStringClass == NULL) {
			return NULL;
		}
//		LOGD("lStringClass found");
		lJavaArray = (*pEnv)->NewObjectArray(pEnv, num_elements, lStringClass,
				NULL);
		(*pEnv)->DeleteLocalRef(pEnv, lStringClass);
		if (lJavaArray == NULL) {
			return NULL;
		}
	//	LOGD("lJavaArray not NULL");
		// Creates a new Java String object for each C string stored.
		// Reference to the String can be removed right after it is
		// added to the Java array, as the latter holds a reference
		// to the String object.

		int32_t i;
		for (i = 0; i < num_elements; ++i) {

			char buff2[100];

			sprintf(buff2, "%d|%s|%f|%d", i, resultArray[i].result,
					resultArray[i].distance, resultArray[i].type);
			LOGD(buff2);
			jstring lString = (*pEnv)->NewStringUTF(pEnv, buff2);
			if (lString == NULL) {
				LOGD("lString is null");
				return NULL;
			}
		//	LOGD("before add to arr");
			// Puts the new string in the array. Exception are
			// checked because of SetObjectArrayElement() (can raise
			// an ArrayIndexOutOfBounds or ArrayStore Exception).
			// If one occurs, any object created here will be freed
			// as they are all referenced locally only.
			(*pEnv)->SetObjectArrayElement(pEnv, lJavaArray, i, lString);
			//LOGD("SetObjectArrayElement");
			// Note that DeleteLocalRef() can still be called safely
			// even if an exception is raised.

			(*pEnv)->DeleteLocalRef(pEnv, lString);
			if ((*pEnv)->ExceptionCheck(pEnv)) {
				LOGD("EXC!");
				return NULL;
			}
			//LOGD("end of cycle");
		}

		///////------------------------------------------------------
//sprintf(buff, "n_e: %d", num_elements);
//LOGD(buff);
		for (i = 0; i < num_elements; i++) {
			free(resultArray[i].result);
		}
//LOGD("result fields free");
		free(resultArray);
		num_allocated = 0;
		num_elements = 0;
		LOGD("Array free!");
		return lJavaArray;

	} else {
		LOGD("Result is NULL!");
		return NULL;

	}

}

JNIEXPORT jobjectArray JNICALL Java_com_packtpub_Store_getStringArray(
		JNIEnv* pEnv, jobject pThis) {
	StoreEntry* lEntry = findEntry(pEnv, &mStore, RESULT_KEY, NULL);
	if (isEntryValid(pEnv, lEntry, StoreType_StringArray)) {
		// An array of String in Java is in fact an array of object.
		jclass lStringClass = (*pEnv)->FindClass(pEnv, "java/lang/String");
		if (lStringClass == NULL) {
			return NULL;
		}
		jobjectArray lJavaArray = (*pEnv)->NewObjectArray(pEnv,
				lEntry->mLength, lStringClass, NULL);
		(*pEnv)->DeleteLocalRef(pEnv, lStringClass);
		if (lJavaArray == NULL) {
			return NULL;
		}

		// Creates a new Java String object for each C string stored.
		// Reference to the String can be removed right after it is
		// added to the Java array, as the latter holds a reference
		// to the String object.
		int32_t i;
		for (i = 0; i < lEntry->mLength; ++i) {
			jstring lString = (*pEnv)->NewStringUTF(pEnv,
					lEntry->mValue.mStringArray[i]);
			if (lString == NULL) {
				return NULL;
			}

			// Puts the new string in the array. Exception are
			// checked because of SetObjectArrayElement() (can raise
			// an ArrayIndexOutOfBounds or ArrayStore Exception).
			// If one occurs, any object created here will be freed
			// as they are all referenced locally only.
			(*pEnv)->SetObjectArrayElement(pEnv, lJavaArray, i, lString);
			// Note that DeleteLocalRef() can still be called safely
			// even if an exception is raised.
			(*pEnv)->DeleteLocalRef(pEnv, lString);
			if ((*pEnv)->ExceptionCheck(pEnv)) {
				return NULL;
			}
		}
		return lJavaArray;
	} else {
		return NULL;
	}
}

JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_setGedData(
		JNIEnv* pEnv, jobject pThis, jstring pString, jint type) {


	const char* lStringTmp = (*pEnv)->GetStringUTFChars(pEnv, pString, NULL);
	if (lStringTmp == NULL) {
		return;
	}

	char buff[100];
	//sprintf(buff, "String: %s, type: %d", lStringTmp, type);
	//LOGD(buff);

	char* pKey = "";
	switch (type) {
	case SEARCHTERM_IDX:
		pKey = SEARCHTERM_KEY;
		break;
	case WORDS_IDX:
		pKey = WORDS_KEY;
		break;

	case TRANS_IDX:
		pKey = TRANS_KEY;
		break;

	case LETTERS_IDX:
		pKey = LETTERS_KEY;
		break;
	default:
		break;
	}
	StoreEntry* lEntry = allocateEntry(pEnv, &mStore, pKey);
	if (lEntry != NULL) {
		lEntry->mType = StoreType_String;

		jsize lStringLength = (*pEnv)->GetStringUTFLength(pEnv, pString);
		lEntry->mValue.mString = (char*) malloc(
				sizeof(char) * (lStringLength + 1));
		strcpy(lEntry->mValue.mString, lStringTmp);
	}
	(*pEnv)->ReleaseStringUTFChars(pEnv, pString, lStringTmp);
}

JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_setMaxEditDist
(JNIEnv* pEnv, jobject pThis, jdouble pDistance){
	 StoreEntry* lEntry = allocateEntry(pEnv, &mStore, DISTANCE_KEY);
	    if (lEntry != NULL) {
	        lEntry->mType = StoreType_Double;
	        lEntry->mValue.mDouble = pDistance;
	    }
}

JNIEXPORT void JNICALL Java_ee_ut_ta_search_ged_NativeGed_setBest
  (JNIEnv* pEnv, jobject pThis, jint pInteger) {
    StoreEntry* lEntry = allocateEntry(pEnv, &mStore, BEST_KEY);
    if (lEntry != NULL) {
        lEntry->mType = StoreType_Integer;
        lEntry->mValue.mInteger = pInteger;
    }
}

