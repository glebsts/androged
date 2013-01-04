package ee.ut.ta.dict;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import ee.ut.ta.dict.transf.ITransformationFile;
import ee.ut.ta.dict.transf.Transformation;

abstract class AbstractDictionary implements IDictionary {
	protected List<String> words = null;
	protected List<Transformation> transformations = null;
	protected List<Letter> letters = null;
	static final String TAG = "ged.abstractdictionary"; // tag for LogCat
	private int id;
	private String name;
	private String description;
	private String fileName;
	private String letterFileName;
	private String transformationFileName;
	private int transformationFileId;
	private IDictionaryStorage storage;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setLetterFileName(String letterFileName) {
		this.letterFileName = letterFileName;
	}

	public String getLetterFileName() {
		return letterFileName;
	}

	public void setTransformationFileId(int transformationFileId) {
		this.transformationFileId = transformationFileId;
	}

	public String getTransformationFileName() {
		if(this.transformationFileName == null){
			this.setTransformationFileName(this.getStorage().getTransformationStorage().
					getTransformationFileById(this.getTransformationFileId()).getFileName());
		}
		return transformationFileName;
	}
	
	public void setTransformationFileName(String transformationFileName) {
		this.transformationFileName = transformationFileName;
	}

	public int getTransformationFileId() {
		return transformationFileId;
	}

	abstract void loadWords(Context ctx);

	public List<String> getWords(Context ctx) {
		if (this.words == null) {
			this.loadWords(ctx);
		}
		return this.words;
	}
	
	public List<String> getWords() {
		if (this.words == null) {
			return new ArrayList<String>();
		}
		return this.words;
	}
	
	abstract void loadTransformations(Context ctx);

	public List<Transformation> getTransformations(Context ctx) {
		if (this.transformations == null) {
			this.loadTransformations(ctx);
		}
		return this.transformations;
	}
	
	public List<Transformation> getTransformations() {
		if (this.transformations == null) {
			return new ArrayList<Transformation>();
		}
		return this.transformations;
	}
	
	abstract void loadLetters(Context ctx);

	public List<Letter> getLetters(Context ctx) {
		if (this.letters == null) {
			this.loadLetters(ctx);
			
		}

		return this.letters;

	}
	
	public void unload(){
		this.words = null;
		Log.d(TAG, "Words unloaded");
		this.letters = null;
		Log.d(TAG, "Letters unloaded");
		this.transformations = null;
		Log.d(TAG, "Transformations unloaded");
	}
	
	public IDictionaryStorage getStorage(){
		return this.storage;
	}
	public void setStorage(IDictionaryStorage storage){
		this.storage=storage;
	}
}
