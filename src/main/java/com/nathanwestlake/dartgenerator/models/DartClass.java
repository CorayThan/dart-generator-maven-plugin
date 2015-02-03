package com.nathanwestlake.dartgenerator.models;

import com.nathanwestlake.dartgenerator.generators.MethodGenerator;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by CorayThan on 1/21/2015.
 */
public class DartClass extends DartEntity {

	private MethodGenerator methodGenerator = new MethodGenerator();
	private final List<DartField> fields;

	protected DartClass(Class<?> clazz, String importLine) {
		super(clazz, importLine);
		fields = getDartFields(clazz);
	}

	@Override
	public List<String> generate(Map<String, DartEntity> classMap) {
		List<String> lines = new ArrayList<String>();

		lines.add("library " + this.getLibraryName() + ";");
		lines.add("");

		lines.add(this.getImportLine() + "jsonable.dart';");
		for (DartField field : this.getFields()) {
			if (field.type == DartType.CLASS || field.type == DartType.ENUM) {
				DartEntity importClass = classMap.get(field.getFieldTypeString());
				if (importClass != null) {
					if (!importClass.equals(this)) {
						lines.add(classMap.get(field.getFieldTypeString()).getImportThisClass());
					}
				} else {
					lines.add("//Could not import field: " + field.getAsString());
				}

			}
		}
		lines.add("");

		lines.add("class " + this.getClassName() + " extends Jsonable {");
		for (DartField field : this.getFields()) {
			lines.add(field.getAsString());
		}
		lines.add("");
		lines.addAll(methodGenerator.generateConstructor(this));
		lines.add("");
		lines.addAll(methodGenerator.generateJsonConstructor(this));
		lines.add("");
		lines.addAll(methodGenerator.generateToJson(this));
		lines.add("");
		lines.add("}");
		return lines;
	}

	public List<DartField> getFields() {
		return fields;
	}

	private List<DartField> getDartFields(Class<?> model) {
		return Arrays.asList(model.getDeclaredFields())
				.stream()
				.filter(field -> !Modifier.isStatic(field.getModifiers()))
				.map(field -> DartField.getDartField(field))
				.collect(Collectors.toList());
	}
}