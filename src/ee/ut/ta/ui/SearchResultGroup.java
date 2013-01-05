package ee.ut.ta.ui;

import java.util.ArrayList;

import ee.ut.ta.search.SearchResult;
/***
 * search result group for adapter
 * @author gleb
 *
 */
public class SearchResultGroup extends ArrayList<SearchResult> {

	private static final long serialVersionUID = 1L;
	private String title = "undefined";

	 
	public SearchResultGroup() {
		
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
