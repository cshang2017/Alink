package com.alibaba.alink.pipeline.similarity;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.similarity.VectorApproxNearestNeighborTrainBatchOp;
import com.alibaba.alink.params.similarity.NearestNeighborPredictParams;
import com.alibaba.alink.params.similarity.VectorApproxNearestNeighborTrainParams;
import com.alibaba.alink.pipeline.Trainer;

/**
 * Find the approximate nearest neighbor of query vectors.
 */
public class VectorApproxNearestNeighbor
	extends Trainer <VectorApproxNearestNeighbor, VectorApproxNearestNeighborModel>
	implements VectorApproxNearestNeighborTrainParams<VectorApproxNearestNeighbor>,
	NearestNeighborPredictParams <VectorApproxNearestNeighbor> {

	public VectorApproxNearestNeighbor() {
		this(null);
	}

	public VectorApproxNearestNeighbor(Params params) {
		super(params);
	}

	@Override
	public BatchOperator <?> train(BatchOperator <?> in) {
		return new VectorApproxNearestNeighborTrainBatchOp(this.getParams()).linkFrom(in);
	}
}
