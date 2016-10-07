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
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import be.derycke.pieter.com.COMException;
import be.derycke.pieter.com.Guid;
import be.derycke.pieter.com.OleDate;
import jmtp.ObjectProperty;
import jmtp.ObjectResource;
import jmtp.OperationUnsuccessfulException;
import jmtp.PortableDeviceObject;
import jmtp.PropVariant;
import jmtp.PropertyKey;
import jmtp.Win32WPDDefines;

/**
 *
 * @author Pieter De Rycke
 */
class PortableDeviceObjectImplWin32 implements PortableDeviceObject {
    
	
	private static final Logger logger = LogManager.getLogger(PortableDeviceObjectImplWin32.class);
	
    protected PortableDeviceImplWin32 device;
    private PortableDeviceKeyCollectionImplWin32 keyCollection;
    private PortableDeviceValuesImplWin32 valuesImpl;
    
    protected String objectID;
    
    PortableDeviceObjectImplWin32(String objectID, 
            PortableDeviceImplWin32 device) {
        this.objectID = objectID;
        this.device = device;
        
    }

    public PortableDeviceImplWin32 getDevice() {
		return device;
	}

	protected PortableDeviceKeyCollectionImplWin32 getKeyCollection() throws COMException{
	    if(keyCollection == null)
	    	keyCollection = new PortableDeviceKeyCollectionImplWin32();
		else		
			keyCollection.clear();
	    return keyCollection;
    }
    
    protected PortableDeviceValuesImplWin32 getValues() throws COMException{
	    if(valuesImpl == null)
	    	valuesImpl = new PortableDeviceValuesImplWin32();
		else		
			valuesImpl.clear();
	    return valuesImpl;
    }


    /**
     * Een String property opvragen.
     * @param key
     * @return
     */
    protected String retrieveStringValue(PropertyKey key) {
    	try {
    		PortableDeviceKeyCollectionImplWin32 keys = getKeyCollection();
            keys.add(key);
            return device.getProperties().getValues(objectID, keys).
                    getStringValue(key);
        }
        catch(COMException e) {
        	if(e.getHresult() == Win32WPDDefines.ERROR_NOT_FOUND)
        		return null;
        	else if(e.getHresult() == Win32WPDDefines.ERROR_NOT_SUPPORTED){
        		throw new UnsupportedOperationException("Couldn't retrieve the specified property."); //$NON-NLS-1$
        	} else {
        		logger.error(String.format("Errror in retrieveStringValue. HRESULT: 0x%x", e.getHresult()), e); //$NON-NLS-1$
        	}
    		return null;
        }
    }
    
    protected void changeStringValue(PropertyKey key, String value) {
    	try {
    		PortableDeviceValuesImplWin32 values = getValues();
    		values.setStringValue(key, value);
    		PortableDeviceValuesImplWin32 results = device.getProperties().setValues(objectID, values);
    		if(results.count() > 0 
    				&& results.getErrorValue(key).getHresult() != COMException.S_OK) {
    			throw new UnsupportedOperationException("Couldn't change the property."); //$NON-NLS-1$
    		}
    	}
    	catch(COMException e) {
    		e.printStackTrace();
    	}
    }
    
    protected long retrieveLongValue(PropertyKey key) {
    	try {
    		PortableDeviceKeyCollectionImplWin32 keys = getKeyCollection();
            keys.add(key);
            return device.getProperties().getValues(objectID, keys).getUnsignedIntegerValue(key);
        }
        catch(COMException e) {
        	if(e.getHresult() == Win32WPDDefines.ERROR_NOT_FOUND)
        		return -1;
        	else if(e.getHresult() == Win32WPDDefines.ERROR_NOT_SUPPORTED)
        		throw new UnsupportedOperationException("Couldn't retrieve the specified property."); //$NON-NLS-1$
        	else {
        		e.printStackTrace();
        		return -1;
        	}
        }
    }
    
    protected void changeLongValue(PropertyKey key, long value) {
    	try {
    		PortableDeviceValuesImplWin32 values = getValues();
    		values.setUnsignedIntegerValue(key, value);
    		PortableDeviceValuesImplWin32 results = device.getProperties().setValues(objectID, values);
    		if(results.count() > 0 
    				&& results.getErrorValue(key).getHresult() != COMException.S_OK) {
    			throw new UnsupportedOperationException("Couldn't change the property."); //$NON-NLS-1$
    		}
    	}
    	catch(COMException e) {
    		e.printStackTrace();
    	}
    }
    
