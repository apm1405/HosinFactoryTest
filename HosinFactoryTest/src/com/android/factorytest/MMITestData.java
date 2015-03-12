package com.android.factorytest;

public class MMITestData {
	public static String[] keys = { "version", "backlight", "lcd", "key",
			"sd", "receiver", "speaker", "vibration", "loopback", "sim",
			"headset", "radio", "tp", "backcamera", "subcamera", "light",
			"gravity", "bluetooth", "wifi", "flashlight", "gps",
			"distance", "power", "report" };
	public static Class[] testItems = { VersionInfo.class,
			TestBackgroundlight.class, LCD.class, TestKey.class, TestSd.class,
			TelephoneReceiver.class, Test_speaker.class, VibrationTest.class,
			LoopbackMicSpk.class, Sim.class, HeadsetTest.class,
			TestRadioAndEarphone.class, Tp_test.class, TestCamera.class,
			TestSubCamera.class, TestLightSensor.class, Gravity.class,
			TestBluetooth.class, TestWiFi.class,
			Flashlight.class, GPS.class, TestDistance.class, Power.class,
			TestReport.class };
	public static int[] itemNameIds = { R.string.versioninfo,
			R.string.test_backgroundlight, R.string.but_test_lcd,
			R.string.but_test_key, R.string.but_test_sd,
			R.string.but_test_receiver, R.string.but_test_loudspeaker,
			R.string.but_test_vibration, R.string.but_test_loopback_micspk,
			R.string.but_test_sim, R.string.headset_test,
			R.string.but_test_radio, R.string.but_test_tp,
			R.string.but_test_backcamera, R.string.but_test_frontcamera,
			R.string.but_test_light, R.string.but_test_gravity,
			 R.string.but_test_bluetooth,
			R.string.but_test_wifi, R.string.but_test_flashlight,
			R.string.but_test_gps, R.string.but_test_distance,
			R.string.but_test_power, R.string.report_title

	};
}
