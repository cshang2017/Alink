
package org.apache.flink.ml.api.core;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;

/**
 * A transformer is a {@link PipelineStage} that transforms an input {@link Table} to a result
 * {@link Table}.
 *
 * @param <T> The class type of the Transformer implementation itself, used by {@link
 *            org.apache.flink.ml.api.misc.param.WithParams}
 */
@PublicEvolving
public interface Transformer<T extends Transformer <T>> extends PipelineStage <T> {
	/**
	 * Applies the transformer on the input table, and returns the result table.
	 *
	 * @param tEnv  the table environment to which the input table is bound.
	 * @param input the table to be transformed
	 * @return the transformed table
	 */
	Table transform(TableEnvironment tEnv, Table input);
}
