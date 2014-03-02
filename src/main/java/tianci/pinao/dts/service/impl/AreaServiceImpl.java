package tianci.pinao.dts.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import tianci.pinao.dts.dao.AreaDao;
import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaHardwareConfig;
import tianci.pinao.dts.models.AreaTempConfig;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.LevelImage;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.service.AreaService;
import tianci.pinao.dts.util.PinaoConstants;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class AreaServiceImpl implements AreaService {
	
	private AreaDao areaDao;
	
	private String fileDir = "/assets/upload/";

	@Override
	public String addFile(String path, String data) {
		String name = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(data)){
			FileOutputStream fs = null;
			try {
				String[] splits = StringUtils.split(data, ";");
				name = fileDir +System.currentTimeMillis() + "." + splits[0].split("/")[1];
				File dir = new File(path + fileDir);
				if(!(dir.exists() && dir.isDirectory()))
					dir.mkdirs();
				fs = new FileOutputStream(path + name);
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
		
		return name;
	}

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
			area.setLastModUserid(userid);
			return areaDao.addArea(area);
		}
		return true;
	}

	@Override
	public boolean updateArea(Area area, int userid) {
		if(area != null && area.getId() > 0){
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

	@Override
	public List<Area> getAllAailableAreas() {
		return areaDao.getAllAreas();
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
	public boolean addMachines(List<Machine> machines) {
		return areaDao.addMachines(machines);
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

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean replaceAreas(String data, int userid) {
		if(StringUtils.isNotBlank(data)){
			String content = getContent(data);
			if(StringUtils.isNotBlank(content)){
				
				String[] lines = StringUtils.split(content, PinaoConstants.TEM_DATA_LINE_SEP);
				if(lines != null && lines.length > 0){
					List<Channel> channels = getAllChannels();
					Map<String, Map<String, Channel>> cMap = new HashMap<String, Map<String, Channel>>();
					if(channels != null && channels.size() > 0)
						for(Channel channel : channels){
							Map<String, Channel> tmp = cMap.get(channel.getMachineName());
							if(tmp == null){
								tmp = new HashMap<String, Channel>();
								cMap.put(channel.getMachineName(), tmp);
							}
							tmp.put(channel.getName(), channel);
						}

					List<Area> areas = new ArrayList<Area>();
					Map<String, Area> parents = new HashMap<String, Area>();
					Map<String, AreaHardwareConfig> hardConfigs = new HashMap<String, AreaHardwareConfig>();
					Map<String, AreaTempConfig> tempConfigs = new HashMap<String, AreaTempConfig>();
					Map<String, AreaChannel> areaChannels = new HashMap<String, AreaChannel>();
					
					for(String line : lines)
						if(StringUtils.isNotBlank(line) && !StringUtils.startsWith(line, PinaoConstants.FILE_COMMENT_PREFIX)){
							String[] cols = StringUtils.split(line, PinaoConstants.TEM_DATA_COL_SEP);
							if(cols != null && cols.length > 0 && StringUtils.isNotBlank(cols[0])){
								Area area = new Area();
								area.setName(StringUtils.trimToEmpty(cols[0]));
								if(cols.length > 1)
									area.setLevel(NumberUtils.toInt(cols[1], 0));
								if(cols.length > 2)
									area.setIndex(NumberUtils.toInt(cols[2], 0));
								if(cols.length > 3 && StringUtils.isNotBlank(cols[3]))
									parents.put(StringUtils.trimToEmpty(cols[3]), area);
								if(cols.length > 4)
									area.setImage(StringUtils.trimToEmpty(cols[4]));
								area.setLastModUserid(userid);
								areas.add(area);
								
								if((cols.length > 5 && StringUtils.isNotBlank(cols[5]))
										|| (cols.length > 6 && StringUtils.isNotBlank(cols[6]))
												|| (cols.length > 7 && StringUtils.isNotBlank(cols[7]))){
									AreaHardwareConfig config = new AreaHardwareConfig();
									if(cols.length > 5 && StringUtils.isNotBlank(cols[5]))
										config.setLight(StringUtils.trimToEmpty(cols[6]));
									if(cols.length > 6 && StringUtils.isNotBlank(cols[6]))
										config.setRelay(StringUtils.trimToEmpty(cols[6]));
									if(cols.length > 7 && StringUtils.isNotBlank(cols[7]))
										config.setVoice(StringUtils.trimToEmpty(cols[7]));
									
									config.setLastModUserid(userid);
									hardConfigs.put(area.getName(), config);
								}
								
								if((cols.length > 8 && NumberUtils.isNumber(cols[8]))
										|| (cols.length > 9 && NumberUtils.isNumber(cols[9]))
										|| (cols.length > 10 && NumberUtils.isNumber(cols[10]))
												|| (cols.length > 11 && NumberUtils.isNumber(cols[11]))){
									AreaTempConfig config = new AreaTempConfig();
									if(cols.length > 8 && NumberUtils.isNumber(cols[8]))
										config.setTemperatureLow(NumberUtils.toInt(cols[8], Integer.MAX_VALUE));
									if(cols.length > 9 && NumberUtils.isNumber(cols[9]))
										config.setTemperatureHigh(NumberUtils.toInt(cols[10], Integer.MAX_VALUE));
									if(cols.length > 10 && NumberUtils.isNumber(cols[10]))
										config.setTemperatureDiff(NumberUtils.toInt(cols[10], Integer.MAX_VALUE));
									if(cols.length > 11 && NumberUtils.isNumber(cols[11]))
										config.setExotherm(NumberUtils.toInt(cols[11], Integer.MAX_VALUE));
									
									config.setLastModUserid(userid);
									tempConfigs.put(area.getName(), config);
								}
								
								if((cols.length > 13 && StringUtils.isNotBlank(cols[13]))
										|| (cols.length > 14 && StringUtils.isNotBlank(cols[14]))
										|| (cols.length > 15 && NumberUtils.isNumber(cols[15]))
												|| (cols.length > 16 && NumberUtils.isNumber(cols[16]))){
									String tmpMachineName = StringUtils.trimToEmpty(cols[14]);
									if(cMap.containsKey(tmpMachineName)){
										String tmpChannelName = StringUtils.trimToEmpty(cols[13]);
										Map<String, Channel> ctmp = cMap.get(tmpMachineName);
										if(ctmp != null && ctmp.containsKey(tmpChannelName)){
											Channel tmpChannel = ctmp.get(tmpChannelName);
											int start = NumberUtils.toInt(cols[15], -1);
											int end = NumberUtils.toInt(cols[16], -1);
											if(start > 0 && end >= start){
												AreaChannel areaChannel = new AreaChannel();
												areaChannel.setStart(start);
												areaChannel.setEnd(end);
												areaChannel.setMachineid(tmpChannel.getMachineid());
												areaChannel.setChannelid(tmpChannel.getId());
												areaChannel.setName(StringUtils.trimToEmpty(cols[12]));
												
												areaChannel.setLastModUserid(userid);
												areaChannels.put(area.getName(), areaChannel);
											}
										}
									}
								}
							}
						}
					
					if(areas != null && areas.size() > 0){
						areaDao.deleteAllAreas(userid);
						if(areaDao.addAreas(areas)){
							areas = getAllAailableAreas();
							
							if(areas != null && areas.size() > 0)
								for(Area area : areas){
									int id = area.getId();
									String name = area.getName();
									
									if(parents.containsKey(name))
										parents.get(name).setParent(id);
									
									if(hardConfigs.containsKey(name))
										hardConfigs.get(name).setAreaid(id);

									if(tempConfigs.containsKey(name))
										tempConfigs.get(name).setAreaid(id);

									if(areaChannels.containsKey(name))
										areaChannels.get(name).setAreaid(id);
								}
							
							if(parents != null && parents.size() > 0)
								areaDao.updateAreaParents(new ArrayList<Area>(parents.values()));
							
							if(hardConfigs != null && hardConfigs.size() > 0){
								areaDao.deleteAllHardwareConfigs(userid);
								areaDao.addHardwareConfigs(new ArrayList<AreaHardwareConfig>(hardConfigs.values()));
							}
							
							if(tempConfigs != null && tempConfigs.size() > 0){
								areaDao.deleteAllTempConfigs(userid);
								areaDao.addTempConfigs(new ArrayList<AreaTempConfig>(tempConfigs.values()));
							}
							
							if(areaChannels != null && areaChannels.size() > 0){
								areaDao.deleteAllAreaChannels(userid);
								areaDao.addAreaChannels(new ArrayList<AreaChannel>(areaChannels.values()));
							}
							
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean replaceChannels(String data, int userid) {
		if(StringUtils.isNotBlank(data)){
			String content = getContent(data);
			if(StringUtils.isNotBlank(content)){
				List<Channel> channels = new ArrayList<Channel>();
				
				String[] lines = StringUtils.split(content, PinaoConstants.TEM_DATA_LINE_SEP);
				if(lines != null && lines.length > 0){
					List<Machine> machines = getAllMachines();
					Map<String, Machine> mMap = new HashMap<String, Machine>();
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines)
							mMap.put(machine.getName(), machine);
					
					for(String line : lines)
						if(StringUtils.isNotBlank(line) && !StringUtils.startsWith(line, PinaoConstants.FILE_COMMENT_PREFIX)){
							String[] cols = StringUtils.split(line, PinaoConstants.TEM_DATA_COL_SEP);
							if(cols != null && cols.length > 2 && mMap.containsKey(cols[1]) && NumberUtils.isNumber(cols[2])){
								Channel channel = new Channel();
								channel.setName(StringUtils.trimToEmpty(cols[0]));
								channel.setMachineid(mMap.get(cols[1]).getId());
								channel.setLength(NumberUtils.toInt(cols[2], -1));
								channel.setLastModUserid(userid);
								channels.add(channel);
							}
						}
				}
				
				if(channels != null && channels.size() > 0){
					areaDao.deleteAllChannels(userid);
					return areaDao.addChannels(channels);
				}
			}
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean replaceMachines(String data, int userid) {
		if(StringUtils.isNotBlank(data)){
			String content = getContent(data);
			if(StringUtils.isNotBlank(content)){
				List<Machine> machines = new ArrayList<Machine>();
				
				String[] lines = StringUtils.split(content, PinaoConstants.TEM_DATA_LINE_SEP);
				if(lines != null && lines.length > 0)
					for(String line : lines)
						if(StringUtils.isNotBlank(line) && !StringUtils.startsWith(line, PinaoConstants.FILE_COMMENT_PREFIX)){
							String[] cols = StringUtils.split(line, PinaoConstants.TEM_DATA_COL_SEP);
							if(cols != null && cols.length > 2){
								Machine machine = new Machine();
								machine.setName(StringUtils.trimToEmpty(cols[0]));
								machine.setSerialPort(StringUtils.trimToEmpty(cols[1]));
								machine.setBaudRate(StringUtils.trimToEmpty(cols[2]));
								machine.setLastModUserid(userid);
								machines.add(machine);
							}
						}
				
				if(machines != null && machines.size() > 0){
					areaDao.deleteAllMachines(userid);
					return areaDao.addMachines(machines);
				}
			}
		}
		return false;
	}
	
	private String getContent(String data){
		return StringUtils.split(data, ";")[1].split(",")[1];
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
