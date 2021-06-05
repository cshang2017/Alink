package com.alibaba.alink.common.io.filesystem.copy.bucket;

import org.apache.flink.annotation.Internal;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.fs.RecoverableWriter;
import org.apache.flink.streaming.api.functions.sink.filesystem.RollingPolicy;

import java.io.IOException;
import java.io.Serializable;

/**
 * A factory able to create {@link Bucket buckets} for the {@link StreamingFileSink}.
 */
@Internal
interface BucketFactory<IN, BucketID> extends Serializable {

	Bucket <IN, BucketID> getNewBucket(
		final RecoverableWriter fsWriter,
		final int subtaskIndex,
		final BucketID bucketId,
		final Path bucketPath,
		final long initialPartCounter,
		final PartFileWriter.PartFileFactory <IN, BucketID> partFileWriterFactory,
		final RollingPolicy <IN, BucketID> rollingPolicy) throws IOException;

	Bucket <IN, BucketID> restoreBucket(
		final RecoverableWriter fsWriter,
		final int subtaskIndex,
		final long initialPartCounter,
		final PartFileWriter.PartFileFactory <IN, BucketID> partFileWriterFactory,
		final RollingPolicy <IN, BucketID> rollingPolicy,
		final BucketState <BucketID> bucketState) throws IOException;
}
