package com.qingting.service;
import com.qingting.utils.Page;
import com.qingting.utils.PageIndex;

import java.util.List;

/**
 * Created by ZHANGBO583 on 2015-10-12.
 */
public interface BaseService<T> {
    public Integer add(T t);
    public Integer update(T t);
    public Integer delete(T t);
    public List<T> getByPage(T t, Page page);
    public T get(T t);
    public Integer getCount(T t);
    public Integer batchAdd(List<T> list);
    public List<T> getList(T t);
    public T getById(Long id);
}
