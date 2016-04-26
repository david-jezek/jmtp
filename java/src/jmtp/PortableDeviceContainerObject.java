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

import java.io.InputStream;
import java.util.List;

/**
 * @author David Jezek
 *
 */
public interface PortableDeviceContainerObject {
    public PortableDeviceObject[] getChildObjects();
    public PortableDeviceFolderObject createFolderObject(String name);
	public PortableDeviceObject createFileObject(String name);
	public PortableDeviceObject createFileObjectWithContent(String name, InputStream content, long size);
	public List<PortableDeviceObject> findFile(String... relativPath);
	public void findFile(String[] relativPathsNames, int startIndex, List<PortableDeviceObject> result);
}	

