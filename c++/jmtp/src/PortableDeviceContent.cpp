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
#include <list>
#include <new>
#include "jmtp.h"
#include "jmtp_implWin32_PortableDeviceContentImplWin32.h"

static inline IPortableDeviceContent* GetPortableDeviceContent(JNIEnv* env, jobject obj)
{
	return (IPortableDeviceContent*)GetComReferencePointer(env, obj, "pDeviceContent");
}

JNIEXPORT jstring JNICALL Java_jmtp_implWin32_PortableDeviceContentImplWin32_createObjectWithPropertiesAndData
	(JNIEnv* env, jobject obj, jobject jobjValues, jobject jobjInputStream, jlong fileSize)
{
	//variabelen
	HRESULT hr;
	IPortableDeviceContent* pDeviceContent;
	IPortableDeviceValues* pDeviceObjectValues;
	jobject jobjValuesReference;
	//jstring jsFileLocation;
	//LPWSTR wszFileLocation;
	DWORD dwBufferSize;
	//CComPtr<IStream> pFileStream;
	CComPtr<IStream> pDeviceStream;
	CComPtr<IPortableDeviceDataStream> pDeviceDataStream;
	//STATSTG fileStats;
	//BYTE* pBuffer;
	//DWORD dwReadFromStream;
	LPWSTR wszObjectID;
	jstring jsObjectID;
	jmethodID mid;

	//printf("in method\n");
	//Methode implementatie
	pDeviceContent = GetPortableDeviceContent(env, obj);
	jobjValuesReference = RetrieveCOMReferenceFromCOMReferenceable(env, jobjValues);
	pDeviceObjectValues = (IPortableDeviceValues*)ConvertComReferenceToPointer(env, jobjValuesReference);
	
	//COM stream object aanmaken
	jclass jInputStreamClass = env->FindClass("java/io/InputStream");
	mid = env->GetMethodID(jInputStreamClass, "read", "([B)I");
	//jsFileLocation = (jstring)env->CallObjectMethod(jobjFile, mid);
	//wszFileLocation = (WCHAR*)env->GetStringChars(jsFileLocation, NULL);
	//hr = SHCreateStreamOnFileW(wszFileLocation, STGM_READ, &pFileStream);
	//env->ReleaseStringChars(jsFileLocation, (jchar*)wszFileLocation); //string resources terug vrijgeven

	//groote van het bestand bepalen
	//(door een beperking in java op het gebied van unsigned integers, moeten we het wel in c++ doen)
	
	pDeviceObjectValues->SetUnsignedLargeIntegerValue(WPD_OBJECT_SIZE, (ULONGLONG)fileSize);
	//printf("before creata\n");

	hr = pDeviceContent->CreateObjectWithPropertiesAndData(+pDeviceObjectValues, 
		&pDeviceStream, &dwBufferSize, NULL);
	//printf("after create\n");

	CheckSuccess(env, hr, L"Couldn't create a COM stream object to the portable device");
	//printf("create ok buf size %d\n", dwBufferSize);

	jshortArray jdataReadBuffer = env->NewShortArray(dwBufferSize);

	//env->SetShortArrayRegion(jdata4, 0, 8, (jshort *)pv.puuid->Data4);
	jshort *pBuffer = new jshort[dwBufferSize];
	//printf("before query Interafce\n");
	pDeviceStream->QueryInterface(IID_IPortableDeviceDataStream, (void**)&pDeviceDataStream);

	//data copy
	//dwReadFromStream = 0;
	jint readed = 0;
	do
	{
		//printf("in do\n");
		readed = env->CallIntMethod(jobjInputStream, mid, jdataReadBuffer);
		//printf("readed %i\n", readed);
		if (readed > 0)
		{
			env->GetShortArrayRegion(jdataReadBuffer, 0, readed, pBuffer);
			//				pFileStream->Read(pBuffer, dwBufferSize, &dwReadFromStream);
			ULONG writed;
			hr = pDeviceDataStream->Write(pBuffer, readed, &writed);
			if (!SUCCEEDED(hr) || readed != writed)
			{
				ThrowCOMException(env, L"Couldn't write data to device", hr);
			}
		}
	}
	while(readed > 0);
	delete[] pBuffer;
	hr = pDeviceDataStream->Commit(STGC_DEFAULT);

	CheckSuccess(env, hr, L"Couldn't commit the data to the portable device");
	pDeviceDataStream->GetObjectID(&wszObjectID);
	jsObjectID = NewJStringFromWCHAR(env, wszObjectID);
	CoTaskMemFree(wszObjectID);
	return jsObjectID;
}

