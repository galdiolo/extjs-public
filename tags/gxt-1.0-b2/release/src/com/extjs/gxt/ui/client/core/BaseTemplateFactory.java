package com.extjs.gxt.ui.client.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseTemplateFactory implements TemplateFactory {

  protected static Cache cache = new Cache();
  
  public static class Cache {

    private Map<Key, Object> cacheMap = new HashMap<Key, Object>();

    public Object get(Object... keyValues) {
      return cacheMap.get(new Key(keyValues));
    }

    public void put(Object template, Object... keyValues) {
      cacheMap.put(new Key(keyValues), template);
    }

    public static class Key {
      private Object keyValues[] = null;

      public Key(Object... keyValues) {
        this.keyValues = keyValues;
      }

      @Override
      public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Key)) {
          return false;
        }
        Key other = (Key) obj;
        if (other.keyValues == null && keyValues == null) {
          return true;
        }
        if (other.keyValues == null || keyValues == null
            || other.keyValues.length != keyValues.length) {
          return false;
        }
        for (int i = 0; i < keyValues.length; i++) {
          if (!(keyValues[i] == other.keyValues[i] || (keyValues[i] != null && keyValues[i].equals(other.keyValues[i])))) {
            return false;
          }
        }
        return true;
      }

      @Override
      public int hashCode() {
        int result = 17;

        if (keyValues != null) {
          for (Object object : keyValues) {
            result = 37 * result + (object == null ? 1 : object.hashCode());
          }
        }
        return result;
      }

      @Override
      public String toString() {
        StringBuffer sb = new StringBuffer("{");
        boolean first = true;

        if (keyValues != null) {
          for (Object object : keyValues) {
            if (!first) {
              sb.append(',');
            } else {
              first = false;
            }
            sb.append(object == null ? "null" : object.toString());
          }
        }
        sb.append('}');
        return sb.toString();
      }

    }
  }


  public static class IterableRange<T extends Number> implements Iterable<T> {
      private T start;
      private T end;
      private int order;

      public IterableRange(T start, T end) {
          this.start = start;
          this.end = end;
          order = ((Comparable) start).compareTo(end);
      }

      public Iterator<T> iterator() {
          return new IterableRangeIterator();
      }

      public class IterableRangeIterator implements Iterator<T> {
          private T current;
          private boolean hasNext = true;
          private int inc = 1;

          public IterableRangeIterator() {
              current = start;
              if (order > 0) {
                  inc = -1;
              }
          }

          public boolean hasNext() {
              return hasNext;
          }

          public T next() {
              if (!hasNext) {
                  throw new IllegalStateException("End of iterator reached.");
              }

              T retVal = current;

              if (retVal instanceof Long) {
                  long value = current.longValue();
                  value += inc;
                  current = (T) new Long(value);
              } else {
                  int value = current.intValue();
                  value += inc;
                  current = (T) new Integer(value);
              }
              hasNext = inc == -1 && ((Comparable) end).compareTo(current) <= 0
                      || inc == 1 && ((Comparable) end).compareTo(current) >= 0;

              return retVal;
          }

          public void remove() {
              throw new RuntimeException("remove() method not supported");
          }

      }

  }

}
