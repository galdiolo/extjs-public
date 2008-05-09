/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;


import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.user.client.Element;

public class FormLayout extends AnchorLayout {

  /**
   * The default label alignment (defautls to 'left').
   */
  public String labelAlign = "left";

  /**
   * True to hide field labels by default (defaults to false).
   */
  public boolean hideLabels;

  /**
   * The label seperator (defaults to ':').
   */
  public String labelSeperator = ":";

  /**
   * The default width in pixels of field labels (defaults to 100).
   */
  public int labelWidth = 100;
  
  public int defaultWidth = 200;

  private Template fieldTemplate;
  private String labelStyle;
  private String elementStyle;

  public void setContainer(Container ct) {
    super.setContainer(ct);
   
    if (labelAlign != null) {
      ct.addStyleName("x-form-label-" + labelAlign);
    }

    if (hideLabels) {
      labelStyle = "display:none";
      elementStyle = "padding-left:0;";
    } else {
      labelStyle = "width:" + labelWidth + "px";
      elementStyle = "padding-left:" + (labelWidth + 5) + "px";

      if (labelAlign.equals("top")) {
        labelStyle = "width:auto;";
        elementStyle = "padding-left:0;";
      }

    }

    if (fieldTemplate == null) {
      StringBuffer sb = new StringBuffer();
      sb.append("<div class='x-form-item {5}' tabIndex='-1'>");
      sb.append("<label for={0} style='{2}' class=x-form-item-label>{1}{4}</label>");
      sb.append("<div class='x-form-element' id='x-form-el-{0}' style='{3}'>");
      sb.append("</div><div class='{6}'></div>");
      sb.append("</div>");
      fieldTemplate = new Template(sb.toString());
      fieldTemplate.compile();
    }

  }

  @Override
  protected boolean isValidParent(Element elem, Element parent) {
    return true;
  }

  @Override
  protected void renderComponent(Component component, int index, El target) {
    if (component instanceof Field) {
      Field f = (Field)component;
      renderField((Field) component, index, target);
      FormData formData = (FormData)f.getData("formData");
      f.setWidth(defaultWidth);
      if (formData != null) {
        if (formData.width > 0) {
          f.setWidth(formData.width);
        }
        if (formData.height > 0) {
          f.setHeight(formData.height);
        }
      }
      
    }
  }

  private void renderField(Field field, int index, El target) {
    if (field != null && !field.isRendered()) {

      fieldTemplate.append(target.dom, field.getId(), field.getFieldLabel(), labelStyle,
          elementStyle, labelSeperator, hideLabels ? "x-hide-label" : "",
          "x-form-clear-left");

      Element parent = XDOM.getElementById("x-form-el-" + field.getId());
      field.render(parent);

    } else {
      super.renderComponent(field, index, target);
    }
  }

}
