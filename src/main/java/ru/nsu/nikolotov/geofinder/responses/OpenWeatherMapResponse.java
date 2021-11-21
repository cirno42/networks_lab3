package ru.nsu.nikolotov.geofinder.responses;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;

public class OpenWeatherMapResponse {
    @Getter
    ArrayList<Weather> weather;

    @Getter
    @JsonAlias("main")
    private MainInfo mainInfo;

    @Getter
    Wind wind;


    public static class Weather {
        @Getter
        int id;
        @Getter
        String description;
    }
    public static class MainInfo {
        @Getter
        private double temp;
        @Getter
        private double feels_like;
        @Getter
        private double humidity;
    }
    public static class Wind {
        @Getter
        private double speed;
        @Getter
        private double deg;
    }

}
