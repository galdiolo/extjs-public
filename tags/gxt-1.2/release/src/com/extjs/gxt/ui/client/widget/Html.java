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
 * A component that contains arbitrary HTML text. The wrapping HTML tag can be
 * configured using {@link #setTagName(String)}. </p>
 * 
 * Code snippet:
 * 
 * <pre>{@code
   Html h = new Html(
     "<div class=text style='padding:5px'>"
     + "<h1>Heading1</h1>"
     + "<i>Some text</i></br>"
     + "Some more text</br>"
     + "  <UL> <LI>item 1 <LI>item 2 </UL></br>"
     + "<u>Final text</u></div>");
   RootPanel.get().add(h);
 * }</pre>
 */
public class Html extends BoxComponent {

  private String tagName = "div";
  private String html;

  /**
   * Creates a new instance.
   */
  public Html() {

  }

  /**
   * Creates a new instance with the given html.
   * 
   * @param html the html
   */
  public Html(String html) {
    this.html = html;
  }

  /**
   * Sets the components HTML.
   * 
   * @param html the html
   */
  public void setHtml(String html) {
    this.html = html;
    if (rendered) {
      getElement().setInnerHTML(html);
    }
  }

  /**
   * Returns the component's HTML.
   * 
   * @return the html
   */
  public String getHtml() {
    return html;
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createElement(getTagName()), target, index);
    if (html != null) {
      setHtml(html);
    }
  }

  /**
   * The HTML tag name that will wrap the text (defaults to 'div'). For inline
   * behavior set the tag name to 'span'.
   * 
   * @param tagName the new tag name
   */
  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  /**
   * @return the tagName
   */
  public String getTagName() {
    return tagName;
  }
}
