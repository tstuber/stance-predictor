package info.stuber.fhnw.thesis.utils;

public enum Parties {
	CON(1, "Conservative Party"), LAB(2, "Labour Party"), LD(3, "Liberal Democrats"), UKIP(4,
			"UK Independence Party"), GREEN(5, "Green Party"), SNP(6, "Scottish National Party"), PC(7, "Plaid Cymru");

	private final int id;
	private final String name;

	Parties(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name();
	}

}
