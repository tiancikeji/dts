package tianci.pinao.dts.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import tianci.pinao.dts.dao.AreaDao;
import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaHardwareConfig;
import tianci.pinao.dts.models.AreaTempConfig;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.LevelImage;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.service.AreaService;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class AreaServiceImpl implements AreaService {
	
	private AreaDao areaDao;
	
	private String fileDir;

	// area
	@Override
	public boolean deleteArea(Area area, int userid) {
		if(area != null && area.getId() > 0){
			return areaDao.deleteArea(area.getId(), userid);
		}
		return false;
	}

	@Override
	public boolean addArea(Area area, int userid) {
		if(area != null){
			if(StringUtils.isNotBlank(area.getImage())){
				saveImage(area);
			}
			area.setLastModUserid(userid);
			return areaDao.addArea(area);
		}
		return true;
	}

	public void saveImage(Area area) {
		FileOutputStream fs = null;
		try {
			String image = area.getImage();
			String[] splits = image.split(";");
			String _name = area.getName() + System.currentTimeMillis() + "." + splits[0].split("/")[1];
			area.setImage(_name);
			String filename = fileDir+_name;
			fs = new FileOutputStream(filename);
		    fs.write(Base64.decode(splits[1].split(",")[1])); 
		} catch (Throwable t) {
			t.printStackTrace();
		} finally{
			if(fs != null)
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public boolean updateArea(Area area, int userid) {
		if(area != null && area.getId() > 0){
			if(StringUtils.isNotBlank(area.getImage())){
				saveImage(area);
			}
			area.setLastModUserid(userid);
			return areaDao.updateArea(area);
		}
		return true;
	}

	@Override
	public List<Area> getAllAreas() {
		List<Area> areas = areaDao.getAllAreas();
		
		List<Area> result = new ArrayList<Area>();
		Map<Integer, Area> tmps = new HashMap<Integer, Area>();
		if(areas != null && areas.size() > 0){
			for(Area area : areas){
				if(area.getParent() <= 0)
					result.add(area);
				tmps.put(area.getId(), area);
			}
			
			for(Area area : areas)
				if(area.getParent() > 0 && tmps.containsKey(area.getParent())){
					Area tmp = tmps.get(area.getParent());
					if(tmp != null){
						List<Area> children = tmp.getChildren();
						if(children == null){
							children = new ArrayList<Area>();
							tmp.setChildren(children);
						}
						children.add(area);
					}
				}
		}
			
		
		return result;
	}

	// area hardware config
	@Override
	public boolean addHardwareConfig(AreaHardwareConfig config, int userid) {
		if(config != null){
			config.setLastModUserid(userid);
			return areaDao.addHardwareConfig(config);
		}else
			return true;
	}

	@Override
	public boolean updateHardwareConfig(AreaHardwareConfig config, int userid) {
		if(config != null && config.getId() > 0){
			config.setLastModUserid(userid);
			return areaDao.updateHardwareConfig(config);
		}else
			return true;
	}

	@Override
	public boolean deleteHardwareConfig(AreaHardwareConfig config, int userid) {
		if(config != null && config.getId() > 0)
			return areaDao.deleteHardwareConfig(config.getId(), userid);
		else
			return true;
	}

	@Override
	public List<AreaHardwareConfig> getAllHardwareConfigs() {
		List<AreaHardwareConfig> configs = areaDao.getAllHardwareConfigs();
		if(configs != null && configs.size() > 0){
			Map<Integer, AreaHardwareConfig> areaids = new HashMap<Integer, AreaHardwareConfig>();
			for(AreaHardwareConfig config : configs)
				areaids.put(config.getAreaid(), config);
			
			List<Area> areas = areaDao.getAllAreas();
			if(areas != null && areas.size() > 0)
				for(Area area : areas)
					if(areaids.keySet().contains(area.getId()))
						areaids.get(area.getId()).setAreaName(area.getName());
		}
		
		return configs;
	}

	// area temp config
	@Override
	public boolean addAreaTempConfig(AreaTempConfig config, int userid) {
		if(config != null){
			config.setLastModUserid(userid);
			return areaDao.addTempConfig(config);
		} else
			return true;
	}

	@Override
	public boolean updateAreaTempConfig(AreaTempConfig config, int userid) {
		if(config != null && config.getId() > 0){
			config.setLastModUserid(userid);
			return areaDao.updateTempConfig(config);
		} else
			return true;
	}

	@Override
	public boolean deleteAreaTempConfig(AreaTempConfig config, int userid) {
		if(config != null && config.getId() > 0)
			return areaDao.deleteTempConfig(config.getId(), userid);
		else 
			return true;
	}

	@Override
	public List<AreaTempConfig> getAllTempConfigs() {
		List<AreaTempConfig> configs = areaDao.getAllTempConfigs();
		if(configs != null && configs.size() > 0){
			Map<Integer, AreaTempConfig> areaids = new HashMap<Integer, AreaTempConfig>();
			for(AreaTempConfig config : configs)
				areaids.put(config.getAreaid(), config);
			
			List<Area> areas = areaDao.getAllAreas();
			if(areas != null && areas.size() > 0)
				for(Area area : areas)
					if(areaids.keySet().contains(area.getId()))
						areaids.get(area.getId()).setAreaName(area.getName());
		}
		
		return configs;
	}

	// area channels
	@Override
	public boolean addAreaChannel(List<AreaChannel> channels, int userid) {
		if(channels != null && channels.size() > 0){
			for(AreaChannel channel : channels)
				channel.setLastModUserid(userid);
			return areaDao.addAreaChannels(channels);
		}
		return true;
	}

	@Override
	public boolean updateAreaChannel(AreaChannel channel, int userid) {
		if(channel != null && channel.getId() > 0){
			channel.setLastModUserid(userid);
			return areaDao.updateAreaChannel(channel);
		} else
			return false;
	}

	@Override
	public boolean deleteAreaChannel(AreaChannel channel, int userid) {
		if(channel != null && channel.getId() > 0){
			return areaDao.deleteAreaChannel(channel.getId(), userid);
		} else
			return false;
	}

	@Override
	public List<AreaChannel> getAllAreaChannels() {
		List<AreaChannel> areachannels = areaDao.getAllAreaChannels();
		
		if(areachannels != null && areachannels.size() > 0){
			Map<Integer, List<AreaChannel>> areaids = new HashMap<Integer, List<AreaChannel>>();
			Map<Integer, List<AreaChannel>> channelids = new HashMap<Integer, List<AreaChannel>>();
			for(AreaChannel areachannel : areachannels){
				List<AreaChannel> tmp = areaids.get(areachannel.getAreaid());
				if(tmp == null){
					tmp = new ArrayList<AreaChannel>();
					areaids.put(areachannel.getAreaid(), tmp);
				}
				tmp.add(areachannel);
				
				tmp = channelids.get(areachannel.getChannelid());
				if(tmp == null){
					tmp = new ArrayList<AreaChannel>();
					channelids.put(areachannel.getChannelid(), tmp);
				}
				tmp.add(areachannel);
			}
			
			List<Area> areas = areaDao.getAllAreas();
			if(areas != null && areas.size() > 0)
				for(Area area : areas)
					if(areaids.keySet().contains(area.getId()))
						for(AreaChannel channel : areaids.get(area.getId()))
							channel.setAreaName(area.getName());
			
			List<Channel> channels = getAllChannels();
			if(channels != null && channels.size() > 0)
				for(Channel channel : channels)
					if(channelids.keySet().contains(channel.getId()))
						for(AreaChannel areachannel : channelids.get(channel.getId())){
							areachannel.setChannelName(channel.getName());
							areachannel.setMachineName(channel.getMachineName());
						}
		}
		
		return areachannels;
	}

	// channels
	@Override
	public boolean addChannel(Channel channel, int userid) {
		if(channel != null){
			channel.setLastModUserid(userid);
			return areaDao.addChannel(channel);
		} else
			return true;
	}

	@Override
	public boolean updateChannel(Channel channel, int userid) {
		if(channel != null && channel.getId() > 0){
			channel.setLastModUserid(userid);
			return areaDao.updateChannel(channel);
		}
		return true;
	}

	@Override
	public boolean deleteChannel(Channel channel, int userid) {
		if(channel != null && channel.getId() > 0){
			channel.setLastModUserid(userid);
			return areaDao.deleteChannel(channel);
		}
		return true;
	}

	@Override
	public List<Channel> getAllChannels() {
		List<Channel> channels = areaDao.getAllChannels();
		if(channels != null && channels.size() > 0){
			Map<Integer, List<Channel>> machineids = new HashMap<Integer, List<Channel>>();
			for(Channel channel : channels){
				List<Channel> tmp = machineids.get(channel.getMachineid());
				if(tmp == null){
					tmp = new ArrayList<Channel>();
					machineids.put(channel.getMachineid(), tmp);
				}
				tmp.add(channel);
			}
			
			List<Machine> machines = getAllMachines();
			if(machines != null && machines.size() > 0)
				for(Machine machine : machines)
					if(machineids.keySet().contains(machine.getId()))
						for(Channel channel : machineids.get(machine.getId()))
							channel.setMachineName(machine.getName());
		}
		
		return channels;
	}
	
	// machines
	@Override
	public boolean addMachine(Machine machine, int userid) {
		if(machine != null){
			machine.setLastModUserid(userid);
			return areaDao.addMachine(machine);
		} else
			return true;
	}

	@Override
	public boolean addMachines(List<Machine> machines, int userid) {
		return areaDao.addMachines(machines, userid);
	}

	@Override
	public boolean updateMachine(Machine machine, int userid) {
		if(machine != null){
			machine.setLastModUserid(userid);
			return areaDao.updateMachine(machine);
		} else
			return true;
	}

	@Override
	public boolean deleteMachine(Machine machine, int userid) {
		if(machine != null){
			machine.setLastModUserid(userid);
			return areaDao.deleteMachine(machine);
		} else
			return true;
	}
	
	public List<Machine> getAllMachines() {
		return areaDao.getAllMachines();
	}

	@Override
	public List<LevelImage> getAllLevels() {
		return areaDao.getAllLevels();
	}

	@Override
	public boolean addLevelImage(LevelImage level, int userid) {
		if(level != null){
			level.setLastModUserid(userid);
			return areaDao.addLevelImage(level);
		} else
			return false;
	}

	@Override
	public boolean modifyLevelImage(LevelImage level, int userid) {
		if(level != null && level.getId() > 0){
			level.setLastModUserid(userid);
			return areaDao.modifyLevelImage(level);
		} else
			return areaDao.modifyLevelImage(level);
	}

	@Override
	public boolean deleteLevelImage(LevelImage level, int userid) {
		if(level != null && level.getId() > 0){
			level.setLastModUserid(userid);
			return areaDao.deleteLevelImage(level);
		} else
			return areaDao.deleteLevelImage(level);
	}

	public AreaDao getAreaDao() {
		return areaDao;
	}

	public void setAreaDao(AreaDao areaDao) {
		this.areaDao = areaDao;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}
}
