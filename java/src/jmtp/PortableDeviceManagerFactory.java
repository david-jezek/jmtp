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

package jmtp;

import jmtp.implWin32.PortableDeviceManagerImplWin32;

/**
 * 
 * @author David Jezek
 *
 */
public class PortableDeviceManagerFactory {

	private static PortableDeviceManager deviceManager;
	
	public static PortableDeviceManager getPortableDeviceManager(){
		if(deviceManager == null){
	        if(System.getProperty("os.name").toLowerCase().contains("windows")) { //$NON-NLS-1$ //$NON-NLS-2$
	        	deviceManager = new PortableDeviceManagerImplWin32();
	        }
	        else
	            throw new RuntimeException("not supported os"); //$NON-NLS-1$

		}
		return deviceManager;
	}
	
}
