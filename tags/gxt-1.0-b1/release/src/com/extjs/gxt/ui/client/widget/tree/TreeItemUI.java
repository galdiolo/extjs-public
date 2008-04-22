/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tree;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.util.Markup;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class TreeItemUI {

  protected static Listener<TreeEvent> clickListener = new Listener<TreeEvent>() {
    public void handleEvent(TreeEvent te) {
      if (te.type == Events.Click) {
        TreeItem item = (TreeItem) te.item;
        Element target = te.getTarget();
        if (target != null && te.within(item.getUI().jointEl)) {
          item.toggle();
        }
        te.cancelBubble();
        item.tree.fireEvent(Events.Click, te);
      } else if (te.type == Events.DoubleClick) {
        te.item.toggle();
      }
    }
  };
  protected static Listener<TreeEvent> mouseListener = new Listener<TreeEvent>() {
    public void handleEvent(TreeEvent ce) {
      int type = ce.type;
      TreeItem item = ce.item;
      switch (type) {
        case Events.MouseOver:
          item.getUI().onOverChange(true);
          break;
        case Events.MouseOut:
          item.getUI().onOverChange(false);
          break;
      }
      ce.cancelBubble();
    }
  };
  protected static Listener<TreeEvent> listener = new Listener<TreeEvent>() {
    public void handleEvent(TreeEvent ce) {
      int type = ce.type;
      TreeItem item = ce.item;
      TreeItemUI ui = item.getUI();
      Element target = ce.getTarget();
      switch (type) {
        case Events.Click:
        case Events.DoubleClick:
          if (DOM.isOrHasChild(ui.checkEl, target)) {
            ce.stopEvent();
            item.setChecked(!item.isChecked());
          } else {
            clickListener.handleEvent(ce);
          }
          return;
        case Events.MouseOver:
        case Events.MouseOut:
          if (DOM.isOrHasChild(ui.jointEl, target)) {
            jointListener.handleEvent(ce);

          } else if (DOM.isOrHasChild(ui.iconEl, target)
              || DOM.isOrHasChild(ui.textEl, target)
              || DOM.isOrHasChild(ui.checkEl, target)) {
            mouseListener.handleEvent(ce);
          }
          break;
      }

    }
  };

  private static Listener<TreeEvent> jointListener = new Listener<TreeEvent>() {
    public void handleEvent(TreeEvent te) {
      int type = te.type;
      TreeItem item = te.item;
      switch (type) {
        case Events.MouseOver:
          item.getUI().itemEl.addStyleName(item.getUI().styleTreeJointOver);
          break;
        case Events.MouseOut:
          item.getUI().itemEl.removeStyleName(item.getUI().styleTreeJointOver);
          break;
      }
      te.stopEvent();
    }
  };
  protected int animDuration = 300;
  protected int indentWidth = 18;
  protected Element containerEl;

  protected Element jointEl, jointDivEl;
  protected TreeItem item;
  protected El itemEl;
  protected Element indentEl;
  protected Element checkEl, checkDivEl;
  protected Element iconEl, iconDivEl;
  protected Element textEl, textSpanEl;

  protected String styleTreeOver = "my-tree-over";
  protected String styleTreeJointOver = "my-tree-joint-over";
  protected String styleTreeChecked = "my-tree-checked";
  protected String styleTreeNotChecked = "my-tree-notchecked";
  protected String styleTreeLoading = "my-tree-loading";
  protected String styleTreeSelected = "my-tree-sel";
  protected String classTreeOpen = "my-tree-open";
  protected String classTreeClose = "my-tree-close";

  private String textStyle;

  public TreeItemUI(TreeItem item) {
    this.item = item;
  }

  public void afterCollapse() {
    item.tree.disableEvents(false);
    itemEl.removeStyleName(styleTreeJointOver);
    TreeEvent te = new TreeEvent(item.tree);
    te.item = item;
    item.fireEvent(Events.Collapse, te);
  }

  public void afterExpand() {
    item.tree.disableEvents(false);
    itemEl.removeStyleName(styleTreeJointOver);
    TreeEvent te = new TreeEvent(item.tree);
    te.item = item;
    item.fireEvent(Events.Expand, te);
  }

  public void animCollapse() {
    El.fly(containerEl).slideOut(Direction.UP, animDuration, new Listener<FxEvent>() {
      public void handleEvent(FxEvent fe) {
        afterCollapse();
      }
    });
    item.tree.disableEvents(true);
  }

  public void animExpand() {
    El.fly(containerEl).slideIn(Direction.DOWN, animDuration, new Listener<FxEvent>() {
      public void handleEvent(FxEvent fe) {
        afterExpand();
      }
    });
    item.tree.disableEvents(true);
  }

  public void collapse() {
    if (item.isRoot()) {
      return;
    }
    updateJoint();
    onIconStyleChange(null);
    if (item.tree.animate) {
      animCollapse();
    } else {
      El.fly(containerEl).setVisible(false);
      afterCollapse();
    }
  }

  public void expand() {
    if (item.isRoot()) {
      return;
    }
    updateJoint();
    if (item.getItemCount() == 0) {
      return;
    }
    onIconStyleChange(null);
    if (item.tree.animate) {
      animExpand();
    } else {
      El.fly(containerEl).setVisible(true);
      afterExpand();
    }
  }

  public Element getCheckEl() {
    return checkEl;
  }

  public int getIndent() {
    return (item.getDepth() - 1) * item.tree.getIndentWidth();
  }

  public Element getJointEl() {
    return jointEl;
  }

  public Listener getListener() {
    return listener;
  }

  public void onCheckChange(boolean checked) {
    String cls = checked ? styleTreeChecked : styleTreeNotChecked;
    XDOM.setStyleName(checkDivEl, cls);
    TreeEvent te = new TreeEvent(item.tree);
    te.item = item;
    item.fireEvent(Events.CheckChange, te);
  }

  public void onClick(ComponentEvent ce) {
    Element target = ce.getTarget();
    if (target != null && ce.within(jointEl)) {
      item.toggle();
    }
    ce.cancelBubble();
  }

  public void onDoubleClick(ComponentEvent ce) {
    item.toggle();
    ce.cancelBubble();
  }

  public void onIconStyleChange(String style) {
    if (item.iconStyle != null) {
      // MyDOM.setVisible(iconEl, true);
      El.fly(iconEl).setStyleAttribute("display", "");
      XDOM.setStyleName(iconDivEl, item.iconStyle);
      return;
    }
    if (!item.leaf) {
      String s = "";
      if (item.isExpanded() && item.tree.openNodeImageStyle != null) {
        s = item.tree.openNodeImageStyle;
      } else if (item.isExpanded() && item.tree.openNodeImageStyle != null) {
        s = item.tree.nodeImageStyle;
      } else if (!item.isExpanded()) {
        s = item.tree.nodeImageStyle;
      }
      El.fly(iconEl).setStyleAttribute("display", "");
      XDOM.setStyleName(iconDivEl, s);
    } else {
      El.fly(iconEl).setStyleAttribute("display", "");
      XDOM.setStyleName(iconDivEl, item.tree.itemImageStyle);
      return;
    }

  }

  public void onLoadingChange(boolean loading) {
    if (loading) {
      item.addStyleName(styleTreeLoading);
    } else {
      item.removeStyleName(styleTreeLoading);
    }
  }

  public void onMouseOut(BaseEvent be) {

  }

  public void onOverChange(boolean over) {
    itemEl.setStyleName(styleTreeOver, over);
  }

  public void onSelectedChange(boolean selected) {
    if (item.isRendered()) {
      itemEl.setStyleName(styleTreeSelected, selected);
      if (!selected) {
        onOverChange(false);
      }
    }
  }

  public void onTextChange(String text) {
    if (!item.root) {
      El.fly(textSpanEl).setInnerHtml(text);
    }
  }

  public void onTextStyleChange(String style) {
    if (textStyle != null) {
      El.fly(textEl).removeStyleName(textStyle);
    }
    textStyle = style;
    if (style != null) {
      El.fly(textEl).addStyleName(style);
    }
  }

  public void removeItem(TreeItem child) {
    DOM.removeChild(containerEl, child.getElement());
  }

  public void render(Element target, int index) {
    if (item.isRoot() == true) {
      return;
    }
    item.setElement(DOM.createDiv());
    item.setStyleName("my-treeitem");

    DOM.insertChild(target, item.getElement(), index);

    DOM.setInnerHTML(item.getElement(), Markup.TREE_ITEM);
    itemEl = item.el.firstChild();
    Element td = itemEl.getSubChild(3);
    indentEl = DOM.getFirstChild(td);
    jointEl = DOM.getNextSibling(td);
    jointDivEl = DOM.getFirstChild(jointEl);
    checkEl = DOM.getNextSibling(DOM.getNextSibling(jointEl));
    checkDivEl = DOM.getFirstChild(checkEl);
    iconEl = DOM.getNextSibling(checkEl);
    iconDivEl = DOM.getFirstChild(iconEl);
    textEl = DOM.getNextSibling(iconEl);
    textSpanEl = DOM.getFirstChild(textEl);
    Element tbl = DOM.getFirstChild(item.getElement());
    containerEl = DOM.getNextSibling(tbl);

    if (item.tree.checkable) {
      El.fly(checkEl).setVisible(true);
    } else {
      El.fly(checkEl).setVisible(false);
    }

    onTextChange(item.getText());
    onIconStyleChange(item.getIconStyle());

    if (item.getTextStyle() != null) {
      onTextStyleChange(item.getTextStyle());
    }

    if (item.isChecked()) {
      onCheckChange(true);
    }

    El.fly(indentEl).setWidth(getIndent());

    if (!GXT.isIE) {
      DOM.setElementPropertyInt(item.getElement(), "tabIndex", 0);
    }

    updateJoint();
    item.disableTextSelection(true);

  }

  public void updateJoint() {
    if (item.isRoot()) {
      return;
    }
    boolean loaded = item.getData("loaded") != null;
    boolean viewer = item.tree.isViewer;
    boolean children = item.getItemCount() != 0;
    boolean open = (!viewer && children) || (viewer && (children || !loaded));

    if (!item.leaf && open) {
      String cls = item.isExpanded() ? classTreeOpen : classTreeClose;
      XDOM.setStyleName(jointDivEl, cls);
    } else {
      XDOM.setStyleName(jointDivEl, "none");
    }

    if (item.tree.checkable) {
      switch (item.tree.checkNodes) {
        case BOTH:
          El.fly(checkEl).setVisible(true);
          break;
        case PARENT:
          El.fly(checkEl).setVisible(!item.isLeaf());
          break;
        case LEAF:
          El.fly(checkEl).setVisible(item.isLeaf());
          break;
      }
    }
  }
}
