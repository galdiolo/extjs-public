/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import com.extjs.gxt.ui.client.core.Templates;

public interface GridTemplates extends Templates {
  
  @Cache
  public String master(String body, String header);
  
  @Cache
  public String header(String cells, String tstyle);
  
  @Cache
  public String headerCell(String id, String value, String style, String toolTip, String istyle, String blankImagUrl, boolean enableHdMenu);

  @Cache
  public String body(String rows);
  
  @Cache
  public String row(String tstyle, String alt, int cols, String cells, boolean enableRowBody, String body, String bodyStyle);
  
  @Cache
  public String cell(String id, String css, String attr, String cellAttr, String style, String value);
  
  @Cache
  public String startGroup(String groupId, String cls, String style, String group);
  
  @Cache
  public String endGroup();
  
}
