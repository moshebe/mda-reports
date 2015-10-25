package com.mdareports.db.reports;

import java.util.Random;

/**
 * Used for simulate reports. used only for debugging
 * 
 */
public class ReportIllustrator {
	private final String prefix = "#";
	private final String suffix = "*על המידע חל חיסיון רפואי ואין להעבירו";

	private String[] msgs;

	public ReportIllustrator() {
		String[] ids = {"223", "86", "195"};
		String[] contents = {"אמב 61 כביש 45\\עטרות למחלף בן ציון     ת.ד. פצ' קלה רוכב אופנוע שנפגע מרכב",
				"אמב 61 בית חורון \\ לכיוון גבעת זאב\\ כביש 443       ת.ד. פצ' קלה 50 מטר לפני מחסום גבעת זאב, מכ",
				"אמב 61 גבעת זאב סביון\\משה\"ב 2 ק 5 ד 10  9    מבוגר מצב חרום בטני חשד לאפנדציט"};
	
		msgs = new String[ids.length];
		for (int i = 0; i < msgs.length; i++) {
			msgs[i] = generateReport(ids[i], contents[i]);
		}
	}

	private String generateReport(String id, String content) {
		return prefix + id + " " + content + suffix;

	}
	
	public String getFakeReport(int i){
		return msgs[i % msgs.length];
	}
	public String getFakeReport(){
		return getFakeReport(new Random().nextInt(msgs.length));
	}
	
	

}
