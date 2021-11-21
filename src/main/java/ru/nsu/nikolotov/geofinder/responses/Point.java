package ru.nsu.nikolotov.geofinder.responses;

import lombok.Getter;
import lombok.Setter;

public class Point {
    @Setter
    @Getter
    double lat;
    @Setter
    @Getter
    double lng;
}
