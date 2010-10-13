/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.aria.FocusFrame;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ColorPaletteEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Grid;

/**
 * Basic color component.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeSelect</b> : ColorPaletteEvent(colorPalette, color)<br>
 * <div>Fires before a color selected.</div>
 * <ul>
 * <li>colorPalette : this</li>
 * <li>color : the selected color</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Select</b> : ColorPaletteEvent(colorPalette, color)<br>
 * <div>Fires when a color is selected.</div>
 * <ul>
 * <li>colorPalette : this</li>
 * <li>color : the selected color</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 */
public class ColorPalette extends Component {

  private boolean allowReselect;
  private XTemplate template;
  private String value;
  private NodeList<Element> elements;
  private String[] colors = new String[] {
      "000000", "993300", "333300", "003300", "003366", "000080", "333399", "333333", "800000", "FF6600", "808000",
      "008000", "008080", "0000FF", "666699", "808080", "FF0000", "FF9900", "99CC00", "339966", "33CCCC", "3366FF",
      "800080", "969696", "FF00FF", "FFCC00", "FFFF00", "00FF00", "00FFFF", "00CCFF", "993366", "C0C0C0", "FF99CC",
      "FFCC99", "FFFF99", "CCFFCC", "CCFFFF", "99CCFF", "CC99FF", "FFFFFF"};

  /**
   * Creates a new color palette.
   */
  public ColorPalette() {
    baseStyle = "x-color-palette";
  }

  /**
   * Returns the colors.
   * 
   * @return the colors
   */
  public String[] getColors() {
    return colors;
  }

  /**
   * Returns the xtemplate.
   * 
   * @return the template
   */
  public XTemplate getTemplate() {
    return template;
  }

  /**
   * Returns the current selected color.
   * 
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns true if re-selections are allowed.
   * 
   * @return the true if re-selections are allowed.
   */
  public boolean isAllowReselect() {
    return allowReselect;
  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    switch (ce.getEventTypeInt()) {
      case Event.ONCLICK:
        onClick(ce);
        break;
      case Event.ONMOUSEOVER:
        onMouseOver(ce);
        break;
      case Event.ONMOUSEOUT:
        onMouseOut(ce);
        break;
      case Event.ONFOCUS:
        onFocus(ce);
        break;
      case Event.ONBLUR:
        onBlur(ce);
        break;
    }
  }

  /**
   * Selects the color.
   * 
   * @param color the color
   */
  public void select(String color) {
    select(color, false);
  }

  /**
   * Selects the color.
   * 
   * @param color the color
   * @param suppressEvent true to suppress the select event
   */
  public void select(String color, boolean suppressEvent) {
    color = color.replace("#", "");
    ColorPaletteEvent ce = new ColorPaletteEvent(this);
    ce.setColor(color);

    if (!suppressEvent) {
      if (!fireEvent(Events.BeforeSelect, ce)) {
        return;
      }
    }

    for (int i = 0; i < elements.getLength(); i++) {
      El el = El.fly(elements.getItem(i));
      el.removeStyleName("x-color-palette-sel");
      el.removeStyleName("x-color-palette-hover");
    }

    if (!color.equals(value) || allowReselect) {
      El a = el().child("a.color-" + color);
      a.addStyleName("x-color-palette-sel");
      getElement().setAttribute("aria-activedescendant", a.getId());
      
      value = color;
      if (!suppressEvent) {
        fireEvent(Events.Select, ce);
      }
    }
  }

  /**
   * True to fire a select event if the current selected value is selected again
   * (default to false).
   * 
   * @param allowReselect true to fire select events if re-selected
   */
  public void setAllowReselect(boolean allowReselect) {
    this.allowReselect = allowReselect;
  }

  /**
   * Sets the colors for the palette.
   * 
   * @param colors the colors to set
   */
  public void setColors(String[] colors) {
    this.colors = colors;
  }

  /**
   * Optionally, sets the xtemplate to be used to render the component.
   * 
   * @param template the xtemplate
   */
  public void setTemplate(XTemplate template) {
    this.template = template;
  }

  /**
   * Sets the selected color.
   * 
   * @param value the value to set
   */
  public void setValue(String value) {
    value = value.replace("#", "");
    this.value = value;
    if (rendered) {
      select(value);
    }
  }

  @Override
  protected void afterRender() {
    super.afterRender();
    if (value != null) {
      String s = getValue();
      value = null;
      select(s);
    }
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    return new ColorPaletteEvent(this, event);
  }

  protected void onClick(ComponentEvent ce) {
    ce.preventDefault();
    if (!disabled) {
      El target = ce.getTarget("a", 3);
      if (target != null) {
        String className = target.getStyleName();
        if (className.indexOf("color-") != -1) {
          select(className.substring(className.indexOf("color-") + 6, className.indexOf("color-") + 12));
        }
      }
    }
  }

