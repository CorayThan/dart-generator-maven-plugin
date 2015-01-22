package com.nathanwestlake.dartgenerator.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by CorayThan on 1/18/2015.
 */
public enum DartType {
	STRING("String"),INT("int"),DOUBLE("double"),BOOL("bool"),MAP("Map"),LIST("List"),SET("Set"),ENUM(null),CLASS(null);

	private final String dartName;

	private DartType(String dartName) {
		this.dartName = dartName;
	}

	public String getDartName(Class<?> clazz) {
		if (dartName != null) {
			return dartName;
		} else {
			return clazz.getSimpleName();
		}
	}

	public static DartType getType(Class<?> clazz) {
		if (clazz.equals(String.class) || clazz.equals(Character.TYPE) || clazz.equals(Character.class)) {
			return STRING;
		} else if (clazz.equals(Integer.TYPE) || clazz.equals(Integer.class) || clazz.equals(Byte.TYPE)
				|| clazz.equals(Byte.class) || clazz.equals(Short.TYPE) || clazz.equals(Short.class)
				|| clazz.equals(Long.class) || clazz.equals(Long.TYPE) || clazz.equals(BigInteger.class)) {
			return INT;
		} else if (clazz.equals(Float.TYPE) || clazz.equals(Float.class) || clazz.equals(Double.TYPE)
				|| clazz.equals(Double.class) || clazz.equals(BigDecimal.class)) {
			return DOUBLE;
		} else if (clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class)) {
			return BOOL;
		} else if (Map.class.isAssignableFrom(clazz)) {
			return MAP;
		} else if (Set.class.isAssignableFrom(clazz)) {
			return SET;
		} else if (Collection.class.isAssignableFrom(clazz)) {
			return LIST;
		} else if (clazz.isEnum()) {
			return ENUM;
		} else {
			return CLASS;
		}
	}
}
