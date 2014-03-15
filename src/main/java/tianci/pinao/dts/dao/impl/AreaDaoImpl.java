package tianci.pinao.dts.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tianci.pinao.dts.dao.AreaDao;
import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaHardwareConfig;
import tianci.pinao.dts.models.AreaTempConfig;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.LevelImage;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.util.SqlConstants;

public class AreaDaoImpl extends JdbcDaoSupport implements AreaDao {
	
	// areas
	@Override
	public List<Area> getAllAreas() {
		return getJdbcTemplate().query("select id, name, level, `index`, parent, image, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA + " where isdel = ?", 
				new Object[]{0}, new AreaRowMapper());
	}

	@Override
	public boolean addArea(Area area) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_AREA + "(name, level, `index`, parent, image, lastmod_time, lastmod_userid, isdel) values(?,?,?,?,?,now(),?,?)", 
				new Object[]{area.getName(), area.getLevel(), area.getIndex(), area.getParent(), area.getImage(), area.getLastModUserid(), 0});
		return count > 0;
	}

	@Override
	public boolean addAreas(final List<Area> areas) {
		getJdbcTemplate().batchUpdate("insert into " + SqlConstants.TABLE_AREA + "(name, level, `index`, parent, image, lastmod_time, lastmod_userid, isdel) values(?,?,?,?,?,now(),?,?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(areas.size() > index){
					Area area = areas.get(index);
					if(area != null){
						ps.setObject(1, area.getName());
						ps.setObject(2, area.getLevel());
						ps.setObject(3, area.getIndex());
						ps.setObject(4, area.getParent());
						ps.setObject(5, area.getImage());
						ps.setObject(6, area.getLastModUserid());
						ps.setObject(7, 0);
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return areas.size();
			}
		});
		return true;
	}

	@Override
	public void updateAreaParents(final List<Area> areas) {
		getJdbcTemplate().batchUpdate("update " + SqlConstants.TABLE_AREA + "set parent = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(areas.size() > index){
					Area area = areas.get(index);
					if(area != null){
						ps.setObject(1, area.getParent());
						ps.setObject(2, area.getLastModUserid());
						ps.setObject(3, area.getId());
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return areas.size();
			}
		});
	}

	@Override
	public Area getAreaById(int id){
		List<Area> areas = getJdbcTemplate().query("select id, name, level, `index`, parent, image, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA + " where isdel = ? and id = ?", 
				new Object[]{0, id}, new AreaRowMapper());
		
		if(areas != null && areas.size() > 0)
			return areas.get(0);
		else
			return null;
	}

	@Override
	public Map<Integer, Area> getAreaByIds(List<Integer> ids){
		List<Area> areas = getJdbcTemplate().query("select id, name, level, `index`, parent, image, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA + " where isdel = ? and id in (" + StringUtils.join(ids, ",") + ")", 
				new Object[]{0}, new AreaRowMapper());
		
		if(areas != null && areas.size() > 0){
			Map<Integer, Area> result = new HashMap<Integer, Area>();
			for(Area area : areas)
				result.put(area.getId(), area);
			return result;
		}else
			return null;
	}

	@Override
	public boolean updateArea(Area area) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA + " set name = ?, level = ?, `index` = ?, parent = ?, image = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{area.getName(), area.getLevel(), area.getIndex(), area.getParent(), area.getImage(), area.getLastModUserid(), area.getId()});
		return count > 0;
	}

	@Override
	public boolean deleteArea(int areaid, int userid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{1, userid, areaid});
		return count > 0;
	}

	@Override
	public void deleteAllAreas(int userid) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA + " set isdel = ?, lastmod_time = now(), lastmod_userid = ?", new Object[]{1, userid});
	}

	// area hardware configs
	@Override
	public boolean addHardwareConfig(AreaHardwareConfig config) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_AREA_HARDWARE_CONFIG + "(area_id, light, relay, voice, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, now(), ?, ?)", 
				new Object[]{config.getAreaid(), config.getLight(), config.getRelay(), config.getVoice(), config.getLastModUserid(), 0});
		return count > 0;
	}

	@Override
	public void addHardwareConfigs(final List<AreaHardwareConfig> hardConfigs) {
		getJdbcTemplate().batchUpdate("insert into " + SqlConstants.TABLE_AREA_HARDWARE_CONFIG + "(area_id, light, relay, voice, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, now(), ?, ?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(hardConfigs.size() > index){
					AreaHardwareConfig config = hardConfigs.get(index);
					if(config != null){
						ps.setObject(1, config.getAreaid());
						ps.setObject(2, config.getLight());
						ps.setObject(3, config.getRelay());
						ps.setObject(4, config.getVoice());
						ps.setObject(5, config.getLastModUserid());
						ps.setObject(6, 0);
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return hardConfigs.size();
			}
		});
	}

	@Override
	public AreaHardwareConfig getHardwareConfigByAreaid(int areaid) {
		List<AreaHardwareConfig> configs = getJdbcTemplate().query("select id, area_id, light, relay, voice, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA_HARDWARE_CONFIG + " where isdel = ? and area_id = ?", 
				new Object[]{0, areaid}, new AreaHardwareConfigRowMapper());
		if(configs != null && configs.size() > 0)
			return configs.get(0);
		else
			return null;
	}

	@Override
	public List<AreaHardwareConfig> getAllHardwareConfigs() {
		return getJdbcTemplate().query("select id, area_id, light, relay, voice, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA_HARDWARE_CONFIG + " where isdel = ?", new Object[]{0}, new AreaHardwareConfigRowMapper());
	}

	@Override
	public boolean updateHardwareConfig(AreaHardwareConfig config) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_HARDWARE_CONFIG + " set area_id = ?, light = ?, relay = ?, voice = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{config.getAreaid(), config.getLight(), config.getRelay(), config.getVoice(), config.getLastModUserid(), config.getId()});
		return count > 0;
	}

	@Override
	public boolean deleteHardwareConfig(int configid, int userid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_HARDWARE_CONFIG + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{1, userid, configid});
		return count > 0;
	}

	@Override
	public void deleteAllHardwareConfigs(int userid) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_HARDWARE_CONFIG + " set isdel = ?, lastmod_time = now(), lastmod_userid = ?", new Object[]{1, userid});
	}

	// area channels
	@Override
	public boolean addAreaChannels(final List<AreaChannel> channels) {
		int[] counts = getJdbcTemplate().batchUpdate("insert into " + SqlConstants.TABLE_AREA_CHANNEL + "(name, area_id, channel_id, start, end, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, now(), ?, ?)", 
				new BatchPreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps, int index) throws SQLException {
						if(channels.size() > index){
							AreaChannel channel = channels.get(index);
							if(channels != null){
								ps.setString(1, channel.getName());
								ps.setInt(2, channel.getAreaid());
								ps.setInt(3, channel.getChannelid());
								ps.setInt(4, channel.getStart());
								ps.setInt(5, channel.getEnd());
								ps.setInt(6, channel.getLastModUserid());
								ps.setInt(7, 0);
							}
						}
					}
					
					@Override
					public int getBatchSize() {
						return channels.size();
					}
				});
		
		if(counts != null && counts.length > 0)
			for(int count : counts)
				if(count <= 0)
					return false;
		return true;
	}

	@Override
	public List<AreaChannel> getAreaChannelsByAreaid(int areaid) {
		return getJdbcTemplate().query("select id, name, area_id, channel_id, start, end, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA_CHANNEL + " where isdel = ? and area_id = ?", 
				new Object[]{0, areaid}, new AreaChannelRowMapper());
	}

	@Override
	public List<AreaChannel> getAllAreaChannels() {
		return getJdbcTemplate().query("select id, name, area_id, channel_id, start, end, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA_CHANNEL + " where isdel = ?", 
				new Object[]{0}, new AreaChannelRowMapper());
	}

	@Override
	public boolean deleteAreaChannelsByAreaid(int areaid, int userid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_CHANNEL + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where area_id = ?", new Object[]{1, userid, areaid});
		return count > 0;
	}

	@Override
	public boolean updateAreaChannel(AreaChannel channel) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_CHANNEL + " set name = ?, area_id = ?, channel_id = ?, start = ?, end = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{channel.getName(), channel.getAreaid(), channel.getChannelid(), channel.getStart(), channel.getEnd(), channel.getLastModUserid(), channel.getId()});
		return count > 0;
	}

	@Override
	public boolean deleteAreaChannel(int channelid, int userid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_CHANNEL + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{1, userid, channelid});
		return count > 0;
	}

	@Override
	public void deleteAllAreaChannels(int userid) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_CHANNEL + " set isdel = ?, lastmod_time = now(), lastmod_userid = ?", new Object[]{1, userid});		
	}

	// area temp configs
	@Override
	public boolean addTempConfig(AreaTempConfig config) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_AREA_TEMP_CONFIG + "(area_id, temperature_low, temperature_high, exotherm, temperature_diff, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, now(), ?, ?)", 
				new Object[]{config.getAreaid(), config.getTemperatureLow(), config.getTemperatureHigh(), config.getExotherm(), config.getTemperatureDiff(), config.getLastModUserid(), 0});
		return count > 0;
	}

	@Override
	public void addTempConfigs(final List<AreaTempConfig> tempConfigs) {
		getJdbcTemplate().batchUpdate("insert into " + SqlConstants.TABLE_AREA_TEMP_CONFIG + "(area_id, temperature_low, temperature_high, exotherm, temperature_diff, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, now(), ?, ?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(tempConfigs.size() > index){
					AreaTempConfig config = tempConfigs.get(index);
					if(config != null){
						ps.setObject(1, config.getAreaid());
						ps.setObject(2, config.getTemperatureLow());
						ps.setObject(3, config.getTemperatureHigh());
						ps.setObject(4, config.getExotherm());
						ps.setObject(5, config.getTemperatureDiff());
						ps.setObject(6, config.getLastModUserid());
						ps.setObject(7, 0);
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return tempConfigs.size();
			}
		});
	}

	@Override
	public AreaTempConfig getTempConfigByAreaid(int areaid) {
		List<AreaTempConfig> configs = getJdbcTemplate().query("select id, area_id, temperature_low, temperature_high, exotherm, temperature_diff, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA_TEMP_CONFIG + " where isdel = ? and area_id = ?", 
				new Object[]{0, areaid}, new AreaTempConfigRowMapper());
		if(configs != null && configs.size() > 0)
			return configs.get(0);
		else
			return null;
	}

	@Override
	public List<AreaTempConfig> getAllTempConfigs() {
		return getJdbcTemplate().query("select id, area_id, temperature_low, temperature_high, exotherm, temperature_diff, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA_TEMP_CONFIG + " where isdel = ?", 
				new Object[]{0}, new AreaTempConfigRowMapper());
	}
	
	@Override
	public boolean updateTempConfig(AreaTempConfig config) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_TEMP_CONFIG + " set area_id = ?, temperature_low = ?, temperature_high = ?, exotherm = ?, temperature_diff = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{config.getAreaid(), config.getTemperatureLow(), config.getTemperatureHigh(), config.getExotherm(), config.getTemperatureDiff(), config.getLastModUserid(), config.getId()});
		return count > 0;
	}

	@Override
	public boolean deleteTempConfig(int configid, int userid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_TEMP_CONFIG + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{1, userid, configid});
		return count > 0;
	}

	@Override
	public void deleteAllTempConfigs(int userid) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_AREA_TEMP_CONFIG + " set isdel = ?, lastmod_time = now(), lastmod_userid = ?", new Object[]{1, userid});
	}

	// channels
	@Override
	public List<Channel> getAllChannels() {
		return getJdbcTemplate().query("select id, machine_id, name, length, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_CHANNEL + " where isdel = ?", new Object[]{0}, new ChannelRowMapper());
	}

	@Override
	public boolean addChannel(Channel channel) {
		int count = getJdbcTemplate().update("insert into " +  SqlConstants.TABLE_CHANNEL + "(machine_id, name, length, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, now(), ?, ?)", 
				new Object[]{channel.getMachineid(), channel.getName(), channel.getLength(), channel.getLastModUserid(), 0});
		return count > 0;
	}

	@Override
	public boolean addChannels(final List<Channel> channels) {
		getJdbcTemplate().batchUpdate("insert into " +  SqlConstants.TABLE_CHANNEL + "(machine_id, name, length, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, now(), ?, ?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(channels.size() > index){
					Channel channel = channels.get(index);
					if(channel != null){
						ps.setObject(1, channel.getMachineid());
						ps.setObject(2, channel.getName());
						ps.setObject(3, channel.getLength());
						ps.setObject(4, channel.getLastModUserid());
						ps.setObject(5, 0);
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return channels.size();
			}
		});
		return true;
	}

	@Override
	public boolean updateChannel(Channel channel) {
		int count = getJdbcTemplate().update("update " +  SqlConstants.TABLE_CHANNEL + " set machine_id = ?, name = ?, length = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{channel.getMachineid(), channel.getName(), channel.getLength(), channel.getLastModUserid(), channel.getId()});
		return count > 0;
	}

	@Override
	public boolean deleteChannel(Channel channel) {
		int count = getJdbcTemplate().update("update " +  SqlConstants.TABLE_CHANNEL + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{1, channel.getLastModUserid(), channel.getId()});
		return count > 0;
	}

	@Override
	public void deleteAllChannels(int userid) {
		getJdbcTemplate().update("update " +  SqlConstants.TABLE_CHANNEL + " set isdel = ?, lastmod_time = now(), lastmod_userid = ?", new Object[]{1, userid});
	}

	// machines
	@Override
	public List<Machine> getAllMachines() {
		return getJdbcTemplate().query("select id, name, serial_port, baud_rate, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_MACHINE + " where isdel = 0", new MachineRowMapper());
	}

	@Override
	public boolean addMachine(Machine machine) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_MACHINE + "(name, serial_port, baud_rate, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, now(), ?, ?)", 
				new Object[]{machine.getName(), machine.getSerialPort(), machine.getBaudRate(), machine.getLastModUserid(), 0});
		return count > 0;
	}

	@Override
	public boolean addMachines(final List<Machine> machines) {
		getJdbcTemplate().batchUpdate("insert into " + SqlConstants.TABLE_MACHINE + "(name, serial_port, baud_rate, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, now(), ?, ?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(machines.size() > index){
					Machine machine = machines.get(index);
					if(machine != null){
						ps.setObject(1, machine.getName());
						ps.setObject(2, machine.getSerialPort());
						ps.setObject(3, machine.getBaudRate());
						ps.setObject(4, machine.getLastModUserid());
						ps.setObject(5, 0);
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return machines.size();
			}
		});
		return true;
	}

	@Override
	public boolean updateMachine(Machine machine) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_MACHINE + " set name = ?, serial_port = ?, baud_rate = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{machine.getName(), machine.getSerialPort(), machine.getBaudRate(), machine.getLastModUserid(), machine.getId()});
		return count > 0;
	}

	@Override
	public boolean deleteMachine(Machine machine) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_MACHINE + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{1, machine.getLastModUserid(), machine.getId()});
		return count > 0;
	}

	@Override
	public void deleteAllMachines(int userid) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_MACHINE + " set isdel = ?, lastmod_time = now(), lastmod_userid = ?",  new Object[]{1, userid});
	}

	// level images
	@Override
	public List<LevelImage> getAllLevels() {
		return getJdbcTemplate().query("select id, level, name, image, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_LEVEL + " where isdel = 0", new LevelImageRowMapper());
	}

	@Override
	public boolean addLevelImage(LevelImage level) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_LEVEL + "(level, name, image, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, now(), ?, ?)", 
				new Object[]{level.getLevel(), level.getName(), level.getImage(), level.getLastModUserid(), 0});
		return count > 0;
	}

	@Override
	public boolean modifyLevelImage(LevelImage level) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_LEVEL + " set level = ?, name = ?, image = ?, lastmod_time = now(), lastmod_userid = ? where id = ? and isdel = ?", 
				new Object[]{level.getLevel(), level.getName(), level.getImage(), level.getLastModUserid(), level.getId(), 0});
		return count > 0;
	}

	@Override
	public boolean deleteLevelImage(LevelImage level) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_LEVEL + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{1, level.getLastModUserid(), level.getId()});
		return count > 0;
	}

	// monitor
	@Override
	public List<AreaChannel> getAreaChannelsByAreaids(List<Integer> areaIds) {
		return getJdbcTemplate().query("select id, name, area_id, channel_id, start, end, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_AREA_CHANNEL + " where isdel = ? and area_id in (" + StringUtils.join(areaIds.toArray(), ",") + ")", 
				new Object[]{0}, new AreaChannelRowMapper());
	}
}

