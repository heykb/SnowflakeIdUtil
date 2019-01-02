package com.zhu.mine.domian;

import com.zhu.mine.util.AddGroup;
import com.zhu.mine.util.IdUtil;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class User {
    @NotNull(message = "username不能为空",groups = AddGroup.class)
    private String username;
    @NotEmpty
    private String password;
    private Integer age;
}
