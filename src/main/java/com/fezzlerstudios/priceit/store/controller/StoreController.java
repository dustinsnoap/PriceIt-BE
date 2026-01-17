package com.fezzlerstudios.priceit.store.controller;

import com.fezzlerstudios.priceit.store.model.ClientLocation;
import com.fezzlerstudios.priceit.store.model.StoreName;
import com.fezzlerstudios.priceit.store.model.dto.FindNearestStoresRequest;
import com.fezzlerstudios.priceit.store.model.dto.FindNearestStoresResponse;
import com.fezzlerstudios.priceit.store.service.StoresService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class StoreController {

    private final StoresService storesService;

    public StoreController(StoresService storesService) {
        this.storesService = storesService;
    }

    @GetMapping("/stores/nearest")
    public FindNearestStoresResponse findNearestStores(
            @RequestParam(name = "street", required = false) String streetAddress,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "lat") double latitude,
            @RequestParam(name = "long") double longitude,
            @RequestParam(name = "zip", required = false) Integer zipcode,
            @RequestParam(name = "lps", required = false) Integer limitPerStore,
            @RequestParam(name = "mdm") Double maxDistanceMiles,
            @RequestParam(name = "s") List<StoreName> storePreferences
    ) {
        ClientLocation clientLocation = ClientLocation.builder()
                .streetAddress(streetAddress)
                .city(city)
                .state(state)
                .latitude(latitude)
                .longitude(longitude)
                .zipcode(zipcode)
                .build();
        FindNearestStoresRequest request = FindNearestStoresRequest.builder()
                .requestId(UUID.randomUUID())
                .clientLocation(clientLocation)
                .limitPerStore(limitPerStore)
                .maxDistanceMiles(maxDistanceMiles)
                .storePreferences(storePreferences)
                .build();

        return storesService.findNearestStore(request);
    }
}
