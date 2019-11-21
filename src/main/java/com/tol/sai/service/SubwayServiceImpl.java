package com.tol.sai.service;

import java.awt.geom.Point2D;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;
 
import com.tol.sai.dao.SubwayDAO;
import com.tol.sai.dto.SubwayVO;

@Service
public class SubwayServiceImpl implements SubwayService{
	
	@Inject
	private SubwayDAO dao;

	@Override
	public List<SubwayVO> searchLocation(String stationName) throws Exception {
		// TODO Auto-generated method stub
		return dao.searchLocation(stationName);
	}
	

	@Override
	public Double[] calculateLocation(List<SubwayVO> subwayInfoList) throws Exception {
		// TODO Auto-generated method stub
		
		Double centric[] = new Double[2];
		
		Point2D.Double mCenterPoint = null;
		ArrayList<Point2D> mVertexs = new ArrayList<Point2D>();
		
		String nextLine = null;
		Double tmX = null;
		Double tmY = null;
		Double wgsX = null;
		Double wgsY = null;
		
		Iterator<SubwayVO> iterator = subwayInfoList.iterator();
		
		while(iterator.hasNext()) {
			
			SubwayVO subwayInfo = iterator.next();
			
			System.out.println("#subwayInfo value :: " + subwayInfo);
			Double mapX = subwayInfo.getWgsX();
			Double mapY = subwayInfo.getWgsY();
			
			mVertexs.add(new Point2D.Double(mapX, mapY));
		}

		double centerX = 0.0, centerY = 0.0, area = 0.0, factor = 0.0;

		mCenterPoint = new Point2D.Double(0.0, 0.0);
		
		int firstIndex, secondIndex, sizeOfVertexs = mVertexs.size();
		Point2D.Double  firstPoint, secondPoint;

		for (firstIndex = 0; firstIndex < sizeOfVertexs; firstIndex++) {
			secondIndex = (firstIndex + 1) % sizeOfVertexs;

			firstPoint  = (java.awt.geom.Point2D.Double) mVertexs.get(firstIndex);
			secondPoint = (java.awt.geom.Point2D.Double) mVertexs.get(secondIndex);

			factor = ((firstPoint.getX() * secondPoint.getY()) - (secondPoint.getX() * firstPoint.getY()));

			area += factor;

			centerX += (firstPoint.getX() + secondPoint.getX()) * factor;
			centerY += (firstPoint.getY() + secondPoint.getY()) * factor;
		}

		area /= 2.0;
		area *= 6.0f;

		factor = 1 / area;

		centerX *= factor;
		centerY *= factor;

		mCenterPoint.setLocation(centerX, centerY);
		System.out.println("#---centerXY : " + mCenterPoint.toString());
		System.out.print("#----area : " + area);
		
		wgsX = mCenterPoint.getX();
		wgsY = mCenterPoint.getY();

		URL locURL = new URL("https://dapi.kakao.com/v2/local/geo/transcoord.json?x=" +  mCenterPoint.getX() + "&y=" + mCenterPoint.getY() + "&input_coord=WGS84&output_coord=TM");
		HttpsURLConnection urlConn;

		urlConn = (HttpsURLConnection) locURL.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setAllowUserInteraction(false); // no user interaction
		urlConn.setDoOutput(true); // want to send
		urlConn.setRequestProperty( "Content-type", "text/xml" );
		urlConn.setRequestProperty( "accept", "text/xml" );
		urlConn.setRequestProperty( "Authorization", "KakaoAK 637a12488992c575ed97c918c0eee5e3");
		Map headerFields = urlConn.getHeaderFields();
		
		System.out.println("#---KaKao TMº¯È¯ -- header fields are: " + headerFields);

		int rspCode = urlConn.getResponseCode();
		System.out.println("#---KaKao TM responseCODE : " + rspCode);
		if (rspCode == 200) {
			InputStream ist = urlConn.getInputStream();
			InputStreamReader isr = new InputStreamReader(ist);
			BufferedReader br = new BufferedReader(isr);

			nextLine = br.readLine();
		}
		
		System.out.println("nextLine :" + nextLine);

		jsonObject = (JSONObject) jsonParser.parse(nextLine);
		JSONArray documents = (JSONArray)jsonObject.get("documents");
			
		for(int j = 0; j < documents.size(); j++) {
			JSONObject list = (JSONObject) documents.get(j);
			tmX = (Double) list.get("x");
			tmY = (Double) list.get("y");

			System.out.println("tmX : " + tmX);
			System.out.println("tmY : " + tmY);
		}	
		
		centric[0] = tmX;
		centric[1] = tmY;

		return centric;
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
