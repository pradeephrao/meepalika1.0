package com.meepalika.dao.queryhelper;

import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.ImmutableMap;

public class Queries {
	private String queryYml;
	private Map<String, String> queries;

	@SuppressWarnings("unchecked")
	public Queries(String queryYml) {
		this.queryYml = queryYml;
		try (InputStream stream = Queries.class.getResourceAsStream(queryYml);) {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			Map<String, String> temp = (Map<String, String>) mapper.readValue(stream, Map.class);
			this.queries = new ImmutableMap.Builder<String, String>().putAll(temp).build();

		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public String getQuery(String key) {
		return this.queries.get(key);
	}

	public Map<String, String> getQueries() {
		return this.queries;
	}

	public String getQueryYml() {
		return this.queryYml;
	}

}
