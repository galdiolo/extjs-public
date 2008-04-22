/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A custom component that supports an icon, text, and tool area.
 */
public class Header extends Component {

  /**
   * Style name added to the header's text element (defaults to null).
   */
  public String textStyle;

  private IconButton iconBtn;
  private El textEl;
  private List<Component> tools = new ArrayList<Component>();
  private HorizontalPanel widgetPanel;
  private String text, iconStyle;

  /**
   * Adds a tool.
   * 
   * @param tool the tool to be inserted
   */
  public void addTool(Component tool) {
    insertTool(tool, getToolCount());
  }

  /**
   * Returns the header's icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return iconStyle;
  }

  /**
   * Returns the header's text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Returns the tool at the given index.
   * 
   * @param index the index
   * @return the tool
   */
  public Component getTool(int index) {
    return tools.get(index);
  }

  /**
   * Returns the number of tool items.
   * 
   * @return the count
   */
  public int getToolCount() {
    return tools.size();
  }

  /**
   * Inserts a tool.
   * 
   * @param tool the tool to insert
   * @param index the insert location
   */
  public void insertTool(Component tool, int index) {
    tools.add(index, tool);
    if (rendered) {
      widgetPanel.insert((Widget)tool, index);
    }
  }

  /**
   * Removes a tool.
   * 
   * @param tool the tool to remove
   */
  public void removeTool(Component tool) {
    tools.remove(tool);
    if (rendered) {
      widgetPanel.remove(tool);
    }
  }

  /**
   * Sets the header's icon style.
   * 
   * @param iconStyle the icon style
   */
  public void setIconStyle(String iconStyle) {
    this.iconStyle = iconStyle;
    if (rendered) {
      textEl.addStyleName("x-panel-icon");
      iconBtn.el.setLeft(1);
      iconBtn.setVisible(true);
      iconBtn.changeStyle(iconStyle);
    }
  }

  /**
   * Sets the header's text.
   * 
   * @param text the new text
   */
  public void setText(String text) {
    this.text = text;
    if (rendered) {
      textEl.setInnerHtml(text);
    }
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(widgetPanel);
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(widgetPanel);
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv(), target, index);
    addStyleName("x-small-editor");
    el.makePositionable();

    iconBtn = new IconButton();
    iconBtn.setVisible(false);
    iconBtn.setStyleAttribute("position", "absolute");
    iconBtn.setStyleAttribute("marginLeft", "0px");
    iconBtn.setWidth(20);
    iconBtn.render(el.dom);

    widgetPanel = new HorizontalPanel();
    widgetPanel.setStyleName("x-panel-toolbar");
    widgetPanel.setLayoutOnChange(true);
    widgetPanel.setStyleAttribute("float", "right");

    if (tools.size() > 0) {
      for (int i = 0; i < tools.size(); i++) {
        widgetPanel.add(tools.get(i));
      }
    }

    widgetPanel.render(getElement());

    textEl = new El(DOM.createSpan());
    el.appendChild(textEl.dom);
    textEl.setStyleName(textStyle);

    if (text != null) {
      setText(text);
    }

    if (iconStyle != null) {
      setIconStyle(iconStyle);
    }
  }

}
