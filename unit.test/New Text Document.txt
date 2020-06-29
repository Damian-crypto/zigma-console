#include <jni.h>
#include "Test.h"

JNIEXPORT jstring JNICALL Java_Test_sayHello(JNIEnv *env, jobject thisObj) {
	char outCStr[] = "Hello, World!";
	return (*env)->NewStringUTF(env, outCStr);
}