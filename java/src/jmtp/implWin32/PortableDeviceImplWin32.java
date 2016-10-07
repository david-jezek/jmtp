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

import java.util.Date;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import jmtp.DeviceAlreadyOpenedException;
import jmtp.DeviceClosedException;
import jmtp.PortableDevice;
import jmtp.PortableDeviceAudioObject;
import jmtp.PortableDeviceManager;
import jmtp.PortableDeviceManagerFactory;
import jmtp.PortableDeviceObject;
import jmtp.PortableDeviceType;
import jmtp.PowerSource;
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
class PortableDeviceImplWin32 implements PortableDevice {

	private String deviceID;
	private String friendlyName;
	// @SuppressWarnings("unused")
	private COMReference pDeviceManager;
	// @SuppressWarnings("unused")
	private COMReference pDevice;

	private PortableDeviceContentImplWin32 content;
	private PortableDevicePropertiesImplWin32 properties;
	private PortableDeviceResourcesImplWin32 resources;
	private PortableDeviceKeyCollectionImplWin32 keyCollection;

	private boolean opened = false;

	PortableDeviceImplWin32(COMReference pDeviceManager, String deviceID) {
		this.pDeviceManager = pDeviceManager;
		this.deviceID = deviceID;
		try {
			pDevice = COM.CoCreateInstance(WPDImplWin32.CLSID_PortableDevice,
					0, COM.CLSCTX_INPROC_SERVER,
					WPDImplWin32.IID_IPortableDevice);
		} catch (COMException e) {
			e.printStackTrace();
		}
	}

	/*
	 * In c++ ge�mplementeerde methoden
	 */
	private native String getDeviceFriendlyName(String deviceID)
			throws COMException;

	private native String getDeviceManufacturer(String deviceID)
			throws COMException;

	private native String getDeviceDescription(String deviceID)
			throws COMException;

	private native void openImpl(PortableDeviceValuesImplWin32 values)
			throws COMException;

	private native void closeImpl() throws COMException;

	private native PortableDeviceContentImplWin32 getDeviceContent()
			throws COMException;

	private native PortableDeviceResourcesImplWin32 getDeviceResources()
			throws COMException;

	public native PortableDeviceValuesImplWin32 sendCommand(
			PortableDeviceValuesImplWin32 values) throws COMException;

	/*
	 * In Java ge�mplementeerde methoden
	 */
	// private void createStructures() throws COMException {
	// if(content == null)
	// content = getDeviceContent();
	//
	// if(properties == null)
	// properties = content.getProperties();
	//
	// if(keyCollection == null)
	// keyCollection = new PortableDeviceKeyCollectionImplWin32();
	// else
	// keyCollection.clear();
	// }

	PortableDeviceKeyCollectionImplWin32 getKeyCollection() throws COMException {
		if (keyCollection == null)
			keyCollection = new PortableDeviceKeyCollectionImplWin32();
		else
			keyCollection.clear();
		return keyCollection;
	}

	PortableDeviceContentImplWin32 getContent() throws COMException {
		if (content == null) {
			content = getDeviceContent();
		}
		return content;
	}

	PortableDeviceResourcesImplWin32 getResources() throws COMException {
		if (resources == null) {
			resources = getDeviceResources();
		}
		return resources;
	}

	PortableDevicePropertiesImplWin32 getProperties() throws COMException {
		if (properties == null) {
			properties = getContent().getProperties();
		}
		return properties;
	}

	private String retrieveStringValue(PropertyKey key) throws COMException {
		try {
			PortableDeviceKeyCollectionImplWin32 keyCollectionImplWin32 = getKeyCollection();
			keyCollectionImplWin32.add(key);
			return getProperties().getValues(
					Win32WPDDefines.WPD_DEVICE_OBJECT_ID,
					keyCollectionImplWin32).getStringValue(key);
		} catch (COMException e) {
			if (e.getHresult() == COMException.E_POINTER) {
				// there is no connection to the device
				throw new DeviceClosedException(
						"The device connection is closed."); //$NON-NLS-1$
			} else {
				throw e;
			}
		}
	}

