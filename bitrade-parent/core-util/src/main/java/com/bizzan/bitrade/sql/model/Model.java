package com.bizzan.bitrade.sql.model;


import com.bizzan.bitrade.lang.Convert;
import com.bizzan.bitrade.sql.DB;
import com.bizzan.bitrade.util.BeanMapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private String tableName;

    private String conditionClause;

    private Object[] params;

    private String primaryKey = "id";

    private String action = "";

    private String fields = "*";

    private String orderClause = "";

    private String limitClause = "";

    private String groupByClause = "";

    private Map<String, Object> row = new HashMap<String, Object>();

    private Map<String, Object> dirtyData = new HashMap<String, Object>();

    public Model(String tableName) {
        this.tableName = tableName;
    }

    public Object get(String field) {
        return this.row.get(field);
    }

    public Object get(int index) {
        int i = 0;
        for (Map.Entry<String, Object> entry : this.row.entrySet()) {
            if (index == i)
                return entry.getValue();
            i++;
        }
        return null;
    }

    public Model set(String field, Object value) {
        this.dirtyData.put(field, String.valueOf(value));
        return this;
    }

    public long delete() {
        this.action = "DELETE";
        long ret = -1L;
        try {
            String cmd = buildSQL();
            ret = DB.exec(cmd, this.params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int count() {
        this.action = "COUNT";
        int ret = -1;
        try {
            String cmd = buildSQL();
            List<Map<String, String>> list = DB.query(cmd, this.params);
            if (list.size() == 0)
                return 0;
            ret = Convert.strToInt((String) ((Map) list.get(0)).get("count"), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public long delete(long key) {
        where(String.valueOf(this.primaryKey) + " = ?", new Object[]{Long.valueOf(key)});
        return delete();
    }

    public Model create(Map<String, Object> map) {
        this.dirtyData = map;
        return this;
    }

    public Model create(Object bean) throws IllegalArgumentException, IllegalAccessException {
        create(BeanMapUtils.bean2Map(bean));
        return this;
    }

    public long insert() throws SQLException {
        this.action = "INSERT INTO";
        long ret = -1L;
        try {
            String cmd = buildSQL();
            ret = DB.exec(cmd, this.params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return ret;
    }

    public long update() throws SQLException {
        this.action = "UPDATE";
        long ret = -1L;
        try {
            String cmd = buildSQL();
            ret = DB.exec(cmd, this.params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return ret;
    }

    public long setField(String field, Object value) throws SQLException {
        set(field, value);
        return update();
    }

    public long setInc(String field, Object value) throws SQLException {
        this.action = "UPDATE_INC";
        long ret = -1L;
        try {
            set(field, value);
            String cmd = buildSQL();
            ret = DB.exec(cmd, this.params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return ret;
    }

    public long setDec(String field, Object value) throws SQLException {
        this.action = "UPDATE_DEC";
        long ret = -1L;
        try {
            set(field, value);
            String cmd = buildSQL();
            ret = DB.exec(cmd, this.params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return ret;
    }

    public long update(long key) throws SQLException {
        where(String.valueOf(this.primaryKey) + " = ?", new Object[]{Long.valueOf(key)});
        return update();
    }

    public List<Map<String, String>> select() throws Exception {
        this.action = "SELECT";
        List<Map<String, String>> list = null;
        try {
            String cmd = buildSQL();
            if (this.params == null) {
                list = DB.query(cmd);
            } else {
                list = DB.query(cmd, this.params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    public <T> List<T> select(Class<T> beanClass) throws Exception {
        List<Map<String, String>> list = select();
        List<T> listBean = new ArrayList<T>();
        for (Map<String, String> map : list) {
            T bean = beanClass.getDeclaredConstructor().newInstance();
            BeanMapUtils.map2Bean(map, bean);
            listBean.add(bean);
        }
        return listBean;
    }

    public Map<String, String> find() throws Exception {
        this.action = "SELECT";
        Map<String, String> map = null;
        try {
            String cmd = buildSQL();
            List<Map<String, String>> list = null;
            if (this.params == null || this.params.length == 0) {
                list = DB.query(cmd);
            } else {
                list = DB.query(cmd, this.params);
            }
            if (list.size() > 0)
                map = list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return map;
    }

    public Map<String, String> find(long key) throws Exception {
        where(String.valueOf(this.primaryKey) + " = ?", new Object[]{Long.valueOf(key)});
        return find();
    }

    public <T> T find(Class<T> beanClass) throws Exception {
        Map<String, String> map = find();
        if (map != null) {
            T bean =beanClass.getDeclaredConstructor().newInstance();
            BeanMapUtils.map2Bean(map, bean);
            return bean;
        }
        return null;
    }

    public Model where(String clause, Object... params) {
        this.conditionClause = clause;
        if (params != null && params.length > 0)
            this.params = params;
        return this;
    }

    public Model where(String clause) {
        this.conditionClause = clause;
        return this;
    }

    public Model limit(int offset, int limit) {
        this.limitClause = "limit " + limit + " offset " + offset;
        return this;
    }

    public Model limit(int limit) {
        this.limitClause = "limit " + limit;
        return this;
    }

    public Model field(String fields) {
        this.fields = fields;
        return this;
    }

    public Model group(String clause) {
        this.groupByClause = "group by " + clause;
        return this;
    }

    public Model order(String... clause) {
        String clauseJoined = StringUtils.join((Object[]) clause, ",");
        if (StringUtils.isNotBlank(clauseJoined))
            this.orderClause = "order by " + clauseJoined;
        return this;
    }

    private String buildSQL() throws SQLException {
        if (this.action == null || this.action.equals(""))
            throw new SQLException("no action defined for model");
        String mdl = this.action;
        if (this.action.equals("UPDATE_INC") || this.action.equals("UPDATE_DEC"))
            mdl = "UPDATE";
        StringBuilder sb = new StringBuilder(mdl);
        if (this.action.equals("SELECT")) {
            sb.append(" " + this.fields + " from " + this.tableName);
            if (StringUtils.isNotBlank(this.conditionClause))
                sb.append(" where " + this.conditionClause);
            if (StringUtils.isNotBlank(this.groupByClause))
                sb.append(" " + this.groupByClause);
            if (StringUtils.isNotBlank(this.orderClause))
                sb.append(" " + this.orderClause);
            if (StringUtils.isNotBlank(this.limitClause))
                sb.append(" " + this.limitClause);
        } else if (this.action.equals("INSERT INTO")) {
            sb.append(" " + this.tableName);
            List<String> columnList = new ArrayList<String>();
            List<Object> valueList = new ArrayList();
            List<String> signList = new ArrayList<String>();
            if (this.dirtyData.isEmpty())
                throw new SQLException("row map for insert is empty");
            for (Map.Entry<String, Object> entry : this.dirtyData.entrySet()) {
                columnList.add("`" + (String) entry.getKey() + "`");
                signList.add("?");
                valueList.add(entry.getValue());
            }
            sb.append("(" + StringUtils.join(columnList, ',') + ")");
            sb.append(" values(" + StringUtils.join(signList, ',') + ")");
            this.params = valueList.toArray();
        } else if (this.action.equals("UPDATE")) {
            sb.append(" " + this.tableName + " set ");
            if (this.dirtyData.isEmpty())
                throw new SQLException("no data to update");
            List<String> columnList = new ArrayList<String>();
            List<Object> valueList = new ArrayList();
            for (Map.Entry<String, Object> entry : this.dirtyData.entrySet()) {
                columnList.add("`" + (String) entry.getKey() + "` = ?");
                valueList.add(entry.getValue());
            }
            sb.append(StringUtils.join(columnList, ','));
            if (StringUtils.isNotBlank(this.conditionClause))
                sb.append(" where " + this.conditionClause);
            if (this.params == null || this.params.length == 0) {
                this.params = valueList.toArray();
            } else {
                this.params = ArrayUtils.addAll(valueList.toArray(), this.params);
            }
        } else if (this.action.equals("UPDATE_INC") || this.action.equals("UPDATE_DEC")) {
            sb.append(" " + this.tableName + " set ");
            char op = this.action.equals("UPDATE_INC") ? '+' : '-';
            List<String> columnList = new ArrayList<String>();
            List<Object> valueList = new ArrayList();
            for (Map.Entry<String, Object> entry : this.dirtyData.entrySet()) {
                columnList.add("`" + (String) entry.getKey() + "` = `" + (String) entry.getKey() + "` " + op + " ?");
                valueList.add(entry.getValue());
            }
            sb.append(StringUtils.join(columnList, ','));
            if (StringUtils.isNotBlank(this.conditionClause))
                sb.append(" where " + this.conditionClause);
            if (this.params == null || this.params.length == 0) {
                this.params = valueList.toArray();
            } else {
                this.params = ArrayUtils.addAll(valueList.toArray(), this.params);
            }
        } else if (this.action.equals("DELETE")) {
            sb.append(" from " + this.tableName);
            if (StringUtils.isNotBlank(this.conditionClause))
                sb.append(" where " + this.conditionClause);
        } else if (this.action.equals("COUNT")) {
            sb = new StringBuilder("SELECT ifnull(count(1),0) as count from " + this.tableName);
            if (StringUtils.isNotBlank(this.conditionClause))
                sb.append(" where " + this.conditionClause);
            if (StringUtils.isNotBlank(this.groupByClause))
                sb.append(" " + this.groupByClause);
        }
        return sb.toString();
    }
}