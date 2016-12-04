package info.stuber.fhnw.thesis.utils;

import org.junit.Assert;
import org.junit.Test;

public class QuestionTest {

	@Test
	public void QuestionCount_Test() {
		Assert.assertTrue(Question.getAllQuestions().size() == 29);
	}
}
