package tianci.pinao.dts.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import tianci.pinao.dts.dao.AlarmDao;
import tianci.pinao.dts.models.Alarm;
import tianci.pinao.dts.models.AlarmHistory;
import tianci.pinao.dts.models.Check;
import tianci.pinao.dts.util.SqlConstants;

public class AlarmDaoImpl extends JdbcDaoSupport implements AlarmDao{

	@Override
	public void addAlarm(final Alarm alarm) {
		KeyHolder holder = new GeneratedKeyHolder();
		int count = getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into " + SqlConstants.TABLE_ALARM + "(type, machine_id, machine_name, channel_id, channel_name, length, area_id, area_name, alarm_name, light, relay, relay1, voice, temperature, temperature_pre, temperature_max, status, add_time, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now(), ?, 0)", Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, alarm.getType());
				ps.setObject(2, alarm.getMachineId());
				ps.setObject(3, alarm.getMachineName());
				ps.setObject(4, alarm.getChannelId());
				ps.setObject(5, alarm.getChannelName());
				ps.setObject(6, alarm.getLength());
				ps.setObject(7, alarm.getAreaId());
				ps.setObject(8, alarm.getAreaName());
				ps.setObject(9, alarm.getAlarmName());
				ps.setObject(10, alarm.getLight());
				ps.setObject(11, alarm.getRelay());
				ps.setObject(12, alarm.getRelay1());
				ps.setObject(13, alarm.getVoice());
				ps.setObject(14, alarm.getTemperatureCurr());
				ps.setObject(15, alarm.getTemperaturePre());
				ps.setObject(16, alarm.getTemperatureMax());
				ps.setObject(17, alarm.getStatus());
				ps.setObject(18, alarm.getLastModUserid());
				return ps;
			}
		}, holder);
		
		if(count > 0 && holder.getKey() != null)
			alarm.setId(holder.getKey().longValue());
	}

	@Override
	public List<Alarm> getAlarms(List<Integer> ids, Date date) {
		return getJdbcTemplate().query("select id, type, machine_id, machine_name, channel_id, channel_name, length, area_id, area_name, alarm_name, light, relay, relay1, voice, temperature, temperature_pre, status, add_time, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_ALARM + " where isdel = ? and area_id in (" + StringUtils.join(ids, ",") + ") and add_time > ?",  
				new Object[]{0, date}, new AlarmRowMapper());
	}

	@Override
	public List<Alarm> getAlarmsByAreaIds(List<Integer> ids, Integer[] status, int start, int step) {
		return getJdbcTemplate().query("select id, type, machine_id, machine_name, channel_id, channel_name, length, area_id, area_name, alarm_name, light, relay, relay1, voice, temperature, temperature_pre, status, add_time, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_ALARM 
				+ " where isdel = ? and area_id in (" + StringUtils.join(ids, ",") + ") and status in (" + StringUtils.join(status, ",") + ") limit ?, ?",  
				new Object[]{0, start, step}, new AlarmRowMapper());
	}

	@Override
	public int getAlarmCountByAreaIds(List<Integer> ids, Integer[] status){
		return getJdbcTemplate().queryForInt("select count(1) from " + SqlConstants.TABLE_ALARM 
				+ " where isdel = ? and area_id in (" + StringUtils.join(ids, ",") + ") and status in (" + StringUtils.join(status, ",") + ")",  
				new Object[]{0}, new AlarmRowMapper());
	}

	@Override
	public List<Alarm> getAlarmsByChannelIds(List<Integer> ids, Integer[] status, int start, int step) {
		return getJdbcTemplate().query("select id, type, machine_id, machine_name, channel_id, channel_name, length, area_id, area_name, alarm_name, light, relay, relay1, voice, temperature, temperature_pre, status, add_time, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_ALARM 
				+ " where isdel = ? and channel_id in (" + StringUtils.join(ids, ",") + ") and status in (" + StringUtils.join(status, ",") + ") limit ?, ?",  
				new Object[]{0, start, step}, new AlarmRowMapper());
	}

	@Override
	public int getAlarmCountByChannelIds(List<Integer> ids, Integer[] status){
		return getJdbcTemplate().queryForInt("select count(1) from " + SqlConstants.TABLE_ALARM 
				+ " where isdel = ? and channel_id in (" + StringUtils.join(ids, ",") + ") and status in (" + StringUtils.join(status, ",") + ")",  
				new Object[]{0});
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

	@Override
	public void addChecks(final List<Check> checks, final long userid) {
		getJdbcTemplate().batchUpdate("insert into " + SqlConstants.TABLE_CHECK + "(machine_id, channel_id, area_id, light, relay, relay1, voice, status, add_time, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, ?, ?, ?, now(), now(), ?, 0)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(checks.size() > index){
					Check check = checks.get(index);
					if(check != null){
						int column = 1;
						ps.setObject(column ++, check.getMachineId());
						ps.setObject(column ++, check.getChannelId());
						ps.setObject(column ++, check.getAreaId());
						ps.setObject(column ++, check.getLight());
						ps.setObject(column ++, check.getRelay());
						ps.setObject(column ++, check.getRelay1());
						ps.setObject(column ++, check.getVoice());
						ps.setObject(column ++, Check.STATUS_NEW);
						ps.setObject(column ++ , userid);
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return checks.size();
			}
		});
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
