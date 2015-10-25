package com.mdareports.db;

public interface DbChangedNotifier {

	/**
	 * Notify about some changes in the database content
	 */
	public void DbChanged();
	
}
