/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client;

import java.util.List;

import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.samples.resources.client.MailItem;
import com.google.gwt.user.client.rpc.RemoteService;

public interface MailService extends RemoteService {

  public Folder getMailFolders(String userId);

  public List<MailItem> getMailItems(Folder folder);

}
