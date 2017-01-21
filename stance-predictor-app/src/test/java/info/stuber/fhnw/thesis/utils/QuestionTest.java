package info.stuber.fhnw.thesis.utils;

import org.junit.Assert;
import org.junit.Test;

public class QuestionTest {

	@Test
	public void inversionQuestion_Test() {
		Assert.assertEquals(-1.0f, Question.questionInversionFlag(99), 0.02);
		Assert.assertEquals(1.0f, Question.questionInversionFlag(50), 0.02);
	}
}
