package ee.ut.ta.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import ee.ut.ta.R;
import ee.ut.ta.search.SearchResult;

public class SearchResultExpandableListAdapter extends BaseExpandableListAdapter {

	private ArrayList<SearchResultGroup> mGroups;
    private Context mContext;

    private String[] groupTitles = new String[]{"Exact","Beginning", "Middle", "Ending"};
    
    
	public SearchResultExpandableListAdapter(List<SearchResult> pItems, Context pCtx){
		processGroups(pItems);
		this.mContext = pCtx;
	}
	
	private void processGroups(List<SearchResult> items) {
		this.mGroups = new ArrayList<SearchResultGroup>();
		SearchResultGroup grp = new SearchResultGroup();
		for(int i=0;i<items.size();i++){
			
			grp.add(items.get(i));
			if(grp.size()==1){
				grp.setTitle(this.groupTitles[grp.get(0).getType() - 1]);
			}
				
			if(i<items.size()-1){
				if(items.get(i).getType() != items.get(i+1).getType()){
					mGroups.add(grp);
					grp = new SearchResultGroup();
				}
			}
		}
		if(grp.size()>0){
			mGroups.add(grp);
		}
		
	}

	public Object getChild(int groupPosition, int childPosition) {
		
		return mGroups.get(groupPosition).get(childPosition);

	}

	public long getChildId(int groupPosition, int childPosition) {
		
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_view, null);
        }

        TextView textWord = (TextView) convertView.findViewById(R.id.lblResultWord);
        textWord.setText(mGroups.get(groupPosition).get(childPosition).getWord());
        TextView textDistance = (TextView) convertView.findViewById(R.id.lblResultDescription);
        textDistance.setText(String.format("%1.2f",mGroups.get(groupPosition).get(childPosition).getDistance()));
         

        return convertView;

	}

	public int getChildrenCount(int groupPosition) {
		
		return mGroups.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		
		return mGroups.get(groupPosition);
	}

	public int getGroupCount() {
		
		return mGroups.size();
	}

	public long getGroupId(int groupPosition) {
		
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_view, null);
        }

		String grpTitle = this.getGroupTitle(groupPosition);
		
        if (isExpanded){
           grpTitle = String.format("%s", grpTitle);
        }
        else{
        	grpTitle = String.format("%s", grpTitle);
        }

        TextView textGroup = (TextView) convertView.findViewById(R.id.lblGroup);
        textGroup.setText(grpTitle);

        return convertView;


	}

	private String getGroupTitle(int groupPosition) {
		return ((SearchResultGroup) this.getGroup(groupPosition)).getTitle();
	}

	public boolean hasStableIds() {
		
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return true;
	}

}
