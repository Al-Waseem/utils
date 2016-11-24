package debug;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.omg.CORBA.Environment;

public final class LogManager {
	
	public static final String LOG_INFO                       			= "LOG_INFO";
    public static final String LOG_DEBUG                      			= "LOG_DEBUG";
    public static final String LOG_ERROR                      			= "LOG_ERROR";
    public static final int    LOG_INFO_TYPE                  			= 2;
    public static final int    LOG_DEBUG_TYPE                 			= 1;
    public static final int    LOG_ERROR_TYPE                 			= 0;

	private static boolean LOG_ACTIVATED = true;
	private static boolean LOG_FILE = true;
	private static boolean LOG_TOAST_ACTIVATED = false;

	private static String separator = "::";
	private static String separatorhash = "::";




	private static String getPreviousHashCode(String lastLine) {

		try {
			String[] fields = lastLine.split(separatorhash);

			return fields[fields.length - 1];
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static void error(String paramString) {

		writeLog(LOG_ERROR + separator + paramString);

	}

	public static void info(String paramString) {

		writeLog(LOG_INFO + separator + paramString);

	}

	public static void debug(String paramString) {

		writeLog(LOG_DEBUG + separator + paramString);

	}

	public static final String DATE_FORMAT_FILE = "yyyyMMdd";

	private static String getFileKeylog() {
		Calendar cal = Calendar.getInstance(Locale.US);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FILE, Locale.US);
		return sdf.format(cal.getTime());
	}


}
