package com.alibaba.alink.common.io.filesystem.copy.bucket;

import org.apache.flink.annotation.Internal;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.fs.RecoverableWriter;
import org.apache.flink.streaming.api.functions.sink.filesystem.Bucket;
import org.apache.flink.util.Preconditions;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * The state of the {@link Bucket} that is to be checkpointed.
 */
@Internal
class BucketState<BucketID> {

	private final BucketID bucketId;

	/**
	 * The directory where all the part files of the bucket are stored.
	 */
	private final Path bucketPath;

	/**
	 * The creation time of the currently open part file,
	 * or {@code Long.MAX_VALUE} if there is no open part file.
	 */
	private final long inProgressFileCreationTime;

	/**
	 * A {@link RecoverableWriter.ResumeRecoverable} for the currently open
	 * part file, or null if there is no currently open part file.
	 */
	@Nullable
	private final RecoverableWriter.ResumeRecoverable inProgressResumableFile;

	/**
	 * The {@link RecoverableWriter.CommitRecoverable files} pending to be
	 * committed, organized by checkpoint id.
	 */
	private final Map <Long, List <RecoverableWriter.CommitRecoverable>> committableFilesPerCheckpoint;

	BucketState(
		final BucketID bucketId,
		final Path bucketPath,
		final long inProgressFileCreationTime,
		@Nullable final RecoverableWriter.ResumeRecoverable inProgressResumableFile,
		final Map <Long, List <RecoverableWriter.CommitRecoverable>> pendingCommittablesPerCheckpoint
	) {
		this.bucketId = Preconditions.checkNotNull(bucketId);
		this.bucketPath = Preconditions.checkNotNull(bucketPath);
		this.inProgressFileCreationTime = inProgressFileCreationTime;
		this.inProgressResumableFile = inProgressResumableFile;
		this.committableFilesPerCheckpoint = Preconditions.checkNotNull(pendingCommittablesPerCheckpoint);
	}

	BucketID getBucketId() {
		return bucketId;
	}

	Path getBucketPath() {
		return bucketPath;
	}

	long getInProgressFileCreationTime() {
		return inProgressFileCreationTime;
	}

	boolean hasInProgressResumableFile() {
		return inProgressResumableFile != null;
	}

	@Nullable
	RecoverableWriter.ResumeRecoverable getInProgressResumableFile() {
		return inProgressResumableFile;
	}

	Map <Long, List <RecoverableWriter.CommitRecoverable>> getCommittableFilesPerCheckpoint() {
		return committableFilesPerCheckpoint;
	}

	@Override
	public String toString() {
		final StringBuilder strBuilder = new StringBuilder();

		strBuilder
			.append("BucketState for bucketId=").append(bucketId)
			.append(" and bucketPath=").append(bucketPath);

		if (hasInProgressResumableFile()) {
			strBuilder.append(", has open part file created @ ").append(inProgressFileCreationTime);
		}

		if (!committableFilesPerCheckpoint.isEmpty()) {
			strBuilder.append(", has pending files for checkpoints: {");
			for (long checkpointId : committableFilesPerCheckpoint.keySet()) {
				strBuilder.append(checkpointId).append(' ');
			}
			strBuilder.append('}');
		}
		return strBuilder.toString();
	}
}
