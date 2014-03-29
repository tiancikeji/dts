package tianci.pinao.dts.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import tianci.pinao.dts.dao.AlarmDao;
import tianci.pinao.dts.dao.AreaDao;
import tianci.pinao.dts.dao.TemDao;
import tianci.pinao.dts.models.Alarm;
import tianci.pinao.dts.models.AlarmHistory;
import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaHardwareConfig;
import tianci.pinao.dts.models.AreaMonitorData;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.ChannelMonitorData;
import tianci.pinao.dts.models.Check;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.models.ReportData;
import tianci.pinao.dts.models.Temperature;
import tianci.pinao.dts.service.TemService;
import tianci.pinao.dts.util.PinaoConstants;

public class TemServiceImpl implements TemService {
	
	private AreaDao areaDao;
	
	private TemDao temDao;
	
	private AlarmDao alarmDao;

	@Override
	public void checkHardware(long userid) {
		List<Channel> channels = areaDao.getAllChannels();
		Map<Integer, Channel> channelMaps = new HashMap<Integer, Channel>();
		if(channels != null && channels.size() > 0)
			for(Channel channel : channels)
				channelMaps.put(channel.getId(), channel);
		
		List<AreaHardwareConfig> ahwcs = areaDao.getAllHardwareConfigs();
		Map<Integer, AreaHardwareConfig> ahwcMaps = new HashMap<Integer, AreaHardwareConfig>();
		if(ahwcs != null && ahwcs.size() > 0)
			for(AreaHardwareConfig ahwc : ahwcs)
				ahwcMaps.put(ahwc.getAreaid(), ahwc);
		
		List<Check> checks = new ArrayList<Check>();
		
		List<AreaChannel> acs = areaDao.getAllAreaChannels();
		if(acs != null && acs.size() > 0)
			for(AreaChannel ac : acs)
				if(channelMaps.containsKey(ac.getChannelid()) && ahwcMaps.containsKey(ac.getAreaid())){
					Check check = new Check();
					check.setMachineId(channelMaps.get(ac.getChannelid()).getMachineid());
					check.setAreaId(ac.getAreaid());
					check.setChannelId(ac.getChannelid());
					
					AreaHardwareConfig config = ahwcMaps.get(ac.getAreaid());
					check.setLight(config.getLight());
					check.setRelay(config.getRelay());
					check.setRelay1(config.getRelay1());
					check.setVoice(config.getVoice());
					
					checks.add(check);
				}
		
		if(checks != null && checks.size() > 0)
			alarmDao.addChecks(checks, userid);
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean updateAlarm(long id, int status, long userid) {
		if(alarmDao.updateAlarm(id, status, userid))
			if(alarmDao.addAlarmHistory(id, status, userid))
				return true;
			else
				throw new RuntimeException("exception in add alarm history, so rollback...");
		return true;
	}

	@Override
	public List<Alarm> getChannelAlarmReportData(List<Channel> channels, int start, int step) {
		if(channels != null && channels.size() > 0){
			List<Integer> ids = new ArrayList<Integer>();
			for(Channel channel : channels)
				ids.add(channel.getId());
			
			// get alarm data
			List<Alarm> alarms = alarmDao.getAlarmsByChannelIds(ids, new Integer[]{Alarm.STATUS_NEW, Alarm.STATUS_ALARMED, Alarm.STATUS_NOTIFY, Alarm.STATUS_MUTE, Alarm.STATUS_MUTED, Alarm.STATUS_RESET, Alarm.STATUS_RESETED}, start, step); 
			
			if(alarms != null && alarms.size() > 0){
				Map<Long, Alarm> alarmMaps = new HashMap<Long, Alarm>();
				Set<Long> alarmIds = new HashSet<Long>();
				for(Alarm alarm : alarms){
					alarmIds.add(alarm.getId());
					alarmMaps.put(alarm.getId(), alarm);
				}

				List<AlarmHistory> historys = alarmDao.getAlarmHistorysByAlarmIds(alarmIds);
				
				if(historys != null && historys.size() > 0){
					for(AlarmHistory history : historys)
						alarmMaps.get(history.getAlarmId()).getHistory().add(history);
				}
			}
			
			return alarms;
		}
		
		return null;
	}

	@Override
	public int getChannelAlarmCount(List<Channel> channels){
		if(channels != null && channels.size() > 0){
			List<Integer> ids = new ArrayList<Integer>();
			for(Channel channel : channels)
				ids.add(channel.getId());
			return alarmDao.getAlarmCountByChannelIds(ids, new Integer[]{Alarm.STATUS_NEW, Alarm.STATUS_ALARMED, Alarm.STATUS_NOTIFY, Alarm.STATUS_MUTE, Alarm.STATUS_MUTED});
		} else
			return 0;
	}

	@Override
	public int getChannelAlarmReportCount(List<Channel> channels){
		if(channels != null && channels.size() > 0){
			List<Integer> ids = new ArrayList<Integer>();
			for(Channel channel : channels)
				ids.add(channel.getId());
			return alarmDao.getAlarmCountByChannelIds(ids, new Integer[]{Alarm.STATUS_NEW, Alarm.STATUS_ALARMED, Alarm.STATUS_NOTIFY, Alarm.STATUS_MUTE, Alarm.STATUS_MUTED, Alarm.STATUS_RESET, Alarm.STATUS_RESETED});
		} else
			return 0;
	}

	@Override
	public List<Alarm> getChannelAlarmData(List<Channel> channels, int start, int step) {
		if(channels != null && channels.size() > 0){
			List<Integer> ids = new ArrayList<Integer>();
			for(Channel channel : channels)
				ids.add(channel.getId());
			
			// get alarm data
			List<Alarm> alarms = alarmDao.getAlarmsByChannelIds(ids, new Integer[]{Alarm.STATUS_NEW, Alarm.STATUS_ALARMED, Alarm.STATUS_NOTIFY, Alarm.STATUS_MUTE, Alarm.STATUS_MUTED}, start, step); 
			
			if(alarms != null && alarms.size() > 0){
				Map<Long, Alarm> alarmMaps = new HashMap<Long, Alarm>();
				Set<Long> alarmIds = new HashSet<Long>();
				for(Alarm alarm : alarms){
					alarmIds.add(alarm.getId());
					alarmMaps.put(alarm.getId(), alarm);
				}

				List<AlarmHistory> historys = alarmDao.getAlarmHistorysByAlarmIds(alarmIds);
				
				if(historys != null && historys.size() > 0){
					for(AlarmHistory history : historys)
						alarmMaps.get(history.getAlarmId()).getHistory().add(history);
				}
			}
			
			return alarms;
		}
		
		return null;
	}

	@Override
	public ChannelMonitorData getChannelData(List<Channel> channels, long time, int start, int end) {
		if(channels != null && channels.size() > 0){
			List<Integer> ids = new ArrayList<Integer>();
			Map<Integer, String> names = new HashMap<Integer, String>();
			for(Channel channel : channels){
				ids.add(channel.getId());
				names.put(channel.getId(), channel.getMachineName() + "-" + channel.getName());
			}
			
			Date date = null;
			if(time > 0)
				date = new Date();
			
			// get temperature
			List<Temperature> tems = temDao.getLatestTemNStocksByChannel(ids, date);
			if(tems != null && tems.size() > 0){
				ChannelMonitorData data = new ChannelMonitorData();
				double max = 0;
				double min = Double.MAX_VALUE;
				double avg = 0;
				int count = 0;
				long latest = 0;
				
				Map<String, Map<String, Object>> _data = new HashMap<String, Map<String,Object>>();
				for(Temperature tem : tems){
					if(tem.getDate() != null){
						long tmpTime = tem.getDate().getTime();
						if(tmpTime > latest)
							latest = tmpTime;
					}

					Map<String, Object> _tmp = new HashMap<String, Object>();
					int channel = tem.getChannel();
					_data.put(names.get(channel), _tmp);
					
					List<Integer> _length = new ArrayList<Integer>();
					_tmp.put("length", _length);
					List<Double> _tems = new ArrayList<Double>();
					_tmp.put("tems", _tems);
					List<Double> _stocks = new ArrayList<Double>();
					_tmp.put("stocks", _stocks);
					List<Double> _unstocks = new ArrayList<Double>();
					_tmp.put("unstocks", _unstocks);
					
					String[] tmpTem = StringUtils.split(tem.getTem(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					String[] tmpStock = StringUtils.split(tem.getStock(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					String[] tmpUnstock = StringUtils.split(tem.getUnstock(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					for(int i = start - 1; i < tmpTem.length && i < end; i ++){
						_length.add(i + 1);
						
						double tmpDouble = NumberUtils.toDouble(tmpTem[i], 0);
						_tems.add(tmpDouble);

						if(tmpStock != null && tmpStock.length > i)
							_stocks.add(NumberUtils.toDouble(tmpStock[i], 0));
						if(tmpUnstock != null && tmpUnstock.length > i)
							_unstocks.add(NumberUtils.toDouble(tmpUnstock[i], 0));
						
						avg += tmpDouble;
						count ++;
						
						if(tmpDouble > max)
							max = tmpDouble;
						if(tmpDouble < min)
							min = tmpDouble;
					}
				}
				
				if(count > 0)
					avg = new BigDecimal(avg).divide(new BigDecimal(count), 5, RoundingMode.HALF_UP).doubleValue();
				
				data.setMax(max);
				data.setMin(min);
				data.setAvg(avg);
				data.setTime(latest);
				data.setData(_data);
				
				return data;
			}
		}
		
		return null;
	}

	@Override
	public ReportData getChannelReportData(List<Channel> channels, Date startDate, Date endDate, int start, int end) {
		if(channels != null && channels.size() > 0){
			List<Integer> ids = new ArrayList<Integer>();
			Map<Integer, String> names = new HashMap<Integer, String>();
			for(Channel channel : channels){
				ids.add(channel.getId());
				names.put(channel.getId(), channel.getMachineName() + "-" + channel.getName());
			}
			
			// get temperature
			List<Temperature> tems = temDao.getTemNStocksByChannel(ids, startDate, endDate);
			if(tems != null && tems.size() > 0){
				double max = 0;
				double min = Double.MAX_VALUE;
				double avg = 0;
				int count = 0;
				Map<String, Map<Date, Double>> temData = new HashMap<String, Map<Date,Double>>();
				Map<String, Map<Date, Double>> stockData = new HashMap<String, Map<Date,Double>>();
				Map<String, Map<Date, Double>> unstockData = new HashMap<String, Map<Date,Double>>();
				
				for(Temperature tem : tems){
					int tmpChannel = tem.getChannel();
	
					String[] tmpTem = StringUtils.split(tem.getTem(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					for(int i = start - 1; i < tmpTem.length && i < end; i ++){
						double tmpDouble = NumberUtils.toDouble(tmpTem[i], 0);
						avg += tmpDouble;
						count ++;
						
						if(tmpDouble > max)
							max = tmpDouble;
						if(tmpDouble < min)
							min = tmpDouble;
						
						String key = names.get(tmpChannel) + "-" + i;
						Map<Date, Double> _tmp = temData.get(key);
						if(_tmp == null){
							_tmp = new HashMap<Date, Double>();
							temData.put(key, _tmp);
						}
						_tmp.put(tem.getDate(), tmpDouble);
					}
	
					String[] tmpStock = StringUtils.split(tem.getStock(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					for(int i = 0; i < tmpStock.length; i ++){
						String key = names.get(tmpChannel) + "-" + i;
						Map<Date, Double> _tmp = stockData.get(key);
						if(_tmp == null){
							_tmp = new HashMap<Date, Double>();
							stockData.put(key, _tmp);
						}
						_tmp.put(tem.getDate(), NumberUtils.toDouble(tmpStock[i], 0));
					}
					
					String[] tmpUnstock = StringUtils.split(tem.getUnstock(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					for(int i = 0; i < tmpUnstock.length; i ++){
						String key = names.get(tmpChannel) + "-" + i;
						Map<Date, Double> _tmp = unstockData.get(key);
						if(_tmp == null){
							_tmp = new HashMap<Date, Double>();
							unstockData.put(key, _tmp);
						}
						_tmp.put(tem.getDate(), NumberUtils.toDouble(tmpUnstock[i], 0));
					}
				
					if(count > 0)
						avg = new BigDecimal(avg).divide(new BigDecimal(count), 5, RoundingMode.HALF_UP).doubleValue();
	
					ReportData data = new ReportData();
					data.setMax(max);
					data.setMin(min);
					data.setAvg(avg);
					data.setTems(temData);
					data.setStocks(stockData);
					data.setUnstocks(unstockData);
					
					return data;
				}
			}
		}
		
		return null;
	}

	@Override
	public ReportData getAreaReportData(Area area, Date startDate, Date endDate) {
		if(area != null){
			// get sub-area ids
			Map<Integer, Area> areaMaps = new HashMap<Integer, Area>();
			areaMaps.put(area.getId(), area);
			getAreaMaps(area.getChildren(), areaMaps);
			
			List<Integer> areaIds = new ArrayList<Integer>(areaMaps.keySet());
			
			// get area channel
			List<AreaChannel> areaChannels = areaDao.getAreaChannelsByAreaids(areaIds);
			if(areaChannels != null && areaChannels.size() > 0){
					Map<Integer, List<AreaChannel>> channelids = new HashMap<Integer, List<AreaChannel>>();
					for(AreaChannel areachannel : areaChannels){
						List<AreaChannel> tmp = channelids.get(areachannel.getChannelid());
						if(tmp == null){
							tmp = new ArrayList<AreaChannel>();
							channelids.put(areachannel.getChannelid(), tmp);
						}
						tmp.add(areachannel);
						
						if(areaMaps.containsKey(areachannel.getAreaid()))
							areachannel.setAreaName(areaMaps.get(areachannel.getAreaid()).getName());
					}
				
				// get channel_id, start, end
				Map<Integer, List<AreaChannel>> acMap = new HashMap<Integer, List<AreaChannel>>();
				for(AreaChannel areaChannel : areaChannels){
					List<AreaChannel> tmp = acMap.get(areaChannel.getChannelid());
					if(tmp == null){
						tmp = new ArrayList<AreaChannel>();
						acMap.put(areaChannel.getChannelid(), tmp);
					}
					tmp.add(areaChannel);
				}
				
				// get temperature
				List<Temperature> tems = temDao.getTemsByChannel(new ArrayList<Integer>(acMap.keySet()), startDate, endDate);
				
				if(tems != null && tems.size() > 0){
					double max = 0;
					double min = Double.MAX_VALUE;
					double avg = 0;
					int count = 0;
					Map<String, Map<Date, Double>> temData = new HashMap<String, Map<Date, Double>>();
					Map<String, Map<Date, Double>> stockData = new HashMap<String, Map<Date, Double>>();
					Map<String, Map<Date, Double>> unstockData = new HashMap<String, Map<Date, Double>>();
					
					for(Temperature tem : tems){
						int tmpChannel = tem.getChannel();
						
						List<AreaChannel> tmp = acMap.get(tmpChannel);
						if(tmp != null && tmp.size() > 0)
							for(AreaChannel channel : tmp){
								int start = channel.getStart();
								int end = channel.getEnd();
								
								int i = start - 1;
								if(i < 0)
									i = 0;

								String[] tmpTem = StringUtils.split(tem.getTem(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
								for(; i < tmpTem.length && i < end; i ++){
									double tmpDouble = NumberUtils.toDouble(tmpTem[i], 0);
									avg += tmpDouble;
									count ++;
									
									if(tmpDouble > max)
										max = tmpDouble;
									if(tmpDouble < min)
										min = tmpDouble;
									
									String key = channel.getAreaName() + "-" + i;
									Map<Date, Double> _tmp = temData.get(key);
									if(_tmp == null){
										_tmp = new HashMap<Date, Double>();
										temData.put(key, _tmp);
									}
									_tmp.put(tem.getDate(), tmpDouble);
								}
								
								i = start - 1;
								if(i < 0)
									i = 0;

								String[] tmpStock = StringUtils.split(tem.getStock(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
								for(; i < tmpStock.length && i < end; i ++){
									String key = channel.getAreaName() + "-" + i;
									Map<Date, Double> _tmp = stockData.get(key);
									if(_tmp == null){
										_tmp = new HashMap<Date, Double>();
										stockData.put(key, _tmp);
									}
									_tmp.put(tem.getDate(), NumberUtils.toDouble(tmpStock[i], 0));
								}
								
								i = start - 1;
								if(i < 0)
									i = 0;

								String[] tmpUnstock = StringUtils.split(tem.getUnstock(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
								for(; i < tmpUnstock.length && i < end; i ++){
									String key = channel.getAreaName() + "-" + i;
									Map<Date, Double> _tmp = unstockData.get(key);
									if(_tmp == null){
										_tmp = new HashMap<Date, Double>();
										unstockData.put(key, _tmp);
									}
									_tmp.put(tem.getDate(), NumberUtils.toDouble(tmpUnstock[i], 0));
								}
							}
					}
					
					if(count > 0)
						avg = new BigDecimal(avg).divide(new BigDecimal(count), 5, RoundingMode.HALF_UP).doubleValue();

					ReportData data = new ReportData();
					data.setMax(max);
					data.setMin(min);
					data.setAvg(avg);
					data.setTems(temData);
					data.setStocks(stockData);
					data.setUnstocks(unstockData);
					
					return data;
				}
			}
		}
		
		return null;
	}
	
	public List<Channel> getAllAvailableChannels() {
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
			
			List<Machine> machines = areaDao.getAllMachines();
			if(machines != null && machines.size() > 0)
				for(Machine machine : machines)
					if(machineids.keySet().contains(machine.getId()))
						for(Channel channel : machineids.get(machine.getId()))
							channel.setMachineName(machine.getName());
		}
		
		return channels;
	}

	@Override
	public List<Alarm> getAreaAlarmReportData(Area area, int start, int end) {
		if(area != null){
			// get sub-area ids
			Map<Integer, Area> areaMaps = new HashMap<Integer, Area>();
			areaMaps.put(area.getId(), area);
			getAreaMaps(area.getChildren(), areaMaps);
			
			List<Integer> areaIds = new ArrayList<Integer>(areaMaps.keySet());
				
			// get alarm data
			List<Alarm> alarms = alarmDao.getAlarmsByAreaIds(areaIds, new Integer[]{Alarm.STATUS_NEW, Alarm.STATUS_ALARMED, Alarm.STATUS_NOTIFY, Alarm.STATUS_MUTE, Alarm.STATUS_MUTED, Alarm.STATUS_RESET, Alarm.STATUS_RESETED}, start, end); 
			
			if(alarms != null && alarms.size() > 0){
				Map<Long, Alarm> alarmMaps = new HashMap<Long, Alarm>();
				Set<Long> alarmIds = new HashSet<Long>();
				for(Alarm alarm : alarms){
					alarmIds.add(alarm.getId());
					alarmMaps.put(alarm.getId(), alarm);
				}

				List<AlarmHistory> historys = alarmDao.getAlarmHistorysByAlarmIds(alarmIds);
				
				if(historys != null && historys.size() > 0){
					for(AlarmHistory history : historys)
						alarmMaps.get(history.getAlarmId()).getHistory().add(history);
				}
			}
			
			return alarms;
		}
		
		return null;
	}

	@Override
	public int getAreaAlarmReportCount(Area area){
		if(area != null){
			// get sub-area ids
			Map<Integer, Area> areaMaps = new HashMap<Integer, Area>();
			areaMaps.put(area.getId(), area);
			getAreaMaps(area.getChildren(), areaMaps);
			
			List<Integer> areaIds = new ArrayList<Integer>(areaMaps.keySet());
				
			// get alarm data
			return alarmDao.getAlarmCountByAreaIds(areaIds, new Integer[]{Alarm.STATUS_NEW, Alarm.STATUS_ALARMED, Alarm.STATUS_NOTIFY, Alarm.STATUS_MUTE, Alarm.STATUS_MUTED, Alarm.STATUS_RESET, Alarm.STATUS_RESETED}); 
		}
		
		return 0;
	}

	@Override
	public List<Alarm> getAreasAlarmData(List<Area> areas, long time) {
		if(areas != null && areas.size() > 0){
			// get sub-area ids
			Map<Integer, Area> areaMaps = new HashMap<Integer, Area>();
			getAreaMaps(areas, areaMaps);
			
			List<Integer> areaIds = new ArrayList<Integer>(areaMaps.keySet());
			
			Date date = null;
			if(time > 0)
				date = new Date(time);
			else
				date = new Date();
			
			// get alarm data
			return alarmDao.getAlarms(areaIds, date); 
		}
		
		return null;
	}

	@Override
	public List<Alarm> getAreaAlarmData(Area area, int start, int end) {
		if(area != null){
			// get sub-area ids
			Map<Integer, Area> areaMaps = new HashMap<Integer, Area>();
			areaMaps.put(area.getId(), area);
			getAreaMaps(area.getChildren(), areaMaps);
			
			List<Integer> areaIds = new ArrayList<Integer>(areaMaps.keySet());
				
			// get alarm data
			List<Alarm> alarms = alarmDao.getAlarmsByAreaIds(areaIds, new Integer[]{Alarm.STATUS_NEW, Alarm.STATUS_ALARMED, Alarm.STATUS_NOTIFY, Alarm.STATUS_MUTE, Alarm.STATUS_MUTED}, start, end); 
			
			if(alarms != null && alarms.size() > 0){
				Map<Long, Alarm> alarmMaps = new HashMap<Long, Alarm>();
				Set<Long> alarmIds = new HashSet<Long>();
				for(Alarm alarm : alarms){
					alarmIds.add(alarm.getId());
					alarmMaps.put(alarm.getId(), alarm);
				}

				List<AlarmHistory> historys = alarmDao.getAlarmHistorysByAlarmIds(alarmIds);
				
				if(historys != null && historys.size() > 0){
					for(AlarmHistory history : historys)
						alarmMaps.get(history.getAlarmId()).getHistory().add(history);
				}
			}
			
			return alarms;
		}
		
		return null;
	}

	@Override
	public int getAreaAlarmCount(Area area){
		if(area != null){
			// get sub-area ids
			Map<Integer, Area> areaMaps = new HashMap<Integer, Area>();
			areaMaps.put(area.getId(), area);
			getAreaMaps(area.getChildren(), areaMaps);
			
			List<Integer> areaIds = new ArrayList<Integer>(areaMaps.keySet());
				
			// get alarm data
			return alarmDao.getAlarmCountByAreaIds(areaIds, new Integer[]{Alarm.STATUS_NEW, Alarm.STATUS_ALARMED, Alarm.STATUS_NOTIFY, Alarm.STATUS_MUTE, Alarm.STATUS_MUTED}); 
		}
		
		return 0;
	}

	@Override
	public AreaMonitorData getAreaData(Area area, long time) {
		if(area != null){
			// get sub-area ids
			Map<Integer, Area> areaMaps = new HashMap<Integer, Area>();
			areaMaps.put(area.getId(), area);
			getAreaMaps(area.getChildren(), areaMaps);
			
			List<Integer> areaIds = new ArrayList<Integer>(areaMaps.keySet());
			
			AreaMonitorData data = new AreaMonitorData();
			
			// get tem
			getAreasLatestTem(time, areaIds, data);
			
			if(area.getParent() <= 0 || (areaIds.size() == 1 && areaIds.get(0) == area.getId())){
				Date date = null;
				if(time > 0)
					date = new Date(time);
				else
					date = new Date();
				
				// get alarm data
				List<Alarm> alarms = alarmDao.getAlarms(areaIds, date); 
				
				if(alarms != null && alarms.size() > 0){
					// get alarmIdx
					// get alarmIds
					if(area.getParent() <= 0){
						Map<Integer, Long> tmpMaps = new HashMap<Integer, Long>();
						for(Alarm alarm : alarms){
							Area tmp = areaMaps.get(alarm.getAreaId());
							if(tmp != null)
								tmpMaps.put(tmp.getIndex(), new Long(tmp.getId()));
						}
						
						for(Integer key : tmpMaps.keySet()){
							data.getAlarmIdx().add(key);
							data.getAlarmIds().add(tmpMaps.get(key));
						}
					} else{
						data.setAlarmName(alarms.get(0).getAlarmName());
						data.setAlarmType(alarms.get(0).getType());
					}
				}
				
				return data;
			}
		}
		
		return null;
	}

	public void getAreasLatestTem(long time, List<Integer> areaIds, AreaMonitorData data) {
		// get area channel
		List<AreaChannel> areaChannels = areaDao.getAreaChannelsByAreaids(areaIds);
		if(areaChannels != null && areaChannels.size() > 0){
			// get channel_id, start, end
			Map<Integer, List<AreaChannel>> acMap = new HashMap<Integer, List<AreaChannel>>();
			for(AreaChannel areaChannel : areaChannels){
				List<AreaChannel> tmp = acMap.get(areaChannel.getChannelid());
				if(tmp == null){
					tmp = new ArrayList<AreaChannel>();
					acMap.put(areaChannel.getChannelid(), tmp);
				}
				tmp.add(areaChannel);
			}
			
			Date date = null;
			if(time > 0)
				date = new Date();
			
			// get temperature
			List<Temperature> tems = temDao.getLatestTemsByChannel(new ArrayList<Integer>(acMap.keySet()), date);
			
			if(tems != null && tems.size() > 0){
				double max = 0;
				double min = Double.MAX_VALUE;
				double avg = 0;
				int count = 0;
				long latest = 0;
				
				for(Temperature tem : tems){
					if(tem.getDate() != null){
						long tmpTime = tem.getDate().getTime();
						if(tmpTime > latest)
							latest = tmpTime;
					}
					
					int tmpChannel = tem.getChannel();
					String[] tmpTem = StringUtils.split(tem.getTem(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					
					List<AreaChannel> tmp = acMap.get(tmpChannel);
					if(tmp != null && tmp.size() > 0)
						for(AreaChannel channel : tmp){
							int start = channel.getStart();
							int end = channel.getEnd();
							
							int i = start - 1;
							if(i < 0)
								i = 0;
							
							for(; i < tmpTem.length && i < end; i ++){
								double tmpDouble = NumberUtils.toDouble(tmpTem[i], 0);
								avg += tmpDouble;
								count ++;
								
								if(tmpDouble > max)
									max = tmpDouble;
								if(tmpDouble < min)
									min = tmpDouble;
							}
						}
				}
				
				if(count > 0)
					avg = new BigDecimal(avg).divide(new BigDecimal(count), 5, RoundingMode.HALF_UP).doubleValue();
				
				data.setMax(max);
				data.setMin(min);
				data.setAvg(avg);
				data.setTime(latest);
			}
		}
	}

	private void getAreaMaps(List<Area> areas, Map<Integer, Area> areaMaps) {
		if(areas != null && areas.size() > 0)
			for(Area area : areas){
				areaMaps.put(area.getId(), area);
				getAreaMaps(area.getChildren(), areaMaps);
			}
	}

	public AreaDao getAreaDao() {
		return areaDao;
	}

	public void setAreaDao(AreaDao areaDao) {
		this.areaDao = areaDao;
	}

	public TemDao getTemDao() {
		return temDao;
	}

	public void setTemDao(TemDao temDao) {
		this.temDao = temDao;
	}

	public AlarmDao getAlarmDao() {
		return alarmDao;
	}

	public void setAlarmDao(AlarmDao alarmDao) {
		this.alarmDao = alarmDao;
	}

}
