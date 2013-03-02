package be.xios.jobfinder.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchBuilder implements Parcelable {

	private long dbId;
	private String keywords;
	private String jobTitle;
	private String countryCode;
	private String postalCode;
	private int distance;
	private String industry;
	private String jobFunction;

	public SearchBuilder() {
	}

	public SearchBuilder(Parcel in) {
		dbId = in.readLong();
		keywords = in.readString();
		jobTitle = in.readString();
		countryCode = in.readString();
		postalCode = in.readString();
		distance = in.readInt();
		industry = in.readString();
		jobFunction = in.readString();
	}

	public long getDbId() {
		return dbId;
	}

	public void setDbId(long id) {
		this.dbId = id;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getJobFunction() {
		return jobFunction;
	}

	public void setJobFunction(String jobFunction) {
		this.jobFunction = jobFunction;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(dbId);
		dest.writeString(keywords);
		dest.writeString(jobTitle);
		dest.writeString(countryCode);
		dest.writeString(postalCode);
		dest.writeInt(distance);
		dest.writeString(industry);
		dest.writeString(jobFunction);

	}

	public static final Parcelable.Creator<SearchBuilder> CREATOR = new Parcelable.Creator<SearchBuilder>() {

		@Override
		public SearchBuilder createFromParcel(Parcel in) {
			return new SearchBuilder(in);
		}

		@Override
		public SearchBuilder[] newArray(int size) {
			return new SearchBuilder[0];
		}
	};
}