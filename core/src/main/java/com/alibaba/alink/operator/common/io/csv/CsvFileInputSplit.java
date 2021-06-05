package com.alibaba.alink.operator.common.io.csv;

import org.apache.flink.core.io.InputSplit;

/**
 * The InputSplit for reading CSV files.
 */
public class CsvFileInputSplit implements InputSplit {

	/**
	 * Starting position of this split.
	 */
	public long start;

	/**
	 * Length of this split.
	 */
	public long length;

	/**
	 * The ending position of this split.
	 */
	public long end;

	private int numSplits;
	private int splitNo;

	/**
	 * The constructor.
	 *
	 * @param numSplits     Number of splits.
	 * @param splitNo       No. of this split.
	 * @param contentLength Total length of the file in bytes.
	 */
	public CsvFileInputSplit(int numSplits, int splitNo, long contentLength) {
		this.numSplits = numSplits;
		this.splitNo = splitNo;

		long avg = contentLength / numSplits;
		long remain = contentLength % numSplits;
		this.length = avg + (splitNo < remain ? 1 : 0);
		this.start = splitNo * avg + Long.min(remain, splitNo);
		long BUFFER_SIZE = 1024L * 1024L;
		this.end = Long.min(start + length + BUFFER_SIZE, contentLength);
	}

	@Override
	public String toString() {
		return "split: " + splitNo + "/" + numSplits + ", " + start + " " + length + " " + end;
	}

	@Override
	public int getSplitNumber() {
		return this.splitNo;
	}
}
