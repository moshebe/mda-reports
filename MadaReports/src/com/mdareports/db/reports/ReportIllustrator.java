package com.mdareports.db.reports;

import java.util.Random;

/**
 * Used for simulate reports. used only for debugging
 * 
 */
public class ReportIllustrator {
	private final String prefix = "#";
	private final String suffix = "*�� ����� �� ������ ����� ���� �������";

	private String[] msgs;

	public ReportIllustrator() {
		String[] ids = {"223", "86", "195"};
		String[] contents = {"��� 61 ���� 45\\����� ����� �� ����     �.�. ��' ��� ���� ������ ����� ����",
				"��� 61 ��� ����� \\ ������ ���� ���\\ ���� 443       �.�. ��' ��� 50 ��� ���� ����� ���� ���, ��",
				"��� 61 ���� ��� �����\\���\"� 2 � 5 � 10  9    ����� ��� ���� ���� ��� ��������"};
	
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