class LevelImageRowMapper implements RowMapper<LevelImage>{

	@Override
	public LevelImage mapRow(ResultSet rs, int index) throws SQLException {
		LevelImage image = new LevelImage();
		image.setId(rs.getInt("id"));
		image.setLevel(rs.getInt("level"));
		image.setName(rs.getString("name"));
		image.setImage(rs.getString("image"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			image.setLastModTime(new Date(ts.getTime()));
		image.setLastModUserid(rs.getInt("lastmod_userid"));
		return image;
	}
	
}

class MachineRowMapper implements RowMapper<Machine>{

	@Override
	public Machine mapRow(ResultSet rs, int index) throws SQLException {
		Machine machine = new Machine();
		machine.setId(rs.getInt("id"));
		machine.setName(rs.getString("name"));
		machine.setSerialPort(rs.getString("serial_port"));
		machine.setBaudRate(rs.getString("baud_rate"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			machine.setLastModTime(new Date(ts.getTime()));
		machine.setLastModUserid(rs.getInt("lastmod_userid"));
		return machine;
	}
	
}

class AreaTempConfigRowMapper implements RowMapper<AreaTempConfig>{

	@Override
	public AreaTempConfig mapRow(ResultSet rs, int index) throws SQLException {
		AreaTempConfig config = new AreaTempConfig();
		config.setId(rs.getInt("id"));
		config.setAreaid(rs.getInt("area_id"));
		config.setTemperatureLow(rs.getInt("temperature_low"));
		config.setTemperatureHigh(rs.getInt("temperature_high"));
		config.setTemperatureDiff(rs.getInt("temperature_diff"));
		config.setExotherm(rs.getInt("exotherm"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			config.setLastModTime(new Date(ts.getTime()));
		config.setLastModUserid(rs.getInt("lastmod_userid"));
		return config;
	}
	
}

class ChannelRowMapper implements RowMapper<Channel>{

	@Override
	public Channel mapRow(ResultSet rs, int index) throws SQLException {
		Channel channel = new Channel();
		channel.setId(rs.getInt("id"));
		channel.setMachineid(rs.getInt("machine_id"));
		channel.setName(rs.getString("name"));
		channel.setLength(rs.getInt("length"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			channel.setLastModTime(new Date(ts.getTime()));
		channel.setLastModUserid(rs.getInt("lastmod_userid"));
		return channel;
	}
	
}

class AreaChannelRowMapper implements RowMapper<AreaChannel>{

	@Override
	public AreaChannel mapRow(ResultSet rs, int index) throws SQLException {
		AreaChannel channel = new AreaChannel();
		channel.setId(rs.getInt("id"));
		channel.setName(rs.getString("name"));
		channel.setAreaid(rs.getInt("area_id"));
		channel.setChannelid(rs.getInt("channel_id"));
		channel.setStart(rs.getInt("start"));
		channel.setEnd(rs.getInt("end"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			channel.setLastModTime(new Date(ts.getTime()));
		channel.setLastModUserid(rs.getInt("lastmod_userid"));
		return channel;
	}
	
}

class AreaHardwareConfigRowMapper implements RowMapper<AreaHardwareConfig>{

	@Override
	public AreaHardwareConfig mapRow(ResultSet rs, int index)
			throws SQLException {
		AreaHardwareConfig config = new AreaHardwareConfig();
		config.setId(rs.getInt("id"));
		config.setAreaid(rs.getInt("area_id"));
		config.setLight(rs.getString("light"));
		config.setRelay(rs.getString("relay"));
		config.setVoice(rs.getString("voice"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			config.setLastModTime(new Date(ts.getTime()));
		config.setLastModUserid(rs.getInt("lastmod_userid"));
		return config;
	}
	
}

class AreaRowMapper implements RowMapper<Area>{

	@Override
	public Area mapRow(ResultSet rs, int index) throws SQLException {
		Area area = new Area();
		area.setId(rs.getInt("id"));
		area.setName(rs.getString("name"));
		area.setLevel(rs.getInt("level"));
		area.setIndex(rs.getInt("index"));
		area.setParent(rs.getInt("parent"));
		area.setImage(rs.getString("image"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			area.setLastModTime(new Date(ts.getTime()));
		area.setLastModUserid(rs.getInt("lastmod_userid"));
		return area;
	}
	
}