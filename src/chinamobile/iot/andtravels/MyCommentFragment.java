package chinamobile.iot.andtravels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import chinamobile.iot.andtravels.SettingFragment.ListViewAdapter;
import chinamobile.iot.andtravels.SettingFragment.ListViewAdapter.ListItemView;

public class MyCommentFragment extends Fragment {

	private final String LOG_TAG = "SettingActivity";
	private Activity mActivity = null;
	private int listItemNum = 1;
	private ListViewAdapter listViewAdapter;
	private List<Map<Object, List<Map<String, Object>>>> listItems;
	private String mTitle;
	private TextView  mTextViewName;
	private ImageView backView;
	
	private String[] TravelPos = { "宽窄巷子" };
	private Integer[] imgeIDs = { R.drawable.kuanzhaixiangzi_small };
	private String[] Traveltitle = { "宽巷子、窄巷子和井巷子" };
	private String[] Traveltime = { "2015-8-4" };
	private String[] commentPerson = {"小小","洋洋"};
	private String[] comment = {"赞一个！！","我也攒一个！！！"};
	private String[] commentTime = {"昨天 22:00","昨天 12:00"};
	private Integer[] imgePersonIDs = { R.drawable.talkhead,R.drawable.talkhead };
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	public MyCommentFragment(String title){
		mTitle = title;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_share, null);

		mTextViewName =(TextView) view.findViewById(R.id.name);
		mTextViewName.setText(mTitle);

		ListView listView = (ListView) view.findViewById(R.id.myShareList);

		listItems = getListItems();
		listViewAdapter = new ListViewAdapter(mActivity, listItems); 
		listView.setAdapter(listViewAdapter);
		
		backView = (ImageView) view.findViewById(R.id.back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();

			}
		});
	
		return view;
	}

	private List<Map<Object, List<Map<String, Object>>>> getListItems() {
		List<Map<Object,List<Map<String,Object>>>> listItems = new ArrayList<Map<Object, List<Map<String,Object>>>>();

		for (int i = 0; i < listItemNum; i++) {
			Map<Object,List<Map<String,Object>>> mapTravelPos = new HashMap<Object,List<Map<String,Object>>>();
			
			List<Map<String,Object>> listTravel = new ArrayList<Map<String,Object>>();
			for( int j = 0; j < 3; j++){
				Map<String, Object> map = new HashMap<String, Object>();
				if( j == 0 ){
					map.put("travelPosImage", imgeIDs[j]);
					map.put("title", Traveltitle[j]);
					map.put("time", Traveltime[j]);
				}else{
					map.put("personImage", imgePersonIDs[j-1]);
					map.put("personName", commentPerson[j-1]);
					map.put("comment", comment[j-1]);
					map.put("commentTime", commentTime[j-1]);
				}
				
				listTravel.add(map);
			}
			
			mapTravelPos.put("travelPos", listTravel);
			
			listItems.add(mapTravelPos);
		}

		return listItems;
	}

	public class ListViewAdapter extends BaseAdapter {
		LayoutInflater inflater;
		private Context context; 
		private List<Map<Object, List<Map<String, Object>>>> listItems; 
		private LayoutInflater listContainer; 

		public final class ListItemView { 
			public ImageView travelPosImage;
			public TextView title;
			public TextView time;
			
			public ImageView personImage;
			public TextView personName;
			public TextView comment;
			public TextView commentTime;
			
		}

		private final int TYPE_1 = 0;
		private final int TYPE_2 = 1;

		public ListViewAdapter(Context context, List<Map<Object,List<Map<String,Object>>>> listItems2) {
			this.context = context;
			listContainer = LayoutInflater.from(context);
			this.listItems = listItems2;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
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
			int listNum = 0, typeNum = TYPE_1;
			
			for(int i = 0; i < listItems.size(); i++){
				
				if(i == 0){
					if (position == 0) {
						typeNum = TYPE_1;
					}else {
						typeNum = TYPE_2;
					}
				}else{
					List<Map<String, Object>> commentList = (List<Map<String, Object>>) listItems.get(i-1);
					listNum += commentList.size();
					if (position == listNum) {
						typeNum = TYPE_1;
					}else {
						typeNum = TYPE_2;
					}
				}
				
			}
			
			return typeNum;

		}

		@Override
		public int getViewTypeCount() {
			return 2;
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
			
			//Log.e(LOG_TAG, "current list type is " + type + "pos is" + position);
			if (convertView == null) {
				inflater = LayoutInflater.from(context);
				switch (type) {
				case TYPE_1: {
					convertView = inflater.inflate(R.layout.my_share_list_item, parent, false);
					listItemView = new ListItemView();

					// 获取控件对象
					listItemView.travelPosImage = (ImageView) convertView.findViewById(R.id.travelImage);
					listItemView.title = (TextView) convertView.findViewById(R.id.travelName);
					listItemView.time = (TextView) convertView.findViewById(R.id.time);
					// 设置控件集到convertView
					convertView.setTag(listItemView);
					break;
				}
				case TYPE_2: {
					convertView = inflater.inflate(R.layout.my_comment_list_item, parent, false);
					listItemView = new ListItemView();

					// 获取控件对象
					listItemView.personImage = (ImageView) convertView.findViewById(R.id.personImage);
					listItemView.personName = (TextView) convertView.findViewById(R.id.personName);
					listItemView.comment = (TextView) convertView.findViewById(R.id.comment);
					listItemView.time = (TextView) convertView.findViewById(R.id.time);
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

			if(type == TYPE_1){
				List<Map<String, Object>> commentList = (List<Map<String, Object>>) listItems.get(position).get("travelPos");
				
				listItemView.travelPosImage.setBackgroundResource((Integer)commentList.get(position).get("travelPosImage"));
				listItemView.title.setText((String) commentList.get(position).get("title"));
				listItemView.time.setText((String) commentList.get(position).get("time"));
				
				TextView openView = (TextView)convertView.findViewById(R.id.open);
				openView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(mActivity, SpotPlaceActivity.class);
						startActivity(intent);
					}
					
				});
				
				
			}else{
				// 设置文字和图片

				List<Map<String, Object>> commentList = (List<Map<String, Object>>) listItems.get(0).get("travelPos");
				listItemView.personImage.setBackgroundResource((Integer)commentList.get(position).get("personImage"));
				listItemView.personName.setText((String) commentList.get(position).get("personName"));
				listItemView.comment.setText((String) commentList.get(position).get("comment"));
				listItemView.time.setText((String) commentList.get(position).get("commentTime"));
			}
			

			return convertView;
		}
	}

}
