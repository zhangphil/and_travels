package chinamobile.iot.andtravels;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class YouJiFragment extends ListFragment {

	private ArrayList<Integer> mArrayList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayAdapter adapter = new MyArrayAdapter(this.getContext(), -1);
		this.setListAdapter(adapter);

		Random rand = new Random();
		mArrayList = new ArrayList<Integer>();
		for (int i = 0; i < 50; i++) {
			int cnt = rand.nextInt(10);
			mArrayList.add(cnt);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getContext(), "点击:" + position, Toast.LENGTH_SHORT).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.youji_fragment, null);
		TextView tv = (TextView) v.findViewById(R.id.title_text);
		tv.setText("游记");
		return v;
	}

	private class MyArrayAdapter extends ArrayAdapter {

		private LayoutInflater mLayoutInflater;

		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			// if(convertView==null)
			convertView = mLayoutInflater.inflate(R.layout.youji_fragment_item, null);

			TextView title = (TextView) convertView.findViewById(R.id.title);
			title.setText("初游草堂，忆童年 （" + pos + "），图" + mArrayList.get(pos));

			TableLayout tableLayout = (TableLayout) convertView.findViewById(R.id.tableLayout);
			//tableLayout.setStretchAllColumns(true);

			final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
			//final int MP = ViewGroup.LayoutParams.MATCH_PARENT;

			int total = mArrayList.get(pos);
		
			int ROW=0;
			int mod=total%3;
			if(mod==0)
				ROW=total/3;
			else
				ROW=total/3+1;
			
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
				
				tableLayout.addView(tableRow, new TableLayout.LayoutParams(WC, WC));
			}

			return convertView;
		}

		@Override
		public int getCount() {
			return mArrayList.size();
		}
	}

	public class ImageAdapter extends ArrayAdapter {

		private LayoutInflater mLayoutInflater;
		private int[] images;

		public ImageAdapter(Context context, int resource, int[] images) {
			super(context, resource);
			mLayoutInflater = LayoutInflater.from(context);
			this.images = images;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = mLayoutInflater.inflate(R.layout.image_layout, null);

			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			image.setImageResource(images[pos]);

			return convertView;
		}

		@Override
		public int getCount() {
			return images.length;
		}
	}

}
