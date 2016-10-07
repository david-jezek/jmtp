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

import java.util.Date;
import java.util.TimeZone;

import be.derycke.pieter.com.CLSID;
import be.derycke.pieter.com.COMReference;

/**
 *
 * @author Pieter De Rycke
 */
public class PropVariant {

	public static final int VT_EMPTY = 0;
	public static final int VT_NULL = 1;
	public static final int VT_BOOL = 11;
	public static final int VT_LPSTR = 30;
	public static final int VT_LPWSTR = 31; // A pointer to a null-terminated
											// Unicode string in the user
											// default locale.

	/*
	 * VARENUM usage key,
	 * 
	 * * [V] - may appear in a VARIANT * [T] - may appear in a TYPEDESC * [P] -
	 * may appear in an OLE property set * [S] - may appear in a Safe Array
	 * 
	 * 
	 * VT_EMPTY [V] [P] nothing VT_NULL [V] [P] SQL style Null VT_I2
	 * [V][T][P][S] 2 byte signed int VT_I4 [V][T][P][S] 4 byte signed int VT_R4
	 * [V][T][P][S] 4 byte real VT_R8 [V][T][P][S] 8 byte real VT_CY
	 * [V][T][P][S] currency VT_DATE [V][T][P][S] date VT_BSTR [V][T][P][S] OLE
	 * Automation string VT_DISPATCH [V][T] [S] IDispatch * VT_ERROR
	 * [V][T][P][S] SCODE VT_BOOL [V][T][P][S] True=-1, False=0 VT_VARIANT
	 * [V][T][P][S] VARIANT * VT_UNKNOWN [V][T] [S] IUnknown * VT_DECIMAL [V][T]
	 * [S] 16 byte fixed point VT_RECORD [V] [P][S] user defined type VT_I1
	 * [V][T][P][s] signed char VT_UI1 [V][T][P][S] unsigned char VT_UI2
	 * [V][T][P][S] unsigned short VT_UI4 [V][T][P][S] ULONG VT_I8 [T][P] signed
	 * 64-bit int VT_UI8 [T][P] unsigned 64-bit int VT_INT [V][T][P][S] signed
	 * machine int VT_UINT [V][T] [S] unsigned machine int VT_INT_PTR [T] signed
	 * machine register size width VT_UINT_PTR [T] unsigned machine register
	 * size width VT_VOID [T] C style void VT_HRESULT [T] Standard return type
	 * VT_PTR [T] pointer type VT_SAFEARRAY [T] (use VT_ARRAY in VARIANT)
	 * VT_CARRAY [T] C style array VT_USERDEFINED [T] user defined type VT_LPSTR
	 * [T][P] null terminated string VT_LPWSTR [T][P] wide null terminated
	 * string VT_FILETIME [P] FILETIME VT_BLOB [P] Length prefixed bytes
	 * VT_STREAM [P] Name of the stream follows VT_STORAGE [P] Name of the
	 * storage follows VT_STREAMED_OBJECT [P] Stream contains an object
	 * VT_STORED_OBJECT [P] Storage contains an object VT_VERSIONED_STREAM [P]
	 * Stream with a GUID version VT_BLOB_OBJECT [P] Blob contains an object
	 * VT_CF [P] Clipboard format VT_CLSID [P] A Class ID VT_VECTOR [P] simple
	 * counted array VT_ARRAY [V] SAFEARRAY* VT_BYREF [V] void* for local use
	 * VT_BSTR_BLOB Reserved for system use
	 */
	// enum VARENUM
	// {
	// VT_EMPTY = 0,
	// VT_NULL = 1,
	// VT_I2 = 2,
	// VT_I4 = 3,
	// VT_R4 = 4,
	// VT_R8 = 5,
	// VT_CY = 6,
	// VT_DATE = 7,
	// VT_BSTR = 8,
	// VT_DISPATCH = 9,
	// VT_ERROR = 10,
	// VT_BOOL = 11,
	// VT_VARIANT = 12,
	// VT_UNKNOWN = 13,
	// VT_DECIMAL = 14,
	// VT_I1 = 16,
	// VT_UI1 = 17,
	// VT_UI2 = 18,
	// VT_UI4 = 19,
	// VT_I8 = 20,
	// VT_UI8 = 21,
	// VT_INT = 22,
	// VT_UINT = 23,
	// VT_VOID = 24,
	// VT_HRESULT = 25,
	// VT_PTR = 26,
	// VT_SAFEARRAY = 27,
	// VT_CARRAY = 28,
	// VT_USERDEFINED = 29,
	// VT_LPSTR = 30,
	// VT_LPWSTR = 31,
	// VT_RECORD = 36,
	// VT_INT_PTR = 37,
	// VT_UINT_PTR = 38,
	// VT_FILETIME = 64,
	// VT_BLOB = 65,
	// VT_STREAM = 66,
	// VT_STORAGE = 67,
	// VT_STREAMED_OBJECT = 68,
	// VT_STORED_OBJECT = 69,
	// VT_BLOB_OBJECT = 70,
	// VT_CF = 71,
	// VT_CLSID = 72,
	// VT_VERSIONED_STREAM = 73,
	// VT_BSTR_BLOB = 0xfff,
	// VT_VECTOR = 0x1000,
	// VT_ARRAY = 0x2000,
	// VT_BYREF = 0x4000,
	// VT_RESERVED = 0x8000,
	// VT_ILLEGAL = 0xffff,
	// VT_ILLEGALMASKED = 0xfff,
	// VT_TYPEMASK = 0xfff
	// } ;

