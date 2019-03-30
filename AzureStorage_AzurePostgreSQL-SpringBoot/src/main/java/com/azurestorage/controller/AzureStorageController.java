package com.azurestorage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AzureStorageController {

	 @Autowired
	    private JdbcTemplate jdbcTemplate;
	 
	 @RequestMapping(value="/testing/{id}", method= RequestMethod.GET)
	 public String storageTest(@PathVariable("id") int id) {
		
		System.out.println("==========  starting: storageTest  ===========");
		 String empName=null;	
		//int empId=emp_id;
	try {
		String sql = "select name from employee where id=?";//+id; 
		 Object[] inputs = new Object[] {id};
		  empName = jdbcTemplate.queryForObject(sql, inputs, String.class); 
		
		System.out.println(" empName =======>> "+empName); 
		
	}
	catch(Exception e) {e.printStackTrace();}
	
		 
 if (empName ==null) empName="Employee Id not found.";
		return "==========Azure Storage Spring Boot Demo ================ Employee Name : " +empName+ "\n=========== Employee Id : "+ id;
					
	}
	
	
}
