package com.ddphin.ddphin.collector.collector.impl;

import com.alibaba.fastjson.JSONObject;
import com.ddphin.ddphin.collector.collector.Collector;
import com.ddphin.ddphin.collector.configuration.CollectorProperties;
import com.ddphin.ddphin.collector.context.*;
import com.ddphin.ddphin.collector.entity.ESSyncItemOutputItem;
import com.ddphin.ddphin.collector.entity.ESSyncItemOutputItemHas;
import com.ddphin.ddphin.collector.mybatis.SQLExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CollectorImpl
 *
 * @Date     2019/7/8 下午3:04
 * @Author   ddphin
 */
@Service
public class CollectorImpl implements Collector {

    @Autowired
    private CollectorProperties properties;
    @Autowired
    private SQLExecutor sqlExecutor;

    /**
     * collect
     *
     * @Date     2019/7/8 下午3:04
     * @Author   ddphin
     *
     * @param    object
     * @param    output
     * @param    executor
     * @param    ms
     * @return   void
     */
    @Override
    public void collect(
            Object object,
            String output,
            Executor executor,
            MappedStatement ms) throws SQLException {
        ESEntry entity = this.initCurrentEntity(output, object, ms.getSqlCommandType());

        this.collect(entity, output, properties, executor, ms);
    }

    /**
     * initCurrentEntity
     *
     * @Date     2019/7/8 下午3:04
     * @Author   ddphin
     *
     * @param    output
     * @param    object
     * @param    operation
     * @return   ESEntry
     */
    private ESEntry initCurrentEntity(String output, Object object, SqlCommandType operation) {
       JSONObject json = (JSONObject) JSONObject.toJSON(object);

        ESSyncItemOutputItem outputItem = properties.getOutput().get(output);

        ESEntry entity = new ESEntry(operation);
        Map<Object, ESEntry> map = ContextHolder.get().getAssociation().computeIfAbsent(output, o -> new HashMap<>());
        if (null != json.get(outputItem.getKey())) {
            entity = map.computeIfAbsent(json.get(outputItem.getKey()), o -> new ESEntry(operation));
            entity.setOperation(operation);
        }
        this.reloadESEntity(json, entity, outputItem);

        return entity;
    }

    /**
     * initAssociationEntity
     *
     * @Date     2019/7/8 下午3:05
     * @Author   ddphin
     *
     * @param    output
     * @param    object
     * @return   ESEntry
     */
    private ESEntry initAssociationEntity(String output, ESEntry object) {
        ESSyncItemOutputItem item = properties.getOutput().get(output);
        ESSyncItemOutputItem associationItem = properties.getOutput().get(item.getAssociation().getBelongs().getTo());

        Map<Object, ESEntry> map = ContextHolder.get().getAssociation().computeIfAbsent(item.getAssociation().getBelongs().getTo(), o -> new HashMap<>());
        ESEntry entity = map.computeIfAbsent(object.getWithOfBelongs(), o -> new ESEntry(SqlCommandType.UNKNOWN));

        if (associationItem.getKey().equals(item.getAssociation().getBelongs().getAs())) {
            entity.setKey(object.getWithOfBelongs());
        }
        if (associationItem.getMap().containsKey(item.getAssociation().getBelongs().getAs())) {
            entity.put(associationItem.getMap().get(item.getAssociation().getBelongs().getAs()), object.getWithOfBelongs());
        }
        if (null != associationItem.getAssociation()) {
            if (associationItem.getAssociation().getOnMissing().getWith().equals(item.getAssociation().getBelongs().getAs())) {
                entity.setWithOfOnMissing(object.getWithOfBelongs());
            }
        }
        if (null != associationItem.getReload()) {
            if (associationItem.getReload().getWith().equals(item.getAssociation().getBelongs().getAs())) {
                entity.setWithOfReload(object.getWithOfBelongs());
            }
        }
        return entity;
    }

