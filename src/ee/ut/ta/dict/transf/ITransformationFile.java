package ee.ut.ta.dict.transf;

import java.util.List;

import android.content.Context;

public interface ITransformationFile {
	public int getId();
	public void setId(int id);
	public String getName();
	public void setName(String name);
	public String getFileName();
	public void setFileName(String fileName);

    public List<Transformation> getTransformations(Context context);
}
