/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.google.gwt.user.client.Element;

/**
 * Basic status bar that supports text and a busy icon, aligned horizontally. 
 */
public class StatusBar extends Component {

  protected HorizontalPanel panel;
  protected IconButton waitBtn;
  protected Html text;

  public StatusBar() {
    baseStyle = "x-status";

    waitBtn = new IconButton("icon-wait");
    waitBtn.setData("width", "25px");
    waitBtn.setSize(17, 17);
    waitBtn.setStyleAttribute("cursor", "default");
    waitBtn.setStyleAttribute("marginRight", "5px");
    waitBtn.setVisible(false);

    text = new Html();
    text.setStyleName("x-status-text");

    panel = new HorizontalPanel();
    panel.setVerticalAlign(VerticalAlignment.MIDDLE);
    panel.add(waitBtn);
    panel.add(text);
  }

  /**
   * Displays the busy icon.
   */
  public void showBusy() {
    waitBtn.setVisible(true);
    waitBtn.changeStyle("icon-wait");
  }

  /**
   * Displays the busy icon with the given message.
   * 
   * @param message the message
   */
  public void showBusy(String message) {
    waitBtn.setVisible(true);
    waitBtn.changeStyle("icon-wait");
    setMessage(message);
  }

  /**
   * Clears the status content.
   */
  public void clear() {
    waitBtn.setVisible(false);
    text.setHtml("");
  }

  /**
   * Sets the status message.
   * 
   * @param text the message
   */
  public void setMessage(String text) {
    this.text.setHtml(text);
  }

  /**
   * Sets the bar's icon style. The style name should match a CSS style that
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
    waitBtn.setVisible(true);
    waitBtn.changeStyle(iconStyle);
  }

  @Override
  protected void onRender(Element target, int index) {
    panel.render(target);
    setElement(panel.getElement());
  }

}
