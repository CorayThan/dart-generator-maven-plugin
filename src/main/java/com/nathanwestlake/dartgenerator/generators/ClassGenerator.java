package com.nathanwestlake.dartgenerator.generators;

import com.google.common.collect.Lists;
import com.nathanwestlake.dartgenerator.models.DartEntity;

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
	private static final List<String> MODEL_MAPPER = Lists.newArrayList(
			"library model_mapper;",
			"",
			"import 'dart:convert';",
			"",
			"/**",
			" * This class will create a map of models to their ids. It is used to recreate complex cyclical references from JSON to",
			" * dart classes. For it to do so, the JSON must be formatted in a specific way, as described here:/",
			" *",
			" * http://wiki.fasterxml.com/JacksonFeatureObjectIdentity",
			" *",
			" * Every object should contain a property '@id' which contains an integer id that uniquely references that object within",
			" * the JSON. If that object would appear a second time in the JSON, instead only that integer id should be the value.",
			"*/",
			"class ModelMapper {"
	);

	public List<String> generate(DartEntity dartClass, Map<String, DartEntity> classMap) {
		return dartClass.generate(classMap);
	}

	public List<String> generateJsonable() {
		return JSONABLE;
	}
}