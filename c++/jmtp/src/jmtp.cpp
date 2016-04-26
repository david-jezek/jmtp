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

#include <objbase.h>
#include <atlbase.h>
#include <jni.h>

#include "jmtp.h"

void ThrowCOMException(JNIEnv* env, LPWSTR message, HRESULT hr)
{
	jclass cls;
	jmethodID mid;
	jstring jsMessage;
	jobject exception;

	cls = env->FindClass("be/derycke/pieter/com/COMException");
	mid = env->GetMethodID(cls, "<init>", "(Ljava/lang/String;I)V");
	jsMessage = NewJStringFromWCHAR(env, message);
	exception = env->NewObject(cls, mid, jsMessage, (jint)hr);
	env->Throw((jthrowable)exception);
}

jobject ConvertGuidToJava(JNIEnv* env, GUID guid)
{
	jclass cls;
	jmethodID mid;
	jshortArray jData4;

	cls = env->FindClass("be/derycke/pieter/com/Guid");
	mid = env->GetMethodID(cls, "<init>", "(JII[S)V");
	
	jData4 = env->NewShortArray(8);
	for(int i = 0; i < 8; i++) {
		jshort jsBuffer = guid.Data4[i];
		env->SetShortArrayRegion(jData4, i, 1, &jsBuffer);
	}

	return env->NewObject(cls, mid, (jlong)guid.Data1, (jint)guid.Data2, (jint)guid.Data3, jData4);
}

GUID ConvertJavaToGuid(JNIEnv* env, jobject jGuid)
{
	GUID guid;
	jmethodID mid;
	jclass cls;

	cls = env->GetObjectClass(jGuid);

	mid	= env->GetMethodID(cls, "getData1", "()J");
	guid.Data1 = static_cast<unsigned long>(env->CallLongMethod(jGuid, mid));

	mid = env->GetMethodID(cls, "getData2", "()I");
	guid.Data2 = static_cast<unsigned short>(env->CallIntMethod(jGuid, mid));

	mid = env->GetMethodID(cls, "getData3", "()I");
	guid.Data3 = static_cast<unsigned short>(env->CallIntMethod(jGuid, mid));

	mid = env->GetMethodID(cls, "getData4", "()[S");
	jshort* data4 = env->GetShortArrayElements((jshortArray)env->CallObjectMethod(jGuid, mid), NULL);
	for(int i = 0; i < 8; i++)
		guid.Data4[i] = static_cast<unsigned char>(data4[i]);

	return guid;
}

jobject ConvertPropertyKeyToJava(JNIEnv* env, PROPERTYKEY key)
{
	jclass cls;
	jmethodID mid;

	cls = env->FindClass("jmtp/PropertyKey");
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/Guid;J)V");

	return env->NewObject(cls, mid, ConvertGuidToJava(env, key.fmtid), (jlong)key.pid);
}

PROPERTYKEY ConvertJavaToPropertyKey(JNIEnv* env, jobject jKey)
{
	PROPERTYKEY key;
	jmethodID mid;
	jclass cls;
	jobject jGuid;

	cls = env->FindClass("jmtp/PropertyKey");

	mid = env->GetMethodID(cls, "getPid", "()J");
	key.pid = static_cast<DWORD>(env->CallLongMethod(jKey, mid));

	mid = env->GetMethodID(cls, "getFmtid", "()Lbe/derycke/pieter/com/Guid;");
	jGuid = env->CallObjectMethod(jKey, mid);
	key.fmtid = ConvertJavaToGuid(env, jGuid);

	return key;
}

