package com.blackbaka.sc.core.context;



import com.blackbaka.sc.core.exception.ExceptionObject;
import com.blackbaka.sc.core.exception.ServiceException;

import javax.validation.constraints.NotNull;

/**
 * 当前上下文环境中登录用户信息的相关操作
 *
 * @author cuijie
 * @since 2018-08-03
 */
public final class UserContext {

    protected static final InheritableThreadLocal<UserInfo> TL = new InheritableThreadLocal<>();

    private UserContext() {
    }


    /**
     * 向当前上下文环境填充用户信息
     *
     * @param userInfo
     */
    public static void set(UserInfo userInfo) {
        TL.set(userInfo);
    }

    /**
     * 获取当前上下文环境中的用户信息，若不存在用户信息则返回null
     *
     * @return
     */
    public static UserInfo get() {
        return TL.get();
    }


    /**
     * 获取当前上下文环境中的用户信息，若不存在用户信息则填充默认值并返回
     *
     * @return
     */
    public static UserInfo getOrDefault() {
        UserInfo userInfo = TL.get();
        if (userInfo == null) {
            TL.set(new UserInfo());
            userInfo = TL.get();
        }
        return userInfo;
    }

    /**
     * 获取当前上下文环境中的用户信息，若不存在用户信息则填充并返回
     *
     * @return
     */
    public static UserInfo getOrDefault(UserInfo uinfo) {
        UserInfo userInfo = TL.get();
        if (userInfo == null) {
            TL.set(uinfo);
            userInfo = TL.get();
        }
        return userInfo;
    }


    /**
     * 获取当前上下文环境中的用户信息，若不存在用户信息则抛出ServiceException异常
     *
     * @return
     */
    public static UserInfo getOrThrow() throws ServiceException {
        UserInfo userInfo = TL.get();
        if (userInfo == null) {
            throw new ServiceException(new ExceptionObject(500, "当前登录用户信息(UserInfo)不存在."));
        }
        return userInfo;
    }

    /**
     * 获取当前上下文环境中的用户信息，若不存在用户信息则抛出异常t
     *
     * @param t
     * @param <T>
     * @return
     * @throws T
     */
    public static <T extends Throwable> UserInfo getOrThrow(@NotNull T t) throws T {
        UserInfo userInfo = TL.get();
        if (userInfo == null) {
            throw t;
        }
        return userInfo;
    }


    /**
     * 删除当前上下文环境中的用户信息
     */
    public static void remove() {
        TL.remove();
    }


    /**
     * 判断当前userinfo的userid是否不为空
     *
     * @return
     */
    public static boolean isUseridNotNull() {
        if (get() == null || get().getUserId() == null) {
            return false;
        } else {
            return true;
        }
    }


}
