package com.nathanwestlake.dartgenerator.generators;

import com.google.common.collect.Lists;
import com.nathanwestlake.dartgenerator.models.DartClass;
import com.nathanwestlake.dartgenerator.models.DartField;
import com.nathanwestlake.dartgenerator.models.DartType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorayThan on 1/21/2015.
 */
public class MethodGenerator {

	public List<String> generateConstructor(DartClass dartClass) {
		String constructor = "  " + dartClass.getClassName() + "({this.";
		for (int x = 0; x < dartClass.getFields().size(); x++) {
			constructor += dartClass.getFields().get(x).name;
			if (x != dartClass.getFields().size() - 1) {
				constructor += ", this.";
			}
		}
		constructor += "});";
		return Lists.newArrayList(constructor);
	}

	public List<String> generateJsonConstructor(DartClass dartClass) {
		List<String> fromJson = new ArrayList<>();
		fromJson.add("  " + dartClass.getClassName() + ".fromJson(Map<String, dynamic> json, {Map models}){");
		fromJson.add("    if (models == null) {");
		fromJson.add("      models = {};");
		fromJson.add("    }");
		fromJson.add("    models[json['@id']] = this;");
		fromJson.add("    this(");
		for (int x = 0; x < dartClass.getFields().size(); x++) {
			DartField dartField = dartClass.getFields().get(x);
			String name = dartField.name;
			String line;
			if (dartField.type == DartType.CLASS) {
				line = "      " + name + ": models[json['" + name + "']] != null ? models[json['" + name
						+ "']] : new " + dartField.getFieldTypeString() + ".fromJson(json['" + name + "'], models)";
				//new House(json['house'], models)
			} else {
				line = "      " + name + ": json['" + name + "']";
			}
			if (x != dartClass.getFields().size() - 1) {
				line += ",";
			}
			fromJson.add(line);
		}
		fromJson.add("      );");
		fromJson.add("  }");
		return fromJson;
	}

	public List<String> generateToJson(DartClass dartClass) {
		List<String> toJson = new ArrayList<>();
		toJson.add("  Map<String, dynamic> toJson() => <String, dynamic>{");
		for (int x = 0; x < dartClass.getFields().size(); x++) {
			String name = dartClass.getFields().get(x).name;
			String line = "    \"" + name + "\": " + name;
			if (x != dartClass.getFields().size() - 1) {
				line += ",";
			}
			toJson.add(line);
		}
		toJson.add("  };");
		return toJson;
	}
}