package com.blackbaka.sc.core.config.feign;


import feign.Param;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

/**
 * @Author Kaiyi Zhang
 * @Date 2019/08/13
 * @Description
 */

public class DateExpander implements Param.Expander {

	//thread-safe
	private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	@Override
	public String expand(Object value) {
		if (value instanceof Date) {
			return DATE_FORMAT.format((Date) value);
		} else {
			return value.toString();
		}
	}

}
