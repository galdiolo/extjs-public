/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RpcMap is used to workaround a part of GWT RPC system.
 * 
 * The GWT RPC rebinder generates field serializers for every type that is
 * assignable to any type in the RPC interfaces.
 * 
 * If BaseModel was to use "Map<String, Serializable> map" this would trigger
 * the RPC system to generate field serializers for EVERY Serializable type in
 * your GWT Module's class path.
 * 
 * Therfore BaseModel uses "Map<String, RpcField> map" and relies on type
 * erasure (cast to Map<Object,Object>) to work around this.
 * 
 * The only drawback is that if you have to ensure that field serializers are
 * generated for any type you add to this map
 * 
 * RpcMap ensures that the following types are supported Byte, Short, Integer,
 * Long, Float, Double, Date, Boolean, and arrays of these types.  As well as List, Set and Map
 */
@SuppressWarnings("unused")
public class RpcMap implements Map<String, Object> {

  private Byte _byte;
  private Short _short;
  private Integer _integer;
  private Long _long;
  private Float _float;
  private Double _double;
  private Date _date;
  private Boolean _boolean;

  private Byte[] _bytes;
  private Short[] _shorts;
  private Integer[] _integers;
  private Long[] _longs;
  private Float[] _floats;
  private Double[] _doubles;
  private Date[] _dates;
  private Boolean[] _booleans;

  private List<String> _list;
  private Set<String> _set;
  private Map<String, String> _map;

  
  private transient Map<String, Object> map = new HashMap<String, Object>();

  public void clear() {
    map.clear();
  }

  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  public Set<Entry<String, Object>> entrySet() {
    return map.entrySet();
  }

  public boolean equals(Object o) {
    return map.equals(o);
  }

  public Object get(Object key) {
    return map.get(key);
  }

  public int hashCode() {
    return map.hashCode();
  }

  public boolean isEmpty() {
    return map.isEmpty();
  }

  public Set<String> keySet() {
    return map.keySet();
  }

  public Object put(String key, Object value) {
    return map.put(key, value);
  }

  public void putAll(Map<? extends String, ? extends Object> m) {
    map.putAll(m);
  }

  public Object remove(Object key) {
    return map.remove(key);
  }

  public int size() {
    return map.size();
  }

  public Collection<Object> values() {
    return map.values();
  }

  @Override
  public String toString() {
    return map.toString();
  }

}