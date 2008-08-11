/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A component that contains text. The text value is not treated as HTML. The
 * HTML tag used can be configured using {@link #tagName}.
 */
public class Text extends BoxComponent {

  private String tagName = "div";
  private String text;

  /**
   * Creates a new text instance.
   */
  public Text() {

  }

  /**
   * Creates a new text instance with the given text.
   * 
   * @param text the text
   */
  public Text(String text) {
    this.text = text;
  }

  /**
   * @return the tagName
   */
  public String getTagName() {
    return tagName;
  }

  /**
   * Returns the current text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * The HTML tag name that will wrap the text (defaults to 'div', pre-render). For inline
   * behavior set the tag name to 'span'.
   * 
   * @param tagName the tag name
   */
  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  /**
   * Sets the text.
   * 
   * @param text the new text
   */
  public void setText(String text) {
    this.text = text;
    if (rendered) {
      DOM.setInnerText(getElement(), text);
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createElement(getTagName()), target, index);

    if (text != null) {
      setText(text);
    }
  }

}
