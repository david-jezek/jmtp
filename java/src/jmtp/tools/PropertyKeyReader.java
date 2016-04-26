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

	private static final String HEADER_FILE = "c:\\Program Files (x86)\\Windows Kits\\8.1\\Include\\um\\PortableDevice.h";

	private StringBuilder buffer;

	private HashMap<String, String> guidMap = new HashMap<>();
	private ArrayList<String> keyList = new ArrayList<>();

	private HashMap<String, String> keyGroups = new HashMap<>();
	private HashMap<String, String> guidGroups = new HashMap<>();

	public PropertyKeyReader(File header) {
		keyGroups.put("WPD_OBJECT_PROPERTIES", "WPD_OBJECT_");
		keyGroups.put("WPD_FUNCTIONAL_OBJECT_PROPERTIES",
				"WPD_FUNCTIONAL_OBJECT_");
		keyGroups.put("WPD_STORAGE_OBJECT_PROPERTIES", "WPD_STORAGE_");
		keyGroups.put("WPD_NETWORK_ASSOCIATION_PROPERTIES",
				"WPD_NETWORK_ASSOCIATION_");
		keyGroups.put("WPD_STILL_IMAGE_CAPTURE_OBJECT_PROPERTIES",
				"WPD_STILL_IMAGE_");
		keyGroups.put("WPD_RENDERING_INFORMATION_OBJECT_PROPERTIES",
				"WPD_RENDERING_INFORMATION_");
		keyGroups.put("WPD_CLIENT_INFORMATION_PROPERTIES", "WPD_CLIENT");
		keyGroups.put("WPD_PROPERTY_ATTRIBUTES", "WPD_PROPERTY_ATTRIBUTE_");
		keyGroups.put("WPD_CLASS_EXTENSION_OPTIONS",
				"WPD_CLASS_EXTENSION_OPTIONS_");
		keyGroups.put("WPD_RESOURCE_ATTRIBUTES", "WPD_RESOURCE_ATTRIBUTE_");
		keyGroups.put("WPD_DEVICE_PROPERTIES", "WPD_DEVICE_");
		keyGroups.put("WPD_SERVICE_PROPERTIES", "WPD_SERVICE_");
		keyGroups.put("WPD_EVENT_PROPERTIES", "WPD_EVENT_PARAMETER_");
		keyGroups.put("WPD_EVENT_OPTIONS", "WPD_EVENT_OPTION_IS_");
		keyGroups.put("WPD_EVENT_ATTRIBUTES", "WPD_EVENT_ATTRIBUTE_");
		keyGroups.put("WPD_API_OPTIONS", "WPD_API_OPTION_");
		keyGroups.put("WPD_FORMAT_ATTRIBUTES", "WPD_FORMAT_ATTRIBUTE_");
		keyGroups.put("WPD_METHOD_ATTRIBUTES", "WPD_METHOD_ATTRIBUTE_");
		keyGroups.put("WPD_PARAMETER_ATTRIBUTES", "WPD_PARAMETER_ATTRIBUTE_");
		keyGroups.put("WPD_CATEGORY_COMMON_COMMANDS", "WPD_COMMAND_COMMON_");
		keyGroups.put("WPD_CATEGORY_COMMON_PROPERTIES", "WPD_PROPERTY_COMMON_");
		keyGroups.put("WPD_CATEGORY_OBJECT_ENUMERATION_COMMANDS",
				"WPD_COMMAND_OBJECT_ENUMERATION_");
		keyGroups.put("WPD_CATEGORY_OBJECT_ENUMERATION_PROPERTIES",
				"WPD_PROPERTY_OBJECT_ENUMERATION_");
		keyGroups.put("WPD_CATEGORY_OBJECT_PROPERTIES_COMMANDS",
				"WPD_COMMAND_OBJECT_PROPERTIES_");
		keyGroups.put("WPD_CATEGORY_OBJECT_PROPERTIES_PROPERTIES",
				"WPD_PROPERTY_OBJECT_PROPERTIES_");
		keyGroups.put("WPD_CATEGORY_OBJECT_PROPERTIES_BULK_COMMANDS",
				"WPD_COMMAND_OBJECT_PROPERTIES_BULK_");
		keyGroups.put("WPD_CATEGORY_OBJECT_PROPERTIES_BULK_PROPERTIES",
				"WPD_PROPERTY_OBJECT_PROPERTIES_BULK_");
		keyGroups.put("WPD_CATEGORY_OBJECT_RESOURCES_COMMANDS",
				"WPD_COMMAND_OBJECT_RESOURCES_");
		keyGroups.put("WPD_CATEGORY_OBJECT_RESOURCES_PROPERTIES",
				"WPD_PROPERTY_OBJECT_RESOURCES_");
		keyGroups.put("WPD_CATEGORY_OBJECT_MANAGEMENT_COMMANDS",
				"WPD_COMMAND_OBJECT_MANAGEMENT_");
		keyGroups.put("WPD_CATEGORY_OBJECT_MANAGEMENT_PROPERTIES",
				"WPD_PROPERTY_OBJECT_MANAGEMENT_");
		keyGroups.put("WPD_CATEGORY_CAPABILITIES_COMMANDS",
				"WPD_COMMAND_CAPABILITIES_");
		keyGroups.put("WPD_CATEGORY_CAPABILITIES_PROPERTIES",
				"WPD_PROPERTY_CAPABILITIES_");
		keyGroups.put("WPD_CATEGORY_STORAGE_COMMANDS", "WPD_COMMAND_STORAGE_");
		keyGroups.put("WPD_CATEGORY_STORAGE_PROPERTIES",
				"WPD_PROPERTY_STORAGE_");
		keyGroups.put("WPD_CATEGORY_SMS_COMMANDS", "WPD_COMMAND_SMS_");
		keyGroups.put("WPD_CATEGORY_SMS_PROPERTIES", "WPD_PROPERTY_SMS_");
		keyGroups.put("WPD_CATEGORY_STILL_IMAGE_CAPTURE_COMMANDS",
				"WPD_COMMAND_STILL_IMAGE_CAPTURE_");
		keyGroups.put("WPD_CATEGORY_MEDIA_CAPTURE_COMMANDS",
				"WPD_COMMAND_MEDIA_CAPTURE_");
		keyGroups.put("WPD_CATEGORY_DEVICE_HINTS_COMMANDS",
				"WPD_COMMAND_DEVICE_HINTS_");
		keyGroups.put("WPD_CATEGORY_DEVICE_HINTS_PROPERTIES",
				"WPD_PROPERTY_DEVICE_HINTS_");
		keyGroups.put("WPD_CLASS_EXTENSION_COMMANDS",
				"WPD_COMMAND_CLASS_EXTENSION_");
		keyGroups.put("WPD_CLASS_EXTENSION_PROPERTIES",
				"WPD_PROPERTY_CLASS_EXTENSION_");
		keyGroups.put("WPD_CATEGORY_SERVICE_CAPABILITIES_COMMANDS",
				"WPD_COMMAND_SERVICE_CAPABILITIES_");
		keyGroups.put("WPD_CATEGORY_SERVICE_CAPABILITIES_PROPERTIES",
				"WPD_PROPERTY_SERVICE_CAPABILITIES_");
		keyGroups.put("WPD_CATEGORY_SERVICE_METHODS_COMMANDS",
				"WPD_COMMAND_SERVICE_METHODS_");
		keyGroups.put("WPD_CATEGORY_SERVICE_METHODS_PROPERTIES",
				"WPD_PROPERTY_SERVICE_METHOD");
		keyGroups
				.put("WPD_CATEGORY_SERVICE_METHODS_RESOURCES", "WPD_RESOURCE_");
		keyGroups.put("WPD_FOLDER_OBJECT_PROPERTIES", "WPD_FOLDER_");
		keyGroups.put("WPD_IMAGE_OBJECT_PROPERTIES", "WPD_IMAGE_");
		keyGroups.put("WPD_DOCUMENT_OBJECT_PROPERTIES", "WPD_DOCUMENT_OBJECT_");
		keyGroups.put("WPD_MEDIA_PROPERTIES", "WPD_MEDIA_");
		keyGroups.put("WPD_CONTACT_OBJECT_PROPERTIES", "WPD_CONTACT_");
		keyGroups.put("WPD_MUSIC_OBJECT_PROPERTIES", "WPD_MUSIC_");
		keyGroups.put("WPD_VIDEO_OBJECT_PROPERTIES", "WPD_VIDEO_");
		keyGroups.put("WPD_COMMON_INFORMATION_OBJECT_PROPERTIES",
				"WPD_COMMON_INFORMATION_");
		keyGroups.put("WPD_MEMO_OBJECT_PROPERTIES", "WPD_MEMO_");
		keyGroups.put("WPD_EMAIL_OBJECT_PROPERTIES", "WPD_EMAIL_");
		keyGroups.put("WPD_APPOINTMENT_OBJECT_PROPERTIES", "WPD_APPOINTMENT_");
		keyGroups.put("WPD_TASK_OBJECT_PROPERTIES", "WPD_TASK_");
		keyGroups.put("WPD_SMS_OBJECT_PROPERTIES", "WPD_SMS_");
		keyGroups.put("WPD_SECTION_OBJECT_PROPERTIES", "WPD_SECTION_DATA_");
		guidGroups.put("WPD_OBJECT_FORMATS", "WPD_OBJECT_FORMAT_");
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
					if (line.startsWith("DEFINE_PROPERTYKEY")) {
						String[] defineParts = line
								.substring(line.indexOf("(") + 1,
										line.indexOf(")")).trim().split(",");

						String name = defineParts[0];
						String[] arguments = new String[defineParts.length - 1];
						for (int i = 1; i < defineParts.length; i++)
							arguments[i - 1] = defineParts[i];

						processPropertyKey(name, arguments);
					} else if (line.startsWith("DEFINE_GUID")) {
						String[] defineParts = line
								.substring(line.indexOf("(") + 1,
										line.indexOf(")")).trim().split(",");

						String name = defineParts[0];
						String[] arguments = new String[defineParts.length - 1];
						for (int i = 1; i < defineParts.length; i++)
							arguments[i - 1] = defineParts[i];

						processGuid(name, arguments);
					} else if (line.startsWith("#define")) {
						String[] parts = line.trim().split(" ");

						if (parts[2].startsWith("L\"")
								&& parts[2].endsWith("\"")) {
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
		return ("" + s.charAt(0)).toUpperCase() + s.substring(1, s.length()); 
	}
	private void createLazyInitializer(String propertyName, String type, String[] preInitCodeLines, String initCode){
		buffer.append("\tprivate static " + type +" " + propertyName+ ";\n");
		buffer.append("\tpublic static " + type +" get"
				+ upperFirstLetter(propertyName) + "() {\n");
		buffer.append("\t\tif(" + propertyName +" == null) {\n");
		if(preInitCodeLines != null){
			for (String line : preInitCodeLines) {
				buffer.append("\t\t\t" + line + "\n");
			}
		}
		buffer.append("\t\t\t" + propertyName + " = "+ initCode + ";\n");
		buffer.append("\t\t}\n");
		buffer.append("\t\treturn " + propertyName + ";\n");
		buffer.append("\t}\n");
	}
	private void processCollections() {
		for (String keyGroupName : keyGroups.keySet()) {
			String prefix = keyGroups.get(keyGroupName);
			String collectionElements = keyList.stream()
					.filter(keyName -> keyName.startsWith(prefix))
					.collect(Collectors.joining(", \n\t\t\t\t"));
			
			createLazyInitializer(keyGroupName, "Collection<PropertyKey>", null, "Collections.unmodifiableCollection(Arrays.asList(\n\t\t\t\t"
					+ collectionElements + ")\n\t\t\t)");
		}
		for (String guidGroupName : guidGroups.keySet()) {
			String prefix = guidGroups.get(guidGroupName);
			String collectionElements = guidMap.values().stream()
					.filter(guidName -> guidName.startsWith(prefix))
					.collect(Collectors.joining(", \n\t\t\t\t"));
			createLazyInitializer(guidGroupName, "Collection<Guid>", null, "Collections.unmodifiableCollection(Arrays.asList(\n\t\t\t\t"
					+ collectionElements + ")\n\t\t\t)");
		}
		String collectionElements = keyList.stream()
				.collect(Collectors.joining(", \n\t\t\t\t"));
		createLazyInitializer("allKnownKeys", "Collection<PropertyKey>", null, "Collections.unmodifiableCollection(Arrays.asList(\n\t\t\t\t"
				+ collectionElements + ")\n\t\t\t)");
		collectionElements = guidMap.values().stream()
				.collect(Collectors.joining(", \n\t\t\t\t"));
		createLazyInitializer("allKnownGuids", "Collection<NamedJmtpGuid>", null, "Collections.unmodifiableCollection(Arrays.asList(\n\t\t\t\t"
				+ collectionElements + ")\n\t\t\t)");
		createLazyInitializer("allKeyMap", "Map<PropertyKey, String>", new String[]{
				"HashMap<PropertyKey, String> keyMap = new HashMap<>();",
				"for (PropertyKey propertyKey : getAllKnownKeys()) {",
				"\tkeyMap.put(propertyKey, propertyKey.getName());",
				"}"
		}, "Collections.unmodifiableMap(keyMap)");
	}

	private void processPropertyKey(String name, String[] arguments) {
		String guidValue = arguments[0].trim() + "l, "
				+ arguments[1].trim() + ", " + arguments[2].trim()
				+ ", new short[]{" + arguments[3].trim() + ", "
				+ arguments[4].trim() + ", " + arguments[5].trim() + ", "
				+ arguments[6].trim() + ", " + arguments[7].trim() + ", "
				+ arguments[8].trim() + ", " + arguments[9].trim() + ", "
				+ arguments[10].trim() + "}";
		guidValue = guidMap.getOrDefault(guidValue, "new NamedJmtpGuid(" + guidValue + ")");
		keyList.add(name);
		buffer.append("\tpublic static final PropertyKey " + name.trim()
				+ " = new PropertyKey(" + guidValue + ", "
				+ arguments[11].trim() + ", \"" + name.trim() + "\");" + "\n");
	}

	private void processGuid(String name, String[] arguments) {
		String guidValue = arguments[0].trim() + "l, "
				+ arguments[1].trim() + ", " + arguments[2].trim()
				+ ", new short[]{" + arguments[3].trim() + ", "
				+ arguments[4].trim() + ", " + arguments[5].trim() + ", "
				+ arguments[6].trim() + ", " + arguments[7].trim() + ", "
				+ arguments[8].trim() + ", " + arguments[9].trim() + ", "
				+ arguments[10].trim() + "}";
		guidMap.put(guidValue, name);
		buffer.append("\tpublic static final NamedJmtpGuid " + name.trim() + " = " + "new NamedJmtpGuid(" + guidValue
				+ ", \"" + name.trim() + "\");" + "\n");
	}

	private void processString(String name, String value) {
		buffer.append("\tpublic static final String " + name + " = \"" + value
				+ "\";\n");
	}

	public void save(File sourceCodeFile) {
		ArrayList<String> lines;
		try(BufferedReader sourceCodeReader = new BufferedReader(new FileReader(sourceCodeFile))){
			lines = sourceCodeReader.lines().collect(Collectors.toCollection( () -> new ArrayList<String>() ));
		}catch(IOException ex){
			throw new RuntimeException("Reading original source file (" + sourceCodeFile.getName() + ")error!", ex);
		}
		boolean insertImportsBlock = false;
		boolean insertCodeBlock = false;
		try(BufferedWriter sourceCodeWriter = new BufferedWriter(new FileWriter(sourceCodeFile))){
			for (String line : lines) {
				if(line.trim().equals("//PLACE GENERATED IMPOPRT HERE END")){
					insertImportsBlock = false;
				} else if(line.trim().equals("//PLACE GENERATED CODE HERE END")){
					insertImportsBlock = false;
				}
				if(!insertImportsBlock && !insertCodeBlock){
					sourceCodeWriter.write(line);
					sourceCodeWriter.newLine();
				} else {
					//ignore old lines in generated blocks 
				}
				if(line.trim().equals("//PLACE GENERATED IMPOPRT HERE BEGIN")){
					insertImportsBlock = true;
					sourceCodeWriter.write("import be.derycke.pieter.com.Guid;");
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.Collection;");
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.Collections;");
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.Arrays;");
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.HashMap;");
					sourceCodeWriter.newLine();
					sourceCodeWriter.write("import java.util.Map;");
					sourceCodeWriter.newLine();
				} else if(line.trim().equals("//PLACE GENERATED CODE HERE BEGIN")){
					insertImportsBlock = true;
					sourceCodeWriter.write(buffer.toString());
				}
			}
		}catch(IOException ex){
			throw new RuntimeException("Generating new source file (" + sourceCodeFile.getName() + ") failed!", ex);
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
		reader.save(new File("src\\jmtp\\Win32WPDDefines.java"));
	}
}
