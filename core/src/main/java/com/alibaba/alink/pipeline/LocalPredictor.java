package com.alibaba.alink.pipeline;

import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;
import org.apache.flink.util.Preconditions;

import com.alibaba.alink.common.io.filesystem.FilePath;
import com.alibaba.alink.common.mapper.Mapper;
import com.alibaba.alink.operator.common.io.csv.CsvUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A LocalPredictor which is generated from {@link LocalPredictable} predict an instance to one or more instances using
 * map or flatMap accordingly.
 * <p>
 * The most important feature of LocalPredictor is that it can run at local and thus we can deploy the predictor to
 * another system.
 */
public class LocalPredictor {
	private final ArrayList <Mapper> mappers = new ArrayList <>();

	public LocalPredictor(String pipelineModelPath, String inputSchemaStr) throws Exception {
		this(new FilePath(pipelineModelPath), CsvUtil.schemaStr2Schema(inputSchemaStr));
	}

	public LocalPredictor(FilePath pipelineModelPath, String inputSchemaStr) throws Exception {
		this(pipelineModelPath, CsvUtil.schemaStr2Schema(inputSchemaStr));
	}

	public LocalPredictor(FilePath pipelineModelPath, TableSchema inputSchema)  {
		this(
			
				ModelExporterUtils
					.loadLocalPredictorFromPipelineModel(
						pipelineModelPath, inputSchema
					).mappers.toArray(new Mapper[0])
		);
	}

	public LocalPredictor(List <Row> pipelineModel, TableSchema modelSchema, TableSchema inputSchema)
		throws Exception {
		this(
			ModelExporterUtils
					.loadLocalPredictorFromPipelineModel(
						pipelineModel, modelSchema, inputSchema
					).mappers.toArray(new Mapper[0])
		);
	}

	public LocalPredictor(Mapper... mappers) {

		this.mappers.addAll(Arrays.asList(mappers));
	}

	public void merge(LocalPredictor otherPredictor) {
		this.mappers.addAll(otherPredictor.mappers);
	}

	public TableSchema getOutputSchema() {
		if (mappers.size() > 0) {
			return mappers.get(mappers.size() - 1).getOutputSchema();
		} else {
			return null;
		}
	}

	/**
	 * map operation method that maps a row to a new row.
	 *
	 * @param row the input Row type data
	 * @return one Row type data
	 * @throws Exception This method may throw exceptions. Throwing an exception will cause the operation to fail.
	 */
	public Row map(Row row) throws Exception {
		Row r = row;
		for (Mapper mapper : mappers) {
			r = mapper.map(r);
		}
		return r;
	}

	public void open() {
		this.mappers.forEach(Mapper::open);
	}

	public void close() {
		this.mappers.forEach(Mapper::close);
	}

}
