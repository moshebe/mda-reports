package com.mdareports.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.mdareports.db.codetables.ICodeTable;
import com.mdareports.db.models.Report;
import com.mdareports.db.models.Treatment;
import com.mdareports.db.models.TreatmentsToReports;
import com.mdareports.ui.activities.TreatmentsActivity;
import com.mdareports.utils.Logger;

public class DatabaseWrapper {
	private String TAG = Logger.makeLogTag(getClass());
	private static DatabaseWrapper instance;
	private DbHelper helper;
	List<DbChangedNotifier> listeners;

	public static DatabaseWrapper getInstance(Context ctx) {
		if (instance == null) {
			instance = new DatabaseWrapper(ctx);
		}
		return instance;
	}

	private DatabaseWrapper(Context ctx) {
		helper = new DbHelper(ctx);
		listeners = new ArrayList<DbChangedNotifier>();
	}

	public void notifyDatabaseChanged() {
		for (DbChangedNotifier listener : listeners) {
			listener.DbChanged();
		}
	}

	public void setDbChangedListener(DbChangedNotifier listener) {
		listeners.add(listener);
	}
	
	public void removeDbChangedListener(DbChangedNotifier listener) {
		listeners.remove(listener);
	}

	// ////////////////////////////
	// Reports Functions
	// ////////////////////////////

