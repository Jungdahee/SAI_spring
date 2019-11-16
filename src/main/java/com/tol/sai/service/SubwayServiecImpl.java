package com.tol.sai.service;

import java.util.List;

import javax.inject.Inject;
 
import org.springframework.stereotype.Service;
 
import com.tol.sai.dao.SubwayDAO;
import com.tol.sai.dto.SubwayVO;

@Service
public class SubwayServiecImpl implements SubwayService{
	
	@Inject
	private SubwayDAO dao;

	@Override
	public List<SubwayVO> searchLocation(String stationName) throws Exception {
		// TODO Auto-generated method stub
		return dao.searchLocation(stationName);
	}
	
}
