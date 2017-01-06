package info.stuber.fhnw.thesis.utils;

import org.junit.Test;
import org.springframework.util.Assert;

public class PartyTest {

	@Test
	public void TestParty() {
		
		Assert.isTrue(Party.CON == Party.fromInteger(1));
		
	}
}
