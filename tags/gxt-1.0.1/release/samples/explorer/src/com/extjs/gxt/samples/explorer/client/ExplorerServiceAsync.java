/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client;


import com.extjs.gxt.samples.explorer.client.model.Post;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExplorerServiceAsync {

  public void getPosts(PagingLoadConfig config, AsyncCallback<PagingLoadResult<Post>> callback);
  
}
