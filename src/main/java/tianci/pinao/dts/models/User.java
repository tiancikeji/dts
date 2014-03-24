package tianci.pinao.dts.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
	
	private static final Map<Integer, String> RIGHTS = new HashMap<Integer, String>();
	
	static {
		RIGHTS.put(1, "1111111111111");
		RIGHTS.put(2, "1111111111110");
		RIGHTS.put(3, "1101111110000");
		RIGHTS.put(4, "0010110110000");
	}
	
	private long id;
	
	private String name;
	
	private String passwordLogin;
	
	private String passwordLogout;
	
	private String passwordReset;
	
	// 1 super admin
	// 2 system admin
	// 3 super user
	// 4 normal user
	private int role;
	
	// 1   其他管理员权限
	// 2 操作工用户进行增加/删除/修改的功能
	// 3  班长用户进行增加/删除/修改的功能
	// 4 分区报警声音设定
	// 5 实时监测区域
	// 6 实时报警查询
	// 7 历史报警查询
	// 8 历史运行日志
	// 9 历史监测区域趋势数据查询功能
	// 10 历史监测区域趋势数据导出功能
	// 11 报警消音
	// 12 报警确认
	// 13 报警恢复
	private long right;
	
	private List<Integer> areaIds;
	
	private Date lastModTime;
	
	private long lastModUserid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public long getRight() {
		return right;
	}

	public void setRight(long right) {
		this.right = right;
	}

	public List<Integer> getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(List<Integer> areaIds) {
		this.areaIds = areaIds;
	}

	public Date getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(Date lastModTime) {
		this.lastModTime = lastModTime;
	}

	public long getLastModUserid() {
		return lastModUserid;
	}

	public void setLastModUserid(long lastModUserid) {
		this.lastModUserid = lastModUserid;
	}

	public static String getRight(int role) {
		return RIGHTS.get(role);
	}

	public String getPasswordLogin() {
		return passwordLogin;
	}

	public void setPasswordLogin(String passwordLogin) {
		this.passwordLogin = passwordLogin;
	}

	public String getPasswordLogout() {
		return passwordLogout;
	}

	public void setPasswordLogout(String passwordLogout) {
		this.passwordLogout = passwordLogout;
	}

	public String getPasswordReset() {
		return passwordReset;
	}

	public void setPasswordReset(String passwordReset) {
		this.passwordReset = passwordReset;
	}

}
