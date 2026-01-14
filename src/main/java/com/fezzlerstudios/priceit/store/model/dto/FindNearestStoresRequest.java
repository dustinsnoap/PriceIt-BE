package com.fezzlerstudios.priceit.store.model.dto;

import com.fezzlerstudios.priceit.store.model.ClientLocation;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record FindNearestStoresRequest (
        UUID requestId,
        ClientLocation clientLocation,
        Integer limitPerStore,
        Double maxDistanceMiles,
        List<String> storePreferences
) { }
