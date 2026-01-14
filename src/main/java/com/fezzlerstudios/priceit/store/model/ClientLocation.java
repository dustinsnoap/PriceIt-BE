package com.fezzlerstudios.priceit.store.model;

import lombok.Builder;

@Builder
public record ClientLocation(
        String streetAddress,
        Double latitude,
        Double longitude,
        String city,
        String state,
        Integer zipcode
) { }
