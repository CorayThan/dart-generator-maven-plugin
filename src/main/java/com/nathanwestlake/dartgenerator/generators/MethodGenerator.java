package com.nathanwestlake.dartgenerator.generators;

import com.google.common.collect.Lists;
import com.nathanwestlake.dartgenerator.models.DartClass;
import com.nathanwestlake.dartgenerator.models.DartField;
import com.nathanwestlake.dartgenerator.models.DartType;

import java.util.ArrayList;
import java.util.List;

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
		fromJson.add("  factory " + dartClass.getClassName() + ".fromJson(Map<String, dynamic> json, {Map models}){");
		fromJson.add("    if (models == null) {");
		fromJson.add("      models = {};");
		fromJson.add("    }");
		fromJson.add("    " + dartClass.getClassName() + " toReturn = new " + dartClass.getClassName() + "(");
		for (int x = 0; x < dartClass.getFields().size(); x++) {
			DartField dartField = dartClass.getFields().get(x);
			String name = dartField.name;
			String line;
			if (dartField.type == DartType.CLASS) {
				line = "      " + name + ": models[json['" + name + "']] != null ? models[json['" + name
						+ "']] : new " + dartField.getFieldTypeString() + ".fromJson(json['" + name + "'], models: models)";
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
		fromJson.add("    models[json['@id']] = toReturn;");
		fromJson.add("    return toReturn;");
		fromJson.add("  }");
		return fromJson;
	}

	public List<String> generateToJson(DartClass dartClass) {
		List<String> toJson = new ArrayList<>();
		toJson.add("  /**");
		toJson.add("   * Returns either a Map<String, dynamic> if this is the first reference to this object");
		toJson.add("   * that toJson is called in on this call, or an int if it is not the first");
		toJson.add("   */");
		toJson.add("  Map<String, dynamic> toJson({Map<dynamic, int> models}) {");
		toJson.add("    if (models == null) {");
		toJson.add("      //element mapping to 0 is the index for the next model");
		toJson.add("      models = {0:1};");
		toJson.add("    }");
		toJson.add("    if (models.containsKey(this)) {");
		toJson.add("      return models[this];");
		toJson.add("    } else {");
		toJson.add("      models[this] = models[0];");
		toJson.add("      models[0] = models[0] + 1;");
		toJson.add("      return <String, dynamic>{");
		for (int x = 0; x < dartClass.getFields().size(); x++) {
			DartField field = dartClass.getFields().get(x);
			String name = field.name;
			String line = "          \"" + name + "\": " + name;
			if (field.type == DartType.CLASS) {
				line += ".toJson(models: models)";
			}
			if (x != dartClass.getFields().size() - 1) {
				line += ",";
			}
			toJson.add(line);
		}
		toJson.add("      };");
		toJson.add("    }");
		toJson.add("  }");
		return toJson;
	}
}