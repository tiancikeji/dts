package tianci.pinao.dts.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaMonitorData;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.ChannelMonitorData;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.models.ReportData;
import tianci.pinao.dts.models.TemData;
import tianci.pinao.dts.service.AreaService;
import tianci.pinao.dts.service.TemService;
import tianci.pinao.dts.util.PinaoUtils;

@Controller
public class TemperatureController {
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private TemService temService;

	@RequestMapping(value="/report/area/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaReportData(long userid, int id, String start, String end){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// TODO check user role
			Area area = searchArea(areaService.getAllAreas(userid), id);

			Map<String, Object> tmp = null;
			Date startDate = PinaoUtils.getDate(start);
			Date endDate = PinaoUtils.getDate(end);
			if(area != null && startDate != null && endDate != null && !endDate.before(startDate))
				tmp = parseReportData(temService.getAreaReportData(area, startDate, endDate));
			else
				tmp = new HashMap<String, Object>();
			
			result.put("data", tmp);
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Map<String, Object> parseReportData(ReportData data) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(data != null){
			result.put("max", data.getMax());
			result.put("min", data.getMin());
			result.put("avg", data.getAvg());
			
			List<Map<String, Object>> tmp = new ArrayList<Map<String,Object>>();
			if(data.getTems() != null && data.getTems().size() > 0)
				for(String name : data.getTems().keySet()){
					Map<String, Object> _tmp = new HashMap<String, Object>();
					_tmp.put("name", name);
					
					Map<Date, Double> _tems = data.getTems().get(name);
					List<Map<String, Object>> _list = new ArrayList<Map<String,Object>>();
					if(_tems != null && _tems.size() > 0)
						for(Date key : sortDate(new ArrayList<Date>(_tems.keySet()))){
							Map<String, Object> _tmp2 = new HashMap<String, Object>();
							_tmp2.put("time", PinaoUtils.getDateString(key));
							_tmp2.put("temp", _tems.get(key));
							_list.add(_tmp2);
						}
					_tmp.put("data", _list);
					
					tmp.add(_tmp);
				}
			result.put("tems", tmp);
			
			tmp = new ArrayList<Map<String,Object>>();
			if(data.getStocks() != null && data.getStocks().size() > 0)
				for(String name : data.getStocks().keySet()){
					Map<String, Object> _tmp = new HashMap<String, Object>();
					_tmp.put("name", name);
					
					Map<Date, Double> _stocks = data.getStocks().get(name);
					List<Map<String, Object>> _list = new ArrayList<Map<String,Object>>();
					if(_stocks != null && _stocks.size() > 0)
						for(Date key : sortDate(new ArrayList<Date>(_stocks.keySet()))){
							Map<String, Object> _tmp2 = new HashMap<String, Object>();
							_tmp2.put("time", PinaoUtils.getDateString(key));
							_tmp2.put("stock", _stocks.get(key));
							_list.add(_tmp2);
						}
					_tmp.put("data", _list);
					
					tmp.add(_tmp);
				}
			result.put("stocks", tmp);
			
			tmp = new ArrayList<Map<String,Object>>();
			if(data.getUnstocks() != null && data.getUnstocks().size() > 0)
				for(String name : data.getUnstocks().keySet()){
					Map<String, Object> _tmp = new HashMap<String, Object>();
					_tmp.put("name", name);
					
					Map<Date, Double> _unstocks = data.getUnstocks().get(name);
					List<Map<String, Object>> _list = new ArrayList<Map<String,Object>>();
					if(_unstocks != null && _unstocks.size() > 0)
						for(Date key : sortDate(new ArrayList<Date>(_unstocks.keySet()))){
							Map<String, Object> _tmp2 = new HashMap<String, Object>();
							_tmp2.put("time", PinaoUtils.getDateString(key));
							_tmp2.put("unstock", _unstocks.get(key));
							_list.add(_tmp2);
						}
					_tmp.put("data", _list);
					
					tmp.add(_tmp);
				}
			result.put("unstocks", tmp);
		}
		
		return result;
	}

	private List<Date> sortDate(ArrayList<Date> dates) {
		if(dates != null && dates.size() > 0)
			Collections.sort(dates, new Comparator<Date>(){

				@Override
				public int compare(Date s1, Date s2) {
					return s1.before(s2) ? 0 : 1;
				}
				
			});
		return dates;
	}

