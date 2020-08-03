package com.blackbaka.sc.account;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.blackbaka.sc.core.dao.mapper.AccountMapper;
import com.blackbaka.sc.core.dao.model.Account;
import com.blackbaka.sc.core.exception.ExceptionObject;
import com.blackbaka.sc.core.exception.ServiceException;
import com.blackbaka.sc.core.rest.AccountApi;
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
public class AccountController implements AccountApi {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public Result<String> reduceMoney(Integer id, Integer money) {
        Account account = accountMapper.selectOne(Wrappers.lambdaQuery(Account.class)
                .eq(Account::getId, id));
        if (account == null) {
            throw new ServiceException(new ExceptionObject(400, "账户不存在"));
        }
        if (account.getMoney() - money < 0) {
            throw new ServiceException(new ExceptionObject(400, "账户余额不足"));
        }
        account.setMoney(account.getMoney() - money);
        accountMapper.updateById(account);
        return ResponseUtil.getSuccess("账户id：" + id + "扣除余额：" + money);
    }

}
