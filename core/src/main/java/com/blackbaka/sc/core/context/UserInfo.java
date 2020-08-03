package com.blackbaka.sc.core.context;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 访问用户信息
 *
 * @author cuijie
 * @since 2018-08-03
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -4033689746127806426L;

    private Long userId;

    private String username;

    private String name;

    private String region;

    private String department;

    /**
     * 来源，区分公众侧
     */
    private String from;

    private List<Integer> dataPermissionList;

}
