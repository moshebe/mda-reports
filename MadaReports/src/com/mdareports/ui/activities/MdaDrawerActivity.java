package com.mdareports.ui.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mdareports.R;
import com.mdareports.ui.drawer.DrawerItem;
import com.mdareports.ui.fragments.AboutUsFragment;
import com.mdareports.ui.fragments.DonateUsFragment;
import com.mdareports.ui.fragments.HomeFragment;
import com.mdareports.ui.fragments.PatientReportFragment;
import com.mdareports.ui.fragments.SettingsFragment;
import com.mdareports.ui.fragments.reportslists.ReportsListsFilters;
import com.mdareports.utils.DeviceInfoUtils;
import com.mdareports.utils.HelpUtils;

/**
 * Responsible for building the application drawer. any application specific
 * elements should appear here.
 */
public class MdaDrawerActivity extends DrawerBaseActivity {

	public enum DrawerMenuItems {
		Home, AllReports, UnreadReports, UnreportedReports, PatientReport, DonateUs, Settings, AboutUs
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// populate the drawer with the items
		List<DrawerItem> items = createMenuItems();
		if (items.size() > 0) {
			drawerAdapter.setItems(items);

			// switch to the default selected item's fragment
			super.putContentFragment(onItemSelected(items.get(drawerAdapter
					.getSelectedIndex())));
		}

		HelpUtils.showHelpOnHomeAction(
				R.string.help_showcase_drawer_menu_title,
				R.string.help_showcase_drawer_menu_description, this);
	}

	private void moveToReportsList(ReportsListsFilters filter) {
		Intent intent = new Intent(this, ReportsListActivity.class);
		intent.putExtra(ReportsListActivity.REPORTS_LIST_ARGS, filter.ordinal());
		startActivity(intent);
	}

	@Override
	protected Fragment onItemSelected(DrawerItem selectedItem) {
		Fragment frgmt = null;

		switch (selectedItem.getItemCode()) {

		case Home:
			frgmt = new HomeFragment();
			break;

		case AllReports:
			moveToReportsList(ReportsListsFilters.All);
			break;

		case UnreadReports:
			moveToReportsList(ReportsListsFilters.Unread);
			break;

		case UnreportedReports:
			moveToReportsList(ReportsListsFilters.Unreported);
			break;

		case PatientReport:
			frgmt = new PatientReportFragment();
			break;

		case DonateUs:
			frgmt = new DonateUsFragment();
			break;
			
		case Settings:
			if (DeviceInfoUtils.hasHoneycomb()) {
				frgmt = new SettingsFragment();
			} else {
				startActivity(new Intent(this, OldSettingsActivity.class));
			}
			break;
			
		case AboutUs:
			frgmt = new AboutUsFragment();		
			break;

		

		default:
			break;
		}

		return frgmt;
	}

	private List<DrawerItem> createMenuItems() {

		List<DrawerItem> items = new ArrayList<DrawerItem>();

		int i = -1; // the ++i will set the first to 0

		/*
		 * Reports Filters
		 */
		items.add(new DrawerItem(++i, R.string.drawer_menu_item_home,
				R.drawable.ic_menu_home, DrawerMenuItems.Home));

		items.add(new DrawerItem(++i,
				R.string.drawer_menu_item_category_reports));

		items.add(new DrawerItem(++i, R.string.drawer_menu_item_reports_all,
				R.drawable.ic_action_view_as_list,
				DrawerMenuItems.AllReports));

		items.add(new DrawerItem(++i, R.string.drawer_menu_item_reports_unread,
				R.drawable.ic_action_unread, DrawerMenuItems.UnreadReports));

		items.add(new DrawerItem(++i,
				R.string.drawer_menu_item_reports_unreported,
				R.drawable.ic_action_warning,
				DrawerMenuItems.UnreportedReports));

		items.add(new DrawerItem(++i,
				R.string.drawer_menu_item_category_general));

		items.add(new DrawerItem(++i,
				R.string.drawer_menu_item_patience_report,
				R.drawable.ic_menu_friendslist, DrawerMenuItems.PatientReport));

		items.add(new DrawerItem(++i, R.string.drawer_menu_item_settings,
				android.R.drawable.ic_menu_preferences,
				DrawerMenuItems.Settings));

		items.add(new DrawerItem(++i, R.string.drawer_menu_item_donate_us,
				R.drawable.ic_action_favorite,
				DrawerMenuItems.DonateUs));
		
		items.add(new DrawerItem(++i, R.string.drawer_menu_item_about_us,
				android.R.drawable.ic_menu_info_details,
				DrawerMenuItems.AboutUs));

		return items;
	}

}
