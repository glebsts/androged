package ee.ut.ta.dict;

public interface IDictionary {
	public int getId();
	public void setId(int id);
	public String getName();
	public void setName(String name);
	public String getDescription();
	public void setDescription(String description);
	public String getFileName();
	public void setFileName(String fileName);
	public String getLetterFileName();
	public void setLetterFileName(String letterFileName);
	public int getTransformationFileId();
	public void setTransformationFileId(int transformationFileId);
}
