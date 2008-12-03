/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A base editor field that handles displaying/hiding on demand and has some
 * built-in sizing and event handling logic.
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>BeforeStartEdit</b> : EditorEvent(editor, boundEl, value)<br>
 * <div>Fires when editing is initiated, but before the value changes.</div>
 * <ul>
 * <li>editor : this</li>
 * <li>boundEl : the underlying element bound to this editor</li>
 * <li>value : the field value being set</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>StartEdit</b> : EditorEvent(editor, value)<br>
 * <div>Fires when this editor is displayed.</div>
 * <ul>
 * <li>editor : this</li>
 * <li>value : the starting field value</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeComplete</b> : EditorEvent(editor, value, startValue)<br>
 * <div>Fires after a change has been made to the field, but before the change
 * is reflected in the underlying field.</div>
 * <ul>
 * <li>editor : this</li>
 * <li>value : the current field value</li>
 * <li>startValue : the original field value</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Complete</b> : EditorEvent(editor, value, startValue)<br>
 * <div>Fires after editing is complete and any changed value has been written
 * to the underlying field.</div>
 * <ul>
 * <li>editor : this</li>
 * <li>value : the current field value</li>
 * <li>startValue : the original field value</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>SpecialKey</b> : EditorEvent(field)<br>
 * <div>Fires when any key related to navigation (arrows, tab, enter, esc, etc.)
 * is pressed.</div>
 * <ul>
 * <li>editor : this</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class Editor extends BoxComponent {

  private boolean revertInvalid;
  private String alignment = "c-c";
  private boolean constrain;
  private boolean swallowKeys = true;
  private boolean completeOnEnter = true;
  private boolean cancelOnEsc;
  private boolean editing;
  private boolean updateEl = false;
  private Field field;
  private Object startValue;
  private Listener<FieldEvent> listener;
  private El boundEl;
  private boolean allowBlur = false;

  /**
   * Creates a new editor.
   * 
   * @param field the field
   */
  public Editor(Field field) {
    this.field = field;
    setShadow(false);
  }

  /**
   * Cancels the editing process and hides the editor without persisting any
   * changes. The field value will be reverted to the original starting value.
   */
  public void cancelEdit() {
    cancelEdit(false);
  }

  /**
   * Ends the editing process, persists the changed value to the underlying
   * field, and hides the editor.
   */
  public void completeEdit() {
    completeEdit(false);
  }

  /**
   * Returns the editor's alignemnt.
   * 
   * @return the alignment
   */
  public String getAlignment() {
    return alignment;
  }

  /**
   * Returns the editor's field.
   * 
   * @return the field
   */
  public Field getField() {
    return field;
  }

  /**
   * Returns the data value of the editor.
   * 
   * @return the value
   */
  public Object getValue() {
    return field.getValue();
  }

  /**
   * Returns true if blurs are allowed.
   * 
   * @return the allow blur state
   */
  public boolean isAllowBlur() {
    return allowBlur;
  }

  /**
   * Returns true if cancel on escape is enabled.
   * 
   * @return the cancel on escape state
   */
  public boolean isCancelOnEsc() {
    return cancelOnEsc;
  }

  /**
   * Returns true if complete on enter is enabled.
   * 
   * @return the complete on enter state
   */
  public boolean isCompleteOnEnter() {
    return completeOnEnter;
  }

  /**
   * Returns true if the editor is constrained to the viewport.
   * 
   * @return the constrain state
   */
  public boolean isConstrain() {
    return constrain;
  }

  /**
   * Returns true if key presses are being swallowed.
   * 
   * @return the swallow key state
   */
  public boolean isSwallowKeys() {
    return swallowKeys;
  }

  /**
   * Returns true if the inner HTML of the bound element is updated when the
   * update is complete.
   * 
   * @return the update element state
   */
  public boolean isUpdateEl() {
    return updateEl;
  }

  /**
   * Called after the editor completes an edit.
   * 
   * @param value the value from the editor
   * @return the updated value
   */
  public Object postProcessValue(Object value) {
    return value;
  }

  /**
   * Called before the editor sets the value on the wrapped field.
   * 
   * @param value the editor value
   * @return the updated value
   */
  public Object preProcessValue(Object value) {
    return value;
  }

  /**
   * Realigns the editor to the bound field based on the current alignment
   * config value.
   */
  public void realign() {
    el().alignTo(boundEl.dom, alignment, null);
  }

  /**
   * The position to align to (see {@link El#alignTo} for more details, defaults
   * to "c-c?").
   * 
   * @param alignment the alignment
   */
  public void setAlignment(String alignment) {
    this.alignment = alignment;
  }

  /**
   * Sets whether editing should be cancelled when the field is blurred
   * (defaults to false).
   * 
   * @param allowBlur true to allow blur
   */
  public void setAllowBlur(boolean allowBlur) {
    this.allowBlur = allowBlur;
  }

  /**
   * True to cancel the edit when the escape key is pressed (defaults to false).
   * 
   * @param cancelOnEsc true to cancel on escape
   */
  public void setCancelOnEsc(boolean cancelOnEsc) {
    this.cancelOnEsc = cancelOnEsc;
  }

  /**
   * True to complete the edit when the enter key is pressed (defaults to
   * false).
   * 
   * @param completeOnEnter true to complete on enter
   */
  public void setCompleteOnEnter(boolean completeOnEnter) {
    this.completeOnEnter = completeOnEnter;
  }

  /**
   * True to constrain the editor to the viewport.
   * 
   * @param constrain true to constrian
   */
  public void setConstrain(boolean constrain) {
    this.constrain = constrain;
  }

  @Override
  public void setSize(int width, int height) {
    field.setSize(width, height);
    if (rendered) {
      el().sync(true);
    }
  }

  /**
   * Handle the keypress events so they don't propagate (defaults to true).
   * 
   * @param swallowKeys true to swallow key press events.
   */
  public void setSwallowKeys(boolean swallowKeys) {
    this.swallowKeys = swallowKeys;
  }

  /**
   * True to update the innerHTML of the bound element when the update completes
   * (defaults to false).
   * 
   * @param updateEl true to update the inner HTML
   */
  public void setUpdateEl(boolean updateEl) {
    this.updateEl = updateEl;
  }

  /**
   * Sets the data value of the editor
   * 
   * @param value any valid value supported by the underlying field
   */
  public void setValue(Object value) {
    field.setValue(value);
  }

  /**
   * Starts the editing process and shows the editor.
   * 
   * @param el the element to edit
   */
  public void startEdit(Element el, Object value) {
    if (editing) {
      completeEdit();
    }
    boundEl = new El(el);

    Object v = value != null ? value : boundEl.getInnerHtml();

    if (!rendered) {
      RootPanel.get().add(this);
    }

    ComponentHelper.doAttach(this);

    EditorEvent e = new EditorEvent(this);
    e.boundEl = boundEl;
    e.value = v;
    if (!fireEvent(Events.BeforeStartEdit, e)) {
      return;
    }

    startValue = value;
    field.setValue(preProcessValue(value));

    doAutoSize();

    editing = true;

    el().setVisible(true);
    el().makePositionable(true);
    el().alignTo(boundEl.dom, alignment, new int[] {-1, -1});

    show();
    field.focus();
  }

  protected void cancelEdit(boolean remainVisible) {
    if (editing) {
      setValue(startValue);
      if (!remainVisible) {
        hide();
      }
    }
  }

  protected void completeEdit(boolean remainVisible) {
    if (!editing) {
      return;
    }

    Object v = getValue();
    if (revertInvalid && field.isValid()) {
      v = startValue;
      cancelEdit(true);
    }
    if (v == startValue) {
      // ignore no change
      editing = false;
      hide();
      return;
    }

    field.clearInvalid();

    EditorEvent e = new EditorEvent(this);
    e.value = postProcessValue(v);
    e.startValue = startValue;

    if (fireEvent(Events.BeforeComplete, e)) {
      editing = false;
      if (updateEl && boundEl != null) {
        boundEl.setInnerHtml(v.toString());
      }

      if (!remainVisible) {
        hide();
      }
      fireEvent(Events.Complete, e);
    }

  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(field);
  }

  protected void doAutoSize() {
    setSize(boundEl.getWidth(), -1);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(field);
  }

  protected void onBlur(FieldEvent fe) {
    if (!allowBlur && editing) {
      completeEdit();
    }
  }

  @Override
  protected void onHide() {
    if (rendered) {
      ComponentHelper.doDetach(this);
    }
    if (editing) {
      completeEdit();
      return;
    }
    field.blur();
    el().setVisible(false);
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);

    setElement(DOM.createDiv(), target, index);
    setStyleName("x-editor");
    el().setVisibility(true);

    setStyleAttribute("overflow", GXT.isGecko ? "auto" : "hidden");
    if (!field.getMessageTarget().equals("title")) {
      field.setMessageTarget("gtip");
    }

    field.setMessageTarget("tooltip");

    field.render(getElement());

    if (GXT.isGecko) {
      field.getElement().setAttribute("autocomplete", "off");
    }

    listener = new Listener<FieldEvent>() {

      public void handleEvent(FieldEvent fe) {
        switch (fe.type) {
          case Events.SpecialKey:
            onSpecialKey(fe);
            break;
          case Events.Blur:
            onBlur(fe);
            break;
        }
      }
    };

    this.field.addListener(Events.SpecialKey, listener);
    this.field.addListener(Events.Blur, listener);

    field.show();
  }

  @Override
  protected void onShow() {
    el().setVisible(true);
    el().updateZIndex(100);
    field.show();
    EditorEvent e = new EditorEvent(this);
    e.boundEl = boundEl;
    e.value = startValue;
    fireEvent(Events.StartEdit, e);
  }

  protected void onSpecialKey(FieldEvent fe) {

    if (completeOnEnter && fe.getKeyCode() == KeyboardListener.KEY_ENTER) {
      // ugly
      if (field instanceof ComboBox) {
        ComboBox box = (ComboBox) field;
        if (box.isExpanded()) {
          return;
        }
      }
      fe.stopEvent();
      completeEdit();
    } else if (cancelOnEsc && fe.getKeyCode() == KeyboardListener.KEY_ESCAPE) {
      cancelEdit();
    } else {
      fireEvent(Events.SpecialKey, fe);
    }
  }

}
