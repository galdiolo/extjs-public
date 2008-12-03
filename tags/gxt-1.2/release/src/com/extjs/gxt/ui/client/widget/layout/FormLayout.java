/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.google.gwt.user.client.Element;

/**
 * Layout for form fields and their labels. FormLayout will only render Field
 * subclasses. All other components will be ignored.
 * 
 * <p/> To add a component that is not a Field subclass, see
 * {@link AdapterField}. To add plain text see {@link LabelField}.
 */
public class FormLayout extends AnchorLayout {

  private LabelAlign labelAlign = LabelAlign.LEFT;
  private boolean hideLabels;
  private String labelSeparator = ":";
  private int labelWidth = 100;
  private int defaultWidth = 200;
  private Template fieldTemplate;
  private String labelStyle;
  private String elementStyle;
  private int labelPad = 10;
  private int padding = 10;
  private int labelAdjust;
  private Listener<ContainerEvent> listener;

  /**
   * Creates a new form layout.
   */
  public FormLayout() {

  }

  public FormLayout(LabelAlign labelAlign) {
    this.labelAlign = labelAlign;
  }

  /**
   * Returns the default field width.
   * 
   * @return the default field width
   */
  public int getDefaultWidth() {
    return defaultWidth;
  }

  /**
   * Returns true if labels are being hidden.
   * 
   * @return the hide label state
   */
  public boolean getHideLabels() {
    return hideLabels;
  }

  /**
   * Returns the label alignment.
   * 
   * @return the label alignment
   */
  public LabelAlign getLabelAlign() {
    return labelAlign;
  }

  /**
   * Returns the label pad.
   * 
   * @return the label pad
   */
  public int getLabelPad() {
    return labelPad;
  }

  /**
   * Returns the label separator.
   * 
   * @return the label separaotr
   */
  public String getLabelSeparator() {
    return labelSeparator;
  }

  /**
   * Returns the label width.
   * 
   * @return the label width
   */
  public int getLabelWidth() {
    return labelWidth;
  }

  /**
   * Returns the panel's padding.
   * 
   * @return the padding
   */
  public int getPadding() {
    return padding;
  }

  @Override
  public void setContainer(Container ct) {
    if (listener == null) {
      listener = new Listener<ContainerEvent>() {
        public void handleEvent(ContainerEvent be) {
          if (be.item instanceof Field) {
            onRemove((Field) be.item);
          }
        }
      };
    }

    if (this.container != null) {
      this.container.removeListener(Events.BeforeRemove, listener);
    }

    super.setContainer(ct);

    this.container.addListener(Events.BeforeRemove, listener);

    if (labelAlign != null) {
      ct.addStyleName("x-form-label-" + labelAlign.name().toLowerCase());
    }

    if (hideLabels) {
      labelStyle = "display:none";
      elementStyle = "padding-left:0;";
      labelAdjust = 0;
    } else {
      labelStyle = "width:" + labelWidth + "px";
      elementStyle = "padding-left:" + (labelWidth + 5) + "px";
      labelAdjust = labelWidth;
      if (labelAlign == LabelAlign.TOP) {
        labelStyle = "width:auto;";
        elementStyle = "padding-left:0;";
        labelAdjust = 0;
      }
    }

    if (fieldTemplate == null) {
      StringBuffer sb = new StringBuffer();
      sb.append("<div class='x-form-item {5}' tabIndex='-1'>");
      sb.append("<label for={0} style='{2};{7}' class=x-form-item-label>{1}{4}</label>");
      sb.append("<div class='x-form-element' id='x-form-el-{0}' style='{3}'>");
      sb.append("</div><div class='{6}'></div>");
      sb.append("</div>");
      fieldTemplate = new Template(sb.toString());
      fieldTemplate.compile();
    }

  }

  /**
   * Sets the default width for fields (defaults to 200).
   * 
   * @param defaultWidth the default width
   */
  public void setDefaultWidth(int defaultWidth) {
    this.defaultWidth = defaultWidth;
  }

  /**
   * True to hide field labels by default (defaults to false).
   * 
   * @param hideLabels true to hide labels
   */
  public void setHideLabels(boolean hideLabels) {
    this.hideLabels = hideLabels;
  }

  /**
   * Sets the label alignment.
   * 
   * @param labelAlign the label align
   */
  public void setLabelAlign(LabelAlign labelAlign) {
    this.labelAlign = labelAlign;
  }

  /**
   * The default padding in pixels for field labels (defaults to 10). labelPad
   * only applies if labelWidth is also specified, otherwise it will be ignored.
   * 
   * @param labelPad the label pad
   */
  public void setLabelPad(int labelPad) {
    this.labelPad = labelPad;
  }

  /**
   * Sets the label separator (defaults to ':').
   * 
   * @param labelSeparator the label separator
   */
  public void setLabelSeparator(String labelSeparator) {
    this.labelSeparator = labelSeparator;
  }

  /**
   * Sets the default width in pixels of field labels (defaults to 100).
   * 
   * @param labelWidth the label width
   */
  public void setLabelWidth(int labelWidth) {
    this.labelWidth = labelWidth;
  }

  /**
   * Sets the padding to be applied to the forms children (defaults to 10).
   * 
   * @param padding the padding
   */
  public void setPadding(int padding) {
    this.padding = padding;
  }

  @Override
  protected int adjustWidthAnchor(int width, Component comp) {
    if (comp instanceof Field) {
      Field f = (Field) comp;
      int adj = XDOM.isVisibleBox ? padding : (padding / 2);
      return width - (f.isHideLabel() ? 0 : (labelAdjust + adj));
    }
    return super.adjustWidthAnchor(width, comp);
  }

  @Override
  protected boolean isValidParent(Element elem, Element parent) {
    return true;
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);
    if (padding > 0) {
      target.setStyleAttribute("padding", padding);
    }
  }

  protected void onRemove(Field field) {
    if (field.isRendered()) {
      El elem = field.el().findParent(".x-form-item", 5);
      if (elem != null) {
        elem.removeFromParent();
      }
    }
  }

  @Override
  protected void renderComponent(Component component, int index, El target) {
    if (component instanceof Field && !(component instanceof HiddenField)) {
      Field f = (Field) component;
      renderField((Field) component, index, target);
      FormData formData = (FormData) getLayoutData(f);
      if (formData == null) {
        formData = f.getData("formData");
      }

      f.setWidth(defaultWidth);
      if (formData != null) {
        if (formData.getWidth() > 0) {
          f.setWidth(formData.getWidth());
        }
        if (formData.getHeight() > 0) {
          f.setHeight(formData.getHeight());
        }
      }
    } else {
      super.renderComponent(component, index, target);
    }
  }

  private void renderField(Field field, int index, El target) {
    if (field != null && !field.isRendered()) {
      String ls = field.getLabelSeparator() != null ? field.getLabelSeparator() : labelSeparator;
      field.setLabelSeparator(ls);

      Params p = new Params();
      p.add(field.getId());
      p.add(field.getFieldLabel());
      p.add(labelStyle);
      p.add(elementStyle);
      p.add(ls);
      p.add(field.isHideLabel() ? "x-hide-label" : "");
      p.add("x-form-clear-left");
      p.add(field.getLabelStyle());

      fieldTemplate.insert(target.dom, index, p);

      Element parent = XDOM.getElementById("x-form-el-" + field.getId());
      field.render(parent);
    } else {
      super.renderComponent(field, index, target);
    }
  }

}
