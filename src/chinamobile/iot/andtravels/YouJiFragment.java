package chinamobile.iot.andtravels;

import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class YouJiFragment extends ListFragment {
	
	private	final	int	TEST_COUNT=50;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayAdapter adapter = new MyArrayAdapter(this.getContext(), -1);
		this.setListAdapter(adapter);
	}
	
	@Override
	public	void onListItemClick(ListView l, View v, int position, long id){
		Toast.makeText(getContext(),"点击:"+position,Toast.LENGTH_SHORT).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.youji_fragment, null);
		TextView tv=(TextView) v.findViewById(R.id.title_text);
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
			if(convertView==null)
				convertView=mLayoutInflater.inflate(R.layout.youji_fragment_item, null);
	
			RecyclerView mRecyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
			
			int spanCount = 3;
			RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
			  
			mRecyclerView.setLayoutManager(mLayoutManager);

			RecyclerView.Adapter imageAdapter = new ImageAdapter();
			mRecyclerView.setAdapter(imageAdapter);
			
			if(imageAdapter.getItemCount() == 0)
				mRecyclerView.setVisibility(View.GONE);
			else
				mRecyclerView.setMinimumHeight(200);
			
			return convertView;
		}

		@Override
		public int getCount() {
			return TEST_COUNT;
		}
	}
	

	public class ImageAdapter extends RecyclerView.Adapter<MyViewHolder> {

		private	final	int	COUNT;
		
		public	ImageAdapter(){
			Random rand=new Random();
			COUNT=rand.nextInt(10);
		}
		
		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			View view = View.inflate(getContext(),R.layout.image_layout, null);  

			MyViewHolder myViewHolder = new MyViewHolder(view);
			myViewHolder.img=(ImageView) view.findViewById(R.id.image);
			
			return myViewHolder;
		}

		@Override
		public int getItemViewType(int position) {
			return super.getItemViewType(position);
		}

		@Override
		public void onBindViewHolder(MyViewHolder viewHolder, int position) {
			viewHolder.position = position;
			viewHolder.img.setImageResource(R.drawable.caotang_samll);
		}

		@Override
		public int getItemCount() {
			return COUNT;
		}
	}

	private class MyViewHolder extends RecyclerView.ViewHolder {
		
		public ImageView img;

		// 埋入一个值position,记录在RecyclerView的位置。
		private int position;

		public MyViewHolder(View itemView) {
			super(itemView);
		}
	}

	
}
