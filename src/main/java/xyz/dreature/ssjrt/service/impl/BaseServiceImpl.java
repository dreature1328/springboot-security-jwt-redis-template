package xyz.dreature.ssjrt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.dreature.ssjrt.common.util.BatchUtils;
import xyz.dreature.ssjrt.mapper.BaseMapper;
import xyz.dreature.ssjrt.service.BaseService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public abstract class BaseServiceImpl<T, ID extends Serializable, M extends BaseMapper<T, ID>> implements BaseService<T, ID> {
    @Autowired
    protected M mapper;

    // ===== 通用基础操作 =====
    // 查询总数
    public int countAll() {
        return mapper.countAll();
    }

    // 查询全表
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    // 查询随机
    public List<T> selectRandom(int count) {
        return mapper.selectRandom(count);
    }

    // 查询页面
    public List<T> selectByPage(int offset, int limit) {
        return mapper.selectByPage(offset, limit);
    }

    // 单项查询
    public T selectById(ID id) {
        return mapper.selectById(id);
    }

    // 逐项查询
    public List<T> selectByIds(ID... ids) {
        return BatchUtils.mapEach(Arrays.asList(ids), mapper::selectById);
    }

    // 单批查询
    public List<T> selectBatchByIds(List<ID> ids) {
        return mapper.selectBatchByIds(ids);
    }

    // 分批查询
    public List<T> selectBatchByIds(List<ID> ids, int batchSize) {
        return BatchUtils.flatMapBatch(ids, batchSize, mapper::selectBatchByIds);
    }

    // 单项插入
    public int insert(T entity) {
        return mapper.insert(entity);
    }

    // 逐项插入
    public int insert(T... entities) {
        return BatchUtils.reduceEachToInt(Arrays.asList(entities), mapper::insert);
    }

    // 单批插入
    public int insertBatch(List<T> entities) {
        return mapper.insertBatch(entities);
    }

    // 分批插入
    public int insertBatch(List<T> entities, int batchSize) {
        return BatchUtils.reduceBatchToInt(entities, batchSize, mapper::insertBatch);
    }

    // 单项更新
    public int update(T entity) {
        return mapper.update(entity);
    }

    // 逐项更新
    public int update(T... entities) {
        return BatchUtils.reduceEachToInt(Arrays.asList(entities), mapper::update);
    }

    // 单批更新
    public int updateBatch(List<T> entities) {
        return mapper.updateBatch(entities);
    }

    // 分批更新
    public int updateBatch(List<T> entities, int batchSize) {
        return BatchUtils.reduceBatchToInt(entities, batchSize, mapper::updateBatch);
    }

    // 单项插入或更新
    public int upsert(T entity) {
        return mapper.upsert(entity);
    }

    // 逐项插入或更新
    public int upsert(T... entities) {
        return BatchUtils.reduceEachToInt(Arrays.asList(entities), mapper::upsert);
    }

    // 单批插入或更新
    public int upsertBatch(List<T> entities) {
        return mapper.upsertBatch(entities);
    }

    // 分批插入或更新
    public int upsertBatch(List<T> entities, int batchSize) {
        return BatchUtils.reduceBatchToInt(entities, batchSize, mapper::upsertBatch);
    }

    // 单项删除
    public int deleteById(ID id) {
        return mapper.deleteById(id);
    }

    // 逐项删除
    public int deleteByIds(ID... ids) {
        return BatchUtils.reduceEachToInt(Arrays.asList(ids), mapper::deleteById);
    }

    // 单批删除
    public int deleteBatchByIds(List<ID> ids) {
        return mapper.deleteBatchByIds(ids);
    }

    // 分批删除
    public int deleteBatchByIds(List<ID> ids, int batchSize) {
        return BatchUtils.reduceBatchToInt(ids, batchSize, mapper::deleteBatchByIds);
    }

    // 清空
    public void truncate() {
        mapper.truncate();
    }
}