JNIEXPORT jstring JNICALL Java_jmtp_implWin32_PortableDeviceContentImplWin32_updateObjectWithPropertiesAndData
(JNIEnv* env, jobject obj, jstring jsObjId, jobject jobjValues, jobject jobjInputStream, jlong fileSize)
{
	//variabelen
	HRESULT hr;
	IPortableDeviceContent* pDeviceContent;
	IPortableDeviceContent2* pDeviceContent2;
	IPortableDeviceValues* pDeviceObjectValues;
	jobject jobjValuesReference;
	//jstring jsFileLocation;
	//LPWSTR wszFileLocation;
	DWORD dwBufferSize;
	//CComPtr<IStream> pFileStream;
	CComPtr<IStream> pDeviceStream;
	CComPtr<IPortableDeviceDataStream> pDeviceDataStream;
	//STATSTG fileStats;
	//BYTE* pBuffer;
	//DWORD dwReadFromStream;
	LPWSTR wszObjectID;
	jmethodID mid;


	//printf("in method\n");
	//Methode implementatie
	pDeviceContent = GetPortableDeviceContent(env, obj);
	jobjValuesReference = RetrieveCOMReferenceFromCOMReferenceable(env, jobjValues);
	pDeviceObjectValues = (IPortableDeviceValues*)ConvertComReferenceToPointer(env, jobjValuesReference);

	//COM stream object aanmaken
	jclass jInputStreamClass = env->FindClass("java/io/InputStream");
	mid = env->GetMethodID(jInputStreamClass, "read", "([B)I");
	//jsFileLocation = (jstring)env->CallObjectMethod(jobjFile, mid);
	//wszFileLocation = (WCHAR*)env->GetStringChars(jsFileLocation, NULL);
	//hr = SHCreateStreamOnFileW(wszFileLocation, STGM_READ, &pFileStream);
	//env->ReleaseStringChars(jsFileLocation, (jchar*)wszFileLocation); //string resources terug vrijgeven

	//groote van het bestand bepalen
	//(door een beperking in java op het gebied van unsigned integers, moeten we het wel in c++ doen)

	// CoCreate an IPortableDeviceValues interface to hold the the object information
	//hr = CoCreateInstance(CLSID_PortableDeviceValues,
	//	nullptr, CLSCTX_INPROC_SERVER, IID_PPV_ARGS(&pDeviceObjectValues));
	//CheckSuccess(env, hr, L"Couldn't get propertiesValue instance");
	hr = pDeviceObjectValues->SetUnsignedLargeIntegerValue(WPD_OBJECT_SIZE, (ULONGLONG)fileSize);
	CheckSuccess(env, hr, L"Couldn't set new file size value.");

	//printf("before update %d\n", fileSize);
	//if (jsObjId == NULL) {
	//	printf("jsobejctid is null\n");
	//}
	LPWSTR objectID = ConvertJavaStringToWCHAR(env, jsObjId);
	//wprintf(L"obejct id:%s\n", objectID);
	pDeviceContent2 = (IPortableDeviceContent2 *)pDeviceContent;
	hr = pDeviceContent2->UpdateObjectWithPropertiesAndData(objectID
		, pDeviceObjectValues,
		&pDeviceStream, &dwBufferSize);
	delete objectID;
	//printf("after update\n");
	CheckSuccess(env, hr, L"Couldn't update values in object and get COM stream toP portable for data.");
	//printf("create ok buf size %d\n", dwBufferSize);

	jbyteArray jdataReadBuffer = env->NewByteArray(dwBufferSize);

	jbyte *pBuffer = new jbyte[dwBufferSize];
	//printf("before query Interafce\n");
	pDeviceStream->QueryInterface(IID_IPortableDeviceDataStream, (void**)&pDeviceDataStream);

	//data copy
	//dwReadFromStream = 0;
	jint readed = 0;
	do
	{
		//printf("in do\n");
		readed = env->CallIntMethod(jobjInputStream, mid, jdataReadBuffer);
		//printf("readed %d\n", readed);
		if (readed > 0)
		{
			env->GetByteArrayRegion(jdataReadBuffer, 0, readed, pBuffer);
			//				pFileStream->Read(pBuffer, dwBufferSize, &dwReadFromStream);
			//printf("readed2 %d\n", readed);
			ULONG writed = 0;
			hr = pDeviceDataStream->Write(pBuffer, (ULONG)readed, &writed);
			//printf("writed %lu hr=%d\n", writed, hr);
			if (!SUCCEEDED(hr) || readed != writed)
			{
				//printf("throw ex\n");
				ThrowCOMException(env, L"Couldn't write data to device", hr);
			}
		}
	} while (readed > 0);
	delete[] pBuffer;
	hr = pDeviceDataStream->Commit(STGC_DEFAULT);

	CheckSuccess(env, hr, L"Couldn't commit the data to the portable device");
	//pDeviceDataStream->GetObjectID(&wszObjectID);
	//jsObjectID = NewJStringFromWCHAR(env, wszObjectID);
	//CoTaskMemFree(wszObjectID);
	//ThrowCOMException(env, L"debug", hr);
	return jsObjId;
}