    protected Date retrieveDateValue(PropertyKey key) {
    	try {
    		PortableDeviceKeyCollectionImplWin32 keys = getKeyCollection();
            keys.add(key);
            return new OleDate(device.getProperties().getValues(objectID, keys).getFloatValue(key));
    	}
    	catch(COMException e) {
    		return null;
    	}
    }
    
    protected void changeDateValue(PropertyKey key, Date value) {
    	try {
    		PortableDeviceValuesImplWin32 values = getValues();
    		values.setFloateValue(key, (float)new OleDate(value).toDouble());
    		PortableDeviceValuesImplWin32 results = device.getProperties().setValues(objectID, values);
    		if(results.count() > 0 
    				&& results.getErrorValue(key).getHresult() != COMException.S_OK) {
    			throw new UnsupportedOperationException("Couldn't change the property."); //$NON-NLS-1$
    		}
    	}
    	catch(COMException e) {}
    }
    
    protected boolean retrieveBooleanValue(PropertyKey key) {
    	try {
    		PortableDeviceKeyCollectionImplWin32 keys = getKeyCollection();
            keys.add(key);
            return device.getProperties().getValues(objectID, keys).getBoolValue(key);
    	}
    	catch(COMException e) {
    		return false;
    	}
    }
    
    protected Guid retrieveGuidValue(PropertyKey key) {
    	try {
    		PortableDeviceKeyCollectionImplWin32 keys = getKeyCollection();
            keys.add(key);
            return device.getProperties().getValues(objectID, keys).getGuidValue(key);
    	}
    	catch(COMException e) {
    		return null;
    	}
    }
    
    protected BigInteger retrieveBigIntegerValue(PropertyKey key) {
    	try {
    		PortableDeviceKeyCollectionImplWin32 keys = getKeyCollection();
            keys.add(key);
            return device.getProperties().getValues(objectID, keys).
                    getUnsignedLargeIntegerValue(key);
        }
        catch(COMException e) {
        	if(e.getHresult() == Win32WPDDefines.ERROR_NOT_FOUND)
        		return new BigInteger("-1"); //$NON-NLS-1$
        	else if(e.getHresult() == Win32WPDDefines.ERROR_NOT_SUPPORTED)
        		throw new UnsupportedOperationException("Couldn't retrieve the specified property."); //$NON-NLS-1$
        	else {
	        	e.printStackTrace();
	            return null;	//comexception -> de string werd niet ingesteld
        	}
        }
    }
    
    protected void changeBigIntegerValue(PropertyKey key, BigInteger value) {
    	try {
    		PortableDeviceValuesImplWin32 values = getValues();
    		values.setUnsignedLargeIntegerValue(key, value);
    		PortableDeviceValuesImplWin32 results = device.getProperties().setValues(objectID, values);
    		if(results.count() > 0 
    				&& results.getErrorValue(key).getHresult() != COMException.S_OK) {
    			throw new UnsupportedOperationException("Couldn't change the property."); //$NON-NLS-1$
    		}
    	}
    	catch(COMException e) {
    		e.printStackTrace();
    	}
    }
    
    public String getID() {
    	return objectID;
    }
    
    public String getName() {
    	return retrieveStringValue(Win32WPDDefines.WPD_OBJECT_NAME);
    }

    public String getOriginalFileName() {
    	return retrieveStringValue(Win32WPDDefines.WPD_OBJECT_ORIGINAL_FILE_NAME);
    }
    
    public boolean canDelete() {
    	return retrieveBooleanValue(Win32WPDDefines.WPD_OBJECT_CAN_DELETE);
    }

    public boolean isHidden() {
        return retrieveBooleanValue(Win32WPDDefines.WPD_OBJECT_ISHIDDEN);
    }

    public boolean isSystemObject() {
        return retrieveBooleanValue(Win32WPDDefines.WPD_OBJECT_ISSYSTEM);
    }

    public Date getDateModified() {
        return retrieveDateValue(Win32WPDDefines.WPD_OBJECT_DATE_MODIFIED);
    }
    
    public Date getDateCreated() {
    	return retrieveDateValue(Win32WPDDefines.WPD_OBJECT_DATE_CREATED);
    }
    
    public Date getDateAuthored() {
    	return retrieveDateValue(Win32WPDDefines.WPD_OBJECT_DATE_AUTHORED);
    }

