
package com.github.slablock.zscheduler.server.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class DefaultObjectMapper extends ObjectMapper {

    public DefaultObjectMapper() {
        this((JsonFactory) null);
    }

    public DefaultObjectMapper(DefaultObjectMapper mapper) {
        super(mapper);
    }

    public DefaultObjectMapper(JsonFactory factory) {
        super(factory);

        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(MapperFeature.AUTO_DETECT_GETTERS, false);
        // See https://github.com/FasterXML/jackson-databind/issues/170
        // configure(MapperFeature.AUTO_DETECT_CREATORS, false);
        configure(MapperFeature.AUTO_DETECT_FIELDS, false);
        configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false);
        configure(MapperFeature.AUTO_DETECT_SETTERS, false);
        configure(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS, false);
        configure(SerializationFeature.INDENT_OUTPUT, false);
        configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, false);
    }

    @Override
    public ObjectMapper copy() {
        return new DefaultObjectMapper(this);
    }
}
