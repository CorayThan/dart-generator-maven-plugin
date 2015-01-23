package com.nathanwestlake.dartgenerator.generators;

import com.google.common.collect.Lists;
import com.nathanwestlake.dartgenerator.models.DartClass;

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
		fromJson.add("  " + dartClass.getClassName() + ".fromJson(Map<String, dynamic> json):");
		fromJson.add("    this(");
		for (int x = 0; x < dartClass.getFields().size(); x++) {
			String name = dartClass.getFields().get(x).name;
			String line = "      " + name + ": json['" + name + "']";
			if (x != dartClass.getFields().size() - 1) {
				line += ",";
			}
			fromJson.add(line);
		}
		fromJson.add("      );");
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