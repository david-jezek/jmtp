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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jmtp.PortableDeviceAudioObject;
import jmtp.PortableDeviceContainerObject;
import jmtp.PortableDeviceFolderObject;
import jmtp.PortableDeviceObject;
import jmtp.PortableDevicePlaylistObject;
import jmtp.PortableDeviceStorageObject;
import jmtp.PropVariant;
import jmtp.Win32WPDDefines;
import be.derycke.pieter.com.COMException;
import be.derycke.pieter.com.OleDate;

//gemeenschappelijke klasse voor storage en folder
abstract class AbstractPortableDeviceContainerImplWin32 extends
		PortableDeviceObjectImplWin32 implements PortableDeviceContainerObject{

	AbstractPortableDeviceContainerImplWin32(String objectID,
			PortableDeviceImplWin32 device) {

		super(objectID, device);
	}

	public PortableDeviceObject[] getChildObjects() {
		try {
			String[] childIDs = device.getContent().listChildObjects(objectID);
			PortableDeviceObject[] objects = new PortableDeviceObject[childIDs.length];
			for (int i = 0; i < childIDs.length; i++)
				objects[i] = WPDImplWin32.convertToPortableDeviceObject(
						childIDs[i], device);

			return objects;
		} catch (COMException e) {
			return new PortableDeviceObject[0];
		}
	}

	// TODO references ondersteuning nog toevoegen
	public PortableDevicePlaylistObject createPlaylistObject(String name,
			PortableDeviceObject[] references) {
		try {
			PortableDeviceValuesImplWin32 values = new PortableDeviceValuesImplWin32();
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_PARENT_ID,
					this.objectID);
			values.setStringValue(
					Win32WPDDefines.WPD_OBJECT_ORIGINAL_FILE_NAME, name
							+ ".pla");
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_NAME, name);
			values.setGuidValue(Win32WPDDefines.WPD_OBJECT_FORMAT,
					Win32WPDDefines.WPD_OBJECT_FORMAT_PLA);
			values.setGuidValue(Win32WPDDefines.WPD_OBJECT_CONTENT_TYPE,
					Win32WPDDefines.WPD_CONTENT_TYPE_PLAYLIST);

			if (references != null) {
				PortableDevicePropVariantCollectionImplWin32 propVariantCollection = new PortableDevicePropVariantCollectionImplWin32();
				for (PortableDeviceObject reference : references)
					propVariantCollection
							.add(new PropVariant(reference.getID()));
				values.setPortableDeviceValuesCollectionValue(
						Win32WPDDefines.WPD_OBJECT_REFERENCES,
						propVariantCollection);
			}

			return new PortableDevicePlaylistObjectImplWin32(device
					.getContent().createObjectWithPropertiesOnly(values),
					device);
		} catch (COMException e) {
			e.printStackTrace();
			return null;
		}
	}

	public PortableDeviceAudioObject addAudioObject(File file, String artist,
			String title, BigInteger duration) throws FileNotFoundException,
			IOException {

		return addAudioObject(file, artist, title, duration, null, null, null,
				-1);
	}

	public PortableDeviceAudioObject addAudioObject(File file, String artist,
			String title, BigInteger duration, String genre, String album,
			Date releaseDate, int track) throws FileNotFoundException,
			IOException {

		try {
			PortableDeviceValuesImplWin32 values = new PortableDeviceValuesImplWin32();
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_PARENT_ID,
					this.objectID);
			values.setStringValue(
					Win32WPDDefines.WPD_OBJECT_ORIGINAL_FILE_NAME,
					file.getName());
			values.setGuidValue(Win32WPDDefines.WPD_OBJECT_FORMAT,
					Win32WPDDefines.WPD_OBJECT_FORMAT_MP3); // TODO nog manier
															// vinden om type te
															// detecteren
			values.setGuidValue(Win32WPDDefines.WPD_OBJECT_CONTENT_TYPE,
					Win32WPDDefines.WPD_CONTENT_TYPE_AUDIO);
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_NAME, title);
			if (artist != null)
				values.setStringValue(Win32WPDDefines.WPD_MEDIA_ARTIST, artist);
			values.setUnsignedLargeIntegerValue(
					Win32WPDDefines.WPD_MEDIA_DURATION, duration);
			if (genre != null)
				values.setStringValue(Win32WPDDefines.WPD_MEDIA_GENRE, genre);
			if (album != null)
				values.setStringValue(Win32WPDDefines.WPD_MUSIC_ALBUM, album);
			if (releaseDate != null)
				values.setFloateValue(Win32WPDDefines.WPD_MEDIA_RELEASE_DATE,
						(float) new OleDate(releaseDate).toDouble());
			if (track >= 0)
				values.setUnsignedIntegerValue(Win32WPDDefines.WPD_MUSIC_TRACK,
						track);

			return new PortableDeviceAudioObjectImplWin32(device.getContent()
					.createObjectWithPropertiesAndData(values,
							new FileInputStream(file), file.length()), device);
		} catch (COMException e) {
			if (e.getHresult() == Win32WPDDefines.E_FILENOTFOUND)
				throw new FileNotFoundException("File " + file
						+ " was not found.");
			else {
				throw new IOException(e);
			}
		}
	}

	public PortableDeviceFolderObject createFolderObject(String name) {
		try {
			PortableDeviceValuesImplWin32 values = new PortableDeviceValuesImplWin32();
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_PARENT_ID,
					this.objectID);
			values.setStringValue(
					Win32WPDDefines.WPD_OBJECT_ORIGINAL_FILE_NAME, name);
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_NAME, name);
			values.setGuidValue(Win32WPDDefines.WPD_OBJECT_CONTENT_TYPE,
					Win32WPDDefines.WPD_CONTENT_TYPE_FOLDER);

			return new PortableDeviceFolderObjectImplWin32(device.getContent()
					.createObjectWithPropertiesOnly(values), device);
		} catch (COMException e) {
			e.printStackTrace();
			return null;
		}
	}

	public PortableDeviceObject createFileObject(String name) {
		try {
			PortableDeviceValuesImplWin32 values = new PortableDeviceValuesImplWin32();
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_PARENT_ID,
					this.objectID);
			values.setStringValue(
					Win32WPDDefines.WPD_OBJECT_ORIGINAL_FILE_NAME, name);
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_NAME, name);
			values.setGuidValue(Win32WPDDefines.WPD_OBJECT_CONTENT_TYPE,
					Win32WPDDefines.WPD_CONTENT_TYPE_GENERIC_FILE);

			return new PortableDeviceFolderObjectImplWin32(device.getContent()
					.createObjectWithPropertiesOnly(values), device);
		} catch (COMException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jmtp.PortableDeviceFolderObject#createFileObjectWithContent(java.lang
	 * .String, java.io.InputStream)
	 */
	@Override
	public PortableDeviceObject createFileObjectWithContent(String name,
			InputStream content, long size) {
		try {
			PortableDeviceValuesImplWin32 values = new PortableDeviceValuesImplWin32();
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_PARENT_ID,
					this.objectID);
			values.setStringValue(
					Win32WPDDefines.WPD_OBJECT_ORIGINAL_FILE_NAME, name);
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_NAME, name);
			values.setGuidValue(Win32WPDDefines.WPD_OBJECT_CONTENT_TYPE,
					Win32WPDDefines.WPD_CONTENT_TYPE_GENERIC_FILE);

			return new PortableDeviceFolderObjectImplWin32(device.getContent()
					.createObjectWithPropertiesAndData(values, content, size), device);
		} catch (COMException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see jmtp.PortableDeviceContainerObject#findFile(java.lang.String)
	 */
	@Override
	public List<PortableDeviceObject> findFile(String... relativPath) {
		List<PortableDeviceObject> result = new ArrayList<PortableDeviceObject>();
		findFile(relativPath, 0, result);
		return result;
	}

	public void findFile(String[] relativPathsNames, int startIndex, List<PortableDeviceObject> result) {
		PortableDeviceObject[] objectsList = getChildObjects();
		boolean found = false;
		PortableDeviceObject obj = null;
		for (PortableDeviceObject o : objectsList) {
			if (o.getOriginalFileName().matches(relativPathsNames[startIndex])) {
				if(startIndex  == relativPathsNames.length - 1){
					result.add(o);
				} else {
					if(o instanceof PortableDeviceContainerObject){
						PortableDeviceContainerObject container = (PortableDeviceContainerObject)o;
						container.findFile(relativPathsNames, startIndex+1, result);
					}
				}
			}
		}
	}

}
