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

import jmtp.PropertyKey;
import be.derycke.pieter.com.COMException;
import be.derycke.pieter.com.COMReference;

/**
 * 
 * @author David Jezek
 *
 */
public class PortableDeviceResourcesImplWin32 {

//	@SuppressWarnings("unused")
	private COMReference pDeviceResource;

	PortableDeviceResourcesImplWin32(COMReference pDeviceResource) {
        this.pDeviceResource = pDeviceResource;
    }

    /**
     * The Cancel method cancels a pending operation called on this interface
     * @throws be.derycke.pieter.com.COMException
     */
    native void cancel() throws COMException;

    native void delete(String objectID, PortableDeviceKeyCollectionImplWin32 resourcesKeys) throws COMException;
    
    native PortableDeviceKeyCollectionImplWin32 getSupportedResources(String objectID) throws COMException;
    
    native PortableDeviceValuesImplWin32 getResourceAttributes(String objectID, PropertyKey resourceKey) throws COMException;
    
    native PortableDeviceObjectInputStream getInputStream(String objectID, PropertyKey resourceKey) throws COMException;
    
    native PortableDeviceObjectOutputStream getOutputStream(String objectID, PropertyKey resourceKey) throws COMException;
}
