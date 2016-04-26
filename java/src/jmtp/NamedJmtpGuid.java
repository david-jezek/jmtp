package jmtp;

import be.derycke.pieter.com.Guid;

public class NamedJmtpGuid extends Guid{

	private String name = null;

	public NamedJmtpGuid(long data1, int data2, int data3, short[] data4) {
		super(data1, data2, data3, data4);
	}

	public NamedJmtpGuid(long data1, int data2, int data3, short[] data4,
			String name) {
		super(data1, data2, data3, data4);
		this.name = name;
	}

	public String getName() {
		if(name == null){
			name = Win32WPDDefines.getAllKeyMap().get(this);
			if(name == null){
				name = "UKNOWN";
			}
		}
		return name;
	}
	
	
}
