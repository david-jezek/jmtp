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
import jmtp.ObjectResource;
import jmtp.PortableDevice;
import jmtp.PropVariant;
import jmtp.PropertyKey;
import jmtp.ResourceAttribute;

/**
 * @author David Jezek
 *
 */
public class ResourceAttributeWin32Impl implements ResourceAttribute {

	private ObjectResourceWin32Impl parent;
	private PropertyKey id;
	PropVariant value;
	
	static Collection<ResourceAttribute> getAttributes(ObjectResourceWin32Impl parent,  PortableDeviceValuesImplWin32 collection) throws COMException{
		ArrayList<ResourceAttribute> attrs = new ArrayList<>();
		long count = collection.count();
		for(long i=0; i < count; i++){
			KeyValuePair keyValuePair = collection.getAt(i);
			attrs.add(new ResourceAttributeWin32Impl(parent, keyValuePair.key, keyValuePair.value));
		}
		return attrs;
	}

	
	public ResourceAttributeWin32Impl(ObjectResourceWin32Impl parent, PropertyKey id,
			PropVariant value) {
		super();
		this.parent = parent;
		this.id = id;
		this.value = value;
	}


	@Override
	public ObjectResource getParent() {
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


	@Override
	public String toString() {
		return "ResourceAttributeWin32Impl [id=" + id
				+ ", value=" + value + "]";
	}

	
}
