package chinamobile.iot.andtravels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class SettingFragment extends Fragment {

	private final String LOG_TAG = "SettingActivity";
	private Activity mActivity = null;
	private int listItemNum = 9;
	private ListViewAdapter listViewAdapter;
	private List<Map<String, Object>> listItems;
	private ShareDialog mPopDialog;
	
	private Integer[] imgeIDs = { R.drawable.darkhead, R.drawable.darkhead, R.drawable.share1, R.drawable.save,
			R.drawable.save, R.drawable.save, R.drawable.talk, R.drawable.talk, R.drawable.share1 };
	private String[] titleNames = { "昵称", "", "我的分享", "我的景点收藏", "", "我的状态收藏", "我的评论", "", "分享App" };

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, null);

		ListView listView = (ListView) view.findViewById(R.id.ShareSetting);

		listItems = getListItems();
		listViewAdapter = new ListViewAdapter(mActivity, listItems); // 创建适配器
		listView.setAdapter(listViewAdapter);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent;
				if (position == 0) {
					
				}else if (position == 2  ) {
					Log.e(LOG_TAG, "我的分享行被点击！！！！！！！！！！！！");
					intent = new Intent(mActivity,GeneralActivity.class);
					intent.putExtra("title", "我的分享");
					mActivity.startActivity(intent);

				}else if (position == 3) {
					Log.e(LOG_TAG, "我的景点收藏行被点击！！！！！！！！！！！！");
					intent = new Intent(mActivity,GeneralActivity.class);
					intent.putExtra("title", "我的景点收藏");
					mActivity.startActivity(intent);
				}else if (position == 5) {
					Log.e(LOG_TAG, "我的状态收藏行被点击！！！！！！！！！！！！");
					intent = new Intent(mActivity,GeneralActivity.class);
					intent.putExtra("title", "我的状态收藏");
					mActivity.startActivity(intent);
				}else if(position == 6){
					Log.e(LOG_TAG, "我的评论行被点击！！！！！！！！！！！！");
					intent = new Intent(mActivity,GeneralActivity.class);
					intent.putExtra("title", "我的评论");
					mActivity.startActivity(intent);
				}else if(position == 8 ){
					Log.e(LOG_TAG, "分享App行被点击！！！！！！！！！！！！");
					
					mPopDialog = new ShareDialog(mActivity);
					mPopDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					mPopDialog.show();  
	                

				}else{
					
				}
			}

		});

		return view;
	}

	/**
	 * 初始化商品信息
	 */
	private List<Map<String, Object>> getListItems() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < listItemNum; i++) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("title", titleNames[i]);
			map.put("showImage", imgeIDs[i]);
			map.put("touchImage", R.drawable.click);

			listItems.add(map);
		}

		return listItems;
	}

	public class ListViewAdapter extends BaseAdapter {
		LayoutInflater inflater;
		private Context context; // 运行上下文
		private List<Map<String, Object>> listItems; // 商品信息集合
		private LayoutInflater listContainer; // 视图容器

		public final class ListItemView { // 自定义控件集合
			public ImageView showImage;
			public TextView title;
			public ImageView touchImage;
		}

		private final int TYPE_1 = 0;
		private final int TYPE_2 = 1;
		private final int TYPE_3 = 2;

		public ListViewAdapter(Context context, List<Map<String, Object>> listItems) {
			this.context = context;
			listContainer = LayoutInflater.from(context);
			this.listItems = listItems;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			int p = position;
			if (p == 0) {
				return TYPE_1;
			} else if (p == 1 || p == 4 || p == 7) {
				return TYPE_3;
			} else {
				return TYPE_2;
			}

		}

		@Override
		public int getViewTypeCount() {
			return 3;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//Log.i("method", "getView");
			final int selectID = position;
			// 自定义视图
			ListItemView listItemView = null;
			int type = getItemViewType(position);

			if (convertView == null) {
				inflater = LayoutInflater.from(context);
				switch (type) {
				case TYPE_1: {
					convertView = inflater.inflate(R.layout.special_list_item, parent, false);
					listItemView = new ListItemView();

					// 获取控件对象
					listItemView.showImage = (ImageView) convertView.findViewById(R.id.personImage);
					listItemView.title = (TextView) convertView.findViewById(R.id.name);
					listItemView.touchImage = (ImageView) convertView.findViewById(R.id.touchImage);
					// 设置控件集到convertView
					convertView.setTag(listItemView);
					break;
				}
				case TYPE_2: {
					convertView = inflater.inflate(R.layout.list_item, parent, false);
					listItemView = new ListItemView();

					// 获取控件对象
					listItemView.showImage = (ImageView) convertView.findViewById(R.id.showImage);
					listItemView.title = (TextView) convertView.findViewById(R.id.title);
					listItemView.touchImage = (ImageView) convertView.findViewById(R.id.touchImage);
					// 设置控件集到convertView
					convertView.setTag(listItemView);
					break;
				}
				case TYPE_3: {
					convertView = inflater.inflate(R.layout.divede_line, parent, false);
					listItemView = new ListItemView();

					// 设置控件集到convertView
					convertView.setTag(listItemView);
					break;
				}
				default: {

					break;
				}
				}

			} else {
				listItemView = (ListItemView) convertView.getTag();
			}

			// 设置文字和图片
			if (type == TYPE_3) {

			} else {
				listItemView.showImage.setBackgroundResource((Integer) listItems.get(position).get("showImage"));
				listItemView.title.setText((String) listItems.get(position).get("title"));
				listItemView.touchImage.setBackgroundResource((Integer) listItems.get(position).get("touchImage"));

			}

			return convertView;
		}
	}

	/*private void initTabMenuView() {
		FragmentManager mfragmentManager = this.getFragmentManager();// getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = mfragmentManager.beginTransaction();
		TabMenuFragment fragment1 = new TabMenuFragment();
		fragmentTransaction.replace(R.id.tab_menu_frame, fragment1);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

	}*/
	
}

