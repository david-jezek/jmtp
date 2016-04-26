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

import java.util.ArrayList;
import java.util.Collection;

import be.derycke.pieter.com.COMException;
import jmtp.DeviceClosedException;
import jmtp.ObjectProperty;
import jmtp.OperationUnsuccessfulException;
import jmtp.PortableDeviceObject;
import jmtp.PropVariant;
import jmtp.PropertyAttribute;
import jmtp.PropertyKey;

/**
 * 
 * @author David Jezek
 *
 */
public class ObjectPropertyWin32Impl implements ObjectProperty {

	private PortableDeviceObjectImplWin32 parent;
	private PropertyKey id;
	private PropVariant value;
	
	static Collection<ObjectProperty> getProperties(PortableDeviceObjectImplWin32 parent,  PortableDeviceKeyCollectionImplWin32 collection) throws COMException{
		ArrayList<ObjectProperty> properties = new ArrayList<>();
		long count = collection.count();
		for(long i=0; i < count; i++){
			properties.add(new ObjectPropertyWin32Impl(parent, collection.getAt(i)));
		}
		return properties;
	}
	
	
	public ObjectPropertyWin32Impl(PortableDeviceObjectImplWin32 parent,
			PropertyKey id) {
		super();
		this.parent = parent;
		this.id = id;
	}


	@Override
	public PortableDeviceObject getParent() {
		return parent;
	}

	@Override
	public PropertyKey getId() {
		return id;
	}

	@Override
	public PropVariant getValue() throws OperationUnsuccessfulException{
		if(value == null){
			try {
				value = parent.getDevice().getProperties().getValue(parent.getID(), id);
			} catch (Exception e) {
				throw new OperationUnsuccessfulException("Cannont read value of this property.", e);
			}
		}
		return value;
	}

	@Override
	public Collection<PropertyAttribute> getAttributes() throws OperationUnsuccessfulException{
		try {
			return PropertyAttributeWin32Impl.getAttributes(this, parent.getDevice().getProperties().getPropertyAttributes(parent.getID(), id));
			
		} catch (COMException e) {
			throw new OperationUnsuccessfulException("Cannot retrive attributes of property.", e); 
		}
	}


	@Override
	public String toString() {
		String s = "";
		PropVariant val = null;
		try {
			val = getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(val != null){
			s = val.toString();
		}
		return "ObjectPropertyWin32Impl [parent=" + parent + ", id=" + id
				+ ", value=" + s + "]";
	}

}
