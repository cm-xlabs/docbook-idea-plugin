package com.coremedia.docbook.idea;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mfritsch
 * Date: 18.09.13
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 * This program uses the Ruby xmlformat script from kitebird:
 * http://http://www.kitebird.com/software/xmlformat/
 */
public class FormatDocBook extends AnAction{
  public void actionPerformed(AnActionEvent event) {
    FileDocumentManager fileManager = FileDocumentManager.getInstance();
    ScriptingContainer ruby = new ScriptingContainer(LocalContextScope.SINGLETHREAD);
    String fileName = getCurrentFile(event, fileManager);
    if (fileName==null)
      throw new RuntimeException("No file defined");
    String[] arguments = {"--in-place", fileName};
    ruby.setClassLoader(ruby.getClass().getClassLoader());
    ruby.setArgv(arguments);
    ClassLoader classLoader = this.getClass().getClassLoader();
    URL confUrl = classLoader.getResource("/format/docbook.xml");
    InputStream confStream = null;
    URL url = null;
    InputStream rubyStream = null;
    try {
      confStream = confUrl.openStream();
      url = classLoader.getResource("/format/xmlformat.xml");
      ruby.put("filename", fileName);
      ruby.put("configstream", confStream);
      rubyStream = url.openStream();
      ruby.runScriptlet(rubyStream, "xmlformat.xml");
    }
    catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } finally {
      try {
          System.out.println("Doing Finally");
          ruby.terminate();
          rubyStream.close();
          confStream.close();
          refreshCurrentFile(event, fileManager, fileName);

      } catch (Throwable e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
    }
  }
  //Make Change visible in Idea editor
  private void refreshCurrentFile(AnActionEvent e, FileDocumentManager fileManager, String fileName) {

    List fileNames = new ArrayList();
    File file = new File(fileName);
    fileNames.add(file);
    com.intellij.openapi.vfs.LocalFileSystem.getInstance().refreshIoFiles(fileNames);
  }

  private String getCurrentFile(AnActionEvent e, FileDocumentManager fileManager) {
    final Project project = e.getProject();
    if (project == null) {
      return null;
    }
    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    if (editor == null) {
      return null;
    }
    final Document document = editor.getDocument();
    if (document == null) {
      return null;
    }
    //Save all current changes before formatting
    fileManager.saveDocument(document);
    VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
    if (virtualFile == null) {
      return null;
    }
    return virtualFile.getPath();
  }
}
