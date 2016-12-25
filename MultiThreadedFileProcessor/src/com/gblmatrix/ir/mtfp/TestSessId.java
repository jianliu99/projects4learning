package com.gblmatrix.ir.mtfp;

import com.gblmatrix.utils.session.SessionUtils;

public class TestSessId {

	public static void main(String[] args) throws Exception {
		
		System.out.println(SessionUtils.getSessionId("10.213.108.21", "11820", null));
	}

}
