package com.zerobase.cms.user.client;

import com.zerobase.cms.user.client.mailgun.SendMailForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "mailgun",
        url = "https://api.mailgun.net/v3/"
)
@Qualifier("mailgun")
public interface MailgunClient {

    @PostMapping(value = "/{domain}/messages")
    ResponseEntity<String> sendEmail(
            @org.springframework.web.bind.annotation.PathVariable("domain") String domain,
            @SpringQueryMap SendMailForm form);
}
