package tianci.pinao.dts.models;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AreaMonitorData {

	private double max;
	
	private double min;
	
	private double avg;
	
	private long time;
	
	private List<Integer> alarmIdx;
	
	private List<Long> alarmIds;
	
	private String alarmName;
	
	private int alarmType = -1;

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<Integer> getAlarmIdx() {
		return alarmIdx;
	}

	public void setAlarmIdx(List<Integer> alarmIdx) {
		this.alarmIdx = alarmIdx;
	}

	public List<Long> getAlarmIds() {
		return alarmIds;
	}

	public void setAlarmIds(List<Long> alarmIds) {
		this.alarmIds = alarmIds;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public int getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
