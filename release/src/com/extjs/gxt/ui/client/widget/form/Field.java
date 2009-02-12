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
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * Base class for form fields that provides default event handling, value
 * handling and other functionality.
 * <dl>
 * <dt>Events:</dt>
 * <dd><b>Focus</b> : FieldEvent(field)<br>
 * <div>Fires when this field receives input focus.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * <dd><b>Blur</b> : FieldEvent(field)<br>
 * <div>Fires when this field loses input focus.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * <dd><b>Change</b> : FieldEvent(field, value, oldValue)<br>
 * <div>Fires after the field's value is changed.</div>
 * <ul>
 * <li>field : this</li>
 * <li>value : the new value</li>
 * <li>oldValue : the old value</li>
 * </ul>
 * </dd>
 * <dd><b>Invalid</b> : FieldEvent(field, message)<br>
 * <div>Fires after the field has been marked as invalid.</div>
 * <ul>
 * <li>field : this</li>
 * <li>message : the validation message</li>
 * </ul>
 * </dd>
 * <dd><b>Valid</b> : FieldEvent(field)<br>
 * <div>Fires after the field has been validated with no errors.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * <dd><b>KeyPress</b> : FieldEvent(field)<br>
 * <div>Fires after a key is pressed.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * <dd><b>SpecialKey</b> : FieldEvent(field)<br>
 * <div>Fires when any key related to navigation (arrows, tab, enter, esc, etc.)
 * is pressed.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * <dl>
 * <dt>Inherited Events:</dt>
 * <dd>BoxComponent Move</dd>
 * <dd>BoxComponent Resize</dd>
 * <dd>Component Enable</dd>
 * <dd>Component Disable</dd>
 * <dd>Component BeforeHide</dd>
 * <dd>Component Hide</dd>
 * <dd>Component BeforeShow</dd>
 * <dd>Component Show</dd>
 * <dd>Component Attach</dd>
 * <dd>Component Detach</dd>
 * <dd>Component BeforeRender</dd>
 * <dd>Component Render</dd>
 * <dd>Component BrowserEvent</dd>
 * <dd>Component BeforeStateRestore</dd>
 * <dd>Component StateRestore</dd>
 * <dd>Component BeforeStateSave</dd>
 * <dd>Component SaveState</dd>
 * </dl>
 * 
 * @param <D> the data type of the field
 */
public abstract class Field<D> extends BoxComponent {

  /**
   * The field messages.
   */
  public class FieldMessages {

    private String invalidText = GXT.MESSAGES.field_invalidText();

    /**
     * Returns the invalid text.
     * 
     * @return the text
     */
    public String getInvalidText() {
      return invalidText;
    }

    /**
     * Sets the error text to use when marking a field invalid and no message is
     * provided (defaults to "The value in this field is invalid").
     * 
     * @param invalidText the invalid text
     */
    public void setInvalidText(String invalidText) {
      this.invalidText = invalidText;
    }

  }

  protected String forceInvalidText;
  protected boolean autoValidate;
  protected int validationDelay = 250;
  protected String emptyText;
  protected IconButton errorIcon;
  protected D value;
  protected String focusStyle = "x-form-focus";
  protected String invalidStyle = "x-form-invalid";
  protected String fieldStyle = "x-form-field";
  protected boolean readOnly;
  protected FieldMessages messages = new FieldMessages();
  protected PropertyEditor<D> propertyEditor;
  protected boolean hasFocus;
  protected Object focusValue;
  protected D originalValue;
 
  private String labelStyle = "";
  private String name;
  private String fieldLabel = "";
  private String messageTarget = "side";
  private boolean validateOnBlur = true;
  private int tabIndex = 0;
  private String labelSeparator;
  private String inputStyle;
  private boolean hideLabel;

  /**
   * Creates a new field.
   */
  protected Field() {
    adjustSize = false;
    propertyEditor = (PropertyEditor<D>) PropertyEditor.DEFAULT;
  }

  /**
   * Adds a CSS style name to the input element of this field.
   * 
   * @param style the style name
   */
  public void addInputStyleName(String style) {
    if (rendered) {
      El inputEl = getInputEl();
      if (inputEl != null) {
        inputEl.addStyleName(style);
      }
    } else {
      inputStyle = inputStyle == null ? style : inputStyle + " " + style;
    }
  }

  /**
   * Adds a key listener.
   * 
   * @param listener the key listener
   */
  public void addKeyListener(KeyListener listener) {
    addListener(Events.KeyPress, listener);
    addListener(Events.KeyUp, listener);
    addListener(Events.KeyDown, listener);
  }

