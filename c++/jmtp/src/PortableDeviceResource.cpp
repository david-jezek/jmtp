/*
* Copyright 2015 David Jezek
*
* This file is part of JMTP.
*
* JTMP is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation, either version 3 of
* the License, or any later version.
*
* JMTP is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU LesserGeneral Public
* License along with JMTP. If not, see <http://www.gnu.org/licenses/>.
*/

#include <PortableDeviceApi.h>
#include <PortableDevice.h>

#include "jmtp.h"
#include "jmtp_implWin32_PortableDeviceResourcesImplWin32.h"

inline IPortableDeviceResources* GetPortableDeviceResources(JNIEnv* env, jobject obj)
{
	return (IPortableDeviceResources*)GetComReferencePointer(env, obj, "pDeviceResource");
}

JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceResourcesImplWin32_cancel
(JNIEnv *env, jobject obj) {
	HRESULT hr;
	hr = GetPortableDeviceResources(env, obj)->Cancel();
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Failed to cancel operation.", hr);
	}

}

JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceResourcesImplWin32_delete
(JNIEnv *env, jobject obj, jstring jsObjectID, jobject jKeys) {
	HRESULT hr;
	IPortableDeviceResources* pResources;
	IPortableDeviceKeyCollection* pKeyCollection;
	LPWSTR wszObjectID;
	jmethodID mid;
	jobject jobjKeyCollectionReference;

	if (jKeys != NULL)
	{
		pResources = GetPortableDeviceResources(env, obj);
		wszObjectID = ConvertJavaStringToWCHAR(env, jsObjectID); //(WCHAR*)env->GetStringChars(jsObjectID, NULL);
		mid = env->GetMethodID(env->GetObjectClass(jKeys), "getReference",
			"()Lbe/derycke/pieter/com/COMReference;");
		jobjKeyCollectionReference = env->CallObjectMethod(jKeys, mid);
		pKeyCollection =
			(IPortableDeviceKeyCollection*)ConvertComReferenceToPointer(env, jobjKeyCollectionReference);
		hr = pResources->Delete(wszObjectID, pKeyCollection);
		delete wszObjectID;// env->ReleaseStringChars(jsObjectID, (jchar*)wszObjectID);
		if (FAILED(hr))
		{
			ThrowCOMException(env, L"Failed to retrieve the property", hr);
		}
	}
	else
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "Keys collection can't be null.");
	}
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceResourcesImplWin32_getSupportedResources
(JNIEnv *env, jobject obj, jstring jObjectId)
{
	HRESULT hr;
	IPortableDeviceResources* pResources;
	IPortableDeviceKeyCollection* pKeyCollection;
	LPWSTR wszObjectID;

	if (jObjectId == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "ObjectID can't be null.");
		return NULL;
	}
		pResources = GetPortableDeviceResources(env, obj);
		wszObjectID = ConvertJavaStringToWCHAR(env, jObjectId); //(WCHAR*)env->GetStringChars(jObjectId, NULL);
		hr = pResources->GetSupportedResources(wszObjectID, &pKeyCollection);
		delete wszObjectID;// env->ReleaseStringChars(jObjectId, (jchar*)wszObjectID);
		if (FAILED(hr))
		{
			ThrowCOMException(env, L"Failed to retrieve the supported resources.", hr);
		}
		return CreateReferenceToKeyCollection(env, pKeyCollection);
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceResourcesImplWin32_getResourceAttributes
(JNIEnv *env, jobject obj, jstring jObjectId, jobject jKey)
{
	HRESULT hr;
	IPortableDeviceResources* pResources;
	IPortableDeviceValues* pPropAttrs;
	LPWSTR wszObjectID;
	PROPERTYKEY key;

	if (jObjectId == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "ObjectID can't be null.");
		return NULL;
	}
	if (jKey == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "PropertyKey can't be null.");
		return NULL;
	}
	pResources = GetPortableDeviceResources(env, obj);
	wszObjectID = ConvertJavaStringToWCHAR(env, jObjectId); //(WCHAR*)env->GetStringChars(jObjectId, NULL);
	key = ConvertJavaToPropertyKey(env, jKey);
	hr = pResources->GetResourceAttributes(wszObjectID, key, &pPropAttrs);
	delete wszObjectID;// env->ReleaseStringChars(jObjectId, (jchar*)wszObjectID);
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Failed to retrieve the property attributes.", hr);
	}
	return CreateReferenceToValues(env, pPropAttrs);
}

