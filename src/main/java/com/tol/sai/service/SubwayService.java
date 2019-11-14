package com.tol.sai.service;

import java.util.List;

import com.tol.sai.dto.SubwayVO;


public interface SubwayService {
	
	public List<SubwayVO> searchLocation() throws Exception;

}