    /**
     * collect
     *
     * @Date     2019/7/8 下午3:05
     * @Author   ddphin
     *
     * @param    entity
     * @param    output
     * @param    properties
     * @param    executor
     * @param    ms
     * @return   void
     */
    private void collect(ESEntry entity,
                         String output,
                         CollectorProperties properties,
                         Executor executor,
                         MappedStatement ms) throws SQLException {
        ESSyncItemOutputItem outputItem = properties.getOutput().get(output);

        if (null != outputItem.getAssociation()) {
            if (!this.mergeESEntryAssociation(entity, output, properties, executor, ms)) {
                return;
            }

            Map<Object, Object> datamap = ContextHolder.get().getData().computeIfAbsent(outputItem.getAssociation().getBelongs().getTo(), o -> new HashMap<>());
            ESNestedEntry association = (ESNestedEntry) datamap.computeIfAbsent(entity.getWithOfBelongs(), o -> new ESNestedEntry(outputItem.getAssociation().getBelongs().getTo(), SqlCommandType.UNKNOWN));
            ESSyncItemOutputItem associationItem = properties.getOutput().get(outputItem.getAssociation().getBelongs().getTo());

            ESSyncItemOutputItemHas.WithType withType = associationItem.getHas().get(output).getWithType();
            if (ESSyncItemOutputItemHas.WithType.primitive.equals(withType)) {
                ESNestedEntry current = this.mergeCurrentESNestedEntryData(output, entity);
                association.putAll(current);
                if (current.get__operation().equals(SqlCommandType.INSERT)
                    ||current.get__operation().equals(SqlCommandType.UPDATE)) {
                    association.set__operation(SqlCommandType.UPDATE);
                }
                else if (current.get__operation().equals(SqlCommandType.DELETE)) {
                    association.get__remove_operation().put(current.get__name(), SqlCommandType.DELETE);
                    association.set__operation(SqlCommandType.UPDATE);
                }
            }
            else if (ESSyncItemOutputItemHas.WithType.array.equals(withType)) {
                ESPrimitiveEntry current = this.mergeCurrentESPrimitiveEntryData(output, entity);
                ESPrimitiveCollection array = (ESPrimitiveCollection)association.computeIfAbsent(associationItem.getHas().get(output).getAs(), o -> new ESPrimitiveCollection());
                array.add(current);
            }
            else if (ESSyncItemOutputItemHas.WithType.nested.equals(withType)) {
                ESNestedEntry current = this.mergeCurrentESNestedEntryData(output, entity);
                ESNestedCollection nested = (ESNestedCollection) association.computeIfAbsent(associationItem.getHas().get(output).getAs(), o -> new ESNestedCollection());
                nested.add(entity.getKey(), current);
            }

            ESEntry associationEntity = this.initAssociationEntity(output, entity);
            this.collect(associationEntity, outputItem.getAssociation().getBelongs().getTo(), properties, executor, ms);
        }
        else {
            ESNestedEntry entry = this.mergeCurrentESNestedEntryData(output, entity);
            ContextHolder.get().getValue().add(entity.getKey(), entry);
        }
    }

    /**
     * mergeESEntryAssociation
     *
     * @Date     2019/7/8 下午3:06
     * @Author   ddphin
     *
     * @param    entity
     * @param    output
     * @param    properties
     * @param    executor
     * @param    ms
     * @return   java.lang.Boolean
     */
    private Boolean mergeESEntryAssociation(
            ESEntry entity,
            String output,
            CollectorProperties properties,
            Executor executor,
            MappedStatement ms) throws SQLException {
        ESSyncItemOutputItem outputItem = properties.getOutput().get(output);

        if (null == entity.getWithOfBelongs()) {
            if (null == entity.getWithOfOnMissing()) {
                return false;
            }

            Map<String, Object> rs = this.executorQuery(
                    output,
                    properties,
                    executor,
                    ms,
                    outputItem.getAssociation().getOnMissing().getQuery(),
                    outputItem.getAssociation().getOnMissing().getWith(),
                    entity.getWithOfOnMissing());
            if (null == rs || null == rs.get(outputItem.getAssociation().getBelongs().getWith())) {
                return false;
            }
            this.reloadESEntity(rs, entity, outputItem);
        }
        else if (null != outputItem.getReload()) {
            Map<String, Object> rs = this.executorQuery(
                    output,
                    properties,
                    executor,
                    ms,
                    outputItem.getReload().getQuery(),
                    outputItem.getReload().getWith(),
                    entity.getWithOfReload());
            if (null == rs ) {
                return false;
            }
            this.reloadESEntity(rs, entity, outputItem);
        }
        return true;
    }

