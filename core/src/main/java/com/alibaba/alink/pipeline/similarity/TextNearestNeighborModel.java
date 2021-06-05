package com.alibaba.alink.pipeline.similarity;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.common.similarity.NearestNeighborsMapper;
import com.alibaba.alink.params.similarity.NearestNeighborPredictParams;
import com.alibaba.alink.pipeline.MapModel;

/**
 * Text nearest neighbor pipeline model.
 */
public class TextNearestNeighborModel extends MapModel <TextNearestNeighborModel>
	implements NearestNeighborPredictParams <TextNearestNeighborModel> {

	public TextNearestNeighborModel() {
		this(null);
	}

	public TextNearestNeighborModel(Params params) {
		super(NearestNeighborsMapper::new, params);
	}
}
