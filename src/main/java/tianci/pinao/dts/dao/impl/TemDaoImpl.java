package tianci.pinao.dts.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tianci.pinao.dts.dao.TemDao;
import tianci.pinao.dts.models.Temperature;
import tianci.pinao.dts.util.SqlConstants;

public class TemDaoImpl extends JdbcDaoSupport implements TemDao {

	@Override
	public List<Temperature> getLatestTemsByChannel(List<Integer> channels, Date time) {
		String sql = "select channel, tem, date from " + SqlConstants.TABLE_TEMPERATURE + " where channel in (" + StringUtils.join(channels.toArray()) + ")";
		Object[] params = new Object[1];
		if(time != null){
			sql += " and date > ?";
			params[0] = time;
		}
		sql += " order by date desc limit 1";
		
		return getJdbcTemplate().query(sql, params, new SimpleTemperatureRowMapper());
	}

	@Override
	public List<Temperature> getTemsByChannel(ArrayList<Integer> channels, Date start, Date end) {
		return getJdbcTemplate().query("select channel, tem, stock, unstock, date from " + SqlConstants.TABLE_TEMPERATURE_LOG + " where channel in (" + StringUtils.join(channels.toArray()) + ") and date between ? and ?",
				new Object[]{start, end}, new TemperatureRowMapper());
	}

	@Override
	public List<Temperature> getLatestTemNStocksByChannel(List<Integer> channels, Date time) {
		String sql = "select channel, tem, stock, unstock, date from " + SqlConstants.TABLE_TEMPERATURE + " where channel in (" + StringUtils.join(channels.toArray()) + ")";
		Object[] params = new Object[1];
		if(time != null){
			sql += " and date > ?";
			params[0] = time;
		}
		sql += " order by date desc limit 1";
		
		return getJdbcTemplate().query(sql, params, new TemperatureRowMapper());
	}

	@Override
	public List<Temperature> getTemNStocksByChannel(List<Integer> channels, Date startDate, Date endDate) {
		return getJdbcTemplate().query("select channel, tem, stock, unstock, date from " + SqlConstants.TABLE_TEMPERATURE_LOG + " where channel in (" + StringUtils.join(channels.toArray()) + ") and date between ? and ?",
				new Object[]{startDate, endDate}, new TemperatureRowMapper());
	}
}

class TemperatureRowMapper implements RowMapper<Temperature>{

	@Override
	public Temperature mapRow(ResultSet rs, int index) throws SQLException {
		Temperature tem = new Temperature();
		
		tem.setChannel(rs.getInt("channel"));
		tem.setTem(rs.getString("tem"));
		tem.setStock(rs.getString("stock"));
		tem.setUnstock(rs.getString("unstock"));
		Timestamp ts = rs.getTimestamp("date");
		if(ts != null)
			tem.setDate(new Date(ts.getTime()));
		
		return tem;
	}
}

class SimpleTemperatureRowMapper implements RowMapper<Temperature>{

	@Override
	public Temperature mapRow(ResultSet rs, int index) throws SQLException {
		Temperature tem = new Temperature();
		
		tem.setChannel(rs.getInt("channel"));
		tem.setTem(rs.getString("tem"));
		Timestamp ts = rs.getTimestamp("date");
		if(ts != null)
			tem.setDate(new Date(ts.getTime()));
		
		return tem;
	}
}