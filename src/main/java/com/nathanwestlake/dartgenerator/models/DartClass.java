package com.nathanwestlake.dartgenerator.models;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorayThan on 1/21/2015.
 */
public class DartClass {
	private final String className;
	private final String libraryName;
	private final List<DartField> fields;

	public DartClass(Class<?> clazz) {
		className = clazz.getSimpleName();
		libraryName = convertToDartFileName(className);
		fields = getDartFields(clazz);
	}

	public String getClassName() {
		return className;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public List<DartField> getFields() {
		return fields;
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

	private List<DartField> getDartFields(Class<?> model) {
		List<DartField> fields = new ArrayList<DartField>();
		for (Field field : model.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				fields.add(DartField.getDartField(field));
			}
		}
		return fields;
	}
}