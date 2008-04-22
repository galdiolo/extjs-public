package com.extjs.gxt.samples.mail.client.model;

import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class MailModel extends BaseTreeModel {

  public MailModel() {
    Folder inbox = new Folder("Inbox");
    Folder sent = new Folder("Sent Items");
    Folder trash = new Folder("Trash");
    
    add(inbox);
    add(sent);
    add(trash);
    
    
  }
  
}
