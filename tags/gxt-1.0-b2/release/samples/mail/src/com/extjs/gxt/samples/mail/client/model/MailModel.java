/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.model;

import java.util.List;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.samples.resources.client.MailItem;
import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class MailModel extends BaseTreeModel {

  private Folder inbox;
  private Folder sent;
  private Folder trash;

  public MailModel() {
    inbox = new Folder("Inbox");
    sent = new Folder("Sent Items");
    trash = new Folder("Trash");

    List items = TestData.getMailItems();
    int count = items.size();
    for (int i = 0; i < count; i++) {
      MailItem item = (MailItem) items.get(i);
      if (i < (count / 2)) {
        inbox.add(item);
      } else {
        sent.add(item);
      }
    }

    add(inbox);
    add(sent);
    add(trash);

  }

  public Folder getInbox() {
    return inbox;
  }

}