jobject ConvertPropVariantToJava(JNIEnv* env, PROPVARIANT pv)
{
	//variabelen
	jclass cls;
	jmethodID mid;
	jstring jsValue;
	cls = env->FindClass("jmtp/PropVariant");
	if (pv.vt == VT_EMPTY) {
		mid = env->GetStaticMethodID(cls, "getEmptyPropValue", "()Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid);
	}
	else if (pv.vt == VT_NULL) {
		mid = env->GetStaticMethodID(cls, "getNullPropValue", "()Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid);

	}
	else if (pv.vt == VT_I1) {
		mid = env->GetStaticMethodID(cls, "getI1PropValue", "(B)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.cVal);
	}
	else if (pv.vt == VT_I2) {
		mid = env->GetStaticMethodID(cls, "getI2PropValue", "(S)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.iVal);
	}
	else if (pv.vt == VT_I4) {
		mid = env->GetStaticMethodID(cls, "getI4PropValue", "(I)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.intVal);
	}
	else if (pv.vt == VT_I8) {
		mid = env->GetStaticMethodID(cls, "getI8PropValue", "(J)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.lVal);
	}
	else if (pv.vt == VT_R4) {
		mid = env->GetStaticMethodID(cls, "getR4PropValue", "(f)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.fltVal);
	}
	else if (pv.vt == VT_R8) {
		mid = env->GetStaticMethodID(cls, "getR8PropValue", "(d)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.dblVal);
	}
	else if (pv.vt == VT_CY) {
		mid = env->GetStaticMethodID(cls, "getCYPropValue", "(d)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.dblVal);
	}
	else if (pv.vt == VT_DATE) {
		mid = env->GetStaticMethodID(cls, "getDatePropValue", "(D)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.date);
	}
	else if (pv.vt == VT_BSTR) {
		mid = env->GetStaticMethodID(cls, "getBSTRPropValue", "(Ljava/lang/String;)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, NewJStringFromWCHAR(env, pv.bstrVal));
	}
	else if (pv.vt == VT_DISPATCH) {
		mid = env->GetStaticMethodID(cls, "getDISPATCHPropValue", "(Ljava/lang/ref/WeakReference/COMReference;)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, 
			CreateReferencableObject(env, "java/lang/ref/WeakReference/COMReference", pv.punkVal));
	}
	else if (pv.vt == VT_ERROR) {
		mid = env->GetStaticMethodID(cls, "getERRORPropValue", "(J)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.scode);
	}
	else if (pv.vt == VT_BOOL) {
		mid = env->GetStaticMethodID(cls, "getBOOLPropValue", "(Z)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.boolVal);
	}
	else if (pv.vt == VT_DECIMAL) {
		mid = env->GetStaticMethodID(cls, "getDECIMALPropValue", "(JJJ)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.decVal.Hi32, pv.decVal.Mid32, pv.decVal.Lo32);
	}
	else if (pv.vt == VT_UI1) {
		mid = env->GetStaticMethodID(cls, "getUI1PropValue", "(C)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.bVal);
	}
	else if (pv.vt == VT_UI2) {
		mid = env->GetStaticMethodID(cls, "getUI2PropValue", "(C)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.uiVal);
	}
	else if (pv.vt == VT_UI4) {
		mid = env->GetStaticMethodID(cls, "getUI4PropValue", "(J)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.ulVal);
	}
	else if (pv.vt == VT_UI8) {
		mid = env->GetStaticMethodID(cls, "getUI8PropValue", "(J)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.uhVal);
	}
	else if (pv.vt == VT_INT) {
		mid = env->GetStaticMethodID(cls, "getINTPropValue", "(I)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.intVal);
	}
	else if (pv.vt == VT_UINT) {
		mid = env->GetStaticMethodID(cls, "getUINTPropValue", "(I)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.uintVal);
	}
	else if (pv.vt == VT_HRESULT) {
		mid = env->GetStaticMethodID(cls, "getHRESULTPropValue", "(J)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.hVal);
	}
	else if(pv.vt == VT_LPWSTR) {
		mid = env->GetMethodID(cls, "<init>", "(Ljava/lang/String;)V");
		jsValue = NewJStringFromWCHAR(env, pv.pwszVal);
		return env->NewObject(cls, mid, jsValue);
	}
	else if (pv.vt == VT_LPSTR) {
		mid = env->GetMethodID(cls, "<init>", "(Ljava/lang/String;)V");
		jsValue = NewJStringFromWCHAR(env, pv.pwszVal);
		return env->NewObject(cls, mid, jsValue);
	}
	else if (pv.vt == VT_FILETIME) {
		mid = env->GetStaticMethodID(cls, "getHRESULTPropValue", "(CC)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, pv.filetime.dwHighDateTime, pv.filetime.dwLowDateTime);
	}
	else if (pv.vt == VT_BLOB) {
		mid = env->GetStaticMethodID(cls, "getBlobPropValue", "([B)Ljmtp/PropVariant;");
		jbyteArray blob = env->NewByteArray(pv.blob.cbSize);
		env->SetByteArrayRegion(blob, 0, pv.blob.cbSize, (jbyte *)pv.blob.pBlobData);
		return env->CallStaticObjectMethod(cls, mid, blob);
	}
	else if (pv.vt == VT_STREAM) {
		mid = env->GetStaticMethodID(cls, "getStreamPropValue", "(Ljava/lang/ref/WeakReference/COMReference;)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid,
			CreateReferencableObject(env, "java/lang/ref/WeakReference/COMReference", pv.pStream));
	}
	else if (pv.vt == VT_STORAGE) {
		mid = env->GetStaticMethodID(cls, "getStoragePropValue", "(Ljava/lang/ref/WeakReference/COMReference;)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid,
			CreateReferencableObject(env, "java/lang/ref/WeakReference/COMReference", pv.pStorage));
	}
	else if (pv.vt == VT_CLSID) {
		mid = env->GetStaticMethodID(cls, "getCLSIDPropValue", "(JII[S)Ljmtp/PropVariant;");
		jshortArray jdata4 = env->NewShortArray(8);
		env->SetShortArrayRegion(jdata4, 0, 8, (jshort *)pv.puuid->Data4);
		return env->CallStaticObjectMethod(cls, mid, pv.puuid->Data1, pv.puuid->Data2, pv.puuid->Data3, jdata4);
	}
	else
	{
		mid = env->GetStaticMethodID(cls, "getTypedPropValue", "(I)Ljmtp/PropVariant;");
		return env->CallStaticObjectMethod(cls, mid, (int)pv.vt);
	}
}


//nog werk nodig! met de union!!
PROPVARIANT ConvertJavaToPropVariant(JNIEnv* env, jobject jobjPropVariant)
{
	//variabelen
	jclass cls;
	jmethodID mid;
	PROPVARIANT pv;
	jstring jsValue;
	LPWSTR wszBuffer;
	LPWSTR wszValue;
	jobject jobjObjectValue;


	//methode implementatie
	PropVariantInit(&pv);
	cls = env->FindClass("jmtp/PropVariant");
	//printf("convert 1\n");
	mid = env->GetMethodID(cls, "getVt", "()I");
	pv.vt = static_cast<VARTYPE>(env->CallIntMethod(jobjPropVariant, mid));
	//printf("convert 2\n");
	int strLength;
	switch(pv.vt)
	{
		case VT_LPWSTR:
			//printf("convert 3\n");
			mid = env->GetMethodID(cls, "getValue", "()Ljava/lang/Object;");
			jsValue = (jstring)env->CallObjectMethod(jobjPropVariant, mid);
			strLength = env->GetStringLength(jsValue);
			//printf("convert3.4:%d\n", strLength);
			wszBuffer = ConvertJavaStringToWCHAR(env, jsValue); //(WCHAR*)env->GetStringChars(jsValue, NULL);
			wszValue = new WCHAR[strLength+1];
			if (wszValue == NULL) {
				//printf("conver tproblem null\n");
			}
			for (int i = 0; i < strLength; i++) {
				wszValue[i] = wszBuffer[i];
			}
			wszValue[strLength] = 0;
			//wcscpy_s(wszValue, strLength, wszBuffer);
			//wprintf(L"convert3.5:%s\n", wszBuffer);
			//wprintf(L"convert3.6:%s\n", wszValue);
			delete wszBuffer;
			pv.pwszVal = wszValue;
			//wprintf(L"convert4:%s\n", pv.pwszVal);
			break;
		case VT_BOOL:
			mid = env->GetMethodID(cls, "getValue", "()Ljava/lang/Object;");
			jobjObjectValue = env->CallObjectMethod(jobjPropVariant, mid);
			mid = env->GetMethodID(env->FindClass("java/lang/Boolean"), "booleanValue", "()Z");
			pv.boolVal = env->CallBooleanMethod(jobjObjectValue, mid);
			break;
	}
	//andere types worden momenteel niet ondersteunt
	
	return pv;
}

jobject CreateJavaValueKeyPair(JNIEnv* env, PROPERTYKEY key, PROPVARIANT pv)
{
	jclass cls;
	jmethodID mid;
	jfieldID fid;

	cls = env->FindClass("jmtp/implWin32/KeyValuePair");
	mid = env->GetMethodID(cls, "<init>", "()V");
	jobject pair = env->NewObject(cls, mid);
	fid = env->GetFieldID(cls, "key", "Ljmtp/PropertyKey;");
	env->SetObjectField(pair, fid, ConvertPropertyKeyToJava(env, key));
	fid = env->GetFieldID(cls, "value", "Ljmtp/PropVariant;");
	env->SetObjectField(pair, fid, ConvertPropVariantToJava(env, pv));
	return pair;
}

ULONGLONG ConvertJavaToUnsignedLongLong(JNIEnv* env, jobject jobjBigInteger)
{
	jmethodID mid;
	jbyteArray jbaArray;
	jboolean isCopy = JNI_TRUE;
	jbyte* buffer;
	jsize jiLength;
	ULONGLONG result = 0;
	byte* tmp;

	mid = env->GetMethodID(env->FindClass("java/math/BigInteger"), "signum", "()I");
	if(env->CallIntMethod(jobjBigInteger, mid) >= 0)
	{
		mid = env->GetMethodID(env->FindClass("java/math/BigInteger"), "toByteArray", "()[B");
		jbaArray = (jbyteArray)env->CallObjectMethod(jobjBigInteger, mid);

		//van big endian naar little endian converteren
		jiLength = env->GetArrayLength(jbaArray);
		buffer = env->GetByteArrayElements(jbaArray, &isCopy);

		//i < 8 is omdat we met een 64 bit getal werken
		tmp = new byte[sizeof(ULONGLONG)];
		for(int i = 0; i < sizeof(ULONGLONG); i++)
		{
			if(i < jiLength)
				tmp[i] = buffer[jiLength - i - 1];
			else
				tmp[i] = 0;
		}
		memcpy_s(&result, sizeof(ULONGLONG), tmp, sizeof(ULONGLONG));
		delete tmp;

		env->ReleaseByteArrayElements(jbaArray, buffer, JNI_ABORT);

		return result;
	}
	else
	{
		env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Only positive integers can be converted.");
		return -1;	//-> wat doen we bij negatieve getallen?
	}
}

jobject ConvertUnsignedLongLongToJava(JNIEnv* env, ULONGLONG number)
{
	jbyteArray buffer;
	jclass cls;
	jmethodID mid;

	buffer = env->NewByteArray(9);
	for(int i = 0; i < 8; i++)
		env->SetByteArrayRegion(buffer, (i + 1), 1, (jbyte*)((byte*)&number + 7 - i));

	cls = env->FindClass("java/math/BigInteger"); 
	mid = env->GetMethodID(cls, "<init>", "([B)V");
	return env->NewObject(cls, mid, buffer);
}

inline jlong ConvertComReferenceToPointer(JNIEnv* env, jobject jobjReference)
{
	jmethodID mid;

	mid = env->GetMethodID(env->FindClass("be/derycke/pieter/com/COMReference"), "getMemoryAddress", "()J");
	return env->CallLongMethod(jobjReference, mid);
}

//De COMReference opvragen van een COMReferencable object
inline jobject RetrieveCOMReferenceFromCOMReferenceable(JNIEnv* env, jobject jobjCOMReferenceable)
{
	jmethodID mid = env->GetMethodID(env->FindClass("be/derycke/pieter/com/COMReferenceable"), 
		"getReference", "()Lbe/derycke/pieter/com/COMReference;");
	return env->CallObjectMethod(jobjCOMReferenceable, mid);
}

jlong GetComReferencePointer(JNIEnv* env, jobject obj, const char* fieldName)
{
	jclass cls;
	jobject reference;

	cls = env->GetObjectClass(obj);
	reference = env->GetObjectField(obj, env->GetFieldID(cls, fieldName, "Lbe/derycke/pieter/com/COMReference;"));
	return ConvertComReferenceToPointer(env, reference);
}
void* RetrievePointerFromCOMReferenceable(JNIEnv* env, jobject jobjCOMReferenceable) {
	jobject jobjValuesReference = RetrieveCOMReferenceFromCOMReferenceable(env, jobjCOMReferenceable);
	return (void *)ConvertComReferenceToPointer(env, jobjValuesReference);
}

jlong GetComReferencePointerFromComReferenceable(JNIEnv* env, jobject jobjCOMReferenceable)
{
	jobject jobjReference = RetrieveCOMReferenceFromCOMReferenceable(env, jobjCOMReferenceable);
	return ConvertComReferenceToPointer(env, jobjReference);
}

jobject CreateReferencableObject(JNIEnv* env, const char *className, IUnknown *ref)
{
	jclass cls;
	jmethodID mid;
	jobject reference;
	//smart reference object aanmaken
	cls = env->FindClass("be/derycke/pieter/com/COMReference");
	mid = env->GetMethodID(cls, "<init>", "(J)V");
	reference = env->NewObject(cls, mid, ref);

	cls = env->FindClass(className);
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
	return env->NewObject(cls, mid, reference);
}

jobject CreateReferenceToKeyCollection(JNIEnv* env, IPortableDeviceKeyCollection *ref)
{
	return CreateReferencableObject(env, "jmtp/implWin32/PortableDeviceKeyCollectionImplWin32", ref);
}

jobject CreateReferenceToValues(JNIEnv* env, IPortableDeviceValues *ref)
{
	return CreateReferencableObject(env, "jmtp/implWin32/PortableDeviceValuesImplWin32", ref);
}

jstring NewJStringFromWCHAR(JNIEnv* env, LPWSTR str)
{
	//TODO resolve conversion warning. Posible problem for long strings
	int strlen = wcslen(str);
	jcharArray jChars = env->NewCharArray(strlen);
	env->SetCharArrayRegion(jChars, 0, strlen, (jchar*)str);
	jclass strClass = env->FindClass("java/lang/String");
	return (jstring)env->NewObject(strClass, env->GetMethodID(strClass, "<init>", "([C)V"), jChars);
	//return env->NewString((jchar*)str, wcslen(str));
}
LPWSTR ConvertJavaStringToWCHAR(JNIEnv* env, jstring objectString)
{
	//printf("java str to wchar\n");
	boolean isCopy;
	const jchar *jChars = env->GetStringChars(objectString, &isCopy);
	//printf("mama jchars\n");
	jsize length = env->GetStringLength(objectString);
	//printf("mama length\n");
	WCHAR *strCopy = new WCHAR[length+1];
	for (int i = 0; i < length; i++)
	{
		strCopy[i] = jChars[i];
	}
	//printf("po for\n");

	strCopy[length] = 0;
	if (isCopy == JNI_TRUE)
	{
		//printf("sttring release copy\n");
		env->ReleaseStringChars(objectString, jChars);
	}
	//wprintf(L"String:%d:%s:--\n", length, strCopy);
	return strCopy;
}

void CheckSuccess(JNIEnv* env, HRESULT hr, LPWSTR exceptionMessage)
{
	if (!SUCCEEDED(hr))
	{
		ThrowCOMException(env, exceptionMessage, hr);
	}
}