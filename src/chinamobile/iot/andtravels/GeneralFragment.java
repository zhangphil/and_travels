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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GeneralFragment extends Fragment {

	private final String LOG_TAG = "MyShareFragment";
	private Activity mActivity = null;
	private int listItemNum = 9;
	private ListViewAdapter listViewAdapter;
	private List<Map<String, Object>> listItems;
	private Integer[] imgeIDs = { R.drawable.caotang_samll, R.drawable.kuanzhaixiangzi_small, R.drawable.kuanzhaixiangzi_small, 
			R.drawable.kuanzhaixiangzi_small,R.drawable.kuanzhaixiangzi_small, R.drawable.kuanzhaixiangzi_small, 
			R.drawable.kuanzhaixiangzi_small, R.drawable.kuanzhaixiangzi_small, R.drawable.kuanzhaixiangzi_small };
	private String[] titleNames = { "宽巷子、窄巷子和井巷子", "杜甫草堂", "青羊宫", "峨眉山", "西岭雪山", "济州岛", "关岛", "巴厘岛", "伦敦、巴黎和都灵" };
	private String[] time = { "2015-8-4", "2015-7-28", "2012-8-4", "2006-8-4", "2003-8-4", "2002-8-4", "1999-8-4", "2000-8-4", "2014-8-4" };

	private ImageView backView;
	private TextView  mTextViewName;
	private String mTitle;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	public GeneralFragment(String title){
		mTitle = title;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.my_share, null);
		mTextViewName =(TextView) view.findViewById(R.id.name);
		mTextViewName.setText(mTitle);

		ListView listView = (ListView) view.findViewById(R.id.myShareList);
		backView = (ImageView) view.findViewById(R.id.back);
		listItems = getListItems();
		listViewAdapter = new ListViewAdapter(mActivity, listItems); 
		listView.setAdapter(listViewAdapter);
	
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();

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

			map.put("showImage", imgeIDs[i]);
			map.put("title", titleNames[i]);
			map.put("time", time[i]);
			
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
			public TextView travelName;
			public TextView time;
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
			return TYPE_1;
			
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ListItemView listItemView = null;
			if(convertView == null) {
				inflater = LayoutInflater.from(context);
				
					convertView = inflater.inflate(R.layout.my_share_list_item, parent, false);
					listItemView = new ListItemView();

					// 获取控件对象
					listItemView.showImage = (ImageView) convertView.findViewById(R.id.travelImage);
					listItemView.travelName = (TextView) convertView.findViewById(R.id.travelName);
					listItemView.time = (TextView) convertView.findViewById(R.id.time);
					// 设置控件集到convertView
					convertView.setTag(listItemView);
				

			}else {
				listItemView = (ListItemView) convertView.getTag();
			}

			listItemView.showImage.setBackgroundResource((Integer) listItems.get(position).get("showImage"));
			listItemView.travelName.setText((String) listItems.get(position).get("title"));
			listItemView.time.setText((String) listItems.get(position).get("time"));
			
			TextView openView = (TextView)convertView.findViewById(R.id.open);
			openView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Intent intent = new Intent(getActivity(), MainActivity.class);
					//getActivity().startActivity(intent);
				}
				
			});

			return convertView;
		}
	}

	public void SetResource(){
		
	}
}
