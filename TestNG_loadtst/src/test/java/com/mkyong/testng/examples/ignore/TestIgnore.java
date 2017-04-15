package com.mkyong.testng.examples.ignore;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestIgnore {

	@Test //defaulr enable=true
	public void test1() {
		Assert.assertEquals(true, true);
	}

	@Test(enabled = true)
	public void test2() {
		Assert.assertEquals(true, true);
	}

	@Test(enabled = false)
	public void test3() {
		Assert.assertEquals(true, true);
	}

}