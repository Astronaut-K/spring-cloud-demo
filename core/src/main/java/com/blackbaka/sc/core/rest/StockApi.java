package com.blackbaka.sc.core.rest;

import com.blackbaka.sc.core.result.Result;
import com.blackbaka.sc.core.pojo.StockVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Kai Yi
 * @Date 2020/07/28
 * @Description
 */

@FeignClient(name = "stock")
public interface StockApi {

    @GetMapping("reduceCount")
    Result<StockVO> reduceCount(@RequestParam("id") Integer id, @RequestParam("count") Integer count);


}
