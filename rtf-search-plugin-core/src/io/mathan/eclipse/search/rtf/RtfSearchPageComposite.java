package io.mathan.eclipse.search.rtf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RtfSearchPageComposite extends Composite {

  private Text txtContainingText;
  private Text txtFileSystem;
  private Button radWorkspace;
  private Button radFileSystem;
  private Button btnFileSystem;

  /**
   * Create the composite.
   */
  public RtfSearchPageComposite(Composite parent, int style) {
    super(parent, style);
    setLayout(new GridLayout(1, false));

    Label lblContainingText = new Label(this, SWT.NONE);
    lblContainingText.setText("Containing Text (RegEx)");

    txtContainingText = new Text(this, SWT.BORDER);
    txtContainingText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
        false, 1, 1));

    Group grpScope = new Group(this, SWT.NONE);
    grpScope.setLayout(new GridLayout(3, false));
    grpScope.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
    grpScope.setText("Scope");

    radWorkspace = new Button(grpScope, SWT.RADIO);
    radWorkspace.setSelection(true);
    radWorkspace.setText("Workspace");
    new Label(grpScope, SWT.NONE);
    new Label(grpScope, SWT.NONE);

    radFileSystem = new Button(grpScope, SWT.RADIO);
    radFileSystem.setText("File System");
    radFileSystem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        enableFileSystem(radFileSystem.getSelection());
      }
    });
    radWorkspace.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        enableFileSystem(radFileSystem.getSelection());
      }
    });

    txtFileSystem = new Text(grpScope, SWT.BORDER);
    txtFileSystem.setEnabled(false);
    txtFileSystem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
        1, 1));

    btnFileSystem = new Button(grpScope, SWT.NONE);
    btnFileSystem.setEnabled(false);
    btnFileSystem.setText("...");
    btnFileSystem.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent()
            .getActiveShell());
        String directory = dialog.open();
        if (directory != null) {
          txtFileSystem.setText(directory);
        }
      }
    });
    txtContainingText.setFocus();
  }

  protected void enableFileSystem(boolean selection) {
    txtFileSystem.setEnabled(selection);
    btnFileSystem.setEnabled(selection);
  }

  @Override
  protected void checkSubclass() {
    // Disable the check that prevents subclassing of SWT components
  }

  public String getContainingText() {
    return txtContainingText == null ? "" : txtContainingText.getText();
  }

  public String getFileSystemText() {
    return txtFileSystem == null ? "" : txtFileSystem.getText();
  }

  public boolean isWorkspaceSearch() {
    return radWorkspace == null || radWorkspace.getSelection();
  }
}
