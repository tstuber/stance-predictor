package info.stuber.fhnw.thesis.utils;

public class Props {

	private static String getProp(String prop) {
		return GetConfigPropertyValues.getProperty(prop);
	}

	public static String getDocPath() {
		return getProp("path_documents");
	}

	public static String getIndexPathWS1() {
		return getProp("path_index_ws1");
	}

	public static String getIndexPathWS2() {
		return getProp("path_index_ws2");
	}

	public static String getIndexPathWS3() {
		return getProp("path_index_ws3");
	}

	public static String getIndexPathWS4() {
		return getProp("path_index_ws4");
	}

	public static String getIndexPathWSTest() {
		return getProp("path_index_test");
	}

	public static int getWindowSize1() {
		return Integer.parseInt(getProp("window_size_1"));
	}

	public static int getWindowSize2() {
		return Integer.parseInt(getProp("window_size_2"));
	}

	public static int getWindowSize3() {
		return Integer.parseInt(getProp("window_size_3"));
	}

	public static int getWindowSize4() {
		return Integer.parseInt(getProp("window_size_4"));
	}

	public static String getCodingUrls() {
		return getProp("path_codingurls");
	}

	public static String getCodingResults() {
		return getProp("path_codingresults");
	}

	public static String getGateHome() {
		return getProp("gate_home");
	}

	public static String getGatePluginsHome() {
		return getProp("gate_plugins_home");
	}
	
	/*** EvaluationProperties ***/
	public static int evalWindowSize() {
		return Integer.parseInt(getProp("window_size"));
	}
	
	public static boolean evalOnlySentiment() {
		return Boolean.parseBoolean(getProp("only_sentimental"));
	}
	
	public static boolean evalFirstHit() {
		return Boolean.parseBoolean(getProp("first_hit"));
	}
	
	public static boolean evalInverseQuestion() {
		return Boolean.parseBoolean(getProp("inverse_question_mode"));
	}
	
	public static String evalAnalyzer() {
		return getProp("analyzer");
	}	
}