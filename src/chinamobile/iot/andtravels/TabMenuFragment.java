package chinamobile.iot.andtravels;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class TabMenuFragment extends Fragment {

	private final String LOG_TAG = "TabMenuFragment";
	private RadioGroup viewTabMenuGroup;
	private boolean mIsLogin = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_menu_fragment, container, false);
        viewTabMenuGroup = (RadioGroup) view.findViewById(R.id.tab_menu);
		viewTabMenuGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
	            case R.id.daoLan:
	            	//此处先检查客户端是否注册,先暂时不处理
	            	if(mIsLogin)
	            	{
	            		Intent daoLanIntent = new Intent();
		            	daoLanIntent.setAction("chinamobile.iot.andtravels.communication.BeaconService");
		            	daoLanIntent.setPackage(getActivity().getPackageName());
		            	getActivity().startService(daoLanIntent);
	            	}
	            	else
	            	{
	            		new AlertDialog.Builder(getActivity()).setTitle("") 
	            		  
	            	     .setMessage("您还没有加入我们的圈子！")
	            	     .setNegativeButton("返回",new DialogInterface.OnClickListener() {  
	            	         @Override  
	            	         public void onClick(DialogInterface dialog, int which) {
	            	  
	            	             // TODO Auto-generated method stub  
	            	             
	            	         }  
	            	  
	            	     })
	            	     .setPositiveButton("确定",new DialogInterface.OnClickListener() {
	            	         @Override  
	            	         public void onClick(DialogInterface dialog, int which) {
	            	  
	            	             // TODO Auto-generated method stub 
	            	        	 Intent intent=new Intent(getActivity(), LoginActivity.class);
	            	        	 getActivity().startActivity(intent);
	            	         }  
	            	  
	            	     })
	            	     .show();
	            	}
	                break;
	            case R.id.youJi:
	            	Intent intent = new Intent("chinamobile.iot.andtravels.BLEScanService.UserAction"); 
	            	intent.putExtra("usrAction", "start");  
                    getActivity().sendBroadcast(intent); 
	            	
	            	
	                break;
	            case R.id.set:
	            	
	            	Intent setIntent=new Intent(getActivity(), SettingFragment.class);
	            	getActivity().startActivity(setIntent);
	            	
	                break;
	            case R.id.person:
	            	
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

}
