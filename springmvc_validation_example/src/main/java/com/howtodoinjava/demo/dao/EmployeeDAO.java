package com.howtodoinjava.demo.dao;

import java.util.List;

import com.howtodoinjava.demo.model.EmployeeVO;

public interface EmployeeDAO 
{
	public List<EmployeeVO> getAllEmployees();
}