	private long retrieveUnsignedIntegerValue(PropertyKey key)
			throws COMException {
		try {
			PortableDeviceKeyCollectionImplWin32 keyCollectionImplWin32 = getKeyCollection();
			keyCollectionImplWin32.add(key);
			return getProperties().getValues(
					Win32WPDDefines.WPD_DEVICE_OBJECT_ID,
					keyCollectionImplWin32).getUnsignedIntegerValue(key);
		} catch (COMException e) {
			if (e.getHresult() == COMException.E_POINTER) {
				// there is no connection to the device
				throw new DeviceClosedException(
						"The device connection is closed."); //$NON-NLS-1$
			} else {
				throw e;
			}
		}
	}

	private boolean retrieveBooleanValue(PropertyKey key) throws COMException {
		try {
			PortableDeviceKeyCollectionImplWin32 keyCollectionImplWin32 = getKeyCollection();
			keyCollectionImplWin32.add(key);
			return getProperties().getValues(
					Win32WPDDefines.WPD_DEVICE_OBJECT_ID,
					keyCollectionImplWin32).getBoolValue(key);
		} catch (COMException e) {
			if (e.getHresult() == COMException.E_POINTER)
				throw new DeviceClosedException(
						"The device connection is closed."); //$NON-NLS-1$
			else
				throw e;
		}
	}

	public synchronized String getFriendlyName() {
		if (friendlyName == null) {
			boolean wasOpened = opened;
			if (!opened) {
				System.out.println("fr name open"); //$NON-NLS-1$
				open();
			}
			try {
				friendlyName = getDeviceFriendlyName(deviceID);
				if (!wasOpened) {
					System.out.println("fr name close"); //$NON-NLS-1$
					close();
				}
			} catch (COMException e) {
				friendlyName = null;
			}
		}
		return friendlyName;
	}

	public String getManufacturer() {
		try {
			return getDeviceManufacturer(deviceID);
		} catch (COMException e) {
			return null;
		}
	}

	public String getDescription() {
		try {
			return getDeviceDescription(deviceID);
		} catch (COMException e) {
			return null;
		}
	}

	public void open(String appName, int appMajor, int appMinor, int appRevision) {
		try {
			PortableDeviceValuesImplWin32 values = new PortableDeviceValuesImplWin32();
			values.setStringValue(Win32WPDDefines.WPD_CLIENT_NAME, appName);
			values.setUnsignedIntegerValue(
					Win32WPDDefines.WPD_CLIENT_MAJOR_VERSION, appMajor);
			values.setUnsignedIntegerValue(
					Win32WPDDefines.WPD_CLIENT_MINOR_VERSION, appMinor);
			values.setUnsignedIntegerValue(Win32WPDDefines.WPD_CLIENT_REVISION,
					appRevision);

			openImpl(values);
		} catch (COMException e) {
			e.printStackTrace();
		}
	}

