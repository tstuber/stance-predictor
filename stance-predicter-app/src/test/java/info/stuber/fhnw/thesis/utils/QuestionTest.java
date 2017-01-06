package info.stuber.fhnw.thesis.utils;

import org.junit.Assert;
import org.junit.Test;

public class QuestionTest {

	@Test
	public void QuestionCount_Test() {
		Assert.assertTrue(Question.getAllQuestions().size() == 29);
	}
	
	@Test
	public void inversionQuestion_Test() {
		Assert.assertEquals(-1.0f, Question.questionInversionFlag(99), 0.02);
		Assert.assertEquals(1.0f, Question.questionInversionFlag(50), 0.02);
	}
}
