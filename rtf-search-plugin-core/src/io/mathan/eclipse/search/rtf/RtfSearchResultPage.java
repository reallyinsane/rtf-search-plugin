package io.mathan.eclipse.search.rtf;

import io.mathan.eclipse.search.rtf.RtfSearchResult.RtfSearchResultMatchEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IPageSite;

public class RtfSearchResultPage implements ISearchResultPage {

  private Table table;
  private IPageSite site;
  private String id;
  private RtfSearchResult result;

  public IPageSite getSite() {
    return site;
  }

  public void init(IPageSite site) throws PartInitException {
    this.site = site;

  }

  public void createControl(Composite parent) {
    table = new Table(parent, SWT.FILL);
    table.setHeaderVisible(true);
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDoubleClick(MouseEvent e) {
        Point pt = new Point(e.x, e.y);
        TableItem item = table.getItem(pt);
        RtfSearchResultMatch match = (RtfSearchResultMatch) item.getData();
        try {
          match.getAccessor().open();
        } catch (CoreException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    TableColumn column = new TableColumn(table, SWT.NONE);
    column.setText("path");
    column.setWidth(300);
    column = new TableColumn(table, SWT.NONE);
    column.setText("matches");
    column.setWidth(100);
  }

  public void dispose() {
    // TODO Auto-generated method stub

  }

  public Control getControl() {
    return table;
  }

  public void setActionBars(IActionBars actionBars) {
  }

  public void setFocus() {
  }

  public Object getUIState() {
    return null;
  }

  public void setInput(ISearchResult search, Object uiState) {
    RtfSearchResult result = (RtfSearchResult) search;
    if (result == null) {
      return;
    }
    this.result = result;
    table.removeAll();
    result.addListener(new ISearchResultListener() {

      public void searchResultChanged(SearchResultEvent e) {
        if (e instanceof RtfSearchResultMatchEvent) {
          RtfSearchResultMatchEvent event = (RtfSearchResultMatchEvent) e;
          final RtfSearchResultMatch match = event.getMatch();
          Display.getDefault().asyncExec(new Runnable() {
            public void run() {
              TableItem item = new TableItem(table, SWT.NONE);
              item.setText(new String[]{match.getName(),
                  String.valueOf(match.getMatchContexts().size())});
              item.setData(match);
              table.setRedraw(true);
            }
          });
        }
      }
    });
  }

  public void setViewPart(ISearchResultViewPart part) {
  }

  public void restoreState(IMemento memento) {
  }

  public void saveState(IMemento memento) {
  }

  public void setID(String id) {
    this.id = id;
  }

  public String getID() {
    return this.id;
  }

  public String getLabel() {
    if (this.result != null) {
      return String.format("RTF search for \"%s\"", result.getQuery()
          .getLabel());
    }
    return "empty";
  }

}
