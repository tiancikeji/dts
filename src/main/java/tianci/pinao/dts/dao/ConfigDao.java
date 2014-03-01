package tianci.pinao.dts.dao;

import java.util.List;

import tianci.pinao.dts.models.Config;
import tianci.pinao.dts.models.License;

public interface ConfigDao {

	// license
	public List<License> getAllLicenses();
	
	public boolean addLifeTimeByLocalMac(String mac, long time);

	public boolean addLifeTime(String mac, long time);

	public Config getConfigByType(int type);

	public boolean addConfig(Config config);

	public boolean updateConfig(Config config);
}
