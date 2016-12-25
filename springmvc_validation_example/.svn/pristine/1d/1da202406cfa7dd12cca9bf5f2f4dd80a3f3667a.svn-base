package com.howtodoinjava.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.howtodoinjava.demo.model.EmployeeVO;
import com.howtodoinjava.demo.service.EmployeeManager;
import com.howtodoinjava.demo.validator.EmployeeValidator;

@Controller
@RequestMapping("/employee-module/addNew")
@SessionAttributes("employee")
public class EmployeeController 
{
	@Autowired
	EmployeeManager manager;
	
	@Autowired
	EmployeeValidator validator;
	
	@RequestMapping(method = RequestMethod.GET)
    public String setupForm(Model model) 
	{
		 EmployeeVO employeeVO = new EmployeeVO();
		 model.addAttribute("employee", employeeVO);
		 return "addEmployee";
    }
	
	@RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("employee") EmployeeVO employeeVO,
            				BindingResult result, SessionStatus status) 
	{
		
		validator.validate(employeeVO, result);
		
		if (result.hasErrors()) {
			return "addEmployee";
		}
		//Store the employee information in database
		//manager.createNewRecord(employeeVO);
		
		//Mark Session Complete
		status.setComplete();
		return "redirect:addNew/success";
    }
	
	@RequestMapping(value = "/success", method = RequestMethod.GET)
    public String success(Model model) 
	{
		 return "addSuccess";
    }
}