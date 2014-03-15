package tianci.pinao.dts.models;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ChannelMonitorData {

	private double max;
	
	private double min;
	
	private double avg;
	
	private long time;
	
	private List<TemData> tems;
	
	private List<TemData> stocks;
	
	private List<TemData> unstocks;
	
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

	public List<TemData> getTems() {
		return tems;
	}

	public void setTems(List<TemData> tems) {
		this.tems = tems;
	}

	public List<TemData> getStocks() {
		return stocks;
	}

	public void setStocks(List<TemData> stocks) {
		this.stocks = stocks;
	}

	public List<TemData> getUnstocks() {
		return unstocks;
	}

	public void setUnstocks(List<TemData> unstocks) {
		this.unstocks = unstocks;
	}
}
