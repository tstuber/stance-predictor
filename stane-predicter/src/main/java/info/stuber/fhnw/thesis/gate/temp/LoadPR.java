package info.stuber.fhnw.thesis.gate.temp;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gate.gui.MainFrame;
import gate.util.GateException;
import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

public class LoadPR {
	
	public static SerialAnalyserController getProcessingResources() throws MalformedURLException, GateException{
		long startLoadResourcesTime = System.currentTimeMillis();	//start time	
		
		if(Gate.getGateHome() == null)
			Gate.setGateHome(new File(GetConfigPropertyValues.getProperty("gate_home")));
		if(Gate.getPluginsHome() == null)
			Gate.setPluginsHome(new File(GetConfigPropertyValues.getProperty("gate_plugins_home")));
		
		Gate.init();	// to prepare the GATE library
		// MainFrame.getInstance().setVisible(true);	// to show the GATE Developer interface
		
		File pluginDir = Gate.getPluginsHome();	// get plugins home directory
		URL anniePlugin = new File(pluginDir, "ANNIE").toURI().toURL();	// specify plugin to be loaded
		Gate.getCreoleRegister().registerDirectories(anniePlugin);	// finally register the plugin

		// setting up searialAnalyserController
		SerialAnalyserController sac = (SerialAnalyserController)Factory.createResource("gate.creole.SerialAnalyserController");

		// setting up processing resources, only tokeniser needed
		ProcessingResource aEngTokeniser = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");

		// featuremaps for .jape files, specifying location of .jape files 
		FeatureMap startJapeFeature = Factory.newFeatureMap();
		startJapeFeature.put("grammarURL", new File("src/grammar/starttag.jape").toURI().toURL());
		FeatureMap endJapeFeature = Factory.newFeatureMap();
		endJapeFeature.put("grammarURL", new File("src/grammar/value.jape").toURI().toURL());
		FeatureMap columnNameJapeFeature = Factory.newFeatureMap();
		columnNameJapeFeature.put("grammarURL", new File("src/grammar/columnname.jape").toURI().toURL());

		// load JAPE language resources with specified features 
		LanguageAnalyser startTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", startJapeFeature);
		LanguageAnalyser endTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", endJapeFeature);
		LanguageAnalyser colNameJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", columnNameJapeFeature);				
		
		// add the language resources to application, here SerialAccessController
		sac.add(aEngTokeniser);
		sac.add(startTagJape);
		sac.add(endTagJape);
		sac.add(colNameJape);
		
		long endLoadResourcesTime = System.currentTimeMillis();	// end time
		long loadResourcesTime = endLoadResourcesTime-startLoadResourcesTime;	// total time
		System.out.println("Time to load Processing resources: "+loadResourcesTime+"ms");
		
		return sac;
	}
}
