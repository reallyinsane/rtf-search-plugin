package io.mathan.eclipse.search.rtf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;

public class RtfSearchResult implements ISearchResult {

  private List<ISearchResultListener> listeners = new ArrayList<ISearchResultListener>();
  private RtfSearchQuery query;
  private List<RtfSearchResultMatch> matches = new ArrayList<RtfSearchResultMatch>();

  public RtfSearchResult(RtfSearchQuery query) {
    this.query = query;

  }

  public void addMatch(RtfSearchResultMatch match) {
    this.matches.add(match);
    fireAdded(match);
  }

  private void fireAdded(RtfSearchResultMatch match) {
    for (ISearchResultListener listener : listeners) {
      listener.searchResultChanged(new RtfSearchResultMatchEvent(this, match));
    }
  }

  public List<RtfSearchResultMatch> getMatches() {
    return matches;
  }

  public void addListener(ISearchResultListener l) {
    this.listeners.add(l);
  }

  public void removeListener(ISearchResultListener l) {
    this.listeners.remove(l);
  }

  public String getLabel() {
    return "result";
  }

  public String getTooltip() {
    return "result tooltip";
  }

  public ImageDescriptor getImageDescriptor() {
    return null;
  }

  public ISearchQuery getQuery() {
    return query;
  }

  public static class RtfSearchResultMatchEvent extends SearchResultEvent {
    private RtfSearchResultMatch match;

    protected RtfSearchResultMatchEvent(ISearchResult searchResult,
        RtfSearchResultMatch match) {
      super(searchResult);
      this.match = match;
    }

    public RtfSearchResultMatch getMatch() {
      return match;
    }

  }

}
