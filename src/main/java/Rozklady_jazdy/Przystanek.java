package Rozklady_jazdy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasa odpowiadajaca za ca³¹ logikê pobierania listy przystanków i odjazdów
 */
public class Przystanek {

    /**
     * Metoda zwracaj¹ca listê odjazdów z danego przystanku
     *
     * @param id przystanku dla ktorego chemy zobaczyc odjazd
     * @return zwraca odjazdy z danego przystanku
     *
     */
    public List<String> pokazOdjazdy(int id) {
        String przystanekId = Integer.toString(id);
        List<String> odjazdyInfo = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            URL url = new URL("https://ckan2.multimediagdansk.pl/departures?stopId=" + przystanekId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                System.out.println("Error in connecting: " + responseCode);
            } else {
                //Odczytanie wartoœci z strumienia i zapisanie ich do StringBuildera
                InputStream stream = conn.getInputStream();
                Scanner in = new Scanner(stream, StandardCharsets.UTF_8);
                StringBuilder info = new StringBuilder();
                while (in.hasNextLine()) {
                    info.append(in.nextLine());
                }
                in.close();
                //Czytnik jêzyka JSON
                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(info.toString());
                JSONArray departures = (JSONArray) responseObject.get("departures");

                if (departures != null) {
                    for (Object departureObject : departures) {
                        JSONObject departure = (JSONObject) departureObject;
                        //Zczytanie odpowiednich wartoœci z ka¿dego obiektu w JSONie
                        String headsign = (String) departure.get("headsign");
                        String nr = (String) departure.get("routeShortName");
                        String estimatedTime = (String) departure.get("estimatedTime");
                        estimatedTime = estimatedTime.substring(11);

                        //Poprawienie godziny bo ta z API jest cofnieta o 2h
                        int hourCorrection = Integer.parseInt(estimatedTime.substring(0, 2)) + 2;
                        estimatedTime = hourCorrection + estimatedTime.substring(2, estimatedTime.length() - 1);
                        //Obliczanie za ile minut przyjedzie autobus
                        LocalTime scheduledDepartureTime = LocalTime.parse(estimatedTime, formatter);
                        LocalTime currentTime = LocalTime.now();
                        Duration duration = Duration.between(currentTime, scheduledDepartureTime);
                        long minutesUntilDeparture = duration.toMinutes();
                        if (minutesUntilDeparture == 0) {
                            odjazdyInfo.add(nr + ", " + headsign + ", Odjazd za: " + "teraz\n");
                        } else {
                            odjazdyInfo.add(nr + ", " + headsign + ", Odjazd za: " + minutesUntilDeparture + " min\n");
                        }
                    }
                } else {
                    odjazdyInfo.add("Nie znaleziono odjazdów");
                }
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return odjazdyInfo;
    }

    /**
     * Metoda odpowiadaj¹ca za zwrócenie listy przystanków
     *
     * @return zwraca ca³a listê przystanków ZTM
     *
     */
    public List<String> pokazPrzystanki() {
        List<String> przystankiInfo = new ArrayList<>();

        try {
            URL url = new URL("https://ckan.multimediagdansk.pl/dataset/c24aa637-3619-4dc2-a171-a23eec8f2172/resource/d3e96eb6-25ad-4d6c-8651-b1eb39155945/download/stopsingdansk.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                System.out.println("Error: " + responseCode);
            } else {
                //Odczytanie wartoœci z strumienia i zapisanie ich do StringBuildera
                InputStream stream = conn.getInputStream();
                Scanner in = new Scanner(stream, StandardCharsets.UTF_8);
                StringBuilder info = new StringBuilder();
                while (in.hasNextLine()) {
                    info.append(in.nextLine());
                }
                in.close();
                //Czytnik JSON
                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(info.toString());
                JSONArray stops = (JSONArray) responseObject.get("stops");

                if (stops != null) {
                    for (Object stopObject : stops) {
                        JSONObject stop = (JSONObject) stopObject;
                        long zoneId = (long) stop.get("zoneId");
                        //Odfiltrowanie listy przystanków po zoneID, gdzie dla Gdañska wynosi ono 1
                        if (zoneId == 1) {
                            long stopId = (long) stop.get("stopId");
                            String name = (String) stop.get("stopName");
                            String code = (String) stop.get("stopCode");
                            przystankiInfo.add("[" + String.valueOf(stopId) + "] " + name + " " + code + "\n");
                        }
                    }
                } else {
                    przystankiInfo.add("Nie znaleziono odjazdów");
                }
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return przystankiInfo;
    }

}
