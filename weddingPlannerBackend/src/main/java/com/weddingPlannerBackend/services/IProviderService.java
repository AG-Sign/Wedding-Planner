package com.weddingPlannerBackend.services;

import com.weddingPlannerBackend.dtos.ProviderDto;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.VerificationToken;

public interface IProviderService {
    Provider registerNewProviderAccount(ProviderDto userDto);

    Provider getProvider(String verificationToken);

    void saveRegisteredProvider(Provider provider);

    void createVerificationToken(Provider provider, String token);

    VerificationToken getVerificationToken(String VerificationToken);
}
