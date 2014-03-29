package tianci.pinao.dts.models;

import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AreaReportData {

	private double max;
	
	private double min;
	
	private double avg;
	
	private Map<String, Map<String, Object>> tems;
	
	private Map<String, Map<String, Object>> stocks;
	
	private Map<String, Map<String, Object>> unstocks;

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

	public Map<String, Map<String, Object>> getTems() {
		return tems;
	}

	public void setTems(Map<String, Map<String, Object>> tems) {
		this.tems = tems;
	}

	public Map<String, Map<String, Object>> getStocks() {
		return stocks;
	}

	public void setStocks(Map<String, Map<String, Object>> stocks) {
		this.stocks = stocks;
	}

	public Map<String, Map<String, Object>> getUnstocks() {
		return unstocks;
	}

	public void setUnstocks(Map<String, Map<String, Object>> unstocks) {
		this.unstocks = unstocks;
	}

}
