/**
 * 
 */
package com.dreamlike.sbgeh.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dreamlike.sbgeh.bean.Response;
import com.dreamlike.sbgeh.util.RespUtil;

/**
 * @author Broly
 *
 */
@RestController
@RequestMapping("/home")
public class HomeController {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Response index() {
		return RespUtil.make(0, "success", "home.index");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Response show(@PathVariable("id") int id) {
		return RespUtil.make(0, "success", "home.show::id=" + String.valueOf(id));
	}

}
