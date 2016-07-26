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

package jmtp.implWin32;

import java.io.IOException;
import java.io.OutputStream;

import be.derycke.pieter.com.COMReference;
import be.derycke.pieter.com.COMReferenceable;

/**
 * @author David Jezek
 *
 */
public class PortableDeviceObjectOutputStream extends OutputStream implements COMReferenceable {

	private byte[] buf;
	private int bufPointer = -1;
	private int bufEndPosition = 0;
	private COMReference iStream;
	private boolean isClosed = false;
	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	
	protected void createBuffer(int size){
		buf = new byte[size];
	}
	
	@Override
	public void write(int b) throws IOException {
		if(!(bufPointer + 1 < bufEndPosition)){
			flush();
		}
		if(bufPointer + 1 < bufEndPosition){
			bufPointer++;
			buf[bufPointer]  = (byte)(b & 0xFF);
		}
	}
	
	public PortableDeviceObjectOutputStream(COMReference iStream) {
		super();
		this.iStream = iStream;
	}

	
	@Override
	public void flush() throws IOException {
		super.flush();
		bufEndPosition = writeDataIStream(buf, buf.length);
		bufPointer = -1;
	}

	private native int writeDataIStream(byte[] buf, int byteCount);
	
	public PortableDeviceObjectOutputStream(COMReference iStream, int bufSize) {
		super();
		this.iStream = iStream;
		buf = new byte[bufSize];
		bufPointer = -1;
	}
	@Override
	public void close() throws IOException {
		flush();
		super.close();
		synchronized(this){
			if(!isClosed){
				closeIStream();
				isClosed = true;
			}
		}
	}
	private native void closeIStream();

	/* (non-Javadoc)
	 * @see be.derycke.pieter.com.COMReferenceable#getReference()
	 */
	@Override
	public COMReference getReference() {
		return iStream;
	}

}
