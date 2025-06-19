package com.zerobase.cms.user.client;

import com.zerobase.cms.user.config.FeignConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "mailgun",
        url = "https://api.mailgun.net/v3/",
        configuration = FeignConfig.class
)
@Qualifier("mailgun")
public interface MailgunClient {

    @PostMapping(value = "/sandboxea50d3421d064ce4902dd1467e461802.mailgun.org/messages",
            consumes = "application/x-www-form-urlencoded")
    ResponseEntity<String> sendEmail(
            @org.springframework.web.bind.annotation.RequestParam("from") String from,
            @org.springframework.web.bind.annotation.RequestParam("to") String to,
            @org.springframework.web.bind.annotation.RequestParam("subject") String subject,
            @org.springframework.web.bind.annotation.RequestParam("text") String text
    );
}
