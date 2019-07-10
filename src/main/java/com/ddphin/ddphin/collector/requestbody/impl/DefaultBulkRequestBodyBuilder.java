package com.ddphin.ddphin.collector.requestbody.impl;

import com.alibaba.fastjson.JSONObject;
import com.ddphin.ddphin.collector.context.ContextHolder;
import com.ddphin.ddphin.collector.context.ESNestedEntry;
import com.ddphin.ddphin.collector.context.ESPrimitiveCollection;
import com.ddphin.ddphin.collector.context.ESPrimitiveEntry;
import com.ddphin.ddphin.collector.entity.ESSyncItemOutputItem;
import com.ddphin.ddphin.collector.entity.ESSyncItemOutputItemHas;
import com.ddphin.ddphin.collector.entity.ESSyncProperties;
import com.ddphin.ddphin.collector.requestbody.RequestBodyBuilder;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DefaultBulkRequestBodyBuilder
 * Function:  DefaultBulkRequestBodyBuilder
 * Date:      2019/7/5 下午3:55
 * Author     DaintyDolphin
 * Version    V1.0
 */

public class DefaultBulkRequestBodyBuilder implements RequestBodyBuilder {
    private Map<String, ESSyncItemOutputItem> outputMap;

    public DefaultBulkRequestBodyBuilder(ESSyncProperties properties) {
        this.setOutputMap(properties.getOutput());
    }

    @Override
    public void setOutputMap(Map<String, ESSyncItemOutputItem> outputMap) {
        this.outputMap = outputMap;
    }

    @Override
    public String build() {
        @SuppressWarnings("unchecked")
        Collection<ESNestedEntry> list = (Collection<ESNestedEntry>) ContextHolder.get().getValue();

        StringBuilder sb = new StringBuilder();
        list.forEach(data -> {
            String index = data.get__name();
            String id = String.valueOf(this.getKeyValue(null, data));
            sb.append(genSource(index, id, data));

        });
        return sb.toString();
    }

    private String genSource(String index, String id, ESNestedEntry data) {

        if (data.get__operation().equals(SqlCommandType.DELETE)) {
            return String.format("{ \"delete\" : { \"_index\" : \"%s\", \"_id\" : \"%s\" } }", index, id);
        }
        else if (data.get__operation().equals(SqlCommandType.INSERT)) {
            String op = String.format("{ \"index\" : { \"_index\" : \"%s\", \"_id\" : \"%s\" } }", index, id);
            return op +
                    System.lineSeparator() +
                    JSONObject.toJSONString(data) +
                    System.lineSeparator();
        }
        else {
            String op =  String.format("{ \"update\" : { \"_index\" : \"%s\", \"_id\" : \"%s\" } }", index, id);
            String script =
                    "{" +
                    "    \"script\" : {" +
                    "        \"source\": \"%s\"," +
                    "        \"lang\" : \"painless\"," +
                    "        \"params\" : %s" +
                    "    }" +
                    "}";
            Map<Object, Object> params = new HashMap<>();
            String source = genNestedUpdate("ctx._source",this.getParamKey(null, null, data), null, data, params);
            script = String.format(script, source, JSONObject.toJSONString(params));
            return op +
                    System.lineSeparator() +
                    script +
                    System.lineSeparator();
        }
    }

    private String genNestedUpdate(String path, String key, ESNestedEntry parent, ESNestedEntry data, Map<Object, Object> params) {
        StringBuilder sb = new StringBuilder();
        ESSyncItemOutputItem item = this.outputMap.get(data.get__name());

        Map<String, Object> param = new HashMap<>();
        param.put(this.getKey(parent, data), this.getKeyValue(parent, data));
        params.put(key, param);

        if (data.get__operation().equals(SqlCommandType.UPDATE)) {
            Map<String, String> fields = new HashMap<>();
            item.getMap().forEach((k, v) -> fields.put(v, null));
            if (null != item.getHas()) {
                item.getHas().forEach((k, v) -> {
                    if (ESSyncItemOutputItemHas.WithType.primitive.equals(v.getWithType())) {
                        this.outputMap.get(k).getMap().forEach((m, n) -> fields.put(n, null));
                    }
                });
            }
            fields.keySet().forEach(k -> {
                if (null != data.get(k)) {
                    param.put(k, data.get(k));
                    sb.append(String.format("%s.%s=params.%s.%s;", path, k, key, k));
                }
            });

            data.get__remove_operation().forEach((k, v) -> {
                ESSyncItemOutputItem hasItem = this.outputMap.get(k);
                hasItem.getMap().forEach((m, n) -> sb.append(String.format("%s.remove('%s');", path, n)));
            });
        }

        if (null != item.getHas()) {
            sb.append(this.genNestedHas(path, key, data, params));
        }
        return sb.toString();
    }

    private String genNestedHas(String path, String prefix, ESNestedEntry data, Map<Object, Object> params) {
        StringBuilder sb = new StringBuilder();
        ESSyncItemOutputItem item = this.outputMap.get(data.get__name());
        item.getHas().forEach((k, v) -> {
            if (ESSyncItemOutputItemHas.WithType.array.equals(v.getWithType())) {
                sb.append(this.genNestedHasPrimitive(v, path, prefix, data, params));
            }
            else if (ESSyncItemOutputItemHas.WithType.nested.equals(v.getWithType())) {
                sb.append(this.genNestedHasNested(v, path, prefix, data, params));
            }
        });
        return sb.toString();
    }