	private PropVariantEnum type;
	private Object value;

	public PropVariant(PropVariantEnum type, Object value) {
		this.type = type;
		this.value = value;
	}

	private PropVariant(int vtCode, Object value) {
		this(PropVariantEnum.getType(vtCode), value);
	}

	public PropVariant() {
		this(VT_EMPTY, null);
	}

	public PropVariant(int type, String value) {
		this(PropVariantEnum.getType(type), value);
	}

	public PropVariant(String value) {
		this(PropVariantEnum.VT_LPWSTR, value);
	}

	public PropVariant(int type, boolean value) {
		this(PropVariantEnum.getType(type), value);
	}

	public PropVariant(boolean value) {
		this(PropVariantEnum.VT_BOOL, value);
	}

	public PropVariant(byte value) {
		this(PropVariantEnum.VT_I1, value);
	}

	public PropVariant(int value) {
		this(PropVariantEnum.VT_I4, value);
	}

	public PropVariant(long value) {
		this(PropVariantEnum.VT_I8, value);
	}

	public PropVariant(Date value) {
		this(PropVariantEnum.VT_DATE, value);
	}

	public PropVariant(long hi32, long mid32, long lo32) {
		this(PropVariantEnum.VT_DECIMAL, new long[]{hi32, mid32, lo32});
	}

	public static PropVariant getEmptyPropValue() {
		return new PropVariant(PropVariantEnum.VT_EMPTY, null);
	}

	public static PropVariant getNullPropValue() {
		return new PropVariant(PropVariantEnum.VT_NULL, null);
	}

	public static PropVariant getI1PropValue(byte value) {
		return new PropVariant(PropVariantEnum.VT_I1, value);
	}

	public static PropVariant getI2PropValue(short value) {
		return new PropVariant(PropVariantEnum.VT_I2, value);
	}

	public static PropVariant getI4PropValue(int value) {
		return new PropVariant(PropVariantEnum.VT_I4, value);
	}

	public static PropVariant getI8PropValue(long value) {
		return new PropVariant(PropVariantEnum.VT_I8, value);
	}

	public static PropVariant getR4PropValue(float value) {
		return new PropVariant(PropVariantEnum.VT_R4, value);
	}

	public static PropVariant getR8PropValue(double value) {
		return new PropVariant(PropVariantEnum.VT_R8, value);
	}

	public static PropVariant getCYPropValue(double value) {
		return new PropVariant(PropVariantEnum.VT_CY, value);
	}

	public static PropVariant getDatePropValue(double vtDate) {
		return new PropVariant(PropVariantEnum.VT_DATE, new Date(convertVtDate2Time(vtDate)));
	}

	public static PropVariant getBSTRPropValue(String value) {
		return new PropVariant(PropVariantEnum.VT_BSTR, value);
	}

	public static PropVariant getDISPATCHPropValue(COMReference iDispatch) {
		return new PropVariant(PropVariantEnum.VT_DISPATCH, iDispatch);
	}

	public static PropVariant getERRORPropValue(long sCode) {
		return new PropVariant(PropVariantEnum.VT_ERROR, sCode);
	}

	public static PropVariant getBOOLPropValue(boolean value) {
		return new PropVariant(PropVariantEnum.VT_BOOL, value);
	}

	public static PropVariant getDECIMALPropValue(long hi32, long mid32, long lo32) {
		return new PropVariant(hi32, mid32, lo32);
	}

	public static PropVariant getUI1PropValue(char value) {
		return new PropVariant(PropVariantEnum.VT_UI1, value);
	}

	public static PropVariant getUI2PropValue(char value) {
		return new PropVariant(PropVariantEnum.VT_UI2, value);
	}

	public static PropVariant getUI4PropValue(long value) {
		return new PropVariant(PropVariantEnum.VT_UI4, value);
	}

	public static PropVariant getUI8PropValue(long value) {
		return new PropVariant(PropVariantEnum.VT_UI8, value);
	}

	public static PropVariant getINTPropValue(int value) {
		return new PropVariant(PropVariantEnum.VT_INT, value);
	}

	public static PropVariant getUINTPropValue(long value) {
		return new PropVariant(PropVariantEnum.VT_UINT, value);
	}

	public static PropVariant getHRESULTPropValue(long value) {
		return new PropVariant(PropVariantEnum.VT_HRESULT, value);
	}

	public static PropVariant getLPSTRPropValue(String value) {
		return new PropVariant(PropVariantEnum.VT_LPSTR, value);
	}

