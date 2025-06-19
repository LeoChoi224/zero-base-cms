package com.zerobase.cms.user.application;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.service.SignUpCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

import static com.zerobase.cms.user.exception.ErrorCode.ALREADY_REGISTER_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpApplication {

    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;

    @Value("${mailgun.key}")
    private String mailgunKey;

    public void customVerify(String email, String code) {
        signUpCustomerService.verifyEmail(email, code);
    }

    public String customerSignUp(SignUpForm form) {
        if (signUpCustomerService.isEmailExists(form.getEmail())) {
            throw new CustomException(ALREADY_REGISTER_USER);
            // Exception
        } else {
            Customer c = signUpCustomerService.signUp(form);

            String code = getRandomCode();
            String emailBody = getVerificationEmailBody(form.getEmail(), form.getName(), code);

            String from = "postmaster@sandboxea50d3421d064ce4902dd1467e461802.mailgun.org";
            String to ="education.leochoi@gmail.com";
            String subject = "Verification Email";

            log.info("Mailgun key: {}", mailgunKey);
            log.info("Expected Authorization header: Basic {}",
                    Base64.getEncoder().encodeToString(("api:" + mailgunKey).getBytes()));

            log.info("Sending email result : {}", mailgunClient.sendEmail(from, to, subject, emailBody).getBody());

            signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);
            return "회원 가입에 성공하였습니다.";
        }
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name, String code) {
        StringBuilder builder = new StringBuilder();
        return builder.append("Hello ")
                .append(name)
                .append("! Please Click Link for verification.\n\n")
                .append("http://localgost:8081/customer/signup/verify?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }
}
