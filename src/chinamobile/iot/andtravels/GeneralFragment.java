package chinamobile.iot.andtravels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.bumptech.glide.Glide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import chinamobile.iot.andtravels.StartActivity.ImageBitmapCache;

public class GeneralFragment extends Fragment {

	private final String LOG_TAG = "MyShareFragment";
	private Activity mActivity = null;
	private int listItemNum = 9;
	private ListViewAdapter listViewAdapter;
	private List<Map<String, Object>> listItems;
	//为了demo版本，临时增加的一些数据
	private Integer[] imgeIDs = { R.drawable.caotang_samll, R.drawable.kuanzhaixiangzi_small, R.drawable.kuanzhaixiangzi_small, 
			R.drawable.kuanzhaixiangzi_small,R.drawable.kuanzhaixiangzi_small, R.drawable.kuanzhaixiangzi_small, 
			R.drawable.kuanzhaixiangzi_small, R.drawable.kuanzhaixiangzi_small, R.drawable.kuanzhaixiangzi_small };
	private String[] titleNames = { "宽巷子、窄巷子和井巷子", "杜甫草堂", "青羊宫", "峨眉山", "西岭雪山", "济州岛", "关岛", "巴厘岛", "伦敦、巴黎和都灵" };
	private String[] time = { "2015-8-4", "2015-7-28", "2012-8-4", "2006-8-4", "2003-8-4", "2002-8-4", "1999-8-4", "2000-8-4", "2014-8-4" };

	private ImageView backView;
	private TextView  mTextViewName;
	private String mTitle;
	private boolean mTest = true;
	
	//从服务器上获取收藏的数据
	private ImageLoader mImageLoader;
	private RequestQueue mQueue;
	private ImageBitmapCache mImageCache = new ImageBitmapCache();
	

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
		
		if(mTest){
			listItems = getListItems();
			listViewAdapter = new ListViewAdapter(mActivity, listItems); 
		}else{
			mQueue = Volley.newRequestQueue(getActivity());
			mImageLoader = new ImageLoader(mQueue, mImageCache);
			ArrayList<ItemView> sourceList = new ArrayList<ItemView>();
			fetchSource(sourceList);
			listViewAdapter = new ListViewAdapter(sourceList); 
		}
		
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
		private Context context; 
		private List<Map<String, Object>> listItems; 
		private List<ItemView> listViewItems;
		private LayoutInflater listContainer; 

		public final class ListItemView { 
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

		public ListViewAdapter(List<ItemView> listItems) {
			this.context = getActivity();
			listContainer = LayoutInflater.from(context);
			this.listViewItems = listItems;
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			if(mTest){
				return listItems.size();
			}else{
				return listViewItems.size();
			}
			
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

			if(mTest){
				listItemView.showImage.setBackgroundResource((Integer) listItems.get(position).get("showImage"));
				listItemView.travelName.setText((String) listItems.get(position).get("title"));
				listItemView.time.setText((String) listItems.get(position).get("time"));
				
			}else{
				ImageListener listener = null;
				listener = ImageLoader.getImageListener(listItemView.showImage,0, 0);
				mImageLoader.get(listViewItems.get(position).get("imageUrl"), listener); 
				
				Glide.with(context).load(listViewItems.get(position).get("imageUrl")).placeholder(R.drawable.loading).crossFade(1000).centerCrop().into(listItemView.showImage);  
				//listItemView.showImage.setBackgroundResource((Integer) listItems.get(position).get("showImage"));
				listItemView.travelName.setText((String)listViewItems.get(position).get("name"));
				listItemView.time.setText((String) listViewItems.get(position).get("time"));
				
			}
			
			TextView openView = (TextView)convertView.findViewById(R.id.open);
			openView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity, SpotPlaceActivity.class);
					startActivity(intent);
				}
				
			});

			return convertView;
		}
	}

	public final class ItemView{
		public String imageUrl;
		public String name;
		public String time;
		public String get(String string) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
private void fetchSource(final ArrayList<ItemView> sourceList){
		
		String url = "http://172.16.0.138:8080/AndTravel/content/introductory/";
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getString("code").equals("1")) {
			        	JSONArray contentArray = response.getJSONArray("message");
			        	if(contentArray.length() > 0){
			        		for(int i = 0; i < contentArray.length(); i++){	
			        			ItemView item = new ItemView();
			        			item.imageUrl = contentArray.get(i).toString();
		        				item.name = contentArray.get(i).toString();
		        				item.time = contentArray.get(i).toString();
		        				sourceList.add(item);	
			        		}
	
						} else {
							Toast.makeText(getActivity(),"GeneralFragment从服务器上资源为空！！！", Toast.LENGTH_SHORT).show();
						}
					}
				} catch (Exception e) {
					Log.e(LOG_TAG, e.toString());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(LOG_TAG, "error = " + error.toString());
				Log.e(LOG_TAG, "GeneralFragment 没有从服务器上获取到资源！！！！");
			}
		});

		mQueue.add(jsonObjectRequest);
			
	}

	public class ImageBitmapCache implements ImageCache {  
		  
	    private LruCache<String, Bitmap> mCache;  
	  
	    public ImageBitmapCache() {  
	        int maxSize = 2 * 1024 * 1024;  
	        mCache = new LruCache<String, Bitmap>(maxSize) {  
	            @Override  
	            protected int sizeOf(String key, Bitmap bitmap) {  
	                return bitmap.getRowBytes() * bitmap.getHeight();  
	            }  
	        };  
	    }  
	  
	    @Override  
	    public Bitmap getBitmap(String url) {  
	    	Log.e(LOG_TAG, "从缓存中获取图片资源！！！");
	    	if( mCache.get(url) == null ){
	    		Log.e(LOG_TAG, "从缓存中获取图片资源为空！！！！！");
	    	}else{
	    		Log.e(LOG_TAG, "从缓存中获取到了图片资源！！！！！");
	    	}
	        return mCache.get(url);  
	    }  
	  
	    @Override  
	    public void putBitmap(String url, Bitmap bitmap) { 
	    	Log.e(LOG_TAG, "往缓存中添加图片资源！！！");
	        mCache.put(url, bitmap);  
	    }  
	  
	} 
	
}
