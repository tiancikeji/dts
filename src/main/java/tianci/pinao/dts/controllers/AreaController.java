package tianci.pinao.dts.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaHardwareConfig;
import tianci.pinao.dts.models.AreaTempConfig;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.LevelImage;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.models.User;
import tianci.pinao.dts.service.AreaService;
import tianci.pinao.dts.util.PinaoConstants;
import tianci.pinao.dts.util.PinaoUtils;

@Controller
public class AreaController {
	
	@Autowired
	private AreaService areaService;

	@RequestMapping(value="/file/addFile", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addFile(HttpServletRequest request, String data){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					String name = areaService.addFile(request.getSession().getServletContext().getRealPath("/"), data);
					if(StringUtils.isNotBlank(name)){
						result.put("status", "0");
						result.put("name", /*request.getContextPath() + */name);
					} else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/level/addLevel", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addLevel(HttpServletRequest request, LevelImage level, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.addLevelImage(level, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/level/modifyLevel", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> modifyLevel(HttpServletRequest request, LevelImage level, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.modifyLevelImage(level, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/level/deleteLevel", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> deleteLevel(HttpServletRequest request, LevelImage level, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.deleteLevelImage(level, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/level/levels", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getLevels(HttpServletRequest request){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					List<LevelImage> levels = areaService.getAllLevels();
					result.put("data", parseLevelImages(levels));
					result.put("status", "0");		
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private List<Map<String, Object>> parseLevelImages(List<LevelImage> levels) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(levels != null && levels.size() > 0)
			for(LevelImage level : levels){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", level.getId());
				tmp.put("name", level.getName());
				tmp.put("image", level.getImage());
				
				data.add(tmp);
			}
		
		return data;
	}

	@RequestMapping(value="/area/addArea", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addArea(HttpServletRequest request, Area area, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.addArea(area, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/updateArea", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> updateArea(HttpServletRequest request, Area area, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					if(area.getParent() == area.getId())
						area.setParent(0);
							boolean success = areaService.updateArea(area, new Long(userid).intValue());
							if(success)
								result.put("status", "0");
							else
								result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/deleteArea", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> deleteArea(HttpServletRequest request, Area area, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.deleteArea(area, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/areanames", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaNames(HttpServletRequest request){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					List<Area> areas = areaService.getAllAailableAreas();
					List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
					if(areas != null && areas.size() > 0)
						for(Area area : areas){
							Map<String, Object> tmp = new HashMap<String, Object>();
							tmp.put("id", area.getId());
							tmp.put("name", area.getName());
							data.add(tmp);
						}
					result.put("data", data);
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/areas", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreas(HttpServletRequest request){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				List<Area> areas = areaService.getAllAailableAreas();
				result.put("data", parseAreas(areas));
				result.put("status", "0");
			} else
				result.put("status", "500");
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
		}
		return result;
	}
	
	@RequestMapping(value="/area/replace", method = RequestMethod.POST)
	public Map<Object, Object> replaceAreas(HttpServletRequest request, String data, long userid) throws IOException{
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.replaceAreas(data, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}
	
	@RequestMapping(value="/area/download", method = RequestMethod.GET)
	public void downloadAllAreas(HttpServletRequest request, HttpServletResponse response) throws IOException{
		OutputStream out = null;
		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					response.setContentType("text/plain");
					String oriFileName = "厂区.txt";
					String agent = request.getHeader("USER-AGENT");
					if (null != agent && -1 != agent.indexOf("Firefox")) {
						response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("utf-8"),"iso-8859-1"));
					} else {
						response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
					}
					out = response.getOutputStream();
					out.write((PinaoConstants.FILE_COMMENT_PREFIX + "名称" + PinaoConstants.TEM_DATA_COL_SEP + "分类" + PinaoConstants.TEM_DATA_COL_SEP + "报警区域" + PinaoConstants.TEM_DATA_COL_SEP + "父级厂区" + PinaoConstants.TEM_DATA_COL_SEP + "图片" + PinaoConstants.TEM_DATA_COL_SEP + "灯号" + PinaoConstants.TEM_DATA_COL_SEP + "继电器号" + PinaoConstants.TEM_DATA_COL_SEP + "声音地址" + PinaoConstants.TEM_DATA_COL_SEP + "低温报警" + PinaoConstants.TEM_DATA_COL_SEP + "高温报警" + PinaoConstants.TEM_DATA_COL_SEP + "差温报警" + PinaoConstants.TEM_DATA_COL_SEP + "温升报警" + PinaoConstants.TEM_DATA_COL_SEP + "报警显示名称" + PinaoConstants.TEM_DATA_COL_SEP + "通道名称" + PinaoConstants.TEM_DATA_COL_SEP + "机器名称" + PinaoConstants.TEM_DATA_COL_SEP + "开始距离" + PinaoConstants.TEM_DATA_COL_SEP + "结束距离" + PinaoConstants.TEM_DATA_LINE_SEP).getBytes());
					
					Map<Integer, AreaHardwareConfig> hMap = new HashMap<Integer, AreaHardwareConfig>();
					List<AreaHardwareConfig> hardConfigs = areaService.getAllHardwareConfigs();
					if(hardConfigs != null && hardConfigs.size() > 0)
						for(AreaHardwareConfig config : hardConfigs)
							hMap.put(config.getAreaid(), config);
					
					Map<Integer, AreaChannel> acMap = new HashMap<Integer, AreaChannel>();
					List<AreaChannel> areaChannels = areaService.getAllAreaChannels();
					if(areaChannels != null && areaChannels.size() > 0)
						for(AreaChannel ac : areaChannels)
							acMap.put(ac.getAreaid(), ac);
					
					Map<Integer, AreaTempConfig> tMap = new HashMap<Integer, AreaTempConfig>();
					List<AreaTempConfig> areaTempConfigs = areaService.getAllTempConfigs();
					if(areaTempConfigs != null && areaTempConfigs.size() > 0)
						for(AreaTempConfig config : areaTempConfigs)
							tMap.put(config.getAreaid(), config);
					
					List<Area> areas = areaService.getAllAailableAreas();
					if(areas != null && areas.size() > 0){
						Map<Integer, Area> aMap = new HashMap<Integer, Area>();
						for(Area area : areas)
							aMap.put(area.getId(), area);
		
						for(Area area : areas){
							Area parent = aMap.get(area.getParent());
							AreaTempConfig temp = tMap.get(area.getId());
							AreaHardwareConfig hc = hMap.get(area.getId());
							AreaChannel ac = acMap.get(area.getId());
							
							out.write((area.getName() + PinaoConstants.TEM_DATA_COL_SEP + area.getLevel() + PinaoConstants.TEM_DATA_COL_SEP + area.getIndex() + PinaoConstants.TEM_DATA_COL_SEP + (parent != null ? parent.getName() : StringUtils.EMPTY) + PinaoConstants.TEM_DATA_COL_SEP + StringUtils.trimToEmpty(area.getImage()) + PinaoConstants.TEM_DATA_COL_SEP).getBytes());
							
							if(hc != null)
								out.write((hc.getLight() + PinaoConstants.TEM_DATA_COL_SEP + hc.getRelay() + PinaoConstants.TEM_DATA_COL_SEP + hc.getVoice() + PinaoConstants.TEM_DATA_COL_SEP).getBytes());
							else
								out.write((PinaoConstants.TEM_DATA_COL_SEP + PinaoConstants.TEM_DATA_COL_SEP + PinaoConstants.TEM_DATA_COL_SEP).getBytes());
							if(temp != null)
								out.write((temp.getTemperatureLow() + PinaoConstants.TEM_DATA_COL_SEP + temp.getTemperatureHigh() + PinaoConstants.TEM_DATA_COL_SEP + temp.getTemperatureDiff() + PinaoConstants.TEM_DATA_COL_SEP + temp.getExotherm()).getBytes());
							else
								out.write((PinaoConstants.TEM_DATA_COL_SEP + PinaoConstants.TEM_DATA_COL_SEP + PinaoConstants.TEM_DATA_COL_SEP + PinaoConstants.TEM_DATA_COL_SEP).getBytes());
							if(ac != null)
								out.write((ac.getName() + "	" + ac.getChannelName() + PinaoConstants.TEM_DATA_COL_SEP + ac.getMachineName() + PinaoConstants.TEM_DATA_COL_SEP + ac.getStart() + PinaoConstants.TEM_DATA_COL_SEP + ac.getEnd()).getBytes());
							else
								out.write((PinaoConstants.TEM_DATA_COL_SEP + PinaoConstants.TEM_DATA_COL_SEP + PinaoConstants.TEM_DATA_COL_SEP + PinaoConstants.TEM_DATA_COL_SEP).getBytes());
							
							out.write((PinaoConstants.TEM_DATA_LINE_SEP).getBytes());
						}
					}
				}
			} else
				response.sendRedirect(request.getContextPath() + "/" + "error.html");
		} catch(Throwable t){
			t.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/" + "error.html");
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				// 文件已下载完毕，不需要处理
			}
		}
	}

