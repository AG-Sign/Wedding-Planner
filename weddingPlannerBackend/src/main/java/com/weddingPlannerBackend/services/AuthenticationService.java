package com.weddingPlannerBackend.services;

import com.weddingPlannerBackend.model.AuthenticationToken;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.repositories.AuthenticationTokenRepo;
import com.weddingPlannerBackend.repositories.ProviderRepo;
import com.weddingPlannerBackend.repositories.UserRepo;
import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService implements IAuthenticationService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProviderRepo providerRepo;

    @Autowired
    private AuthenticationTokenRepo authenticationTokenRepo;

    @Override
    public AuthenticationToken getByToken(String token) {
        return authenticationTokenRepo.findByToken(token);
    }

    @Override
    public String createToken(String email, String password, String type) {
        if (type.equalsIgnoreCase("provider")) return createTokenForProvider(email, password);
        else if (type.equalsIgnoreCase("user")) return createTokenForUser(email, password);
        else throw new IllegalArgumentException("Only supports provider or user");
    }

    @Override
    public boolean delToken(String token) {
        if (!authenticationTokenRepo.existsById(token)) return false;
        authenticationTokenRepo.deleteById(token);
        return true;
    }

    @Override
    public boolean isAuthenticated(String token) {
        return authenticationTokenRepo.existsById(token);
    }


    private String createTokenForUser(String email, String password) {
    	System.out.println("email " + email);
    	System.out.println("Password" + password);
        Optional<User> optionalUser = userRepo.findById(email);
        if (optionalUser.isEmpty()) return "";
        User user = optionalUser.get();
        if (!BCrypt.checkpw(password, user.getPassword())) return "";
        if (!user.isVerified()) return "not verified";
        //everything OK
        Optional<AuthenticationToken> optional = authenticationTokenRepo.findByUser(user);
        optional.ifPresent(authenticationToken -> authenticationTokenRepo.deleteById(authenticationToken.getToken()));
        String token = UUID.randomUUID().toString();
        AuthenticationToken authenticationToken = new AuthenticationToken(token);
        authenticationToken.setUser(user);
        authenticationTokenRepo.save(authenticationToken);
        return token;
    }

    private String createTokenForProvider(String email, String password) {
        Optional<Provider> optionalProvider = providerRepo.findById(email);
        if (optionalProvider.isEmpty()) return "";
        Provider provider = optionalProvider.get();
        if (!BCrypt.checkpw(password, provider.getPassword())) return "";
        if (!provider.isVerified()) return "not verified";
        //everything OK
        Optional<AuthenticationToken> optional = authenticationTokenRepo.findByProvider(provider);
        optional.ifPresent(authenticationToken -> authenticationTokenRepo.deleteById(authenticationToken.getToken()));
        String token = UUID.randomUUID().toString();
        AuthenticationToken authenticationToken = new AuthenticationToken(token);
        authenticationToken.setProvider(provider);
        authenticationTokenRepo.save(authenticationToken);
        return token;
    }

}
