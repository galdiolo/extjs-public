/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.google.gwt.user.client.Element;

/**
 * A item contained in a <code>List</code>.
 * 
 * @see DataList
 */
public class DataListItem extends Component {

  protected DataList list;
  protected IconButton checkBtn;
  
  private boolean checked;
  private String text, iconStyle;

  /**
   * Creates a new list item.
   */
  public DataListItem() {
  }

  /**
   * Creates a new list item.
   * 
   * @param text the text
   */
  public DataListItem(String text) {
    this();
    setText(text);
  }

  /**
   * Adds a selection listener.
   * 
   * @param listener the listener
   */
  public void addSelectionListener(SelectionListener listener) {
    addListener(Events.Select, listener);
  }

  /**
   * Returns the item's parent list.
   * 
   * @return the list
   */
  public DataList getList() {
    return list;
  }

  /**
   * Returns <code>true</code> if the item is checked.
   * 
   * @return the checked state
   */
  public boolean isChecked() {
    return checked;
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeSelectionListener(SelectionListener listener) {
    removeListener(Events.Select, listener);
  }

  /**
   * Sets the item's checked state.
   * 
   * @param checked the check state
   */
  public void setChecked(boolean checked) {
    this.checked = checked;
    if (rendered) {
      list.onCheckChange(this, checked);
    }
  }

  /**
   * Returns the item's text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the item's icon style.
   * 
   * @param iconStyle the icon style
   */
  public void setIconStyle(String iconStyle) {
    if (rendered) {
      El elem = el().selectNode("." + "x-icon-btn");
      elem.replaceStyleName(this.iconStyle, iconStyle);
    }
    this.iconStyle = iconStyle;
  }

  /**
   * Sets the item's text.
   * 
   * @param text the text
   */
  public void setText(String text) {
    this.text = text;
    if (rendered) {
      El elem = el().selectNode("." + list.itemStyle + "-text");
      elem.dom.setInnerHTML(text);
    }
  }

  /**
   * Returns the icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return iconStyle;
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    list.onRenderItem(this, target, index);
  }

}
