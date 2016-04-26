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

import jmtp.PortableDeviceObject;
import jmtp.PortableDevicePlaylistObject;
import jmtp.Win32WPDDefines;
import be.derycke.pieter.com.COMException;

class PortableDevicePlaylistObjectImplWin32 extends PortableDeviceObjectImplWin32 
	implements PortableDevicePlaylistObject{
	
	PortableDevicePlaylistObjectImplWin32(String objectID, PortableDeviceImplWin32 device) {
        
        super(objectID, device);
	}

	@Override
	public PortableDeviceObject[] getReferences() {
		try {
            PortableDeviceKeyCollectionImplWin32 keyCollection = getKeyCollection();
            keyCollection.add(Win32WPDDefines.WPD_OBJECT_REFERENCES);
            PortableDevicePropVariantCollectionImplWin32 propVariantCollection = 
            	device.getProperties().getValues(objectID, keyCollection).
            	getPortableDeviceValuesCollectionValue(Win32WPDDefines.WPD_OBJECT_REFERENCES);
            
            //long naar int? misschien een probleem bij *enorm* grote afspeellijsten, 
            //maar kan normaal wel niet gebeuren
            PortableDeviceObject[] references = 
            	new PortableDeviceObjectImplWin32[(int)propVariantCollection.count()];
            for(int i = 0; i < references.length; i++) {
            	references[i] = WPDImplWin32.convertToPortableDeviceObject(
            			(String)propVariantCollection.getAt(i).getValue(), device);
            }
            return references;
            	
        }
        catch(COMException e) {
            return new PortableDeviceObject[0];
        }
	}

}
