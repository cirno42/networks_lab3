package ru.nsu.nikolotov.geofinder.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.nikolotov.geofinder.responses.OtmPlacesResponse;
import ru.nsu.nikolotov.geofinder.responses.OtmPlaceInfo;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class OpenTripMapAPI {
    public static CompletableFuture<OtmPlacesResponse> getInterestingPlacesOrNull(double lat, double lng, int radius, String key) {
        String latS = Double.toString(lat).replace(',', '.');
        String lngS = Double.toString(lng).replace(',', '.');
        String uriString = String.format("https://api.opentripmap.com/0.1/en/places/radius?radius=%d&lon=%s&lat=%S&apikey=%s",
                radius,
                lngS,
                latS,
                key);

        URI uri = URI.create(uriString);
        return APIUtils.doAndParseRequest(uri, OtmPlacesResponse.class, "ERROR: Status code of Open Trip map (places) response isn't 200");

    }

    public static CompletableFuture<OtmPlaceInfo> getInfoAboutPlaceOrNull(String xid, String key) {
        String uriString = String.format("https://api.opentripmap.com/0.1/en/places/xid/%s?apikey=%s",
                xid,
                key);

        URI uri = URI.create(uriString);
        return APIUtils.doAndParseRequest(uri, OtmPlaceInfo.class, "ERROR: Status code of Open Trip map (info) response isn't 200!");
    }
}
