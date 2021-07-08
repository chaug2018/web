/* DO NOT EDIT THIS FILE - it is machine generated */
#include "jni.h"
/* Header for class uk_co_mmscomputing_device_twain_jtwain */

#ifndef _Included_uk_co_mmscomputing_device_twain_jtwain
#define _Included_uk_co_mmscomputing_device_twain_jtwain
#ifdef __cplusplus
extern "C" {
#endif
/* Inaccessible static: installed */
/* Inaccessible static: nativeThread */
/* Inaccessible static: sourceManager */
/* Inaccessible static: scanner */
/* Inaccessible static: class_00024uk_00024co_00024mmscomputing_00024device_00024twain_00024TwainSource */
/* Inaccessible static: class_00024uk_00024co_00024mmscomputing_00024device_00024twain_00024jtwain */
/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ninitLib
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ninitLib
  (JNIEnv *, jclass);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    nstart
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nstart
  (JNIEnv *, jclass);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ntrigger
 * Signature: (Ljava/lang/Object;I)V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ntrigger
  (JNIEnv *, jclass, jobject, jint);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ncallSourceManager
 * Signature: (III[B)I
 */
JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncallSourceManager
  (JNIEnv *, jclass, jint, jint, jint, jbyteArray);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ngetContainer
 * Signature: (II)[B
 */
JNIEXPORT jbyteArray JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ngetContainer
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    nsetContainer
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nsetContainer
  (JNIEnv *, jclass, jint, jbyteArray);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    nfree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nfree
  (JNIEnv *, jclass, jint);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ncallSource
 * Signature: ([BIII[B)I
 */
JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncallSource
  (JNIEnv *, jclass, jbyteArray, jint, jint, jint, jbyteArray);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ntransferImage
 * Signature: (I)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ntransferImage
  (JNIEnv *, jclass, jint);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    nnew
 * Signature: ([BI)V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nnew
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ncopy
 * Signature: ([BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncopy
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ndelete
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ndelete
  (JNIEnv *, jclass, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif