package com.weddingPlannerBackend.mappers;

import com.weddingPlannerBackend.dtos.ProviderDto;
import com.weddingPlannerBackend.model.Provider;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {
    public ProviderDto toDto(Provider provider) {
        if (provider == null)
            return null;

        ProviderDto providerDto = new ProviderDto();
        providerDto.setName(provider.getName());
        providerDto.setEmail(provider.getEmail());
        providerDto.setAddress(provider.getAddress());
        providerDto.setVerified(provider.isVerified());

        return providerDto;
    }

    public Provider fromDto(ProviderDto providerDto) {
        Provider provider = new Provider();

        provider.setName(providerDto.getName());
        provider.setEmail(providerDto.getEmail());
        provider.setAddress(providerDto.getAddress());
        if (providerDto.getPassword() != null)
            provider.setPassword(BCrypt.hashpw(providerDto.getPassword(), BCrypt.gensalt()));
        return provider;
    }
}
