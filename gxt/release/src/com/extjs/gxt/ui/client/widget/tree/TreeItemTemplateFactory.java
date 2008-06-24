/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tree;

import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.core.Templates;

public interface TreeItemTemplateFactory extends Templates {

  @Cache
  public Template createTemplate();
}