    public PortableDeviceObject getParent() {
    	String parentID = retrieveStringValue(Win32WPDDefines.WPD_OBJECT_PARENT_ID);
    	if(parentID != null)
    		return WPDImplWin32.convertToPortableDeviceObject(parentID, device);
    	else
    		return null;
    }

    public BigInteger getSize() {
        return retrieveBigIntegerValue(Win32WPDDefines.WPD_OBJECT_SIZE);
    }

    public String getPersistentUniqueIdentifier() {
    	return retrieveStringValue(Win32WPDDefines.WPD_OBJECT_PERSISTENT_UNIQUE_ID);
    }
    
	public boolean isDrmProtected() {
		return retrieveBooleanValue(Win32WPDDefines.WPD_OBJECT_IS_DRM_PROTECTED);
	}
	
	public String getSyncID() {
		return retrieveStringValue(Win32WPDDefines.WPD_OBJECT_SYNC_ID);
	}
	
	//TODO slechts tijdelijk de guids geven -> enum aanmaken
	public Guid getFormat() {
		return retrieveGuidValue(Win32WPDDefines.WPD_OBJECT_FORMAT);
	}
	
	public void setSyncID(String value) {
		changeStringValue(Win32WPDDefines.WPD_OBJECT_SYNC_ID, value);
	}
	
	public void delete() throws OperationUnsuccessfulException {
		try {
			PortableDevicePropVariantCollectionImplWin32 collection = 
				new PortableDevicePropVariantCollectionImplWin32();
			collection.add(new PropVariant(this.objectID));
			this.device.getContent().delete(Win32WPDDefines.PORTABLE_DEVICE_DELETE_NO_RECURSION, collection);
		}
		catch(COMException e) {
			throw new OperationUnsuccessfulException("Object cannot be deleted.", e); //$NON-NLS-1$
		}
	}

    
    @Override
    public String toString() {
        return objectID;
    }
    
    public boolean equals(Object o) {
        if(o instanceof PortableDeviceObjectImplWin32) {
            PortableDeviceObjectImplWin32 object = (PortableDeviceObjectImplWin32)o;
            return object.objectID.equals(this.objectID);
        }
        else
            return false;
    }

	@Override
	public Collection<ObjectProperty> getSupportedProperties() throws OperationUnsuccessfulException{
		try {
			PortableDeviceKeyCollectionImplWin32 collection = device.getProperties().getSupprtedProperties(objectID);
			return ObjectPropertyWin32Impl.getProperties(this, collection);
		} catch (COMException e) {
			throw new OperationUnsuccessfulException("Collection of supported properties cannot be retrieved from device.", e);  //$NON-NLS-1$
		}
	}

	@Override
	public Collection<ObjectResource> getSupportedResources() throws OperationUnsuccessfulException {
		try {
			device.getResources().getSupportedResources(objectID);
			PortableDeviceKeyCollectionImplWin32 propertyKes = device.getResources().getSupportedResources(objectID);
			return ObjectResourceWin32Impl.getResources(this, propertyKes);
		} catch (COMException e) {
			throw new OperationUnsuccessfulException("Collection of supported resources cannot be retrieved from device.", e);  //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see jmtp.PortableDeviceObject#updateContent(java.io.InputStream)
	 */
	@Override
	public void updateContent(InputStream newContent, long size) throws OperationUnsuccessfulException{
		try {
			PortableDeviceValuesImplWin32 values = new PortableDeviceValuesImplWin32();
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_PARENT_ID,
					this.getParent().getID());
			values.setStringValue(
					Win32WPDDefines.WPD_OBJECT_ORIGINAL_FILE_NAME, getOriginalFileName());
			values.setStringValue(Win32WPDDefines.WPD_OBJECT_NAME, getName());
			values.setGuidValue(Win32WPDDefines.WPD_OBJECT_CONTENT_TYPE,
					Win32WPDDefines.WPD_CONTENT_TYPE_GENERIC_FILE);
			PortableDevicePropVariantCollectionImplWin32 objectIds = new PortableDevicePropVariantCollectionImplWin32();
			String obId = getID();
			System.out.println(obId);
			PropVariant pv = new PropVariant(obId);
			objectIds.add(pv);
			device.getContent().delete(0, objectIds);
			System.out.println("deleted"); //$NON-NLS-1$
			this.objectID = device.getContent().createObjectWithPropertiesAndData(values, newContent, size);
		} catch (COMException e) {
			throw new OperationUnsuccessfulException("Update content unsucessfull.", e); //$NON-NLS-1$
		}
	}
	
	
}
