package chinamobile.iot.andtravels;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import chinamobile.iot.andtravels.SettingFragment.ListViewAdapter;
import chinamobile.iot.andtravels.SettingFragment.ListViewAdapter.ListItemView;

public class PersonSettingFragment extends Fragment {

	private final String LOG_TAG = "PersonSettingActivity";
	private int listItemNum = 6;
	private ListViewAdapter listViewAdapter;
	private List<Map<String, Object>> mListItems;
	private Integer[] imgeIDs = { R.drawable.talkhead, R.drawable.talkhead, R.drawable.talkhead, R.drawable.talkhead,
			R.drawable.talkhead, R.drawable.talkhead };
	private String[] titleNames = { "头像", "个人资料", "昵称", "手机", "账号完全", "修改密码" };

	private Activity mActivity;
	private View mView;
	private AlertDialog dialog;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView = inflater.inflate(R.layout.personsetting, null);

		ListView listView = (ListView) mView.findViewById(R.id.PersonSetting);

		mListItems = getListItems();
		listViewAdapter = new ListViewAdapter(mActivity, mListItems); // 创建适配器
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					// 修改头像
					LayoutInflater inflater = LayoutInflater.from(mActivity);  
			        View choseImageView = inflater.inflate(R.layout.choose_image_dialog, null);
			        
					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); 
					builder.setView(choseImageView);
			        dialog = builder.create();
			       
			        
			        TextView view1 = (TextView)choseImageView.findViewById(R.id.item1);
			        view1.setOnClickListener(new android.view.View.OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							getImageFromAlbum();
						}
			        	
			        });
			        
			        choseImageView.findViewById(R.id.item2).setOnClickListener(new android.view.View.OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//getImageFromAlbum();
						}
			        	
			        });
			        
			        choseImageView.findViewById(R.id.item3).setOnClickListener(new android.view.View.OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
			        	
			        });
			        
			        dialog.show();
			        Window window = dialog.getWindow();         
			        window.setGravity(Gravity.BOTTOM);

				} else if (position == 1) {
					//

				} else if (position == 2) {
					// 修改昵称
					Intent intent = new Intent(mActivity, ChangeNameActivity.class);
					mActivity.startActivity(intent);

				} else if (position == 3) {
					// 修改手机号
					// Intent intent=new Intent(mActivity,
					// ChangePhoneNumActivity.class);
					// mActivity.startActivity(intent);

				} else if (position == 4) {

				} else if (position == 5) {

				} else {

				}
			}

		});

		// initTabMenuView();
		return mView;
	}

	private void getImageFromAlbum(){
		Intent intent = new Intent();  
        intent.setType("image/*");  
        intent.setAction(Intent.ACTION_GET_CONTENT);   
        startActivityForResult(intent, 1);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == Activity.RESULT_OK) {  
            Uri uri = data.getData();  
            Log.e("LOG_TAG", "获取到图片uri：" + uri.toString());  
            ContentResolver cr = mActivity.getContentResolver();  
            try {  
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
                CircleImageView imageView = (CircleImageView) mView.findViewById(R.id.manImage);  
                /* 将Bitmap设定到ImageView */ 
                imageView.setBackgroundResource(0);
                imageView.setImageBitmap(bitmap); 
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }  
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }  
	
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
			public CircleImageView manImage;
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
			} else if (p == 1 || p == 4) {
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
					convertView = inflater.inflate(R.layout.personset_special_list_item, parent, false);
					listItemView = new ListItemView();

					// 获取控件对象
					listItemView.title = (TextView) convertView.findViewById(R.id.title);
					listItemView.manImage = (CircleImageView) convertView.findViewById(R.id.manImage);
					listItemView.touchImage = (ImageView) convertView.findViewById(R.id.touchImage);
					// 设置控件集到convertView
					convertView.setTag(listItemView);
					break;
				}
				case TYPE_2: {
					convertView = inflater.inflate(R.layout.personsetlist_item, parent, false);
					listItemView = new ListItemView();

					// 获取控件对象
					listItemView.title = (TextView) convertView.findViewById(R.id.title);
					listItemView.touchImage = (ImageView) convertView.findViewById(R.id.touchImage);
					// 设置控件集到convertView
					convertView.setTag(listItemView);
					break;
				}
				case TYPE_3: {
					convertView = inflater.inflate(R.layout.devide_line_person_set, parent, false);
					listItemView = new ListItemView();

					// 获取控件对象
					listItemView.title = (TextView) convertView.findViewById(R.id.title);

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
			listItemView.title.setText((String) mListItems.get(position).get("title"));

			if (type == TYPE_1 && position == 0) {
				listItemView.manImage.setBackgroundResource((Integer) listItems.get(position).get("showImage"));
			}

			if (type == TYPE_1 || type == TYPE_2) {
				listItemView.touchImage.setBackgroundResource((Integer) listItems.get(position).get("touchImage"));
			}

			return convertView;
		}
	}

}
