package com.fezzlerstudios.priceit.store.model.dto;

import com.fezzlerstudios.priceit.store.model.ClientLocation;
import com.fezzlerstudios.priceit.store.model.StoreName;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record FindNearestStoresRequest (
        Boolean valid,
        UUID requestId,
        ClientLocation clientLocation,
        Integer limitPerStore,
        Double maxDistanceMiles,
        List<StoreName> storePreferences,
        String errorMessage
) {
    public static FindNearestStoresRequest ok(UUID requestId, ClientLocation clientLocation, Integer limitPerStore, Double maxDistanceMiles, List<StoreName> storePreferences) {
        return new FindNearestStoresRequest(
                true,
                requestId,
                clientLocation,
                limitPerStore,
                maxDistanceMiles,
                storePreferences,
                null
        );
    }

    public static FindNearestStoresRequest error(UUID requestId, String errorMessage) {
        return new FindNearestStoresRequest(
                false,
                requestId,
                null,
                null,
                null,
                null,
                errorMessage
        );
    }
}
