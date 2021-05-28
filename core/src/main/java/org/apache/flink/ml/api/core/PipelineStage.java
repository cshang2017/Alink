package org.apache.flink.ml.api.core;

import org.apache.flink.ml.api.misc.param.WithParams;

import java.io.Serializable;

/**
 * Base class for a stage in a pipeline. The interface is only a concept, and does not have any
 * actual functionality. Its subclasses must be either Estimator or Transformer. No other classes
 * should inherit this interface directly.
 *
 * <p>Each pipeline stage is with parameters, and requires a public empty constructor for
 * restoration in Pipeline.
 *
 * @param <T> The class type of the PipelineStage implementation itself, used by {@link
 *            org.apache.flink.ml.api.misc.param.WithParams}
 * @see WithParams
 */
public interface PipelineStage<T extends PipelineStage <T>> extends WithParams <T>, Serializable {

	default String toJson() {
		return getParams().toJson();
	}

	default void loadJson(String json) {
		getParams().loadJson(json);
	}
}
