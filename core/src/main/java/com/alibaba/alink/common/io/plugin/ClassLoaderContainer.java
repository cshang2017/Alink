package com.alibaba.alink.common.io.plugin;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;

import com.alibaba.alink.common.AlinkGlobalConfiguration;
import com.alibaba.alink.common.utils.JsonConverter;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClassLoaderContainer {

	private static final ClassLoaderContainer INSTANCE = new ClassLoaderContainer();

	public synchronized static ClassLoaderContainer getInstance() {
		return INSTANCE;
	}

	private PluginManager pluginManager;
	private final Map <RegisterKey, ClassLoader> registeredClassLoaders = new HashMap <>();

	public static Map <String, String> createPluginContextOnClient() {
		return ImmutableMap.<String, String>builder()
			.put(ConfigConstants.ENV_FLINK_PLUGINS_DIR, AlinkGlobalConfiguration.getPluginDir())
			.build();
	}

	private ClassLoaderContainer() {
	}

	public synchronized <T> ClassLoader create(
		RegisterKey key,
		Map <String, String> context,
		Class <T> service,
		Predicate <T> serviceFilter,
		Function <Tuple2 <T, PluginDescriptor>, String> versionGetter) {

		ClassLoader hit = registeredClassLoaders.get(key);

		if (hit != null) {
			return hit;
		}

		hit = registeredClassLoaders.get(new RegisterKey(key.getName(), null));

		if (hit != null) {
			return hit;
		}

		hit = loadFromPlugin(key, context, service, serviceFilter, versionGetter);

		if (hit != null) {
			return hit;
		}


		return Thread.currentThread().getContextClassLoader();
	}

	private <T> ClassLoader filterFromServices(RegisterKey key, List <Tuple2 <T, PluginDescriptor>> loadedServices,
											   Function <Tuple2 <T, PluginDescriptor>, String> versionGetter) {
		if (!loadedServices.isEmpty()) {

			ClassLoader hit = null;

			if (key.getVersion() == null) {
				hit = loadedServices.get(0).f0.getClass().getClassLoader();
			} else {
				for (Tuple2 <T, PluginDescriptor> loaded : loadedServices) {
					String version = versionGetter.apply(loaded);

					if (version.compareToIgnoreCase(key.getVersion()) == 0) {
						hit = loaded.f0.getClass().getClassLoader();
						break;
					}
				}
			}

			if (hit == null) {

				if (loadedServices.size() > 1) {
				}

				key = new RegisterKey(key.getName(), null);

				hit = loadedServices.get(0).f0.getClass().getClassLoader();
			}

			registeredClassLoaders.put(key, hit);

			return hit;
		}

		return null;
	}

	private <T> ClassLoader loadFromPlugin(
		RegisterKey key,
		Map <String, String> context,
		Class <T> service,
		Predicate <T> serviceFilter,
		Function <Tuple2 <T, PluginDescriptor>, String> versionGetter) {

		final List <Tuple2 <T, PluginDescriptor>> loadedServices = new ArrayList <>();

		// from plugin
		if (pluginManager == null) {
			pluginManager = PluginUtils.createPluginManagerFromRootFolder(readPluginConf(context));
		}

			pluginManager
				.load(service, AlinkGlobalConfiguration.getFlinkVersion(), key.getName(), key.getVersion())
				.forEachRemaining(t -> {
					if (serviceFilter.test(t.f0)) {
						loadedServices.add(t);
					}
				});

			return filterFromServices(key, loadedServices, versionGetter);
	}

	private static Configuration readPluginConf(Map <String, String> context) {

		Configuration configuration;

		if (context.isEmpty()) {
			// Run in flink console, user should set the plugin follow the configuration of flink.
			configuration = org.apache.flink.configuration.GlobalConfiguration.loadConfiguration().clone();
		} else {
			// Run in Local and RemoteEnv in PyAlink
			configuration = new Configuration();

			for (Map.Entry<String, String> entry : context.entrySet()) {
				configuration.setString(entry.getKey(), entry.getValue());
			}
		}

		return configuration;
	}
}
