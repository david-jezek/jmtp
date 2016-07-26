/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class jmtp_implWin32_PortableDeviceValuesImplWin32 */

#ifndef _Included_jmtp_implWin32_PortableDeviceValuesImplWin32
#define _Included_jmtp_implWin32_PortableDeviceValuesImplWin32
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    clear
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_clear
  (JNIEnv *, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    count
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_count
  (JNIEnv *, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    setStringValue
 * Signature: (Ljmtp/PropertyKey;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_setStringValue
  (JNIEnv *, jobject, jobject, jstring);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getStringValue
 * Signature: (Ljmtp/PropertyKey;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getStringValue
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    setGuidValue
 * Signature: (Ljmtp/PropertyKey;Lbe/derycke/pieter/com/Guid;)V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_setGuidValue
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getGuidValue
 * Signature: (Ljmtp/PropertyKey;)Lbe/derycke/pieter/com/Guid;
 */
JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getGuidValue
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    setUnsignedIntegerValue
 * Signature: (Ljmtp/PropertyKey;J)V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_setUnsignedIntegerValue
  (JNIEnv *, jobject, jobject, jlong);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getUnsignedIntegerValue
 * Signature: (Ljmtp/PropertyKey;)J
 */
JNIEXPORT jlong JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getUnsignedIntegerValue
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    setPortableDeviceValuesCollectionValue
 * Signature: (Ljmtp/PropertyKey;Ljmtp/implWin32/PortableDevicePropVariantCollectionImplWin32;)V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_setPortableDeviceValuesCollectionValue
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getPortableDeviceValuesCollectionValue
 * Signature: (Ljmtp/PropertyKey;)Ljmtp/implWin32/PortableDevicePropVariantCollectionImplWin32;
 */
JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getPortableDeviceValuesCollectionValue
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    setBoolValue
 * Signature: (Ljmtp/PropertyKey;Z)V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_setBoolValue
  (JNIEnv *, jobject, jobject, jboolean);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getBoolValue
 * Signature: (Ljmtp/PropertyKey;)Z
 */
JNIEXPORT jboolean JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getBoolValue
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    setFloateValue
 * Signature: (Ljmtp/PropertyKey;F)V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_setFloateValue
  (JNIEnv *, jobject, jobject, jfloat);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getFloatValue
 * Signature: (Ljmtp/PropertyKey;)F
 */
JNIEXPORT jfloat JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getFloatValue
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    setErrorValue
 * Signature: (Ljmtp/PropertyKey;Lbe/derycke/pieter/com/COMException;)V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_setErrorValue
  (JNIEnv *, jobject, jobject, jthrowable);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getErrorValue
 * Signature: (Ljmtp/PropertyKey;)Lbe/derycke/pieter/com/COMException;
 */
JNIEXPORT jthrowable JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getErrorValue
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    setUnsignedLargeIntegerValue
 * Signature: (Ljmtp/PropertyKey;Ljava/math/BigInteger;)V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_setUnsignedLargeIntegerValue
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getUnsignedLargeIntegerValue
 * Signature: (Ljmtp/PropertyKey;)Ljava/math/BigInteger;
 */
JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getUnsignedLargeIntegerValue
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    setBufferValue
 * Signature: (Ljmtp/PropertyKey;[B)V
 */
JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_setBufferValue
  (JNIEnv *, jobject, jobject, jbyteArray);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getBufferValue
 * Signature: (Ljmtp/PropertyKey;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getBufferValue
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_implWin32_PortableDeviceValuesImplWin32
 * Method:    getAt
 * Signature: (J)Ljmtp/implWin32/KeyValuePair;
 */
JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceValuesImplWin32_getAt
  (JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif
#endif