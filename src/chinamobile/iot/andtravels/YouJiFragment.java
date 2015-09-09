package chinamobile.iot.andtravels;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class YouJiFragment extends ListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Random rand = new Random();
		ArrayList<Integer> mArrayList = new ArrayList<Integer>();
		for (int i = 0; i < 50; i++) {
			int cnt = rand.nextInt(10);
			mArrayList.add(cnt);
		}

		ArrayAdapter adapter = new MyArrayAdapter(getContext(), -1, mArrayList);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getContext(), YouJiActivity.class);
		startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.youji_fragment, null);
		TextView tv = (TextView) v.findViewById(R.id.title_text);
		tv.setText("游记");
		return v;
	}

	private class MyArrayAdapter extends ArrayAdapter {

		private ArrayList<Integer> mItems;
		private LayoutInflater mLayoutInflater;
		private ViewHolder holder;

		private class ViewHolder {
			public TableLayout tableLayout;
			public TextView title;
			public TextView detail;
		}

		public MyArrayAdapter(Context context, int resource, ArrayList<Integer> mArrayList) {
			super(context, resource);
			this.mLayoutInflater = LayoutInflater.from(context);
			this.mItems = mArrayList;
		}

		// 注意！！！此处没有对性能进行优化！后期必须对此进行优化！
		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.youji_item, null);

				TextView title = (TextView) convertView.findViewById(R.id.title);
				TextView detail = (TextView) convertView.findViewById(R.id.detail);
				TableLayout tableLayout = (TableLayout) convertView.findViewById(R.id.tableLayout);

				holder = new ViewHolder();

				holder.title = title;
				holder.detail = detail;
				holder.tableLayout = tableLayout;
				
				holder.detail.setEllipsize(TruncateAt.END);
				holder.detail.setMaxLines(3);

				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.title.setText("初游草堂，忆童年 （" + pos + "），" + mItems.get(pos) + "图");

			int total = getItem(pos);

			if (total == 0) {
				holder.tableLayout.setVisibility(View.GONE);
			} else {
				holder.tableLayout.removeAllViewsInLayout();
				holder.tableLayout.setVisibility(View.VISIBLE);

				int ROW = 0;
				int mod = total % 3;
				if (mod == 0)
					ROW = total / 3;
				else
					ROW = total / 3 + 1;

				int k = 0;
				for (int i = 0; i < ROW; i++) {
					TableRow tableRow = new TableRow(getContext());

					for (int j = 0; j < 3; j++) {
						if (k < total) {
							View v = mLayoutInflater.inflate(R.layout.image_layout, null);
							tableRow.addView(v);

							k++;
						}
					}

					holder.tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				}
			}

			return convertView;
		}

		@Override
		public Integer getItem(int pos) {
			return (Integer) mItems.get(pos);
		}

		@Override
		public int getCount() {
			return mItems.size();
		}
	}

	// public class ImageAdapter extends ArrayAdapter {
	//
	// private LayoutInflater mLayoutInflater;
	// private int[] images;
	//
	// public ImageAdapter(Context context, int resource, int[] images) {
	// super(context, resource);
	// mLayoutInflater = LayoutInflater.from(context);
	// this.images = images;
	// }
	//
	// @Override
	// public View getView(int pos, View convertView, ViewGroup parent) {
	// if (convertView == null)
	// convertView = mLayoutInflater.inflate(R.layout.image_layout, null);
	//
	// ImageView image = (ImageView) convertView.findViewById(R.id.image);
	// image.setImageResource(images[pos]);
	//
	// return convertView;
	// }
	//
	// @Override
	// public int getCount() {
	// return images.length;
	// }
	// }

}