	@RequestMapping(value="/monitor/area/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaData(long userid, int id, long time){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// TODO check user role
			Area area = searchArea(areaService.getAllAreas(userid), id);

			Map<String, Object> tmp = null;
			if(area != null)
				tmp = parseAreaData(temService.getAreaData(area, time));
			else
				tmp = new HashMap<String, Object>();
			
			result.put("data", tmp);
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}
	
	private Area searchArea(List<Area> areas, int id){
		if(areas != null && areas.size() > 0)
			for(Area area : areas)
				if(area.getId() == id)
					return area;
				else{
					Area tmp = searchArea(area.getChildren(), id);
					if(tmp != null)
						return tmp;
				}
		
		return null;
	}

	private Map<String, Object> parseAreaData(AreaMonitorData data) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(data != null){
			result.put("max", data.getMax());
			result.put("min", data.getMin());
			result.put("avg", data.getAvg());
			result.put("time", data.getTime());
			if(data.getAlarmIds() != null && data.getAlarmIdx() != null){
				result.put("alarmIdxs", data.getAlarmIdx());
				result.put("alarmIds", data.getAlarmIds());
			}
			if(data.getAlarmName() != null && data.getAlarmType() > -1){
				result.put("alarmName", data.getAlarmName());
				result.put("alarmType", data.getAlarmType());
			}
		}
		
