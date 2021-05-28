
package org.apache.flink.ml.api.core;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;

/**
 * Estimators are {@link PipelineStage}s responsible for training and generating machine learning
 * models.
 *
 * <p>The implementations are expected to take an input table as training samples and generate a
 * {@link Model} which fits these samples.
 *
 * @param <E> class type of the Estimator implementation itself, used by {@link
 *            org.apache.flink.ml.api.misc.param.WithParams}.
 * @param <M> class type of the {@link Model} this Estimator produces.
 */
@PublicEvolving
public interface Estimator<E extends Estimator <E, M>, M extends Model <M>> extends PipelineStage <E> {

	/**
	 * Train and produce a {@link Model} which fits the records in the given {@link Table}.
	 *
	 * @param tEnv  the table environment to which the input table is bound.
	 * @param input the table with records to train the Model.
	 * @return a model trained to fit on the given Table.
	 */
	M fit(TableEnvironment tEnv, Table input);
}
