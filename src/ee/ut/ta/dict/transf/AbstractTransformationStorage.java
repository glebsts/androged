package ee.ut.ta.dict.transf;

import java.util.List;
/***
 * abstract class for holding and management of transformation rule files
 * @author gleb
 *
 */
abstract class AbstractTransformationStorage implements ITransformationStorage {
	private List<ITransformationFile> transformationFiles = null;
	static final String TAG = "ged.abstracttransformationstorage"; // tag for LogCat

	abstract List<ITransformationFile> loadTransformationFiles();
	
	public ITransformationFile getTransformationFileById(int id) {
		if (this.transformationFiles == null) {
			this.transformationFiles = this.loadTransformationFiles();
		}

		for(int i= 0;i<this.transformationFiles.size();i++){
			if(this.transformationFiles.get(i).getId() == id){
				return this.transformationFiles.get(i);
			}
		}
		
		return null;

	}
	
	
}
