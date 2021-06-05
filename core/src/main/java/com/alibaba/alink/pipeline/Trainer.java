package com.alibaba.alink.pipeline;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.common.MLEnvironmentFactory;
import com.alibaba.alink.common.lazy.HasLazyPrintModelInfo;
import com.alibaba.alink.common.lazy.HasLazyPrintTrainInfo;
import com.alibaba.alink.common.lazy.HasLazyPrintTransformInfo;
import com.alibaba.alink.common.lazy.LazyObjectsManager;
import com.alibaba.alink.common.lazy.WithModelInfoBatchOp;
import com.alibaba.alink.common.lazy.WithTrainInfo;
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.stream.StreamOperator;

import java.lang.reflect.ParameterizedType;

import static com.alibaba.alink.common.lazy.HasLazyPrintModelInfo.LAZY_PRINT_MODEL_INFO_ENABLED;
import static com.alibaba.alink.common.lazy.HasLazyPrintModelInfo.LAZY_PRINT_MODEL_INFO_TITLE;
import static com.alibaba.alink.common.lazy.HasLazyPrintTrainInfo.LAZY_PRINT_TRAIN_INFO_ENABLED;
import static com.alibaba.alink.common.lazy.HasLazyPrintTrainInfo.LAZY_PRINT_TRAIN_INFO_TITLE;

/**
 * Abstract class for a trainer that train a machine learning model.
 *
 * The different between {@link EstimatorBase} and {@link Trainer} is that
 * some of {@link EstimatorBase} have its own feature such as some ensemble algorithms,
 * some frequent item set mining algorithms, auto tuning, etc.
 *
 * @param <T> The class type of the {@link Trainer} implementation itself
 * @param <M> class type of the {@link ModelBase} this Trainer produces.
 */
public abstract class Trainer<T extends Trainer <T, M>, M extends ModelBase <M>>
	extends EstimatorBase <T, M> implements HasLazyPrintTransformInfo <T> {

	public Trainer() {
		super();
	}

	public Trainer(Params params) {
		super(params);
	}

	@Override
	public M fit(BatchOperator <?> input) {
		BatchOperator <?> trainer = train(input);
		return createModel(trainer);
	}


	@Override
	public M fit(StreamOperator <?> input) {
		throw new UnsupportedOperationException("Only support batch fit!");
	}

	private M createModel(BatchOperator <?> model) {
			ParameterizedType pt =
				(ParameterizedType) this.getClass().getGenericSuperclass();

			Class <M> classM = (Class <M>) pt.getActualTypeArguments()[1];

			return (M) classM.getConstructor(Params.class)
				.newInstance(getParams())
				.setModelData(model);
	}

	protected abstract BatchOperator <?> train(BatchOperator <?> in);

	protected StreamOperator <?> train(StreamOperator <?> in) {
		throw new UnsupportedOperationException("Only support batch fit!");
	}

}
