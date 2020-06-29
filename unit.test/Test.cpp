#include <jni.h>
#include "Test.h"

extern "C" {
	JNIEXPORT jstring JNICALL Java_Test_sayHello(JNIEnv *env, jobject thisObj) {
		char outCStr[] = "Hello, World!";
		return env->NewStringUTF(outCStr);
	}
}