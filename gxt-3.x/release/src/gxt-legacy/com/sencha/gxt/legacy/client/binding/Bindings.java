/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.legacy.client.binding;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.legacy.client.data.ModelData;
import com.sencha.gxt.widget.core.client.form.Field;

/**
 * Aggregates one to many field bindings.
 * 
 * <dl>
 * <dt><b>Events:</dt>
 * 
 * <dd><b>BeforeBind</b> : BindingEvent(source, model)<br>
 * <div>Fires before binding.</div>
 * <ul>
 * <li>source : this</li>
 * <li>model : the model to bind</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Bind</b> : BindingEvent(source, model)<br>
 * <div>Fires after successful binding.</div>
 * <ul>
 * <li>source : this</li>
 * <li>model : the binded model</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>UnBind</b> : BindingEvent(source, model)<br>
 * <div>Fires after successful unbinding.</div>
 * <ul>
 * <li>source : this</li>
 * <li>model : the unbound model</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @see FieldBinding
 */
@SuppressWarnings("deprecation")
public class Bindings {

  protected ModelData model;
  protected Map<String, FieldBinding> bindings;

  private final HandlerManager handlerManager = new HandlerManager(this);

  /**
   * Creates a new bindings instance.
   */
  public Bindings() {
    bindings = new FastMap<FieldBinding>();
  }

  /**
   * Adds a field binding.
   * 
   * @param binding the binding instance to add
   */
  public void addFieldBinding(FieldBinding binding) {
    bindings.put(binding.getField().getId(), binding);
  }

  /**
   * Binds the model instance.
   * 
   * @param model the model
   */
  public void bind(final ModelData model) {
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        BindingEvent e = new BindingEvent(model, Bind);
        handlerManager.fireEvent(e);
        if (!e.isCancelled()) {
          if (Bindings.this.model != null) {
            unbind();
          }
          Bindings.this.model = model;
          for (FieldBinding binding : bindings.values()) {
            binding.bind(model);
          }
          handlerManager.fireEvent(e);
        }
      }
    });
  }

  /**
   * Clears all fields by setting the value for each field to <code>null</code>.
   */
  public void clear() {
    for (FieldBinding fieldBinding : getBindings()) {
      fieldBinding.getField().clear();
    }
  }

  /**
   * Returns the field binding for the given field.
   * 
   * @param field the field
   * @return the field binding or null of no match
   */
  public FieldBinding getBinding(Field<?> field) {
    return bindings.get(field.getId());
  }

  /**
   * Returns all bindings.
   * 
   * @return the collection of bindings
   */
  public Collection<FieldBinding> getBindings() {
    return bindings.values();
  }

  /**
   * Returns the currently bound model;
   * 
   * @return the currently bound model;
   */
  public ModelData getModel() {
    return model;
  }

  /**
   * Removes a field binding.
   * 
   * @param binding the binding instance to remove
   */
  public void removeFieldBinding(FieldBinding binding) {
    bindings.remove(binding.getField().getId());
  }

  /**
   * Unbinds the current model.
   */
  public void unbind() {
    if (model != null) {
      for (FieldBinding binding : bindings.values()) {
        binding.unbind();
      }
      handlerManager.fireEvent(new BindingEvent(model, UnBind));
      model = null;
    }
  }

  static final GwtEvent.Type<BindingHandler> UnBind = new GwtEvent.Type<BindingHandler>();

  static final GwtEvent.Type<BindingHandler> Bind = new GwtEvent.Type<BindingHandler>();
  static final GwtEvent.Type<BindingHandler> BeforeBind = new GwtEvent.Type<BindingHandler>();

  public static class BindingEvent extends GwtEvent<BindingHandler> {
    private final Type<BindingHandler> type;
    private boolean cancelled;

    public BindingEvent(ModelData model, GwtEvent.Type<BindingHandler> type) {
      this.type = type;
    }
    @Override
    public GwtEvent.Type<BindingHandler> getAssociatedType() {
      return type;
    }

    @Override
    protected void dispatch(BindingHandler handler) {
      handler.handleEvent(this);
    }

    public boolean isCancelled() {
      return cancelled;
    }

    public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
    }
  }
  interface BindingHandler extends EventHandler {
    void handleEvent(BindingEvent evt);
  }
}
