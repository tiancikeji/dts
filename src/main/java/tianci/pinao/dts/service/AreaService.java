package tianci.pinao.dts.service;

import java.util.List;
import java.util.Map;

import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaHardwareConfig;
import tianci.pinao.dts.models.AreaTempConfig;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.LevelImage;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.models.User;

public interface AreaService {

	public List<Area> getAllAreas(long userid, User user);

	public Map<Machine, List<Channel>> getAllChannels(long userid);

	// only for download...
	public List<Area> getAllAailableAreas();
	// end of only for download...

	public boolean addArea(Area area, int userid);

	public boolean updateArea(Area area, int userid);

	public boolean deleteArea(Area area, int userid);

	public List<AreaHardwareConfig> getAllHardwareConfigs();

	public boolean addHardwareConfig(AreaHardwareConfig config, int userid);

	public boolean updateHardwareConfig(AreaHardwareConfig config, int userid);

	public boolean deleteHardwareConfig(AreaHardwareConfig config, int userid);

	public List<AreaTempConfig> getAllTempConfigs();

	public boolean addAreaTempConfig(AreaTempConfig config, int userid);

	public boolean updateAreaTempConfig(AreaTempConfig config, int userid);

	public boolean deleteAreaTempConfig(AreaTempConfig config, int userid);

	public List<AreaChannel> getAllAreaChannels();

	public boolean addAreaChannel(List<AreaChannel> channels, int userid);

	public boolean updateAreaChannel(AreaChannel channel, int userid);

	public boolean deleteAreaChannel(AreaChannel channel, int userid);

	public List<Channel> getAllAvailableChannels();

	public boolean addChannel(Channel channel, int userid);

	public boolean updateChannel(Channel channel, int userid);

	public boolean deleteChannel(Channel channel, int userid);

	public List<Machine> getAllMachines();

	public boolean addMachine(Machine machine, int userid);

	public boolean addMachines(List<Machine> machine);

	public boolean updateMachine(Machine machine, int userid);

	public boolean deleteMachine(Machine machine, int userid);
	
	// level - images
	public List<LevelImage> getAllLevels();
	
	public boolean addLevelImage(LevelImage level, int userid);
	
	public boolean modifyLevelImage(LevelImage level, int userid);
	
	public boolean deleteLevelImage(LevelImage level, int userid);

	// upload image
	public String addFile(String path, String data);

	// upload areas/channels/machines
	public boolean replaceAreas(String data, int userid);

	public boolean replaceChannels(String data, int userid);

	public boolean replaceMachines(String data, int userid);
	
}
