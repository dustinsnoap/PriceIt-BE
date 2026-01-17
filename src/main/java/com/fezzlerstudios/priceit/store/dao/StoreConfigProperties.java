package com.fezzlerstudios.priceit.store.dao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "priceit.stores")
public record StoreConfigProperties (
        FindNearest findNearest
) {
    public record FindNearest(
            Integer perStoreMin,
            Integer perStoreMax,
            Double minDistanceMiles,
            Double maxDistanceMiles,
            Integer storeCountMin,
            Integer storeCountMax
    ) {}
}
