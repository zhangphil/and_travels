package chinamobile.iot.andtravels;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import chinamobile.iot.andtravels.BLEScanService.UserActionReceiver;

public class TabMenuFragment extends Fragment {

	private final String LOG_TAG = "TabMenuFragment";
	private RadioGroup viewTabMenuGroup;
	private boolean mIsLogin = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_menu_fragment, container, false);
		viewTabMenuGroup = (RadioGroup) view.findViewById(R.id.tab_menu);
		viewTabMenuGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			int pos = 0;
			Intent intent; 
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.daoLan:
					// 此处先检查客户端是否注册,先暂时不处理
					if ((boolean)((StartActivity) getActivity()).getUserIsLogin()) {
						pos = 0;
						intent = new Intent(getActivity(), MainActivity.class);
						//intent.putExtra("curViewPos", pos);
						getActivity().startActivity(intent);
					} else {
						new AlertDialog.Builder(getActivity()).setTitle("")

						.setMessage("您还没有加入我们的圈子！").setNegativeButton("返回", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								// TODO Auto-generated method stub

							}

						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								// TODO Auto-generated method stub
								Intent intent = new Intent(getActivity(), LoginActivity.class);
								getActivity().startActivity(intent);
							}

						}).show();
					}
					break;
				case R.id.youJi:
					pos = 1;
					intent = new Intent(getActivity(), ContainerActivity.class);
					intent.putExtra("curViewPos", pos);
					getActivity().startActivity(intent);

					break;
				case R.id.set:
					pos = 2;
					intent = new Intent(getActivity(), ContainerActivity.class);
					intent.putExtra("curViewPos", pos);
					getActivity().startActivity(intent);

					break;
				case R.id.person:
					pos = 3;
					intent = new Intent(getActivity(), ContainerActivity.class);
					intent.putExtra("curViewPos", pos);
					getActivity().startActivity(intent);

					break;
				default:
					break;
				}
				
				
			}

		});
			
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
}
