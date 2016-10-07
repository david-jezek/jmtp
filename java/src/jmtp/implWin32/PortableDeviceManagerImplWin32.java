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

import be.derycke.pieter.com.COM;
import be.derycke.pieter.com.COMException;
import be.derycke.pieter.com.COMReference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jmtp.PortableDevice;
import jmtp.PortableDeviceManager;

/**
 * TODO phantomreferences -> gebruiken
 * @author Pieter De Rycke
 */
public class PortableDeviceManagerImplWin32 implements PortableDeviceManager {
    
	
	private static final Logger logger = LogManager.getLogger(PortableDeviceManagerImplWin32.class);
	
	static{
		String archDataModel = System.getProperty("sun.arch.data.model"); //$NON-NLS-1$
	    System.loadLibrary("jmtp"+archDataModel); //$NON-NLS-1$
	}

    
    private COMReference pDeviceManager;
    
    private Map<String, PortableDeviceImplWin32> deviceMap;
    
    /**
     * constructor
     */
    public PortableDeviceManagerImplWin32() {
        try {
            pDeviceManager = COM.CoCreateInstance(WPDImplWin32.CLSID_PortableDeviceManager, 
                    0, COM.CLSCTX_INPROC_SERVER, WPDImplWin32.IID_IPortableDeviceManager);
            deviceMap = new HashMap<>();
        }
        catch (Exception e) {
            throw new RuntimeException("probleem met de com", e); //$NON-NLS-1$
        }
    }
    
    private native String[] getDevicesImpl() throws COMException;
    
    private native void refreshDeviceListImpl() throws COMException;
    
    @Override
    public PortableDeviceImplWin32[] getDevices() {
        try {
            String[] devices = getDevicesImpl();

            Set<String> deviceSet = new HashSet<>();
            for (String deviceID : devices) {
                if(!deviceMap.containsKey(deviceID))
                    deviceMap.put(deviceID, new PortableDeviceImplWin32(pDeviceManager, deviceID));
                              
                deviceSet.add(deviceID);
            }
            
            for(String deviceID : deviceMap.keySet())
                if(!deviceSet.contains(deviceID))
                    deviceMap.remove(deviceID);
            
            return deviceMap.values().toArray(new PortableDeviceImplWin32[0]);
        }
        catch (COMException e) {
            logger.error("Error geting device list.", e); //$NON-NLS-1$
            return new PortableDeviceImplWin32[0];
        }
    }
    
    @Override
    public void refreshDeviceList() {
        try {
            refreshDeviceListImpl();
        }
        catch(COMException e) {
        	logger.error("Error refresh device list.", e); //$NON-NLS-1$
        }
    }

    @Override
    public Iterator<PortableDevice> iterator() {
        return new PortableDeviceIterator();
    }
    
    private class PortableDeviceIterator implements Iterator<PortableDevice> {
        
        private Iterator<String> iterator;
        
        public PortableDeviceIterator() {
            getDevices();   //de map met devices updaten
            iterator = deviceMap.keySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public PortableDevice next() {
            String deviceID = iterator.next();
            return deviceMap.get(deviceID);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Devices can't be removed"); //$NON-NLS-1$
        }
        
    }
}
