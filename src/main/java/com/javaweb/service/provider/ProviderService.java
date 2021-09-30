package com.kuang.service.provider;

import com.kuang.pojo.Provider;

import java.util.List;

public interface ProviderService {

    List<Provider> getProviderList(String queryProCode, String queryProName);

    Provider getProviderById(int id);

    boolean modifyProvider(Provider provider);

    boolean addProvider(Provider provider);

    int deleteProvider(int id);
}
