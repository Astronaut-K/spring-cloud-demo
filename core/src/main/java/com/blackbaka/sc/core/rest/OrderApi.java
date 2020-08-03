package com.blackbaka.sc.core.rest;

import com.blackbaka.sc.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Kai Yi
 * @Date 2020/07/28
 * @Description
 */

@FeignClient(name = "order")
public interface OrderApi {


    @GetMapping("addOrder")
    Result<String> addOrder(@RequestParam("accountId") Integer accountId, @RequestParam("stockId") Integer stockId,
                            @RequestParam("count") Integer count,
                            @RequestParam("singleMoney") Integer singleMoney, @RequestParam("allMoney") Integer allMoney);


}
