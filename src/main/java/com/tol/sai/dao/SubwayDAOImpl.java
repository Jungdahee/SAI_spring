package com.tol.sai.dao;

import java.util.List;

import javax.inject.Inject;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.tol.sai.dto.SubwayVO;

@Repository
public class SubwayDAOImpl implements SubwayDAO{
	@Inject
    private SqlSession sqlSession;
    
    private static final String Namespace = "com.tol.sai.mappers.subwayMapper";

	@Override
	public List<SubwayVO> searchLocation(String stationName) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList(Namespace + ".searchLocation", stationName);
	}

}