		return result;
	}

	@RequestMapping(value="/report/areas", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getReportAreas(long userid){
		return getAreas(userid);
	}

	@RequestMapping(value="/monitor/areas", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreas(long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			List<Area> areas = areaService.getAllAreas(userid);
			result.put("data", parseAreas(areas));
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Object parseAreas(List<Area> areas) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if(areas != null && areas.size() > 0){
			for(Area area : areas)
				result.add(parseArea(area));
		}
		return result;
	}

	private Map<String, Object> parseArea(Area area) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(area != null){
			result.put("id", area.getId());
			result.put("name", area.getName());
			result.put("level", area.getLevel());
			result.put("index", area.getIndex());
			result.put("image", area.getImage());
			result.put("parent", area.getParent());
			
			result.put("children", area.getChildren());
		}
		return result;
	}

	@RequestMapping(value="/report/channel/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getChannelReportData(long userid, int id, String start, String end){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			//TODO check user role
			Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
			List<Channel> channels = new ArrayList<Channel>();
			if(machines != null && machines.size() > 0)
				for(Machine machine : machines.keySet())
					for(Channel channel : machines.get(machine))
						if(channel.getId() == id){
							channels.add(channel);
							break;
						}
				
			Date startDate = PinaoUtils.getDate(start);
			Date endDate = PinaoUtils.getDate(end);
			Map<String, Object> data = null;
			if(channels != null && channels.size() > 0)
				data = parseReportData(temService.getChannelReportlData(channels, startDate, endDate));
			else
				data = new HashMap<String, Object>();
			result.put("data", data);
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/monitor/channel/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getChannelData(long userid, int id, long time){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			//TODO check user role
			Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
			List<Channel> channels = new ArrayList<Channel>();
			if(machines != null && machines.size() > 0)
				for(Machine machine : machines.keySet())
					for(Channel channel : machines.get(machine))
						if(channel.getId() == id){
							channels.add(channel);
							break;
						}
				
			Map<String, Object> data = null;
			if(channels != null && channels.size() > 0)
				data = parseChannelData(temService.getChannelData(channels, time));
			else
				data = new HashMap<String, Object>();
			result.put("data", data);
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/report/machine/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getMachineReportData(long userid, int id, String start, String end){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			//TODO check user role
			Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
			List<Channel> channels = null;
			if(machines != null && machines.size() > 0)
				for(Machine machine : machines.keySet())
					if(machine.getId() == id){
						channels = machines.get(machine);
						break;
					}
			
			Date startDate = PinaoUtils.getDate(start);
			Date endDate = PinaoUtils.getDate(end);
			Map<String, Object> data = null;
			if(channels != null && channels.size() > 0)
				data = parseReportData(temService.getChannelReportlData(channels, startDate, endDate));
			else
				data = new HashMap<String, Object>();
			result.put("data", data);
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/monitor/machine/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getMachineData(long userid, int id, long time){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			//TODO check user role
			Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
			List<Channel> channels = null;
			if(machines != null && machines.size() > 0)
				for(Machine machine : machines.keySet())
					if(machine.getId() == id){
						channels = machines.get(machine);
						break;
					}
				
			Map<String, Object> data = null;
			if(channels != null && channels.size() > 0)
				data = parseChannelData(temService.getChannelData(channels, time));
			else
				data = new HashMap<String, Object>();
			result.put("data", data);
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Map<String, Object> parseChannelData(ChannelMonitorData data) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(data != null){
			result.put("max", data.getMax());
			result.put("min", data.getMin());
			result.put("avg", data.getAvg());
			result.put("time", data.getTime());
			
			List<Map<String, Object>> tmp = new ArrayList<Map<String,Object>>();
			if(data.getTems() != null && data.getTems().size() > 0)
				for(TemData temData : data.getTems()){
					Map<String, Object> _tmp = new HashMap<String, Object>();
					_tmp.put("name", temData.getName());
					
					List<Map<String, Object>> _list = new ArrayList<Map<String,Object>>();
					if(temData.getData() != null && temData.getData().size() > 0)
						for(Integer key : sortLength(new ArrayList<Integer>(temData.getData().keySet()))){
							Map<String, Object> _tmp2 = new HashMap<String, Object>();
							_tmp2.put("length", key);
							_tmp2.put("temp", temData.getData().get(key));
							_list.add(_tmp2);
						}
					_tmp.put("data", _list);
					
					tmp.add(_tmp);
				}
			result.put("tems", tmp);
			
			tmp = new ArrayList<Map<String,Object>>();
			if(data.getStocks() != null && data.getStocks().size() > 0)
				for(TemData stockData : data.getStocks()){
					Map<String, Object> _tmp = new HashMap<String, Object>();
					_tmp.put("name", stockData.getName());
					
					List<Map<String, Object>> _list = new ArrayList<Map<String,Object>>();
					if(stockData.getData() != null && stockData.getData().size() > 0)
						for(Integer key : sortLength(new ArrayList<Integer>(stockData.getData().keySet()))){
							Map<String, Object> _tmp2 = new HashMap<String, Object>();
							_tmp2.put("length", key);
							_tmp2.put("stock", stockData.getData().get(key));
							_list.add(_tmp2);
						}
					_tmp.put("data", _list);
					
					tmp.add(_tmp);
				}
			result.put("stocks", tmp);
			
			tmp = new ArrayList<Map<String,Object>>();
			if(data.getUnstocks() != null && data.getUnstocks().size() > 0)
				for(TemData unstockData : data.getUnstocks()){
					Map<String, Object> _tmp = new HashMap<String, Object>();
					_tmp.put("name", unstockData.getName());
					
					List<Map<String, Object>> _list = new ArrayList<Map<String,Object>>();
					if(unstockData.getData() != null && unstockData.getData().size() > 0)
						for(Integer key : sortLength(new ArrayList<Integer>(unstockData.getData().keySet()))){
							Map<String, Object> _tmp2 = new HashMap<String, Object>();
							_tmp2.put("length", key);
							_tmp2.put("unstock", unstockData.getData().get(key));
							_list.add(_tmp2);
						}
					_tmp.put("data", _list);
					
					tmp.add(_tmp);
				}
			result.put("unstocks", tmp);
		}
		
		return result;
	}

	private List<Integer> sortLength(List<Integer> tmp) {
		if(tmp != null && tmp.size() > 0)
			Collections.sort(tmp, new Comparator<Integer>() {
	
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1 - o2;
				}
			});
		
		return tmp;
	}

	@RequestMapping(value="/report/channels", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getReportChannels(long userid){
		return getChannels(userid);
	}

	@RequestMapping(value="/monitor/channels", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getChannels(long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			Map<Machine, List<Channel>> channels = areaService.getAllChannels(userid);
			result.put("data", parseMachines(channels));
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private List<Map<String, Object>> parseMachines(Map<Machine, List<Channel>> channels) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(channels != null && channels.size() > 0)
			for(Machine machine : channels.keySet()){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", machine.getId());
				tmp.put("name", machine.getName());
				tmp.put("channels", parseChannels(channels.get(machine)));
				
				data.add(tmp);
			}
		
		return data;
	}

	private List<Map<String, Object>> parseChannels(List<Channel> channels) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(channels != null)
			for(Channel channel : channels){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", channel.getId());
				tmp.put("name", channel.getName());
				
				data.add(tmp);
			}
		
		return data;
	}

}