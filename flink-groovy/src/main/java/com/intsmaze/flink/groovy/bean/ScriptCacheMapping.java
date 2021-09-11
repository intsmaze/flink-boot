package com.intsmaze.flink.groovy.bean;

import com.google.common.collect.Maps;
import groovy.lang.Script;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

public class ScriptCacheMapping implements Serializable {

    private static ConcurrentMap<Long, Pair<String, Script>> scriptCache = Maps.newConcurrentMap();

    public ConcurrentMap<Long, Pair<String, Script>> getScriptCache() {
        return scriptCache;
    }

    public void addScript(Long id, String hash, Script obj) {
        scriptCache.put(id, Pair.of(hash, obj));
    }

    public boolean contains(Long id) {
        return scriptCache.containsKey(id);
    }

    public boolean isDifference(Long id, String hash) {
        if (!contains(id)) {
            return false;
        }
        return scriptCache.get(id).getLeft().equals(hash);
    }

    public Script getScript(Long id) {
        if (!contains(id)) {
            return null;
        }
        return scriptCache.get(id).getRight();
    }

    public void remove(Long id) {
        if (contains(id)) {
            scriptCache.remove(id);
        }
    }

}
