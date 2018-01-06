package io.mathan.eclipse.search.rtf;

import java.util.List;

public class RtfSearchResultMatch {

  private final String name;
  private final RtfSearchResultMatchAccessor accessor;
  private final List<String> matchContexts;

  public RtfSearchResultMatch(String name,
      RtfSearchResultMatchAccessor accessor, List<String> matchContexts) {
    this.name = name;
    this.accessor = accessor;
    this.matchContexts = matchContexts;
  }

  public RtfSearchResultMatchAccessor getAccessor() {
    return accessor;
  }

  public List<String> getMatchContexts() {
    return matchContexts;
  }

  public String getName() {
    return name;
  }

}
