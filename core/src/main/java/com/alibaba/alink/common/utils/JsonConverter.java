package com.alibaba.alink.common.utils;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.PropertyAccessor;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Wrapper class of a JSON library to provide the unified json format.
 *
 * <p>Currently, we use jackson as the json library.
 */
public class JsonConverter {
	/**
	 * An instance of ObjectMapper in jackson.
	 */
	private static final ObjectMapper JSON_INSTANCE = new ObjectMapper()
		.setVisibility(PropertyAccessor.ALL, Visibility.NONE)
		.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

	/**
	 * serialize an object to json.
	 *
	 * @param src the object serialized as json
	 * @return the json string
	 */
	public static String toJson(Object src) {
			return JSON_INSTANCE.writeValueAsString(src);
	}

	/**
	 * serialize an object to pretty json.
	 *
	 * @param src the object serialized as json
	 * @return the json string
	 */
	public static String toPrettyJson(Object src) {
			return JSON_INSTANCE.writerWithDefaultPrettyPrinter().writeValueAsString(src);
	}

	/**
	 * Deserialize an object from json using the {@link Class} of object.
	 *
	 * @param json     the json string
	 * @param classOfT the class of object
	 * @param <T>      the type of object
	 * @return the deserialized object
	 */
	public static <T> T fromJson(String json, Class <T> classOfT) {
		return fromJson(json, (Type) classOfT);
	}

	/**
	 * Deserialize an object from json using the {@link Type} of object.
	 *
	 * @param json    the json string
	 * @param typeOfT the type class of object
	 * @param <T>     the type of object
	 * @return the deserialized object
	 */
	public static <T> T fromJson(String json, Type typeOfT) {
		return JSON_INSTANCE.readValue(json, JSON_INSTANCE.getTypeFactory().constructType(typeOfT));
	}

	/**
	 * Exception to indict the json format error.
	 */
	public static class IllegalJsonFormatException extends IllegalArgumentException {

		IllegalJsonFormatException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	/* open ends here */

	@Deprecated
	public static Gson gson = new GsonBuilder().disableHtmlEscaping().serializeSpecialFloatingPointValues().create();

}
