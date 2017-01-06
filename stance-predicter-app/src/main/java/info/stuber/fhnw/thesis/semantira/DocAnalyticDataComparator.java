package info.stuber.fhnw.thesis.semantira;

import java.util.Comparator;

import com.semantria.mapping.output.DocAnalyticData;

public class DocAnalyticDataComparator implements Comparator<DocAnalyticData> {

	@Override
	public int compare(DocAnalyticData o1, DocAnalyticData o2) {
		// TODO Auto-generated method stub
		int o1Id = Integer.parseInt(o1.getId().split("_")[1]);
		int o2Id = Integer.parseInt(o2.getId().split("_")[1]);
		
		return o1Id < o2Id ? -1 : o1Id == o2Id ? 0 : 1;
	}
}
