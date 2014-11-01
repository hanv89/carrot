package au.com.codeka.carrot.base;

import java.util.HashMap;
import java.util.Map;

public class Context {

  public static final int SCOPE_GLOBAL = 1;
  public static final int SCOPE_SESSION = 2;

  protected Map<String, Object> sessionBindings;
  protected Application application;

  public Context() {
    application = new Application();
    sessionBindings = new HashMap<String, Object>();
  }

  public Context(Application application) {
    if (application == null) {
      application = new Application();
    }
    this.application = application;
    sessionBindings = new HashMap<String, Object>();
  }

  public Application getApplication() {
    return application;
  }

  public Configuration getConfiguration() {
    return application.config;
  }

  public Object getAttribute(String varName, int scope) {
    switch (scope) {
    case SCOPE_GLOBAL:
      return application.globalBindings.get(varName);
    case SCOPE_SESSION:
      return sessionBindings.get(varName);
    default:
      return null;
    }
  }

  public Object getAttribute(String varName) {
    if (sessionBindings.containsKey(varName)) {
      return sessionBindings.get(varName);
    } else if (application.globalBindings.containsKey(varName)) {
      return application.globalBindings.get(varName);
    }
    return null;
  }

  public void setAttribute(String varName, Object value, int scope) {
    switch (scope) {
    case SCOPE_GLOBAL:
      application.globalBindings.put(varName, value);
      break;
    case SCOPE_SESSION:
      sessionBindings.put(varName, value);
      break;
    default:
      throw new IllegalArgumentException("Illegal scope value.");
    }
  }

  public void initBindings(Map<String, Object> bindings, int scope) {
    switch (scope) {
    case SCOPE_GLOBAL:
      application.globalBindings = bindings;
      break;
    case SCOPE_SESSION:
      sessionBindings = bindings;
      break;
    default:
      throw new IllegalArgumentException("Illegal scope value.");
    }
  }

  public void setAttributes(Map<String, Object> bindings, int scope) {
    switch (scope) {
    case SCOPE_GLOBAL:
      application.globalBindings.putAll(bindings);
      break;
    case SCOPE_SESSION:
      sessionBindings.putAll(bindings);
      break;
    default:
      throw new IllegalArgumentException("Illegal scope value.");
    }
  }

  public void reset(int scope) {
    switch (scope) {
    case SCOPE_GLOBAL:
      application.globalBindings.clear();
      break;
    case SCOPE_SESSION:
      sessionBindings.clear();
      break;
    default:
      throw new IllegalArgumentException("Illegal scope value.");
    }
  }

}
