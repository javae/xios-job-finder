package be.xios.jobfinder.data;

import java.util.List;

import be.xios.jobfinder.R;
import be.xios.jobfinder.model.SearchBuilder;
import be.xios.jobfinder.util.JobFinderUtil;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SavedSearchAdapter extends ArrayAdapter<SearchBuilder> {

	Context context;
	int layoutResourceId;
	List<SearchBuilder> objects;

	public SavedSearchAdapter(Context context, int layoutResourceId,
			List<SearchBuilder> objects) {
		super(context, layoutResourceId, objects);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SearchHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new SearchHolder();
			holder.txtKeywords = (TextView) row.findViewById(R.id.txtKeywords);
			holder.txtLocation = (TextView) row.findViewById(R.id.txtLocation);

			row.setTag(holder);

		} else {
			holder = (SearchHolder) row.getTag();

		}

		SearchBuilder savedSearch = objects.get(position);
		
		//TODO: eventueel omschrijving van opdracht vragen bij opslaan?
		String keyword = "";
		
		if (!savedSearch.getKeywords().equalsIgnoreCase("")) {
			keyword = savedSearch.getKeywords();
		} else {
			if (!savedSearch.getJobTitle().equalsIgnoreCase("")) {
				keyword = savedSearch.getJobTitle();
			} else {
				if (!savedSearch.getJobFunction().equalsIgnoreCase("")) {
					keyword = savedSearch.getJobFunction();
				} else {
					if (!savedSearch.getIndustry().equalsIgnoreCase("")) {
						keyword = savedSearch.getIndustry();
					} else {
							keyword = "Geen omschrijving";
					}
				}
			}
		}
		
		holder.txtKeywords.setText(keyword);

		holder.txtLocation.setText(JobFinderUtil.getDisplayString(savedSearch
				.getPostalCode())
				+ " - "
				+ JobFinderUtil.getDisplayString(savedSearch.getCountryCode()));

		return row;
	}

	static class SearchHolder {
		TextView txtKeywords;
		TextView txtLocation;
	}

}
