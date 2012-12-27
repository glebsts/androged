package ee.ut.ta.ui;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ee.ut.ta.R;
import ee.ut.ta.dict.IDictionary;

public class DictionaryAdapter extends ArrayAdapter<IDictionary> {

	static final String TAG = "ged.da"; // tag for LogCat
	private List<IDictionary> items;

	public DictionaryAdapter(Context context, int resource,
			int textViewResourceId, List<IDictionary> pItems) {
		super(context, resource, textViewResourceId, pItems);
		this.items = pItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		try {
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.dict_spinner_row, null);
			}
			IDictionary item = items.get(position);
			if (item != null) {
				TextView tt = (TextView) v.findViewById(R.id.dictName);
				if (tt != null) {
					tt.setText(item.getName());
				}

				tt = (TextView) v.findViewById(R.id.dictDescription);
				if (tt != null) {
					tt.setText(item.getDescription());
				}
			}
		} catch (Exception exc) {
			Log.e(TAG, exc.getMessage());

		}

		return v;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		try {
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.dict_spinner_row, null);
			}
			IDictionary item = items.get(position);
			if (item != null) {
				TextView tt = (TextView) v.findViewById(R.id.dictName);
				if (tt != null) {
					tt.setText(item.getName());
				}

				tt = (TextView) v.findViewById(R.id.dictDescription);
				if (tt != null) {
					tt.setText(item.getDescription());
				}
			}
		} catch (Exception exc) {
			Log.e(TAG, exc.getMessage());

		}

		return v;
	}

}
