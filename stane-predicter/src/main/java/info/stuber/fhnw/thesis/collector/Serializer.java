package info.stuber.fhnw.thesis.collector;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

public class Serializer {

	private static final String PATH_DOCUMENT = GetConfigPropertyValues.getProperty("path_documents");

	public static void main(String args[]) {

		// test data.
		Coding coding = new Coding(1, 1, "url");
		coding.addParty(2);
		coding.setContent("slkfjaslkfda sdlkfjaösd flkajsdflkas jflaksfjjj");

		Serializer.serializeCoding(coding);
	}

	public static boolean serializeCoding(Coding coding) {

		boolean success = false;
		UUID uuid = java.util.UUID.randomUUID();
		String filePath = PATH_DOCUMENT + uuid.toString() + ".ser";
		
		try {
			FileOutputStream fout = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(coding);
			oos.close();
			System.out.println("File stored: " + uuid + ".ser");
			success = true;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return success;
	}
}
