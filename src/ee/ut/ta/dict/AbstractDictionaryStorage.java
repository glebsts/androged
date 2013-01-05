package ee.ut.ta.dict;

import java.util.List;

import ee.ut.ta.dict.transf.ITransformationStorage;
/***
 * abstract class for dictionary collection handling
 * @author gleb
 *
 */
abstract class AbstractDictionaryStorage implements IDictionaryStorage {
	private List<IDictionary> dictionaries = null;
	static final String TAG = "ged.abstractdictionarystorage"; // tag for LogCat

	ITransformationStorage transformationStorage;
	abstract List<IDictionary> loadDictionaries();
	
	public List<IDictionary> getDictionaries() {
		if (this.dictionaries == null) {
			this.dictionaries = this.loadDictionaries();
			//Log.d(TAG,
			//		"Dictionaries loaded, count = " + this.dictionaries.size());
		}

		return this.dictionaries;

	}
	public ITransformationStorage getTransformationStorage(){
		return this.transformationStorage;
	}
}
