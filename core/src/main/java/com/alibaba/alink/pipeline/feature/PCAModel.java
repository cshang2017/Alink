package com.alibaba.alink.pipeline.feature;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.common.feature.pca.PcaModelMapper;
import com.alibaba.alink.params.feature.PcaPredictParams;
import com.alibaba.alink.pipeline.MapModel;

/**
 * It is pca model.
 */
public class PCAModel extends MapModel <PCAModel>
	implements PcaPredictParams <PCAModel> {

	public PCAModel() {
		this(null);
	}

	public PCAModel(Params params) {
		super(PcaModelMapper::new, params);
	}

}
