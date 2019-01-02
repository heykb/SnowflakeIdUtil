package com.zhu.mine.util;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.zhu.mine.domian.User;

import javax.validation.Validation;
import java.awt.datatransfer.FlavorEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;


public class FluentValidatorUtil {
    public static ThreadLocal<javax.validation.Validator> VALIDATOR = new ThreadLocal<javax.validation.Validator>(){
        @Override
        protected javax.validation.Validator initialValue() {
            Locale.setDefault(Locale.ENGLISH);
            return Validation.buildDefaultValidatorFactory().getValidator();
        }
    };

    private static <T> void validate(List<T> list,Class<?>... groups){
        Result result = FluentValidator.checkAll(groups)
                .onEach(list,new HibernateSupportedValidator<T>().setHiberanteValidator(VALIDATOR.get()))
                .doValidate().result(toSimple());
        if(!result.isSuccess()){
                throw new RuntimeException(result.getErrors().get(0));
        }
    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<User>();
        users.add(User.builder()
                .username("fsdfdsf")
                .password("fdsfdf").build());
        users.add(User.builder()
                        .password("fdsfdf").build());
        FluentValidatorUtil.validate(users,AddGroup.class);
    }


}
