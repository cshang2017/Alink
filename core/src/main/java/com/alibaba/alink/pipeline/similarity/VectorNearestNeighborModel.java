package com.alibaba.alink.pipeline.similarity;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.common.similarity.NearestNeighborsMapper;
import com.alibaba.alink.params.similarity.NearestNeighborPredictParams;
import com.alibaba.alink.pipeline.MapModel;

/**
 * Vector nearest neighbor pipeline model.
 */
public class VectorNearestNeighborModel extends MapModel <VectorNearestNeighborModel>
	implements NearestNeighborPredictParams <VectorNearestNeighborModel> {

	public VectorNearestNeighborModel() {
		this(null);
	}

	public VectorNearestNeighborModel(Params params) {
		super(NearestNeighborsMapper::new, params);
	}
}
