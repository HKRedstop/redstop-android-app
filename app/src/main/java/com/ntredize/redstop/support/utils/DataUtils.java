package com.ntredize.redstop.support.utils;

import java.util.Date;
import java.util.List;

public class DataUtils {
	
	/* Common */
	public boolean isEmpty(Object obj) {
		if (obj == null) return true;
		else if (obj instanceof String) return ((String) obj).isEmpty();
		else if (obj instanceof List) return ((List) obj).isEmpty();
		else return false;
	}


	/* Date Diff */
	public long getMsDiff(Date date1, Date date2) {
		return date2.getTime() - date1.getTime();
	}

	public double getSecondDiff(Date date1, Date date2) {
		return 1.0 * getMsDiff(date1, date2) / 1000.0;
	}

	public double getMinDiff(Date date1, Date date2) {
		return getSecondDiff(date1, date2) / 60.0;
	}

	public double getHourDiff(Date date1, Date date2) {
		return getMinDiff(date1, date2) / 60.0;
	}

	public double getDayDiff(Date date1, Date date2) {
		return getHourDiff(date1, date2) / 24.0;
	}
	
}
