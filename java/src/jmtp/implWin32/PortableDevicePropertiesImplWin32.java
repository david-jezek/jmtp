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

import jmtp.DeviceClosedException;
import jmtp.OperationUnsuccessfulException;
import jmtp.PropVariant;
import jmtp.PropertyKey;
import jmtp.Win32WPDDefines;
import be.derycke.pieter.com.COM;
import be.derycke.pieter.com.COMException;
import be.derycke.pieter.com.COMReference;

/**
 *
 * @author Pieter De Rycke
 */
class PortableDevicePropertiesImplWin32 {
    
//	@SuppressWarnings("unused")
	private COMReference pDeviceProperties;
    
    PortableDevicePropertiesImplWin32(COMReference pDeviceProperties) {
        this.pDeviceProperties = pDeviceProperties;
    }
    
    native PortableDeviceValuesImplWin32 getPropertyAttributes(String objectID,
            PropertyKey key) throws COMException;
    
    native PortableDeviceKeyCollectionImplWin32 getSupprtedProperties(String objectID) throws COMException;

    native PortableDeviceValuesImplWin32 getValues(String objectID,
            PortableDeviceKeyCollectionImplWin32 keys) throws COMException;
    
    native PortableDeviceValuesImplWin32 setValues(String objectID, 
    		PortableDeviceValuesImplWin32 values) throws COMException;

    private PortableDeviceKeyCollectionImplWin32 keyCollection;
    
    PortableDeviceKeyCollectionImplWin32 getKeyCollection() throws COMException{
	    if(keyCollection == null)
			keyCollection = new PortableDeviceKeyCollectionImplWin32();
		else		
			keyCollection.clear();
	    return keyCollection;
    }
    
    public PropVariant getValue(String objectID, PropertyKey key) throws COMException, DeviceClosedException{
    	try {
	        PortableDeviceKeyCollectionImplWin32 keyCollectionImplWin32 = getKeyCollection();
	        keyCollectionImplWin32.add(key);
	        PortableDeviceValuesImplWin32 values = getValues(objectID , keyCollectionImplWin32);
	        long count = values.count();
	        for(long i = 0; i < count; i++){
	        	KeyValuePair pair = values.getAt(i);
	        	if(pair.key.equals(key)){
	        		return pair.value;
	        	}
	        }
	        throw new OperationUnsuccessfulException("Returned list of values do not contains value with requested key."); //$NON-NLS-1$
    	}
    	catch(COMException e) {
    		if(e.getHresult() == COMException.E_POINTER) {
    			//there is no connection to the device
    			throw new DeviceClosedException("The device connection is closed.", e); //$NON-NLS-1$
    		}
    		else {
	    		throw e;
    		}
    	}
    	
    }

}
