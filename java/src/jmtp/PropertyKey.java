/*
 * Original work Copyright 2007 Pieter De Rycke
 * Modified work Copyright 2015 David Jezek
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

import be.derycke.pieter.com.Guid;

/**
 *
 * @author Pieter De Rycke
 */
public class PropertyKey {
    
    private Guid fmtid;
    private long pid;
    private String name;
    
    public PropertyKey(Guid fmtid, long pid) {
        this.fmtid = fmtid;
        this.pid = pid;
    }
    
    public PropertyKey(Guid fmtid, long pid, String name) {
		super();
		this.fmtid = fmtid;
		this.pid = pid;
		this.name = name;
	}

	public Guid getFmtid() {
        return fmtid;
    }

    public long getPid() {
        return pid;
    }
    
    
    public String getName() {
		if(name == null){
			name = Win32WPDDefines.getAllKeyMap().get(this);
			if(name == null){
				name = "UKNOWN"; //$NON-NLS-1$
			}
		}
		return name;
	}

	@Override
    public String toString() {
        return getName() + "(" + fmtid.toString() + ", " + pid + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PropertyKey) {
            PropertyKey other = (PropertyKey)o;
            return other.fmtid.equals(fmtid) && pid == other.pid;
        }
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.fmtid != null ? this.fmtid.hashCode() : 0);
        hash = 41 * hash + (int) (this.pid ^ (this.pid >>> 32));
        return hash;
    }
}
