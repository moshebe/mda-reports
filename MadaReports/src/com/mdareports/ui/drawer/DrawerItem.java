package com.mdareports.ui.drawer;

import com.mdareports.ui.activities.MdaDrawerActivity;

public class DrawerItem {
	private int position;
	private int resIdTitle;
	private int resIdIcon;
	private boolean isTitle;
	private MdaDrawerActivity.DrawerMenuItems itemCode; // used for the
														// switch-case when
														// switching fragments

	public DrawerItem(int position, int resIdTitle, int resIdIcon,
			MdaDrawerActivity.DrawerMenuItems itemCode, boolean isTitle) {
		this.position = position;
		this.resIdTitle = resIdTitle;
		this.resIdIcon = resIdIcon;
		this.itemCode = itemCode;
		this.isTitle = isTitle;
	}

	public DrawerItem(int position, int resIdTitle, int resIdIcon,
			MdaDrawerActivity.DrawerMenuItems itemCode) {
		this(position, resIdTitle, resIdIcon, itemCode, false);
	}
	
	public DrawerItem(int position, int resIdTitle) {
		this(position, resIdTitle, -1, MdaDrawerActivity.DrawerMenuItems.Home, true);
	}
	
	

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getTitleResourceId() {
		return resIdTitle;
	}

	public void setTitle(int resIdTitle) {
		this.resIdTitle = resIdTitle;
	}

	public int getIconResourceId() {
		return resIdIcon;
	}

	public void setIcon(int resIdIcon) {
		this.resIdIcon = resIdIcon;
	}

	public boolean isTitle() {
		return isTitle;
	}

	public MdaDrawerActivity.DrawerMenuItems getItemCode() {
		return itemCode;
	}

	public void setItemCode(MdaDrawerActivity.DrawerMenuItems itemCode) {
		this.itemCode = itemCode;
	}

}
