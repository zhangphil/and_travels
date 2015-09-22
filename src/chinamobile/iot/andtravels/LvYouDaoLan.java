package chinamobile.iot.andtravels;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import chinamobile.iot.andtravels.utils.Utils;

public class LvYouDaoLan extends Fragment {

	private MyFragmentPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

	private Handler handler;
	private final int MESSAGE_WHAT_CHANGED = 100;

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.lvyoudaohang, null);

		ImageView backImageView = (ImageView) view.findViewById(R.id.back_ImageView);
		backImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
			}
		});

		mViewPager = (ViewPager) view.findViewById(R.id.viewpager_head);
		mPagerAdapter = new MyFragmentPagerAdapter(this.getFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				set(pos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		final CircleIndicatorView mCircleIndicatorView = (CircleIndicatorView) view
				.findViewById(R.id.circleIndicatorView);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MESSAGE_WHAT_CHANGED:
					mCircleIndicatorView.setCircleCount(mPagerAdapter.getCount());
					mCircleIndicatorView.setCircleSelectedPosition(mViewPager.getCurrentItem());
					mCircleIndicatorView.drawCircleView();

					break;
				}
			};
		};

		// 初始化选择第一项
		if (mPagerAdapter.getCount() > 0) {
			set(0);
		}

		ListView lv = (ListView) view.findViewById(R.id.listView);
		ArrayAdapter mArrayAdapter = new MyArrayAdapter(this.getContext(), -1);
		lv.setAdapter(mArrayAdapter);
		final Context mContext = this.getContext();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mContext, SpotPlaceActivity.class);
				startActivity(intent);
			}
		});

		return view;
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private void back() {
		Utils.onKeyEvent(KeyEvent.KEYCODE_BACK);
	}

	private void set(int pos) {
		mViewPager.setCurrentItem(pos, true);
		handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
	}

	
	private class ImageFragment extends Fragment {
		
		private	String url = null;
		
		@Override
		public	void	onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			
			Bundle b = this.getArguments();
			url = b.getString("URL");
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);

			View view = inflater.inflate(R.layout.image_layout, null);
			ImageView image = (ImageView) view.findViewById(R.id.image);
			Log.d(this.getClass().getName(), "加载URL:"+url);
			Glide.with(getContext()).load(url).placeholder(R.drawable.loading).centerCrop().crossFade().into(image);

			return view;
		}
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		private ArrayList<String> mURLs = null;
		private	FragmentPagerAdapter mFragmentPagerAdapter;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			
			mFragmentPagerAdapter=this;

			mURLs = new ArrayList<String>();

			String url = "http://172.16.0.138:8080/AndTravel/area/getallarea/成都";
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(url, null, new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

					try {
						JSONObject jobj = (JSONObject) response.get("message");
						JSONArray ja = (JSONArray) jobj.get("info");
						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo = ja.getJSONObject(i);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					try {
						JSONObject jobj = (JSONObject) response.get("message");
						JSONObject jo = (JSONObject) jobj.get("content");

						Iterator<String> iter = jo.keys();
						while (iter.hasNext()) {
							String id = (String) iter.next();
							String s = jo.getString(id);
							JSONArray ja = new JSONArray(s);
							for (int i = 0; i < ja.length(); i++) {
								JSONObject joo = ja.getJSONObject(i);
								String url = joo.getString("contentUrl");
								
								Log.d(this.getClass().getName(), "添加URL:"+url);
								mURLs.add(url);
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					mFragmentPagerAdapter.notifyDataSetChanged();
				}
			});
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
		}

		@Override
		public Fragment getItem(int pos) {
			Log.d(this.getClass().getName(), "构建Fragment");
			
			Bundle b = new Bundle();
			b.putString("URL", mURLs.get(pos));

			Fragment f = new ImageFragment();
			f.setArguments(b);

			return f;
		}

		@Override
		public int getCount() {
			return mURLs.size();
		}
	}

	
	private class MyArrayAdapter extends ArrayAdapter {

		private LayoutInflater mLayoutInflater;

		private String[] distance = { "12.6km", "7.8km", "10.5km" };
		private int[] res = { R.drawable.wuhouci_small, R.drawable.caotang_samll, R.drawable.kuanzhaixiangzi_small };
		private String[] details = { "纪念三国时蜀汉丞相诸葛亮的祠堂。", "是目前我国最大、最完整的杜甫文物保护中心。", "走进了最成都、最世界、最古老、最时尚的老成都名片。" };
		private String[] placeNames = { "武侯祠", "杜甫草堂", "宽窄巷子" };

		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = mLayoutInflater.inflate(R.layout.lvyoudaolan_item, null);

			ImageView imageViewPlace = (ImageView) convertView.findViewById(R.id.imageViewPlace);
			imageViewPlace.setImageResource(res[pos]);

			TextView placeName = (TextView) convertView.findViewById(R.id.placeName);
			placeName.setText(placeNames[pos]);

			TextView detail = (TextView) convertView.findViewById(R.id.textViewDetail);
			detail.setText(details[pos]);

			TextView distancetv = (TextView) convertView.findViewById(R.id.textViewDistance);
			distancetv.setText(distance[pos]);

			return convertView;
		}

		@Override
		public int getCount() {
			return 3;
		}
	}
}
