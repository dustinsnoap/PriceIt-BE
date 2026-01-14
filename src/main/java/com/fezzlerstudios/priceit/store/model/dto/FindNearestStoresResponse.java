package com.fezzlerstudios.priceit.store.model.dto;

import com.fezzlerstudios.priceit.store.model.ClientLocation;
import com.fezzlerstudios.priceit.store.model.StoreLocation;
import com.fezzlerstudios.priceit.store.model.StoreName;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record FindNearestStoresResponse(
    boolean ok,
    ClientLocation clientLocation,
    Map<StoreName, List<StoreLocation>> stores
) {}
