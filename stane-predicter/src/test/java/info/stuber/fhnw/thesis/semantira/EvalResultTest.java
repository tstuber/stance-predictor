package info.stuber.fhnw.thesis.semantira;

import org.junit.Test;

import junit.framework.Assert;

public class EvalResultTest {

	int p = 1;
	int q = 1;
	
	@Test
	public void EvalResult_NeutralNoOpinion_Test() {
		
		Assert.assertTrue(new EvalResult(p, q, 3, 3).isSuccess());
		Assert.assertTrue(new EvalResult(p, q, 6, 3).isSuccess());
		Assert.assertFalse(new EvalResult(p, q, 2, 3).isSuccess());
	}
	
	@Test
	public void EvalResult_Agreement_Test() {
		Assert.assertTrue(new EvalResult(p, q, 1, 1).isSuccess());
		Assert.assertTrue(new EvalResult(p, q, 2, 1).isSuccess());
		Assert.assertTrue(new EvalResult(p, q, 1, 2).isSuccess());
		Assert.assertTrue(new EvalResult(p, q, 2, 2).isSuccess());
		Assert.assertFalse(new EvalResult(p, q, 3, 1).isSuccess());
		Assert.assertFalse(new EvalResult(p, q, 2, 3).isSuccess());
	}
	
	@Test
	public void EvalResult_DisAgreement_Test() {
		Assert.assertTrue(new EvalResult(p, q, 4, 4).isSuccess());
		Assert.assertTrue(new EvalResult(p, q, 4, 5).isSuccess());
		Assert.assertTrue(new EvalResult(p, q, 5, 4).isSuccess());
		Assert.assertTrue(new EvalResult(p, q, 5, 5).isSuccess());
		Assert.assertFalse(new EvalResult(p, q, 5, 3).isSuccess());
		Assert.assertFalse(new EvalResult(p, q, 1, 4).isSuccess());
	}
}