  protected void onFocus(ComponentEvent ce) {
    FocusFrame.get().frame(this);
  }
  
  protected void onBlur(ComponentEvent ce) {
    FocusFrame.get().unframe();
  }

  protected void onKeyDown(ComponentEvent ce) {
    if (value != null) {
      Element a = el().child("a.color-" + getValue()).dom;
      int row = Integer.valueOf(a.getAttribute("row"));
      if (row < 4) {
        int idx = indexOf(elements, a);
        idx = idx + 8;
        a = elements.getItem(idx);
        String color = getColorFromElement(a);
        select(color, true);
      }
    } else {
      select(getColorFromElement(elements.getItem(0)), true);
    }
  }

  protected void onKeyEnter(ComponentEvent ce) {
    if (value != null) {
      allowReselect = true;
      select(value);
      allowReselect = false;
    }
  }

  protected void onKeyUp(ComponentEvent ce) {
    if (value != null) {
      Element a = el().child("a.color-" + getValue()).dom;
      int row = Integer.valueOf(a.getAttribute("row"));
      if (row > 0) {
        int idx = indexOf(elements, a);
        idx = idx - 8;
        a = elements.getItem(idx);
        String color = getColorFromElement(a);
        select(color, true);
      }
    }
  }

  protected void onKeyRight(ComponentEvent ce) {
    if (value != null) {
      Element a = el().child("a.color-" + getValue()).dom;
      int col = Integer.valueOf(a.getAttribute("col"));
      if (col == 7) {
        return;
      }
      int idx = indexOf(elements, a);
      if (idx < elements.getLength() - 2) {
        a = elements.getItem(idx + 1);
        String color = getColorFromElement(a);
        select(color, true);
      }
    } else {
      select(getColorFromElement(elements.getItem(0)), true);
    }
  }

  protected void onKeyLeft(ComponentEvent ce) {
    if (value != null) {
      Element a = el().child("a.color-" + getValue()).dom;
      int col = Integer.valueOf(a.getAttribute("col"));
      if (col == 0) {
        return;
      }
      int idx = indexOf(elements, a);
      if (idx > 0) {
        a = elements.getItem(idx - 1);
        String color = getColorFromElement(a);
        select(color, true);
      }
    }
  }

  protected void onMouseOut(ComponentEvent ce) {
    El target = ce.getTarget("a", 3);
    if (target != null) {
      target.removeStyleName("x-color-palette-hover");
    }
  }

  protected void onMouseOver(ComponentEvent ce) {
    El target = ce.getTarget("a", 3);
    if (target != null) {
      target.addStyleName("x-color-palette-hover");
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv(), target, index);
    super.onRender(target, index);

    Grid grid = new Grid(5, 8);
    grid.getElement().setAttribute("role", "presentation");
    grid.setCellPadding(0);
    grid.setCellSpacing(0);
    int mark = 0;
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 8; j++) {
        String c = colors[mark];
        grid.setHTML(i, j, "<a aria-label=" + c + " id=" + XDOM.getUniqueId() + " role=gridcell row=" + i + " col=" + j + " class=\"color-" + c
            + "\"><em role=presentation><span role=presentation style=\"background:#" + c + "\" unselectable=\"on\">&#160;</span></em></a>");
        mark++;
      }
    }
    getElement().appendChild(grid.getElement());

    NodeList<Element> trs = el().select("tr");
    for (int i = 0; i < trs.getLength(); i++) {
      trs.getItem(i).setAttribute("role", "row");
    }

    new KeyNav<ComponentEvent>(this) {
      @Override
      public void onDown(ComponentEvent ce) {
        onKeyDown(ce);
      }

      @Override
      public void onEnter(ComponentEvent ce) {
        onKeyEnter(ce);
      }

      @Override
      public void onLeft(ComponentEvent ce) {
        onKeyLeft(ce);
      }

      @Override
      public void onRight(ComponentEvent ce) {
        onKeyRight(ce);
      }

      @Override
      public void onUp(ComponentEvent ce) {
        onKeyUp(ce);
      }

    };

    el().setTabIndex(0);
    el().setElementAttribute("hideFocus", "true");

    elements = el().select("a");

    if (GXT.isAriaEnabled()) {
      getElement().setAttribute("role", "grid");
    }

    sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.FOCUSEVENTS);
  }

  private String getColorFromElement(Element elem) {
    String className = elem.getClassName();
    if (className.indexOf("color-") != -1) {
      return className.substring(className.indexOf("color-") + 6, className.indexOf("color-") + 12);
    }
    return null;
  }

  private int indexOf(NodeList<Element> elements, Element elem) {
    for (int i = 0; i < elements.getLength(); i++) {
      if (elements.getItem(i) == elem) {
        return i;
      }
    }
    return -1;
  }

}
