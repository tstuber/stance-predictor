package info.stuber.fhnw.thesis.utils;

import java.util.HashMap;
import java.util.Map;

public class Question {

	private static Map<Integer, String> questions;
	
	public static String getQuestionById(int id)  {
		
		if(questions == null)
			initQuestions();
		
		return questions.get(id);
	}
	
	public static Map<Integer, String> getAllQuestions() {
		if(questions == null)
			initQuestions();
		
		return questions;
	}
	
	private static void initQuestions() {
		questions = new HashMap<Integer, String>();
		
		questions.put(1, "Government spending should be cut further in order to balance the budget.");
		questions.put(2, "A 'mansion tax' should be levied on high-value residential properties.");
		questions.put(4, "Inheritance tax should be abolished.");
		questions.put(5, "Special concessions for pensioners (e.g. winter fuel allowance, free travel passes) should only be provided to the less well-off.");
		questions.put(6, "The top rate of income tax should be reduced.");
		questions.put(7, "The railways should be renationalised.");
		questions.put(8, "The government should scrap what is commonly known as the bedroom tax.");
		questions.put(9, "Private sector involvement in the NHS should be reduced.");
		questions.put(10, "The option of imprisonment should be retained for the possession of drugs, including for personal consumption.");
		questions.put(11, "To fight terrorism and other serious crimes, internet service providers and telecoms companies should keep and surrender records of users' activities if required by government agencies.");
		questions.put(13, "The government should allow the extraction of underground shale gas (fracking).");
		questions.put(14, "The government should end subsidies for wind farms.");
		questions.put(15, "Same sex and heterosexual couples should enjoy the same rights to marry.");
		questions.put(17, "The United Kingdom should be more confident about its Christian heritage.");
		questions.put(18, "The current legal entitlement of two weeks paternity leave should be increased.");
		questions.put(19, "For social housing, priority should be given to people whose parents and grandparents were born locally.");
		questions.put(20, "State schools should be able to select pupils according to ability.");
		questions.put(21, "Free Schools and Academies should be brought back under Local Authority control.");
		questions.put(22, "University tuition fees should be scrapped.");
		questions.put(23, "The United Kingdom should remain within the European Union.");
		questions.put(25, "The United Kingdom should maintain its support to developing countries through foreign aid.");
		questions.put(26, "The Trident nuclear weapons system should be scrapped.");
		questions.put(27, "The United Kingdom should set quotas on the number of EU immigrants entering the country.");
		questions.put(29, "The National Health Service should give priority to British citizens.");
		questions.put(30, "State benefits should only be available to those who have lived in the United Kingdom for at least five years.");
		questions.put(31, "Only English MPs should have the right to vote on Issues that only affect England.");
		questions.put(32, "The House of Lords should be replaced by a directly elected chamber.");
		questions.put(33, "Young people should be given the right to vote at the age of 16.");
		questions.put(34, "The United Kingdom should withdraw from the European Convention on Human Rights.");
		// questions.put(38, "Young people out of work, education or training for six months should be made to do unpaid community work in order to get benefits.");
	}
}
