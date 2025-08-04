package xyz.dreature.ssjrt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.dreature.ssjrt.common.vo.Result;
import xyz.dreature.ssjrt.service.BaseService;

import java.io.Serializable;
import java.util.List;

// 基接口
public abstract class BaseController<T, ID extends Serializable, S extends BaseService<T, ID>> {
    @Autowired
    protected S service;

    // ===== 通用基础操作 =====
    // 查询总数
    @RequestMapping("/count-all")
    public ResponseEntity<Result<Integer>> countAll() {
        int count = service.countAll();
        String message = String.format("查询总数为 %d 条", count);
        return ResponseEntity.ok().body(Result.success(message, count));
    }

    // 查询全表
    @RequestMapping("/select-all")
    public ResponseEntity<Result<List<T>>> selectAll() {
        List<T> result = service.selectAll();
        int resultCount = result.size();
        String message = String.format("全表查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // 查询随机
    @RequestMapping("/select-random")
    public ResponseEntity<Result<List<T>>> selectRandom(
            @RequestParam(name = "count", defaultValue = "1") int count
    ) {
        List<T> result = service.selectRandom(count);
        int resultCount = result.size();
        String message = String.format("随机查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // 查询页面
    @RequestMapping("/select-by-page")
    public ResponseEntity<Result<List<T>>> selectByPage(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit") int limit
    ) {
        List<T> result = service.selectByPage(offset, limit);
        int resultCount = result.size();
        String message = String.format("页面查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // 单项插入
    @RequestMapping("/insert")
    public ResponseEntity<Result<Void>> insert(
            @RequestBody T entity
    ) {
        int affectedRows = service.insert(entity);
        String message = String.format("单项插入 %d 条数据", affectedRows);
        return ResponseEntity.ok().body(Result.success(message, null));
    }

    // 单批插入
    @RequestMapping("/insert-batch")
    public ResponseEntity<Result<Void>> insertBatch(
            @RequestBody List<T> entities
    ) {
        int affectedRows = service.insertBatch(entities);
        String message = String.format("单批插入 %d 条数据", affectedRows);
        return ResponseEntity.ok().body(Result.success(message, null));
    }

    // 单项更新
    @RequestMapping("/update")
    public ResponseEntity<Result<Void>> update(
            @RequestBody T entity
    ) {
        int affectedRows = service.update(entity);
        String message = String.format("单项更新 %d 条数据", affectedRows);
        return ResponseEntity.ok().body(Result.success(message, null));
    }

    // 单批更新
    @RequestMapping("/update-batch")
    public ResponseEntity<Result<Void>> updateBatch(
            @RequestBody List<T> entities
    ) {
        int affectedRows = service.updateBatch(entities);
        String message = String.format("单批更新 %d 条数据", affectedRows);
        return ResponseEntity.ok().body(Result.success(message, null));
    }

    // 单项插入或更新
    @RequestMapping("/upsert")
    public ResponseEntity<Result<Void>> upsert(
            @RequestBody T entity
    ) {
        int affectedRows = service.upsert(entity);
        String message = String.format("单项插入或更新 %d 条数据", affectedRows);
        return ResponseEntity.ok().body(Result.success(message, null));
    }

    // 单批插入或更新
    @RequestMapping("/upsert-batch")
    public ResponseEntity<Result<Void>> upsertBatch(
            @RequestBody List<T> entities
    ) {
        int affectedRows = service.upsertBatch(entities);
        String message = String.format("单批插入或更新 %d 条数据", affectedRows);
        return ResponseEntity.ok().body(Result.success(message, null));
    }

    // 清空
    @RequestMapping("/truncate")
    public ResponseEntity<Result<Void>> truncate() {
        int count = service.countAll();
        service.truncate();
        String message = String.format("清空 %d 条数据", count);
        return ResponseEntity.ok().body(Result.success(message, null));
    }
}
