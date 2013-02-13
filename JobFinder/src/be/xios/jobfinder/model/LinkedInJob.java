package be.xios.jobfinder.model;

import java.util.GregorianCalendar;

public class LinkedInJob {

	private int id;
	private String positionTitle;
	private String companyName;
	private String location;
	private GregorianCalendar postingDate;

	public LinkedInJob() {
	}

	public LinkedInJob(int id, String positionTitle, String companyName,
			String location, GregorianCalendar postingDate) {
		setId(id);
		setPositionTitle(positionTitle);
		setCompanyName(companyName);
		setLocation(location);
		setPostingDate(postingDate);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public GregorianCalendar getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(GregorianCalendar postingDate) {
		this.postingDate = postingDate;
	}
}