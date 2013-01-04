package ee.ut.ta;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;
import ee.ut.ta.dict.AssetDictionaryStorage;
import ee.ut.ta.dict.IDictionary;
import ee.ut.ta.search.SearchOptions;
import ee.ut.ta.search.SearchProcessor;
import ee.ut.ta.ui.DictionaryAdapter;
import ee.ut.ta.ui.SearchResultExpandableListAdapter;

public class MainActivity extends Activity implements OnItemSelectedListener {

	static final String TAG = "ged.main"; // tag for LogCat
	// menu items
	private static final int MNU_EXIT = 102;
	// dictionaries
	AssetDictionaryStorage assetDictionaryStorage;
	List<IDictionary> dictionaries;

	// search
	SearchOptions searchOptions = new SearchOptions();
	SearchProcessor searchProcessor;
	Thread searchThread;

	// screen controls
	Spinner cmbDictionary;
	Button btnSearchOptions;
	Button btnStartSearch;
	EditText txtSearchTerm, txtMaxEditDistance, txtBest;
	ExpandableListView elvResults;

	// adapters
	DictionaryAdapter dictionaryAdapter;
	SearchResultExpandableListAdapter searchResultAdapter;
	private int selectedDictionary;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dictionaries = (assetDictionaryStorage = new AssetDictionaryStorage(
				getApplicationContext())).getDictionaries();
		Log.d(TAG, "Loaded dict count = " + dictionaries.size());
		cmbDictionary = (Spinner) findViewById(R.id.cmbDictionary);
		dictionaryAdapter = new DictionaryAdapter(this,
				R.layout.dict_spinner_row, R.id.dictName, dictionaries);
		cmbDictionary.setAdapter(dictionaryAdapter);
		cmbDictionary.setOnItemSelectedListener(this);

		btnSearchOptions = (Button) findViewById(R.id.btnSearchOptions);
		btnSearchOptions.setOnClickListener(searchOptionsClickHandler);

		btnStartSearch = (Button) findViewById(R.id.btnStartSearch);
		btnStartSearch.setOnClickListener(startSearchClickHandler);

		txtSearchTerm = (EditText) findViewById(R.id.txtSearchTerm);
		txtSearchTerm.setText("paket");
		txtMaxEditDistance = (EditText) findViewById(R.id.txtMaxEditDistance);
		txtBest = (EditText) findViewById(R.id.txtBest);

		elvResults = (ExpandableListView) findViewById(R.id.elvResults);
	}

	Button.OnClickListener startSearchClickHandler = new Button.OnClickListener() {
		public void onClick(View v) {
			if (txtSearchTerm.getText().length() == 0) {
				txtSearchTerm.requestFocus();
				return;
			}

			double maxDistance = -1;
			int best = -1;
			if (txtMaxEditDistance.getText().length() != 0) {

				try {
					maxDistance = Double.parseDouble(txtMaxEditDistance
							.getText().toString());
				} catch (NumberFormatException exc) {
					txtMaxEditDistance.requestFocus();
					return;
				}
				if (maxDistance <= 0) {
					txtMaxEditDistance.requestFocus();
					return;
				}
				best = -1;

			}else {
				if (txtBest.getText().length() != 0) {
					try {
						best = Integer.parseInt(txtBest
								.getText().toString());
					} catch (NumberFormatException exc) {
						txtBest.requestFocus();
						return;
					}
					if (best <= 0) {
						txtBest.requestFocus();
						return;
					}
					maxDistance = -1;
				}
			}
			if((maxDistance < 0 && best<0) || (maxDistance>0 && best>0)){
				Toast.makeText(getApplicationContext(),
						"Only max edit distance XOR best must be set and > 0.",
						Toast.LENGTH_LONG).show();
				txtMaxEditDistance.requestFocus();
				return;
			}
			searchProcessor = new SearchProcessor(getApplicationContext(),
					gedHandler, txtSearchTerm.getText().toString(),
					searchOptions, dictionaries.get(selectedDictionary),
					maxDistance, best);
			disableUI();
			searchThread = new Thread(null, searchProcessor, "Search thread");
			searchThread.start();
			Log.d(TAG, "Started search processor thread");

		}
	};

	final Handler gedHandler = new Handler() {
		public void handleMessage(Message msg) {
			enableUI();
			Log.d(TAG, "Message! " + msg.obj.toString());
			setResults((Long) msg.obj);
		}

	};

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

	protected void setResults(long time) {
		if (searchProcessor != null) {
			if (searchProcessor.getResults() != null) {
				searchResultAdapter = new SearchResultExpandableListAdapter(
						searchProcessor.getResults(), getApplicationContext());
				elvResults.setAdapter(searchResultAdapter);
			}
			Toast.makeText(getApplicationContext(),
					String.format("Time: %1.2f sec", time / 1000.0),
					Toast.LENGTH_LONG).show();
		}

	}

	protected void enableUI() {
		btnSearchOptions.setEnabled(true);
		btnStartSearch.setEnabled(true);

	}

	protected void disableUI() {
		btnSearchOptions.setEnabled(false);
		btnStartSearch.setEnabled(false);

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
