package io.mathan.eclipse.search.rtf;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class RtfSearchPage extends DialogPage implements ISearchPage {
  private RtfSearchPageComposite composite;

  public RtfSearchPage() {
    // TODO Auto-generated constructor stub
  }

  public RtfSearchPage(String title) {
    super(title);
    // TODO Auto-generated constructor stub
  }

  public RtfSearchPage(String title, ImageDescriptor image) {
    super(title, image);
    // TODO Auto-generated constructor stub
  }

  public void createControl(Composite parent) {
    composite = new  RtfSearchPageComposite(parent, SWT.FILL);
    setControl(composite);
  }

  public boolean performAction() {
    NewSearchUI.runQuery(newQuery());
    return true;
  }

  private ISearchQuery newQuery() {
    if(composite.isWorkspaceSearch()) {
      return new RtfSearchQuery(composite.getContainingText());
    } else {
      return new RtfSearchQuery(composite.getContainingText(), composite.getFileSystemText());
    }
  }

  public void setContainer(ISearchPageContainer container) {
    // TODO Auto-generated method stub

  }

}
