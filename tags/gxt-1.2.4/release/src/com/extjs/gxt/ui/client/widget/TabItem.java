/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Frame;

/**
 * TabItems are added to a {@link TabPanel}. TabItems can be closable, disabled
 * and support icons.</p>Code snippet:
 * 
 * <pre>
 * TabItem ti = new TabItem(&quot;Tab One&quot;);
 * ti.setClosable(true);
 * ti.setEnabled(false);
 * tabPanel.add(ti);
 * </pre>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeClose</b> : TabPanelEvent(tabPanel, item)<br>
 * <div>Fires before an item is closed by the user clicking the close icon.
 * Listeners can set the <code>doit</code> field to <code>false</code> to cancel
 * the action.</div>
 * <ul>
 * <li>tabPanel : this</li>
 * <li>item : the item that was closed.</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : TabPanelEvent(tabPanel, item)<br>
 * <div>Fires after an item is closed by the user clicking the close icon.</div>
 * <ul>
 * <li>tabPanel : this</li>
 * <li>item : the item that was closed.</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Select</b> : TabPanelEvent(tabPanel, item)<br>
 * <div>Fires after the item is selected.</div>
 * <ul>
 * <li>tabPanel : this</li>
 * <li>item : the item that was closed.</li>
 * </ul>
 * </dd>
 * <dl>
 */
public class TabItem extends LayoutContainer {

  public class HeaderItem extends Component {

    String text, iconStyle;

    /**
     * Returns the header's icon style
     * 
     * @return the icon style
     */
    public String getIconStyle() {
      return iconStyle;
    }

    /**
     * Returns the header's text.
     * 
     * @return the text
     */
    public String getText() {
      return text;
    }

    @Override
    public void onComponentEvent(ComponentEvent ce) {
      super.onComponentEvent(ce);
      switch (ce.getEventType()) {
        case Event.ONCLICK:
          onClick(ce);
          break;
        case Event.ONMOUSEOVER:
          onMouseOver(ce);
          break;
        case Event.ONMOUSEOUT:
          onMouseOut(ce);
          break;
      }
    }

    /**
     * Sets the item's icon style. The style name should match a CSS style that
     * specifies a background image using the following format:
     * 
     * <pre>
     * 
     * &lt;code&gt; .my-icon { background: url(images/icons/my-icon.png) no-repeat
     * center left !important; } &lt;/code&gt;
     * 
     * </pre>
     * 
     * @param iconStyle the icon style
     */
    public void setIconStyle(String iconStyle) {
      if (rendered) {
        el().selectNode(".x-tab-strip-text").removeStyleName(this.iconStyle).addStyleName(
            iconStyle);
      }
      this.iconStyle = iconStyle;
    }

    /**
     * Sets the header's text.
     * 
     * @param text the text
     */
    public void setText(String text) {
      this.text = text;
      if (rendered) {
        el().child(".x-tab-strip-text").dom.setInnerHTML(text);
      }
    }

    protected void onClick(ComponentEvent ce) {
      tabPanel.onItemClick(TabItem.this, ce);
    }

    protected void onMouseOut(ComponentEvent ce) {
      tabPanel.onItemOver(TabItem.this, false);
    }

    protected void onMouseOver(BaseEvent be) {
      tabPanel.onItemOver(TabItem.this, true);
    }

    protected void onRender(Element target, int pos) {
      tabPanel.onItemRender(TabItem.this, target, pos);
    }

  }

  Template template;
  TabPanel tabPanel;
  HeaderItem header;

  private String textStyle;
  private boolean closable;
  private RequestBuilder autoLoad;

  /**
   * Creates a new tab item.
   */
  public TabItem() {
    header = new HeaderItem();
  }

  /**
   * Creates a new tab item with the given text.
   * 
   * @param text the item's text
   */
  public TabItem(String text) {
    this();
    setText(text);
  }

  /**
   * Closes the tab item.
   */
  public void close() {
    tabPanel.remove(this);
  }

  @Override
  public void disable() {
    super.disable();
    header.disable();
  }

  @Override
  public void enable() {
    super.enable();
    header.enable();
  }

  /**
   * Returns the item's header component.
   * 
   * @return the header component
   */
  public HeaderItem getHeader() {
    return header;
  }

  /**
   * Returns the item's icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return header.getIconStyle();
  }

  /**
   * Returns the item's tab panel.
   * 
   * @return the tab panel
   */
  public TabPanel getTabPanel() {
    return tabPanel;
  }

  /**
   * Returns the item's text.
   * 
   * @return the text
   */
  public String getText() {
    return header.getText();
  }

  /**
   * Returns the item's text style name.
   * 
   * @return the style name
   */
  public String getTextStyle() {
    return textStyle;
  }

  /**
   * Returns true if the item can be closed.
   * 
   * @return the closable the close state
   */
  public boolean isClosable() {
    return closable;
  }

  /**
   * Sends a remote request and sets the item's content using the returned HTML.
   * 
   * @param requestBuilder the request builder
   */
  public void setAutoLoad(RequestBuilder requestBuilder) {
    this.autoLoad = requestBuilder;
  }

  /**
   * Sets whether the tab may be closed (defaults to false).
   * 
   * @param closable the closabable state
   */
  public void setClosable(boolean closable) {
    this.closable = closable;
  }

  /**
   * Sets the item's icon style. The style name should match a CSS style that
   * specifies a background image using the following format:
   * 
   * <pre>
   * .my-icon { background: url(images/icons/my-icon.png) no-repeat center left !important; }
   * </pre>
   * 
   * @param iconStyle the icon style
   */
  public void setIconStyle(String iconStyle) {
    header.setIconStyle(iconStyle);
  }

  /**
   * Sets the item's text.
   * 
   * @param text the new text
   */
  public void setText(String text) {
    header.setText(text);
  }

  /**
   * Sets the style name to be applied to the item's text element.
   * 
   * @param textStyle the style name
   */
  public void setTextStyle(String textStyle) {
    this.textStyle = textStyle;
  }

  /**
   * Sets a url for the content area of the item.
   * 
   * @param url the url
   * @return the frame widget
   */
  public Frame setUrl(String url) {
    Frame f = new Frame(url);
    f.getElement().setPropertyInt("frameBorder", 0);
    f.setSize("100%", "100%");
    removeAll();
    add(new WidgetComponent(f));
    return f;
  }

  @Override
  public String toString() {
    return el() != null ? el().toString() : super.toString();
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    if (autoLoad != null) {
      el().load(autoLoad);
    }
  }

}
