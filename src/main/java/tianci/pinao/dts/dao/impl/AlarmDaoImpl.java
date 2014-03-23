package tianci.pinao.dts.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tianci.pinao.dts.dao.AlarmDao;
import tianci.pinao.dts.models.Alarm;
import tianci.pinao.dts.models.AlarmHistory;
import tianci.pinao.dts.util.SqlConstants;

public class AlarmDaoImpl extends JdbcDaoSupport implements AlarmDao{

	@Override
	public List<Alarm> getAlarms(List<Integer> ids, Date date) {
		return getJdbcTemplate().query("select id, type, machine_id, machine_name, channel_id, channel_name, length, area_id, area_name, alarm_name, light, relay, relay1, voice, temperature, temperature_pre, status, add_time, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_ALARM + " where isdel = ? and area_id in (" + StringUtils.join(ids, ",") + ") and add_time > ?",  
				new Object[]{0, date}, new AlarmRowMapper());
	}

	@Override
	public List<Alarm> getAlarmsByAreaIds(List<Integer> ids, Object[] status) {
		return getJdbcTemplate().query("select id, type, machine_id, machine_name, channel_id, channel_name, length, area_id, area_name, alarm_name, light, relay, relay1, voice, temperature, temperature_pre, status, add_time, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_ALARM 
				+ " where isdel = ? and area_id in (" + StringUtils.join(ids, ",") + ") and status in (" + StringUtils.join(status, ",") + ")",  
				new Object[]{0}, new AlarmRowMapper());
	}

	@Override
	public List<Alarm> getAlarmsByChannelIds(List<Integer> ids, Object[] status) {
		return getJdbcTemplate().query("select id, type, machine_id, machine_name, channel_id, channel_name, length, area_id, area_name, alarm_name, light, relay, relay1, voice, temperature, temperature_pre, status, add_time, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_ALARM 
				+ " where isdel = ? and channel_id in (" + StringUtils.join(ids, ",") + ") and status in (" + StringUtils.join(status, ",") + ")",  
				new Object[]{0}, new AlarmRowMapper());
	}

	@Override
	public List<AlarmHistory> getAlarmHistorysByAlarmIds(Set<Long> alarmIds) {
		return getJdbcTemplate().query("select alarm_id, operation, add_time, add_userid from " + SqlConstants.TABLE_ALARM_HISTORY + " where alarm_id in (" + StringUtils.join(alarmIds, ",") + ")",
				new RowMapper<AlarmHistory>(){

					@Override
					public AlarmHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
						AlarmHistory history = new AlarmHistory();
						
						history.setAlarmId(rs.getLong("alarm_id"));
						history.setOperation(rs.getInt("operation"));
						history.setAddUserid(rs.getLong("add_userid"));
						Timestamp ts = rs.getTimestamp("add_time");
						if(ts != null)
							history.setAddTime(new Date(ts.getTime()));
						
						return history;
					}
			
		});
	}

	@Override
	public boolean updateAlarm(long id, int status, long userid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_ALARM + " set status = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{status, userid, id});
		return count > 0;
	}

	@Override
	public boolean addAlarmHistory(long id, int status, long userid) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_ALARM_HISTORY + "(alarm_id, operation, add_time, add_userid) values(?, ?, now(), ?)", 
				new Object[]{id, status, userid});
		return count > 0;
	}

}

class AlarmRowMapper implements RowMapper<Alarm>{

	@Override
	public Alarm mapRow(ResultSet rs, int rowNum) throws SQLException {
		Alarm alarm = new Alarm();
		
		alarm.setId(rs.getLong("id"));
		alarm.setType(rs.getInt("type"));
		alarm.setMachineId(rs.getInt("machine_id"));
		alarm.setMachineName(rs.getString("machine_name"));
		alarm.setChannelId(rs.getInt("channel_id"));
		alarm.setChannelName(rs.getString("channel_name"));
		alarm.setAreaId(rs.getInt("area_id"));
		alarm.setAreaName(rs.getString("area_name"));
		alarm.setAlarmName(rs.getString("alarm_name"));
		alarm.setLength(rs.getInt("length"));
		alarm.setLight(rs.getString("light"));
		alarm.setRelay(rs.getString("relay"));
		alarm.setRelay1(rs.getString("relay1"));
		alarm.setVoice(rs.getString("voice"));
		alarm.setTemperatureCurr(rs.getDouble("temperature"));
		alarm.setTemperaturePre(rs.getDouble("temperature_pre"));
		alarm.setStatus(rs.getInt("status"));
		alarm.setLastModUserid(rs.getInt("lastmod_userid"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			alarm.setLastModTime(new Date(ts.getTime()));
		ts = rs.getTimestamp("add_time");
		if(ts != null)
			alarm.setAddTime(new Date(ts.getTime()));
		
		return alarm;
	}

}
