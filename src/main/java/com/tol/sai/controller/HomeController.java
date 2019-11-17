package com.tol.sai.controller;

import java.util.*;
import javax.inject.Inject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tol.sai.dto.*;
import com.tol.sai.service.*;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
    BasicDataSource dataSource;
	
	@Inject
    private SubwayService subwayService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "index.jsp";
	}
	
	//�߰� ���� ��� �޼ҵ�
	@RequestMapping(value = "/searchCentricPoing.do", method = RequestMethod.GET)
	public String searchCentricPoint(String people[], Model model) throws Exception {
		
		//subwayInfoList -> ������ ������� stationName, mapX, mapY
		List<SubwayVO> subwayInfoList = subwayService.searchLocation(people);
		
		//centricLocation -> ���� �߽� ��ǥ�� mapX, mapY
		String centricLocation[] = subwayService.calculateLocation(subwayInfoList);
		
		//subwayList -> �αٿ� 10���� stationName
		String subwayList[] = subwayService.searchTenSubway(centricLocation);
		
		//lastSub -> ���� ��õ StationName
		String lastSubway = subwayService.calculateLastSubway(people, subwayList);
	
		return "middlePage.jsp";
	}
	
}
