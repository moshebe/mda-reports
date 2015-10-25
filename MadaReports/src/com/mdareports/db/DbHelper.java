package com.mdareports.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mdareports.R;
import com.mdareports.db.models.Report;
import com.mdareports.db.models.Treatment;
import com.mdareports.db.models.TreatmentsToReports;
import com.mdareports.utils.Logger;

public class DbHelper extends OrmLiteSqliteOpenHelper {
	private String TAG = Logger.makeLogTag(getClass());

	private Context context;
	
	// name of the database file for your application
	private static final String DATABASE_NAME = "MadaReportsDB.sqlite";

	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 2;

	// the dao's to access the tables
	private Dao<Report, Integer> reportsDao = null;
	private Dao<Treatment, Integer> treatmentDao = null;
	private Dao<TreatmentsToReports, Integer> treatmentsToReports = null;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context; 
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			// Create here the database tables
			TableUtils.createTable(connectionSource, Report.class);
			TableUtils.createTable(connectionSource, Treatment.class);
			TableUtils.createTable(connectionSource, TreatmentsToReports.class);


			// Set some default treatments
			String[] defaultTreatments = this.context.getResources().getStringArray(R.array.default_treatments);
			for (String treatment : defaultTreatments) {
				getTreatmentDao().create(new Treatment(treatment));
			}

		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			switch (oldVersion) {
				case 1:
					TableUtils.dropTable(connectionSource, Report.class, true);
					TableUtils.dropTable(connectionSource, Treatment.class, true);
					TableUtils.dropTable(connectionSource, TreatmentsToReports.class, true);
					// Create the tables again
					onCreate(db, connectionSource);
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}

	}

	public Dao<Report, Integer> getReportDao() {
		if (reportsDao == null) {
			try {
				reportsDao = getDao(Report.class);
			} catch (Exception e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
		return reportsDao;
	}


	public Dao<Treatment, Integer> getTreatmentDao() {
		if (treatmentDao == null) {
			try {
				treatmentDao = getDao(Treatment.class);
			} catch (Exception e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
		return treatmentDao;
	}

	public Dao<TreatmentsToReports, Integer> getTreatmentsToReportsDao() {
		if (treatmentsToReports == null) {
			try {
				treatmentsToReports = getDao(TreatmentsToReports.class);
			} catch (Exception e) {
				Logger.LOGE(TAG, e.getMessage());
			}
		}
		return treatmentsToReports;
	}
}