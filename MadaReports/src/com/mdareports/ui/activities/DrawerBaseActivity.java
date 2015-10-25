package com.mdareports.ui.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mdareports.R;
import com.mdareports.ui.drawer.DrawerItem;
import com.mdareports.ui.drawer.DrawerItemsAdapter;

public class DrawerBaseActivity extends BaseActivity {

	private ActionBarDrawerToggle actionBarDrawerToggle;
	protected DrawerLayout drawerLayout;
	protected ListView drawerList;
	protected DrawerItemsAdapter drawerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer_main);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Find the DrawerLayout view and the DrawerList view
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.drawerMenu);

		// Set the drawer's list view with click listener
		drawerList.setOnItemClickListener(drawerListOnClickListener);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		drawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /*
										 * nav drawer image to replace 'Up'
										 * caret
										 */
		R.string.drawer_open_accessibility, R.string.drawer_close_accessibility);

		// Set the initialized drawerToggle as the DrawerListener
		drawerLayout.setDrawerListener(actionBarDrawerToggle);

		// Set adapter to the drawer's list
		drawerAdapter = new DrawerItemsAdapter(this);
		drawerList.setAdapter(drawerAdapter);
	}

	OnItemClickListener drawerListOnClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			DrawerItem selectedDrawerItem = (DrawerItem) drawerAdapter
					.getItem(position);
			onDrawerLayoutItemSelected(selectedDrawerItem);
		}
	};

	/**
	 * Select The fragment to be shown in the content frame by the position on
	 * the item in the DrawerList.
	 */
	protected void onDrawerLayoutItemSelected(DrawerItem selectedDrawerItem) {
		// perform change only if the selected index was changed
		if (selectedDrawerItem.getPosition() != drawerAdapter
				.getSelectedIndex()) {

			// Save the selected DrawerItem Id as the last one that selected for
			// future use
			drawerAdapter.setSelected(selectedDrawerItem.getPosition());

			// load the suitbale fragment according to the selected item
			putContentFragment(onItemSelected(selectedDrawerItem));
		}

		drawerLayout.closeDrawer(drawerList);
	}

	/**
	 * Show the inputed fragment in the content section
	 * 
	 * @param f
	 */
	protected void putContentFragment(Fragment f) {
		if (f != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.drawerFrameLayoutContent, f).commit();
		}
	}

	/**
	 * Getting the fragment to be displayed according to the selected item
	 */
	protected Fragment onItemSelected(DrawerItem selectedItem) {
		return null;
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	public boolean isDrawerLayoutOpened() {
		return drawerLayout.isDrawerOpen(drawerList);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Change the content fragment or moving to new activity if needed.
	 * 
	 * @param item
	 *            - the chosen menu item
	 */
	public void MoveTo(MdaDrawerActivity.DrawerMenuItems item) {
		int position = drawerAdapter.getItemPosition(item);
		if (position != -1){
			onDrawerLayoutItemSelected((DrawerItem)drawerAdapter.getItem(position));			
		}
	}

}
