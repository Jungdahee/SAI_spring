package com.tol.sai.service;

import java.util.List;

import com.tol.sai.dto.SubwayVO;


public interface SubwayService {
	
	//지하철 이름 -> WGS 좌표 반환 메소드
	public List<SubwayVO> searchLocation(String stationName) throws Exception;
	
	public Double[] calculateLocation(List<SubwayVO> subwayInfoList) throws Exception;

	public String[] searchTenSubway(String centricLocation[]) throws Exception;

	public String calculateLastSubway(String people[], String subwayList[]) throws Exception;

}
