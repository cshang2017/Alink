package com.alibaba.alink.pipeline.similarity;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.common.similarity.NearestNeighborsMapper;
import com.alibaba.alink.params.similarity.NearestNeighborPredictParams;
import com.alibaba.alink.pipeline.MapModel;

/**
 * Text approximate nearest neighbor pipeline model.
 */
public class TextApproxNearestNeighborModel extends MapModel <TextApproxNearestNeighborModel>
	implements NearestNeighborPredictParams <TextApproxNearestNeighborModel> {


	public TextApproxNearestNeighborModel() {
		this(null);
	}

	public TextApproxNearestNeighborModel(Params params) {
		super(NearestNeighborsMapper::new, params);
	}
}
