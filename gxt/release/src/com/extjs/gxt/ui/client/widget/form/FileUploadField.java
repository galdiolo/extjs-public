/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.El;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A file upload filed. When using this field, the containing form panel's
 * encoding must be set to MULTIPART using @link
 * {@link FormPanel#setEncoding(Encoding)}. In addition, the method should be set
 * to POST using
 * {@link FormPanel#setMethod(com.extjs.gxt.ui.client.widget.form.FormPanel.Method)}
 * .
 */
public class FileUploadField extends Field {

  /**
   * FileUpload Messages.
   */
  public class FileUploadFieldMessages extends FieldMessages {

    private String blankText = GXT.MESSAGES.textField_blankText();

    /**
     * Returns the blank text.
     * 
     * @return the blank text
     */
    public String getBlankText() {
      return blankText;
    }

    /**
     * Sets the error text to display if the allow blank validation fails
     * (defaults to "This field is required").
     * 
     * @param blankText the blank text
     */
    public void setBlankText(String blankText) {
      this.blankText = blankText;
    }

  }

  private boolean allowBlank = true;

  /**
   * Creates a new file upload field.
   */
  public FileUploadField() {
    messages = new FileUploadFieldMessages();
  }

  /**
   * Returns the field's allow blank state.
   * 
   * @return true if blank values are allowed
   */
  public boolean getAllowBlank() {
    return allowBlank;
  }

  /**
   * Returns the file name.
   * 
   * @return the file name
   */
  public String getFileName() {
    if (rendered) {
      return getInputEl().getValue();
    } else {
      return "";
    }
  }

  @Override
  public FileUploadFieldMessages getMessages() {
    return (FileUploadFieldMessages) messages;
  }

  /**
   * Sets whether a field is value when its value length = 0 (default to true).
   * 
   * @param allowBlank true to allow blanks, false otherwise
   */
  public void setAllowBlank(boolean allowBlank) {
    this.allowBlank = allowBlank;
  }

  @Override
  protected El getInputEl() {
    return el();
  }

  @Override
  protected void onRender(Element parent, int index) {
    setElement(Document.get().createFileInputElement());
    DOM.insertChild(parent, getElement(), index);
    super.onRender(parent, index);
    getInputEl().addStyleName("x-form-text");
  }

  @Override
  @SuppressWarnings("deprecation")
  protected boolean validateValue(String value) {
    boolean result = super.validateValue(value);
    if (!result) {
      return false;
    }
    int length = value.length();
    if (length < 1 || value.equals("")) {
      if (allowBlank) {
        clearInvalid();
        return true;
      } else {
        markInvalid(getMessages().getBlankText());
        return false;
      }
    }
    return true;
  }

}