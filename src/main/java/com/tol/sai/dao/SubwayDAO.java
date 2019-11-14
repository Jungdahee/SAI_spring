package com.tol.sai.dao;

import java.util.*;

import com.tol.sai.dto.SubwayVO;

public interface SubwayDAO {
	
	public List<SubwayVO> searchLocation() throws Exception;

}
