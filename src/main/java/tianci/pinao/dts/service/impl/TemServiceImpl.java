package tianci.pinao.dts.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import tianci.pinao.dts.dao.AreaDao;
import tianci.pinao.dts.dao.TemDao;
import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaMonitorData;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.ChannelMonitorData;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.models.ReportData;
import tianci.pinao.dts.models.TemData;
import tianci.pinao.dts.models.Temperature;
import tianci.pinao.dts.service.TemService;
import tianci.pinao.dts.util.PinaoConstants;

public class TemServiceImpl implements TemService {
	
	private AreaDao areaDao;
	
	private TemDao temDao;

	@Override
	public ChannelMonitorData getChannelData(List<Channel> channels, long time) {
		if(channels != null && channels.size() > 0){
			List<Integer> ids = new ArrayList<Integer>();
			Map<Integer, String> names = new HashMap<Integer, String>();
			for(Channel channel : channels){
				ids.add(channel.getId());
				names.put(channel.getId(), channel.getName());
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
				List<TemData> temData = new ArrayList<TemData>();
				List<TemData> stockData = new ArrayList<TemData>();
				List<TemData> unstockData = new ArrayList<TemData>();
				
				for(Temperature tem : tems){
					if(tem.getDate() != null){
						long tmpTime = tem.getDate().getTime();
						if(tmpTime > latest)
							latest = tmpTime;
					}

					int channel = tem.getChannel();
					TemData tmp = new TemData();
					tmp.setName(names.get(channel));
					Map<Integer, Double> tmpMap = new HashMap<Integer, Double>();
					tmp.setData(tmpMap);
					temData.add(tmp);
					
					String[] tmpTem = StringUtils.split(tem.getTem(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					for(int i = 0; i < tmpTem.length; i ++){
						double tmpDouble = NumberUtils.toDouble(tmpTem[i], 0);
						avg += tmpDouble;
						count ++;
						
						if(tmpDouble > max)
							max = tmpDouble;
						if(tmpDouble < min)
							min = tmpDouble;
						
						tmpMap.put(i + 1, tmpDouble);
					}
					
					tmp = new TemData();
					tmp.setName(names.get(channel));
					tmpMap = new HashMap<Integer, Double>();
					tmp.setData(tmpMap);
					stockData.add(tmp);
					
					String[] tmpStock = StringUtils.split(tem.getStock(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					for(int i = 0; i < tmpStock.length; i ++)
						tmpMap.put(i + 1, NumberUtils.toDouble(tmpStock[i], 0));
					
					tmp = new TemData();
					tmp.setName(names.get(channel));
					tmpMap = new HashMap<Integer, Double>();
					tmp.setData(tmpMap);
					unstockData.add(tmp);
					
					String[] tmpUnstock = StringUtils.split(tem.getUnstock(), PinaoConstants.TEM_DATA_ELEMENT_SEP);
					for(int i = 0; i < tmpUnstock.length; i ++)
						tmpMap.put(i + 1, NumberUtils.toDouble(tmpUnstock[i], 0));
				}
				
				if(count > 0)
					avg = new BigDecimal(avg).divide(new BigDecimal(count), 5, RoundingMode.HALF_UP).doubleValue();
				
				data.setMax(max);
				data.setMin(min);
				data.setAvg(avg);
				data.setTime(latest);
				data.setTems(temData);
				data.setStocks(stockData);
				data.setUnstocks(unstockData);
				
				return data;
			}
		}
		
		return null;
	}

	@Override
	public ReportData getChannelReportlData(List<Channel> channels, Date startDate, Date endDate) {
		if(channels != null && channels.size() > 0){
			List<Integer> ids = new ArrayList<Integer>();
			Map<Integer, String> names = new HashMap<Integer, String>();
			for(Channel channel : channels){
				ids.add(channel.getId());
				names.put(channel.getId(), channel.getName());
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
					for(int i = 0; i < tmpTem.length; i ++){
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
			List<Integer> areaIds = new ArrayList<Integer>();
			areaIds.add(area.getId());
			getAreaIds(area.getChildren(), areaIds);
			
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
					}
					
					List<Channel> channels = getAllAvailableChannels();
					if(channels != null && channels.size() > 0)
						for(Channel channel : channels)
							if(channelids.keySet().contains(channel.getId()))
								for(AreaChannel areachannel : channelids.get(channel.getId())){
									areachannel.setChannelName(channel.getName());
									areachannel.setMachineName(channel.getMachineName());
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
									
									String key = channel.getMachineName() + "-" + channel.getChannelName() + "-" + i;
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
									String key = channel.getMachineName() + "-" + channel.getChannelName() + "-" + i;
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
									String key = channel.getMachineName() + "-" + channel.getChannelName() + "-" + i;
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
	public AreaMonitorData getAreaData(Area area, long time) {
		if(area != null){
			// get sub-area ids
			List<Integer> areaIds = new ArrayList<Integer>();
			areaIds.add(area.getId());
			getAreaIds(area.getChildren(), areaIds);
			
			AreaMonitorData data = new AreaMonitorData();
			
			// get tem
			getAreasLatestTem(time, areaIds, data);
			
			if(data.getTime() > 0){
				// TODO get alarm data
				
				// get alarmIdx
				
				// get alarmName
				
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

	private void getAreaIds(List<Area> areas, List<Integer> areaIds) {
		if(areas != null && areas.size() > 0)
			for(Area area : areas){
				areaIds.add(area.getId());
				getAreaIds(area.getChildren(), areaIds);
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

}
