package com.fezzlerstudios.priceit.store.service;

import com.fezzlerstudios.priceit.store.model.dto.FindNearestStoresRequest;
import com.fezzlerstudios.priceit.store.model.dto.FindNearestStoresResponse;
import com.fezzlerstudios.priceit.store.service.verify.VerifyFindNearestStoresRequest;
import org.springframework.stereotype.Service;

@Service
public class StoresService {

    private final VerifyFindNearestStoresRequest verifyFindNearestStoresRequest;

    public StoresService (
            VerifyFindNearestStoresRequest verifyFindNearestStoresRequest
    ) {
        this.verifyFindNearestStoresRequest = verifyFindNearestStoresRequest;
    }

    public FindNearestStoresResponse findNearestStore(FindNearestStoresRequest request) {
        // verify request
        FindNearestStoresRequest validRequest = verifyFindNearestStoresRequest.verify(request);
        if (!validRequest.valid()) return null;

        //calculate nearest

        // build and return response
        return null;
    }
}