	public List<Report> getAllReports() {
		List<Report> reports = null;
		try {
			reports = helper.getReportDao().queryBuilder()
					.orderBy(Report.ID_COLUMN_NAME, false).query();
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return reports;
	}

	public List<Report> getUnreadReports() {
		List<Report> reports = null;
		try {
			reports = helper.getReportDao().queryBuilder()
					.orderBy(Report.ID_COLUMN_NAME, false).where()
					.eq(Report.IS_READ_COLUMN_NAME, false).query();
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return reports;
	}

	public List<Report> getUnreportedReports() {
		List<Report> reports = null;
		try {
			reports = helper.getReportDao().queryBuilder()
					.orderBy(Report.ID_COLUMN_NAME, false).where()
					.eq(Report.IS_REPORTED_COLUMN_NAME, false).query();
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return reports;
	}

	public boolean deleteAllReports() {
		try {
			helper.getReportDao().delete(getAllReports());
			notifyDatabaseChanged();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	public boolean deleteReports(Report[] reports) {
		try {
			for (Report r : reports) {
				helper.getReportDao().delete(r);
			}

			notifyDatabaseChanged();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	public boolean deleteReport(Report report) {
		try {
			helper.getReportDao().delete(report);
			notifyDatabaseChanged();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	public void createReport(Report report) {
		try {
			helper.getReportDao().create(report);
			notifyDatabaseChanged();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public int countAllReports(){
		Dao<Report, Integer> dao = helper.getReportDao();
		try {
			return (int)dao.countOf();			
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return -1;
		}
	}
	
	public int countUnreportedReports() {
		Dao<Report, Integer> dao = helper.getReportDao();
		try {
			return (int) dao.countOf(dao.queryBuilder().setCountOf(true)
					.where().eq(Report.IS_REPORTED_COLUMN_NAME, false).prepare());
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return -1;
		}
	}
	
	public int countUnreadReports() {
		Dao<Report, Integer> dao = helper.getReportDao();
		try {
			return (int) dao.countOf(dao.queryBuilder().setCountOf(true)
					.where().eq(Report.IS_READ_COLUMN_NAME, false).prepare());
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return -1;
		}
	}

	public Report getReportById(int id) {
		Report reportToReturn = null;
		try {
			reportToReturn = helper.getReportDao().queryForId(id);
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return reportToReturn;
	}

	public void updateReport(Report report) {
		try {
			helper.getReportDao().update(report);
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	// ////////////////////////////
	// End of Reports Functions
	// ////////////////////////////

	// ////////////////////////////
	// Code Tables Functions
	// ////////////////////////////

	@SuppressWarnings("unchecked")
	public List<ICodeTable> getAll(String codeTableActivityName) {
		List<ICodeTable> records = null;
		try {
			if (codeTableActivityName
					.equals(TreatmentsActivity.class.getName())) {
				records = (List<ICodeTable>) (List<?>) helper.getTreatmentDao()
						.queryForAll();
			}
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return records;
	}

	public List<Treatment> getAllTreatments() {
		List<Treatment> treatments = null;
		try {
			treatments = helper.getTreatmentDao().queryForAll();
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return treatments;
	}

	public void createCodeTableRecord(ICodeTable record) {
		try {
			String recordClassName = record.getClass().getName();
			if (recordClassName.equals(Treatment.class.getName())) {
				helper.getTreatmentDao().create((Treatment) record);

			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public boolean deleteCodeTableRecord(ICodeTable record) {
		try {
			String recordClassName = record.getClass().getName();
			if (recordClassName.equals(Treatment.class.getName())) {
				if (getTreatmentsToReportsByTreatmentId(
						((Treatment) record).getId()).isEmpty()) {
					helper.getTreatmentDao().delete((Treatment) record);
				} else {
					throw new SQLException(
							"this treatment is foriegn key in some Reports");
				}
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	public void updateCodeTableRecord(ICodeTable record) {
		try {
			String recordClassName = record.getClass().getName();

			if (recordClassName.equals(Treatment.class.getName())) {
				helper.getTreatmentDao().update((Treatment) record);
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public List<Treatment> getTreatmentsByReportId(Integer reportId) {
		List<Treatment> treatments = new ArrayList<Treatment>();
		try {
			List<TreatmentsToReports> treatmentsToReportsByReportId = getTreatmentsToReportsByReportId(reportId);
			List<Integer> TreatmentsId = new ArrayList<Integer>();
			for (TreatmentsToReports currentTreatmentsToReports : treatmentsToReportsByReportId) {
				TreatmentsId.add(currentTreatmentsToReports.getTreatment()
						.getId());
			}
			treatments = helper.getTreatmentDao().queryBuilder().where()
					.in(Treatment.TREATMENT_ID_COLUMN_NAME, TreatmentsId)
					.query();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}

		return treatments;
	}

	public List<Treatment> getAllOtherTreatments(Integer reportKey) {
		List<Treatment> treatments = new ArrayList<Treatment>();
		try {
			List<TreatmentsToReports> treatmentsToReports = helper
					.getTreatmentsToReportsDao().queryBuilder().where()
					.in(TreatmentsToReports.REPORT_ID_COLUMN_NAME, reportKey)
					.query();
			List<Integer> treatmentsId = new ArrayList<Integer>();
			for (TreatmentsToReports currentTreatmentsToReports : treatmentsToReports) {
				treatmentsId.add(currentTreatmentsToReports.getTreatment()
						.getId());
			}
			treatments = helper.getTreatmentDao().queryBuilder().where()
					.notIn(Treatment.TREATMENT_ID_COLUMN_NAME, treatmentsId)
					.query();
		} catch (Exception e) {
			Logger.LOGE(TAG, e.getMessage());
		}
		return treatments;
	}

	public List<TreatmentsToReports> getTreatmentsToReportsByTreatmentId(
			Integer treatmentId) {
		List<TreatmentsToReports> treatmentsToReports = new ArrayList<TreatmentsToReports>();
		try {
			// Get all the reports id's of this treatment
			treatmentsToReports = helper
					.getTreatmentsToReportsDao()
					.queryBuilder()
					.where()
					.eq(TreatmentsToReports.TREATMENT_ID_COLUMN_NAME,
							treatmentId).query();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}

		return treatmentsToReports;
	}

	public List<TreatmentsToReports> getTreatmentsToReportsByReportId(
			Integer reportId) {
		List<TreatmentsToReports> treatmentsToReports = new ArrayList<TreatmentsToReports>();
		try {
			// Get all the treatments id's of this report
			treatmentsToReports = helper.getTreatmentsToReportsDao()
					.queryBuilder().where()
					.eq(TreatmentsToReports.REPORT_ID_COLUMN_NAME, reportId)
					.query();
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}

		return treatmentsToReports;
	}

	public void createTreatmentToReport(TreatmentsToReports treatmentsToReports) {
		try {
			// Check if there is no similar row
			List<TreatmentsToReports> query = helper
					.getTreatmentsToReportsDao()
					.queryBuilder()
					.where()
					.eq(TreatmentsToReports.REPORT_ID_COLUMN_NAME,
							treatmentsToReports.getReport().getId())
					.and()
					.eq(TreatmentsToReports.TREATMENT_ID_COLUMN_NAME,
							treatmentsToReports.getTreatment().getId()).query();

			// if there is no row similar, create one
			if (query.isEmpty()) {
				helper.getTreatmentsToReportsDao().create(treatmentsToReports);
			}
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
		}
	}

	public void deleteTreatmentsToReportByReportId(Integer reportId) {
		List<TreatmentsToReports> treatmentsToReportsByReportId = getTreatmentsToReportsByReportId(reportId);
		for (TreatmentsToReports crrentTreatmentsToReports : treatmentsToReportsByReportId) {
			deleteTreatmentToReport(crrentTreatmentsToReports);
		}
	}

	public boolean deleteTreatmentToReport(
			TreatmentsToReports treatmentsToReports) {
		try {
			helper.getTreatmentsToReportsDao().delete(treatmentsToReports);
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	public boolean deleteTreatmentsToReportByReportAndTreatmentId(
			Integer reportId, Integer TreatmentId) {
		try {
			Dao<TreatmentsToReports, Integer> treatmentsToReportsDao = helper
					.getTreatmentsToReportsDao();
			List<TreatmentsToReports> allTreatmentsToReports = treatmentsToReportsDao
					.queryBuilder()
					.where()
					.eq(TreatmentsToReports.REPORT_ID_COLUMN_NAME, reportId)
					.and()
					.eq(TreatmentsToReports.TREATMENT_ID_COLUMN_NAME,
							TreatmentId).query();
			treatmentsToReportsDao.delete(allTreatmentsToReports);
		} catch (SQLException e) {
			Logger.LOGE(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	// ////////////////////////////
	// End of Code Tables Functions
	// ////////////////////////////
}