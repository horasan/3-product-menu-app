package com.horasan.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.horasan.dto.MenuItem;
import com.horasan.services.MenuOperationService;

@RestController
@RequestMapping("/menuapi")
@CrossOrigin
public class MenuOperationController {

	@Autowired
	private MenuOperationService menuOperationService; 
	
	
	@PostMapping("/")
	public MenuItem createMenuItem(@RequestBody MenuItem menuItem) {
		return menuOperationService.createMenuItem(menuItem); 
	}
	
	@GetMapping("/")
	public ResponseEntity<List<MenuItem>> getAllMenuItems() throws IOException {
		
		return new ResponseEntity<>(menuOperationService.getAllMenuItems(), HttpStatus.OK); 
	}
	
	@GetMapping("/description/{searchValue}")
	public ResponseEntity<List<MenuItem>> getMenuItemByDescription(@PathVariable String searchValue) throws IOException {
		
		if(!StringUtils.hasText(searchValue)) {
			new ResponseEntity<>(new ArrayList<MenuItem>(), HttpStatus.NOT_FOUND);
		}
		
		if(searchValue.length() < 3) {
			new ResponseEntity<>(new ArrayList<MenuItem>(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(menuOperationService.queryDescriptionSearchAsYouType(searchValue), HttpStatus.OK); 
	}
	
}
