package com.ddphin.ddphin.collector.mybatis;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SQLExecutor
 *
 * @Date     2019/7/8 下午2:55
 * @Author   ddphin
 */
public class SQLExecutor {
    private final SQLExecutor.MSUtils msUtils;
    private final SqlSession sqlSession;

    /**
     * getOne
     *
     * @Date     2019/7/8 下午2:59
     * @Author   ddphin
     *
     * @param    list
     * @return   T
     */
    private <T> T getOne(List<T> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    /**
     * SQLExecutor
     *
     * @Date     2019/7/8 下午2:59
     * @Author   ddphin
     *
     * @param    sqlSession
     * @return
     */
    public SQLExecutor(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.msUtils = new SQLExecutor.MSUtils(sqlSession.getConfiguration());
    }

    /**
     * selectOne
     *
     * @Date     2019/7/8 下午2:59
     * @Author   ddphin
     *
     * @param    sql
     * @return   java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String, Object> selectOne(String sql) {
        List<Map<String, Object>> list = selectList(sql);
        return getOne(list);
    }

    /**
     * selectOne
     *
     * @Date     2019/7/8 下午3:00
     * @Author   ddphin
     *
     * @param    sql
     * @param    value
     * @return   java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String, Object> selectOne(String sql, Object value) {
        List<Map<String, Object>> list = selectList(sql, value);
        return getOne(list);
    }

    /**
     * selectOne
     *
     * @Date     2019/7/8 下午3:00
     * @Author   ddphin
     *
     * @param    sql
     * @param    resultType
     * @return   T
     */
    public <T> T selectOne(String sql, Class<T> resultType) {
        List<T> list = selectList(sql, resultType);
        return getOne(list);
    }

    /**
     * selectOne
     *
     * @Date     2019/7/8 下午3:00
     * @Author   ddphin
     *
     * @param    sql
     * @param    value
     * @param    resultType
     * @return   T
     */
    public <T> T selectOne(String sql, Object value, Class<T> resultType) {
        List<T> list = selectList(sql, value, resultType);
        return getOne(list);
    }

    /**
     * selectList
     *
     * @Date     2019/7/8 下午3:00
     * @Author   ddphin
     *
     * @param    sql
     * @return   java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public List<Map<String, Object>> selectList(String sql) {
        String msId = msUtils.select(sql);
        return sqlSession.selectList(msId);
    }

    /**
     * selectList
     *
     * @Date     2019/7/8 下午3:00
     * @Author   ddphin
     *
     * @param    sql
     * @param    value
     * @return   java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public List<Map<String, Object>> selectList(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.selectDynamic(sql, parameterType);
        return sqlSession.selectList(msId, value);
    }

    /**
     * selectList
     *
     * @Date     2019/7/8 下午3:00
     * @Author   ddphin
     *
     * @param    sql
     * @param    resultType
     * @return   java.util.List<T>
     */
    public <T> List<T> selectList(String sql, Class<T> resultType) {
        String msId;
        if (resultType == null) {
            msId = msUtils.select(sql);
        } else {
            msId = msUtils.select(sql, resultType);
        }
        return sqlSession.selectList(msId);
    }

    /**
     * selectList
     *
     * @Date     2019/7/8 下午3:01
     * @Author   ddphin
     *
     * @param    sql
     * @param    value
     * @param    resultType
     * @return   java.util.List<T>
     */
    public <T> List<T> selectList(String sql, Object value, Class<T> resultType) {
        String msId;
        Class<?> parameterType = value != null ? value.getClass() : null;
        if (resultType == null) {
            msId = msUtils.selectDynamic(sql, parameterType);
        } else {
            msId = msUtils.selectDynamic(sql, parameterType, resultType);
        }
        return sqlSession.selectList(msId, value);
    }

    /**
     * insert
     *
     * @Date     2019/7/8 下午3:01
     * @Author   ddphin
     *
     * @param    sql
     * @return   int
     */
    public int insert(String sql) {
        String msId = msUtils.insert(sql);
        return sqlSession.insert(msId);
    }

    /**
     * insert
     *
     * @Date     2019/7/8 下午3:01
     * @Author   ddphin
     *
     * @param    sql
     * @param    value
     * @return   int
     */
    public int insert(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.insertDynamic(sql, parameterType);
        return sqlSession.insert(msId, value);
    }

    /**
     * update
     *
     * @Date     2019/7/8 下午3:01
     * @Author   ddphin
     *
     * @param    sql
     * @return   int
     */
    public int update(String sql) {
        String msId = msUtils.update(sql);
        return sqlSession.update(msId);
    }

    /**
     * update
     *
     * @Date     2019/7/8 下午3:01
     * @Author   ddphin
     *
     * @param    sql
     * @param    value
     * @return   int
     */
    public int update(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.updateDynamic(sql, parameterType);
        return sqlSession.update(msId, value);
    }

    /**
     * delete
     *
     * @Date     2019/7/8 下午3:02
     * @Author   ddphin
     *
     * @param    sql
     * @return   int
     */
    public int delete(String sql) {
        String msId = msUtils.delete(sql);
        return sqlSession.delete(msId);
    }

    /**
     * delete
     *
     * @Date     2019/7/8 下午3:02
     * @Author   ddphin
     *
     * @param    sql
     * @param    value
     * @return   int
     */
    public int delete(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = msUtils.deleteDynamic(sql, parameterType);
        return sqlSession.delete(msId, value);
    }

    private class MSUtils {
        private Configuration configuration;
        private LanguageDriver languageDriver;

        private MSUtils(Configuration configuration) {
            this.configuration = configuration;
            languageDriver = configuration.getDefaultScriptingLanguageInstance();
        }

        /**
         * newMsId
         *
         * @Date     2019/7/8 下午3:02
         * @Author   ddphin
         *
         * @param    sql
         * @param    sqlCommandType
         * @return   java.lang.String
         */
        private String newMsId(String sql, SqlCommandType sqlCommandType) {
            StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
            msIdBuilder.append(".").append(sql.hashCode());
            return msIdBuilder.toString();
        }

        /**
         * hasMappedStatement
         *
         * @Date     2019/7/8 下午3:02
         * @Author   ddphin
         *
         * @param    msId
         * @return   boolean
         */
        private boolean hasMappedStatement(String msId) {
            return configuration.hasStatement(msId, false);
        }

        /**
         * newSelectMappedStatement
         *
         * @Date     2019/7/8 下午3:02
         * @Author   ddphin
         *
         * @param    msId
         * @param    sqlSource
         * @param    resultType
         * @return   void
         */
        private void newSelectMappedStatement(String msId, SqlSource sqlSource, final Class<?> resultType) {
            MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, SqlCommandType.SELECT)
                    .resultMaps(new ArrayList<ResultMap>() {
                        {
                            add(new ResultMap.Builder(configuration, "defaultResultMap", resultType, new ArrayList<>(0)).build());
                        }
                    })
                    .build();
            //缓存
            configuration.addMappedStatement(ms);
        }

        /**
         * newUpdateMappedStatement
         *
         * @Date     2019/7/8 下午3:03
         * @Author   ddphin
         *
         * @param    msId
         * @param    sqlSource
         * @param    sqlCommandType
         * @return   void
         */
        private void newUpdateMappedStatement(String msId, SqlSource sqlSource, SqlCommandType sqlCommandType) {
            MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, sqlCommandType)
                    .resultMaps(new ArrayList<ResultMap>() {
                        {
                            add(new ResultMap.Builder(configuration, "defaultResultMap", int.class, new ArrayList<>(0)).build());
                        }
                    })
                    .build();
            //缓存
            configuration.addMappedStatement(ms);
        }

