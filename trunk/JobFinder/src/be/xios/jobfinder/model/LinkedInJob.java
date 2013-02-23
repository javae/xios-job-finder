package be.xios.jobfinder.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class LinkedInJob implements Parcelable {

	private int id;
	private String positionTitle;
	private String companyName;
	private String location;
	private Date postingDate;

	public LinkedInJob() {
	}

	public LinkedInJob(Parcel in) {
		id = in.readInt();
		positionTitle = in.readString();
		companyName = in.readString();
		location = in.readString();
		postingDate = new Date(in.readLong());
	}

	public LinkedInJob(int id, String positionTitle, String companyName,
			String location, Date postingDate) {
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

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(positionTitle);
		dest.writeString(companyName);
		dest.writeString(location);
		dest.writeLong(postingDate.getTime());
	}
	
	public static final Parcelable.Creator<LinkedInJob> CREATOR = new Parcelable.Creator<LinkedInJob>() {

		@Override
		public LinkedInJob createFromParcel(Parcel in) {
			return new LinkedInJob(in);
		}

		@Override
		public LinkedInJob[] newArray(int size) {
			return new LinkedInJob[size];
		}
		
	};
}