/*
* Class:     jmtp_implWin32_PortableDeviceResourcesImplWin32
* Method:    getInputStream
* Signature: (Ljava/lang/String;Ljmtp/PropertyKey;)Ljmtp/implWin32/PortableDeviceObjectInputStream;
*/
JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceResourcesImplWin32_getInputStream
(JNIEnv *env, jobject jthis, jstring jObjectId, jobject propKey)
{
	HRESULT hr;
	IPortableDeviceResources* pResources;
	LPWSTR wszObjectID;
	IStream  *stream;

	if (jObjectId == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "ObjectID can't be null.");
		return NULL;
	}
	pResources = GetPortableDeviceResources(env, jthis);
	wszObjectID = ConvertJavaStringToWCHAR(env, jObjectId); //(WCHAR*)env->GetStringChars(jObjectId, NULL);
	DWORD bufSize = 0;
	PROPERTYKEY key = ConvertJavaToPropertyKey(env, propKey);
	hr = pResources->GetStream(wszObjectID, key, STGM_READ, &bufSize, &stream);
	delete wszObjectID;// env->ReleaseStringChars(jObjectId, (jchar*)wszObjectID);
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Failed to retrieve the supported resources.", hr);
		return NULL;
	}
	jobject jStream = CreateReferencableObject(env, "jmtp/implWin32/PortableDeviceObjectInputStream", stream);
	jclass cls = env->FindClass("jmtp/implWin32/PortableDeviceObjectInputStream");
	jmethodID mid = env->GetMethodID(cls, "createBuffer", "(I)V");
	env->CallVoidMethod(jStream, mid, bufSize);
	return  jStream;
}

/*
* Class:     jmtp_implWin32_PortableDeviceResourcesImplWin32
* Method:    getOutputStream
* Signature: (Ljava/lang/String;Ljmtp/PropertyKey;)Ljmtp/implWin32/PortableDeviceObjectOutputStream;
*/
JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceResourcesImplWin32_getOutputStream
(JNIEnv *env, jobject jthis, jstring jObjectId, jobject propKey)
{
	HRESULT hr;
	IPortableDeviceResources* pResources;
	LPWSTR wszObjectID;
	IStream  *stream;

	if (jObjectId == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "ObjectID can't be null.");
		return NULL;
	}
	pResources = GetPortableDeviceResources(env, jthis);
	wszObjectID = ConvertJavaStringToWCHAR(env, jObjectId); //(WCHAR*)env->GetStringChars(jObjectId, NULL);
	DWORD bufSize = 0;
	PROPERTYKEY key = ConvertJavaToPropertyKey(env, propKey);
	hr = pResources->GetStream(wszObjectID, key, STGM_WRITE, &bufSize, &stream);
	delete wszObjectID;// env->ReleaseStringChars(jObjectId, (jchar*)wszObjectID);
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Failed to retrieve the supported resources.", hr);
		return NULL;
	}
	jobject jStream = CreateReferencableObject(env, "jmtp/implWin32/PortableDeviceObjectOutputStream", stream);
	jclass cls = env->FindClass("jmtp/implWin32/PortableDeviceObjectOutputStream");
	jmethodID mid = env->GetMethodID(cls, "createBuffer", "(I)V");
	env->CallVoidMethod(jStream, mid, bufSize);
	return  jStream;
}
