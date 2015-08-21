package chinamobile.iot.andtravels;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LvYouDaoLan extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view=inflater.inflate(R.layout.lvyoudaohang, null);
		
		ListView lv=(ListView)view.findViewById(R.id.listView);
		ArrayAdapter mArrayAdapter=new MyArrayAdapter(this.getContext(),-1);
		lv.setAdapter(mArrayAdapter);
		
		return view;
	}
	
	private	class	MyArrayAdapter	extends	ArrayAdapter{

		private	LayoutInflater mLayoutInflater;
		
		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);
			
			mLayoutInflater=LayoutInflater.from(context);
		}
		
		@Override
		public	View	getView(int position, View convertView, ViewGroup parent){
			if(convertView==null)
				convertView=mLayoutInflater.inflate(R.layout.lvyoudaolan_item, null);
			
			return	convertView;
		}
		
		@Override
		public	int	getCount(){
			return	3;
		}
	}	
}
