package com.thor.eat.api.entities.requests;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * Created by wangjinggang on 2018/9/1.
 */
@Getter
@Setter
public class LoginRequest {
    @SafeHtml
    @NotBlank
    private String username;

    @SafeHtml
    @NotBlank
    private String password;
}
