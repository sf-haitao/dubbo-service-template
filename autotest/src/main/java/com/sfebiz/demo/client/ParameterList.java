package com.sfebiz.demo.client;

import java.util.HashMap;
import java.util.Set;

public class ParameterList {
    private HashMap<String, String> params = null;

    public ParameterList() {
        params = new HashMap<String, String>();
    }

    public ParameterList(int initialCapacity) {
        params = new HashMap<String, String>(initialCapacity);
    }

    public final void put(String name, String value) {
        if (name == null || name.length() == 0 || value == null) return;
        params.put(name, value);
    }

    public final Set<String> keySet() {
        return params.keySet();
    }

    public final String get(String key) {
        return params.get(key);
    }

    public final boolean containsKey(String key) {
        return params.containsKey(key);
    }

    public final int size() {
        return params.size();
    }
}
