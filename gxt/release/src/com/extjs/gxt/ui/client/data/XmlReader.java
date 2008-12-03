/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.ArrayList;
import java.util.List;

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
 * <code><pre>
 *  // defines the xml structure
 *  ModelType type = new ModelType();
 *  type.recordName = "record"; // The repeated element which contains row information
 *  type.root = "records"; // the root element that contains the total attribute (optional)
 *  type.totalName = "total"; // The element which contains the total dataset size (optional)
 * </pre></code>
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

    NodeList list = doc.getElementsByTagName(modelType.recordName);
    ArrayList<ModelData> records = new ArrayList<ModelData>();
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      Element elem = (Element) node;
      ModelData model = newModelInstance();
      for (int j = 0; j < modelType.getFieldCount(); j++) {
        DataField field = modelType.getField(j);
        String map = field.map != null ? field.map : field.name;
        String v = getValue(elem, map);
        model.set(field.name, v);
      }
      records.add(model);
    }

    int totalCount = records.size();

    Node root = doc.getElementsByTagName(modelType.root).item(0);
    if (root != null && modelType.totalName != null) {
      Node totalNode = root.getAttributes().getNamedItem(modelType.totalName);
      if (totalNode != null) {
        String sTot = totalNode.getNodeValue();
        totalCount = Integer.parseInt(sTot);
      }
    }
    ListLoadResult result = newLoadResult(loadConfig, records);
    if (result instanceof PagingLoadResult) {
      PagingLoadResult r = (PagingLoadResult) result;
      r.setTotalLength(totalCount);
    }
    return result;
  }

  protected native JavaScriptObject getJsObject(Element elem) /*-{
    return elem.@com.google.gwt.xml.client.impl.DOMItem::getJsObject()();
    }-*/;

  protected String getValue(Element elem, String name) {
    return DomQuery.selectValue(name, getJsObject(elem));
  }

  /**
   * Template method that provides load result.
   * 
   * @param models the models
   * @return the load result
   */
  protected ListLoadResult newLoadResult(C loadConfig, List<ModelData> models) {
    return new BaseListLoadResult(models);
  }

  /**
   * Returns the new model instances. Subclasses may override to provide a model
   * data subclass.
   * 
   * @return the new model data instance
   */
  protected ModelData newModelInstance() {
    return new BaseModelData();
  }

}
