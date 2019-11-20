package com.tol.sai.service;

import java.util.List;

import com.tol.sai.dto.SubwayVO;


public interface SubwayService {
	
	//���� �߽� ��ǥ ��ȯ �޼ҵ�
	public String[] searchCentricPoint(String people[]) throws Exception;
	
	//����ö �̸� -> WGS ��ǥ ��ȯ �޼ҵ�
	public List<SubwayVO> searchLocation(String stationName) throws Exception;
	
	public String[] calculateLocation(List<SubwayVO> subwayInfoList) throws Exception;

	public String[] searchTenSubway(String centricLocation[]) throws Exception;

	public String calculateLastSubway(String people[], String subwayList[]) throws Exception;

}
