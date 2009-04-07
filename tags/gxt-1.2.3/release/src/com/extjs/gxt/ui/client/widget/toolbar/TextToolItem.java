/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.toolbar;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.user.client.Element;

/**
 * A tool item with optional text and icon style.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Select</b> : ToolBarEvent(container, item, event)<br>
 * <div>Fires after the item is selected.</div>
 * <ul>
 * <li>container : the parent tool bar</li>
 * <li>item : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class TextToolItem extends ToolItem {

  /**
   * The wrapped button instance.
   */
  protected Button button;

  /**
   * Creates a new text tool item.
   */
  public TextToolItem() {
    button = new Button();
  }

  /**
   * Creates a new text tool item.
   * 
   * @param text the tool item text
   */
  public TextToolItem(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a new text tool item.
   * 
   * @param text the item's text
   * @param selectionListener a selection listener
   */
  public TextToolItem(String text, SelectionListener<ToolBarEvent> selectionListener) {
    this(text);
    addSelectionListener(selectionListener);
  }

  /**
   * Creates a new text tool item.
   * 
   * @param text the text
   * @param iconStyle the icon style
   */
  public TextToolItem(String text, String iconStyle) {
    this();
    setText(text);
    setIconStyle(iconStyle);
  }

  /**
   * Adds a selection listener.
   * 
   * @param listener the selection listener
   */
  public void addSelectionListener(SelectionListener listener) {
    addListener(Events.Select, listener);
  }

  /**
   * Returns the item's icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return button.getIconStyle();
  }

  /**
   * Returns the item's menu (if it has one).
   * 
   * @return the menu
   */
  public Menu getMenu() {
    return button.getMenu();
  }

  /**
   * Returns the button's text.
   * 
   * @return the text
   */
  public String getText() {
    return button.getText();
  }

  /**
   * Returns the item's parent tool bar.
   * 
   * @return the toolbar
   */
  public ToolBar getToolBar() {
    return toolBar;
  }

  @Override
  public ToolTip getToolTip() {
    return button.getToolTip();
  }

  /**
   * Removes a selection listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeSelectionListener(SelectionListener listener) {
    removeListener(Events.Select, listener);
  }

  /**
   * Sets the item's icon style. The style name should match a CSS style that
   * specifies a background image using the following format:
   * 
   * <pre><code>
   * .my-icon {
   *    background: url(images/icons/my-icon.png) no-repeat center left !important;
   * }
   * </code></pre>
   * 
   * @param iconStyle the icon style
   */
  public void setIconStyle(String iconStyle) {
    button.setIconStyle(iconStyle);
  }

  /**
   * Sets the item's menu.
   * 
   * @param menu the menu
   */
  public void setMenu(Menu menu) {
    button.setMenu(menu);
  }

  /**
   * Sets the item's text.
   * 
   * @param text the text
   */
  public void setText(String text) {
    button.setText(text);
  }

  @Override
  public void setToolTip(String text) {
    button.setToolTip(text);
  }

  @Override
  public void setToolTip(ToolTipConfig config) {
    button.setToolTip(config);
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(button);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(button);
  }

  protected void onButtonSelect(ButtonEvent be) {
    ToolBarEvent evt = new ToolBarEvent(toolBar, this);
    evt.event = be.event;
    fireEvent(Events.Select, evt);
  }

  @Override
  protected void onDisable() {
    button.disable();
  }

  @Override
  protected void onEnable() {
    button.enable();
  }

  @Override
  protected void onRender(Element target, int index) {
    button.render(target, index);
    button.addListener(Events.Select, new Listener<ButtonEvent>() {
      public void handleEvent(ButtonEvent be) {
        onButtonSelect(be);
      }
    });
    setElement(button.getElement());
  }

}
