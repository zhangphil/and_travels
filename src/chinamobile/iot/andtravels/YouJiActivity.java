package chinamobile.iot.andtravels;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;


public class YouJiActivity extends Activity{

	@Override
	public	void	onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		LayoutInflater mLayoutInflater=LayoutInflater.from(this);
		
		View view=View.inflate(this, R.layout.youji_activity, null);
		
		ListView listView=(ListView) view.findViewById(android.R.id.list);
		ArrayAdapter adapter=new MyArrayAdapter(this,-1);
		listView.setAdapter(adapter);
		listView.setHeaderDividersEnabled(false);
		
		View v=mLayoutInflater.inflate(R.layout.youji_item, null);
		listView.addHeaderView(v);
		
		TableLayout tableLayout = (TableLayout) v.findViewById(R.id.tableLayout);
		final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
		int total = 6;
		int ROW=0;
		int mod=total%3;
		if(mod==0)
			ROW=total/3;
		else
			ROW=total/3+1;
		
		int k = 0;
		for (int i = 0; i < ROW; i++) {
			TableRow tableRow = new TableRow(this);
			
			for (int j = 0; j < 3; j++) {
				if (k < total) {
					View vv = mLayoutInflater.inflate(R.layout.image_layout, null);
					tableRow.addView(vv);
					
					k++;
				}
			}
			
			tableLayout.addView(tableRow, new TableLayout.LayoutParams(WC, WC));
		}
		
		setContentView(view);
	}
	
	private	class	MyArrayAdapter	extends	ArrayAdapter{

		private LayoutInflater mLayoutInflater;

		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			if(convertView==null)
				convertView = mLayoutInflater.inflate(R.layout.comment_item, null);
			return convertView;
		}

		@Override
		public int getCount() {
			return 10;
		}
	}
}
