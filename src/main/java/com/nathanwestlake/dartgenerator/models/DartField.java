package com.nathanwestlake.dartgenerator.models;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorayThan on 1/18/2015.
 */
public class DartField {
	public final DartType type;
	public final String name;
	public final List<DartType> generics;
	public final Class<?> fieldClass;

	private DartField(String name, DartType type, List<DartType> generics, Class<?> fieldClass) {
		this.name = name;
		this.type = type;
		this.generics = generics;
		this.fieldClass = fieldClass;
	}

	public String getAsString() {
		return "  " + this.type.getDartName(fieldClass) + " " + this.name + ";";
	}

	public String getFieldTypeString() {
		return this.type.getDartName(fieldClass);
	}

	public List<String> getFieldImports() {
		return null;
	}

	public static DartField getDartField(Field field) {
		Class<?> clazz = field.getType();
		String fieldName = field.getName();
		DartType fieldType = DartType.getType(field.getType());
		List<DartType> fieldGenerics = new ArrayList<DartType>();
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

	private static DartType getTypeArg(Field field, int index) {
		ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
		Class<?> typeArg = (Class<?>) stringListType.getActualTypeArguments()[index];
		return DartType.getType(typeArg);
	}
}