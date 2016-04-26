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
#include <Stringapiset.h>

#include "jmtp.h"
#include "jmtp_implWin32_PortableDeviceObjectInputStream.h"

JNIEXPORT jint JNICALL Java_jmtp_implWin32_PortableDeviceObjectInputStream_readDataToBuffer
(JNIEnv *env, jobject jThis, jbyteArray buf, jint bufSize) 
{
	IStream* stream = (IStream *)RetrievePointerFromCOMReferenceable(env, jThis);
	BYTE* localBuf = new BYTE[bufSize];
	ULONG readed;
	HRESULT hr = stream->Read(localBuf, bufSize, &readed);
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Failed to read data from mobile device", hr);
		return -1;
	}
	env->SetByteArrayRegion(buf, 0, readed, (jbyte *)localBuf);
	return readed;
}


JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceObjectInputStream_closeIStream
(JNIEnv *env, jobject jThis)
{
	IStream* stream = (IStream *)RetrievePointerFromCOMReferenceable(env, jThis);
	stream->Release();
}

