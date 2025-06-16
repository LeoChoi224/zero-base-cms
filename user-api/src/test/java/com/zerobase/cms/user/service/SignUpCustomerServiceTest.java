package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignUpCustomerServiceTest {

    @Autowired
    private  SignUpCustomerService service;

    @Test
    void signUp() {
        SignUpForm form = SignUpForm.builder()
                .name("name")
                .birth("birth")
                .email("abc@gmail.com")
                .password("1")
                .phone("01012345678")
                .build();

        Customer c = service.signUp(form);
        assertNotNull(c.getId());
        assertNotNull(c.getCreatedAt());
//        Assert.isTrue(service.signUp(form).getId()!=null);
    }

}