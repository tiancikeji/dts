package tianci.pinao.dts.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tianci.pinao.dts.dao.UserDao;
import tianci.pinao.dts.models.User;
import tianci.pinao.dts.util.SqlConstants;

public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	@Override
	public User getUser(String name, String password) {
		List<User> users = getJdbcTemplate().query("select id, name, password_login, password_reset, password_logout, role, area_ids from " + SqlConstants.TABLE_USER + " where isdel = ? and name = ? and password_login = ?", 
				new Object[]{0, name, password}, new UserRowMapper());
		
		if(users != null && users.size() > 0)
			return users.get(0);
		else
			return null;
	}

	@Override
	public User getResetUser(String name, String password) {
		List<User> users = getJdbcTemplate().query("select id, name, password_login, password_reset, password_logout, role, area_ids from " + SqlConstants.TABLE_USER + " where isdel = ? and name = ? and password_reset = ?", 
				new Object[]{0, name, password}, new UserRowMapper());
		
		if(users != null && users.size() > 0)
			return users.get(0);
		else
			return null;
	}

	@Override
	public void addUser(User user) {
		getJdbcTemplate().update("insert into " + SqlConstants.TABLE_USER + "(name, password_login, password_reset, password_logout, role, area_ids, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, ?, now(), ?, ?)", 
				new Object[]{user.getName(), user.getPasswordLogin(), user.getPasswordReset(), user.getPasswordLogout(), user.getRole(), StringUtils.join(user.getAreaIds(), ","), user.getLastModUserid(), 0});
	}

	@Override
	public void modifyUser(User user) {
		String sql = "update " + SqlConstants.TABLE_USER + " set name = ?, role = ?, area_ids = ?, lastmod_time = now(), lastmod_userid = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(user.getName());
		params.add(user.getRole());
		params.add(StringUtils.join(user.getAreaIds(), ","));
		params.add(user.getLastModUserid());
		if(StringUtils.isNotBlank(user.getPasswordLogin())){
			sql += ", password_login = ?";
			params.add(user.getPasswordLogin());
		}
		if(StringUtils.isNotBlank(user.getPasswordLogout())){
			sql += ", password_logout = ?";
			params.add(user.getPasswordLogout());
		}
		if(StringUtils.isNotBlank(user.getPasswordReset())){
			sql += ", password_reset = ?";
			params.add(user.getPasswordReset());
		}
		sql+= " where id = ? and isdel = ?";
		params.add(user.getId());
		params.add(0);
		
		getJdbcTemplate().update(sql, params.toArray());
	}

	@Override
	public void delUser(long id, long userid) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_USER + " set lastmod_time = now(), lastmod_userid = ?, isdel = ? where id = ?", new Object[]{userid, 1, id});	
	}

	@Override
	public List<User> getUser(int role) {
		return getJdbcTemplate().query("select id, name, password_login, password_reset, password_logout, role, area_ids from " + SqlConstants.TABLE_USER + " where isdel = ? and role > ?", 
				new Object[]{0, role}, new UserRowMapper());
	}

	@Override
	public User getUserById(long id) {
		List<User> users = getJdbcTemplate().query("select id, name, password_login, password_reset, password_logout, role, area_ids from " + SqlConstants.TABLE_USER + " where isdel = ? and id = ?", 
				new Object[]{0, id}, new UserRowMapper());
		
		if(users != null && users.size() > 0)
			return users.get(0);
		else
			return null;
	}

}

class UserRowMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int index) throws SQLException {
		User user = new User();
		
		user.setId(rs.getLong("id"));
		user.setName(rs.getString("name"));
		user.setPasswordLogin(rs.getString("password_login"));
		user.setPasswordReset(rs.getString("password_reset"));
		user.setPasswordLogout(rs.getString("password_logout"));
		user.setRole(rs.getInt("role"));
		String[] tmp = StringUtils.split(rs.getString("area_ids"), ",");
		List<Integer> areaIds = new ArrayList<Integer>();
		if(tmp != null && tmp.length > 0)
			for(String tt : tmp){
				int id = NumberUtils.toInt(tt, -1);
				if(id > 0)
					areaIds.add(id);
			}
		
		user.setAreaIds(areaIds);
		
		return user;
	}
	
}