	public static PropVariant getLPWSTRPropValue(String value) {
		return new PropVariant(PropVariantEnum.VT_LPWSTR, value);
	}

	/**
	 * Copied from project jna GitHub project
	 * https://github.com/java-native-access/jna from file
	 * jna/contrib/platform/src/com/sun/jna/platform/win32/WinBase.java
	 * 
	 * <p>
	 * The difference between the Windows epoch (1601-01-01 00:00:00) and the
	 * Unix epoch (1970-01-01 00:00:00) in milliseconds: 11644473600000L. (Use
	 * your favorite spreadsheet program to verify the correctness of this
	 * value. By the way, did you notice that you can tell from the epochs which
	 * operating system is the modern one? :-))
	 * </p>
	 */
	private static final long EPOCH_DIFF = 11644473600000L;

	/**
	 * Copied from project jna GitHub project
	 * https://github.com/java-native-access/jna from file
	 * jna/contrib/platform/src/com/sun/jna/platform/win32/WinBase.java TODO:
	 * use jna as library
	 * 
	 * <p>
	 * Converts a Windows FILETIME into a {@link Date}. The Windows FILETIME
	 * structure holds a date and time associated with a file. The structure
	 * identifies a 64-bit integer specifying the number of 100-nanosecond
	 * intervals which have passed since January 1, 1601. This 64-bit value is
	 * split into the two double words stored in the structure.
	 * </p>
	 *
	 * @param high
	 *            The higher double word of the FILETIME structure.
	 * @param low
	 *            The lower double word of the FILETIME structure.
	 * @return The Windows FILETIME as a {@link Date}.
	 */
	public static PropVariant getFileTimePropValue(char highBites, char lowBites) {
		final long high = ((long) highBites) << 32;
		final long low = ((long) lowBites) & 0xffffffffL;
		final long filetime = high | low;
		final long ms_since_16010101 = filetime / (1000 * 10);
		final long ms_since_19700101 = ms_since_16010101 - EPOCH_DIFF;
		return new PropVariant(PropVariantEnum.VT_FILETIME, new Date(
				ms_since_19700101));
	}

	public static PropVariant getBlobPropValue(byte[] data) {
		return new PropVariant(PropVariantEnum.VT_BLOB, data);
	}

	public static PropVariant getStreamPropValue(COMReference iStream) {
		return new PropVariant(PropVariantEnum.VT_STREAM, iStream);
	}

	public static PropVariant getStoragePropValue(COMReference iStorage) {
		return new PropVariant(PropVariantEnum.VT_STORAGE, iStorage);
	}

	public static PropVariant getCLSIDPropValue(long data1, int data2, int data3, short[] data4) {
		return new PropVariant(PropVariantEnum.VT_STORAGE, new CLSID(data1, data2, data3, data4));
	}

	public static PropVariant getTypedPropValue(int type) {
		return new PropVariant(PropVariantEnum.getType(type), null);
	}

	public Object getValue() {
		return value;
	}

	public PropVariantEnum getType() {
		return type;
	}
	private int getVt() {
		return type.getVtCode();
	}

	@Override
	public String toString() {
		return "PropVariant [type=" + type + ", value=" + value + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Class VTDate supports date and time conversion between COM's VT_DATE
	 * values and Java Date class's UTC values. VT_DATE base = 12/30/1899
	 * 00:00:00, units = fractional days Date base = 01/01/1970 00:00:00, units
	 * = milliseconds 25569 = days since 12/30/1899 to 01/01/1970 86400000 =
	 * msec per day 60000 = msec per minute Conversion requires a timezone
	 * offset.
	 * <p>
	 * Usage examples: // Convert 1/1/1970 00:00:00, local timezone back and
	 * forth int myZone = new Date().getTimezoneOffset(); long myutc =
	 * VTDate.convert(25569., myZone); // returns 0L double myVtDate =
	 * VtDate.convert(0L, myZone); // returns 25569.
	 * <p>
	 * 
	 * @version 0.01
	 * @author cm
	 */
	public static final long convertVtDate2Time(double vtDate) {
		int timezoneOffset = getTimeZoneOffset();
		return (long) ((vtDate - 25569.) * 86400000.)
				/*+ ((long) timezoneOffset * 60000L)*/; //TODO first test shows that time is already with timezone 
	}

	/**
	 * @param utcDate
	 *            UTC time value
	 * @param timezoneOffset
	 *            Timezone offset from GMT in minutes
	 * @return VT_DATE
	 */
	public static final double convertTime2VtDate(long utcDate) {
		int timezoneOffset = getTimeZoneOffset();
		return (double) (utcDate /*- (long) timezoneOffset * 60000L*/) / 86400000. + 25569.; //TODO first test shows that time is already with timezone
	}
	
	public static final int getTimeZoneOffset(){
		TimeZone tz = TimeZone.getDefault();
		return tz.getOffset(new Date().getTime()) / 1000 / 60;
	}
}