JNIEXPORT jstring JNICALL Java_jmtp_implWin32_PortableDeviceContentImplWin32_createObjectWithPropertiesOnly
	(JNIEnv* env, jobject obj, jobject jobjValues)
{
	//variabelen
	HRESULT hr;
	IPortableDeviceContent* pDeviceContent;
	IPortableDeviceValues* pValues;
	LPWSTR wszObjectID;
	jstring jsObjectID;

	
	//Methode implementatie
	if(jobjValues != NULL)
	{
		pDeviceContent = GetPortableDeviceContent(env, obj);
		pValues = (IPortableDeviceValues*)GetComReferencePointerFromComReferenceable(env, jobjValues);
		hr = pDeviceContent->CreateObjectWithPropertiesOnly(pValues, &wszObjectID);

		if(SUCCEEDED(hr))
		{
			jsObjectID = NewJStringFromWCHAR(env, wszObjectID);
			CoTaskMemFree(wszObjectID);
			return jsObjectID;
		}
		else
		{
			ThrowCOMException(env, L"Couldn't create the file", hr);
			return NULL;
		}
	}
	else
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "values can't be null");
		return NULL;
	}
}

JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceContentImplWin32_delete
	(JNIEnv* env, jobject obj, jint jiOptions, jobject jobjObjectIDs)
{
	//variabelen
	HRESULT hr;
	IPortableDeviceContent* pDeviceContent;
	IPortableDevicePropVariantCollection* pObjectIDs;
	//printf("in delete\n");
	//Methode implementatie
	if(jiOptions == 0 || jiOptions == 1)
	{
		pDeviceContent = GetPortableDeviceContent(env, obj);
		pObjectIDs = (IPortableDevicePropVariantCollection*)GetComReferencePointerFromComReferenceable(env, jobjObjectIDs);
		//printf("delete before\n");
		hr = pDeviceContent->Delete(jiOptions, pObjectIDs, NULL);
		DWORD count=0;
		pObjectIDs->GetCount(&count);
		//printf("count- %d\n", count);
		if(FAILED(hr))
		{
			ThrowCOMException(env, L"Failed to delete the files", hr);
			return;
		}
	}
	else
	{
		env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "The parameter options has an invalid value.");
		return;
	}
}

