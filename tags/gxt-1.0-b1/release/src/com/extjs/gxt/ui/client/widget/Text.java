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

  /**
   * The HTML tag name that will wrap the text (defaults to 'div'). For inline
   * behavior set the tag name to 'span'.
   */
  public String tagName = "div";

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

  /**
   * Returns the current text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createElement(tagName), target, index);

    if (text != null) {
      setText(text);
    }
  }

}
