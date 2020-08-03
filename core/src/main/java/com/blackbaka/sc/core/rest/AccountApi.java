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

@FeignClient(name = "account")
public interface AccountApi {

    @GetMapping("reduceMoney")
    Result<String> reduceMoney(@RequestParam("id") Integer id, @RequestParam("money") Integer money);


}
