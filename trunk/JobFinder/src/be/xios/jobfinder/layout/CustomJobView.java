package be.xios.jobfinder.layout;

import be.xios.jobfinder.model.LinkedInJob;
import xios.be.jobfinder.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomJobView extends RelativeLayout {

	private TextView jobTitle;
	private TextView companyNameLocation;
	private TextView jobPostingDate;
	
	private LinkedInJob job;
	
	public CustomJobView(Context context) {
		super(context);
	}
	
	public CustomJobView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.job_row_layout, this);
		
		jobTitle = (TextView) findViewById(R.id.tv_job_title);
		companyNameLocation = (TextView) findViewById(R.id.tv_company_name_location);
		jobPostingDate = (TextView) findViewById(R.id.tv_job_posting_date);
	}
	
	public String getJobTitle() {
		return (String) jobTitle.getText();
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle.setText(jobTitle);
	}

	public String getCompanyNameLocation() {
		return (String) companyNameLocation.getText();
	}

	public void setCompanyNameLocation(String companyNameLocation) {
		this.companyNameLocation.setText(companyNameLocation);
	}

	public String getJobPostingDate() {
		return (String) jobPostingDate.getText();
	}

	public void setJobPostingDate(String jobPostingDate) {
		this.jobPostingDate.setText(jobPostingDate);
	}
	
	public LinkedInJob getJob() {
		return job;
	}
	
	public void setJob(LinkedInJob job) {
		this.job = job;
	}
}