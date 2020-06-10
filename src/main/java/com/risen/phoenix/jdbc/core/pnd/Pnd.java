package com.risen.phoenix.jdbc.core.pnd;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxId;
import com.risen.phoenix.jdbc.util.CaseUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

public class Pnd<T> implements ISqlExp{

    private List<ISqlExp> cndExps;
    private List<ISqlExp> orderBys;
    private Set<String> orderBySet;
    private boolean where;
    private T t;

    public Pnd(T t) {
        this.t = t;
        this.setCndExps((List)(new ArrayList(4)));
    }

    public Pnd(List<ISqlExp> cndExps, T t) {
        this.t = t;
        this.where = false;
        this.setCndExps(cndExps);
    }

    public Pnd(ISqlExp cndExp, T t) {
        this(t);
        if (cndExp instanceof Pnd) {
            Pnd cnd = (Pnd)cndExp;
            this.cndExps = cnd.cndExps;
            this.orderBys = cnd.orderBys;
            this.orderBySet = cnd.orderBySet;
            this.where = cnd.where;
        } else {
            this.append(cndExp);
        }

    }

    public Pnd append(ISqlExp cndExp) {
        if (cndExp instanceof Pnd) {
            Pnd cnd = (Pnd)cndExp;
            this.getCndExps().addAll(cnd.getCndExps());
            if (cnd.hasOrderBy()) {
                this.getOrderBys().addAll(cnd.getOrderBys());
                this.getOrderBySet().addAll(cnd.getOrderBySet());
                cnd.getOrderBys().clear();
                cnd.getOrderBySet().clear();
            }
        } else if (!cndExp.isEmpty()) {
            this.getCndExps().add(cndExp);
        }

        return this;
    }

    public Pnd append(ISqlExp... cndExps) {
        ISqlExp[] var5 = cndExps;
        int var4 = cndExps.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            ISqlExp cndExp = var5[var3];
            this.append(cndExp);
        }

