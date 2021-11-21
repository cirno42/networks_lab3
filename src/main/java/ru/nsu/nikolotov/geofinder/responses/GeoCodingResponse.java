package ru.nsu.nikolotov.geofinder.responses;

import lombok.Getter;

import java.util.ArrayList;

public class GeoCodingResponse {
    @Getter
    private ArrayList<Address> hits;
}
