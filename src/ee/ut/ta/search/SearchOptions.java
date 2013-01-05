package ee.ut.ta.search;

/***
 * class for holding user preferences for the search
 * @author gleb
 *
 */
public class SearchOptions {
	private Boolean exactMatches = true;
	private Boolean beginningMatch = true;
	private Boolean middleMatch = false;
	private Boolean endingMatch = false;
	private Boolean caseSensitive = false;

	public void setExactMatches(Boolean exactMatches) {
		this.exactMatches = exactMatches;
	}

	public Boolean getExactMatches() {
		return exactMatches;
	}

	public void setBeginningMatch(Boolean beginningMatch) {
		this.beginningMatch = beginningMatch;
	}

	public Boolean getBeginningMatch() {
		return beginningMatch;
	}

	public void setMiddleMatch(Boolean middleMatch) {
		this.middleMatch = middleMatch;
	}

	public Boolean getMiddleMatch() {
		return middleMatch;
	}

	public void setEndingMatch(Boolean endingMatch) {
		this.endingMatch = endingMatch;
	}

	public Boolean getEndingMatch() {
		return endingMatch;
	}

	public void setCaseSensitive(Boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public Boolean getCaseSensitive() {
		return caseSensitive;
	}

}
