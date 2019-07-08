package com.ddphin.ddphin.collector.context;

import java.util.*;

/**
 * ClassName: ESPrimitiveCollection
 * Function:  ESPrimitiveCollection
 * Date:      2019/7/5 下午6:58
 * Author     DaintyDolphin
 * Version    V1.0
 */
public class ESPrimitiveCollection implements Collection {
    private transient Map<Object, ESPrimitiveEntry> map = new HashMap<>();

    @Override
    public int size() {
        return map.size();
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
        return map.values().stream().map(ESPrimitiveEntry::getValue).iterator();
    }
    public Collection<ESPrimitiveEntry> details() {
        return map.values();
    }

    @Override
    public Object[] toArray() {
        return map.values().stream().map(ESPrimitiveEntry::getValue).toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return map.values().stream().map(ESPrimitiveEntry::getValue).toArray(o -> a);
    }

    @Override
    public boolean add(Object o) {
        map.put(((ESPrimitiveEntry)o).getValue(), (ESPrimitiveEntry) o);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        map.remove(((ESPrimitiveEntry)o).getValue());
        return true;
    }

    @Deprecated
    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Deprecated
    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Deprecated
    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Deprecated
    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public void clear() {
        map.clear();
    }
}
