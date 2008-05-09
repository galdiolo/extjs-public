/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ButtonBarEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A horizontal row of buttons.
 * 
 * <dt><b>Events:</b></dt>
 * <dd><b>Select</b> : ButtonBarEvent(buttonBar, button)<br>
 * <div>Fires when a button is selected.</div>
 * <ul>
 * <li>buttonBar : the button bar</li>
 * <li>button : the button that was clicked</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeAdd</b> : ButtonEvent(buttonBar, item, index)<br>
 * <div>Fires before a widget is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>buttonBar : this</li>
 * <li>item : the widget being added</li>
 * <li>index : the index at which the widget will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : ButtonEvent(buttonBar, button)<br>
 * <div>Fires before a widget is removed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>buttonBar : this</li>
 * <li>button : the button being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : ButtonEvent(buttonBar, button, index)<br>
 * <div>Fires after a widget has been added or inserted.</div>
 * <ul>
 * <li>buttonBar : this</li>
 * <li>button : the button that was added</li>
 * <li>index : the index at which the button was added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : ButtonEvent(buttonBar, button)<br>
 * <div>Fires after a widget has been removed.</div>
 * <ul>
 * <li>buttonBar : this</li>
 * <li>button : the button that was removed</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-btn-bar (the button bar itself)</dd>
 * </dl>
 */
public class ButtonBar extends AbstractContainer<Button> {

  private int buttonWidth = 75;
  private HorizontalAlignment buttonAlign = HorizontalAlignment.LEFT;
  private Button buttonPressed;
  private HorizontalPanel panel;
  private Listener listener = new Listener<ButtonEvent>() {
    public void handleEvent(ButtonEvent be) {
      switch (be.type) {
        case Events.BeforeSelect:
          buttonPressed = be.button;
          break;
        case Events.Select:
          onButtonPressed(be);
          break;
      }
    }
  };

  /**
   * Creates a left aligned button bar.
   */
  public ButtonBar() {
    baseStyle = "x-button-bar";
    panel = new HorizontalPanel();
    panel.setLayoutOnChange(true);
  }

  /**
   * Adds a button to the bar. Fires the <i>BeforeAdd</i> event before
   * inserting, then fires the <i>Add</i> event after the widget has been
   * inserted.
   * 
   * @param button the button to be added
   */
  public void add(Button button) {
    insert(button, getItemCount());
  }

  /**
   * Returns the button at the specified index.
   * 
   * @param index the index
   * @return the button or <code>null</code>
   */
  public Button getButton(int index) {
    return getItem(index);
  }

  /**
   * Returns the bar's horizontal alignment.
   * 
   * @return the buttonAlign the alignment
   */
  public HorizontalAlignment getButtonAlign() {
    return buttonAlign;
  }

  /**
   * Returns the button with the specified button id.
   * 
   * @param buttonId the button id
   * @return the button or <code>null</code> if no match
   */
  public Button getButtonById(String buttonId) {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      Button btn = getButton(i);
      if (btn.getItemId().equals(buttonId)) {
        return btn;
      }
    }
    return null;
  }

  /**
   * Returns the last button that was selected.
   * 
   * @return the last button or <codee>null</code>
   */
  public Button getButtonPressed() {
    return buttonPressed;
  }

  /**
   * @return the buttonWidth
   */
  public int getButtonWidth() {
    return buttonWidth;
  }

  /**
   * Inserts a button at the specified location. Fires the <i>BeforeAdd</i>
   * event before inserting, then fires the <i>Add</i> event after the widget
   * has been inserted.
   * 
   * @param button the button to be inserted
   * @param index the insert location
   */
  public void insert(Button button, int index) {
    ButtonBarEvent bbe = new ButtonBarEvent(this);
    bbe.button = button;
    bbe.index = index;
    if (fireEvent(Events.BeforeAdd, bbe)) {
      super.insert(button, index);
      panel.insert(button, index);
      button.addListener(Events.BeforeSelect, listener);
      button.addListener(Events.Select, listener);
      // if (rendered) {
      // renderButton(button, index);
      // }
      fireEvent(Events.Add, bbe);
    }
  }

  /**
   * Removes a button from the bar.
   * 
   * @param button the button to be removed
   */
  public boolean remove(Button button) {
    ButtonBarEvent be = new ButtonBarEvent(this);
    be.button = button;

    if (fireEvent(Events.BeforeRemove, be)) {
      button.removeListener(Events.Select, listener);
      button.removeListener(Events.BeforeSelect, listener);
      panel.remove(button);
      fireEvent(Events.Remove, be);
    }
    return false;
  }

  /**
   * Removes all the buttons.
   */
  public void removeAll() {
    while (getItemCount() > 0) {
      remove(getButton(0));
    }
  }

  /**
   * Sets the bar's horizontal alignment.
   * 
   * @param buttonAlign the alignment
   */
  public void setButtonAlign(HorizontalAlignment buttonAlign) {
    this.buttonAlign = buttonAlign;
  }

  /**
   * @param buttonWidth the buttonWidth to set
   */
  public void setButtonWidth(int buttonWidth) {
    this.buttonWidth = buttonWidth;
  }

  protected void onButtonPressed(ButtonEvent be) {
    Button btn = be.button;
    buttonPressed = btn;
    ButtonBarEvent bbe = new ButtonBarEvent(this);
    bbe.button = be.button;
    fireEvent(Events.Select, bbe);
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    for (int i = 0; i < getItemCount(); i++) {
      getButton(i).disable();
    }
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    for (int i = 0; i < getItemCount(); i++) {
      getButton(i).enable();
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv(), target, index);
    setStyleAttribute("overflow", "visible");
    addStyleName("x-panel-btns-ct");
    el.setHeight(24);

    El wrap = el.insertFirst("<div class='x-panel-btns x-panel-btns-" + getButtonAlign()
        + "'></div>");

    panel.setHorizontalAlign(getButtonAlign());
    panel.setVerticalAlign(VerticalAlignment.MIDDLE);

    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      Button button = getButton(i);
      renderButton(button, i);
    }

    panel.render(wrap.dom);
  }

  protected void renderButton(Button button, int index) {
    TableData data = new TableData();
    data.style = "x-panel-btn-td";
    button.setData(data);
    panel.insert(button, index);
    button.setMinWidth(getButtonWidth());
  }

}
