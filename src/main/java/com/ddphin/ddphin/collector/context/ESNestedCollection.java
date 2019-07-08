package com.ddphin.ddphin.collector.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ClassName: ESNestedCollection
 * Function:  ESNestedCollection
 * Date:      2019/7/5 上午10:10
 * Author     DaintyDolphin
 * Version    V1.0
 */
public class ESNestedCollection implements Collection {
    private transient Map<Object, ESNestedEntry> map = new HashMap<>();
    @Override
    public int size() {
        return map.values().size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator iterator() {
        return map.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return map.values().toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return map.values().toArray(a);
    }

    @Deprecated
    @Override
    public boolean add(Object o) {
        return false;
    }
    public ESNestedEntry add(Object k, ESNestedEntry v) {
        return map.put(k, v);
    }

    @Override
    public boolean remove(Object o) {
        return null != map.remove(o);
    }

    @Deprecated
    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Deprecated
    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Deprecated
    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Deprecated
    @Override
    public boolean containsAll(Collection c) {
        return false;
    }
}
