package tianci.pinao.dts.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tianci.pinao.dts.dao.LogDao;
import tianci.pinao.dts.models.Log;
import tianci.pinao.dts.util.SqlConstants;

public class LogDaoImpl extends JdbcDaoSupport implements LogDao {

	@Override
	public boolean addLog(Log log) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_LOG + "(type, value, source, lastmod_time, isdel) values(?, ?, ?,now(),?)", 
				new Object[]{log.getType(), log.getValue(), log.getSource(), 0});
		return count > 0;
	}

	@Override
	public List<Log> getLogs(Date startDate, Date endDate) {
		return getJdbcTemplate().query("select id, type, value, source, lastmod_time from " + SqlConstants.TABLE_LOG + " where isdel = 0 and lastmod_time between ? and ?", 
				new Object[]{startDate, endDate}, new RowMapper<Log>(){

			@Override
			public Log mapRow(ResultSet rs, int index) throws SQLException {
				Log log = new Log();
				
				log.setId(rs.getInt("id"));
				log.setType(rs.getInt("type"));
				log.setValue(rs.getString("value"));
				log.setSource(rs.getString("source"));
				Timestamp ts = rs.getTimestamp("lastmod_time");
				if(ts != null)
					log.setLastModTime(new Date(ts.getTime()));
				
				return log;
			}
			
		});
	}
}
