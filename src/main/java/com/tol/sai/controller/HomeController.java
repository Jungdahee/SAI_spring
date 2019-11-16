package com.tol.sai.controller;

import java.util.*;
import javax.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		
		return "home";
	}
	
	@Inject
    private SubwayService subwayService;

	@RequestMapping(value = "/searchLocation.do", method = RequestMethod.GET)
    public String searchLocation(Locale locale, Model model) throws Exception{
 
        logger.info("home");
        
        String str = "´öÁ¤";
        
        List<SubwayVO> subwayList = subwayService.searchLocation(str);
        
        System.out.println(subwayList.get(0).getWgsX());
        
        model.addAttribute("subwayList", subwayList);
 
        return null;
    }
	
}
