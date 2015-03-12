package com.android.factorytest;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TestKey extends BaseActivity {
	private GridView gridView;
	private List<Key> list;
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initList();
		initView();
		
	}
	
	private void initList(){
		list = new ArrayList<Key>();
		Key home = new Key(KeyEvent.KEYCODE_HOME,R.string.home_button, 0);
		Key menu = new Key(KeyEvent.KEYCODE_MENU,R.string.menu_button, 0);
		Key back = new Key(KeyEvent.KEYCODE_BACK,R.string.back_button, 0);
		Key volumeUp = new Key(KeyEvent.KEYCODE_VOLUME_UP,R.string.volume_up_button, 0);
		Key volumeDown = new Key(KeyEvent.KEYCODE_VOLUME_DOWN,R.string.volume_down_button, 0);
		list.add(home);
		list.add(menu);
		list.add(back);
		list.add(volumeUp);
		list.add(volumeDown);
	}
	class Key{
		public int id;//按键对应的id值
		public int name;//按键名称对应的id
		public int num;//按键测试的次数
		public Key(int id,int name,int num){
			this.id = id;
			this.name = name;
			this.num = num;
		}
	}
	public void initView(){
	
		gridView = (GridView) findViewById(R.id.key_test_grid);
		adapter = new MyAdapter();
		gridView.setAdapter(adapter);
		
		mSuccess.setEnabled(false);
		mRetest.setEnabled(false);
		
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
				convertView = getLayoutInflater().inflate(R.layout.key_grid_item, null);
				TextView keyText = (TextView) convertView.findViewById(R.id.key_grid_item_text);
				Key key = list.get(position);
				keyText.setText(getString(key.name));
			
				if(0 == key.num){
					keyText.setBackgroundResource(R.color.light_blue);
				}
				else if(1 == key.num){
					keyText.setBackgroundResource(R.color.yellow);
				} 
			
			return convertView;
		}
		
	}
	
	public void updateButtonStates(int keycode){
		for(int i =0;i<list.size();i++){
			Key key = list.get(i);
			if(keycode == key.id){
				key.num++;
				if(key.num>=2){
					list.remove(key);
					break;
				}
			}
		}
		if(0 == list.size()){
			mSuccess.setEnabled(true);
			mFailed.setEnabled(false);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		mRetest.setEnabled(true);
		switch (keyCode) {
		case KeyEvent.KEYCODE_POWER:
			return true;
		case KeyEvent.KEYCODE_HOME:	
				updateButtonStates(keyCode);
			return true;
		case KeyEvent.KEYCODE_MENU:
				updateButtonStates(keyCode);
			return true;
		case KeyEvent.KEYCODE_BACK:
				updateButtonStates(keyCode);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
				updateButtonStates(keyCode);
			
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
				updateButtonStates(keyCode);
			
			return true;
		default:
			return false;
		}
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.key);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		initList();
		mSuccess.setEnabled(false);
		mRetest.setEnabled(false);
		mFailed.setEnabled(true);
		adapter.notifyDataSetChanged();
		return ;
	}
}
