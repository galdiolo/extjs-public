/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.io.Serializable;
import java.util.Collection;

/**
 * Contains the results of a data load operation.
 * 
 * @param <D> the data type
 */
public class BaseLoadResult<D> implements LoadResult<D>, Serializable {

  public BaseLoadResult() {
    this(null);
  }

  public BaseLoadResult(D data) {
    this.data = data;
    this.success = true;
    this.totalLength = data == null ? 0 : 1;
  }

  /**
   * Specifies if the load was successful. Default value is <code>true</code>.
   */
  protected boolean success = true;

  protected int offset = 0;

  /**
   * Allow the server to communicate that the offset had to be changed becuase
   * of a change in data on the server
   */
  public int getOffset() {
    return offset;
  }

  /**
   * The total number of elements. This may not be the same as the number of
   * elements when implementing paging.
   */
  protected int totalLength = 0;

  public boolean isSuccess() {
    return success;
  }

  public int getTotalLength() {
    return totalLength;
  }

  protected D data;

  public D getData() {
    return data;
  }

  public static class CollectionLoadResult<X> extends BaseLoadResult<Collection<X>> {
    public CollectionLoadResult() {
      this(null);
    }

    public CollectionLoadResult(Collection<X> data) {
      this.data = data;
      this.success = true;
      this.totalLength = data == null ? 0 : data.size();
    }

    public CollectionLoadResult(Collection<X> data, int totalLength) {
      this.data = data;
      this.success = true;
      this.totalLength = totalLength;
    }

    public CollectionLoadResult(Collection<X> data, int totalLength, int offset) {
      this.data = data;
      this.success = true;
      this.totalLength = totalLength;
      this.offset = offset;
    }
  }

  public static class ModelCollectionLoadResult<X extends Model> extends
      BaseLoadResult<Collection<X>> {

    /**
     * this field is needed to workaround a RPC issue with GWT 1.5 m2 and
     * generics
     */
    protected Collection<X> data;

    public ModelCollectionLoadResult() {
      this(null);
    }

    public ModelCollectionLoadResult(Collection<X> data) {
      this.data = data;
      this.success = true;
      this.totalLength = data == null ? 0 : data.size();
    }

    public ModelCollectionLoadResult(Collection<X> data, int totalLength) {
      this.data = data;
      this.success = true;
      this.totalLength = totalLength;
    }

    public ModelCollectionLoadResult(Collection<X> data, int totalLength, int offset) {
      this.data = data;
      this.success = true;
      this.totalLength = totalLength;
      this.offset = offset;
    }

    /**
     * this method is needed to workaround a RPC issue with GWT 1.5 m2 and
     * generics
     */
    public Collection<X> getData() {
      return data;
    }
  }

  public static class FailedLoadResult extends BaseLoadResult<Throwable> {

    public FailedLoadResult() {
      this(null);
    }

    public FailedLoadResult(Throwable throwable) {
      this.data = throwable;
      this.success = false;
      this.totalLength = 0;
    }
  }

}
