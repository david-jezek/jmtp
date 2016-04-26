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
#include "jmtp_implWin32_PortableDeviceObjectOutputStream.h"

JNIEXPORT jint JNICALL Java_jmtp_implWin32_PortableDeviceObjectOutputStream_readDataToBuffer
(JNIEnv *env, jobject jThis, jbyteArray buf, jint byteCount)
{
	IStream* stream = (IStream *)RetrievePointerFromCOMReferenceable(env, jThis);
	BYTE* localBuf = new BYTE[byteCount];
	ULONG toWrite;
	env->GetByteArrayRegion(buf, 0, byteCount, (jbyte *)localBuf);
	HRESULT hr = stream->Write(localBuf, byteCount, &toWrite);
	if (FAILED(hr))
	{
		ThrowCOMException(env, L"Failed to write data into mobile device", hr);
		return -1;
	}
	return toWrite;
}


JNIEXPORT void JNICALL Java_jmtp_implWin32_PortableDeviceObjectOutputStream_closeIStream
(JNIEnv *env, jobject jThis)
{
	IStream* stream = (IStream *)RetrievePointerFromCOMReferenceable(env, jThis);
	stream->Release();
}

