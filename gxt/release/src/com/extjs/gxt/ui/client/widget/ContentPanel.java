/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.core.DomHelper;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Markup;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel is a component container that has specific functionality and
 * structural components that make it the perfect building block for
 * application-oriented user interfaces. The Panel contains bottom and top
 * toolbars, along with separate header, footer and body sections. It also
 * provides built-in expandable and collapsible behavior, along with a variety
 * of prebuilt tool buttons that can be wired up to provide other customized
 * behavior.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeExpand</b> : ComponentEvent(component)<br>
 * <div>Fires before the panel is expanded. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : ComponentEvent(component)<br>
 * <div>Fires after the panel is expanded</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : ComponentEvent(component)<br>
 * <div>Fires before the panel is collpased. Listeners can set the
 * <code>doit</code> field <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : ComponentEvent(component)<br>
 * <div>Fires after the panel is collapsed.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeClose</b> : ComponentEvent(component)<br>
 * <div>Fires before a content panel is closed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the operation.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Close</b> : ComponentEvent(component)<br>
 * <div>Fires after a content panel is closed.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class ContentPanel extends LayoutContainer {

  protected boolean frame;
  protected Header head;
  protected ButtonBar buttonBar;
  protected El body, bwrap;

  private String bodyStyle, bodyStyleName;
  private boolean headerVisible = true;
  private boolean collapsed, hideCollapseTool;
  private boolean footer, titleCollapse;
  private HorizontalAlignment buttonAlign = HorizontalAlignment.RIGHT;
  private boolean animCollapse = true;
  private boolean collapsible;
  private boolean bodyBorder = true, insetBorder = true;
  private Component topComponent;
  private Component bottomComponent;
  private boolean animating;
  private ToolButton collapseBtn;
  private String headerStyle, footerStyle;
  private String headerTextStyle;
  private String bwrapStyle;
  private String tbarStyle, bbarStyle;
  private String bodStyle;
  private String collapseStyle;
  private El foot, tbar, bbar;

  /**
   * Creates a new panel instance.
   */
  public ContentPanel() {
    super();
    baseStyle = "x-panel";
    buttonBar = new ButtonBar();
    head = new Header();
  }

  /**
   * Creates a new content panel.
   * 
   * @param layout the panel's layout
   */
  public ContentPanel(Layout layout) {
    this();
    setLayout(layout);
  }

  /**
   * Adds a button the the panel.
   * 
   * @param button the button to add
   */
  public void addButton(Button button) {
    buttonBar.add(button);
  }

  /**
   * Collapses the panel body so that it becomes hidden. Fires the
   * <i>BeforeCollapse</i> before collapsing, then the <i>Collapse</i> event
   * after collapsing.
   */
  public void collapse() {
    if (rendered) {
      if (collapsible && !collapsed && !animating) {
        if (fireEvent(Events.BeforeCollapse)) {
          onCollapse();
        }
      }
    } else {
      collapsed = true;
    }
  }

  /**
   * Expands the panel body so that it becomes visible. Fires the
   * <i>BeforeExpand</i> before expanding, then the <i>Expand</i> event after
   * expanding.
   */
  public void expand() {
    if (rendered && getCollapsible() && isCollapsed()) {
      if (fireEvent(Events.BeforeExpand)) {
        removeStyleName(collapseStyle);
        onExpand();
      }
    }
  }

  /**
   * Returns true if animation is enabled for expand / collapse.
   * 
   * @return the animCollapse true for animations
   */
  public boolean getAnimCollapse() {
    return animCollapse;
  }

  /**
   * Returns the panel's body element.
   * 
   * @return the body
   */
  public El getBody() {
    return body;
  }

  /**
   * Returns true if the body border is enabled.
   * 
   * @return the body border state
   */
  public boolean getBodyBorder() {
    return bodyBorder;
  }

  /**
   * Returns the body style.
   * 
   * @return the body style
   */
  public String getBodyStyle() {
    return bodyStyle;
  }

  /**
   * Returns the panel's bottom component.
   * 
   * @return the bottom component
   */
  public Component getBottomComponent() {
    return bottomComponent;
  }

  /**
   * Returns the panel's button alignment.
   * 
   * @return the button alignment
   */
  public HorizontalAlignment getButtonAlign() {
    return buttonAlign;
  }

  /**
   * Returns the panel's button bar.
   * 
   * @return the button bar
   */
  public ButtonBar getButtonBar() {
    return buttonBar;
  }

  /**
   * Returns true if the panel is collapsible.
   * 
   * @return the collapsible state
   */
  public boolean getCollapsible() {
    return collapsible;
  }

  /**
   * Provides access to internal elements.
   * <p>
   * Valid values are:
   * <ul>
   * <li>"body"</li>
   * <li>"header"</li>
   * <li>"bwrap"</li>
   * </ul>
   * </p>
   * 
   * @param name the element name
   * @return the element
   */
  public Element getElement(String name) {
    if (name.equals("header")) {
      return head.getElement();
    } else if (name.equals("bwrap")) {
      return bwrap.dom;
    } else if (name.equals("body")) {
      return body.dom;
    }
    return null;
  }

  /**
   * @return the footer
   */
  public boolean getFooter() {
    return footer;
  }

  /**
   * Returns true if framing is enabled.
   * 
   * @return the frame state
   */
  public boolean getFrame() {
    return frame;
  }

  /**
   * Returns the height in pixels of the framing elements of this panel
   * (including any top and bottom bars and header and footer elements, but not
   * including the body height). To retrieve the body height see
   * {@link #getInnerHeight}.
   * 
   * @return the frame height
   */
  public int getFrameHeight() {
    int h = el().getFrameWidth("tb");
    h += (tbar != null ? tbar.getHeight() : 0) + (bbar != null ? bbar.getHeight() : 0);
    if (frame) {
      Element hd = el().firstChild().dom;
      Element ft = bwrap.dom.getLastChild().cast();
      h += (fly(hd).getHeight() + fly(ft).getHeight());
      Element mc = bwrap.subChild(3).dom;
      h += fly(mc).getFrameWidth("tb");
    } else {
      if (head != null) {
        h += head.getOffsetHeight();
      }
      if (foot != null) {
        h += foot.getHeight();
      }
    }
    return h;
  }

  /**
   * Returns the width in pixels of the framing elements of this panel (not
   * including the body width). To retrieve the body width see
   * {@link #getInnerWidth}.
   * 
   * @return The frame width
   */
  public int getFrameWidth() {
    int w = el().getFrameWidth("lr");
    if (frame) {
      Element l = bwrap.firstChild().dom;
      w += (fly(l).getFrameWidth("l") + fly(l).firstChild().getFrameWidth("r"));
      Element mc = bwrap.subChild(3).dom;
      w += fly(mc).getFrameWidth("lr");
    }
    return w;
  }

  /**
   * Returns the panel's header.
   * 
   * @return the header
   */
  public Header getHeader() {
    return head;
  }

  /**
   * Returns the panel's heading.
   * 
   * @return the heading
   */
  public String getHeading() {
    return head.getText();
  }

  /**
   * Returns the panel's icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return head.getIconStyle();
  }

  /**
   * Returns the height in pixels of the body element (not including the height
   * of any framing elements). For the frame height see {@link #getFrameHeight}.
   * 
   * @return the inner height
   */
  public int getInnerHeight() {
    return getSize().height - getFrameHeight();
  }

  /**
   * Returns the width in pixels of the body element (not including the width of
   * any framing elements). For the frame width see {@link #getFrameWidth}.
   * 
   * @return the body width
   */
  public int getInnerWidth() {
    return this.getSize().width - this.getFrameWidth();
  }

  @Override
  public El getLayoutTarget() {
    return body;
  }

  /**
   * @return the titleCollapse
   */
  public boolean getTitleCollapse() {
    return titleCollapse;
  }

  /**
   * Returns the panel's title text.
   * 
   * @return the title text
   * @deprecated use {@link #getHeading()}
   */
  public String getTitleText() {
    return head.getText();
  }

  /**
   * Returns the panels top component.
   * 
   * @return the top component
   */
  public Component getTopComponent() {
    return topComponent;
  }

  public Widget getWidget(String field) {
    if ("collapseBtn".equals(field)) {
      return collapseBtn;
    }
    return null;
  }

  /**
   * Returns <code>true</code> if the panel is expanded.
   * 
   * @return the expand state
   */
  public boolean isExpanded() {
    return !isCollapsed();
  }

  /**
   * Returns true if the header is visible.
   * 
   * @return the header visible state
   */
  public boolean isHeaderVisible() {
    return headerVisible;
  }

  /**
   * Returns true if the collapse tool is hidden.
   * 
   * @return the hide collapse tool state
   */
  public boolean isHideCollapseTool() {
    return hideCollapseTool;
  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    if (ce.type == Event.ONCLICK) {
      onClick(ce);
    }
  }

  /**
   * Sets whether exand and collapse is animating (defaults to true,
   * pre-render).
   * 
   * @param animCollapse true to enable animations
   */
  public void setAnimCollapse(boolean animCollapse) {
    assertPreRender();
    this.animCollapse = animCollapse;
  }

  /**
   * True to display the borders of the panel's body element, false to hide them
   * (defaults to true, pre-render). By default, the border is a 2px wide inset
   * border, but this can be further altered by setting
   * {@link #setBodyBorder(boolean)} to false.
   * 
   * @param bodyBorder true for a body border
   */
  public void setBodyBorder(boolean bodyBorder) {
    assertPreRender();
    this.bodyBorder = bodyBorder;
  }

  /**
   * Custom CSS styles to be applied to the body element in the format expected
   * by {@link El#applyStyles} (pre-render).
   * 
   * @param bodyStyle the body style
   */
  public void setBodyStyle(String bodyStyle) {
    assertPreRender();
    this.bodyStyle = bodyStyle;
  }

  /**
   * A style name that is added to the panel's body element (pre-render).
   * 
   * @param style the style name
   */
  public void setBodyStyleName(String style) {
    assertPreRender();
    this.bodyStyleName = style;
  }

  /**
   * Sets the panel's bottom component (pre-render). The component's natural
   * height will be used and will not be changed by the panel.
   * 
   * @param bottomComponent the bottom component
   */
  public void setBottomComponent(Component bottomComponent) {
    assertPreRender();
    this.bottomComponent = bottomComponent;
  }

  /**
   * Sets the button alignment of any buttons added to this panel (defaults to
   * RIGHT, pre-render).
   * 
   * @param buttonAlign the button alignment
   */
  public void setButtonAlign(HorizontalAlignment buttonAlign) {
    assertPreRender();
    this.buttonAlign = buttonAlign;
  }

  public void setButtonBar(ButtonBar buttonBar) {
    assertPreRender();
    this.buttonBar = buttonBar;
  }

  /**
   * True to make the panel collapsible and have the expand/collapse toggle
   * button automatically rendered into the header tool button area, false to
   * keep the panel statically sized with no button (defaults to false,
   * pre-render).
   * 
   * @param collapsible the collapsible to set
   */
  public void setCollapsible(boolean collapsible) {
    assertPreRender();
    this.collapsible = collapsible;
  }

  /**
   * Sets the panel's expand state.
   * 
   * @param expanded <code>true<code> true to expand
   */
  public void setExpanded(boolean expanded) {
    if (expanded) {
      expand();
    } else {
      collapse();
    }
  }

  /**
   * True to create the footer element explicitly, false to skip creating it
   * (pre-render). By default, when footer is not specified, if one or more
   * buttons have been added to the panel the footer will be created
   * automatically, otherwise it will not.
   * 
   * @param footer the footer state
   */
  public void setFooter(boolean footer) {
    assertPreRender();
    this.footer = footer;
  }

  /**
   * True to render the panel with custom rounded borders, false to render with
   * plain 1px square borders (defaults to false, pre-render).
   * 
   * @param frame true to use the frame style
   */
  public void setFrame(boolean frame) {
    assertPreRender();
    this.frame = frame;
  }

  /**
   * True to create the header element explicitly, false to skip creating it
   * (defaults to true, pre-render). By default, when header is not specified,
   * if a {@link #setHeading(String)} is set the header will be created
   * automatically, otherwise it will not. If a title is set but header is
   * explicitly set to false, the header will not be rendered.
   * 
   * @param headerVisible true to show the header
   */
  public void setHeaderVisible(boolean headerVisible) {
    assertPreRender();
    this.headerVisible = headerVisible;
  }

  /**
   * Sets the title text for the panel.
   * 
   * @param text the title text
   */
  public void setHeading(String text) {
    head.setText(text);
  }

  /**
   * Sets whether the collapse tool should be displayed when the panel is
   * collapsible.
   * 
   * @param hideCollapseTool true if the tool is hidden
   */
  public void setHideCollapseTool(boolean hideCollapseTool) {
    this.hideCollapseTool = hideCollapseTool;
    if (rendered) {
      collapseBtn.setVisible(!hideCollapseTool);
    }
  }

  /**
   * Sets the header's icon style.
   * 
   * @param iconStyle the icon style
   */
  public void setIconStyle(String iconStyle) {
    head.setIconStyle(iconStyle);
  }

  /**
   * True to display an interior border on the body element of the panel, false
   * to hide it (defaults to true, pre-render). This only applies when
   * {@link #setBodyBorder(boolean)} == true.
   * 
   * @param insetBorder true to display the interior border
   */
  public void setInsetBorder(boolean insetBorder) {
    assertPreRender();
    this.insetBorder = insetBorder;
  }

  /**
   * True to allow expanding and collapsing the panel (when {@link #collapsible} =
   * true) by clicking anywhere in the header bar, false to allow it only by
   * clicking to tool button (defaults to false, pre-render).
   * 
   * @param titleCollapse the titleCollapse to set
   */
  public void setTitleCollapse(boolean titleCollapse) {
    assertPreRender();
    this.titleCollapse = titleCollapse;
  }

  /**
   * Sets the panel's top component (pre-render).
   * 
   * @param topComponent the component
   */
  public void setTopComponent(Component topComponent) {
    assertPreRender();
    this.topComponent = topComponent;
  }

  /**
   * Adds the content from the given url.
   * 
   * @param url the url
   * @return the new frame instance
   */
  public Frame setUrl(String url) {
    Frame f = new Frame(url);
    fly(f.getElement()).setStyleAttribute("frameBorder", "0");
    f.setSize("100%", "100%");
    removeAll();
    add(new WidgetComponent(f));
    return f;
  }

  protected void afterCollapse() {
    addStyleName(collapseStyle);
    collapsed = true;
    animating = false;
    el().setHeight("auto");
    ComponentEvent ce = new ComponentEvent(this);
    fireEvent(Events.Collapse, ce);
  }

  protected void afterExpand() {
    collapsed = false;
    animating = false;
    ComponentEvent ce = new ComponentEvent(this);
    fireEvent(Events.Expand, ce);
  }

  @Override
  protected void afterRender() {
    super.afterRender();
  }

  protected void createStyles(String baseStyle) {
    headerStyle = baseStyle + "-header";
    headerTextStyle = baseStyle + "-header-text";
    bwrapStyle = baseStyle + "-bwrap";
    tbarStyle = baseStyle + "-tbar";
    bodStyle = baseStyle + "-body";
    bbarStyle = baseStyle + "-bbar";
    footerStyle = baseStyle + "-footer";
    collapseStyle = baseStyle + "-collapsed";
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    if (head != null) head.onAttach();
    if (getFooter() && buttonBar != null) {
      if (buttonBar.getItemCount() > 0) {
        buttonBar.onAttach();
      }
    }
    if (topComponent != null) topComponent.onAttach();
    if (bottomComponent != null) bottomComponent.onAttach();
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(head);
    ComponentHelper.doDetach(buttonBar);
    ComponentHelper.doDetach(topComponent);
    ComponentHelper.doDetach(bottomComponent);
  }

  protected void initTools() {
    if (collapsible && !hideCollapseTool) {
      collapseBtn = new ToolButton("x-tool-toggle");
      collapseBtn.addListener(Events.Select, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          ce.stopEvent();
          setExpanded(!isExpanded());
        }
      });
      head.addTool(collapseBtn);
    }
  }

  protected void onClick(ComponentEvent ce) {
    if (head != null && ce.within(head.getElement())) {
      setExpanded(!isExpanded());
    }
  }

  protected void onCollapse() {
    if (animCollapse && !animating) {
      animating = true;
      bwrap.slideOut(Direction.UP, new FxConfig(300, new Listener<FxEvent>() {
        public void handleEvent(FxEvent fe) {
          afterCollapse();
        }
      }));
    } else {
      bwrap.setVisible(false);
      afterCollapse();
    }
  }

  protected void onExpand() {
    if (animCollapse && !animating) {
      animating = true;
      bwrap.slideIn(Direction.DOWN, new FxConfig(300, new Listener<FxEvent>() {
        public void handleEvent(FxEvent fe) {
          afterExpand();
        }
      }));
    } else {
      bwrap.setVisible(true);
      afterExpand();
    }
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    if (buttonBar.getItemCount() > 0) {
      buttonBar.setButtonAlign(buttonAlign);
      setFooter(true);
    }

    if (frame) {
      String s = Format.substitute(Markup.BBOX, baseStyle);
      DomHelper.insertHtml("afterBegin", el().dom, s);

      head.baseStyle = headerStyle;
      head.setTextStyle(headerTextStyle);
      initTools();
      head.render(el().dom);
      el().subChild(3).dom.appendChild(head.getElement());
      bwrap = el().createChild("<div class='" + bwrapStyle + "'></div>");

      Element bw = bwrap.dom;
      Element ml = DOM.getChild(el().dom, 1);
      Element bl = DOM.getChild(el().dom, 2);
      DOM.appendChild(bw, ml);
      DOM.appendChild(bw, bl);

      Element mc = fly(bw).getSubChild(3);

      if (topComponent != null) {
        tbar = fly(mc).createChild("<div class=" + tbarStyle + "></div>");
      }
      body = fly(mc).createChild("<div class=" + bodStyle + "></div>");
      if (bottomComponent != null) {
        bbar = fly(mc).createChild("<div class=" + bbarStyle + "></div>");
      }

      El e = fly(bw).lastChild().firstChild().firstChild();
      foot = e.createChild("<div class=" + footerStyle + "></div>");

      if (!headerVisible) {
        head.setVisible(false);
        body.addStyleName(bodStyle + "-noheader");
        if (tbar != null) {
          tbar.addStyleName(tbarStyle + "-noheader");
        }
      }

    } else {
      head.baseStyle = headerStyle;
      head.setTextStyle(headerTextStyle);
      initTools();
      head.render(el().dom);
      bwrap = el().createChild("<div class=" + bwrapStyle + "></div>");

      Element bw = bwrap.dom;
      if (topComponent != null) {
        tbar = fly(bw).createChild("<div class=" + tbarStyle + "></div>");
      }
      body = fly(bw).createChild("<div class=" + bodStyle + "></div>");
      if (bottomComponent != null) {
        bbar = fly(bw).createChild("<div class=" + bbarStyle + "></div>");
      }
      foot = fly(bw).createChild("<div class=" + footerStyle + "></div>");

      if (!headerVisible) {
        head.setVisible(false);
        body.addStyleName(bodStyle + "-noheader");
        if (tbar != null) {
          tbar.addStyleName(tbarStyle + "-noheader");
        }
      }
    }

    if (footer && buttonBar.getItemCount() > 0) {
      buttonBar.render(foot.dom);
    }

    if (!footer) {
      bwrap.lastChild().addStyleName("x-panel-nofooter");
    }

    if (!bodyBorder) {
      el().addStyleName(baseStyle + "-noborder");
      body.addStyleName(bodStyle + "-noborder");
      if (tbar != null) {
        tbar.addStyleName(tbarStyle + "-noborder");
      }
      if (bbar != null) {
        bbar.addStyleName(bbarStyle + "-noborder");
      }
    }

    if (!insetBorder) {
      body.setBorders(false);
    }

    if (bodyStyle != null) {
      body.applyStyles(bodyStyle);
    }

    if (bodyStyleName != null) {
      body.addStyleName(bodyStyleName);
    }

    if (headerVisible) {
      head.disableTextSelection(true);
    }

    if (topComponent != null) {
      topComponent.render(tbar.dom);
    }

    if (bottomComponent != null) {
      bottomComponent.render(bbar.dom);
    }

    if (titleCollapse) {
      head.setStyleAttribute("cursor", "pointer");
      el().addEventsSunk(Event.ONCLICK);
    }

    if (collapsed) {
      boolean anim = animCollapse;
      collapsed = false;
      setAnimCollapse(false);
      collapse();
      setAnimCollapse(anim);
    }
  }

  @Override
  protected void onResize(final int width, final int height) {
    super.onResize(width, height);
    if (isAutoWidth()) {
      getLayoutTarget().setWidth("auto");
    } else if (width != -1) {
      getLayoutTarget().setWidth(width - getFrameWidth(), true);
    }
    if (!collapsed) {
      if (isAutoHeight()) {
        getLayoutTarget().setHeight("auto");
      } else if (height != -1) {
        getLayoutTarget().setHeight(height - getFrameHeight(), true);
      }
    }
  }

  /**
   * Returns true if the panel is collapsed.
   * 
   * @return the collapsed state
   */
  private boolean isCollapsed() {
    return collapsed;
  }

}
