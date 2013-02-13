package be.xios.jobfinder.data;

import java.text.SimpleDateFormat;
import java.util.List;

import be.xios.jobfinder.layout.CustomJobView;
import be.xios.jobfinder.model.LinkedInJob;
import be.xios.jobfinder.util.JobFinderUtil;

import xios.be.jobfinder.R;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class JobListAdapter extends ArrayAdapter<LinkedInJob> {

	private final Context context;
	private final List<LinkedInJob> jobs;

	static class ViewHolder {
		public CustomJobView jobView;
	}

	public JobListAdapter(Context context, List<LinkedInJob> jobs) {
		super(context, R.layout.custom_job_view, jobs);
		this.context = context;
		this.jobs = jobs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = layoutInflater.inflate(R.layout.custom_job_view, null);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.jobView = (CustomJobView) rowView.findViewById(R.id.customjobview);
			viewHolder.jobView.setJob(jobs.get(position));
			
			rowView.setTag(viewHolder);
		}
		
		ViewHolder viewHolder = (ViewHolder) rowView.getTag();
		LinkedInJob job = jobs.get(position);
		viewHolder.jobView.setJobTitle(job.getPositionTitle());
		viewHolder.jobView.setCompanyNameLocation(job.getCompanyName());
		viewHolder.jobView.setJobPostingDate(JobFinderUtil.formatDate(job.getPostingDate().getTime(), "yyyy-MM-DD"));
		
		return rowView;
	}

}
