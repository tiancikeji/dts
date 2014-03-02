package tianci.pinao.dts.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tianci.pinao.dts.dao.ConfigDao;
import tianci.pinao.dts.models.Config;
import tianci.pinao.dts.models.License;
import tianci.pinao.dts.util.SqlConstants;

public class ConfigDaoImpl extends JdbcDaoSupport implements ConfigDao {

	@Override
	public Config getConfigByType(int type) {
		List<Config> configs = getJdbcTemplate().query("select id, type, value, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_CONFIG + " where isdel = ? and type = ? order by lastmod_time desc limit 1",  
				new Object[]{0, type}, new ConfigRowMapper());
		
		if(configs != null && configs.size() > 0)
			return configs.get(0);
		else
			return null;
	}

	@Override
	public boolean addConfig(Config config) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_CONFIG + "(type, value, intr, lastmod_time, lastmod_userid, isdel) values(?, ?, null, now(), ?, ?)", 
				new Object[]{config.getType(), config.getValue(), config.getLastModUserid(), 0});
		return count > 0;
	}

	@Override
	public boolean updateConfig(Config config) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_CONFIG + " set value = ?, lastmod_time = now(), lastmod_userid = ? where type = ? and isdel = ?", 
				new Object[]{config.getValue(), config.getLastModUserid(), config.getType(), 0});
		return count > 0;
	}

	@Override
	public List<License> getAllLicenses() {
		return getJdbcTemplate().query("select id, mac, use_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_LICENSE + " where isdel = 0",  new LicenseRowMapper());
	}

	@Override
	public boolean addLifeTimeByLocalMac(String mac, long time) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_LICENSE + " set use_time = use_time + ?, lastmod_time = now() where mac = ? and isdel = 0", new Object[]{time, mac});
		return count > 0;
	}

	@Override
	public boolean addLifeTime(String mac, long time) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_LICENSE + "(mac, use_time, lastmod_time, lastmod_userid, isdel) values(?, ?, now(), ?, ?)", new Object[]{mac, time, -1, 0});
		return count > 0;
	}

}

class ConfigRowMapper implements RowMapper<Config>{

	@Override
	public Config mapRow(ResultSet rs, int index) throws SQLException {
		Config config = new Config();
		
		config.setId(rs.getInt("id"));
		config.setType(rs.getInt("type"));
		config.setValue(rs.getLong("value"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			config.setLastModTime(new Date(ts.getTime()));
		config.setLastModUserid(rs.getInt("lastmod_userid"));
		
		return config;
	}
	
}

class LicenseRowMapper implements RowMapper<License>{

	@Override
	public License mapRow(ResultSet rs, int index) throws SQLException {
		License license = new License();

		license.setId(rs.getInt("id"));
		license.setMac(rs.getString("mac"));
		license.setUseTime(rs.getLong("use_time"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			license.setLastModTime(new Date(ts.getTime()));
		license.setLastModUserid(rs.getInt("lastmod_userid"));
		
		return license;
	}
	
}