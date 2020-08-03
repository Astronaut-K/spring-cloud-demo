package com.blackbaka.sc.service;

import com.blackbaka.sc.core.pojo.StockVO;
import com.blackbaka.sc.core.rest.AccountApi;
import com.blackbaka.sc.core.rest.OrderApi;
import com.blackbaka.sc.core.rest.StockApi;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kai Yi
 * @Date 2020/07/28
 * @Description
 */

@RestController
@RequestMapping("/api/")
@Api(tags = "Api")
@Slf4j
public class ApiController {

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private StockApi stockApi;

    @Autowired
    private OrderApi orderApi;


    @GetMapping("buySuccess")
    public String buySuccess(Integer accountId, Integer stockId, Integer count) {
        StockVO stockVO = stockApi.reduceCount(stockId, count).getData();
        accountApi.reduceMoney(accountId, stockVO.getMoney() * count);
        orderApi.addOrder(accountId, stockId, count, stockVO.getMoney(), stockVO.getMoney() * count);

        return "OK";
    }

}