	public synchronized void open() {
		if (!opened) {
			try {
				openImpl(new PortableDeviceValuesImplWin32());
				opened = true;
			} catch (COMException e) {
				if (e.getHresult() == Win32WPDDefines.E_WPD_DEVICE_ALREADY_OPENED) {
					throw new DeviceAlreadyOpenedException(
							"The device connection has already been opened."); //$NON-NLS-1$
				} else {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void close() {
		try {
			closeImpl();
			opened = false;
		} catch (COMException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return deviceID;
	}

	public PortableDeviceObject[] getRootObjects() {
		try {
			PortableDeviceContentImplWin32 content = getDeviceContent();

			String[] childIDs = content
					.listChildObjects(Win32WPDDefines.WPD_DEVICE_OBJECT_ID);
			PortableDeviceObject[] objects = new PortableDeviceObject[childIDs.length];
			for (int i = 0; i < childIDs.length; i++)
				objects[i] = WPDImplWin32.convertToPortableDeviceObject(
						childIDs[i], this);

			return objects;
		} catch (COMException e) {
			if (e.getHresult() == COMException.E_POINTER) {
				// there is no connection to the device
				throw new DeviceClosedException(
						"The device connection is closed."); //$NON-NLS-1$
			} else {
				e.printStackTrace();
				return null;
			}
		}
	}

	public PortableDeviceObject[] getPortableDeviceObjectsFromPersistentUniqueIDs(
			String[] persistentUniqueIDs) {

		try {
			PortableDevicePropVariantCollectionImplWin32 persistentUniqueIDCollection = new PortableDevicePropVariantCollectionImplWin32();
			for (String persistentUniqueID : persistentUniqueIDs)
				persistentUniqueIDCollection.add(new PropVariant(
						persistentUniqueID));

			PortableDevicePropVariantCollectionImplWin32 objectIDCollection = getDeviceContent()
					.getObjectIDsFromPersistentUniqueIDs(
							persistentUniqueIDCollection);

			PortableDeviceObject[] result = new PortableDeviceObject[(int) objectIDCollection
					.count()];
			for (int i = 0; i < result.length; i++) {
				result[i] = WPDImplWin32.convertToPortableDeviceObject(
						(String) objectIDCollection.getAt(i).getValue(), this);
			}

			return result;
		} catch (COMException e) {
			if (e.getHresult() == COMException.E_POINTER) {
				// there is no connection to the device
				throw new DeviceClosedException(
						"The device connection is closed."); //$NON-NLS-1$
			} else {
				e.printStackTrace();
				return null;
			}
		}
	}

	public PortableDeviceObject getPortableDeviceObjectsFromPersistentUniqueIDs(
			String persistentUniqueID) {

		return getPortableDeviceObjectsFromPersistentUniqueIDs(new String[] { persistentUniqueID })[0];
	}

	public String getSerialNumber() {
		try {
			return retrieveStringValue(Win32WPDDefines.WPD_DEVICE_SERIAL_NUMBER);
		} catch (COMException e) {
			return null;
		}
	}

	public String getFirmwareVersion() {
		try {
			return retrieveStringValue(Win32WPDDefines.WPD_DEVICE_FIRMWARE_VERSION);
		} catch (COMException e) {
			return null;
		}
	}

	public String getModel() {
		try {
			return retrieveStringValue(Win32WPDDefines.WPD_DEVICE_MODEL);
		} catch (COMException e) {
			return null;
		}
	}

	public String getProtocol() {
		try {
			return retrieveStringValue(Win32WPDDefines.WPD_DEVICE_PROTOCOL);
		} catch (COMException e) {
			return null;
		}
	}

	public String getSyncPartner() {
		try {
			return retrieveStringValue(Win32WPDDefines.WPD_DEVICE_SYNC_PARTNER);
		} catch (COMException e) {
			return null;
		}
	}

	public int getPowerLevel() {
		try {
			// we can cast to an int because only values in the range [0-100]
			return (int) retrieveUnsignedIntegerValue(Win32WPDDefines.WPD_DEVICE_POWER_LEVEL);
		} catch (COMException e) {
			return -1;
		}
	}

	public PortableDeviceType getType() {
		try {
			return PortableDeviceType.values()[(int) retrieveUnsignedIntegerValue(Win32WPDDefines.WPD_DEVICE_TYPE)];
		} catch (COMException e) {
			return PortableDeviceType.GENERIC;
		}
	}

	public PowerSource getPowerSource() {
		try {
			return PowerSource.values()[(int) retrieveUnsignedIntegerValue(Win32WPDDefines.WPD_DEVICE_POWER_SOURCE)];
		} catch (COMException e) {
			return PowerSource.BATTERY;
		}
	}

	public boolean isNonConsumableSupported() {
		try {
			return retrieveBooleanValue(Win32WPDDefines.WPD_DEVICE_SUPPORTS_NON_CONSUMABLE);
		} catch (COMException e) {
			return false;
		}
	}

	public static void main(String[] args) {
		String id = "{00276159-0000-0000-0000-000000000000}"; //$NON-NLS-1$
		@SuppressWarnings("unused")
		String wmp_id = "{00128F26-0000-0000-0000-000000000000}"; //$NON-NLS-1$

		PortableDeviceManager manager;
		PortableDevice device;
		PortableDeviceImplWin32 device32;
		PortableDeviceValuesImplWin32 input;
		PortableDeviceValuesImplWin32 results;
		COMException wpdError;
		long driverError;

		manager = PortableDeviceManagerFactory.getPortableDeviceManager();
		device = manager.getDevices()[0];
		device.open();

		PropertyKey commandKey = Win32WPDDefines.WPD_COMMAND_COMMON_RESET_DEVICE;

		device32 = (PortableDeviceImplWin32) device;
		try {
			input = new PortableDeviceValuesImplWin32();
			input.setGuidValue(
					Win32WPDDefines.WPD_PROPERTY_COMMON_COMMAND_CATEGORY,
					commandKey.getFmtid());
			input.setUnsignedIntegerValue(
					Win32WPDDefines.WPD_PROPERTY_COMMON_COMMAND_ID,
					commandKey.getPid());

			results = device32.sendCommand(input);

			// check for success or failure to carry out the command
			try {
				wpdError = results
						.getErrorValue(Win32WPDDefines.WPD_PROPERTY_COMMON_HRESULT);
				System.out.println("Exception: "); //$NON-NLS-1$
				wpdError.printStackTrace();
			} catch (COMException e) {
				// ignore exception if "ERROR_NOT_FOUND" -> item not in
				// collection
				if (e.getHresult() != Win32WPDDefines.ERROR_NOT_FOUND) {
					System.out.println("Error: " + e.getErrorCode()); //$NON-NLS-1$
				}
			}

			// check driver-specific error code
			try {
				driverError = results
						.getUnsignedIntegerValue(Win32WPDDefines.WPD_PROPERTY_COMMON_DRIVER_ERROR_CODE);
				System.out.println("Driver Error Code: " + driverError); //$NON-NLS-1$
			} catch (COMException e) {
				// ignore exception if "ERROR_NOT_FOUND" -> item not in
				// collection
				if (e.getHresult() != Win32WPDDefines.ERROR_NOT_FOUND) {
					System.out.println("Error: " + e.getErrorCode()); //$NON-NLS-1$
				}
			}
		} catch (COMException e) {
			System.out.println("Error: " + e.getErrorCode()); //$NON-NLS-1$
		}

		/*
		 * PortableDeviceStorageObject storage =
		 * (PortableDeviceStorageObject)device.getRootObjects()[0];
		 * PortableDeviceObject o = storage.getChildObjects()[5]; o =
		 * ((PortableDeviceFolderObject)o).getChildObjects()[10]; o =
		 * ((PortableDeviceFolderObject)o).getChildObjects()[0]; o =
		 * ((PortableDeviceFolderObject)o).getChildObjects()[0];
		 * System.out.println(o.getOriginalFileName());
		 * System.out.println(o.getPersistentUniqueIdentifier());
		 * System.out.println();
		 * 
		 * Date datum = ((PortableDeviceAudioObject)o).getReleaseDate();
		 * ((PortableDeviceAudioObject)o).setReleaseDate(datum);
		 * System.out.println(((PortableDeviceAudioObject)o).getReleaseDate());
		 */

		PortableDeviceAudioObject o = (PortableDeviceAudioObject) device
				.getPortableDeviceObjectsFromPersistentUniqueIDs(id);
		System.out.println("Original File Name: " + o.getOriginalFileName()); //$NON-NLS-1$
		System.out.println("Duration: " + o.getDuraction()); //$NON-NLS-1$
		System.out.println("Size: " + o.getSize()); //$NON-NLS-1$
		System.out.println("Track Number: " + o.getTrackNumber()); //$NON-NLS-1$
		System.out.println("Duration: " + o.getDuraction()); //$NON-NLS-1$
		System.out.println("Use Count: " + o.getUseCount()); //$NON-NLS-1$

		// Date datum = Calendar.getInstance().getTime();
		Date datum = o.getReleaseDate();
		System.out.println(datum);
		o.setReleaseDate(datum);
		System.out.println("Release Date: " + o.getReleaseDate()); //$NON-NLS-1$

		/*
		 * PortableDeviceStorageObject storage =
		 * (PortableDeviceStorageObject)device.getRootObjects()[0];
		 * System.out.println("File System Type: " +
		 * storage.getFileSystemType()); System.out.println("Description: " +
		 * storage.getDescription()); System.out.println("Serial Number: " +
		 * storage.getSerialNumber()); System.out.println("Capacity: " +
		 * storage.getCapacity()); System.out.println("Free Space : " +
		 * storage.getFreeSpace()); System.out.println("Type : " +
		 * storage.getType());
		 */
		device.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtp.PortableDevice#getId()
	 */
	@Override
	public String getId() {
		return deviceID;
	}

	/* (non-Javadoc)
	 * @see jmtp.PortableDevice#findObjectById(java.lang.String)
	 */
	@Override
	public PortableDeviceObject findObjectById(String id) {
		return WPDImplWin32.convertToPortableDeviceObject(id, this);
	}
}
