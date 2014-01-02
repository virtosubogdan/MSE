package nradl;

public interface Generator {

	public void setLimit(String limit);
	
	public String getLimit();
	
	public void setVerbose(boolean isVerbose);
	
	public boolean isVerbose();
	
	public void changeNumber();
}
