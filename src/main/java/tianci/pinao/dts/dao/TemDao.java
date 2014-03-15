package tianci.pinao.dts.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tianci.pinao.dts.models.Temperature;

public interface TemDao {

	public List<Temperature> getLatestTemsByChannel(List<Integer> channels, Date time);

	public List<Temperature> getTemsByChannel(ArrayList<Integer> channels, Date start, Date end);

	public List<Temperature> getLatestTemNStocksByChannel(List<Integer> channels, Date date);

	public List<Temperature> getTemNStocksByChannel(List<Integer> channels, Date startDate, Date endDate);

}
