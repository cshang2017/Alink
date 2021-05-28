package org.apache.flink.ml.api.misc.param;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.util.Preconditions;

/**
 * Definition of a parameter, including name, type, default value, validator and so on.
 *
 * <p>A parameter can either be optional or non-optional.
 * <ul>
 * <li>
 * A non-optional parameter should not have a default value. Instead, its value must be provided by the users.
 * </li>
 * <li>
 * An optional parameter may or may not have a default value.
 * </li>
 * </ul>
 *
 * <p>Please see {@link Params#get(ParamInfo)} and {@link Params#contains(ParamInfo)} for more details about the
 * behavior.
 *
 * <p>A parameter may have aliases in addition to the parameter name for convenience and compatibility purposes. One
 * should not set values for both parameter name and an alias. One and only one value should be set either under
 * the parameter name or one of the alias.
 *
 * @param <V> the type of the param value
 */
@PublicEvolving
@Getter
public class ParamInfo<V> {
	private final String name;
	private final String[] alias;
	private final String description;
	private final boolean isOptional;
	private final boolean hasDefaultValue;
	private final V defaultValue;
	private final ParamValidator <V> validator;
	private final Class <V> valueClass;

	ParamInfo(String name, String[] alias, String description, boolean isOptional,
			  boolean hasDefaultValue, V defaultValue,
			  ParamValidator <V> validator, Class <V> valueClass) {
		this.name = name;
		this.alias = alias;
		this.description = description;
		this.isOptional = isOptional;
		this.hasDefaultValue = hasDefaultValue;
		this.defaultValue = defaultValue;
		this.validator = validator;
		this.valueClass = valueClass;
	}
}
