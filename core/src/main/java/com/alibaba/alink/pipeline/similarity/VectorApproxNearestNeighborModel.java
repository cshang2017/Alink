package com.alibaba.alink.pipeline.similarity;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.common.similarity.NearestNeighborsMapper;
import com.alibaba.alink.params.similarity.NearestNeighborPredictParams;
import com.alibaba.alink.pipeline.MapModel;

/**
 * Vector approximate nearest neighbor pipeline model.
 */
public class VectorApproxNearestNeighborModel extends MapModel <VectorApproxNearestNeighborModel>
	implements NearestNeighborPredictParams <VectorApproxNearestNeighborModel> {

	public VectorApproxNearestNeighborModel() {
		this(null);
	}

	public VectorApproxNearestNeighborModel(Params params) {
		super(NearestNeighborsMapper::new, params);
	}
}
