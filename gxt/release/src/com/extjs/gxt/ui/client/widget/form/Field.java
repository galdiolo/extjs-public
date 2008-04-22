/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.IconButton;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * Base class for form fields that provides default event handling, value
 * handling and other functionality.
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Focus</b> : FieldEvent(field)<br>
 * <div>Fires when this field receives input focus.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Blur</b> : FieldEvent(field)<br>
 * <div>Fires when this field loses input focus.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Change</b> : FieldEvent(field, value, oldValue)<br>
 * <div>Fires just before the field blurs if the field value has changed.</div>
 * <ul>
 * <li>field : this</li>
 * <li>value : the new value</li>
 * <li>oldValue : the old value</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Invalid</b> : FieldEvent(field, message)<br>
 * <div>Fires after the field has been marked as invalid.</div>
 * <ul>
 * <li>field : this</li>
 * <li>message : the validation message</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Valid</b> : FieldEvent(field, name)<br>
 * <div>Fires after the field has been validated with no errors.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>KeyPress</b> : FieldEvent(field, name)<br>
 * <div>Fires after a key is pressed</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * </dl>
 */
public abstract class Field extends BoxComponent {

  /**
   * The field's HTML name attribute (defaults to null).
   */
  public String name;

  /**
   * The label text to display next to this field (defaults to '')
   */
  public String fieldLabel = "";

  /**
   * A CSS style specification to apply directly to this field's label (defaults
   * to the container's labelStyle value if set, or ''). For example,
   * <code>labelStyle: 'font-weight:bold;'</code>
   */
  public String labelStyle = "";

  /**
   * The error text to use when marking a field invalid and no message is
   * provided (defaults to "The value in this field is invalid").
   */
  public String invalidText = GXT.MESSAGES.field_invalidText();

  protected boolean autoValidate = true;
  protected int validationDelay = 250;
  protected String emptyText;
  protected IconButton errorIcon;
  protected Object value;
  protected String focusStyle = "x-form-focus";

  private String fieldStyle = "x-form-field";
  private String invalidStyle = "x-form-invalid";
  private String messageTarget = "side";
  private boolean validateOnBlur = true;
  private boolean readOnly;
  private Object focusValue;
  private Object originalValue;
  private boolean hasFocus;
  private int tabIndex = Style.DEFAULT;

  public Field() {
    adjustSize = false;
  }

  /**
   * Clear any invalid styles / messages for this field.
   */
  public void clearInvalid() {
    if (!rendered) {
      return;
    }
    getInputEl().removeStyleName(invalidStyle);
    if ("side".equals(messageTarget)) {
      if (errorIcon != null && errorIcon.isAttached()) {
        WidgetHelper.doDetach(errorIcon);
        errorIcon.setVisible(false);
      }
    } else if ("title".equals(messageTarget)) {
      setTitle("");
    } else if ("tooltip".equals(messageTarget)) {
      hideToolTip();
    } else {
      Element elem = DOM.getElementById(messageTarget);
      if (elem != null) {
        fly(elem).setInnerHtml("");
      }
    }
    fireEvent(Events.Valid, new FieldEvent(this));
  }

  /**
   * Returns true if the field value is validated on each key press.
   * 
   * @return the auto validate state
   */
  public boolean getAutoValidate() {
    return autoValidate;
  }

  /**
   * Returns the field's empty text.
   * 
   * @return the empty text
   */
  public String getEmptyText() {
    return emptyText;
  }

  /**
   * Returns the field's message target.
   * 
   * @return
   */
  public String getMessageTarget() {
    return messageTarget;
  }

  /**
   * Returns the name attribute of the field if available.
   * 
   * @return the field name
   */
  public String getName() {
    return rendered ? getInputEl().getElementAttribute("name") : name;
  }

  /**
   * Returns the raw data value which may or may not be a valid, defined value.
   * To return a normalized value see {@link #getValue}.
   * 
   * @return the raw value
   */
  public String getRawValue() {
    String v = rendered ? getInputEl().getValue() : "";
    if (v.equals(emptyText)) {
      return "";
    }
    return v;
  }

  /**
   * Returns true if the value is validate on blur.
   * 
   * @return the validate on blur state
   */
  public boolean getValidateOnBlur() {
    return validateOnBlur;
  }

  /**
   * Returns the field's validation delay in milliseconds.
   * 
   * @return the delay in millseconds
   */
  public int getValidationDelay() {
    return validationDelay;
  }

