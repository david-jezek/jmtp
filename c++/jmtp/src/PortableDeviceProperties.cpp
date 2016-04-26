/*
 * Copyright 2007 Pieter De Rycke
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
#include "jmtp_implWin32_PortableDevicePropertiesImplWin32.h"

inline IPortableDeviceProperties* GetPortableDeviceProperties(JNIEnv* env, jobject obj)
{
	return (IPortableDeviceProperties*)GetComReferencePointer(env, obj, "pDeviceProperties");
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDevicePropertiesImplWin32_getPropertyAttributes
	(JNIEnv* env, jobject obj, jstring jsObjectID, jobject jKey)
{
	HRESULT hr;
	IPortableDeviceProperties* pProperties;
	IPortableDeviceValues* pValues;
	LPWSTR wszObjectID;
	PROPERTYKEY key;
	jclass cls;
	jmethodID mid;
	jobject reference;

	pProperties = GetPortableDeviceProperties(env, obj);
	wszObjectID = ConvertJavaStringToWCHAR(env, jsObjectID);//(WCHAR*)env->GetStringChars(jsObjectID, NULL);
	key = ConvertJavaToPropertyKey(env, jKey);
	hr = pProperties->GetPropertyAttributes(wszObjectID, key, &pValues);
	delete wszObjectID;// env->ReleaseStringChars(jsObjectID, (jchar*)wszObjectID);

	if(FAILED(hr))
	{
		ThrowCOMException(env, L"Failed to retrieve the property", hr);
	}

	//smart reference object aanmaken
	cls = env->FindClass("be/derycke/pieter/com/COMReference");
	mid = env->GetMethodID(cls, "<init>", "(J)V");
	reference = env->NewObject(cls, mid, pValues);
	
	cls = env->FindClass("jmtp/implWin32/PortableDeviceValuesImplWin32");
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
	return env->NewObject(cls, mid, reference);
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDevicePropertiesImplWin32_getValues
(JNIEnv* env, jobject obj, jstring jsObjectID, jobject jobjKeyCollection)
{
	HRESULT hr;
	IPortableDeviceProperties* pProperties;
	IPortableDeviceValues* pValues;
	IPortableDeviceKeyCollection* pKeyCollection;
	LPWSTR wszObjectID;
	jclass cls;
	jmethodID mid;
	jobject jobjKeyCollectionReference;
	jobject jobjReference;

	pProperties = GetPortableDeviceProperties(env, obj);
	mid = env->GetMethodID(env->GetObjectClass(jobjKeyCollection), "getReference",
		"()Lbe/derycke/pieter/com/COMReference;");
	jobjKeyCollectionReference = env->CallObjectMethod(jobjKeyCollection, mid);
	pKeyCollection =
		(IPortableDeviceKeyCollection*)ConvertComReferenceToPointer(env, jobjKeyCollectionReference);
	wszObjectID = ConvertJavaStringToWCHAR(env, jsObjectID); (WCHAR*)env->GetStringChars(jsObjectID, NULL);
	PROPERTYKEY prop;
	pKeyCollection->GetAt(0, &prop);
	hr = pProperties->GetValues(wszObjectID, pKeyCollection, &pValues);
	delete wszObjectID;// env->ReleaseStringChars(jsObjectID, (jchar*)wszObjectID);

	if(FAILED(hr))
	{
		ThrowCOMException(env, L"Failed to retrieve the properties", hr);
		return NULL;
	}

	//smart reference object aanmaken
	cls = env->FindClass("be/derycke/pieter/com/COMReference");
	mid = env->GetMethodID(cls, "<init>", "(J)V");
	jobjReference = env->NewObject(cls, mid, pValues);
	
	cls = env->FindClass("jmtp/implWin32/PortableDeviceValuesImplWin32");
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
	return env->NewObject(cls, mid, jobjReference);
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDevicePropertiesImplWin32_setValues
	(JNIEnv* env, jobject obj, jstring jsObjectID, jobject jobjValues)
{
	//variabelen
	HRESULT hr;
	IPortableDeviceProperties* pProperties;
	IPortableDeviceValues* pValues;
	IPortableDeviceValues* pResults;
	LPWSTR wszObjectID;
	jclass cls;
	jmethodID mid;
	jobject jobjReference;


	//methode implementatie
	if(jsObjectID != NULL && jobjValues != NULL)
	{
		pProperties = GetPortableDeviceProperties(env, obj);

		jobjReference = RetrieveCOMReferenceFromCOMReferenceable(env, jobjValues);
		pValues = (IPortableDeviceValues*)ConvertComReferenceToPointer(env, jobjReference);
		
		wszObjectID = ConvertJavaStringToWCHAR(env, jsObjectID); //(WCHAR*)env->GetStringChars(jsObjectID, NULL);
		hr = pProperties->SetValues(wszObjectID, pValues, &pResults);
		delete wszObjectID;//env->ReleaseStringChars(jsObjectID, (jchar*)wszObjectID);

		if(SUCCEEDED(hr))
		{
			//smart reference object aanmaken
			cls = env->FindClass("be/derycke/pieter/com/COMReference");
			mid = env->GetMethodID(cls, "<init>", "(J)V");
			jobjReference = env->NewObject(cls, mid, pResults);
			
			cls = env->FindClass("jmtp/implWin32/PortableDeviceValuesImplWin32");
			mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
			return env->NewObject(cls, mid, jobjReference);
		}
		else
		{
			ThrowCOMException(env, L"Couldn't change the properties of the element", hr);
			return NULL;
		}
	}
	else
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "arguments can't be null");
		return NULL;
	}
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDevicePropertiesImplWin32_getSupprtedProperties
(JNIEnv *env, jobject obj, jstring jObjectId)
{
	HRESULT hr;
	IPortableDeviceProperties* pProperties;
	IPortableDeviceKeyCollection* pKeyCollection;
	LPWSTR wszObjectID;
	if (jObjectId == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "ObjectID can't be null.");
		return NULL;
	}
	pProperties = GetPortableDeviceProperties(env, obj);
	wszObjectID = ConvertJavaStringToWCHAR(env, jObjectId); //(WCHAR*)env->GetStringChars(jObjectId, NULL);
	hr = pProperties->GetSupportedProperties(wszObjectID, &pKeyCollection);
	delete wszObjectID;// env->ReleaseStringChars(jObjectId, (jchar*)wszObjectID);
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Failed to retrieve the supported properties.", hr);
	}
	return CreateReferenceToKeyCollection(env, pKeyCollection);
}
