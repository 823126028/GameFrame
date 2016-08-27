package com.quanmin.mybatis.wrapper;

import org.mybatis.spring.support.SqlSessionDaoSupport;

public class BaseDao<T extends IModel> extends SqlSessionDaoSupport implements IBaseDao <T>{

}
