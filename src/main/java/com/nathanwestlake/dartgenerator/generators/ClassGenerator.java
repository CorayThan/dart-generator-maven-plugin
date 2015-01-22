package com.nathanwestlake.dartgenerator.generators;

import com.nathanwestlake.dartgenerator.models.DartClass;
import com.nathanwestlake.dartgenerator.models.DartField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorayThan on 1/21/2015.
 */
public class ClassGenerator {
	private MethodGenerator methodGenerator = new MethodGenerator();

	public List<String> generate(DartClass dartClass) {
		List<String> lines = new ArrayList<String>();
		List<String> imports = new ArrayList<String>();

		lines.add("library " + dartClass.getLibraryName() + ";");
		lines.add("");

		lines.add("class " + dartClass.getClassName() + " extends Jsonable {");
		for (DartField field : dartClass.getFields()) {
			lines.add(field.getAsString());
		}
		lines.add("}");
		return lines;
	}
}
