package info.stuber.fhnw.thesis.collector;

import java.util.List;

public interface ISourceLoader {

	public void print();

	public List<Coding> getCodings();

	public int getCodingCount();

}