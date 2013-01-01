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
#define RESULT_KEY "Results"
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

	doAll(&mStore);

	jobjectArray lJavaArray;
	char buff[100];
	if (resultArray != NULL) {

		int ci1 = 0;
		for (ci1 = 0; ci1 < num_elements; ci1++) {
			sprintf(buff, "%d: %s %1.2f", ci1, resultArray[ci1].result,
					resultArray[ci1].distance);
			LOGD(buff);
		}

		///////------------------------------------------------------

		jclass lStringClass = (*pEnv)->FindClass(pEnv, "java/lang/String");
		if (lStringClass == NULL) {
			return NULL;
		}
		LOGD("lStringClass found");
		lJavaArray = (*pEnv)->NewObjectArray(pEnv, num_elements, lStringClass,
				NULL);
		(*pEnv)->DeleteLocalRef(pEnv, lStringClass);
		if (lJavaArray == NULL) {
			return NULL;
		}
		LOGD("lJavaArray not NULL");
		// Creates a new Java String object for each C string stored.
		// Reference to the String can be removed right after it is
		// added to the Java array, as the latter holds a reference
		// to the String object.
		int32_t i;
		for (i = 0; i < num_elements; ++i) {

			char buff2[100];

			sprintf(buff2, "%d|%s|%1.2f|%d", i, resultArray[i].result,
					resultArray[i].distance, resultArray[i].type);
			LOGD(buff2);
			jstring lString = (*pEnv)->NewStringUTF(pEnv, buff2);
			if (lString == NULL) {
				LOGD("lString is null");
				return NULL;
			}
			LOGD("before add to arr");
			// Puts the new string in the array. Exception are
			// checked because of SetObjectArrayElement() (can raise
			// an ArrayIndexOutOfBounds or ArrayStore Exception).
			// If one occurs, any object created here will be freed
			// as they are all referenced locally only.
			(*pEnv)->SetObjectArrayElement(pEnv, lJavaArray, i, lString);
			LOGD("SetObjectArrayElement");
			// Note that DeleteLocalRef() can still be called safely
			// even if an exception is raised.

			(*pEnv)->DeleteLocalRef(pEnv, lString);
			if ((*pEnv)->ExceptionCheck(pEnv)) {
				LOGD("EXC!");
				return NULL;
			}
			LOGD("end of cycle");
		}

		///////------------------------------------------------------


		for (ci1 = 0; ci1 < num_elements; ci1++) {
			free(resultArray[ci1].result);
		}
		free(resultArray);
		LOGD("Array free!");
		return lJavaArray;

	} else {
		LOGD("Result is NULL!");
		return NULL;

	}

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
