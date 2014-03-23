package tianci.pinao.dts.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tianci.pinao.dts.models.User;
import tianci.pinao.dts.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> login(HttpServletRequest request, String name, String password){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			User user = userService.getUser(name, password);
			
			if(user != null && request != null){
				request.getSession().setAttribute("" + user.getId(), user);
				result.put("data", parseUser(user));
				result.put("status", "0");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/logout", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> logout(HttpServletRequest request, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			request.getSession().removeAttribute("" + userid);
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/user/new", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> addUser(long userid, String name, String password, int role, String areaIds){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// TODO check user role
			User user = new User();
			user.setName(name);
			user.setPassword(password);
			user.setRole(role);
			user.setLastModUserid(userid);
			String[] tmp = StringUtils.split(areaIds, ",");
			List<Integer> areaList = new ArrayList<Integer>();
			if(tmp != null && tmp.length > 0)
				for(String tt : tmp){
					int id = NumberUtils.toInt(tt, -1);
					if(id > 0)
						areaList.add(id);
				}
			
			user.setAreaIds(areaList);
			userService.addUser(user);
			
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/user/modify", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> modifyUser(long userid, long id, String name, String password, int role, String areaIds){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// TODO check user role
			User user = new User();
			user.setId(id);
			user.setName(name);
			user.setPassword(password);
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
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/user/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> delUser(long userid, long id){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// TODO check user role
			userService.delUser(id, userid);
			
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/user/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> getUsers(long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// TODO check user role
			int role = -1;
			List<User> users = userService.getUser(role);
			
			result.put("data", parseUsers(users));
			result.put("status", "0");
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
