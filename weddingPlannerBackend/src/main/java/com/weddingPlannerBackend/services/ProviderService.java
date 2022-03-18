package com.weddingPlannerBackend.services;

import com.weddingPlannerBackend.dtos.ProviderDto;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.VerificationToken;
import com.weddingPlannerBackend.repositories.ProviderRepo;
import com.weddingPlannerBackend.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class ProviderService implements IProviderService {
    @Autowired
    private ProviderRepo repository;

    @Autowired
    private ProviderMapper providerMapper;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public Provider registerNewProviderAccount(ProviderDto providerDto) {
        Provider provider = providerMapper.fromDto(providerDto);
        if (!validateProvider(provider) || emailExist(provider.getEmail()))
            return null;

        return repository.save(provider);
    }

    private boolean emailExist(String email) {
        return repository.findById(email).isPresent();
    }

    private boolean validateProvider(Provider provider) {
        return validateEmail(provider.getEmail()) && provider.getName() != null && provider.getPassword() != null;
    }

    private boolean validateEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);

        return matcher.matches();
    }

    @Override
    public Provider getProvider(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getProvider();
    }

    @Override
    public void saveRegisteredProvider(Provider provider) {
        repository.save(provider);
    }

    @Override
    public void createVerificationToken(Provider provider, String token) {
        VerificationToken myToken = new VerificationToken(token, provider);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
}
