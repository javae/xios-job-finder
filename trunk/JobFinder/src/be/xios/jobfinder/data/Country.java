package be.xios.jobfinder.data;

public class Country {
	private String name;
	private String displayName;
	private String iso2;
	private String iso3;
	private String numCode;

	public Country(String iso2, String name, String displayName, String iso3, String numCode) {
		this.name = name;
		this.displayName = displayName;
		this.iso2 = iso2;
		this.iso3 = iso3;
		this.numCode = numCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getIso2() {
		return this.iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	public String getIso3() {
		return this.iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	public String getNumCode() {
		return this.numCode;
	}

	public void setNumCode(String numCode) {
		this.numCode = numCode;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
}
