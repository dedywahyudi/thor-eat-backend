package com.thor.eat.api.entities.requests;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/9/7.
 */
@Getter
@Setter
public class StandardDivisionsEmailRequest {
    @NotNull
    private List<String> to = new ArrayList<>();

    private List<String> cc = new ArrayList<>();

    @NotBlank
    @SafeHtml
    private String subject;

    @NotBlank
    private String body;

    @NotNull
    private List<Long> standardDivisionIds = new ArrayList<>();
}
