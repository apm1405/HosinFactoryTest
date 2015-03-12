package com.android.factorytest;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.ServiceManager;
import android.os.StatFs;
import android.os.storage.IMountService;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.widget.TextView;

public class TestSd extends BaseActivity {

	private TextView mSdcard0;
	private TextView mSdcard1;
	private StorageManager mStorageMgr;
	private StorageVolume[] mVolumes;
	private String[] mStrings = { null, null, null };
	private boolean mIsSDExist;
	private IMountService mMountService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSdcard0 = (TextView) findViewById(R.id.sdcard0);
		mSdcard1 = (TextView) findViewById(R.id.sdcard1);

		setRetestBtnVisible(false);

		mStorageMgr = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
		mVolumes = mStorageMgr.getVolumeList();
		if (mVolumes != null) {
			for (int i = 0; i < mVolumes.length; i++) {
				mStrings[i] = mVolumes[i].getPath();
			}
		}

		//if (FeatureOption.MTK_2SDCARD_SWAP) {
			// try {
			mMountService = IMountService.Stub.asInterface(ServiceManager
					.getService("mount"));
			mIsSDExist = false; // mMountService.isSDExist();
			// } catch (RemoteException e) {
			// TODO: handle exception
			// }
			mSdcard0.setText(getSDCard0());
			mSdcard1.setText(getSDCard1());
		//} else {
		//	StringBuffer text = getSDCard(mStrings[1]);
		//	if (text == null) {
		//		mIsSDExist = false;
		//		mSdcard0.setText(getSDCard(mStrings[0]));
		//	} else {
		//		mIsSDExist = true;
		//		mSdcard1.setText(getSDCard(mStrings[1]));
		//		mSdcard0.setText(getSDCard(mStrings[0]));
		//	}
		//}

	}



	private StringBuffer getSDCard(String path) {
		// TODO Auto-generated method stub
		StringBuffer info = null;
		StatFs statfs;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			statfs = new StatFs(path);
			long blockSize = statfs.getBlockSize();
			long totalBlock = statfs.getBlockCount();
			long totalSize = blockSize * totalBlock / (1024 * 1024);
			long availableBlock = statfs.getAvailableBlocks();
			long availableSize = blockSize * availableBlock / (1024 * 1024);
			if (totalSize != 0) {
				info = new StringBuffer();
				if (path.equals("/storage/sdcard0")) {
					if (mIsSDExist) {
						info.append(
								getString(R.string.sd_inserted_external_storage))
								.append("\n"
										+ getString(R.string.sd_total_memory))
								.append(totalSize + "MB")
								.append("\n"
										+ getString(R.string.sd_avail_memory))
								.append(availableSize + "MB");
					} else {
						info.append(
								getString(R.string.sd_inserted_phone_storage))
								.append("\n"
										+ getString(R.string.sd_total_memory))
								.append(totalSize + "MB")
								.append("\n"
										+ getString(R.string.sd_avail_memory))
								.append(availableSize + "MB");
					}
				} else if (path.equals("/storage/sdcard1")) {
					info.append(getString(R.string.sd_inserted_phone_storage))
							.append("\n" + getString(R.string.sd_total_memory))
							.append(totalSize + "MB")
							.append("\n" + getString(R.string.sd_avail_memory))
							.append(availableSize + "MB");
				}
			}
		} else {
			info.append(getString(R.string.sd_not_found));
		}
		return info;
	}

	private StringBuffer getSDCard0() {
		StringBuffer info = new StringBuffer();
		StatFs statfs;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (mIsSDExist) {
				statfs = new StatFs(mStrings[1]);
			} else {
				statfs = new StatFs(mStrings[0]);
			}
			long blockSize = statfs.getBlockSize();
			long totalBlock = statfs.getBlockCount();
			long totalSize = blockSize * totalBlock / (1024 * 1024);
			long availableBlock = statfs.getAvailableBlocks();
			long availableSize = blockSize * availableBlock / (1024 * 1024);
			info.append(getString(R.string.sd_inserted_phone_storage))
					.append("\n" + getString(R.string.sd_total_memory))
					.append(totalSize + "MB")
					.append("\n" + getString(R.string.sd_avail_memory))
					.append(availableSize + "MB");
		} else {
			info.append(getString(R.string.sd_not_found));
		}
		return info;
	}

	private StringBuffer getSDCard1() {
		StringBuffer info = new StringBuffer();
		StatFs statfs;
		if (mIsSDExist) {
			statfs = new StatFs(mStrings[0]);
		} else {
			statfs = new StatFs(mStrings[1]);
		}
		long blockSize = statfs.getBlockSize();
		long totalBlock = statfs.getBlockCount();
		long totalSize = blockSize * totalBlock / (1024 * 1024);
		long availableBlock = statfs.getAvailableBlocks();
		long availableSize = blockSize * availableBlock / (1024 * 1024);
		if (totalSize != 0) {
			info.append(getString(R.string.sd_inserted_external_storage))
					.append("\n" + getString(R.string.sd_total_memory))
					.append(totalSize + "MB")
					.append("\n" + getString(R.string.sd_avail_memory))
					.append(availableSize + "MB");
		} else {
			info.append(getString(R.string.sd_not_found));
		}
		return info;
	}


	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_sd);
	}



	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
	}
}
