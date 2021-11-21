package ru.nsu.nikolotov.geofinder.api;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class APIKeys {
    @Setter
    @Getter
    String geoCodingAPIKey;

    @Setter
    @Getter
    String openWeatherAPIKey;

    @Getter
    @Setter
    String openTripMapAPIKey;

    public APIKeys(String propPath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propPath));
            geoCodingAPIKey = properties.getProperty("GraphHopper");
            openWeatherAPIKey = properties.getProperty("OpenWeatherMap");
            openTripMapAPIKey = properties.getProperty("OpenTripMap");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: File with api keys wasn't found!");
        }
    }
}

//Add more constructors