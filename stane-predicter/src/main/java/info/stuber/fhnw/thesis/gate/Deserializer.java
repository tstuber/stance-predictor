package info.stuber.fhnw.thesis.gate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import info.stuber.fhnw.thesis.collector.Coding;
import info.stuber.fhnw.thesis.collector.Serializer;
import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

public class Deserializer {

	private static final String PATH_DOCUMENT = GetConfigPropertyValues.getProperty("path_documents");

	public static void main(String args[]) {

		int size = Deserializer.deserializeAllCoding().size();
		System.out.println(size + " Files deserialized");
	}

	public static List<Coding> deserializeAllCoding() {

		List<Coding> objects = new ArrayList<Coding>();

		List<String> fileNames = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(PATH_DOCUMENT))) {
			for (Path path : directoryStream) {
				fileNames.add(path.getFileName().toString());
				
			}
		} catch (IOException ex) {
		}
		
		for(String path : fileNames) {
			objects.add(deserializeCoding(path));
		}

		return objects;

	}

	public static Coding deserializeCoding(String fileName) {

		try {
			FileInputStream fius = new FileInputStream(PATH_DOCUMENT + fileName);
			ObjectInputStream ois = new ObjectInputStream(fius);
			Coding coding = (Coding) ois.readObject();
			coding.printDebug();
			ois.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