  /**
   * Returns the normalized data value (undefined or emptyText will be returned
   * as ''). To return the raw value see {@link #getRawValue}.
   * 
   * @return the fields value
   */
  public Object getValue() {
    if (!rendered) {
      return value;
    }
    String v = getInputEl().getValue();
    if (emptyText != null && v.equals(emptyText)) {
      return "";
    }
    return v;
  }

  /**
   * Returns <code>true</code> if this field has been changed since it was
   * originally loaded and is not disabled.
   * 
   * @return the dirty state
   */
  public boolean isDirty() {
    if (!isEnabled()) {
      return false;
    }
    return !getValue().equals(originalValue);
  }

  /**
   * Returns the read only state.
   * 
   * @return <code>true</code> if read only, otherwise <code>false</code>
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * Returns whether or not the field value is currently valid.
   * 
   * @return <code>true</code> if the value is valid, otherwise
   *         <code>false</code>
   */
  public boolean isValid() {
    if (disabled) {
      return true;
    }
    return validateValue(getRawValue());
  }

  public void onComponentEvent(ComponentEvent ce) {
    FieldEvent fe = new FieldEvent(this);
    fe.event = ce.event;
    switch (ce.type) {
      case Event.ONFOCUS:
        onFocus(ce);
        break;
      case Event.ONBLUR:
        onBlur(ce);
        break;
      case Event.ONKEYUP:
        fe.type = Events.KeyPress;
        onKeyPress(fe);
        fireEvent(Events.KeyPress, fe);
        break;
      case Event.ONCLICK:
        onClick(ce);
    }
  }

  /**
   * Resets the current field value to the originally loaded value and clears
   * any validation messages.
   */
  public void reset() {
    setValue(originalValue);
    clearInvalid();
  }

  /**
   * Sets whether the value is validated on each key press (defaults to false).
   * 
   * @param autoValidate true to validate on each key press
   */
  public void setAutoValidate(boolean autoValidate) {
    this.autoValidate = autoValidate;
  }

  /**
   * Sets the default text to display in an empty field.
   * 
   * @param emptyText the empty text
   */
  public void setEmptyText(String emptyText) {
    this.emptyText = emptyText;
  }

