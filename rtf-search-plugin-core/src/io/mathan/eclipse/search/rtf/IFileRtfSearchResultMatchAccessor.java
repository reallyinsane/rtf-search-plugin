package io.mathan.eclipse.search.rtf;

import java.awt.Desktop;
import java.io.IOException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

public class IFileRtfSearchResultMatchAccessor implements
    RtfSearchResultMatchAccessor {

  private final IFile file;

  public IFileRtfSearchResultMatchAccessor(IFile file) {
    this.file = file;
  }

  public void open() throws CoreException {
    try {
      Desktop.getDesktop().open(file.getLocation().toFile());
    } catch (IOException e) {
      Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
          "Could not open document", e);
      throw new CoreException(status);
    }

  }

}
