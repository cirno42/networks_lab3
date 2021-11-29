package ru.nsu.nikolotov.geofinder.app;

import ru.nsu.nikolotov.geofinder.api.APIKeys;
import ru.nsu.nikolotov.geofinder.api.GraphHopperAPI;
import ru.nsu.nikolotov.geofinder.api.OpenTripMapAPI;
import ru.nsu.nikolotov.geofinder.api.OpenWeatherMapAPI;
import ru.nsu.nikolotov.geofinder.layout.VerticalLayout;
import ru.nsu.nikolotov.geofinder.responses.*;
import ru.nsu.nikolotov.geofinder.responses.Point;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class App {
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 800;
    private static final int DEFAULT_RADIUS_IN_METERS = 1000;
    private static final String DEFAULT_PATH_TO_KEYS = "./api_keys.prop";
    private final APIKeys keys = new APIKeys(DEFAULT_PATH_TO_KEYS);

    private final ArrayList<Point> geoPoints = new ArrayList<>();
    private final ArrayList<String> xids = new ArrayList<>();

    private final JFrame frame = new JFrame("Geofinder");
    private final JTextField placeTextField = new JTextField("");


    private final JButton findButton = new JButton("Find!");
    private final JButton getInfoAboutPlaceButton = new JButton("Get info");
    private final JButton getMoreInfoAboutPlaceButton = new JButton("More info about place");


    private final DefaultListModel<String> addressesListModel = new DefaultListModel();
    private final JList<String> addressesList = new JList<String>(addressesListModel);

    private final JLabel weatherLabel = new JLabel("");

    private final DefaultListModel<String> interestingPlacesModel = new DefaultListModel();
    private final JList<String> interestingPlacesList = new JList<String>(interestingPlacesModel);


    private final JScrollPane addressesScrollPane = new JScrollPane(addressesList);
    private final JScrollPane interestingPlacesScrollPane = new JScrollPane(interestingPlacesList);

    public App() {
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setLayout(new VerticalLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSizes();

        addressesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        interestingPlacesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addActionListeners();
        addComponents();
    }
    public void start() {
        frame.setVisible(true);
    }

    private void updateGeoCoding(GeoCodingResponse resp) {
        if (resp == null) {
            return;
        }
        addressesListModel.clear();
        geoPoints.clear();
        var addresses = resp.getHits();
        for(int i = 0; i < addresses.size(); i++) {
            addressesListModel.add(i, addresses.get(i).toString());
            geoPoints.add(i, addresses.get(i).getPoint());
        }

    }

    private void updateWeatherInfo(OpenWeatherMapResponse resp) {
        if (resp == null) {
            return;
        }
        weatherLabel.setText( "<html> Weather: " + resp.getWeather().get(0).getDescription() +
                "<br> Temperature feels like: " + resp.getMainInfo().getFeels_like() +
                "<br> Wind speed: " + resp.getWind().getSpeed() +
                "<br> Humidity: " + resp.getMainInfo().getHumidity() +
                "</html>");
    }

    private OtmPlacesResponse updateListOfInterestingPlaces(OtmPlacesResponse resp) {
        if (resp == null) {
            return null;
        }
        interestingPlacesModel.clear();
        var places = resp.getFeatures();
        if (places.size() == 0) {
            interestingPlacesModel.add(0, "No interesting places was found, try another location");
        }
        for(int i = 0; i < places.size(); i++) {
            interestingPlacesModel.add(i, i + ". " + places.get(i).getProperties().getName());
        }
        return resp;
    }

    private void getXidsOfPlaces(OtmPlacesResponse resp) {
        if (resp == null) {
            return;
        }
        xids.clear();
        var places = resp.getFeatures();
        for (int i = 0; i < places.size(); i++) {
            String xid = places.get(i).getProperties().getXid();
            xids.add(i, xid);

        }
    }

    private void showInfoAboutInterestingPlace(OtmPlaceInfo info) {
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(info.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(frame, scrollPane);
    }

    private void addComponents() {
        addressesListModel.add(0, "Please, type name of place you want to know about and click \"Find!\"");
        frame.add(placeTextField);
        frame.add(findButton);
        frame.add(addressesScrollPane);
        frame.add(getInfoAboutPlaceButton);
        frame.add(weatherLabel);
        frame.add(interestingPlacesScrollPane);
        frame.add(getMoreInfoAboutPlaceButton);
    }

    private void setSizes() {
        interestingPlacesList.setPreferredSize(new Dimension(360, 200));
        addressesList.setPreferredSize(new Dimension(360, 200));
        placeTextField.setPreferredSize(new Dimension(100, 20));
        weatherLabel.setPreferredSize(new Dimension(400, 100));
        placeTextField.setPreferredSize( new Dimension(100, 20));

    }

    private void addActionListeners() {
        findButton.addActionListener((l) -> {
            String placeName = placeTextField.getText();
            if (placeName != null) {
                GraphHopperAPI.getAddressesByNameOrNull(placeName, keys.getGeoCodingAPIKey()).
                        thenAccept(this::updateGeoCoding);
            }
        });


        getInfoAboutPlaceButton.addActionListener(l -> {
            double lat = geoPoints.get(addressesList.getSelectedIndex()).getLat();
            double lng = geoPoints.get(addressesList.getSelectedIndex()).getLng();
            OpenWeatherMapAPI.getWeatherByCordsOrNull(lat, lng, keys.getOpenWeatherAPIKey())
                    .thenAccept(this::updateWeatherInfo);
            OpenTripMapAPI.getInterestingPlacesOrNull(lat, lng, DEFAULT_RADIUS_IN_METERS, keys.getOpenTripMapAPIKey())
                    .thenApply(this::updateListOfInterestingPlaces)
                    .thenAccept(this::getXidsOfPlaces);


        });

        getMoreInfoAboutPlaceButton.addActionListener(l -> {
            int index = interestingPlacesList.getSelectedIndex();
            if (xids.size() > index) {
                String xid = xids.get(index);
                OpenTripMapAPI.getInfoAboutPlaceOrNull(xid, keys.getOpenTripMapAPIKey())
                        .thenAccept(this::showInfoAboutInterestingPlace);
            }
        });
    }
}
