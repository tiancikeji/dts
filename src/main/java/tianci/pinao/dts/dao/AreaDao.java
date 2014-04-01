package tianci.pinao.dts.dao;

import java.util.List;
import java.util.Map;

import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaHardwareConfig;
import tianci.pinao.dts.models.AreaTempConfig;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.LevelImage;
import tianci.pinao.dts.models.Machine;

public interface AreaDao {
	
	// area
	public boolean addArea(Area area);
	
	public Area getAreaById(int id);
	
	public Map<Integer, Area> getAreaByIds(List<Integer> ids);
	
	public List<Area> getAllAreas(int start, int step);
	
	public boolean updateArea(Area area);
	
	public boolean deleteArea(int areaid, int userid);

	public void deleteAllAreas(int userid);

	public boolean addAreas(List<Area> areas);

	public void updateAreaParents(List<Area> values);
	
	// area harware config
	public boolean addHardwareConfig(AreaHardwareConfig config);
	
	public AreaHardwareConfig getHardwareConfigByAreaid(int areaid);
	
	public List<AreaHardwareConfig> getAllHardwareConfigs(int start, int step);
	
	public boolean updateHardwareConfig(AreaHardwareConfig config);
	
	public boolean deleteHardwareConfig(int configid, int userid);

	public void deleteAreaHardwareConfigs(int areaid, int userid);

	public void deleteAllHardwareConfigs(int userid);

	public void addHardwareConfigs(List<AreaHardwareConfig> hardConfigs);
	
	// area channel
	public boolean addAreaChannels(List<AreaChannel> channels);
	
	public List<AreaChannel> getAreaChannelsByAreaid(int areaid);

	public List<AreaChannel> getAllAreaChannels(int start, int step);
	
	public boolean deleteAreaChannelsByAreaid(int areaid, int userid);
	
	public boolean updateAreaChannel(AreaChannel channel);
	
	public boolean deleteAreaChannel(int channelid, int userid);

	public void deleteAllAreaChannels(int userid);
	
	// area temp config
	public boolean addTempConfig(AreaTempConfig config);
	
	public AreaTempConfig getTempConfigByAreaid(int areaid);
	
	public boolean updateTempConfig(AreaTempConfig config);
	
	public boolean deleteTempConfig(int configid, int userid);

	public List<AreaTempConfig> getAllTempConfigs(int start, int step);

	public void deleteAllTempConfigs(int userid);

	public void deleteAreaTemConfigs(int areaid, int userid);

	public void addTempConfigs(List<AreaTempConfig> tempConfigs);

	// channels
	public List<Channel> getAllChannels(int start, int step);

	public boolean addChannel(Channel channel);

	public boolean addChannels(List<Channel> channels);

	public boolean updateChannel(Channel channel);

	public boolean deleteChannel(Channel channel);

	public void deleteAllChannels(int userid);

	public boolean deleteChannels(int machineid, int userid);

	// machines
	public List<Machine> getAllMachines(int start, int step);

	public boolean addMachine(Machine machine);

	public boolean addMachines(List<Machine> machines);

	public boolean updateMachine(Machine machine);

	public boolean deleteMachine(Machine machine);
	
	public void deleteAllMachines(int userid);
	
	// level - images
	public List<LevelImage> getAllLevels(int start, int step);
	
	public boolean addLevelImage(LevelImage level);
	
	public boolean modifyLevelImage(LevelImage level);
	
	public boolean deleteLevelImage(LevelImage level);

	// monitor
	public List<AreaChannel> getAreaChannelsByAreaids(List<Integer> areaIds);
}
