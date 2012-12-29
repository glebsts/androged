package ee.ut.ta.dict.transf;

import java.util.List;

import android.content.Context;

public abstract class AbstractTransformationFile implements ITransformationFile {
	protected List<Transformation> transformations = null;
	static final String TAG = "ged.abstracttransformationfile"; // tag for LogCat
	private int id;
	private String name;
	private String fileName;
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

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	abstract void loadTransformations(Context context);
	
	public List<Transformation> getTransformations(Context context) {
		if (this.transformations == null) {
			this.loadTransformations(context);

		}

		return this.transformations;
	}
}
