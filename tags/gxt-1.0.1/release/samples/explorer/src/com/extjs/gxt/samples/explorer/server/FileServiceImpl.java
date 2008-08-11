/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.server;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.samples.explorer.client.FileService;
import com.extjs.gxt.samples.explorer.client.model.FileModel;
import com.extjs.gxt.samples.explorer.client.model.FolderModel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FileServiceImpl extends RemoteServiceServlet implements FileService {

  private File root;
  private FilenameFilter filter;

  public FileServiceImpl() {
    URL rootUrl = getClass().getClassLoader().getResource("com/extjs");
    root = new File(rootUrl.getFile());
    filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return !name.startsWith(".");
      }
    };
  }

  public List<FileModel> getFolderChildren(FileModel folder) {
    File[] files = null;
    if (folder == null) {
      files = root.listFiles(filter);
    } else {
      File f = new File(folder.getPath());
      files = f.listFiles(filter);
    }
    List<FileModel> models = new ArrayList<FileModel>();
    for (File f : files) {
      FileModel m = null;
      if (f.isDirectory()) {
        m = new FolderModel(f.getName(), f.getAbsolutePath());
      } else {
        m = new FileModel(f.getName(), f.getAbsolutePath());
        m.set("size", f.length());
        m.set("date", new Date(f.lastModified()));
      }
      models.add(m);
    }
    return models;
  }
}
