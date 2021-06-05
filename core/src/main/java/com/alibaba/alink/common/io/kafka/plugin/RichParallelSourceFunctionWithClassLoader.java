package com.alibaba.alink.common.io.kafka.plugin;

import org.apache.flink.api.common.functions.IterationRuntimeContext;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.CheckpointListener;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.types.Row;
import org.apache.flink.util.InstantiationUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RichParallelSourceFunctionWithClassLoader extends RichParallelSourceFunction <Row> implements
	CheckpointListener,
	ResultTypeQueryable <Row>,
	CheckpointedFunction {

	private final KafkaClassLoaderFactory factory;
	private final byte[] serializedRichParallelSourceFunction;

	private transient RichParallelSourceFunction <Row> internal;

	public RichParallelSourceFunctionWithClassLoader(
		KafkaClassLoaderFactory factory,
		RichParallelSourceFunction <Row> internal) {

		this.factory = factory;
		this.internal = internal;

			serializedRichParallelSourceFunction = InstantiationUtil.serializeObject(internal);
	}

	private RichParallelSourceFunction <Row> getRichParallelSourceFunction() {

		if (internal == null) {
				internal = InstantiationUtil.deserializeObject(serializedRichParallelSourceFunction, factory.create());
		}

		return internal;
	}

	@Override
	public void setRuntimeContext(RuntimeContext t) {
		factory.doAsThrowRuntime(() -> getRichParallelSourceFunction().setRuntimeContext(t));
	}

	@Override
	public RuntimeContext getRuntimeContext() {
		return factory.doAsThrowRuntime(getRichParallelSourceFunction()::getRuntimeContext);
	}

	@Override
	public IterationRuntimeContext getIterationRuntimeContext() {
		return factory.doAsThrowRuntime(getRichParallelSourceFunction()::getIterationRuntimeContext);
	}

	@Override
	public void open(Configuration parameters) throws Exception {
		factory.doAsThrowRuntime(() -> getRichParallelSourceFunction().open(parameters));
	}

	@Override
	public void close() throws Exception {
		factory.doAsThrowRuntime(getRichParallelSourceFunction()::close);
	}

	@Override
	public TypeInformation <Row> getProducedType() {
		if (internal instanceof ResultTypeQueryable) {
			return factory.doAsThrowRuntime(((ResultTypeQueryable <Row>) getRichParallelSourceFunction())::getProducedType);
		} else {
			throw new IllegalStateException("Internal is not the ResultTypeQueryable.");
		}
	}

	@Override
	public void notifyCheckpointComplete(long checkpointId) throws Exception {
		if (internal instanceof CheckpointListener) {
			factory.doAsThrowRuntime(() -> (CheckpointListener) getRichParallelSourceFunction()).notifyCheckpointComplete(checkpointId);
		}
	}

	@Override
	public void snapshotState(FunctionSnapshotContext context) throws Exception {
		if (internal instanceof CheckpointedFunction) {
			factory.doAsThrowRuntime(() -> (CheckpointedFunction) getRichParallelSourceFunction()).snapshotState(context);
		}
	}

	@Override
	public void initializeState(FunctionInitializationContext context) throws Exception {
		if (internal instanceof CheckpointedFunction) {
			factory.doAsThrowRuntime(() -> (CheckpointedFunction) getRichParallelSourceFunction()).initializeState(context);
		}
	}

	@Override
	public void run(SourceContext <Row> ctx) throws Exception {
		factory.doAsThrowRuntime(() -> getRichParallelSourceFunction().run(ctx));
	}

	@Override
	public void cancel() {
		factory.doAsThrowRuntime(getRichParallelSourceFunction()::cancel);
	}
}
