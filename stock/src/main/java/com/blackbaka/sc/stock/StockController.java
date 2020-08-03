package com.blackbaka.sc.stock;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.blackbaka.sc.core.dao.mapper.StockMapper;
import com.blackbaka.sc.core.dao.model.Stock;
import com.blackbaka.sc.core.exception.ExceptionObject;
import com.blackbaka.sc.core.exception.ServiceException;
import com.blackbaka.sc.core.pojo.StockVO;
import com.blackbaka.sc.core.rest.StockApi;
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
public class StockController implements StockApi {

    @Resource
    private StockMapper stockMapper;

    @Override
    public Result<StockVO> reduceCount(Integer id, Integer count) {
        Stock stock = stockMapper.selectOne(Wrappers.lambdaQuery(Stock.class)
                .eq(Stock::getId, id));
        if (stock == null) {
            throw new ServiceException(new ExceptionObject(400, "库存不存在"));
        }
        if (stock.getCount() - count < 0) {
            throw new ServiceException(new ExceptionObject(400, "库存数量不足"));
        }
        stock.setCount(stock.getCount() - count);
        stockMapper.updateById(stock);

        StockVO vo = new StockVO();
        vo.setId(stock.getId());
        vo.setCount(count);
        vo.setMoney(stock.getMoney());

        return ResponseUtil.getSuccess(vo);
    }

}
