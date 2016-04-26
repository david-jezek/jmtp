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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletionException;

import be.derycke.pieter.com.COMException;
import be.derycke.pieter.com.COMReference;
import be.derycke.pieter.com.COMReferenceable;
import jmtp.ObjectProperty;
import jmtp.ObjectResource;
import jmtp.OperationUnsuccessfulException;
import jmtp.PortableDeviceObject;
import jmtp.PropertyKey;
import jmtp.ResourceAttribute;

/**
 * @author David Jezek
 *
 */
public class ObjectResourceWin32Impl implements ObjectResource{

	private PortableDeviceObjectImplWin32 parent;
	private PropertyKey id;

	
	static Collection<ObjectResource> getResources(PortableDeviceObjectImplWin32 parent,  PortableDeviceKeyCollectionImplWin32 collection) throws COMException{
		ArrayList<ObjectResource> resources = new ArrayList<>();
		long count = collection.count();
		for(long i=0; i < count; i++){
			resources.add(new ObjectResourceWin32Impl(parent, collection.getAt(i)));
		}
		return resources;
	}

	
	public ObjectResourceWin32Impl(PortableDeviceObjectImplWin32 parent,
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
	public Collection<ResourceAttribute> getAttributes()  throws OperationUnsuccessfulException{
		try {
			return ResourceAttributeWin32Impl.getAttributes(this, parent.getDevice().getResources().getResourceAttributes(getParent().getID(), id));
		} catch (COMException e) {
			throw new OperationUnsuccessfulException("Getting attributes of resource was unsuccessfull.", e);
		}
	}

	@Override
	public InputStream getDataInputStream() throws COMException{
		PortableDeviceResourcesImplWin32 res = parent.device.getResources();
		return res.getInputStream(getParent().getID(), id);
	}

	@Override
	public OutputStream getDataOutputStream() throws COMException{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toString() {
		return "ObjectResourceWin32Impl [parent=" + parent + ", id=" + id + "]";
	}


}
