package com.dm.teamquery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamqueryApplicationTests {

	@Test
	public void contextLoads() throws Exception {

		String query = "%22this%20is%20a%20%22%20quoted%20string%20OR%20this%20%7B%7D%2F%2F";

		String result = java.net.URLDecoder.decode(query, StandardCharsets.UTF_8.name());

		System.out.println();
	}

}
