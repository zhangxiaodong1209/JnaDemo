#include "Test.h"
#include <iostream>

JNIEXPORT void JNICALL Java_Test_printHelloWorld(JNIEnv *env, jobject obj) {
  printf("Hello, World!\n");
}
