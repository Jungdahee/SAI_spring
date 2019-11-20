package com.tol.sai.service;

import java.util.List;

import com.tol.sai.dto.SubwayVO;


public interface SubwayService {
	
	//무게 중심 좌표 반환 메소드
	public String[] searchCentricPoint(String people[]) throws Exception;
	
	//지하철 이름 -> WGS 좌표 반환 메소드
	public List<SubwayVO> searchLocation(String stationName) throws Exception;
	
	public String[] calculateLocation(List<SubwayVO> subwayInfoList) throws Exception;

	public String[] searchTenSubway(String centricLocation[]) throws Exception;

	public String calculateLastSubway(String people[], String subwayList[]) throws Exception;

}
