package tianci.pinao.dts.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tianci.pinao.dts.models.Log;
import tianci.pinao.dts.models.User;
import tianci.pinao.dts.service.ConfigService;
import tianci.pinao.dts.service.LogService;
import tianci.pinao.dts.service.UserService;
import tianci.pinao.dts.util.PinaoUtils;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private ConfigService configService;

	@RequestMapping(value="/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> login(HttpServletRequest request, HttpServletResponse response, String name, String password){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			User user = userService.getUser(name, password);
			
			if(user != null && request != null){
				request.getSession().setAttribute("" + user.getId(), user);
				response.addCookie(new Cookie("userid", "" + user.getId()));
				result.put("data", parseUser(user));
				result.put("status", "0");
				
				Log log = new Log();
				log.setType(Log.TYPE_USER_LOGIN);
				log.setValue("" + user.getId());
				log.setSource(user.getName());
				logService.addLog(log);
			}
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/logout", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> logout(HttpServletRequest request, long userid, String password){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(StringUtils.equals(user.getPasswordLogout(), password)){
					request.getSession().removeAttribute("" + userid);
					result.put("status", "0");
					
					Log log = new Log();
					log.setType(Log.TYPE_USER_LOGOUT);
					log.setValue("" + user.getId());
					log.setSource(user.getName());
					logService.addLog(log);
				} else
					result.put("status", "700");
			}
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/user/new", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addUser(HttpServletRequest request, long userid, String name, String passwordLogin, String passwordReset, String passwordLogout, int role, String areaIds){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User admin = (User) obj;
				if(admin.getRole() < 4){
					if(role > admin.getRole()){
						if(configService.checkLifeTime() || admin.getRole() == 1){
							User user = new User();
							user.setName(name);
							user.setPasswordLogin(passwordLogin);
							user.setPasswordReset(passwordReset);
							user.setPasswordLogout(passwordLogout);
							user.setRole(role);
							user.setLastModUserid(userid);
							String[] tmp = StringUtils.split(areaIds, ",");
							List<Integer> areaList = new ArrayList<Integer>();
							if(tmp != null && tmp.length > 0)
								for(String tt : tmp){
									int _tmp = NumberUtils.toInt(tt, -1);
									if(_tmp > 0)
										areaList.add(_tmp);
								}
							
							user.setAreaIds(areaList);
							userService.addUser(user);
							
							result.put("status", "0");
						} else
							result.put("status", "1000");
					} else
						result.put("status", "600");
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

	@RequestMapping(value="/user/modify", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> modifyUser(HttpServletRequest request, long userid, long id, String name, String passwordLogin, String passwordReset, String passwordLogout, int role, String areaIds){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			result.put("status", "400");
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User admin = (User) obj;
				if(admin.getRole() < 4){
					User user = userService.getUserById(id);
					if(user != null && user.getRole() > admin.getRole() && role > admin.getRole()){
						if(configService.checkLifeTime() || admin.getRole() == 1){
							user.setId(id);
							user.setName(name);
							user.setPasswordLogin(passwordLogin);
							user.setPasswordReset(passwordReset);
							user.setPasswordLogout(passwordLogout);
							user.setRole(role);
							user.setLastModUserid(userid);
							String[] tmp = StringUtils.split(areaIds, ",");
							List<Integer> areaList = new ArrayList<Integer>();
							if(tmp != null && tmp.length > 0)
								for(String tt : tmp){
									int _tmp = NumberUtils.toInt(tt, -1);
									if(_tmp > 0)
										areaList.add(_tmp);
								}
							
							user.setAreaIds(areaList);
							userService.modifyUser(user);
							
							result.put("status", "0");
						} else
							result.put("status", "1000");
					} else
						result.put("status", "600");
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

	@RequestMapping(value="/user/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> delUser(HttpServletRequest request, long userid, long id){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			result.put("status", "400");
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() < 4){
					User tmp = userService.getUserById(id);
					if(tmp != null && tmp.getRole() > user.getRole()){
						if(configService.checkLifeTime() || user.getRole() == 1){
							userService.delUser(id, userid);
							result.put("status", "0");
						} else
							result.put("status", "1000");
					} else
						result.put("status", "600");
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

	@RequestMapping(value="/user/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getUsers(HttpServletRequest request, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			result.put("status", "400");
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() < 4){
					if(configService.checkLifeTime() || user.getRole() == 1){
						List<User> users = userService.getUser(user.getRole());
						
						result.put("data", parseUsers(users));
						result.put("status", "0");
					} else
						result.put("status", "1000");
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

	private List<Map<String, Object>> parseUsers(List<User> users) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(users != null && users.size() > 0)
			for(User user : users)
				data.add(parseUser(user));
		
		return data;
	}

	private Map<String, Object> parseUser(User user) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		if(user != null){
			data.put("userid", user.getId());
			data.put("name", user.getName());
			data.put("role", user.getRole());
			data.put("right", User.getRight(user.getRole()));
			data.put("areaIds", user.getAreaIds());
		}
		
		return data;
	}
}