        return this;
    }

    public Pnd append(String snippet) {
        return this.append((ISqlExp)(new SimpleSqlExp(snippet)));
    }

    public Pnd append(String... snippets) {
        return this.append((ISqlExp)(new SimpleSqlExp(snippets)));
    }

    public Pnd andAppend(String snippet) {
        if (!this.isEmpty()) {
            this.append((ISqlExp)OP.AND);
        }

        return this.append((ISqlExp)(new SimpleSqlExp(snippet)));
    }

    public Pnd AND(ISqlExp cndExp) {
        if (!this.isEmpty() && !cndExp.isEmpty()) {
            this.append((ISqlExp)OP.AND);
        }

        return this.append(cndExp);
    }

    public Pnd AND(String colName, OP op, String propName) {
        if (!StringUtils.hasText(colName)) {
            colName = propName;
            if (propName.lastIndexOf(".") > 0) {
                colName = propName.substring(propName.lastIndexOf(".") + 1);
            }

            colName = PhoenixUtils.mapperColName(colName);
        }

        propName = result(colName);
        if (!this.isEmpty()) {
            this.append((ISqlExp)OP.AND);
        }

        return this.append(colName, op.getSql(), propName);
    }

    public Pnd andEquals(String propName) {
        return this.andEquals((String)null, propName);
    }

    public Pnd andEquals(String colName, String propName) {
        return this.AND(colName, OP.EQ, propName);
    }

    public Pnd andNotEquals(String propName) {
        return this.andNotEquals((String)null, propName);
    }

    public Pnd andNotEquals(String colName, String propName) {
        return this.AND(colName, OP.NEQ, propName);
    }

    public Pnd andGT(String propName) {
        return this.andGT((String)null, propName);
    }

    public Pnd andGT(String colName, String propName) {
        return this.AND(colName, OP.GT, propName);
    }

    public Pnd andGTE(String propName) {
        return this.andGTE((String)null, propName);
    }

    public Pnd andGTE(String colName, String propName) {
        return this.AND(colName, OP.GTE, propName);
    }

    public Pnd andLT(String propName) {
        return this.andLT((String)null, propName);
    }

    public Pnd andLT(String colName, String propName) {
        return this.AND(colName, OP.LT, propName);
    }

    public Pnd andLTE(String propName) {
        return this.andLTE((String)null, propName);
    }

    public Pnd andLTE(String colName, String propName) {
        return this.AND(colName, OP.LTE, propName);
    }

    public Pnd andExpandOR(String propName, String listName, Collection<?> list) {
        return this.andExpandOR((String)null, propName, listName, list);
    }

    public Pnd andExpandOR(String colName, String propName, String listName, Collection<?> list) {
        colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
        propName = expandOR(colName, OP.EQ, listName, list);
        if (!this.getCndExps().isEmpty()) {
            this.append((ISqlExp)OP.AND);
        }

        return list.size() == 0 ? this.append("1=0") : this.append(propName);
    }

    public Pnd andIn(String propName, String listName, Collection<?> list) {
        return this.andIn((String)null, propName, listName, list);
    }

    public Pnd andIn(String colName, String propName, String listName, Collection<?> list) {
        colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
        propName = expandIn(listName, list);
        if (!this.getCndExps().isEmpty()) {
            this.append((ISqlExp)OP.AND);
        }

        return list.size() == 0 ? this.append("1=0") : this.append(colName, OP.IN.toString(), propName);
    }

    public Pnd andNotIn(String propName, String listName, Collection<?> list) {
        return this.andNotIn((String)null, propName, listName, list);
    }

    public Pnd andNotIn(String colName, String propName, String listName, Collection<?> list) {
        colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
        propName = expandIn(listName, list);
        if (!this.getCndExps().isEmpty()) {
            this.append((ISqlExp)OP.AND);
        }

        return list.size() == 0 ? this.append("1=0") : this.append(colName, OP.NIN.toString(), propName);
    }

    public Pnd andLike(String propName) {
        return this.andLike((String)null, propName);
    }

    public Pnd andLike(String colName, String propName) {
        return this.AND(colName, OP.LIKE, propName);
    }

    public Pnd andLike(String propName, boolean useUpper) {
        return this.andLike((String)null, propName, useUpper);
    }

    public Pnd andLike(String colName, String propName, boolean useUpper) {
        colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
        if (useUpper) {
            colName = "UPPER(" + colName + ")";
        } else {
            colName = "LOWER(" + colName + ")";
        }

        return this.AND(colName, OP.LIKE, propName);
    }

    public Pnd andNotLike(String propName) {
        return this.andNotLike((String)null, propName);
    }

    public Pnd andNotLike(String colName, String propName) {
        return this.AND(colName, OP.NLIKE, propName);
    }

    public Pnd andNotLike(String propName, boolean useUpper) {
        return this.andNotLike((String)null, propName, useUpper);
    }

    public Pnd andNotLike(String colName, String propName, boolean useUpper) {
        colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
        colName = "UPPER(" + colName + ")";
        return this.AND(colName, OP.NLIKE, propName);
    }

    public Pnd andNull(String propName) {
        return this.AND(new SimpleSqlExp(PhoenixUtils.mapperColName(propName) + " IS NULL"));
    }

    /** @deprecated */
    @Deprecated
    public Pnd andIsNull(String colName) {
        return this.andColNull(colName);
    }

    public Pnd andColNull(String colName) {
        return this.AND(new SimpleSqlExp(colName + " IS NULL"));
    }

    public Pnd andNotNull(String propName) {
        return this.AND(new SimpleSqlExp(PhoenixUtils.mapperColName(propName) + " IS NOT NULL"));
    }

    /** @deprecated */
    @Deprecated
    public Pnd andIsNotNull(String colName) {
        return this.andColNotNull(colName);
    }

    public Pnd andColNotNull(String colName) {
        return this.AND(new SimpleSqlExp(colName + " IS NOT NULL"));
    }

    public Pnd OR(String colName, OP op, String propName) {
        if (!StringUtils.hasText(colName)) {
            colName = propName;
            if (propName.lastIndexOf(".") > 0) {
                colName = propName.substring(propName.lastIndexOf(".") + 1);
            }

            colName = PhoenixUtils.mapperColName(colName);
        }

        propName = result(colName);
        if (!this.getCndExps().isEmpty()) {
            this.append((ISqlExp)OP.OR);
        }

        return this.append(colName, op.getSql(), propName);
    }

    public Pnd OR(ISqlExp cndExp) {
        if (!this.getCndExps().isEmpty()) {
            this.append((ISqlExp)OP.OR);
        }

        return this.append(cndExp);
    }

    public Pnd orEquals(String propName) {
        return this.orEquals((String)null, propName);
    }

    public Pnd orEquals(String colName, String propName) {
        return this.OR(colName, OP.EQ, propName);
    }

    public Pnd orNotEquals(String propName) {
        return this.orNotEquals((String)null, propName);
    }

    public Pnd orNotEquals(String colName, String propName) {
        return this.OR(colName, OP.NEQ, propName);
    }

    public Pnd orGT(String propName) {
        return this.orGT((String)null, propName);
    }

    public Pnd orGT(String colName, String propName) {
        return this.OR(colName, OP.GT, propName);
    }

    public Pnd orGTE(String propName) {
        return this.orGTE((String)null, propName);
    }

    public Pnd orGTE(String colName, String propName) {
        return this.OR(colName, OP.GTE, propName);
    }

    public Pnd orLT(String propName) {
        return this.orLT((String)null, propName);
    }

    public Pnd orLT(String colName, String propName) {
        return this.OR(colName, OP.LT, propName);
    }

    public Pnd orLTE(String propName) {
        return this.orLTE((String)null, propName);
    }

    public Pnd orLTE(String colName, String propName) {
        return this.OR(colName, OP.LTE, propName);
    }

    public Pnd orExpandOR(String propName, String listName, Collection<?> list) {
        return this.orExpandOR((String)null, propName, listName, list);
    }

    public Pnd orExpandOR(String colName, String propName, String listName, Collection<?> list) {
        if (list.size() == 0) {
            return this;
        } else {
            colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
            propName = expandOR(colName, OP.EQ, listName, list);
            if (!this.getCndExps().isEmpty()) {
                this.append((ISqlExp)OP.OR);
            }

            return this.append(propName);
        }
    }

    public Pnd orIn(String propName, String listName, Collection<?> list) {
        return this.orIn((String)null, propName, listName, list);
    }

    public Pnd orIn(String colName, String propName, String listName, Collection<?> list) {
        colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
        propName = expandIn(listName, list);
        if (list.size() == 0) {
            return this;
        } else {
            if (!this.getCndExps().isEmpty()) {
                this.append((ISqlExp)OP.OR);
            }

            return this.append(colName, OP.IN.toString(), propName);
        }
    }

    public Pnd orNotIn(String propName, String listName, Collection<?> list) {
        return this.orNotIn((String)null, propName, listName, list);
    }

    public Pnd orNotIn(String colName, String propName, String listName, Collection<?> list) {
        colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
        propName = expandIn(listName, list);
        if (!this.getCndExps().isEmpty()) {
            this.append((ISqlExp)OP.OR);
        }

        return this.append(colName, OP.NIN.toString(), propName);
    }

    public Pnd orLike(String propName) {
        return this.orLike((String)null, propName);
    }

    public Pnd orLike(String colName, String propName) {
        return this.OR(colName, OP.LIKE, propName);
    }

    public Pnd orLike(String propName, boolean useUpper) {
        return this.orLike((String)null, propName, useUpper);
    }

    public Pnd orLike(String colName, String propName, boolean useUpper) {
        colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
        colName = "UPPER(" + colName + ")";
        return this.OR(colName, OP.LIKE, propName);
    }

    public Pnd orNotLike(String propName) {
        return this.orNotLike((String)null, propName);
    }

    public Pnd orNotLike(String colName, String propName) {
        return this.OR(colName, OP.NLIKE, propName);
    }

    public Pnd orNotLike(String propName, boolean useUpper) {
        return this.orNotLike((String)null, propName, useUpper);
    }

    public Pnd orNotLike(String colName, String propName, boolean useUpper) {
        colName = (String)PhoenixUtils.getValue(colName, PhoenixUtils.mapperColName(propName));
        colName = "UPPER(" + colName + ")";
        return this.OR(colName, OP.NLIKE, propName);
    }

    public Pnd orNull(String propName) {
        return this.OR(new SimpleSqlExp(PhoenixUtils.mapperColName(propName) + " IS NULL"));
    }

    /** @deprecated */
    @Deprecated
    public Pnd orIsNull(String colName) {
        return this.orColNull(colName);
    }

    public Pnd orColNull(String colName) {
        return this.OR(new SimpleSqlExp(colName + " IS NULL"));
    }

    public Pnd orNotNull(String propName) {
        return this.OR(new SimpleSqlExp(PhoenixUtils.mapperColName(propName) + " IS NOT NULL"));
    }

    /** @deprecated */
    @Deprecated
    public Pnd orIsNotNull(String colName) {
        return this.orColNotNull(colName);
    }

    public Pnd orColNotNull(String colName) {
        return this.OR(new SimpleSqlExp(colName + " IS NOT NULL"));
    }

    public Pnd group() {
        if (!this.isEmpty() && !OP.LP.equals(this.getCndExps().get(0))) {
            this.getCndExps().add(0, OP.LP);
            this.getCndExps().add(OP.RP);
        }

        return this;
    }

    public Pnd groupAnd(ISqlExp cndExp) {
        this.group();
        return this.AND(cndExp);
    }

    public Pnd NOT() {
        if (!this.isEmpty()) {
            this.getCndExps().add(0, new SimpleSqlExp(OP.NOT.toString()));
        }

        return this;
    }

    public Pnd orderBy(String propName) {
        return this.orderBy(propName, ORDER.ASC);
    }

    public Pnd orderBy(String propName, ORDER order) {
        String colName = PhoenixUtils.mapperColName(propName);
        this.orderBy(colName, order.name());
        return this;
    }

    public Pnd orderBy(String colName, String order) {
        if (!this.getOrderBySet().contains(colName)) {
            this.getOrderBys().add(new SimpleSqlExp(new String[]{colName, order}));
            this.getOrderBySet().add(colName);
        }

        return this;
    }

    public Pnd resetCnd() {
        this.getCndExps().clear();
        return this;
    }

    public Pnd resetOrderBy() {
        this.setOrderBys((List)null);
        this.orderBySet = null;
        return this;
    }

    public Pnd reset() {
        this.resetOrderBy();
        this.resetCnd();
        return this;
    }

    public static String expandIn(String listName, Collection<?> list) {
        StringBuilder builder = new StringBuilder("(");
        Iterator<?> iterator = list.iterator();

        for(int i = 0; iterator.hasNext(); builder.append("}")) {
            Object item = iterator.next();
            if (i != 0) {
                builder.append(",");
            }

            builder.append("#{").append(listName).append("[").append(i++).append("]");
            if (item == null) {
                builder.append(",javaType=").append("java.lang.Object");
            } else {
                builder.append(",javaType=").append(item.getClass().getName());
            }
        }

        builder.append(")");
        return builder.toString();
    }

    public static String expandOR(String colName, OP op, String listName, Collection<?> list) {
        return expand(colName, op, listName, list, OP.OR);
    }

    public static String expand(String colName, OP op, String listName, Collection<?> list, OP expandOp) {
        StringBuilder builder = new StringBuilder("(");
        int index = 0;

        for(Iterator var8 = list.iterator(); var8.hasNext(); builder.append("}")) {
            Object item = var8.next();
            if (index != 0) {
                builder.append(" ").append(expandOp).append(" ");
            }

            builder.append(colName).append(" ").append(op).append(" ");
            builder.append("#{").append(listName).append("[").append(index++).append("]");
            if (item == null) {
                builder.append(",javaType=").append("java.lang.Object");
            } else {
                builder.append(",javaType=").append(item.getClass().getName());
            }
        }

        builder.append(")");
        return builder.toString();
    }

    public boolean isWhere() {
        return this.where;
    }

    public boolean hasOrderBy() {
        return this.getOrderBys() != null && !this.getOrderBys().isEmpty();
    }

    public boolean hasOrderBy(String prop) {
        if (this.orderBySet == null) {
            return false;
        } else {
            String colName = PhoenixUtils.mapperColName(prop);
            return this.getOrderBySet().contains(colName) || this.getOrderBySet().contains(prop);
        }
    }

    public void setWhere(boolean where) {
        this.where = where;
    }

    @Override
    public boolean isEmpty() {
        List<ISqlExp> cndExps = this.getCndExps();
        Iterator var3 = cndExps.iterator();

        while(var3.hasNext()) {
            ISqlExp cndExp = (ISqlExp)var3.next();
            if (!cndExp.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getSql() {
        List<ISqlExp> cndExps = this.getCndExps();
        StringBuilder builder = new StringBuilder();
        if (!this.isEmpty() && this.isWhere()) {
            builder.append(" WHERE ");
        }

        int i;
        for(i = 0; i < cndExps.size(); ++i) {
            if (i != 0) {
                builder.append(" ");
            }

            builder.append(((ISqlExp)cndExps.get(i)).getSql());
        }

        if (!this.getOrderBys().isEmpty()) {
            builder.append(" ORDER BY ");
        }

        for(i = 0; i < this.getOrderBys().size(); ++i) {
            if (i != 0) {
                builder.append(",");
            }

            builder.append(((ISqlExp)this.getOrderBys().get(i)).getSql());
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        try {
            return this.getSql();
        } catch (Exception var2) {
            throw new RuntimeException("Pnd.toString异常:", var2);
        }
    }

    /*public Pnd getCopy() {
        Pnd cnd = new Pnd(this.getCndExps());
        if (this.hasOrderBy()) {
            cnd.setOrderBys(new ArrayList(this.getOrderBys()));
            cnd.orderBySet = new HashSet(this.orderBySet);
        }

        return cnd;
    }*/

    public Set<String> getOrderBySet() {
        return this.orderBySet == null ? (this.orderBySet = new HashSet()) : this.orderBySet;
    }

    public List<ISqlExp> getOrderBys() {
        return this.orderBys == null ? (this.orderBys = new ArrayList()) : this.orderBys;
    }

    public void setOrderBys(List<ISqlExp> orderBys) {
        this.orderBys = orderBys;
    }

    public void setCndExps(List<ISqlExp> cndExps) {
        this.cndExps = cndExps;
    }

    public List<ISqlExp> getCndExps() {
        return this.cndExps;
    }

    private String result(String colName){
        colName = colName.toUpperCase();
        Class<?> aClass = t.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            boolean bar1 = field.isAnnotationPresent(PhxId.class);
            boolean bar2 = field.isAnnotationPresent(PhxField.class);
            if (bar1 || bar2) {
                String fina2 = field.getName();
                if (colName.equals(fina2.toUpperCase()) || colName.equals(CaseUtils.lowerCamel(fina2).toUpperCase())) {
                    field.setAccessible(true);
                    String s = null;
                    try {
                        return CaseUtils.commaNorm(field.getType().getSimpleName(), field.get(t), "");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return null;
    }

}