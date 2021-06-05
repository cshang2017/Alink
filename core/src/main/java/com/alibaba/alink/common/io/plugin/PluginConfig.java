package com.alibaba.alink.common.io.plugin;

import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.CoreOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

/**
 * Stores the configuration for plugins mechanism.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class PluginConfig {
	private static final Logger LOG = LoggerFactory.getLogger(PluginConfig.class);

	private final Optional<Path> pluginsPath;

	private final String[] alwaysParentFirstPatterns;

	private PluginConfig(Optional<Path> pluginsPath, String[] alwaysParentFirstPatterns) {
		this.pluginsPath = pluginsPath;
		this.alwaysParentFirstPatterns = alwaysParentFirstPatterns;
	}

	public Optional<Path> getPluginsPath() {
		return pluginsPath;
	}

	public String[] getAlwaysParentFirstPatterns() {
		return alwaysParentFirstPatterns;
	}

	public static PluginConfig fromConfiguration(Configuration configuration) {
		return new PluginConfig(
			getPluginsDirPath(configuration),
			CoreOptions.getParentFirstLoaderPatterns(configuration));
	}

	public static final String DEFAULT_FLINK_PLUGINS_DIRS = "plugins";

	private static Optional<Path> getPluginsDirPath(Configuration configuration) {
		Map <String, String> env = System.getenv();

		String pluginsDir = env.getOrDefault(ConfigConstants.ENV_FLINK_PLUGINS_DIR, DEFAULT_FLINK_PLUGINS_DIRS);

		if (!env.containsKey(ConfigConstants.ENV_FLINK_PLUGINS_DIR)
			&& configuration.containsKey(ConfigConstants.ENV_FLINK_PLUGINS_DIR)) {

			pluginsDir = configuration.getString(ConfigConstants.ENV_FLINK_PLUGINS_DIR, null);
		}

		if (pluginsDir == null) {
			LOG.info("Environment variable [{}] is not set", ConfigConstants.ENV_FLINK_PLUGINS_DIR);
			return Optional.empty();
		}

		File pluginsDirFile = new File(pluginsDir);
		if (!pluginsDirFile.isDirectory()) {
			LOG.warn("Environment variable [{}] is set to [{}] but the directory doesn't exist",
				ConfigConstants.ENV_FLINK_PLUGINS_DIR,
				pluginsDir);
			return Optional.empty();
		}
		return Optional.of(pluginsDirFile.toPath());
	}
}
