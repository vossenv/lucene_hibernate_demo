package com.dm.teamquery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamqueryApplicationTests {

	@Test
	public void contextLoads() {


		int rows = 17;
		int size = 5;

		double x = Math.ceil((double) rows / (double) size);

		System.out.println();


	}

}

