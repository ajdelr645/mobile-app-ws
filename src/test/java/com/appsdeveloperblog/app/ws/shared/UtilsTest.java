package com.appsdeveloperblog.app.ws.shared;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/* extendwith is for bringing up spring context
 * can use to access property files
 * springboottest is for integration testing
 */


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {
	
	@Autowired
	Utils utils;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGenerateUserId() {
		String userId = utils.generateUserId(30);
		String userId2 = utils.generateUserId(30);
		
		assertNotNull(userId);
		assertNotNull(userId2);
		
		assertTrue(userId.length() == 30);
		assertTrue( !userId.equalsIgnoreCase(userId2) );
	}

	@Test
	final void testHasTokenNotExpired() {
		String token = utils.generateEmailVerificationToken("4yr65hhyid84");
		assertNotNull(token);
		
		boolean hasTokenExpired = Utils.hasTokenExpired(token);
		assertFalse(hasTokenExpired);
	}
	
	
	//note: must use an actual expired token
	//@Test
	//@Disabled
	final void testHasTokenExpired()
	{
		String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0M0B0ZXN0LmNvbSIsImV4cCI6MTYzNTkzMDM4OX0.wnUQZ4Y6-cvNjEiJvJk0oSt1Mo-UDPI0bKcKIbxRjm1jlTJ0vl-dtScVGnxZH1rNo_aD6F0v_pNCNxLSt5wDNQ";
		boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
		
		assertTrue(hasTokenExpired);
	}

}
