package com.br.ipad.gsanas.adapters;

import java.util.ArrayList;
import java.util.Arrays;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ExecutedServiceAdapter extends ArrayAdapter<String> implements
		Filterable {
	
	private ArrayList<String> original;
	private ArrayList<String> suggestions;
	Context c;
	
	public ExecutedServiceAdapter(Context c, int textViewResourceId,String[] resultList){
		super(c, textViewResourceId,resultList);
		this.original = new ArrayList<String>(Arrays.asList(resultList));
		this.suggestions = (ArrayList<String>)this.original.clone();
		this.c = c;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
	
	
	@Override
	public int getCount(){
		return suggestions.size();
	}
	
	@Override
	public String getItem(int i){
		return suggestions.get(i);
	}
	
	@Override
	public Filter getFilter(){
		Filter filter = new Filter(){
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				
				FilterResults results = new FilterResults();
				ArrayList<String> resultList = new ArrayList<String>();
				
				if(constraint != null){
					for (String s : original){
						if(s.toLowerCase().contains(constraint.toString().toLowerCase())){
							resultList.add(s);
						}
					}
					
					results.count = resultList.size();
					results.values = resultList;
				}			
				
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				
				ArrayList<String> filteredList = (ArrayList<String>) results.values;
				suggestions.clear();
				
				if(results != null && results.count > 0) {
					for(String s: filteredList){
						suggestions.add(s);
					}
					notifyDataSetChanged();
				}
				else{
					notifyDataSetInvalidated();
				}
							
			}		
			
		};
		
		return filter;
	}
	

}
