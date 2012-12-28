package ee.ut.ta.dict;

import java.util.List;

import android.util.Log;

abstract class AbstractDictionary implements IDictionary {
	protected List<String> words = null;
	static final String TAG = "ged.abstractdictionary"; // tag for LogCat
	private int id;
	private String name;
	private String description;
	private String fileName;
	private String letterFileName;
	private int transformationFileId;

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

	public int getTransformationFileId() {
		return transformationFileId;
	}

	abstract List<String> loadWords();

	public List<String> getWords() {
		if (this.words == null) {
			this.words = this.loadWords();
			Log.d(TAG,"Words loaded, count = " + words.size());
		}

		return this.words;

	}
}
