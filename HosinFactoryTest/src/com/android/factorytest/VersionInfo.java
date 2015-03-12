package com.android.factorytest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.os.SystemProperties;
import android.widget.TextView;

public class VersionInfo extends BaseActivity {

	private TextView mTextView;
	private static final String FILENAME_PROC_VERSION = "/proc/version";
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetestBtnVisible(false);
		mTextView = (TextView) findViewById(R.id.version_id);
		
		@SuppressWarnings("rawtypes")
		Class c = null;
		Object invoker = null;
		Object result = null;
		Method m = null;
		String baseband = null;
		try {
			c = Class.forName("android.os.SystemProperties");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c != null) {
			try {
				invoker = c.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				m = c.getMethod("get",
						new Class[] { String.class, String.class });
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (m != null && invoker != null) {
				try {
					result = m.invoke(invoker, new Object[] {
							"gsm.version.baseband", "no message" });
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (result != null) {
			baseband = (String) result;
		}

		String phoneInfo = getResources().getString(R.string.modelNumber)
				+ android.os.Build.MODEL;
		phoneInfo += "\n" + getResources().getString(R.string.CPU_ABI)
				+ android.os.Build.CPU_ABI;

		if ((SystemProperties.get("ro.lewa.version",
				getResources().getString(R.string.device_info_default)))
				.equals(getResources().getString(R.string.device_info_default))) {
			phoneInfo += "\n"
					+ getResources().getString(R.string.user_version)
					+ SystemProperties.get(
							"ro.custom.build.version",
							getResources().getString(
									R.string.device_info_default));
		} else {
			phoneInfo += "\n"
					+ getResources().getString(R.string.user_version)
					+ SystemProperties.get("ro.lewa.version", getResources()
							.getString(R.string.device_info_default));
		}

		phoneInfo += "\n" + getResources().getString(R.string.android_version)
				+ android.os.Build.VERSION.RELEASE;
		phoneInfo += "\n" + getResources().getString(R.string.version)
				+ android.os.Build.DISPLAY;
		if (baseband != null) {
			phoneInfo += "\n"
					+ getResources().getString(R.string.baseband_version)
					+ baseband;
		}
		phoneInfo += "\n" + getResources().getString(R.string.kernel_version)
				+ getFormattedKernelVersion();
		mTextView.setText(phoneInfo);

	}

	private String getFormattedKernelVersion() {
		String procVersionStr;
		try {
			procVersionStr = readLine(FILENAME_PROC_VERSION);

			final String PROC_VERSION_REGEX = "\\w+\\s+" + /* ignore: Linux */
			"\\w+\\s+" + /* ignore: version */
			"([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
			"\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /*
														 * group 2:
														 * (xxxxxx@xxxxx
														 * .constant)
														 */
			"\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
			"([^\\s]+)\\s+" + /* group 3: #26 */
			"(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
			"(.+)"; /* group 4: date */

			Pattern p = Pattern.compile(PROC_VERSION_REGEX);
			Matcher m = p.matcher(procVersionStr);

			if (!m.matches()) {
				return "Unavailable";
			} else if (m.groupCount() < 4) {
				return "Unavailable";
			} else {
				/*
				 * return (new StringBuilder(m.group(1)).append(".").append(
				 * m.group(2)).append(".").append(m.group(3)).append(".")
				 * .append(m.group(4))).toString();
				 */

				String str = SystemProperties.get("gsm.serial");

				String btft = null;
				String gsm = null;
				String tdscdma = null;
				String lte = null;
				if (str != null && !str.isEmpty()) {
					if (str.length() >= 62 && str.charAt(60) == '1'
							&& str.charAt(61) == '0') {
						btft = "BT/FT : PASS"; // + "  DEBUG" +
												// str.substring(42, 62) +
												// "DEBUG" + str.length();
						/*
						 * if (str.charAt(60) == '1' && str.charAt(61) == '0' &&
						 * str.charAt(62) == 'P') { gsm = "2G : PASS"; } else {
						 * gsm = "2G : FAILURE"; } if (str.charAt(46) == 'P' &&
						 * str.charAt(47) == 'P') { tdscdma = "3G : PASS"; }
						 * else { tdscdma = "3G : FAILURE"; } if (str.charAt(42)
						 * == 'P' && str.charAt(43) == 'P') { lte = "4G : PASS";
						 * } else { lte = "4G : FAILURE"; }
						 */
					} else {
						btft = "BT/FT : FAILURE"; // + "  DEBUG" +
													// str.substring(60, 62) +
													// "DEBUG" + str.length();
						/*
						 * gsm = "2G : FAILURE"; tdscdma = "3G : FAILURE"; lte =
						 * "4G : FAILURE";
						 */
					}
				} else {
					/*
					 * gsm = "2G : null"; tdscdma = "3G : null"; lte =
					 * "4G : null";
					 */
					btft = "BT/FT : null";
				}
				return (new StringBuilder(m.group(1)).append(".")
						.append("builder@build-server").append(".")
						.append(m.group(3)).append(".").append(m.group(4))
						.append("\n").append("SN: ")
						.append(SystemProperties.get("gsm.serial"))
						.append("\n")
				// .append(gsm).append("\n").append(tdscdma).append("\n").append(lte).append("\n")).toString();
						.append(btft)).toString();
			}
		} catch (IOException e) {
			return "Unavailable";
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private String readLine(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename),
				256);
		try {
			return reader.readLine();
		} finally {
			reader.close();
		}
	}

	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.version);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
	}

}