JNIEXPORT jobjectArray JNICALL Java_jmtp_implWin32_PortableDeviceContentImplWin32_listChildObjects
	(JNIEnv* env, jobject obj, jstring jsParentID)
{
	//variabelen
	HRESULT hr;
	IPortableDeviceContent* pDeviceContent;
	LPWSTR wszParentID;
	ComPtr<IEnumPortableDeviceObjectIDs> pEnum;
	std::list<LPWSTR> childList;
	std::list<LPWSTR>::iterator iterator;
	LPWSTR* wszObjectID;
	ULONG fetched;
	jobjectArray jobjaChildArray;


	//Methode implementatie
	pDeviceContent = GetPortableDeviceContent(env, obj);
	wszParentID = ConvertJavaStringToWCHAR(env, jsParentID);//(WCHAR*)env->GetStringChars(jsParentID, NULL);
	hr = pDeviceContent->EnumObjects(0, wszParentID, NULL, &pEnum);
	delete wszParentID; //env->ReleaseStringChars(jsParentID, (jchar*)wszParentID);
	//printf("mam enum\n");
	if(SUCCEEDED(hr))
	{
		wszObjectID = new LPWSTR[10];
		fetched = 0;
		//printf("pred while\n");
		while(SUCCEEDED(pEnum->Next(10, wszObjectID, &fetched)) && fetched > 0)
		{
			for (int i = 0; i < fetched; i++)
			{
				childList.push_back(wszObjectID[i]);
			}
		}
		delete[] wszObjectID;
		//printf("mama potomku:%d\n", childList.size());
		jobjaChildArray = env->NewObjectArray(childList.size(), env->FindClass("Ljava/lang/String;"), NULL);
		int i = 0;
		for (iterator = childList.begin(); iterator != childList.end(); iterator++)
		{
			//wprintf(L"iterator:%s:--\n", *iterator);
			env->SetObjectArrayElement(jobjaChildArray, i, NewJStringFromWCHAR(env, *iterator));
			CoTaskMemFree(*iterator);
			i++;
		}

		return jobjaChildArray;
	}
	else
	{
		ThrowCOMException(env, L"Failed to retrieve the enumerator", hr);
	}
	
	return NULL;
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceContentImplWin32_getProperties
	(JNIEnv* env, jobject obj)
{
	//variabelen
	HRESULT hr;
	IPortableDeviceContent* pContent;
	IPortableDeviceProperties* pProperties;
	jclass cls;
	jmethodID mid;
	jobject jobjReference;


	//Methode implementatie
	pContent = GetPortableDeviceContent(env, obj);
	hr = pContent->Properties(&pProperties);

	if(SUCCEEDED(hr))
	{
		//smart reference object aanmaken
		cls = env->FindClass("be/derycke/pieter/com/COMReference");
		mid = env->GetMethodID(cls, "<init>", "(J)V");
		jobjReference = env->NewObject(cls, mid, pProperties);
		
		cls = env->FindClass("jmtp/implWin32/PortableDevicePropertiesImplWin32");
		mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
		return env->NewObject(cls, mid, jobjReference);
	}
	else
	{
		ThrowCOMException(env, L"Failed to retrieve the property object", hr);
		return NULL;
	}
}

JNIEXPORT jobject JNICALL Java_jmtp_implWin32_PortableDeviceContentImplWin32_getObjectIDsFromPersistentUniqueIDs
	(JNIEnv* env, jobject obj, jobject jobjPropVariantCollection)
{
	//variabelen
	HRESULT hr;
	IPortableDeviceContent* pContent;
	IPortableDevicePropVariantCollection* pPersistentUniqueIDs;
	IPortableDevicePropVariantCollection* pObjectIDs;
	jclass cls;
	jmethodID mid;
	jobject jobjReference;

	//Methode implementatie
	if(jobjPropVariantCollection != NULL)
	{
		pContent = GetPortableDeviceContent(env, obj);
		pPersistentUniqueIDs =
			(IPortableDevicePropVariantCollection*)GetComReferencePointerFromComReferenceable(env, jobjPropVariantCollection);

		hr = pContent->GetObjectIDsFromPersistentUniqueIDs(pPersistentUniqueIDs, &pObjectIDs);

		if(SUCCEEDED(hr))
		{
			//smart reference object aanmaken
			cls = env->FindClass("be/derycke/pieter/com/COMReference");
			mid = env->GetMethodID(cls, "<init>", "(J)V");
			jobjReference = env->NewObject(cls, mid, pObjectIDs);
			
			cls = env->FindClass("jmtp/implWin32/PortableDevicePropVariantCollectionImplWin32");
			mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
			return env->NewObject(cls, mid, jobjReference);
		}
		else
		{
			ThrowCOMException(env, L"Failed to retrieve the ObjectIDs", hr);
			return NULL;
		}
	}
	else
	{
		env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "persistentUniqueIDs can't be null");
		return NULL;
	}
}