package io.mathan.eclipse.search.rtf;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

public class FileRtfSearchResultMatchAccessor implements
    RtfSearchResultMatchAccessor {

  private final File file;

  public FileRtfSearchResultMatchAccessor(File file) {
    this.file = file;
  }

  public void open() throws CoreException {
    try {
      Desktop.getDesktop().open(file);
    } catch (IOException e) {
      Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
          "Could not open document", e);
      throw new CoreException(status);
    }
  }

}
