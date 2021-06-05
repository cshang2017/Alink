package com.alibaba.alink.common.io.filesystem.copy.bucket;

import org.apache.flink.annotation.Internal;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.fs.RecoverableWriter;
import org.apache.flink.streaming.api.functions.sink.filesystem.RollingPolicy;

import java.io.IOException;

/**
 * A factory returning {@link Bucket buckets}.
 */
@Internal
class DefaultBucketFactoryImpl<IN, BucketID> implements BucketFactory <IN, BucketID> {

	private static final long serialVersionUID = 1L;

	@Override
	public Bucket <IN, BucketID> getNewBucket(
		final RecoverableWriter fsWriter,
		final int subtaskIndex,
		final BucketID bucketId,
		final Path bucketPath,
		final long initialPartCounter,
		final PartFileWriter.PartFileFactory <IN, BucketID> partFileWriterFactory,
		final RollingPolicy <IN, BucketID> rollingPolicy) {

		return Bucket.getNew(
			fsWriter,
			subtaskIndex,
			bucketId,
			bucketPath,
			initialPartCounter,
			partFileWriterFactory,
			rollingPolicy);
	}

	@Override
	public Bucket <IN, BucketID> restoreBucket(
		final RecoverableWriter fsWriter,
		final int subtaskIndex,
		final long initialPartCounter,
		final PartFileWriter.PartFileFactory <IN, BucketID> partFileWriterFactory,
		final RollingPolicy <IN, BucketID> rollingPolicy,
		final BucketState <BucketID> bucketState) throws IOException {

		return Bucket.restore(
			fsWriter,
			subtaskIndex,
			initialPartCounter,
			partFileWriterFactory,
			rollingPolicy,
			bucketState);
	}
}
