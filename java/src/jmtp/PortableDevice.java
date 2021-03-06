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

package jmtp;

/**
 *
 * @author Pieter De Rycke
 */
public interface PortableDevice extends AutoCloseable {
    
    public String getFriendlyName();
    public String getManufacturer();
    public String getDescription();
    public String getId();
    public void open(String appName, int appMajor, int appMinor, int appRevision);
    public void open();
    public void close();
    public PortableDeviceObject[] getRootObjects();
    public PortableDeviceObject[] getPortableDeviceObjectsFromPersistentUniqueIDs(String[] persistentUniqueIDs);
    public PortableDeviceObject getPortableDeviceObjectsFromPersistentUniqueIDs(String persistentUniqueID);
    public String getSerialNumber();
    public String getFirmwareVersion();
    public String getModel();
    public String getProtocol();
    public String getSyncPartner();
    public int getPowerLevel();
    public PortableDeviceType getType();
    public PowerSource getPowerSource();
    public boolean isNonConsumableSupported();
    public PortableDeviceObject findObjectById(String id);
}