	@RequestMapping(value="/area/addhwconfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addAreaHardwareConfig(HttpServletRequest request, AreaHardwareConfig config, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.addHardwareConfig(config, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/updatehwconfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> updateAreaHardwareConfig(HttpServletRequest request, AreaHardwareConfig config, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() < 3){
					boolean success = areaService.updateHardwareConfig(config, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/deletehwconfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> deleteAreaHardwareConfig(HttpServletRequest request, AreaHardwareConfig config, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.deleteHardwareConfig(config, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/hwconfigs", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaHardwareConfigs(HttpServletRequest request){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() < 3){
					List<AreaHardwareConfig> configs = areaService.getAllHardwareConfigs();
					result.put("data", parseHardwareConfigs(configs));
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Object parseHardwareConfigs(List<AreaHardwareConfig> configs) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(configs != null && configs.size() > 0)
			for(AreaHardwareConfig config : configs){
				Map<String, Object> tmp = new HashMap<String, Object>();
				tmp.put("id", config.getId());
				tmp.put("areaid", config.getAreaid());
				tmp.put("areaname", config.getAreaName());
				tmp.put("light", config.getLight());
				tmp.put("relay", config.getRelay());
				tmp.put("voice", config.getVoice());
				result.add(tmp);
			}
		
		return result;
	}

	@RequestMapping(value="/area/addTempconfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addAreaTempConfig(HttpServletRequest request, AreaTempConfig config, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			 userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.addAreaTempConfig(config, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/updateTempconfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> updateAreaTempConfig(HttpServletRequest request, AreaTempConfig config, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.updateAreaTempConfig(config, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/deleteTempconfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> deleteAreaTempConfig(HttpServletRequest request, AreaTempConfig config, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.deleteAreaTempConfig(config, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/tempconfigs", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaTempConfigs(HttpServletRequest request){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					List<AreaTempConfig> configs = areaService.getAllTempConfigs();
					result.put("data", parseTempConfigs(configs));
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Object parseTempConfigs(List<AreaTempConfig> configs) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(configs != null && configs.size() > 0)
			for(AreaTempConfig config : configs){
				Map<String, Object> tmp = new HashMap<String, Object>();
				tmp.put("id", config.getId());
				tmp.put("areaid", config.getAreaid());
				tmp.put("areaname", config.getAreaName());
				tmp.put("low", config.getTemperatureLow());
				tmp.put("high", config.getTemperatureHigh());
				tmp.put("exotherm", config.getExotherm());
				tmp.put("diff", config.getTemperatureDiff());
				result.add(tmp);
			}
		
		return result;
	}

	@RequestMapping(value="/area/addchannel", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addAreaChannel(HttpServletRequest request, AreaChannel channel, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					List<AreaChannel> channels = new ArrayList<AreaChannel>();
					channels.add(channel);
					boolean success = areaService.addAreaChannel(channels, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/updatechannel", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> updateAreaChannel(HttpServletRequest request, AreaChannel channel, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.updateAreaChannel(channel, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/deletechannel", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> deleteeAreaChannel(HttpServletRequest request, AreaChannel channel, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.deleteAreaChannel(channel, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/area/channels", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaChannels(HttpServletRequest request){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					List<AreaChannel> channels = areaService.getAllAreaChannels();
					result.put("data", parseAreaChannels(channels));
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Object parseAreaChannels(List<AreaChannel> channels) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(channels != null && channels.size() > 0)
			for(AreaChannel channel : channels){
				Map<String, Object> tmp = new HashMap<String, Object>();
				tmp.put("id", channel.getId());
				tmp.put("name", channel.getName());
				tmp.put("areaid", channel.getAreaid());
				tmp.put("areaname", channel.getAreaName());
				tmp.put("channelid", channel.getChannelid());
				tmp.put("channelname", channel.getChannelName());
				tmp.put("machineid", channel.getMachineid());
				tmp.put("machinename", channel.getMachineName());
				tmp.put("start", channel.getStart());
				tmp.put("end", channel.getEnd());
				result.add(tmp);
			}
		
		return result;
	}

	@RequestMapping(value="/channel/addchannel", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addChannel(HttpServletRequest request, Channel channel, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success= areaService.addChannel(channel, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/channel/updatechannel", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> updateChannel(HttpServletRequest request, Channel channel, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success= areaService.updateChannel(channel, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/channel/deletechannel", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> deleteChannel(HttpServletRequest request, Channel channel, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success= areaService.deleteChannel(channel, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/channel/channels", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAllChannels(HttpServletRequest request){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					List<Channel> channels = areaService.getAllAvailableChannels();
					result.put("data", parseChannels(channels));
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Object parseChannels(List<Channel> channels) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(channels != null && channels.size() > 0)
			for(Channel channel : channels){
				Map<String, Object> tmp = new HashMap<String, Object>();
				tmp.put("id", channel.getId());
				tmp.put("name", channel.getName());
				tmp.put("machineid", channel.getMachineid());
				tmp.put("machinename", channel.getMachineName());
				tmp.put("length", channel.getLength());
				result.add(tmp);
			}
		
		return result;
	}

	@RequestMapping(value="/channel/replace", method = RequestMethod.POST)
	public Map<Object, Object> replaceChannels(HttpServletRequest request, String data, long userid) throws IOException{
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, 0);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.replaceChannels(data, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}
	
	@RequestMapping(value="/channel/download", method = RequestMethod.GET)
	public void downloadAllChannels(HttpServletRequest request, HttpServletResponse response) throws IOException{
		OutputStream out = null;
		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					response.setContentType("text/plain");
					String oriFileName = "通道.txt";
					String agent = request.getHeader("USER-AGENT");
					if (null != agent && -1 != agent.indexOf("Firefox")) {
						response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("utf-8"),"iso-8859-1"));
					} else {
						response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
					}
					out = response.getOutputStream();
					out.write((PinaoConstants.FILE_COMMENT_PREFIX + "通道id" + PinaoConstants.TEM_DATA_COL_SEP + "机器ID" + PinaoConstants.TEM_DATA_COL_SEP + "通道长度" + PinaoConstants.TEM_DATA_LINE_SEP).getBytes());
					
					List<Channel> channels = areaService.getAllAvailableChannels();
					if(channels != null && channels.size() > 0)
						for(Channel channel : channels)
							out.write((channel.getName() + PinaoConstants.TEM_DATA_COL_SEP + channel.getMachineName() + PinaoConstants.TEM_DATA_COL_SEP + channel.getLength() + PinaoConstants.TEM_DATA_LINE_SEP).getBytes());
				}
			} else
				response.sendRedirect(request.getContextPath() + "/" + "error.html");
		} catch(Throwable t){
			t.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/" + "error.html");
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				// 文件已下载完毕，不需要处理
			}
		}
	}

	@RequestMapping(value="/machine/addmachine", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addMachine(HttpServletRequest request, Machine machine, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.addMachine(machine, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/machine/updatemachine", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> updateMachine(HttpServletRequest request, Machine machine, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.updateMachine(machine, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/machine/deletemachine", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> deleteMachine(HttpServletRequest request, Machine machine, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.deleteMachine(machine, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/machine/machines", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAllMachines(HttpServletRequest request){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					List<Machine> machines = areaService.getAllMachines();
					result.put("data", parseMachines(machines));
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Object parseMachines(List<Machine> machines) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(machines != null && machines.size() > 0)
			for(Machine machine : machines){
				Map<String, Object> tmp = new HashMap<String, Object>();
				tmp.put("id", machine.getId());
				tmp.put("name", machine.getName());
				tmp.put("serialport", machine.getSerialPort());
				tmp.put("baudrate", machine.getBaudRate());
				result.add(tmp);
			}
		
		return result;
	}

	@RequestMapping(value="/machine/replace", method = RequestMethod.POST)
	public Map<Object, Object> replaceMachines(HttpServletRequest request, String data, long userid) throws IOException{
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = areaService.replaceMachines(data, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/machine/download", method = RequestMethod.GET)
	public void downloadAllMachines(HttpServletRequest request, HttpServletResponse response) throws IOException{
		OutputStream out = null;
		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					response.setContentType("text/plain");
					String oriFileName = "机器.txt";
					String agent = request.getHeader("USER-AGENT");
					if (null != agent && -1 != agent.indexOf("Firefox")) {
						response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("utf-8"),"iso-8859-1"));
					} else {
						response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
					}
					out = response.getOutputStream();
					out.write((PinaoConstants.FILE_COMMENT_PREFIX + "机器id" + PinaoConstants.TEM_DATA_COL_SEP + "光开关" + PinaoConstants.TEM_DATA_COL_SEP + "串口" + PinaoConstants.TEM_DATA_LINE_SEP).getBytes());
					
					List<Machine> machines = areaService.getAllMachines();
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines)
							out.write((machine.getName() + PinaoConstants.TEM_DATA_COL_SEP + machine.getSerialPort() + PinaoConstants.TEM_DATA_COL_SEP + machine.getBaudRate() + PinaoConstants.TEM_DATA_LINE_SEP).getBytes());
				}
			} else
				response.sendRedirect(request.getContextPath() + "/" + "error.html");
		} catch(Throwable t){
			t.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/" + "error.html");
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				// 文件已下载完毕，不需要处理
			}
		}
	}
}
