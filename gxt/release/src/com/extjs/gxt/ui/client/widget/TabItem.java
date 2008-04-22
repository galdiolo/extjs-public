/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Frame;

/**
 * A tab in a <code>TabPanel</code>.
 */
public class TabItem extends Container {

  class HeaderItem extends Component {

    public String text, iconStyle;

    @Override
    public void onComponentEvent(ComponentEvent ce) {
      super.onComponentEvent(ce);
      if (ce.getEventType() == Event.ONCLICK) {
        onClick(ce);
      }
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
      if (rendered) {
        el.child(".x-tab-strip-text").setInnerHtml(text);
      }
    }

    public String getIconStyle() {
      return iconStyle;
    }

    public void setIconStyle(String iconStyle) {
      this.iconStyle = iconStyle;
    }

    protected void onRender(Element target, int pos) {
      tabPanel.onItemRender(TabItem.this, target, pos);
    }

    protected void onClick(ComponentEvent ce) {
      tabPanel.onItemClick(TabItem.this, ce);
    }

    protected void onMouseOut(ComponentEvent ce) {
      tabPanel.onItemOver(TabItem.this, false);
    }

    protected void onMouseOver(BaseEvent be) {
      tabPanel.onItemOver(TabItem.this, true);
    }

  }

  /**
   * True to make tab item closable (defaults to false).
   */
  public boolean closable;

  /**
   * Optional style name that is added to the item's text element (defaults to
   * null).
   */
  public String textStyle;

  Template template;
  TabPanel tabPanel;
  HeaderItem header;

  /**
   * Creates a new tab item.
   */
  public TabItem() {
    header = new HeaderItem();
  }

  /**
   * Creates a new tab item with the given text.
   * 
   * @param text the item's text
   */
  public TabItem(String text) {
    this();
    setText(text);
  }

  /**
   * Closes the tab item.
   */
  public void close() {
    tabPanel.remove(this);
  }

  /**
   * Returns the item's icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return header.getIconStyle();
  }

  /**
   * Returns the item's tab panel.
   * 
   * @return the tab panel
   */
  public TabPanel getTabPanel() {
    return tabPanel;
  }

  /**
   * Returns the item's text.
   * 
   * @return the text
   */
  public String getText() {
    return header.getText();
  }

  /**
   * Sets the item's icon style.
   * 
   * @param iconStyle the icon style
   */
  public void setIconStyle(String iconStyle) {
    header.setIconStyle(iconStyle);
  }

  /**
   * Sets the item's text.
   * 
   * @param text the new text
   */
  public void setText(String text) {
    header.setText(text);
  }

  /**
   * Sets a url for the content area of the item.
   * 
   * @param url the url
   * @return the frame widget
   */
  public Frame setUrl(String url) {
    Frame f = new Frame(url);
    fly(f.getElement()).setStyleAttribute("frameBorder", "0");
    f.setSize("100%", "100%");
    removeAll();
    add(new WidgetComponent(f));
    return f;
  }
  
  @Override
  public String toString() {
    return el != null ? el.toString() : super.toString();
  }

}
