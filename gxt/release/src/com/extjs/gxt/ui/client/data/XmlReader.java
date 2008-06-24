/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.ArrayList;

import com.extjs.gxt.ui.client.core.DomQuery;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * A <code>DataReader</code> implementation that reads XML data using a
 * <code>ModelType</code> definition and produces a set of
 * <code>ModelData</code> instances. Subclass may override
 * {@link #newModelInstance()} to return any model data subclass.
 * 
 * @param <C> the load config type
 */
public class XmlReader<C> implements DataReader<C, ListLoadResult<ModelData>> {

  private ModelType modelType;

  /**
   * Creates a new xml reader instance.
   * 
   * @param modelType the model type
   */
  public XmlReader(ModelType modelType) {
    this.modelType = modelType;
  }

  public ListLoadResult read(C loadConfig, Object data) {
    Document doc = XMLParser.parse((String) data);
    Node root = doc.getFirstChild();
    NodeList list = doc.getElementsByTagName(modelType.recordName);
    ArrayList<BaseModel> records = new ArrayList<BaseModel>();
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      Element elem = (Element) node;
      BaseModel model = new BaseModel();
      for (int j = 0; j < modelType.getFieldCount(); j++) {
        DataField field = modelType.getField(j);
        String map = field.map != null ? field.map : field.name;
        String v = getValue(elem, map);
        model.set(field.name, v);
      }
      records.add(model);
    }

    int totalCount = records.size();

    if (modelType.totalName != null) {
      Node totalNode = root.getAttributes().getNamedItem(modelType.totalName);
      if (totalNode != null) {
        String sTot = totalNode.getNodeValue();
        totalCount = Integer.parseInt(sTot);
      }
    }

    return new BasePagingLoadResult(records, 0, totalCount);
  }

  protected native JavaScriptObject getJsObject(Element elem) /*-{
     return elem.@com.google.gwt.xml.client.impl.DOMItem::getJsObject()();
   }-*/;

  protected String getValue(Element elem, String name) {
    return DomQuery.selectValue(name, getJsObject(elem));
  }

  /**
   * Returns the new model instances. Subclasses may override to provide a
   * model data subclass.
   * 
   * @return the new model data instance
   */
  protected ModelData newModelInstance() {
    return new BaseModelData();
  }

}
