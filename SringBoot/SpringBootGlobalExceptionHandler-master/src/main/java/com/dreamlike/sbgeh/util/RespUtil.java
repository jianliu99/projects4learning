/**
 * 
 */
package com.dreamlike.sbgeh.util;

import com.dreamlike.sbgeh.bean.Response;

/**
 * @author Broly
 *
 */
public class RespUtil {

	public static Response make(int retcode, String retmsg) {
		return new Response(retcode, retmsg, null);
	}

	public static Response make(int retcode, String retmsg, Object object) {
		return new Response(retcode, retmsg, object);
	}

}
