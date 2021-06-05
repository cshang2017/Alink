package com.alibaba.alink.pipeline.feature;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.common.feature.QuantileDiscretizerModelMapper;
import com.alibaba.alink.params.feature.QuantileDiscretizerPredictParams;
import com.alibaba.alink.pipeline.MapModel;

/**
 * It is quantile discretizer model.
 */
public class QuantileDiscretizerModel extends MapModel <QuantileDiscretizerModel>
	implements QuantileDiscretizerPredictParams <QuantileDiscretizerModel> {

	
	public QuantileDiscretizerModel() {
		this(null);
	}

	public QuantileDiscretizerModel(Params params) {
		super(QuantileDiscretizerModelMapper::new, params);
	}
}
