package com.alibaba.alink.pipeline.similarity;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.common.similarity.NearestNeighborsMapper;
import com.alibaba.alink.params.similarity.NearestNeighborPredictParams;
import com.alibaba.alink.pipeline.MapModel;

/**
 * String approximate nearest neighbor pipeline model.
 */
public class StringApproxNearestNeighborModel extends MapModel <StringApproxNearestNeighborModel>
	implements NearestNeighborPredictParams <StringApproxNearestNeighborModel> {

	public StringApproxNearestNeighborModel() {
		this(null);
	}

	public StringApproxNearestNeighborModel(Params params) {
		super(NearestNeighborsMapper::new, params);
	}
}
