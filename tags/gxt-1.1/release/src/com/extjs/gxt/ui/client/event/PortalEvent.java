/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.extjs.gxt.ui.client.widget.custom.Portlet;

public class PortalEvent extends ContainerEvent<Portal, Portlet> {

  public Portal portal;
  
  public Portlet portlet;
  
  public int startColumn;

  public int startRow;

  public int column;

  public int row;

  public PortalEvent(Portal portal) {
    super(portal);
    this.portal = portal;
  }

  public PortalEvent(Portal portal, Portlet portlet, int startColumn, int startRow, int column, int row) {
    super(portal);
    this.portlet = portlet;
    this.startColumn = startColumn;
    this.startRow = startRow;
    this.column = column;
    this.row = row;
  }

  public PortalEvent(Portal portal, Portlet portlet) {
    super(portal, portlet);
    this.portal = portal;
    this.portlet = portlet;
  }

}