        private String select(String sql) {
            String msId = newMsId(sql, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newSelectMappedStatement(msId, sqlSource, Map.class);
            return msId;
        }

        private String selectDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newSelectMappedStatement(msId, sqlSource, Map.class);
            return msId;
        }

        private String select(String sql, Class<?> resultType) {
            String msId = newMsId(resultType + sql, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newSelectMappedStatement(msId, sqlSource, resultType);
            return msId;
        }

        private String selectDynamic(String sql, Class<?> parameterType, Class<?> resultType) {
            String msId = newMsId(resultType + sql + parameterType, SqlCommandType.SELECT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newSelectMappedStatement(msId, sqlSource, resultType);
            return msId;
        }

        private String insert(String sql) {
            String msId = newMsId(sql, SqlCommandType.INSERT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
            return msId;
        }

        private String insertDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.INSERT);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
            return msId;
        }

        private String update(String sql) {
            String msId = newMsId(sql, SqlCommandType.UPDATE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
            return msId;
        }

        private String updateDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.UPDATE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
            return msId;
        }

        private String delete(String sql) {
            String msId = newMsId(sql, SqlCommandType.DELETE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
            return msId;
        }

        private String deleteDynamic(String sql, Class<?> parameterType) {
            String msId = newMsId(sql + parameterType, SqlCommandType.DELETE);
            if (hasMappedStatement(msId)) {
                return msId;
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
            newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
            return msId;
        }
    }
}
