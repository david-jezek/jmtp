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

package jmtp.implWin32;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jmtp.PortableDeviceFolderObject;
import jmtp.PortableDeviceObject;
import jmtp.PortableDeviceStorageObject;
import jmtp.PropVariant;
import jmtp.Win32WPDDefines;
import be.derycke.pieter.com.COMException;

/**
 *
 * @author Pieter De Rycke
 */
class PortableDeviceFolderObjectImplWin32 extends AbstractPortableDeviceContainerImplWin32 implements PortableDeviceFolderObject{

    public PortableDeviceFolderObjectImplWin32(String objectID, PortableDeviceImplWin32 device) {
        
        super(objectID, device);
    }
    
    public PortableDeviceObject[] getChildObjects() {
        try {
            String[] childIDs = device.getContent().listChildObjects(objectID);
            PortableDeviceObject[] objects = new PortableDeviceObject[childIDs.length];
            for(int i = 0; i < childIDs.length; i++)
            	objects[i] = WPDImplWin32.convertToPortableDeviceObject(childIDs[i], device);
            
            return objects;
        }
        catch (COMException e) {
            return new PortableDeviceObject[0];
        }
    }
    
    public void delete(boolean recursive) {
		try {
			PortableDevicePropVariantCollectionImplWin32 collection = 
				new PortableDevicePropVariantCollectionImplWin32();
			collection.add(new PropVariant(this.objectID));
			this.device.getContent().delete(Win32WPDDefines.PORTABLE_DEVICE_DELETE_WITH_RECURSION, collection);
		}
		catch(COMException e) {
			//TODO -> misschien een exception gooien?
			e.printStackTrace();
		}
    }

}
