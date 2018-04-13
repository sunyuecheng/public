package com.sct.jdbctest.dao.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlDataMapping {
    
    protected static Logger logger = Logger.getLogger(SqlDataMapping.class);
    
    public <T> T queryForObject(JdbcTemplate jdbcTemplate, final String sql, final Object[] args, final Class<T> cls) {
        if(jdbcTemplate == null || sql == null || args ==null) {
            return null;
        }
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args);
            if (list != null && list.size() > 0) {
                Map<String, Object> obj = list.get(0);
                if (obj != null) {
                    T retObj = this.mapObject(obj, cls);
                    return retObj;
                }
            }
        } catch (Exception e) {
            logger.error("Query for object error,error info(" + e.getMessage() + ")");
        }
        return null;
    }
    
    protected <T> List<T> queryForObjectList(JdbcTemplate jdbcTemplate, final String sql, final Object[] args, final Class<T> cls) {
        if(jdbcTemplate == null || sql == null || args ==null) {
            return null;
        }
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args);
            List<T> retList = new ArrayList<T>();

            for (Map<String, Object> obj : list) {
                T data = this.mapObject(obj, cls);
                retList.add(data);
            }
            return retList;
        } catch (Exception e) {
            logger.error("Query for object list error,error info(" + e.getMessage() + ")");
        }
        return null;
    }
    
    public <T> List<T> queryForPageObjectList(JdbcTemplate jdbcTemplate, final String sql,final Object[] args,
            final String countSql,final Object[] countArgs, final int pageIndex, final int pageSize, final Class<T> cls) {
        if(jdbcTemplate == null || sql == null || args ==null || countSql == null
                || pageIndex < 0 || pageSize<=0 ) {
            return null;
        }
        int totalResult= 0;
        if (countArgs == null) {
            totalResult = jdbcTemplate.queryForObject(countSql, Integer.class);
        } else {
            totalResult = jdbcTemplate.queryForObject(countSql, countArgs, Integer.class);
        }
                
        int startIndex = pageIndex * pageSize;
        int endIndex = (pageIndex + 1) * pageSize - 1;
        
        //int startIndex = pageIndex * pageSize + 1;
        //int endIndex = (pageIndex + 1) * pageSize ;

        //String pageSql = "SELECT * FROM ( SELECT A.*, ROWNUM RN FROM (" + sql + ") A WHERE ROWNUM <= " + endIndex + " ) WHERE RN >= " + startIndex;       
        String pageSql = sql + " limit " + startIndex + "," + endIndex;       
        List<Map<String, Object>> list = jdbcTemplate.queryForList(pageSql, args);

        List<T> retList = new ArrayList<T>();
        try {
            for (Map<String, Object> obj : list) {
                T data = this.mapObject(obj, cls);
                retList.add(data);
            }
            return retList;
        } catch (Exception e) {
            logger.error("Query for page object list error,error info(" + e.getMessage() + ")");
        }
        return null;
    }
    
    private <T> T mapObject(final Map<String, Object> data, final Class<T> cls) throws Exception {
        Field[] fields = cls.getDeclaredFields();
        T retObj = cls.newInstance();
        for (Field field : fields) {     
            if(field.isAnnotationPresent(Column.class)){
                Column fieldName = (Column) field.getAnnotation(Column.class);
                String fieldNameStr = fieldName.value();            
                String classFieldNameStr = field.getName();
                Object fieldValue = data.get(fieldNameStr);

                if (fieldNameStr != null && classFieldNameStr != null && fieldValue != null) {
                    PropertyDescriptor pd = new PropertyDescriptor(classFieldNameStr, cls);
                    Method setMethod = pd.getWriteMethod();

                    Class type = field.getType();
                    if (Integer.class.equals(type)) {
                        Integer integerVal = Integer.valueOf(fieldValue.toString());
                        setMethod.invoke(retObj, integerVal);
                    } else if (Long.class.equals(type)) {
                        Long longVal = null;
                        if (fieldValue.getClass().equals(Long.class)) {
                            longVal = Long.valueOf(fieldValue.toString());
                        } else {
                            longVal = new Long(((Date)fieldValue).getTime());
                        }
                        setMethod.invoke(retObj, longVal);
                    } else if (Date.class.equals(type)) {
                        Date time = new Date(((Date) fieldValue).getTime());
                        setMethod.invoke(retObj, time);
                    } else if (Short.class.equals(type)) {
                        if (fieldValue.getClass().equals(Boolean.class)) {
                            if (fieldValue.equals(Boolean.TRUE)) {
                                fieldValue = new Integer(1);
                            }
                            if (fieldValue.equals(Boolean.FALSE)) {
                                fieldValue = new Integer(0);
                            }
                        }
                        Short shortVal = Short.valueOf(fieldValue.toString());
                        setMethod.invoke(retObj, shortVal);
                    } else if (Float.class.equals(type)) {
                        setMethod.invoke(retObj, new Float(fieldValue.toString()));
                    } else if (BigInteger.class.equals(type)) {
                        setMethod.invoke(retObj, new BigInteger(fieldValue.toString(), 10));
                    } else {
                        setMethod.invoke(retObj, fieldValue);
                    }              
                }
            }
        }
        return retObj;
    }
}
