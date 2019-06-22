/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-07 15:39:43
 */
package com.ycg.ksh.common.extend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单封装
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-07 15:39:43
 */
public class MapCollection<K, V> extends HashMap<K, List<V>> {
    
    private static final long serialVersionUID = 6846690937106084641L;

    
    public MapCollection() {
        super();
    }

    public MapCollection(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MapCollection(int initialCapacity) {
        super(initialCapacity);
    }

    public MapCollection(Map<? extends K, ? extends List<V>> m) {
        super(m);
    }

    public void push(K key, V value) {
        List<V> collection = get(key);
        if(collection == null) {
            collection = new ArrayList<V>();
            collection.add(value);
            put(key, collection);
        }else {
            collection.add(value); 
        }
    }
}   
