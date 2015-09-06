package chinamobile.iot.andtravels;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.aprilbrother.aprilbrothersdk.BeaconManager;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

public class StartMainFragment extends Fragment {

	private static final String LOG_TAG = "StartActivity";
	private ImageHandler handler = new ImageHandler(new WeakReference<StartActivity>(getActivity().this));
	private ViewPager viewPager;
	private RadioGroup viewTabMenuGroup;
	private RadioGroup viewDaoLanGroup;

	private final Activity mActivity;
	private Fragment mFragment;
	private boolean mIsExit = false;
	private boolean mIsLogin = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);
		// super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.start_main_fragment, null);

		ListView listView = (ListView) view.findViewById(R.id.ShareSetting);

		
			

		// initTabMenuView();
		return view;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_main);

		// 连接BLE扫描服务
		// Intent service = new Intent(this, BLEScanService.class);
		// bindService(service, mServiceConn, BIND_AUTO_CREATE);

		// 初始化iewPager的内容
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		LayoutInflater inflater = LayoutInflater.from(this);

		ImageView view1 = (ImageView) inflater.inflate(R.layout.viewpage_item, null).findViewById(R.id.imageViewPage);
		ImageView view2 = (ImageView) inflater.inflate(R.layout.viewpage_item02, null)
				.findViewById(R.id.imageViewPage02);
		ImageView view3 = (ImageView) inflater.inflate(R.layout.viewpage_item03, null)
				.findViewById(R.id.imageViewPage03);
		ImageView view4 = (ImageView) inflater.inflate(R.layout.viewpage_item04, null)
				.findViewById(R.id.imageViewPage04);

		view1.setImageResource(R.drawable.start_view01);
		view2.setImageResource(R.drawable.start_view01);
		view3.setImageResource(R.drawable.start_view01);
		view4.setImageResource(R.drawable.start_view01);

		ArrayList<ImageView> views = new ArrayList<ImageView>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);

		viewPager.setAdapter(new ImageAdapter(views));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			// 配合Adapter的currentItem字段进行设置。
			@Override
			public void onPageSelected(int arg0) {
				handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			// 覆写该方法实现轮播效果的暂停和恢复
			@Override
			public void onPageScrollStateChanged(int arg0) {
				switch (arg0) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
					break;
				default:
					break;
				}
			}
		});

		viewPager.setCurrentItem(Integer.MAX_VALUE / 2);// 默认在中间，使用户看不到边界
		// 开始轮播效果
		handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);

		initTabMenuView();
		initDaoLanView();
		startBle();
	}// end of onCreate

	

	private static class ImageHandler extends Handler {

		protected static final int MSG_UPDATE_IMAGE = 1;
		protected static final int MSG_KEEP_SILENT = 2;
		protected static final int MSG_BREAK_SILENT = 3;
		protected static final int MSG_PAGE_CHANGED = 4;

		protected static final long MSG_DELAY = 3000;

		// 使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
		private WeakReference<StartActivity> weakReference;
		private int currentItem = 0;

		protected ImageHandler(WeakReference<StartActivity> wk) {
			weakReference = wk;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.d(LOG_TAG, "receive message " + msg.what);
			StartActivity activity = weakReference.get();
			if (activity == null) {
				// Activity已经回收，无需再处理UI了
				return;
			}
			// 检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
			if (activity.handler.hasMessages(MSG_UPDATE_IMAGE)) {
				activity.handler.removeMessages(MSG_UPDATE_IMAGE);
			}
			switch (msg.what) {
			case MSG_UPDATE_IMAGE:
				currentItem++;
				activity.viewPager.setCurrentItem(currentItem);
				// 准备下次播放
				activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				break;
			case MSG_KEEP_SILENT:
				// 只要不发送消息就暂停了
				break;
			case MSG_BREAK_SILENT:
				activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				break;
			case MSG_PAGE_CHANGED:
				// 记录当前的页号，避免播放的时候页面显示不正确。
				currentItem = msg.arg1;
				break;
			default:
				break;
			}
		}
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
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 对ViewPager页号求模取出View列表中要显示的项
			position %= viewlist.size();
			if (position < 0) {
				position = viewlist.size() + position;
			}
			ImageView view = viewlist.get(position);
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

	private void initDaoLanView() {
		viewDaoLanGroup = (RadioGroup) findViewById(R.id.kaiShiDaoLan);
		viewDaoLanGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.kaiShiDaoLanButtom:
					// 此处先检查客户端是否注册,先暂时不处理
					Log.i("DaoLanView", "On Click");

					if (!mIsLogin) {
						// Intent intent=new Intent(mActivity,
						// LoginActivity.class);
						// mActivity.startActivity(intent);
						Intent daoLanIntent = new Intent();
						daoLanIntent.setAction("chinamobile.iot.andtravels.communication.BeaconService");
						daoLanIntent.setPackage(getPackageName());
						startService(daoLanIntent);
					} else {

						// mScanService.startScanBle();

					}
					break;

				default:
					break;
				}

			}

		});

	}



	/*
	 * //@Override public boolean onKeyDown(int keyCode, KeyEvent event) { //
	 * TODO Auto-generated method stub if(keyCode == KeyEvent.KEYCODE_BACK){
	 * ToQuitTheApp(); return false; } else{ return super.onKeyDown(keyCode,
	 * event); }
	 * 
	 * 
	 * }
	 */

	

}
