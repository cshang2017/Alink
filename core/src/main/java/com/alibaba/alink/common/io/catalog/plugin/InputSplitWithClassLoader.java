package com.alibaba.alink.common.io.catalog.plugin;

import org.apache.flink.core.io.InputSplit;
import org.apache.flink.util.InstantiationUtil;

import com.alibaba.alink.common.io.plugin.ClassLoaderFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class InputSplitWithClassLoader implements InputSplit {
	private final ClassLoaderFactory factory;
	private final byte[] serializedInputSplit;

	private transient InputSplit inputSplit;

	public InputSplitWithClassLoader(ClassLoaderFactory factory, InputSplit inputSplit) {
		this.factory = factory;
		this.inputSplit = inputSplit;

			serializedInputSplit = InstantiationUtil.serializeObject(inputSplit);
	}

	public InputSplit getInputSplit() {

		if (inputSplit == null) {
				inputSplit = InstantiationUtil.deserializeObject(serializedInputSplit, factory.create());
		}

		return inputSplit;
	}

	@Override
	public int getSplitNumber() {
		return getInputSplit().getSplitNumber();
	}
}
