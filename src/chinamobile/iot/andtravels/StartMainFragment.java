package chinamobile.iot.andtravels;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import chinamobile.iot.andtravels.SettingFragment.ListViewAdapter;
import chinamobile.iot.andtravels.TabMenuFragment.UserLoginReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;

public class StartMainFragment extends Fragment implements OnPageChangeListener{

	private static final String LOG_TAG = "StartMainFragment";
	private ViewPager viewPager;
	private RadioGroup viewDaoLanGroup;
	private ImageAdapter mImageAdapter;
	private Handler handler;
	private final int MESSAGE_WHAT_CHANGED = 100;
	private  CircleIndicatorView mCircleIndicatorView;
	private final int mViewPageNum = 4;

	public static final String strBroadcastMessage = "chinamobile.iot.andtravels.SetLogin";
	private UserLoginReceiver recv;
	LocalBroadcastManager mBroadcastManager;
	private boolean mIsLogin = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 初始化用户登录的广播
		mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
		IntentFilter filter = new IntentFilter(strBroadcastMessage);
		recv = new UserLoginReceiver();
		mBroadcastManager.registerReceiver(recv, filter);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mBroadcastManager.unregisterReceiver(recv);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.start_main, null);
		
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		ImageView personView = (ImageView)view.findViewById(R.id.personImage);
		ImageView view1 = (ImageView) inflater.inflate(R.layout.viewpage_item, null).findViewById(R.id.imageViewPage);
		ImageView view2 = (ImageView) inflater.inflate(R.layout.viewpage_item02, null)
				.findViewById(R.id.imageViewPage02);
		ImageView view3 = (ImageView) inflater.inflate(R.layout.viewpage_item03, null)
				.findViewById(R.id.imageViewPage03);
		ImageView view4 = (ImageView) inflater.inflate(R.layout.viewpage_item04, null)
				.findViewById(R.id.imageViewPage04);

		view1.setImageResource(R.drawable.start_view01);
		view2.setImageResource(R.drawable.start_view02);
		view3.setImageResource(R.drawable.start_view03);
		view4.setImageResource(R.drawable.start_view04);

		ArrayList<ImageView> views = new ArrayList<ImageView>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);

		mImageAdapter = new ImageAdapter(views);
		viewPager.setAdapter(mImageAdapter);
		viewPager.setOnPageChangeListener(this);
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
	
		initDaoLanView(view);
		
	    mCircleIndicatorView =  (CircleIndicatorView) view.findViewById(R.id.circleIndicatorView);
	    
	    personView.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//进入我的界面去
				int pos = 3;
				Intent intent = new Intent(getActivity(), ContainerActivity.class);
				intent.putExtra("curViewPos", pos);
				getActivity().startActivity(intent);
			}
	    	
	    });

		return view;
	}
	
	private class ImageAdapter extends PagerAdapter {

		private ArrayList<ImageView> viewlist;

		public ImageAdapter(ArrayList<ImageView> viewlist) {
			this.viewlist = viewlist;
		}

		@Override
		public int getCount() {
			// 设置成最大，使用户看不到边界
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// Warning：不要在这里调用removeView
			((ViewPager)container).removeView(viewlist.get(position % (viewlist.size())));  
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 对ViewPager页号求模取出View列表中要显示的项
			position %= viewlist.size();
			if (position < 0) {
				position = viewlist.size() + position;
			}
			ImageView view = viewlist.get(position);
			
			mCircleIndicatorView.setCircleCount(mViewPageNum);
			mCircleIndicatorView.setCircleSelectedPosition(position);
			mCircleIndicatorView.drawCircleView();
			
			// 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
			ViewParent vp = view.getParent();
			if (vp != null) {
				ViewGroup parent = (ViewGroup) vp;
				parent.removeView(view);
			}
			container.addView(view);
			// add listeners here if necessary
			return view;
		}
	}

	private void initDaoLanView(View view) {
		viewDaoLanGroup = (RadioGroup) view.findViewById(R.id.kaiShiDaoLan);
		viewDaoLanGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.kaiShiDaoLanButtom:
					// 此处先检查客户端是否注册,先暂时不处理
					Log.i("DaoLanView", "On Click");

					if (!mIsLogin) {
						AlertDialog dialog =new AlertDialog.Builder(getActivity()).setTitle("")
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
								
								dialog.dismiss();
							}

						}).show();
					} else {
						//提示用户需要付费才能使用
						int pos = 0;
						Intent intent = new Intent(getActivity(), ContainerActivity.class);
						intent.putExtra("curViewPos", pos);
						getActivity().startActivity(intent);

					}
					break;

				default:
					break;
				}

			}

		});

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public class UserLoginReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(LOG_TAG, "接收到广播：用户已经登录了！！！");
			mIsLogin = true;
		}
	}

}
