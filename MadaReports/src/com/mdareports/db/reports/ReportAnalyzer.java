package com.mdareports.db.reports;

/**
 * analyze the received message text. All the parsing logic should appear only
 * here.
 */
public class ReportAnalyzer {// TODO turn this class logic into regular
								// expression
	private String messageBody; // the received message as it came
	private String strippedMessageBody; // the message without the irrelevant
										// parts, for more efficient analyzing
	private final String[] partsToBeRemoved = { "*על המידע חל חיסיון רפואי ואין להעבירו" };

	public ReportAnalyzer(String messageBody) {
		this.messageBody = messageBody;
		this.strippedMessageBody = getStrippedMessage();
	}

	/**
	 * The report id which starts right after the '#' sign until the first
	 * whitespace
	 * 
	 * @return the report id from the message
	 */
	public int getId() {
		return Integer.parseInt(messageBody.substring(1,
				messageBody.indexOf(" ")));
	}

	public String getDisplayContent() {
		return strippedMessageBody.replace("#", "").replace("אמב 61 ", "")
				.replaceAll("  ", " ").replaceFirst("  ", "\n");
	}

	/**
	 * The address which starts at the right after the 3th whitespace until the
	 * "   " (whitespace X 3)
	 * 
	 * @return the address from the message
	 */
	public String getAddress() {

		int startIndex = getIndexOf(strippedMessageBody, ' ', 3) + 1;
		int endIndex = strippedMessageBody.indexOf("   ");

		if (startIndex > 0 && endIndex > 0 && startIndex < endIndex) {
			return strippedMessageBody.substring(startIndex, endIndex);
		}

		return "";
	}

	/**
	 * The description which starts right after the "   " (whitespace X 3) until
	 * the '*'. because we stripped the * part we can cut it until the end of
	 * the message
	 * 
	 * @return the description from the message
	 */
	public String getDescription() {
		String whitespaceSeperator = "   ";
		int startIndex = strippedMessageBody.indexOf(whitespaceSeperator)
				+ whitespaceSeperator.length() + 1;
		int endIndex = strippedMessageBody.length();

		if (startIndex > 0 && endIndex > 0 && startIndex < endIndex) {
			return strippedMessageBody.substring(startIndex, endIndex).trim();
		}

		return "";
	}

	/**
	 * Get the Nth index of specified char in a string
	 * 
	 * @param str
	 *            - the string to search within
	 * @param c
	 *            - the char to be search
	 * @param occurence
	 *            - the Nth position that the inputed char is appeared in the
	 *            string
	 * @return the index of the Nth occurrence if there is one, or -1 otherwise.
	 */
	public int getIndexOf(String str, char c, int occurence) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				occurence--;
				if (occurence == 0)
					return i;
			}
		}
		return -1;
	}

	/*
	 * The address is after the 3th whitespace until the multiple whitespaces
	 * TODO check about case that there is no ambulance information
	 */
	private String getStrippedMessage() {
		String messageBodyCopy = messageBody;
		for (String part : partsToBeRemoved) {
			messageBodyCopy = messageBodyCopy.replace(part, "");
		}
		return messageBodyCopy;
	}

	/**
	 * Check whether the incoming SMS message represents report
	 * 
	 * @param messageBody
	 *            - the message body to be checked
	 * @return True if the message is report, False otherwise
	 */
	public static boolean isRelevantMessage(String messageBody) {
		return messageBody.startsWith("#") && messageBody.contains("*על המידע חל חיסיון רפואי ואין להעבירו");
	}
}
