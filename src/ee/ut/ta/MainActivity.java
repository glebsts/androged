package ee.ut.ta;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import ee.ut.ta.dict.AssetDictionaryStorage;
import ee.ut.ta.dict.IDictionary;
import ee.ut.ta.search.SearchOptions;
import ee.ut.ta.ui.DictionaryAdapter;

public class MainActivity extends Activity implements OnItemSelectedListener {

	static final String TAG = "ged.main"; // tag for LogCat
	// menu items
	private static final int MNU_EXIT = 102;
	// dictionaries
	AssetDictionaryStorage assetDictionaryStorage;
	List<IDictionary> dictionaries;

	// search options
	SearchOptions searchOptions = new SearchOptions();

	// screen controls
	Spinner cmbDictionary;
	Button btnSearchOptions;

	// adapters
	DictionaryAdapter dictionaryAdapter;
	private int selectedDictionary;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dictionaries = (assetDictionaryStorage = new AssetDictionaryStorage(
				getApplicationContext())).getDictionaries();
		Log.d(TAG, "Loaded dict size = " + dictionaries.size());
		cmbDictionary = (Spinner) findViewById(R.id.cmbDictionary);
		dictionaryAdapter = new DictionaryAdapter(this,
				R.layout.dict_spinner_row, R.id.dictName, dictionaries);
		cmbDictionary.setAdapter(dictionaryAdapter);
		cmbDictionary.setOnItemSelectedListener(this);

		btnSearchOptions = (Button) findViewById(R.id.btnSearchOptions);
		btnSearchOptions.setOnClickListener(searchOptionsClickHandler);
	}

	Button.OnClickListener searchOptionsClickHandler = new Button.OnClickListener() {
		public void onClick(View v) {

			final Dialog dialog = new Dialog(MainActivity.this);
			dialog.setContentView(R.layout.search_options_dialog);
			dialog.setTitle("Choose options");
			OnClickListener ctvClick = new OnClickListener() {
				public void onClick(View v) {
					if (v instanceof CheckedTextView) {
						((CheckedTextView) v).toggle();
					}
				}
			};

			CheckedTextView text = (CheckedTextView) dialog
					.findViewById(R.id.ctvSOexactMatches);
			text.setChecked(searchOptions.getExactMatches());
			text.setOnClickListener(ctvClick);
			
			text = (CheckedTextView) dialog
					.findViewById(R.id.ctvSObeginningMatch);
			text.setChecked(searchOptions.getBeginningMatch());
			text.setOnClickListener(ctvClick);
			
			text = (CheckedTextView) dialog.findViewById(R.id.ctvSOmiddleMatch);
			text.setChecked(searchOptions.getMiddleMatch());
			text.setOnClickListener(ctvClick);
			
			text = (CheckedTextView) dialog.findViewById(R.id.ctvSOendingMatch);
			text.setChecked(searchOptions.getEndingMatch());
			text.setOnClickListener(ctvClick);
			
			text = (CheckedTextView) dialog
					.findViewById(R.id.ctvSOcaseSensitive);
			text.setChecked(searchOptions.getCaseSensitive());
			text.setOnClickListener(ctvClick);
			
			Button btnCancel = (Button) dialog.findViewById(R.id.btnSOCancel);
			btnCancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			Button btnOk = (Button) dialog.findViewById(R.id.btnSOOk);
			btnOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					SearchOptions soResult = new SearchOptions();
					CheckedTextView text = (CheckedTextView) dialog
							.findViewById(R.id.ctvSOexactMatches);
					soResult.setExactMatches(text.isChecked());
					text = (CheckedTextView) dialog
							.findViewById(R.id.ctvSObeginningMatch);
					soResult.setBeginningMatch(text.isChecked());
					text = (CheckedTextView) dialog
							.findViewById(R.id.ctvSOmiddleMatch);
					soResult.setMiddleMatch(text.isChecked());
					text = (CheckedTextView) dialog
							.findViewById(R.id.ctvSOendingMatch);
					soResult.setEndingMatch(text.isChecked());
					text = (CheckedTextView) dialog
							.findViewById(R.id.ctvSOcaseSensitive);
					soResult.setCaseSensitive(text.isChecked());

					dialog.dismiss();
					searchOptions = soResult;
				}
			});

			dialog.show();

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MNU_EXIT, 1, "Exit");
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(0, MNU_EXIT, 1, "Exit");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MNU_EXIT:
			Log.d(TAG, "Finishing..");
			this.finish();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Log.d(TAG, "Item at pos " + pos + " selected");
		this.selectedDictionary = pos;
		/*
		 * Toast.makeText( getApplicationContext(),
		 * ((IDictionary)parent.getItemAtPosition(pos)).getName() ,
		 * Toast.LENGTH_SHORT).show();
		 */

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		Log.d(TAG, "Nothing selected");

	}
}
