package info.stuber.fhnw.thesis.utils;

public enum Party {
	UNKNOWN(0, "Unknown Party"), CON(1, "Conservative Party"), LAB(2, "Labour Party"), LD(3,
			"Liberal Democrats"), UKIP(4, "UK Independence Party"), GREEN(5, "Green Party"),
	// SNP(6, "Scottish National Party"),
	// PC(7, "Plaid Cymru")
	;

	private final int id;
	private final String name;

	Party(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name();
	}

	public String toString() {
		return Integer.toString(this.id);
	}

	public boolean Compare(int i) {
		return id == i;
	}

	public static Party fromInteger(int id) {
		Party[] parties = Party.values();
		for (int i = 0; i < parties.length; i++) {
			if (parties[i].Compare(id))
				return parties[i];
		}
		return Party.UNKNOWN;
	}

}
