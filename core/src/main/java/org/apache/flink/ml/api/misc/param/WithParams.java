package org.apache.flink.ml.api.misc.param;

/**
 * Interface for the object, which need set/get parameters.
 *
 * @param <T> the type of the object
 */
public interface WithParams<T> {

	Params getParams();

	default <V> T set(ParamInfo <V> info, V value) {
			getParams().set(info, value);
		
		return (T) this;
	}

	default <V> V get(ParamInfo <V> info) {
			return getParams().get(info);
	}
}