    private String genNestedHasPrimitive(ESSyncItemOutputItemHas has, String path, String prefix, ESNestedEntry data, Map<Object, Object> params) {
        StringBuilder sb = new StringBuilder();
        ESPrimitiveCollection array = (ESPrimitiveCollection) data.get(has.getAs());
        if (null != array) {
            array.details().forEach(o -> {
                String key = this.getParamKey(prefix, o);
                String newPath = path + "." +has.getAs();
                if (o.get__operation().equals(SqlCommandType.INSERT)) {
                    sb.append(this.genPrimitiveInsert(newPath, key, o, params));
                }
                else if (o.get__operation().equals(SqlCommandType.UPDATE)) {
                    sb.append(this.genPrimitiveDelete(newPath, key, o, params));
                }
            });
        }
        return sb.toString();
    }
    private String genNestedHasNested(ESSyncItemOutputItemHas has, String path, String prefix, ESNestedEntry data, Map<Object, Object> params) {
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("unchecked")
        Collection<ESNestedEntry> nested = (Collection<ESNestedEntry>) data.get(has.getAs());
        if (null != nested) {
            nested.forEach( o -> {
                String key = this.getParamKey(prefix, data, o);
                String newPath = path + "." +has.getAs();
                if (o.get__operation().equals(SqlCommandType.INSERT)) {
                    sb.append(this.genNestedInsert(newPath, prefix, data, o, params));
                }
                else if (o.get__operation().equals(SqlCommandType.DELETE)) {
                    sb.append(this.genNestedDelete(newPath, prefix, data, o, params));
                }
                else {
                    sb.append(String.format("for (%s in %s) {", o.get__name(), newPath));
                    sb.append(String.format("   if (%s.%s == params.%s.%s) {", o.get__name(), has.getUniqueBy(), key, has.getUniqueBy()));
                    sb.append(this.genNestedUpdate(o.get__name(), key, data, o, params));
                    sb.append(              "   }");
                    sb.append(              "}");
                }
            });
        }
        return sb.toString();
    }

    private String genNestedInsert(String path, String prefix, ESNestedEntry parent, ESNestedEntry data, Map<Object, Object> params) {
        StringBuilder sb = new StringBuilder();
        String paramKey = this.getParamKey(prefix, parent, data);
        params.put(paramKey, data);

        sb.append(String.format("if (null == %s) {", path));
        sb.append(String.format("   %s=[];", path));
        sb.append(              "}");
        sb.append(String.format("%s.add(%s);", path, "params."+paramKey));

        return sb.toString();
    }

    private String genNestedDelete(String path, String prefix, ESNestedEntry parent, ESNestedEntry data, Map<Object, Object> params) {
        StringBuilder sb = new StringBuilder();

        ESSyncItemOutputItem parentItem = this.outputMap.get(parent.get__name());
        ESSyncItemOutputItemHas has = parentItem.getHas().get(data.get__name());
        if (null == data.get(has.getUniqueBy())) {
            sb.append(String.format("%s=[];", path));
        }
        else {
            String paramKey = this.getParamKey(prefix, parent, data);
            Map<String, Object> param = new HashMap<>();
            param.put(this.getKey(parent, data), this.getKeyValue(parent, data));
            params.put(paramKey, param);
            sb.append(String.format("%s.removeIf(%s -> %s.%s==params.%s.%s);", path, data.get__name(), data.get__name(), has.getUniqueBy(), paramKey, has.getUniqueBy()));
        }
        return sb.toString();
    }

    private String genPrimitiveInsert(String path, String key, ESPrimitiveEntry data, Map<Object, Object> params) {
        StringBuilder sb = new StringBuilder();
        params.put(key, data.getValue());

        sb.append(String.format("if (null == %s) {", path));
        sb.append(String.format("   %s=[];", path));
        sb.append(              "}");
        sb.append(String.format("%s.add(params.%s);", path, key));

        return sb.toString();
    }
    private String genPrimitiveDelete(String path, String key, ESPrimitiveEntry data, Map<Object, Object> params) {
        StringBuilder sb = new StringBuilder();

        if (null == data.getValue()) {
            sb.append(String.format("%s=[];", path));
        }
        else {
            params.put(key, data.getValue());
            sb.append(String.format("%s.removeIf(%s -> %s == params.%s);", path, data.get__name(), data.get__name(), key));
        }
        return sb.toString();
    }


    private String getParamKey(String prefix, ESNestedEntry parent, ESNestedEntry data) {
        return this.getParamKey(prefix, String.valueOf(this.getKeyValue(parent, data)), data.get__name());
    }
    private String getParamKey(String prefix, ESPrimitiveEntry data) {
        return this.getParamKey(prefix, String.valueOf(data.getValue()), data.get__name());
    }
    private String getParamKey(String prefix, String key, String name) {
        if (null != prefix) {
            return prefix + "_" + name + "_" + key;
        }
        else {
            return name + "_" + key;
        }
    }
    private Object getKeyValue(ESNestedEntry parent, ESNestedEntry data) {
        Object value = data.get(this.getKey(parent, data));
        if (value instanceof  Map) {
            return ((Map)value).get("input");
        }
        else {
            return value;
        }

    }
    private String getKey(ESNestedEntry parent, ESNestedEntry data) {
        if (null == parent) {
            return this.outputMap.get(data.get__name()).getKey();
        }
        else {
            return this.outputMap.get(parent.get__name()).getHas().get(data.get__name()).getUniqueBy();
        }
    }
}
