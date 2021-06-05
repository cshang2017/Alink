package com.alibaba.alink.common.io.plugin;

import org.apache.flink.configuration.Configuration;

import java.nio.file.Paths;

/**
 * Utility functions for the plugin mechanism.
 */
public final class PluginUtils {

	private PluginUtils() {
		throw new AssertionError("Singleton class.");
	}

	public static PluginManager createPluginManagerFromRootFolder(Configuration configuration) {
		return createPluginManagerFromRootFolder(PluginConfig.fromConfiguration(configuration));
	}

	private static PluginManager createPluginManagerFromRootFolder(PluginConfig pluginConfig) {
		if (pluginConfig.getPluginsPath().isPresent()) {
			return new PluginManager(
				new PluginDirectory(pluginConfig.getPluginsPath().get()), pluginConfig.getAlwaysParentFirstPatterns()
			);
		} else {
			return new PluginManager(
				new PluginDirectory(Paths.get(PluginConfig.DEFAULT_FLINK_PLUGINS_DIRS)),
				pluginConfig.getAlwaysParentFirstPatterns()
			);
		}
	}
}
