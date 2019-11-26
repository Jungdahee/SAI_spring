package com.tol.sai.service;

import java.awt.geom.Point2D;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;

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
		
		System.out.println("#---KaKao TM변환 -- header fields are: " + headerFields);

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
		
		String subwayList[] = new String[10];
		
		String locURL = "http://swopenapi.seoul.go.kr/api/subway/5947735178616c77393747474d664c/xml/nearBy/0/10/" + mapX + "/" + mapY;

		DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
		Document doc = dBuilder.parse(locURL);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("row");

		for(int i = 0; i < nList.getLength(); i++){
			Node nNode = nList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){

				Element eElement = (Element) nNode;

				NodeList statnNm = eElement.getElementsByTagName("statnNm").item(0).getChildNodes();
				Node statnNmInfo = (Node) statnNm.item(0);
				String state = statnNmInfo.getNodeValue();

				NodeList subwayNm = eElement.getElementsByTagName("subwayNm").item(0).getChildNodes();
				Node subwayNmInfo = (Node) subwayNm.item(0);
				String subway = subwayNmInfo.getNodeValue();

				NodeList subwayXcnts = eElement.getElementsByTagName("subwayXcnts").item(0).getChildNodes();
				Node subwayXcntsInfo = (Node) subwayXcnts.item(0);
				String subwayX = subwayXcntsInfo.getNodeValue();

				NodeList subwayYcnts = eElement.getElementsByTagName("subwayYcnts").item(0).getChildNodes();
				Node subwayYcntsInfo = (Node) subwayYcnts.item(0);
				String subwayY = subwayYcntsInfo.getNodeValue();

				subwayList[i] = state;
			}
			
			System.out.println("subwayList" + i + " : " + subwayList[i]);
		}

		return subwayList;
	}
	

	@Override
	public String calculateLastSubway(String[] people, String[] subwayList) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("SearchMiddlePoint->SearchTenSub -> ChooseLastSub ");
		
		double sum = 0.0, avg = 0.0, var = 0.0, std = 0.0, time = 0.0;
		double totalTime[] = new double[10];
		double sevTime[] = new double[SearchMiddlePoint.friends.size()];
		double totalSd[] = new double[10];
		double total[] = new double[10];
		HashMap result = new HashMap();

		for(int i = 0; i < subwayList.length; i++) {
			System.out.println("SearchTenSub.statName  ****" + subwayList);
			for(int j = 0; j < SearchMiddlePoint.friends.size(); j++) {
				System.out.println("SearchTenSub.friends " + SearchMiddlePoint.friends.get(j));

				try {			
					String url = "http://swopenapi.seoul.go.kr/api/subway/sample/xml/shortestRoute/0/1/";
					String fURL = URLEncoder.encode(SearchMiddlePoint.friends.get(j), "UTF-8");
					String sURL = URLEncoder.encode(subwayList[i], "UTF-8");
					String shortURL = url + fURL + "/" + sURL;

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(shortURL);

					document.getDocumentElement().normalize();

					NodeList shortList = document.getElementsByTagName("row");
					
					
					Node nNode = shortList.item(0);
					
					if(nNode.getNodeType() == Node.ELEMENT_NODE){

						Element eElement = (Element) nNode;

						NodeList shtTravelTm = eElement.getElementsByTagName("shtTravelTm").item(0).getChildNodes();
						Node shtTravelInfo = (Node) shtTravelTm.item(0);
						String shtTime = shtTravelInfo.getNodeValue();

						time = Double.parseDouble(shtTime);
						totalTime[i] += time;
					} 

				} catch (Exception e) {
					e.printStackTrace();
				}	
				sevTime[j] = time;
			}

			//역마다의 평균 시간 계산
			avg = totalTime[i] / SearchMiddlePoint.friends.size();
			System.out.println("역마다의 평균 시간 : " + avg);

			sum = 0; 
			for(int l = 0; l < SearchMiddlePoint.friends.size(); l++) {
				sum += Math.pow(sevTime[l] - avg, 2.0);
			}
			
			//표준 편차를 구하기 위한 계산
			var = sum / SearchMiddlePoint.friends.size();

			//표준 편차 계산
			std = Math.sqrt(var);
			totalSd[i] = std;
			
			System.out.println("표준 편차 : " + std);
			
			//역마다의 표준 편차와 총 소요시간 계산 -> 공평성을 위한 변수들
			total[i] = totalSd[i] + totalTime[i];
			
			result.put(total[i], subwayList[i]);
		}	
		
		//표준 편차와 소요시간을 더한 값 오름차순으로 정렬
		Arrays.sort(total);
		
		//정렬 후 가장 짧은 시간 선택
		lastSub = (String)result.get(total[0]);
		System.out.println("최종역:: " + lastSub);
		
		//중간지점 반환(String)
		return lastSub;
	}
	
}
