package tianci.pinao.dts.models;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AlarmHistory {
	
	public static int OPERATION_NOTIFY = 2;
	
	public static int OPERATION_MUTE = 3;
	
	public static int OPERATION_RESET = 4;
	
	private long alarmId;
	
	private int operation;
	
	private Date addTime;
	
	private long addUserid;

	public long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(long alarmId) {
		this.alarmId = alarmId;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public long getAddUserid() {
		return addUserid;
	}

	public void setAddUserid(long addUserid) {
		this.addUserid = addUserid;
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
