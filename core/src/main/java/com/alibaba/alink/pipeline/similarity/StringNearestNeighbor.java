package com.alibaba.alink.pipeline.similarity;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.similarity.StringNearestNeighborTrainBatchOp;
import com.alibaba.alink.params.similarity.NearestNeighborPredictParams;
import com.alibaba.alink.params.similarity.StringTextNearestNeighborTrainParams;
import com.alibaba.alink.pipeline.Trainer;

/**
 * Find the nearest neighbor of query items.
 */
public class StringNearestNeighbor extends Trainer <StringNearestNeighbor, StringNearestNeighborModel>
	implements StringTextNearestNeighborTrainParams<StringNearestNeighbor>,
	NearestNeighborPredictParams <StringNearestNeighbor> {


	public StringNearestNeighbor() {
		this(null);
	}

	public StringNearestNeighbor(Params params) {
		super(params);
	}

	@Override
	protected BatchOperator <?> train(BatchOperator <?> in) {
		return new StringNearestNeighborTrainBatchOp(this.getParams()).linkFrom(in);
	}
}
