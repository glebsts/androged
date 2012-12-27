package ee.ut.ta.dict;

import java.util.List;

import android.util.Log;

abstract class AbstractDictionaryStorage implements IDictionaryStorage {
	private List<IDictionary> dictionaries = null;
	static final String TAG = "ged.abstractstorage"; // tag for LogCat

	abstract List<IDictionary> loadDictionaries();

	public List<IDictionary> getDictionaries() {
		if (this.dictionaries == null) {
			this.dictionaries = this.loadDictionaries();
			Log.d(TAG,
					"Dictionaries loaded, count = " + this.dictionaries.size());
		}

		return this.dictionaries;

	}
}
