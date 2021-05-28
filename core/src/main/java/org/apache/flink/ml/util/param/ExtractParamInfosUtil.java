
package org.apache.flink.ml.util.param;

import org.apache.flink.ml.api.misc.param.ParamInfo;
import org.apache.flink.ml.api.misc.param.WithParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to extract all ParamInfos defined in a WithParams, mainly used in persistence.
 */
public final class ExtractParamInfosUtil {
	/**
	 * Extracts all ParamInfos defined in the given WithParams, including those in its superclasses
	 * and interfaces.
	 *
	 * @param s the WithParams to extract ParamInfos from
	 * @return the list of all ParamInfos defined in s
	 */
	public static List <ParamInfo> extractParamInfos(WithParams s) {
		return extractParamInfos(s, s.getClass());
	}

	private static List <ParamInfo> extractParamInfos(WithParams s, Class clz) {
		List <ParamInfo> result = new ArrayList <>();
		if (clz == null) {
			return result;
		}

		Field[] fields = clz.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			if (ParamInfo.class.isAssignableFrom(f.getType())) {
					result.add((ParamInfo) f.get(s));
			}
		}

		result.addAll(extractParamInfos(s, clz.getSuperclass()));
		for (Class c : clz.getInterfaces()) {
			result.addAll(extractParamInfos(s, c));
		}

		return result;
	}
}
