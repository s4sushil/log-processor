package com.creditsuisse.app;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.creditsuisse.app.configuration.MapUtil;

public class MapUtilTest {
	
	@Test
	public void testInstanceCreatedOnce() {
		MapUtil util1 = MapUtil.getInstance();
		MapUtil util2 = MapUtil.getInstance();

		assertThat(util1, equalTo(util2));		
	}

}
