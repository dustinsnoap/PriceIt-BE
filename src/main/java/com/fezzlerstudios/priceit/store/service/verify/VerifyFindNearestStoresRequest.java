package com.fezzlerstudios.priceit.store.service.verify;

import com.fezzlerstudios.priceit.store.dao.StoreConfigProperties;
import com.fezzlerstudios.priceit.store.model.ClientLocation;
import com.fezzlerstudios.priceit.store.model.StoreName;
import com.fezzlerstudios.priceit.store.model.dto.FindNearestStoresRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class VerifyFindNearestStoresRequest {

    private static final Logger log = LoggerFactory.getLogger(VerifyFindNearestStoresRequest.class);
    private final StoreConfigProperties.FindNearest config;

    public VerifyFindNearestStoresRequest(
            StoreConfigProperties storeConfigProperties
    ) {

        this.config = storeConfigProperties.findNearest();
    }

    public FindNearestStoresRequest verify(FindNearestStoresRequest request) {
        try {
            // start timer
            long startTime = System.nanoTime();

            // ensure not null
            verifyNotNull(request);
            verifyRequiredDataIsPresent(request.clientLocation());

            // verify latitude and longitude
            verifyLatitude(request.clientLocation().latitude());
            verifyLongitude(request.clientLocation().longitude());

            // verify other values
            Integer limitPerStore = verifyLimitPerStore(request.limitPerStore());
            Double maxDistanceMiles = verifyMaxDistanceMiles(request.maxDistanceMiles());
            List<StoreName> storePreferences = verifyStorePreferences(request.storePreferences());

            // log response time
            Duration responseTime = Duration.ofNanos(System.nanoTime() - startTime);
            log.info("Verify Response Time: {}ns", responseTime);

            //build response
            return FindNearestStoresRequest.ok(request.requestId(), request.clientLocation(), limitPerStore, maxDistanceMiles, storePreferences);
        } catch (InvalidParameterException | IllegalStateException e){
            log.error(e.getMessage());
            return FindNearestStoresRequest.error(request.requestId(), e.getMessage());
        }
    }

    private void verifyNotNull(FindNearestStoresRequest request) {
        if (config == null) throw new IllegalStateException("MISSING_CONFIG");
        if (config.perStoreMin() == null) throw new IllegalStateException("MISSING_CONFIG_VALUE: perStoreMin");
        if (config.perStoreMax() == null) throw new IllegalStateException("MISSING_CONFIG_VALUE: perStoreMax");
        if (config.minDistanceMiles() == null) throw new IllegalStateException("MISSING_CONFIG_VALUE: minDistanceMiles");
        if (config.maxDistanceMiles() == null) throw new IllegalStateException("MISSING_CONFIG_VALUE: maxDistanceMiles");

        if (request == null) throw new InvalidParameterException("REQUEST_IS_NULL");
        if (request.clientLocation() == null) throw new InvalidParameterException("CLIENT_LOCATION_IS_NULL");
        if (request.requestId() == null) throw new InvalidParameterException("MISSING_REQUEST_ID");
        if (request.storePreferences() == null || request.storePreferences().isEmpty()) throw new InvalidParameterException("MISSING_STORE_PREFERENCES");
    }

    private void verifyRequiredDataIsPresent(ClientLocation clientLocation) {
        if (clientLocation.latitude() != null && clientLocation.longitude() != null) return;
        if (!clientLocation.city().isBlank() && clientLocation.state().isBlank() && clientLocation.streetAddress().isBlank()) return;
        throw new InvalidParameterException("MISSING_REQUIRED_LOCATION_DATA | clientLocation=" + clientLocation);
    }

    private void verifyLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) throw new InvalidParameterException("LATITUDE_NOT_WITHIN_BOUNDS");
    }

    private void verifyLongitude(double longitude) {
        if (longitude < -90 || longitude > 90) throw new InvalidParameterException("LONGITUDE_NOT_WITHIN_BOUNDS");
    }

    private Integer verifyLimitPerStore(Integer limitPerStore) {
        Integer perStoreMin = config.perStoreMin();
        Integer perStoreMax = config.perStoreMax();

        if (limitPerStore == null) return perStoreMin;
        if (limitPerStore < perStoreMin) {
            log.warn("LIMIT_PER_STORE_TOO_SMALL | Updating: {} -> {}", limitPerStore, perStoreMin);
            return perStoreMin;
        }
        if (limitPerStore > perStoreMax) {
            log.warn("LIMIT_PER_STORE_TOO_LARGE | Updating: {} -> {}", limitPerStore, perStoreMax);
            return perStoreMax;
        }
        return limitPerStore;
    }

    private Double verifyMaxDistanceMiles(Double distance) {
        Double minDistanceMiles = config.minDistanceMiles();
        Double maxDistanceMiles = config.maxDistanceMiles();

        if (distance == null) return maxDistanceMiles;
        if (distance < minDistanceMiles) {
            log.warn("MAX_DISTANCE_MILES_TOO_SMALL | Updating: {} -> {}", distance, minDistanceMiles);
            return minDistanceMiles;
        }
        if (distance > maxDistanceMiles) {
            log.warn("MAX_DISTANCE_MILES_TOO_LARGE | Updating: {} -> {}", distance, maxDistanceMiles);
        }
        return distance;
    }

    private List<StoreName> verifyStorePreferences(List<StoreName> storePreferences) {
        if (storePreferences == null || storePreferences.isEmpty()) throw new InvalidParameterException("MISSING_STORE_PREFERENCES");

        List<StoreName> storeNamesList = new ArrayList<>(storePreferences.size());
        for (StoreName storeName : storePreferences) {
            try {
                storeNamesList.add(storeName);
            } catch (IllegalArgumentException e) {
                log.warn("STORE_PREFERENCE_DOESNT_EXIST");
            }
        }

        if (storeNamesList.size() < config.storeCountMin()) throw new InvalidParameterException("TOO_FEW_VALID_STORE_PREFERENCES");
        if (storeNamesList.size() > config.storeCountMax()) {
            log.warn("TOO_MANY_STORE_PREFERENCES | Updating: {} -> {}", storeNamesList.size(), config.storeCountMax());
            return storeNamesList.subList(0, config.storeCountMax());
        }
        return storeNamesList;
    }
}
