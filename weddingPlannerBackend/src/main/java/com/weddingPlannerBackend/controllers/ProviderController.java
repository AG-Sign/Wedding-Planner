package com.weddingPlannerBackend.controllers;

import com.weddingPlannerBackend.dtos.ProviderDto;
import com.weddingPlannerBackend.events.OnRegistrationCompleteEvent;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.VerificationToken;
import com.weddingPlannerBackend.services.AuthenticationService;
import com.weddingPlannerBackend.services.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Base64;
import java.util.Calendar;

@CrossOrigin
@RestController
@RequestMapping("/api/providers")
public class ProviderController {
    @Autowired
    private ProviderService providerService;

    @Autowired
    private ProviderMapper providerMapper;


    @Autowired
    private AuthenticationService authenticationService;


    @Value("${redirectUrl}")
    private String redirectUrl;

    @Autowired
    ApplicationEventPublisher eventPublisher;

//    @Bean
//    @Autowired
//    public MappedInterceptor getProviderMappedInterceptor(AuthenticationController.MyHandlerInterceptor myHandlerInterceptor) {
//        return new MappedInterceptor(new String[]{"/api/providers/logout"}, myHandlerInterceptor);
//    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> registerProviderAccount(@RequestBody ProviderDto providerDto, HttpServletRequest request) {

        Provider registered = providerService.registerNewProviderAccount(providerDto);
        if (registered == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{ \"message\": \" Invalid provider data or email already exists\" }");
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        return ResponseEntity.status(HttpStatus.CREATED).body(providerMapper.toDto(registered));
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<Void> confirmRegistration(WebRequest request, Model model,
                                                    @RequestParam("token") String token) {

        VerificationToken verificationToken = providerService.getVerificationToken(token);


        Calendar cal = Calendar.getInstance();
        if (verificationToken == null || (verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }


        Provider provider = verificationToken.getProvider();
        provider.setVerified(true);
        providerService.saveRegisteredProvider(provider);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();


    }

    @GetMapping("/login")
    public ResponseEntity<String> loginProviderAccount(@RequestHeader(value = "Authorization") String providerInfo) {
        String[] arr = getDecodedInfo(providerInfo);
        String email = arr[0];
        String password = arr[1];
        String token = authenticationService.createToken(email, password, "provider");
        if (token.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"message\": \" Invalid email or password\" }");
        if (token.equalsIgnoreCase("not verified"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"message\": \" Email not verified\" }");
        return ResponseEntity.status(HttpStatus.OK).body("{ \"token\": \"" + token + "\" }");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUserAccount(@RequestHeader(value = "Authorization") String encodedToken) {
        String token = getDecodedToken(encodedToken);
        boolean deleted = authenticationService.delToken(token);
        if (!deleted)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"message\": \" Invalid token\" }");
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    private String[] getDecodedInfo(String encodedUserInfo) {
        String encodedUsernamePassword = encodedUserInfo.substring("Basic ".length()).trim();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(encodedUsernamePassword);
        String decodedLine = new String(bytes);
        int separator = decodedLine.indexOf(':');
        String[] decodedUserInfo = new String[2];
        decodedUserInfo[0] = decodedLine.substring(0, separator);
        decodedUserInfo[1] = decodedLine.substring(separator + 1);
        return decodedUserInfo;
    }

    private String getDecodedToken(String encodedToken) {
        return encodedToken.split(" ")[1];
    }

}
