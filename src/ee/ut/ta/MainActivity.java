package ee.ut.ta;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;
import ee.ut.ta.dict.AssetDictionaryStorage;
import ee.ut.ta.dict.IDictionary;
import ee.ut.ta.ui.DictionaryAdapter;

public class MainActivity extends Activity implements OnItemSelectedListener {
	
	static final String TAG = "ged.main"; // tag for LogCat
	// menu items
	private static final int MNU_EXIT = 102;
	// dictionaries
	AssetDictionaryStorage assetDictionaryStorage;
	List<IDictionary> dictionaries;
	
	// screen controls
	Spinner cmbDictionary;

	
	//adapters
	DictionaryAdapter dictionaryAdapter;
	private int selectedDictionary;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dictionaries = (assetDictionaryStorage = new AssetDictionaryStorage(getApplicationContext())).getDictionaries();
        Log.d(TAG, "Loaded dict size = " +dictionaries.size());
        cmbDictionary = (Spinner)findViewById(R.id.cmbDictionary);
        dictionaryAdapter = new DictionaryAdapter(this, R.layout.dict_spinner_row, R.id.dictName, dictionaries);
        cmbDictionary.setAdapter(dictionaryAdapter);
        cmbDictionary.setOnItemSelectedListener(this);
        
        
    }

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
		Log.d(TAG, "Item at pos "+pos+" selected");
		this.selectedDictionary = pos;
		/*Toast.makeText(
				getApplicationContext(),
				((IDictionary)parent.getItemAtPosition(pos)).getName()
						, Toast.LENGTH_SHORT).show();*/
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		Log.d(TAG, "Nothing selected");
		
	}
}
