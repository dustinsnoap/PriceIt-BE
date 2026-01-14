package com.fezzlerstudios.priceit.store.model;

import lombok.Builder;

@Builder
public record StoreLocation(
        Integer storeId,
        StoreName storeName,
        String streetAddress,
        Double latitude,
        Double longitude,
        String city,
        String state,
        Integer zipcode,
        Double distance
) { }
