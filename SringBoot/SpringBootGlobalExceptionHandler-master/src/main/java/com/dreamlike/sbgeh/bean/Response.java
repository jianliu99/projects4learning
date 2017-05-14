/**
 * 
 */
package com.dreamlike.sbgeh.bean;

/**
 * @author Broly
 *
 */
public class Response {

	private int retcode;
	private String retmsg;
	private Object retdata;

	public Response() {
	}

	public Response(int retcode, String retmsg, Object retdata) {
		super();
		this.retcode = retcode;
		this.retmsg = retmsg;
		this.retdata = retdata;
	}

	public int getRetcode() {
		return retcode;
	}

	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public Object getRetdata() {
		return retdata;
	}

	public void setRetdata(Object retdata) {
		this.retdata = retdata;
	}

}