  /**
   * The location where error text should display. Should be one of the
   * following values (defaults to 'side'): <code><pre>
   * Value         Description
   * -----------   ----------------------------------------------------------------------
   * tooltip       Display a tool tip when the user hovers over the field
   * title         Display a default browser title attribute popup
   * side          Add an error icon to the right of the field with a popup on hover
   * [element id]  Add the error text directly to the innerHTML of the specified element
   * </pre></code>
   * 
   * @param messageTarget the message target
   */
  public void setMessageTarget(String messageTarget) {
    this.messageTarget = messageTarget;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the underlying DOM field's value directly, bypassing validation. To
   * set the value with validation see {@link #setValue}.
   * 
   * @param value the raw value
   */
  public void setRawValue(String value) {
    if (rendered) {
      getInputEl().setValue(value == null ? "" : value);
    }
  }

  /**
   * Sets the field's read only state.
   * 
   * @param readOnly the read only state
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
    if (isRendered()) {
      DOM.setElementProperty(getElement(), "readOnly", "" + readOnly);
    }
  }

  /**
   * Sets the tab index.
   * 
   * @param index the tab index value
   */
  public void setTabIndex(int index) {
    this.tabIndex = index;
    if (rendered) {
      getInputEl().setElementAttribute("tabIndex", index);
    }
  }

  /**
   * Sets whether the field should validate when it loses focus (defaults to
   * true).
   * 
   * @param validateOnBlur true to validate on blur, otherwise false
   */
  public void setValidateOnBlur(boolean validateOnBlur) {
    this.validateOnBlur = validateOnBlur;
  }

  /**
   * Sets length of time in milliseconds after user input begins until
   * validation is initiated (defaults to 250).
   * 
   * @param validationDelay the delay in milliseconds
   */
  public void setValidationDelay(int validationDelay) {
    this.validationDelay = validationDelay;
  }

  /**
   * Sets a data value into the field and validates it. To set the value
   * directly without validation see {@link #setRawValue}.
   * 
   * @param value the value to set
   */
  public void setValue(Object value) {
    Object oldValue = getValue();
    this.value = value;
    fireChangeEvent(oldValue, value);
    if (rendered) {
      getInputEl().setValue(value == null ? "" : value.toString());
      validate();
    }
  }

  /**
   * Validates the field value.
   * 
   * @return <code>true</code> if valid, otherwise <code>false</code>
   */
  public boolean validate() {
    if (!isEnabled() || validateValue(getRawValue())) {
      clearInvalid();
      return true;
    }
    return false;
  }

  protected int adjustWidth(String tag, int w) {
    tag = tag.toLowerCase();
    if (!GXT.isSafari) {
      if (GXT.isIE && tag.equals("input") || tag.equals("textarea")) {
        if (tag.equals("input") && GXT.isStrict) {
          return w - (!GXT.isIE7 ? 4 : 1);
        }
        if (tag.equals("textarea") && GXT.isStrict) {
          return w - 2;
        }
      } else if (GXT.isOpera && GXT.isStrict) {
        if (tag.equals("input")) {
          return w + 2;
        }
        if (tag.equals("textarea")) {
          return w - 2;
        }
      }
    }
    return w;
  }

  protected void alignErrorIcon() {
    errorIcon.el.alignTo(getElement(), "tl-tr", new int[] {2, 1});
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    if (errorIcon != null && errorIcon.isAttached()) {
      WidgetHelper.doDetach(errorIcon);
    }
  }

  protected void fireChangeEvent(Object oldValue, Object value) {
    if (!value.equals(oldValue)) {
      FieldEvent e = new FieldEvent(this);
      e.oldValue = oldValue;
      e.value = value;
      fireEvent(Events.Change, e);
    }
  }

  /**
   * Provides support for wrapping the actual input element.
   * 
   * @return the input element
   */
  protected El getInputEl() {
    return el;
  }

  protected El getStyleEl() {
    return el;
  }

  /**
   * Mark this field as invalid.
   * 
   * @param msg the validation message
   */
  protected void markInvalid(String msg) {
    if (!rendered) {
      return;
    }
    getInputEl().addStyleName(invalidStyle);
    msg = msg == null ? invalidText : msg;

    if (messageTarget.equals("side")) {
      if (errorIcon == null) {
        errorIcon = new IconButton("x-form-invalid-icon");
        Element p = el.getParent().dom;
        errorIcon.render(p);
        errorIcon.removeStyleName("x-tool");
        errorIcon.el.setVisibility(true);
      }
      errorIcon.setVisible(true);
      errorIcon.setToolTip(msg);
      errorIcon.getToolTip().addStyleName("x-form-invalid-tip");
      alignErrorIcon();

      if (!errorIcon.isAttached()) {
        WidgetHelper.doAttach(errorIcon);
      }

    } else if (messageTarget.equals("title")) {
      setTitle(msg);
    } else if ("tooltip".equals(messageTarget)) {
      setToolTip(msg);
    } else {
      Element elem = DOM.getElementById(messageTarget);
      if (elem != null) {
        fly(elem).setInnerHtml(msg);
      }
    }

    FieldEvent fe = new FieldEvent(this);
    fe.message = msg;
    fireEvent(Events.Invalid, fe);
  }

  protected void onBlur(ComponentEvent be) {
    if (!GXT.isOpera && focusStyle != null) {
      getFocusEl().removeStyleName(focusStyle);
    }
    hasFocus = false;
    if (!autoValidate && validateOnBlur) {
      validate();
    }
    fireChangeEvent(focusValue, getValue());
    fireEvent(Events.Blur, new FieldEvent(this));
  }

  protected void onClick(ComponentEvent ce) {

  }

  protected void onFocus(ComponentEvent ce) {
    if (!GXT.isOpera && focusStyle != null) {
      getInputEl().addStyleName(focusStyle);
    }
    if (!hasFocus) {
      hasFocus = true;
      focusValue = getValue();
      fireEvent(Events.Focus, new FieldEvent(this));
    }
  }

  protected void onKeyPress(FieldEvent fe) {

  }

  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);

    addStyleName(fieldStyle);

    String type = getInputEl().getElementAttribute("type");
    getInputEl().addStyleName("x-form-" + type);

    if (name != null) {
      getInputEl().setElementAttribute("name", name);
    }

    if (readOnly) {
      setReadOnly(true);
    }

    if (tabIndex != Style.DEFAULT) {
      el.setIntElementProperty("tabIndex", tabIndex);
    }

    originalValue = getValue();

    getInputEl().addEventsSunk(Event.ONCLICK | Event.KEYEVENTS | Event.FOCUSEVENTS);

    if (value != null) {
      setValue(value);
    }
  }

  /**
   * Subclasses should provide the validation implementation by overriding this.
   * 
   * @param value the value to validate
   * @return <code>true</code> for valid
   */
  protected boolean validateValue(String value) {
    return true;
  }
}
