package info.stuber.fhnw.thesis.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Question {

	private static Map<Integer, String> questions;
	
	public static String getQuestionById(int id)  {
		
		if(questions == null)
			// NOTE: Selection of questions has to be manually here!!!!
			// initQuestions();
			initKeyConceptQuestions();
		
		return questions.get(id);
	}
	

	// All questions which should be inverted de
	public static float questionInversionFlag(int questionId) {
		
		Set<Integer> invertedQuestions = new HashSet<Integer>(Arrays.asList(8,9,14,23,23,26,34));

		if (invertedQuestions.contains(questionId)) {
		    return -1.0f;
		} else {
			return 1.0f;
		}
	}
	
	private static void initKeyConceptQuestions() {
		questions = new HashMap<Integer, String>();
		
		questions.put(1, "Government spending cut balance budget");
		questions.put(2, "(mansion tax)^2  High Value Property Levy levided on residential properties");		
		questions.put(4, "inheritance tax abolished cuts");
		questions.put(5, "special concessions pensioner^1.4 winter fuel allowance free travel passes less well-off");
		questions.put(6, "top rate income tax reduction");
		questions.put(7, "railways renationalised");
		questions.put(8, "The government should scrap what is commonly known as the (bedroom tax)^2");
		questions.put(9, "NHS privatisation");
		questions.put(10, "option of imprisonment retained possession of drugs, personal consumption.");
		questions.put(11, "surrender records user activities government agencies fight terrorism crime internet service providers ISP telecoms government surveillance^1.4 ");
		questions.put(13, "The government allow extraction of (underground shale gas)^1.2 fracking^2");
		questions.put(14, "end subsidies for (wind farms)^2");
		questions.put(15, "Same-sex heterosexual couples same right to marry \"gay marriage\"");
		questions.put(17, "United Kingdom confident about (christian heritage)^2 Church of England");
		questions.put(18, "legal entitlement^2 two weeks paternity^2 \"paternity leave\"^2 increase");
		questions.put(19, "(social housing)^2 priority parents grandparents born locally");
		questions.put(20, "State schools grammar schools select pupils^2 ability");
		questions.put(21, "(Free schools)^1.4 and academies (local authority)^1.5 LA control");
	    questions.put(22, "University tuition fees scrap");
		questions.put(23, "\"United Kingdom\" UK remain^2 \"European Union\" EU Brexit^2");
		questions.put(25, "United Kingdom maintain support to \"developing countries\" foreign aid development");
		questions.put(26, "The Trident nuclear weapons system should be scrapped");
		questions.put(27, "United Kingdom quotas^1.4 limit number EU immigrants^1.4 immigration^1.2 entering country");
		questions.put(29, "National Health Service NHS^2 priority British citizens");
		questions.put(30, "State benefits^1.4 only available lived^1.4 in United Kingdom for at least 5^1.2 five^1.2 years Welfare^1.5 benefits");
		questions.put(31, "Only English^2 MPs^2 vote on issues affect England \"Member of Parliament\"");
		questions.put(32, "(House of Lords)^1.4 replaced (direct elected chamber)^2");
		questions.put(33, "teenage young people right to vote age^1.4 of 16^2");
		questions.put(34, "United Kingdom withdraw European Conventaion of Human Rights ECHR^2");
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
	}
}
