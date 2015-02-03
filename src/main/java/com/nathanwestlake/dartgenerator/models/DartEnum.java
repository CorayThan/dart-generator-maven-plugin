package com.nathanwestlake.dartgenerator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DartEnum extends DartEntity {
	private final List<String> enumValues;

	protected DartEnum(Class<?> clazz, String importLine) {
		super(clazz, importLine);
		enumValues = getEnumValues(clazz);
	}

	@Override
	public List<String> generate(Map<String, DartEntity> classMap) {
		List<String> lines = new ArrayList<String>();

		lines.add("library " + this.getLibraryName() + ";");
		lines.add("");
		lines.add("enum " + getClassName() + " {");

		lines.add("  " + String.join(", ", enumValues));
		lines.add("}");

		return lines;
	}

	public List<String> getEnumValues(Class<?> clazz) {
		return Arrays.asList(clazz.getEnumConstants())
				.stream()
				.map(T -> T.toString())
				.collect(Collectors.toList());
	}
}