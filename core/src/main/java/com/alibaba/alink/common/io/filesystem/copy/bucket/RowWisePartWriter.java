package com.alibaba.alink.common.io.filesystem.copy.bucket;

import org.apache.flink.annotation.Internal;
import org.apache.flink.api.common.serialization.Encoder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.fs.RecoverableFsDataOutputStream;
import org.apache.flink.core.fs.RecoverableWriter;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.PartFileInfo;
import org.apache.flink.util.Preconditions;

import java.io.IOException;

/**
 * A {@link PartFileWriter} for row-wise formats that use an {@link Encoder}.
 * This also implements the {@link PartFileInfo}.
 */
@Internal
final class RowWisePartWriter<IN, BucketID> extends PartFileWriter <IN, BucketID> {

	private final Encoder <IN> encoder;

	private RowWisePartWriter(
		final BucketID bucketId,
		final RecoverableFsDataOutputStream currentPartStream,
		final Encoder <IN> encoder,
		final long creationTime) {
		super(bucketId, currentPartStream, creationTime);
		this.encoder = Preconditions.checkNotNull(encoder);
	}

	@Override
	void write(IN element, long currentTime) throws IOException {
		encoder.encode(element, currentPartStream);
		markWrite(currentTime);
	}

	/**
	 * A factory that creates {@link RowWisePartWriter RowWisePartWriters}.
	 *
	 * @param <IN>       The type of input elements.
	 * @param <BucketID> The type of ids for the buckets, as returned by the {@link BucketAssigner}.
	 */
	static class Factory<IN, BucketID> implements PartFileWriter.PartFileFactory <IN, BucketID> {

		private final Encoder <IN> encoder;

		Factory(Encoder <IN> encoder) {
			this.encoder = encoder;
		}

		@Override
		public PartFileWriter <IN, BucketID> resumeFrom(
			final BucketID bucketId,
			final RecoverableFsDataOutputStream stream,
			final RecoverableWriter.ResumeRecoverable resumable,
			final long creationTime) throws IOException {

			Preconditions.checkNotNull(stream);
			Preconditions.checkNotNull(resumable);

			return new RowWisePartWriter <>(bucketId, stream, encoder, creationTime);
		}

		@Override
		public PartFileWriter <IN, BucketID> openNew(
			final BucketID bucketId,
			final RecoverableFsDataOutputStream stream,
			final Path path,
			final long creationTime) throws IOException {

			Preconditions.checkNotNull(stream);
			Preconditions.checkNotNull(path);

			return new RowWisePartWriter <>(bucketId, stream, encoder, creationTime);
		}
	}
}
