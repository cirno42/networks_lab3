package ru.nsu.nikolotov.geofinder.responses;

import lombok.Getter;

import java.util.ArrayList;

public class OtmPlacesResponse {
    @Getter
    ArrayList<OtmFeature> features;

    public static class OtmFeature {
        @Getter
        String type;
        @Getter
        String id;
        @Getter
        OtmProperties properties;
    }
    public static class OtmProperties {
        @Getter
        String xid;
        @Getter
        String name;
        @Getter
        double dist;
        @Getter
        int rate;
        @Getter
        String kinds;
    }
}
