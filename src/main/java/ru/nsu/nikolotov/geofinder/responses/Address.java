package ru.nsu.nikolotov.geofinder.responses;

import lombok.Getter;
import lombok.Setter;

public class Address {
    //@JsonProperty("point")

    @Getter
    private Point point;
    @Getter
    @Setter
    private String osm_id;

    @Getter
    @Setter
    private String osm_type;

    @Getter
    @Setter
    private String osm_key;

    @Getter
    @Setter
    private String osm_value;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String country;

    @Getter
    @Setter
    private String city;

    @Getter @Setter
    private String state;

    @Getter
    @Setter
    private String street;


    @Getter
    @Setter
    private String postcode;

    public String getAddressString() {
        StringBuilder stringBuilder = new StringBuilder("");
        if (country != null) {
            stringBuilder.append(country).append(",");
        }
        if (city != null) {
            stringBuilder.append(city).append(",");
        }
        if (state != null) {
            stringBuilder.append(state).append(",");
        }
        if (street != null) {
            stringBuilder.append(street).append(",");
        }
        if (name != null) {
            stringBuilder.append(name).append(",");
        }
        if (postcode != null) {
            stringBuilder.append(postcode);
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }
}
