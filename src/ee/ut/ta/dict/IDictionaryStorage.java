package ee.ut.ta.dict;

import ee.ut.ta.dict.transf.ITransformationFile;
import ee.ut.ta.dict.transf.ITransformationStorage;


public interface IDictionaryStorage {
	public ITransformationStorage getTransformationStorage();

}
