package com.alibaba.alink.common.io.plugin;

import java.net.URL;
import java.util.Arrays;

/**
 * Descriptive meta information for a plugin.
 */
public class PluginDescriptor {

	/** Unique identifier of the plugin. */
	private final String pluginId;

	/** URLs to the plugin resources code. Usually this contains URLs of the jars that will be loaded for the plugin. */
	private final URL[] pluginResourceURLs;

	private final String[] allowedFlinkPackages;

	private final String version;

	public PluginDescriptor(String pluginId, URL[] pluginResourceURLs, String[] allowedFlinkPackages, final String version) {
		this.pluginId = pluginId;
		this.pluginResourceURLs = pluginResourceURLs;
		this.allowedFlinkPackages = allowedFlinkPackages;
		this.version = version;
	}

	public String getPluginId() {
		return pluginId;
	}

	public URL[] getPluginResourceURLs() {
		return pluginResourceURLs;
	}

	public String[] getAllowedFlinkPackages() {
		return allowedFlinkPackages;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return "PluginDescriptor{" +
			"pluginId='" + pluginId + '\'' +
			", pluginResourceURLs=" + Arrays.toString(pluginResourceURLs) +
			", loaderExcludePatterns=" + Arrays.toString(allowedFlinkPackages) +
			", version=" + version +
			'}';
	}
}