    /**
     * reloadESEntity
     *
     * @Date     2019/7/8 下午3:06
     * @Author   ddphin
     *
     * @param    map
     * @param    entity
     * @param    outputItem
     * @return   void
     */
    private void reloadESEntity(Map<String, Object> map, ESEntry entity, ESSyncItemOutputItem outputItem) {
        entity.setKey(map.get(outputItem.getKey()));
        if (null != outputItem.getAssociation()) {
            entity.setWithOfBelongs(map.get(outputItem.getAssociation().getBelongs().getWith()));
            entity.setWithOfOnMissing(map.get(outputItem.getAssociation().getOnMissing().getWith()));
        }
        if (null != outputItem.getReload()) {
            entity.setWithOfReload(map.get(outputItem.getReload().getWith()));
        }
        for (Map.Entry<String, String> m : outputItem.getMap().entrySet()) {
            entity.put(m.getValue(), map.get(m.getKey()));
        }
    }

    /**
     * mergeCurrentESNestedEntryData
     *
     * @Date     2019/7/8 下午3:06
     * @Author   ddphin
     *
     * @param    output
     * @param    entity
     * @return   ESNestedEntry
     */
    private ESNestedEntry mergeCurrentESNestedEntryData(String output, ESEntry entity) {
        Map<Object, Object> currentMap = ContextHolder.get().getData().computeIfAbsent(output, o -> new HashMap<>());
        ESNestedEntry current = (ESNestedEntry) currentMap.computeIfAbsent(entity.getKey(), o -> new ESNestedEntry(output, entity.getOperation()));

        current.putAll(entity);

        if (!entity.getOperation().equals(SqlCommandType.UNKNOWN)) {
            current.set__operation(entity.getOperation());
        }
        return current;
    }

    /**
     * mergeCurrentESPrimitiveEntryData
     *
     * @Date     2019/7/8 下午3:06
     * @Author   ddphin
     *
     * @param    output
     * @param    entity
     * @return   ESPrimitiveEntry
     */
    private ESPrimitiveEntry mergeCurrentESPrimitiveEntryData(String output, ESEntry entity) {
        Map<Object, Object> currentMap = ContextHolder.get().getData().computeIfAbsent(output, o -> new HashMap<>());
        ESPrimitiveEntry current = (ESPrimitiveEntry) currentMap.computeIfAbsent(entity.getKey(), o -> new ESPrimitiveEntry(output, entity.getOperation()));

        if (!CollectionUtils.isEmpty(entity.values())) {
            current.setValue(entity.values().iterator().next());
        }

        if (!entity.getOperation().equals(SqlCommandType.UNKNOWN)) {
            current.set__operation(entity.getOperation());
        }
        return current;
    }

    /**
     * executorQuery
     *
     * @Date     2019/7/8 下午3:06
     * @Author   ddphin
     *
     * @param    output
     * @param    properties
     * @param    executor
     * @param    ms
     * @param    method
     * @param    k
     * @param    v
     * @return   java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String, Object> executorQuery(
            String output,
            CollectorProperties properties,
            Executor executor,
            MappedStatement ms,
            String method,
            String k,
            Object v) throws SQLException {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put(k, v);

        String id = properties.getInputR().get(output)+"."+method;
        MappedStatement queryMs = ms.getConfiguration().getMappedStatement(id);
        if (null != queryMs) {
            List query = executor.query(
                    queryMs,
                    parameter,
                    RowBounds.DEFAULT,
                    Executor.NO_RESULT_HANDLER);
            return CollectionUtils.isEmpty(query) ? null : (JSONObject) JSONObject.toJSON(query.get(0));
        }
        else {
            return sqlExecutor.selectOne(method, parameter);
        }
    }
}
