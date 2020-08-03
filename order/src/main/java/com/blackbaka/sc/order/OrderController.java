package com.blackbaka.sc.order;

import com.blackbaka.sc.core.dao.mapper.OrdersMapper;
import com.blackbaka.sc.core.dao.model.Orders;
import com.blackbaka.sc.core.rest.OrderApi;
import com.blackbaka.sc.core.result.Result;
import com.blackbaka.sc.core.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Kai Yi
 * @Date 2020/07/28
 * @Description
 */

@RestController
@Slf4j
public class OrderController implements OrderApi {

    @Resource
    private OrdersMapper ordersMapper;


    @Override
    public Result<String> addOrder(Integer accountId, Integer stockId, Integer count, Integer singleMoney, Integer allMoney) {
        Orders orders = new Orders();
        orders.setAccountId(accountId).setStockId(stockId).setSingleMoney(singleMoney).setAllMoney(allMoney).setCount(count);
        ordersMapper.insert(orders);

        return ResponseUtil.operateSuccess();
    }
}
