package com.xzm.monitor.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

public class BaseUtil {
	
	private final SimpleDateFormat sdf_mouth = new SimpleDateFormat("yyyyMM");
	
	private final SimpleDateFormat sdf_day = new SimpleDateFormat("yyyyMMdd");
	
	
	/**
	 * 
	 * @Title: getMouth
	 * @Description: TODO
	 * @param @param mouth 距离当前月的值 比如：上月为 1 当月为0
	 * @param @return
	 * @return String 201502
	 * @throws
	 */
	public String getMouth(String mouth) {

		Calendar calendar = Calendar.getInstance();

		if (StringUtils.isEmpty(mouth) || Integer.valueOf(mouth) < 1) {
			return getMouth();
		}
		calendar.add(Calendar.MONTH, -Integer.valueOf(mouth));
		Date date = calendar.getTime();
		return sdf_mouth.format(date);

	}

	private String getMouth() {
		return sdf_mouth.format(new Date());
	}
	
	
	public String getDay(String day) {

		Calendar calendar = Calendar.getInstance();

		if (StringUtils.isEmpty(day) || Integer.valueOf(day) < 1) {
			return getDay();
		}
		calendar.add(Calendar.DAY_OF_YEAR, -Integer.valueOf(day));
		Date date = calendar.getTime();
		return sdf_day.format(date);

	}

	private String getDay() {
		return sdf_day.format(new Date());
	}
}
