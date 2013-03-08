package be.xios.jobfinder.data;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import be.xios.jobfinder.R;
import be.xios.jobfinder.model.LinkedInJob;

public class FavoritesAdapter extends ArrayAdapter<LinkedInJob> {

	Context context;
	int layoutResourceId;
	List<LinkedInJob> objects;

	public FavoritesAdapter(Context context, int layoutResourceId,
			List<LinkedInJob> objects) {
		super(context, layoutResourceId, objects);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FavHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new FavHolder();
			holder.txtJobTitle = (TextView) row.findViewById(R.id.txtJobtitle);
			holder.txtCompany = (TextView) row.findViewById(R.id.txtCompany);

			row.setTag(holder);

		} else {
			holder = (FavHolder) row.getTag();

		}

		LinkedInJob favJob = objects.get(position);

		holder.txtJobTitle.setText(favJob.getPositionTitle());
		holder.txtCompany.setText(favJob.getCompanyName());

		return row;
	}

	static class FavHolder {
		TextView txtJobTitle;
		TextView txtCompany;
	}

}
