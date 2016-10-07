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

package jmtp.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * TODO pla ondersteuning missing in PortableDevice.h static final Guid
 * WPD_OBJECT_FORMAT_PLA = new Guid(0xBA050000, 0xAE6C, 0x4804, new
 * short[]{0x98, 0xBA, 0xC5, 0x7B, 0x46, 0x96, 0x5F, 0xe7});
 * 
 * @author Pieter De Rycke
 */
public class PropertyKeyReader {

	private static final String HEADER_FILE = "c:\\Program Files (x86)\\Windows Kits\\8.1\\Include\\um\\PortableDevice.h"; //$NON-NLS-1$

	private StringBuilder buffer;

	private HashMap<String, String> guidMap = new HashMap<>();
	private ArrayList<String> keyList = new ArrayList<>();

	private HashMap<String, String> keyGroups = new HashMap<>();
	private HashMap<String, String> guidGroups = new HashMap<>();

	public PropertyKeyReader(File header) {
		keyGroups.put("WPD_OBJECT_PROPERTIES", "WPD_OBJECT_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_FUNCTIONAL_OBJECT_PROPERTIES", //$NON-NLS-1$
				"WPD_FUNCTIONAL_OBJECT_"); //$NON-NLS-1$
		keyGroups.put("WPD_STORAGE_OBJECT_PROPERTIES", "WPD_STORAGE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_NETWORK_ASSOCIATION_PROPERTIES", //$NON-NLS-1$
				"WPD_NETWORK_ASSOCIATION_"); //$NON-NLS-1$
		keyGroups.put("WPD_STILL_IMAGE_CAPTURE_OBJECT_PROPERTIES", //$NON-NLS-1$
				"WPD_STILL_IMAGE_"); //$NON-NLS-1$
		keyGroups.put("WPD_RENDERING_INFORMATION_OBJECT_PROPERTIES", //$NON-NLS-1$
				"WPD_RENDERING_INFORMATION_"); //$NON-NLS-1$
		keyGroups.put("WPD_CLIENT_INFORMATION_PROPERTIES", "WPD_CLIENT"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_PROPERTY_ATTRIBUTES", "WPD_PROPERTY_ATTRIBUTE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_CLASS_EXTENSION_OPTIONS", //$NON-NLS-1$
				"WPD_CLASS_EXTENSION_OPTIONS_"); //$NON-NLS-1$
		keyGroups.put("WPD_RESOURCE_ATTRIBUTES", "WPD_RESOURCE_ATTRIBUTE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_DEVICE_PROPERTIES", "WPD_DEVICE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_SERVICE_PROPERTIES", "WPD_SERVICE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_EVENT_PROPERTIES", "WPD_EVENT_PARAMETER_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_EVENT_OPTIONS", "WPD_EVENT_OPTION_IS_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_EVENT_ATTRIBUTES", "WPD_EVENT_ATTRIBUTE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_API_OPTIONS", "WPD_API_OPTION_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_FORMAT_ATTRIBUTES", "WPD_FORMAT_ATTRIBUTE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_METHOD_ATTRIBUTES", "WPD_METHOD_ATTRIBUTE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_PARAMETER_ATTRIBUTES", "WPD_PARAMETER_ATTRIBUTE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_CATEGORY_COMMON_COMMANDS", "WPD_COMMAND_COMMON_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_CATEGORY_COMMON_PROPERTIES", "WPD_PROPERTY_COMMON_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_CATEGORY_OBJECT_ENUMERATION_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_OBJECT_ENUMERATION_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_OBJECT_ENUMERATION_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_OBJECT_ENUMERATION_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_OBJECT_PROPERTIES_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_OBJECT_PROPERTIES_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_OBJECT_PROPERTIES_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_OBJECT_PROPERTIES_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_OBJECT_PROPERTIES_BULK_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_OBJECT_PROPERTIES_BULK_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_OBJECT_PROPERTIES_BULK_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_OBJECT_PROPERTIES_BULK_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_OBJECT_RESOURCES_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_OBJECT_RESOURCES_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_OBJECT_RESOURCES_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_OBJECT_RESOURCES_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_OBJECT_MANAGEMENT_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_OBJECT_MANAGEMENT_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_OBJECT_MANAGEMENT_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_OBJECT_MANAGEMENT_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_CAPABILITIES_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_CAPABILITIES_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_CAPABILITIES_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_CAPABILITIES_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_STORAGE_COMMANDS", "WPD_COMMAND_STORAGE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_CATEGORY_STORAGE_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_STORAGE_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_SMS_COMMANDS", "WPD_COMMAND_SMS_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_CATEGORY_SMS_PROPERTIES", "WPD_PROPERTY_SMS_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_CATEGORY_STILL_IMAGE_CAPTURE_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_STILL_IMAGE_CAPTURE_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_MEDIA_CAPTURE_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_MEDIA_CAPTURE_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_DEVICE_HINTS_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_DEVICE_HINTS_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_DEVICE_HINTS_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_DEVICE_HINTS_"); //$NON-NLS-1$
		keyGroups.put("WPD_CLASS_EXTENSION_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_CLASS_EXTENSION_"); //$NON-NLS-1$
		keyGroups.put("WPD_CLASS_EXTENSION_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_CLASS_EXTENSION_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_SERVICE_CAPABILITIES_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_SERVICE_CAPABILITIES_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_SERVICE_CAPABILITIES_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_SERVICE_CAPABILITIES_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_SERVICE_METHODS_COMMANDS", //$NON-NLS-1$
				"WPD_COMMAND_SERVICE_METHODS_"); //$NON-NLS-1$
		keyGroups.put("WPD_CATEGORY_SERVICE_METHODS_PROPERTIES", //$NON-NLS-1$
				"WPD_PROPERTY_SERVICE_METHOD"); //$NON-NLS-1$
		keyGroups
				.put("WPD_CATEGORY_SERVICE_METHODS_RESOURCES", "WPD_RESOURCE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_FOLDER_OBJECT_PROPERTIES", "WPD_FOLDER_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_IMAGE_OBJECT_PROPERTIES", "WPD_IMAGE_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_DOCUMENT_OBJECT_PROPERTIES", "WPD_DOCUMENT_OBJECT_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_MEDIA_PROPERTIES", "WPD_MEDIA_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_CONTACT_OBJECT_PROPERTIES", "WPD_CONTACT_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_MUSIC_OBJECT_PROPERTIES", "WPD_MUSIC_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_VIDEO_OBJECT_PROPERTIES", "WPD_VIDEO_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_COMMON_INFORMATION_OBJECT_PROPERTIES", //$NON-NLS-1$
				"WPD_COMMON_INFORMATION_"); //$NON-NLS-1$
		keyGroups.put("WPD_MEMO_OBJECT_PROPERTIES", "WPD_MEMO_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_EMAIL_OBJECT_PROPERTIES", "WPD_EMAIL_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_APPOINTMENT_OBJECT_PROPERTIES", "WPD_APPOINTMENT_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_TASK_OBJECT_PROPERTIES", "WPD_TASK_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_SMS_OBJECT_PROPERTIES", "WPD_SMS_"); //$NON-NLS-1$ //$NON-NLS-2$
		keyGroups.put("WPD_SECTION_OBJECT_PROPERTIES", "WPD_SECTION_DATA_"); //$NON-NLS-1$ //$NON-NLS-2$
		guidGroups.put("WPD_OBJECT_FORMATS", "WPD_OBJECT_FORMAT_"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer = new StringBuilder();
		process(header);
	}

	private void process(File header) {
		BufferedReader reader = null;
		try {
			try {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(header)));

				String line = reader.readLine();
				while (line != null) {
					if (line.startsWith("DEFINE_PROPERTYKEY")) { //$NON-NLS-1$
						String[] defineParts = line
								.substring(line.indexOf("(") + 1, //$NON-NLS-1$
										line.indexOf(")")).trim().split(","); //$NON-NLS-1$ //$NON-NLS-2$

						String name = defineParts[0];
						String[] arguments = new String[defineParts.length - 1];
						for (int i = 1; i < defineParts.length; i++)
							arguments[i - 1] = defineParts[i];

						processPropertyKey(name, arguments);
					} else if (line.startsWith("DEFINE_GUID")) { //$NON-NLS-1$
						String[] defineParts = line
								.substring(line.indexOf("(") + 1, //$NON-NLS-1$
										line.indexOf(")")).trim().split(","); //$NON-NLS-1$ //$NON-NLS-2$

						String name = defineParts[0];
						String[] arguments = new String[defineParts.length - 1];
						for (int i = 1; i < defineParts.length; i++)
							arguments[i - 1] = defineParts[i];

						processGuid(name, arguments);
					} else if (line.startsWith("#define")) { //$NON-NLS-1$
						String[] parts = line.trim().split(" "); //$NON-NLS-1$

						if (parts[2].startsWith("L\"") //$NON-NLS-1$
								&& parts[2].endsWith("\"")) { //$NON-NLS-1$
							processString(parts[1], parts[2].substring(2,
									parts[2].length() - 1));
						}
					}

					line = reader.readLine();
				}
			} finally {
				if (reader != null)
					reader.close();
			}
		} catch (IOException e) {
		}
		processCollections();
	}
	
	private String upperFirstLetter(String s){
		if(s == null){
			return null;
		}
		if(s.length() < 1){
			return s.toUpperCase();
		}
		return ("" + s.charAt(0)).toUpperCase() + s.substring(1, s.length());  //$NON-NLS-1$
	}
	private void createLazyInitializer(String propertyName, String type, String[] preInitCodeLines, String initCode){
		buffer.append("\tprivate static " + type +" " + propertyName+ ";\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		buffer.append("\tpublic static " + type +" get" //$NON-NLS-1$ //$NON-NLS-2$
				+ upperFirstLetter(propertyName) + "() {\n"); //$NON-NLS-1$
		buffer.append("\t\tif(" + propertyName +" == null) {\n"); //$NON-NLS-1$ //$NON-NLS-2$
		if(preInitCodeLines != null){
			for (String line : preInitCodeLines) {
				buffer.append("\t\t\t" + line + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		buffer.append("\t\t\t" + propertyName + " = "+ initCode + ";\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		buffer.append("\t\t}\n"); //$NON-NLS-1$
		buffer.append("\t\treturn " + propertyName + ";\n"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("\t}\n"); //$NON-NLS-1$
	}
	private void processCollections() {
		for (String keyGroupName : keyGroups.keySet()) {
			String prefix = keyGroups.get(keyGroupName);
			String collectionElements = keyList.stream()
					.filter(keyName -> keyName.startsWith(prefix))
					.collect(Collectors.joining(", \n\t\t\t\t")); //$NON-NLS-1$
			
			createLazyInitializer(keyGroupName, "Collection<PropertyKey>", null, "Collections.unmodifiableCollection(Arrays.asList(\n\t\t\t\t" //$NON-NLS-1$ //$NON-NLS-2$
					+ collectionElements + ")\n\t\t\t)"); //$NON-NLS-1$
		}
		for (String guidGroupName : guidGroups.keySet()) {
			String prefix = guidGroups.get(guidGroupName);
			String collectionElements = guidMap.values().stream()
					.filter(guidName -> guidName.startsWith(prefix))
					.collect(Collectors.joining(", \n\t\t\t\t")); //$NON-NLS-1$
			createLazyInitializer(guidGroupName, "Collection<Guid>", null, "Collections.unmodifiableCollection(Arrays.asList(\n\t\t\t\t" //$NON-NLS-1$ //$NON-NLS-2$
					+ collectionElements + ")\n\t\t\t)"); //$NON-NLS-1$
		}
		String collectionElements = keyList.stream()
				.collect(Collectors.joining(", \n\t\t\t\t")); //$NON-NLS-1$
		createLazyInitializer("allKnownKeys", "Collection<PropertyKey>", null, "Collections.unmodifiableCollection(Arrays.asList(\n\t\t\t\t" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ collectionElements + ")\n\t\t\t)"); //$NON-NLS-1$
		collectionElements = guidMap.values().stream()
				.collect(Collectors.joining(", \n\t\t\t\t")); //$NON-NLS-1$
		createLazyInitializer("allKnownGuids", "Collection<NamedJmtpGuid>", null, "Collections.unmodifiableCollection(Arrays.asList(\n\t\t\t\t" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ collectionElements + ")\n\t\t\t)"); //$NON-NLS-1$
		createLazyInitializer("allKeyMap", "Map<PropertyKey, String>", new String[]{ //$NON-NLS-1$ //$NON-NLS-2$
				"HashMap<PropertyKey, String> keyMap = new HashMap<>();", //$NON-NLS-1$
				"for (PropertyKey propertyKey : getAllKnownKeys()) {", //$NON-NLS-1$
				"\tkeyMap.put(propertyKey, propertyKey.getName());", //$NON-NLS-1$
				"}" //$NON-NLS-1$
		}, "Collections.unmodifiableMap(keyMap)"); //$NON-NLS-1$
	}

	private void processPropertyKey(String name, String[] arguments) {
		String guidValue = arguments[0].trim() + "l, " //$NON-NLS-1$
				+ arguments[1].trim() + ", " + arguments[2].trim() //$NON-NLS-1$
				+ ", new short[]{" + arguments[3].trim() + ", " //$NON-NLS-1$ //$NON-NLS-2$
				+ arguments[4].trim() + ", " + arguments[5].trim() + ", " //$NON-NLS-1$ //$NON-NLS-2$
				+ arguments[6].trim() + ", " + arguments[7].trim() + ", " //$NON-NLS-1$ //$NON-NLS-2$
				+ arguments[8].trim() + ", " + arguments[9].trim() + ", " //$NON-NLS-1$ //$NON-NLS-2$
				+ arguments[10].trim() + "}"; //$NON-NLS-1$
		guidValue = guidMap.getOrDefault(guidValue, "new NamedJmtpGuid(" + guidValue + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		keyList.add(name);
		buffer.append("\tpublic static final PropertyKey " + name.trim() //$NON-NLS-1$
				+ " = new PropertyKey(" + guidValue + ", " //$NON-NLS-1$ //$NON-NLS-2$
				+ arguments[11].trim() + ", \"" + name.trim() + "\");" + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void processGuid(String name, String[] arguments) {
		String guidValue = arguments[0].trim() + "l, " //$NON-NLS-1$
				+ arguments[1].trim() + ", " + arguments[2].trim() //$NON-NLS-1$
				+ ", new short[]{" + arguments[3].trim() + ", " //$NON-NLS-1$ //$NON-NLS-2$
				+ arguments[4].trim() + ", " + arguments[5].trim() + ", " //$NON-NLS-1$ //$NON-NLS-2$
				+ arguments[6].trim() + ", " + arguments[7].trim() + ", " //$NON-NLS-1$ //$NON-NLS-2$
				+ arguments[8].trim() + ", " + arguments[9].trim() + ", " //$NON-NLS-1$ //$NON-NLS-2$
				+ arguments[10].trim() + "}"; //$NON-NLS-1$
		guidMap.put(guidValue, name);
		buffer.append("\tpublic static final NamedJmtpGuid " + name.trim() + " = " + "new NamedJmtpGuid(" + guidValue //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ", \"" + name.trim() + "\");" + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void processString(String name, String value) {
		buffer.append("\tpublic static final String " + name + " = \"" + value //$NON-NLS-1$ //$NON-NLS-2$
				+ "\";\n"); //$NON-NLS-1$
	}

	public void save(File sourceCodeFile) {
		ArrayList<String> lines;
		try(BufferedReader sourceCodeReader = new BufferedReader(new FileReader(sourceCodeFile))){
			lines = sourceCodeReader.lines().collect(Collectors.toCollection( () -> new ArrayList<String>() ));
		}catch(IOException ex){
			throw new RuntimeException("Reading original source file (" + sourceCodeFile.getName() + ")error!", ex); //$NON-NLS-1$ //$NON-NLS-2$
		}
		boolean insertImportsBlock = false;
		boolean insertCodeBlock = false;
		try(BufferedWriter sourceCodeWriter = new BufferedWriter(new FileWriter(sourceCodeFile))){
			for (String line : lines) {
				if(line.trim().equals("//PLACE GENERATED IMPOPRT HERE END")){ //$NON-NLS-1$
					insertImportsBlock = false;
				} else if(line.trim().equals("//PLACE GENERATED CODE HERE END")){ //$NON-NLS-1$
					insertImportsBlock = false;
				}
				if(!insertImportsBlock && !insertCodeBlock){
					sourceCodeWriter.write(line);
					sourceCodeWriter.newLine();
				} else {
					//ignore old lines in generated blocks 
				}
				if(line.trim().equals("//PLACE GENERATED IMPOPRT HERE BEGIN")){ //$NON-NLS-1$
					insertImportsBlock = true;
					sourceCodeWriter.write("import be.derycke.pieter.com.Guid;"); //$NON-NLS-1$
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.Collection;"); //$NON-NLS-1$
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.Collections;"); //$NON-NLS-1$
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.Arrays;"); //$NON-NLS-1$
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.HashMap;"); //$NON-NLS-1$
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.Map;"); //$NON-NLS-1$
					sourceCodeWriter.newLine();
				} else if(line.trim().equals("//PLACE GENERATED CODE HERE BEGIN")){ //$NON-NLS-1$
					insertImportsBlock = true;
					sourceCodeWriter.write(buffer.toString());
				}
			}
		}catch(IOException ex){
			throw new RuntimeException("Generating new source file (" + sourceCodeFile.getName() + ") failed!", ex); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
//		OutputStreamWriter writer = null;
//		try {
//			try {
//				File sourceFile = new File(outputDirectory.getAbsoluteFile(),
//						className + ".java");
//				sourceFile.createNewFile();
//
//				writer = new OutputStreamWriter(
//						new FileOutputStream(sourceFile));
//				if (packageName != null) {
//					writer.write("package " + packageName + ";\n");
//					writer.write("\n");
//				}
//
//				writer.write("import be.derycke.pieter.com.Guid;\n");
//				writer.write("import java.util.Collection;\n");
//				writer.write("import java.util.Collections;\n");
//				writer.write("import java.util.Arrays;\n");
//				writer.write("\n");
//				writer.write("public class " + className + " {\n");
//
//				writer.write(buffer.toString());
//
//				writer.write("}");
//				writer.flush();
//			} finally {
//				if (writer != null)
//					writer.close();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public static void main(String[] args) {
		PropertyKeyReader reader = new PropertyKeyReader(new File(HEADER_FILE));
		reader.save(new File("src\\jmtp\\Win32WPDDefines.java")); //$NON-NLS-1$
	}
}
