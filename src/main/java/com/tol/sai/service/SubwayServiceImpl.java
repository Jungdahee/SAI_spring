package com.tol.sai.service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
 
import org.springframework.stereotype.Service;
 
import com.tol.sai.dao.SubwayDAO;
import com.tol.sai.dto.SubwayVO;

@Service
public class SubwayServiceImpl implements SubwayService{
	
	@Inject
	private SubwayDAO dao;
	
	@Override
	public String[] searchCentricPoint(String people[]) throws Exception {
		
		String centric[] = new String[2];
		
		Point2D.Double mCenterPoint = null;
		ArrayList<Point2D> mVertexs = new ArrayList<Point2D>();
		
		String nextLine = null;
		Double tmX = null;
		Double tmY = null;
		
		mVertexs.add(new Point2D.Double(mapX, mapY));
		
		
		// TODO Auto-generated method stub
		return centric;
	}

	@Override
	public List<SubwayVO> searchLocation(String stationName) throws Exception {
		// TODO Auto-generated method stub
		return dao.searchLocation(stationName);
	}
	

	@Override
	public String[] calculateLocation(List<SubwayVO> subwayInfoList) throws Exception {
		// TODO Auto-generated method stub
		
		return null;
	}
	

	@Override
	public String[] searchTenSubway(String[] centricLocation) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public String calculateLastSubway(String[] people, String[] subwayList) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
