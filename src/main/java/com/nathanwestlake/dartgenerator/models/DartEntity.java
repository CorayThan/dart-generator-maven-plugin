package com.nathanwestlake.dartgenerator.models;

import java.util.List;
import java.util.Map;

public abstract class DartEntity {
	private final String className;
	private final String libraryName;
	private final String importLine;

	public static DartEntity newInstance(Class<?> clazz, String importLine) {
		if (Enum.class.isAssignableFrom(clazz)) {
			return new DartEnum(clazz, importLine);
		} else {
			return new DartClass(clazz, importLine);
		}
	}

	protected DartEntity(Class<?> clazz, String importLine) {
		className = clazz.getSimpleName();
		libraryName = convertToDartFileName(className);
		this.importLine = importLine;
	}

	public abstract List<String> generate(Map<String, DartEntity> classMap);

	public String getClassName() {
		return className;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public String getImportThisClass() {
		return importLine + libraryName + ".dart';";
	}

	public String getImportLine() {
		return importLine;
	}

	private String convertToDartFileName(String className) {
		char[] letters = className.toCharArray();
		StringBuilder converted = new StringBuilder();
		for (char letter : letters) {
			if (Character.isUpperCase(letter)) {
				if (converted.length() > 0) {
					converted.append('_');
				}
				converted.append(Character.toLowerCase(letter));
			} else {
				converted.append(letter);
			}
		}
		return converted.toString();
	}
}
