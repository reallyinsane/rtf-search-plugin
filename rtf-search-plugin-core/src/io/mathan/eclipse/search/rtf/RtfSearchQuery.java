package io.mathan.eclipse.search.rtf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

public class RtfSearchQuery implements ISearchQuery {

  private final Pattern pattern;
  private final boolean workspaceSearch;
  private final String directory;
  private final RtfSearchResult result;

  public RtfSearchQuery(String text) {
    this(text, true, null);
  }

  public RtfSearchQuery(String text, String directory) {
    this(text, false, directory);
  }

  private RtfSearchQuery(String text, boolean workspaceSearch, String directory) {
    this.pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
    this.workspaceSearch = workspaceSearch;
    this.directory = directory;
    this.result = new RtfSearchResult(this);
  }

  public IStatus run(IProgressMonitor monitor)
      throws OperationCanceledException {

    if (workspaceSearch) {
      IResource resource = ResourcesPlugin.getWorkspace().getRoot();
      List<IFile> files = new ArrayList<IFile>();
      RtfResourceVisitor visitor = new RtfResourceVisitor(files);
      try {
        resource.accept(visitor);
        findMatches(files);
      } catch (CoreException e) {
        return new Status(Status.ERROR, Activator.PLUGIN_ID,
            "Problem during search in workspace", e);
      } catch (IOException e) {
        return new Status(Status.ERROR, Activator.PLUGIN_ID,
            "Problem during search in workspace", e);
      }
    } else {
      File directory = new File(this.directory);
      try {
        findMatches(directory);
      } catch (IOException e) {
        return new Status(Status.ERROR, Activator.PLUGIN_ID,
            "Problem during search in file system", e);
      }
    }
    return Status.OK_STATUS;
  }

  private void findMatches(File directory) throws IOException {
    File[] files = directory.listFiles(new FileFilter() {

      public boolean accept(File file) {
        return file.isDirectory() || file.getName().toLowerCase().endsWith(".rtf");
      }
    });
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          findMatches(file);
        } else {
          FileInputStream in = new FileInputStream(file);
          List<String> contexts = findMatches(in);
          if (contexts.size() > 0) {
            result.addMatch(new RtfSearchResultMatch(file.getAbsolutePath(),
                new FileRtfSearchResultMatchAccessor(file), contexts));
          }
          in.close();
        }
      }
    }
  }

  private void findMatches(List<IFile> files) throws CoreException, IOException {
    for (IFile file : files) {
      InputStream in = file.getContents();
      List<String> contexts = findMatches(in);
      if (contexts.size() > 0) {
        result.addMatch(new RtfSearchResultMatch(file.getProject() + " "
            + file.getProjectRelativePath(),
            new IFileRtfSearchResultMatchAccessor(file), contexts));
      }
    }
  }

  private static final String[] REGEX_TO_REPLACE = {"\\{", "\\}",
      "\\\\[a-zA-Z0-9]*", "\\s+"};

  private List<String> findMatches(InputStream in) throws IOException {
    List<String> matches = new ArrayList<String>();
    String content = getContent(in);
    for (String regex : REGEX_TO_REPLACE) {
      content = content.replaceAll(regex, "");
    }
    Matcher matcher = pattern.matcher(content);
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches;
  }

  private static String getContent(InputStream in) throws IOException {
    StringBuilder sb = new StringBuilder();
    String line;
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    while ((line = reader.readLine()) != null) {
      sb.append(line);
    }
    reader.close();
    return sb.toString();
  }

  public String getLabel() {
    return pattern.pattern();
  }

  public boolean canRerun() {
    return false;
  }

  public boolean canRunInBackground() {
    return false;
  }

  public ISearchResult getSearchResult() {
    return result;
  }

  static class RtfResourceVisitor implements IResourceVisitor {

    private List<IFile> files;

    RtfResourceVisitor(List<IFile> files) {
      this.files = files;
    }

    public List<IFile> getFiles() {
      return files;
    }

    public boolean visit(IResource resource) throws CoreException {
      if (resource.getType() == IResource.FILE
          && "rtf".equalsIgnoreCase(resource.getFileExtension())) {
        files.add((IFile) resource);
      }
      return true;
    }
  }

}
