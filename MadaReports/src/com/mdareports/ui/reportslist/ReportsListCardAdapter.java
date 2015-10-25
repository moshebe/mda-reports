package com.mdareports.ui.reportslist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdareports.R;
import com.mdareports.db.DatabaseWrapper;
import com.mdareports.db.models.Report;
import com.mdareports.ui.activities.details.DetailsActivity;

public class ReportsListCardAdapter extends BaseAdapter implements Filterable {
	private Context context;
	private List<Report> reportsList;
	private List<Report> originalReportsList;
	private LayoutInflater inflater;

	static class ViewHolder {
		TextView tvId;
		TextView tvReportReceivedAt;
		TextView tvReportDescription;
		TextView tvReportAddress;
		ImageView imgReportIsReported;
		ImageView imgReportIcon;
		ImageView imgHasLocation;
	}

	public ReportsListCardAdapter(Context context, List<Report> reports) {
		this.context = context;
		this.reportsList = (ArrayList<Report>) reports;
		this.originalReportsList = this.reportsList;
		this.inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	public ReportsListCardAdapter(Context context) {
		this(context, DatabaseWrapper.getInstance(context).getAllReports());
	}

	public Context getContext() {
		return this.context;
	}

	public void resetData() {
		reportsList = originalReportsList;
	}

	public Report[] getReports() {
		return (Report[]) originalReportsList.toArray();
	}

	/**
	 * Generate the view that represents the list. Choose the layout that should
	 * be displayed according to the {@link Report}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_card_report,
					parent, false);

			// bind the view holder to the proper widgets
			holder = new ViewHolder();
			holder.imgReportIcon = (ImageView) convertView
					.findViewById(R.id.imgReportIcon);
			holder.tvId = (TextView) convertView.findViewById(R.id.tvReportId);
			holder.tvReportReceivedAt = (TextView) convertView
					.findViewById(R.id.tvReportReceivedAt);
			holder.tvReportDescription = (TextView) convertView
					.findViewById(R.id.tvReportDescription);
			holder.imgReportIsReported = (ImageView) convertView
					.findViewById(R.id.imgReportIsReported);
			holder.imgHasLocation = (ImageView) convertView
					.findViewById(R.id.imgHasLocation);
			holder.tvReportAddress = (TextView) convertView
					.findViewById(R.id.tvReportAddress);

			holder.tvReportDescription.setMaxLines(2);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// populate the item with values
		final Report report = (Report) getItem(position);
		if (report != null) {
			holder.tvId.setText(report.getReportId() + "#");
			holder.tvReportReceivedAt.setText(new SimpleDateFormat(
					"E dd-MM-yyyy hh:mm").format(report.getReceivedAt()));
			holder.tvReportDescription.setText(report.getDescription());
			holder.tvReportAddress.setText(report.getAddress());

			// set the text and the icon according to the read status
			if (report.isRead()) {
				holder.imgReportIcon
						.setImageResource(R.drawable.ic_action_read);
				holder.tvReportDescription.setTextAppearance(getContext(),
						R.style.ReportsListItem_HeaderText_Read);
			} else {
				holder.imgReportIcon
						.setImageResource(R.drawable.ic_action_unread);
				holder.tvReportDescription.setTextAppearance(getContext(),
						R.style.ReportsListItem_HeaderText_Unread);
			}

			int textColor = context.getResources().getColor(
					report.isRead() ? R.color.reports_list_item_read_textcolor
							: R.color.reports_list_item_unread_textcolor);

			holder.tvReportDescription.setEllipsize(TextUtils.TruncateAt.END);
			holder.tvReportDescription.setMaxLines(2);

			holder.tvReportDescription.setTextColor(textColor);
			holder.tvId.setTextColor(textColor);
			holder.tvReportReceivedAt.setTextColor(textColor);

			// set the is-reported icon
			holder.imgReportIsReported
					.setImageResource(report.isReported() ? R.drawable.ic_action_accept
							: R.drawable.ic_action_warning);

			// set the has-location icon visibility
			holder.imgHasLocation
					.setVisibility(report.hasLocation() ? View.VISIBLE
							: View.GONE);

			// register to the on-click event
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(),
							DetailsActivity.class);
					intent.putExtra(DetailsActivity.REPORT_ID_EXTRA,
							report.getId());
					v.getContext().startActivity(intent);
				}
			});

		}
		return convertView;
	}

	// TODO: check about more efficient filter mechanism
	@SuppressLint("DefaultLocale")
	@Override
	public Filter getFilter() {
		return new Filter() {

			private boolean isPassedFilterTest(Report report,
					CharSequence constraint) {
				String combined = report.getReportId() + ", "
						+ report.getDescription();

				return combined.toLowerCase().contains(
						constraint.toString().toLowerCase());
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				// We implement here the filter logic
				if (constraint == null || constraint.length() == 0) {
					// No filter implemented we return all the list
					results.values = originalReportsList;
					results.count = originalReportsList.size();
				} else {
					// We perform filtering operation
					List<Report> filteredList = new ArrayList<Report>();

					for (Report r : reportsList) {
						if (isPassedFilterTest(r, constraint))
							filteredList.add(r);
					}

					results.values = filteredList;
					results.count = filteredList.size();

				}
				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				reportsList = (ArrayList<Report>) results.values;
				notifyDataSetChanged();
			}
		};
	}

	@Override
	public int getCount() {
		return reportsList.size();
	}

	public void updateReports(List<Report> reports) {
		this.originalReportsList = reports;
		resetData();
		notifyDataSetChanged();
	}

	public List<String> getToShareStringOfReports() {
		List<String> returnList = new ArrayList<String>();
		for (Report report : reportsList) {
			returnList.add(report.toShareString(getContext()));
		}

		return returnList;
	}

	@Override
	public Object getItem(int position) {
		return this.originalReportsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.originalReportsList.get(position).getId();
	}
}