  /**
   * Clear any invalid styles / messages for this field.
   */
  public void clearInvalid() {
    if (!rendered) {
      return;
    }
    getInputEl().removeStyleName(invalidStyle);

    if (forceInvalidText != null) {
      forceInvalidText = null;
    }

    if ("side".equals(messageTarget)) {
      if (errorIcon != null && errorIcon.isAttached()) {
        ComponentHelper.doDetach(errorIcon);
        errorIcon.setVisible(false);
      }
    } else if ("title".equals(messageTarget)) {
      setTitle("");
    } else if ("tooltip".equals(messageTarget)) {
      hideToolTip();
      if (toolTip != null) {
        toolTip.disable();
      }
    } else {
      Element elem = DOM.getElementById(messageTarget);
      if (elem != null) {
        elem.setInnerHTML("");
      }
    }
    fireEvent(Events.Valid, new FieldEvent(this));
  }

  @Override
  public void focus() {
    super.focus();
    if (rendered) {
      onFocus(new FieldEvent(this));
    }
  }

  /**
   * Forces the field to be invalid using the given error message. When using
   * this feature, {@link #clearInvalid()} must be called to clear the error.
   * Also, no other validation logic will execute.
   * 
   * @param msg the error text
   */
  public void forceInvalid(String msg) {
    forceInvalidText = msg;
    markInvalid(msg);
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
   * Returns the field's label.
   * 
   * @return the label
   */
  public String getFieldLabel() {
    return fieldLabel;
  }

  /**
   * Returns the field's label separator.
   * 
   * @return the label separator
   */
  public String getLabelSeparator() {
    return labelSeparator;
  }

  /**
   * Returns the field's label style.
   * 
   * @return the label style
   */
  public String getLabelStyle() {
    return labelStyle;
  }

  /**
   * Returns the field's messages.
   * 
   * @return the messages
   */
  public FieldMessages getMessages() {
    return messages;
  }

  /**
   * Returns the field's message target.
   * 
   * @return the message target
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
    if (rendered) {
      String n = getInputEl().dom.getAttribute("name");
      if (!n.equals("")) {
        return n;
      }
    }
    return name;
  }

  /**
   * Returns the original value of the field, which is the value of the field
   * when it is first rendered.
   * 
   * @return the original value
   */
  public D getOriginalValue() {
    return originalValue;
  }

  /**
   * Returns the field's property editor.
   * 
   * @return the property editor
   */
  public PropertyEditor<D> getPropertyEditor() {
    return propertyEditor;
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
   * Returns the typed value of the field.
   * 
   * @return the fields value
   */
  public D getValue() {
    if (!rendered) {
      return value;
    }
    String v = getRawValue();
    if (emptyText != null && v.equals(emptyText)) {
      return null;
    }
    if (v == null || v.equals("")) {
      return null;
    }
    try {
      return propertyEditor.convertStringValue(v);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Returns <code>true</code> if this field is dirty. A field is dirty, if the
   * current value is different than it's original value. The original value is
   * the value of the field when the field is rendered. Disabled and
   * pre-rendered fields are never dirty.
   * 
   * @return the dirty state
   */
  public boolean isDirty() {
    if (disabled || !rendered) {
      return false;
    }
    Object v = getValue();
    if (v == null) {
      return originalValue != null;
    }
    return !getValue().equals(originalValue);
  }

  /**
   * Returns true if the label is hidden.
   * 
   * @return the hidel label state
   */
  public boolean isHideLabel() {
    return hideLabel;
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

  /**
   * Marks this field as invalid. Validation will still run if called again, and
   * the error message will be changed or cleared based on validation. To set a
   * error message that will not be cleared until manually cleared see
   * {@link #forceInvalid(String)}
   * 
   * @param msg the validation message
   */
  public void markInvalid(String msg) {
    if (!rendered) {
      return;
    }
    getInputEl().addStyleName(invalidStyle);
    msg = XDOM.escapeHtml(msg == null ? getMessages().getInvalidText() : msg);

    if (messageTarget.equals("side")) {
      if (errorIcon == null) {
        errorIcon = new IconButton("x-form-invalid-icon");
        Element p = el().getParent().dom;
        errorIcon.render(p);
        errorIcon.removeStyleName("x-tool");
        errorIcon.el().setVisibility(true);
      }
      if (!errorIcon.isAttached()) {
        ComponentHelper.doAttach(errorIcon);
      }

      errorIcon.el().setVisible(true);
      alignErrorIcon();
      alignErrorIcon();// fixes weird initial render with ie
      errorIcon.setToolTip(msg);
      errorIcon.getToolTip().addStyleName("x-form-invalid-tip");

    } else if (messageTarget.equals("title")) {
      setTitle(msg);
    } else if ("tooltip".equals(messageTarget)) {
      setToolTip(msg);
      getToolTip().addStyleName("x-form-invalid-tip");
      getToolTip().enable();
    } else {
      Element elem = DOM.getElementById(messageTarget);
      if (elem != null) {
        elem.setInnerHTML(msg);
      }
    }

    FieldEvent fe = new FieldEvent(this);
    fe.message = msg;
    fireEvent(Events.Invalid, fe);
  }

  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    FieldEvent fe = new FieldEvent(this);
    fe.event = ce.event;
    switch (ce.type) {
      case Event.ONFOCUS:
        onFocus(ce);
        break;
      case Event.ONBLUR:
        onBlur(ce);
        break;
      case Event.ONCLICK:
        // in some cases, focus event is not fired when event preview was
        // active when the click is fired
        if (!hasFocus) focus();
        onClick(ce);
        break;
      case Event.ONKEYUP:
        onKeyUp(fe);
        break;
      case Event.ONKEYDOWN:
        onKeyDown(fe);
        if (GXT.isIE || GXT.isSafari) {
          fireKey(fe);
        }
        break;
      case Event.ONKEYPRESS:
        onKeyPress(fe);
        if (!GXT.isIE && !GXT.isSafari) {
          fireKey(fe);
        }
        break;
    }
  }

  /**
   * Removes a CSS style name from the input element of this field.
   * 
   * @param style the style name
   */
  public void removeInputStyleName(String style) {
    if (rendered) {
      El inputEl = getInputEl();
      if (inputEl != null) {
        inputEl.removeStyleName(style);
      }
    } else if (style != null) {
      String[] s = inputStyle.split(" ");
      inputStyle = "";
      for (int i = 0; i < s.length; i++) {
        if (!s[i].equals(style)) {
          inputStyle += " " + s[i];
        }
      }
    }
  }

  /**
   * Removes the key listener.
   * 
   * @param listener the key listener
   */
  public void removeKeyListener(KeyListener listener) {
    removeListener(Events.KeyPress, listener);
    removeListener(Events.KeyUp, listener);
    removeListener(Events.KeyDown, listener);
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
   * Sets the field's label.
   * 
   * @param fieldLabel the label
   */
  public void setFieldLabel(String fieldLabel) {
    this.fieldLabel = fieldLabel;
    if (rendered) {
      El elem = findLabelElement();
      if (elem != null) {
        elem.setInnerHtml(fieldLabel + labelSeparator);
      }
    }
  }

  /**
   * True to completely hide the label element (defaults to false, pre-render).
   * 
   * @param hideLabel true to hide the label
   */
  public void setHideLabel(boolean hideLabel) {
    this.hideLabel = hideLabel;
  }

  /**
   * The standard separator to display after the text of each form label
   * (defaults to the value of {@link FormLayout#setLabelSeparator(String)},
   * which is a colon ':' by default).
   * 
   * @param labelSeparator the label separator or "" for none
   */
  public void setLabelSeparator(String labelSeparator) {
    this.labelSeparator = labelSeparator;
  }

  /**
   * A CSS style specification to apply directly to this field's label. For
   * example, <code>labelStyle: 'font-weight:bold;'</code>
   * 
   * @param labelStyle the label style
   */
  public void setLabelStyle(String labelStyle) {
    assertPreRender();
    this.labelStyle = labelStyle;
  }

  /**
   * Sets the field's messages.
   * 
   * @param messages the messages
   */
  public void setMessages(FieldMessages messages) {
    this.messages = messages;
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

  /**
   * Sets the field's HTML name attribute.
   * 
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
    if (rendered) {
      getInputEl().dom.setAttribute("name", name);
    }
  }

  /**
   * Updates the original value of the field. The originalValue is the value of
   * the field when it is rendered. This method is useful when a form / field is
   * being reused and the originaValue needs to be reset.
   * 
   * @param originalValue
   */
  public void setOriginalValue(D originalValue) {
    this.originalValue = originalValue;
  }

  /**
   * Sets the field's property editor which is used to translate typed values to
   * string, and string values back to typed values.
   * 
   * @param propertyEditor the property editor
   */
  public void setPropertyEditor(PropertyEditor<D> propertyEditor) {
    this.propertyEditor = propertyEditor;
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
    if (rendered) {
      getInputEl().dom.setPropertyBoolean("readOnly", readOnly);
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
      getInputEl().dom.setPropertyInt("tabIndex", index);
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
   * Sets a data value into the field and validates it. If the field is
   * rendered, To set the value directly without validation see
   * {@link #setRawValue}.
   * 
   * @param value the value to set
   */
  public void setValue(D value) {
    this.value = value;
    if (rendered) {
      String v = value == null ? "" : propertyEditor.getStringValue(value);
      setRawValue(v);
      validate();
    }
  }

  /**
   * Updates the original value of the field. This method is useful when a form
   * / field is being reused and the originaValue needs to be reset.
   * 
   * @param value the new original value
   */
  public void updateOriginalValue(D value) {
    originalValue = value;
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
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        errorIcon.el().alignTo(getElement(), "tl-tr", new int[] {2, 3});
      }
    });
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    return new FieldEvent(this);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    if (errorIcon != null && errorIcon.isAttached()) {
      errorIcon.setVisible(false);
      ComponentHelper.doDetach(errorIcon);
    }
  }

  protected void fireChangeEvent(Object oldValue, Object value) {
    if (oldValue != value) {
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
    return el();
  }

  protected El getStyleEl() {
    return el();
  }

  protected void initValue() {
    if (value != null) {
      setValue(value);
    }
  }

  protected void onBlur(ComponentEvent be) {
    if (!GXT.isOpera && focusStyle != null) {
      getInputEl().removeStyleName(focusStyle);
    }
    hasFocus = false;
    if (validateOnBlur) {
      validate();
    }

    if ((focusValue == null && getValue() != null)
        || (focusValue != null && !focusValue.equals(getValue()))) {
      fireChangeEvent(focusValue, getValue());
    }
    fireEvent(Events.Blur, new FieldEvent(this));
  }

  protected void onClick(ComponentEvent ce) {

  }

  @Override
  protected void onDisable() {
    super.onDisable();
    getInputEl().dom.setPropertyString("disabled", "true");
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    getInputEl().dom.removeAttribute("disabled");
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

  @Override
  protected void onHide() {
    super.onHide();
    El lbl = findLabelElement();
    if (lbl != null) {
      lbl.setVisible(false);
    }
  }

  protected void onKeyDown(FieldEvent fe) {
    fireEvent(Events.KeyDown, new FieldEvent(this, fe.event));
  }

  protected void onKeyPress(FieldEvent fe) {
    fireEvent(Events.KeyPress, new FieldEvent(this, fe.event));
  }

  protected void onKeyUp(FieldEvent fe) {
    fireEvent(Events.KeyUp, new FieldEvent(this, fe.event));
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);

    addStyleName(fieldStyle);

    String type = getInputEl().dom.getAttribute("type");
    getInputEl().addStyleName("x-form-" + type);

    if (name != null) {
      getInputEl().dom.setAttribute("name", name);
    }

    if (readOnly) {
      setReadOnly(true);
    }

    if (tabIndex != Style.DEFAULT) {
      getInputEl().setIntElementProperty("tabIndex", tabIndex);
    }

    if (disabled) {
      onDisable();
    }

    if (inputStyle != null) {
      addInputStyleName(inputStyle);
      inputStyle = null;
    }

    originalValue = value;

    getInputEl().addEventsSunk(
        Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS | Event.KEYEVENTS);

    initValue();
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);
    if (errorIcon != null && errorIcon.isAttached()) {
      alignErrorIcon();
    }
  }

  @Override
  protected void onShow() {
    super.onShow();
    El lbl = findLabelElement();
    if (lbl != null) {
      lbl.setVisible(true);
    }
  }

  /**
   * Subclasses should provide the validation implementation by overriding this.
   * 
   * @param value the value to validate
   * @return <code>true</code> for valid
   */
  protected boolean validateValue(String value) {
    if (forceInvalidText != null) {
      markInvalid(forceInvalidText);
      return false;
    }
    return true;
  }

  private El findLabelElement() {
    if (rendered) {
      El elem = el().findParent(".x-form-item", 5);
      if (elem != null) {
        return elem.firstChild();
      }
    }
    return null;
  }

  private void fireKey(FieldEvent fe) {
    if (fe.isSpecialKey()) {
      fireEvent(Events.SpecialKey, fe);
    }
  }

}
