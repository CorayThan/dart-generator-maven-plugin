package com.nathanwestlake.dartgenerator.generators;

import com.google.common.collect.Lists;
import com.nathanwestlake.dartgenerator.models.DartClass;
import com.nathanwestlake.dartgenerator.models.DartField;
import com.nathanwestlake.dartgenerator.models.DartType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CorayThan on 1/21/2015.
 */
public class ClassGenerator {
	private static final List<String> JSONABLE = Lists.newArrayList(
			"library jsonable;",
			"",
			"import 'dart:convert';",
			"",
			"abstract class Jsonable {",
			"  Map<String, dynamic> toJson();",
			"  String toJsonString() => JSON.encode(this.toJson());",
			"}"
	);
	private MethodGenerator methodGenerator = new MethodGenerator();

	public List<String> generate(DartClass dartClass, Map<String, DartClass> classMap) {
		List<String> lines = new ArrayList<String>();

		lines.add("library " + dartClass.getLibraryName() + ";");
		lines.add("");

		lines.add(dartClass.getImportLine() + "jsonable.dart';");
		for (DartField field : dartClass.getFields()) {
			if (field.type == DartType.CLASS || field.type == DartType.ENUM) {
				DartClass importClass = classMap.get(field.getFieldTypeString());
				if (importClass != null) {
					if (!importClass.equals(dartClass)) {
						lines.add(classMap.get(field.getFieldTypeString()).getImportThisClass());
					}
				} else {
					lines.add("//Could not import field: " + field.getAsString());
				}

			}
		}
		lines.add("");

		lines.add("class " + dartClass.getClassName() + " extends Jsonable {");
		for (DartField field : dartClass.getFields()) {
			lines.add(field.getAsString());
		}
		lines.add("");
		lines.addAll(methodGenerator.generateConstructor(dartClass));
		lines.add("");
		lines.addAll(methodGenerator.generateJsonConstructor(dartClass));
		lines.add("");
		lines.addAll(methodGenerator.generateToJson(dartClass));
		lines.add("");
		lines.add("}");
		return lines;
	}

	public List<String> generateJsonable() {
		return JSONABLE;
	}
}