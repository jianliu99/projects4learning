package com.mkyong.testng.examples.timeout;

import org.testng.annotations.Test;

public class TestTimeout {

	@Test(timeOut = 5000)
	public void testThisShouldPass() throws InterruptedException {
		Thread.sleep(4000);
	}

	@Test(timeOut = 1000)
	public void testThisShouldFail() {
		while (true);
	}

}