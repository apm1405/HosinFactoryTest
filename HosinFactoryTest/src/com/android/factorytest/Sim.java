package com.android.factorytest;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.provider.Telephony.SIMInfo;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class Sim extends BaseActivity {

	private TelephonyManager tm1;
	private TelephonyManager tm2;
	private TelephonyManager tm;
	private TextView sim1;
	private TextView sim2;
	private StringBuffer sb1;
	private StringBuffer sb2;
	private StringBuffer sb;
	private String IMSI_SIM1;
	private String IMSI_SIM2;
	private String IMSI_SIM;
	private boolean isMulti = false;
	private List<SIMInfo> mSiminfoList = new ArrayList<SIMInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		sim1 = (TextView) findViewById(R.id.sim1);
		sim2 = (TextView) findViewById(R.id.sim2);
		setRetestBtnVisible(false);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
	//	isMulti = FeatureOption.MTK_GEMINI_SUPPORT;
		sim1.setText(getSIM1());
		// sim1.setText(getSIM());
		if (isMulti) {
			sim2.setVisibility(View.VISIBLE);
			sim2.setText(getSIM2());
		} else {
			sim2.setVisibility(View.GONE);
		}
		super.onResume();
	}



	private StringBuffer getSIM() {

		sb = new StringBuffer();

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		sb.append(getString(R.string.sim1));
		if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {

			sb.append(getString(R.string.sim_good));
			IMSI_SIM = tm.getSubscriberId();
			if (IMSI_SIM.startsWith("46000") || IMSI_SIM.startsWith("46002")) {
				sb.append(getString(R.string.sim_yd));
			} else if (IMSI_SIM.startsWith("46001")) {
				sb.append(getString(R.string.sim_lt));
			} else if (IMSI_SIM.startsWith("46003")) {
				sb.append(getString(R.string.sim_dx));
			}
		} else if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
			sb.append(getString(R.string.sim_not_found));
		} else {
			sb.append(getString(R.string.sim_state_unknown));
		}
		return sb;
	}

	/*
	 * private StringBuffer getSIM1(){ sb1 = new StringBuffer(); tm1 =
	 * (TelephonyManager)getSystemService(PhoneFactory.getDefaultPhone());
	 * if(isMulti){ sb1.append(getString(R.string.sim1)); }else{
	 * sb1.append(getString(R.string.sim)); } if(isSimInserted(0)){
	 * sb1.append(getString(R.string.sim_good)); IMSI_SIM1 =
	 * tm1.getSubscriberId();
	 * if(IMSI_SIM1.startsWith("46000")||IMSI_SIM1.startsWith("46002")){
	 * sb1.append(getString(R.string.sim_yd)); }else
	 * if(IMSI_SIM1.startsWith("46001")){
	 * sb1.append(getString(R.string.sim_lt)); }else
	 * if(IMSI_SIM1.startsWith("46003")){
	 * sb1.append(getString(R.string.sim_dx)); } }else
	 * if(tm1.getSimState()==TelephonyManager.SIM_STATE_ABSENT){
	 * sb1.append(getString(R.string.sim_not_found)); }else{
	 * sb1.append(getString(R.string.sim_state_unknown)); } return sb1; }
	 * 
	 * private StringBuffer getSIM2(){ sb2 = new StringBuffer(); tm2 =
	 * (TelephonyManager
	 * )getSystemService(PhoneFactory.getServiceName(Context.TELEPHONY_SERVICE
	 * ,1)); sb2.append(getString(R.string.sim2)); if(isSimInserted(1)){
	 * sb2.append(getString(R.string.sim_good)); IMSI_SIM2 =
	 * tm2.getSubscriberId();
	 * if(IMSI_SIM2.startsWith("46000")||IMSI_SIM2.startsWith("46002")){
	 * sb2.append(getString(R.string.sim_yd)); }else
	 * if(IMSI_SIM2.startsWith("46001")){
	 * sb2.append(getString(R.string.sim_lt)); }else
	 * if(IMSI_SIM2.startsWith("46003")){
	 * sb2.append(getString(R.string.sim_dx)); } }else
	 * if(tm2.getSimState()==TelephonyManager.SIM_STATE_ABSENT){
	 * sb2.append(getString(R.string.sim_not_found)); }else{
	 * sb2.append(getString(R.string.sim_state_unknown)); } return sb2; }
	 */

	private StringBuffer getSIM1() {
		sb1 = new StringBuffer();
		Phone phone = PhoneFactory.getDefaultPhone();
		if (isMulti) {
			sb1.append(getString(R.string.sim1));
		} else {
			sb1.append(getString(R.string.sim));
		}
		if (/* isSimInserted(0) */findSIMInofBySlotId(0) != null) {
			sb1.append(getString(R.string.sim_good));
		} else {
			sb1.append(getString(R.string.sim_not_found));
		}
		return sb1;
	}

	private StringBuffer getSIM2() {
		sb2 = new StringBuffer();
		Phone phone = PhoneFactory.getDefaultPhone();
		if (isMulti) {
			sb2.append(getString(R.string.sim2));
		} else {
			sb2.append(getString(R.string.sim));
		}
		if (/* isSimInserted(1) */findSIMInofBySlotId(1) != null) {
			sb2.append(getString(R.string.sim_good));
		} else {
			sb2.append(getString(R.string.sim_not_found));
		}
		return sb2;
	}

	/*
	 * private boolean isSimInserted(int id) { boolean simInserted = false;
	 * //ITelephony phone =
	 * ITelephony.Stub.asInterface(ServiceManager.checkService("phone")); final
	 * ITelephony iTel = ITelephony.Stub.asInterface(ServiceManager
	 * .getService(Context.TELEPHONY_SERVICE));
	 * 
	 * if (iTel != null) { try { simInserted = iTel.isSimInsert(id); } catch
	 * (RemoteException e) { e.printStackTrace(); } } return simInserted; }
	 */
	private SIMInfo findSIMInofBySlotId(int mslot) {
		mSiminfoList = SIMInfo.getInsertedSIMList(this);
		for (SIMInfo siminfo : mSiminfoList) {
			if (siminfo.mSlot == mslot) {
				return siminfo;
			}
		}
		return null;
	}


	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.sim);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
	}

}
