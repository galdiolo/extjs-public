/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.aria;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class FocusHandler {

  public abstract boolean canHandleKeyPress(Component component, PreviewEvent pe);

  public void onTab(Component component, PreviewEvent pe) {

  }

  public void onLeft(Component component, PreviewEvent pe) {

  }

  public void onRight(Component component, PreviewEvent pe) {

  }

  public void onEscape(Component component, PreviewEvent pe) {
    stepOut(component);
  }

  public void onEnter(Component component, PreviewEvent pe) {

  }

  protected void stepInto(Widget w, PreviewEvent pe) {
    if (w instanceof LayoutContainer) {
      pe.stopEvent();
      LayoutContainer c = (LayoutContainer) w;
      if (c.getItemCount() > 0) {
        focusWidget(c.getItem(0));
      }

    } else if (w instanceof ComplexPanel) {
      pe.stopEvent();
      ComplexPanel panel = (ComplexPanel) w;
      if (panel.getWidgetCount() > 0) {
        focusWidget(panel.getWidget(0));
      }
    }
  }

  protected void stepOut(Widget w) {
    Widget p = w.getParent();
    if (p != null) {
      if (p instanceof TabItem) {
        ((TabItem) p).getTabPanel().focus();
      } else if (p instanceof Component) {
        Component c = (Component) p;
        while (c.getData("aria-ignore") != null) {
          p = c.getParent();
          if (p instanceof Component) {
            c = (Component) p;
          } else {
            El.fly(p.getElement()).focus();
            return;
          }
        }

        focusWidget(c);
      }
    }
  }

  @SuppressWarnings("unchecked")
  protected void focusNextWidget(Widget c) {
    Widget p = c.getParent();
    if (p instanceof Container) {
      Container con = (Container) p;
      int index = con.indexOf((Component) c);
      if (index == con.getItemCount() - 1) {
        index = 0;
      } else {
        ++index;
      }
      focusWidget(con.getItem(index));
    } else if (p instanceof ComplexPanel) {
      ComplexPanel panel = (ComplexPanel) p;
      int index = panel.getWidgetIndex(c);
      if (index == panel.getWidgetCount() - 1) {
        index = 0;
      } else {
        ++index;
      }
      Widget w = panel.getWidget(index);
      focusWidget(w);

    }
  }

  protected void focusWidget(Widget w) {
    if (w instanceof TabItem) {
      focusWidget(w.getParent());

    } else if (w instanceof Component) {
      ((Component) w).focus();
    } else {
      El.fly(w.getElement()).focus();
    }
  }

  @SuppressWarnings("unchecked")
  protected void focusPreviousWidget(Widget c) {
    Widget p = c.getParent();
    if (p instanceof Container) {
      Container con = (Container) p;
      int index = con.indexOf((Component) c);
      if (index > 0) {
        index--;
      } else {
        index = con.getItemCount() - 1;
      }
      con.getItem(index).focus();
    } else if (p instanceof ComplexPanel) {
      ComplexPanel panel = (ComplexPanel) p;
      int index = panel.getWidgetIndex(c);
      if (index > 0) {
        index--;
      } else {
        index = panel.getWidgetCount() - 1;
      }
      Widget w = panel.getWidget(index);
      focusWidget(w);
    }
  }

  protected void focusParent(Widget c) {
    Widget p = c.getParent();
    if (p != null) {
      if (p instanceof TabItem) {
        ((TabItem) p).getTabPanel().focus();
      } else if (p instanceof LayoutContainer) {
        FocusFrame.get().frame((Component) p);
        ((LayoutContainer) p).focus();
      }
    }
  }

}
