/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tree;

import com.extjs.gxt.ui.client.core.El;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

class RootTreeItem extends TreeItem {

  public RootTreeItem(Tree tree) {
    this.tree = tree;
  }

  @Override
  protected TreeItemUI getTreeItemUI() {
    ui = new TreeItemUI(this);
    ui.containerEl = new El(getElement());
    setData("loaded", true);
    ui.containerEl.setStyleAttribute("padding", "4px");
    return ui;
  }

  @Override
  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv(), target, index);
    setStyleAttribute("padding", "4 0 0 4px");
    getTreeItemUI();
  }

}
