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
	
	public List<Area> getAllAreas();
	
	public boolean updateArea(Area area);
	
	public boolean deleteArea(int areaid, int userid);
	
	// area harware config
	public boolean addHardwareConfig(AreaHardwareConfig config);
	
	public AreaHardwareConfig getHardwareConfigByAreaid(int areaid);
	
	public List<AreaHardwareConfig> getAllHardwareConfigs();
	
	public boolean updateHardwareConfig(AreaHardwareConfig config);
	
	public boolean deleteHardwareConfig(int configid, int userid);
	
	// area channel
	public boolean addAreaChannels(List<AreaChannel> channels);
	
	public List<AreaChannel> getAreaChannelsByAreaid(int areaid);

	public List<AreaChannel> getAllAreaChannels();
	
	public boolean deleteAreaChannelsByAreaid(int areaid, int userid);
	
	public boolean updateAreaChannel(AreaChannel channel);
	
	public boolean deleteAreaChannel(int channelid, int userid);
	
	// area temp config
	public boolean addTempConfig(AreaTempConfig config);
	
	public AreaTempConfig getTempConfigByAreaid(int areaid);
	
	public boolean updateTempConfig(AreaTempConfig config);
	
	public boolean deleteTempConfig(int configid, int userid);

	public List<AreaTempConfig> getAllTempConfigs();

	// channels
	public List<Channel> getAllChannels();

	public boolean addChannel(Channel channel);

	public boolean updateChannel(Channel channel);

	public boolean deleteChannel(Channel channel);

	// machines
	public List<Machine> getAllMachines();

	public boolean addMachine(Machine machine);

	public boolean addMachines(List<Machine> machines, int userid);

	public boolean updateMachine(Machine machine);

	public boolean deleteMachine(Machine machine);
	
	// level - images
	public List<LevelImage> getAllLevels();
	
	public boolean addLevelImage(LevelImage level);
	
	public boolean modifyLevelImage(LevelImage level);
	
	public boolean deleteLevelImage(LevelImage level);
}
