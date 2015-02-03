package com.nathanwestlake.dartgenerator.models;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by CorayThan on 1/18/2015.
 */
public class DartField {
	public final DartType type;
	public final String name;
	public final List<Class<?>> generics;
	public final Class<?> fieldClass;

	private DartField(String name, DartType type, List<Class<?>> generics, Class<?> fieldClass) {
		this.name = name;
		this.type = type;
		this.generics = generics;
		this.fieldClass = fieldClass;
	}

	public String getAsString() {
		return "  " + this.type.getDartName(fieldClass) + getGenerics() + " " + this.name + ";";
	}

	public String getGenerics() {
		String genericsString = "";
		if (generics != null && !generics.isEmpty()) {
			genericsString += "<"
					+ generics.stream()
					.map(clazz -> DartType.getType(clazz).getDartName(clazz))
					.collect(Collectors.joining(", "))
					+ ">";
		}
		return genericsString;
	}

	public String getFieldTypeString() {
		return this.type.getDartName(fieldClass);
	}

	public static DartField getDartField(Field field) {
		Class<?> clazz = field.getType();
		String fieldName = field.getName();
		DartType fieldType = DartType.getType(field.getType());
		List<Class<?>> fieldGenerics = new ArrayList<>();
		if (fieldType == DartType.MAP) {
			fieldGenerics.add(getTypeArg(field, 0));
			fieldGenerics.add(getTypeArg(field, 1));
		} else if (fieldType == DartType.SET) {
			fieldGenerics.add(getTypeArg(field, 0));
		} else if (fieldType == DartType.LIST) {
			fieldGenerics.add(getTypeArg(field, 0));
		}

		return new DartField(fieldName, fieldType, fieldGenerics, clazz);
	}

	private static Class<?> getTypeArg(Field field, int index) {
		ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
		return (Class<?>) stringListType.getActualTypeArguments()[index];
	}
}