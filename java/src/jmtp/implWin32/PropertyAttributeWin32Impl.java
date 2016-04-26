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
import jmtp.ObjectProperty;
import jmtp.PropVariant;
import jmtp.PropertyAttribute;
import jmtp.PropertyKey;
import jmtp.ResourceAttribute;

/**
 * @author David Jezek
 *
 */
public class PropertyAttributeWin32Impl implements PropertyAttribute {

	private ObjectPropertyWin32Impl parent;
	private PropertyKey id;
	PropVariant value;
	
	static Collection<PropertyAttribute> getAttributes(ObjectPropertyWin32Impl parent,  PortableDeviceValuesImplWin32 collection) throws COMException{
		ArrayList<PropertyAttribute> attrs = new ArrayList<>();
		long count = collection.count();
		for(long i=0; i < count; i++){
			KeyValuePair keyValuePair = collection.getAt(i);
			attrs.add(new PropertyAttributeWin32Impl(parent, keyValuePair.key, keyValuePair.value));
		}
		return attrs;
	}

	
	public PropertyAttributeWin32Impl(ObjectPropertyWin32Impl parent,
			PropertyKey id, PropVariant value) {
		super();
		this.parent = parent;
		this.id = id;
		this.value = value;
	}


	@Override
	public ObjectProperty geParent() {
		return parent;
	}

	@Override
	public PropertyKey getId() {
		return id;
	}

	@Override
	public PropVariant getValue() {
		return value;
	}

}
