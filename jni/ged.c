#define TAG "ged.jni" // the tag to be shown in logcat
#include "ee_ut_ta_search_SearchProcessor.h"
#include <android/log.h>
#include <string.h>
#include <stdint.h>

JNIEXPORT void JNICALL Java_ee_ut_ta_search_SearchProcessor_jniStoreWords
(JNIEnv *pEnv, jclass pObj, jobjectArray arg) {

	jsize lLength = (*pEnv)->GetArrayLength(pEnv, arg);
	char buffer[100];
	sprintf(buffer, "Test %s %d", "blah", lLength);

	__android_log_print(ANDROID_LOG_DEBUG, TAG, buffer);

	int32_t i, j;
	for (i = 0; i < 300; ++i) {
		jstring lStr = (*pEnv)->GetObjectArrayElement(pEnv, arg, i);
		const char* lKeyTmp = (*pEnv)->GetStringUTFChars(pEnv, lStr, NULL);
		if (lKeyTmp != NULL) {
			char buffer[100];
			sprintf(buffer, "Test %d: %s", i, lKeyTmp);
			__android_log_print(ANDROID_LOG_DEBUG, TAG, buffer);
		}
		(*pEnv)->ReleaseStringUTFChars(pEnv, lStr, lKeyTmp);
		//(*pEnv)->DeleteLocalRef(lKeyTmp);
	}


/*	const char* lKeyTmp = (*pEnv)->GetStringUTFChars(pEnv, arg[0], NULL);
 if (lKeyTmp == NULL) {

 __android_log_print(ANDROID_LOG_ERROR, TAG, "Error on get string");
 return;
 }
 */

//sprintf(buffer, "Test %s", lKeyTmp);
//__android_log_print(ANDROID_LOG_DEBUG, TAG, buffer);

//	(*pEnv)->ReleaseStringUTFChars(pEnv, arg[0], lKeyTmp);


}
