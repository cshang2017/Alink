
package org.apache.flink.ml.api.misc.param;

import org.apache.flink.annotation.PublicEvolving;

import java.io.Serializable;

/**
 * An interface used by {@link ParamInfo} to do validation when a parameter value is set.
 *
 * @param <V> the type of the value to validate
 */
@PublicEvolving
public interface ParamValidator<V> extends Serializable {
	/**
	 * Validates a parameter value.
	 *
	 * @param value value to validate
	 * @return {@code true} if the value is valid, {@code false} otherwise
	 */
	boolean validate(V value);

	void validateThrows(V value);

	void setParamName(String paraName);

}
