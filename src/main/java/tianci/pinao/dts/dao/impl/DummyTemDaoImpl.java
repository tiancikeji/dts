package tianci.pinao.dts.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import tianci.pinao.dts.dao.TemDao;
import tianci.pinao.dts.models.Temperature;

public class DummyTemDaoImpl implements TemDao {

	@Override
	public List<Temperature> getLatestTemsByChannel(List<Integer> channels, Date time) {
		List<Temperature> result = new ArrayList<Temperature>();
		for(Integer key : channels){
			Temperature tmp = new Temperature();
			tmp.setChannel(key);
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < 1000; i ++)
				sb.append((20 + random.nextInt(10)) + ".6,");
			tmp.setTem(sb.toString());
			tmp.setDate(new Date());
			result.add(tmp);
		}
		
		return result;
	}

	@Override
	public List<Temperature> getTemsByChannel(ArrayList<Integer> channels, Date start, Date end) {
		List<Temperature> result = new ArrayList<Temperature>();
		for(Integer key : channels){
			Random random = new Random();
			Date date = new Date(start.getTime() + 1000);
			for(int i = 0; i < 10; i ++){
				Temperature tmp = new Temperature();
				tmp.setChannel(key);
				StringBuilder sb = new StringBuilder();
				for(int j = 0; j < 1000; j ++)
					sb.append((20 + random.nextInt(10)) + ".6,");
				tmp.setTem(sb.toString());
				sb = new StringBuilder();
				for(int j = 0; j < 1000; j ++)
					sb.append((20 + random.nextInt(10)) + ".6,");
				tmp.setStock(sb.toString());
				sb = new StringBuilder();
				for(int j = 0; j < 1000; j ++)
					sb.append((20 + random.nextInt(10)) + ".6,");
				tmp.setUnstock(sb.toString());
				tmp.setDate(date);
				result.add(tmp);
			}
		}
		
		return result;
	}

	@Override
	public List<Temperature> getLatestTemNStocksByChannel(List<Integer> channels, Date time) {
		List<Temperature> result = new ArrayList<Temperature>();
		for(Integer key : channels){
			Temperature tmp = new Temperature();
			tmp.setChannel(key);
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < 1000; i ++)
				sb.append((20 + random.nextInt(10)) + ".6,");
			tmp.setTem(sb.toString());
			sb = new StringBuilder();
			for(int i = 0; i < 1000; i ++)
				sb.append((20 + random.nextInt(10)) + ".6,");
			tmp.setStock(sb.toString());
			sb = new StringBuilder();
			for(int i = 0; i < 1000; i ++)
				sb.append((20 + random.nextInt(10)) + ".6,");
			tmp.setUnstock(sb.toString());
			tmp.setDate(new Date());
			result.add(tmp);
		}
		
		return result;
	}

	@Override
	public List<Temperature> getTemNStocksByChannel(List<Integer> channels, Date start, Date end) {
		List<Temperature> result = new ArrayList<Temperature>();
		for(Integer key : channels){
			Random random = new Random();
			Date date = new Date(start.getTime() + 1000);
			for(int j = 0; j < 10; j ++){
				Temperature tmp = new Temperature();
				tmp.setChannel(key);
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < 1000; i ++)
					sb.append((20 + random.nextInt(10)) + ".6,");
				tmp.setTem(sb.toString());
				sb = new StringBuilder();
				for(int i = 0; i < 1000; i ++)
					sb.append((20 + random.nextInt(10)) + ".6,");
				tmp.setStock(sb.toString());
				sb = new StringBuilder();
				for(int i = 0; i < 1000; i ++)
					sb.append((20 + random.nextInt(10)) + ".6,");
				tmp.setUnstock(sb.toString());
				tmp.setDate(date);
				result.add(tmp);
			}
		}
		
		return result;
	}
}

