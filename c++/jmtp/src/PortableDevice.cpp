/*
 * Copyright 2010 Pieter De Rycke
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

#include <objbase.h>
#include <PortableDeviceApi.h>

#include "jmtp.h"
#include "jmtp_implWin32_PortableDeviceImplWin32.h"

static inline IPortableDevice* GetPortableDevice(JNIEnv* env, jobject obj)
{
	return (IPortableDevice*)GetComReferencePointer(env, obj, "pDevice");
}

static inline IPortableDeviceManager* GetPortableDeviceManager(JNIEnv* env, jobject obj)
{
	return (IPortableDeviceManager*)GetComReferencePointer(env, obj, "pDeviceManager");
}

JNIEXPORT jstring JNICALL Java_jmtp_implWin32_PortableDeviceImplWin32_getDeviceFriendlyName
	(JNIEnv* env, jobject obj, jstring deviceID)
{
	IPortableDeviceManager* pDeviceManager;
	LPWSTR wszDeviceID;
	LPWSTR wszDeviceFriendlyName;
	DWORD length;
	jstring friendlyName;

	pDeviceManager = GetPortableDeviceManager(env, obj);
	wszDeviceID = ConvertJavaStringToWCHAR(env, deviceID);// (WCHAR*)env->GetStringChars(deviceID, NULL);
	pDeviceManager->GetDeviceFriendlyName(wszDeviceID, NULL, &length);
	wszDeviceFriendlyName = new WCHAR[length + 1];
	pDeviceManager->GetDeviceFriendlyName(wszDeviceID, wszDeviceFriendlyName, &length);	
	friendlyName = NewJStringFromWCHAR(env, wszDeviceFriendlyName);

	delete wszDeviceID;//env->ReleaseStringChars(deviceID, (jchar*)wszDeviceID);
	delete wszDeviceFriendlyName;

	return friendlyName;
}

JNIEXPORT jstring JNICALL Java_jmtp_implWin32_PortableDeviceImplWin32_getDeviceManufacturer
	(JNIEnv* env, jobject obj, jstring deviceID)
{
	IPortableDeviceManager* pDeviceManager;
	LPWSTR wszDeviceID;
	LPWSTR wszDeviceManufacturer;
	LPWSTR error = NULL;
	DWORD length;
	jstring manufacturer;
	HRESULT hr;

	pDeviceManager = GetPortableDeviceManager(env, obj);
	wszDeviceID = ConvertJavaStringToWCHAR(env, deviceID);//(WCHAR*)env->GetStringChars(deviceID, NULL);
	//wprintf(L"get man: %s:--\n", wszDeviceID);
	length = 0;
	hr = pDeviceManager->GetDeviceManufacturer(wszDeviceID, NULL, &length);
	CheckSuccess(env, hr, L"Unable retrive manufacture length.");
	//printf("manufacture length %d\n", length);
	wszDeviceManufacturer = new WCHAR[length + 1];
	hr = pDeviceManager->GetDeviceManufacturer(wszDeviceID, wszDeviceManufacturer, &length);	
	CheckSuccess(env, hr, L"Unable retrive manufacture string.");
	manufacturer = NewJStringFromWCHAR(env, wszDeviceManufacturer);
	//env->ReleaseStringChars(deviceID, (jchar*)wszDeviceID);
	delete wszDeviceID;
	delete wszDeviceManufacturer;
	//env->ThrowNew(env->FindClass("java/lang/RuntimeException"), "error");

	return manufacturer;
}

JNIEXPORT jstring JNICALL Java_jmtp_implWin32_PortableDeviceImplWin32_getDeviceDescription
	(JNIEnv* env, jobject obj, jstring deviceID)
{
	IPortableDeviceManager* pDeviceManager;
	LPWSTR wszDeviceID;
	LPWSTR wszDeviceDescription;
	DWORD length;
	jstring description;

	pDeviceManager = GetPortableDeviceManager(env, obj);
	wszDeviceID = ConvertJavaStringToWCHAR(env, deviceID);// (WCHAR*)env->GetStringChars(deviceID, NULL);

	pDeviceManager->GetDeviceDescription(wszDeviceID, NULL, &length);
	wszDeviceDescription = new WCHAR[length + 1];
	pDeviceManager->GetDeviceDescription(wszDeviceID, wszDeviceDescription, &length);	
	description = NewJStringFromWCHAR(env, wszDeviceDescription);

	delete wszDeviceID;//env->ReleaseStringChars(deviceID, (jchar*)wszDeviceID);
	delete wszDeviceDescription;

	return description;
}

JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceImplWin32_openImpl
	(JNIEnv* env, jobject obj, jobject values)
{
	HRESULT hr;
	IPortableDevice* pDevice;
	jobject reference;
	jmethodID mid;
	IPortableDeviceValues* pClientInfo;
	LPWSTR wszDeviceID;
	jfieldID fid;
	jstring jsDeviceID;

	pDevice = GetPortableDevice(env, obj);

	//clientinfo value object opvragen
	mid = env->GetMethodID(env->GetObjectClass(values), "getReference", 
		"()Lbe/derycke/pieter/com/COMReference;");
	reference = env->CallObjectMethod(values, mid);
	mid = env->GetMethodID(env->FindClass("be/derycke/pieter/com/COMReference"), "getMemoryAddress", "()J");
	pClientInfo = (IPortableDeviceValues*)env->CallLongMethod(reference, mid);

	//deviceID opvragen
	fid = env->GetFieldID(env->GetObjectClass(obj), "deviceID", "Ljava/lang/String;");
	jsDeviceID = (jstring)env->GetObjectField(obj, fid);
	wszDeviceID = ConvertJavaStringToWCHAR(env, jsDeviceID);//(WCHAR*)env->GetStringChars(jsDeviceID, NULL);

	hr = pDevice->Open(wszDeviceID, pClientInfo);
	delete wszDeviceID;
	//env->ReleaseStringChars(jsDeviceID, (jchar*)wszDeviceID);

	if(FAILED(hr))
	{
		ThrowCOMException(env, L"Couldn't open the device", hr);
		return;
	}
}

JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceImplWin32_closeImpl
	(JNIEnv* env, jobject obj)
{
	HRESULT hr;
	IPortableDevice* pDevice;

	pDevice = GetPortableDevice(env, obj);
	hr = pDevice->Close();
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Couldn't close the device", hr);
		return;
	}
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceImplWin32_getDeviceContent
	(JNIEnv* env, jobject obj)
{
	HRESULT hr;
	IPortableDevice* pDevice;
	IPortableDeviceContent* pContent;
	jclass cls;
	jmethodID mid;
	jobject reference;

	pDevice = GetPortableDevice(env, obj);
	hr = pDevice->Content(&pContent);
	if(FAILED(hr))
	{
		ThrowCOMException(env, L"Couldn't retrieve the device content", hr);
		return NULL;
	}

	//smart reference object aanmaken
	cls = env->FindClass("be/derycke/pieter/com/COMReference");
	mid = env->GetMethodID(cls, "<init>", "(J)V");
	reference = env->NewObject(cls, mid, pContent);
	
	cls = env->FindClass("jmtp/implWin32/PortableDeviceContentImplWin32");
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
	return env->NewObject(cls, mid, reference);
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceImplWin32_getDeviceResources
(JNIEnv *env, jobject obj)
{
	HRESULT hr;
	IPortableDevice* pDevice;
	IPortableDeviceContent* pContent;
	IPortableDeviceResources* pResource;
	jclass cls;
	jmethodID mid;
	jobject reference;

	pDevice = GetPortableDevice(env, obj);
	hr = pDevice->Content(&pContent);
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Couldn't retrieve the device content", hr);
		return NULL;
	}
	hr = pContent->Transfer(&pResource);
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Couldn't retrieve the device resource", hr);
		return NULL;
	}

	//smart reference object aanmaken
	cls = env->FindClass("be/derycke/pieter/com/COMReference");
	mid = env->GetMethodID(cls, "<init>", "(J)V");
	reference = env->NewObject(cls, mid, pResource);

	cls = env->FindClass("jmtp/implWin32/PortableDeviceResourcesImplWin32");
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
	return env->NewObject(cls, mid, reference);
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceImplWin32_sendCommand
	(JNIEnv* env, jobject obj, jobject values)
{
	HRESULT hr;
	IPortableDevice* pDevice;
	IPortableDeviceValues* pValuesIn;
	IPortableDeviceValues* pValuesOut;
	jclass cls;
	jmethodID mid;
	jobject reference;

	pDevice = GetPortableDevice(env, obj);

	//clientinfo value object opvragen
	mid = env->GetMethodID(env->GetObjectClass(values), "getReference", 
		"()Lbe/derycke/pieter/com/COMReference;");
	reference = env->CallObjectMethod(values, mid);
	mid = env->GetMethodID(env->FindClass("be/derycke/pieter/com/COMReference"), "getMemoryAddress", "()J");
	pValuesIn = (IPortableDeviceValues*)env->CallLongMethod(reference, mid);

	hr = pDevice->SendCommand(0, pValuesIn, &pValuesOut);

	if(FAILED(hr))
	{
		ThrowCOMException(env, L"The custom command failed.", hr);
		return NULL;
	}

	//smart reference object aanmaken
	cls = env->FindClass("be/derycke/pieter/com/COMReference");
	mid = env->GetMethodID(cls, "<init>", "(J)V");
	reference = env->NewObject(cls, mid, pValuesOut);
	
	cls = env->FindClass("jmtp/implWin32/PortableDeviceValuesImplWin32");
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
	return env->NewObject(cls, mid, reference);
}