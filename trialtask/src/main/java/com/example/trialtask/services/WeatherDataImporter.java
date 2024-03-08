package com.example.trialtask.services;

import com.example.trialtask.objects.WeatherData;
import com.example.trialtask.repositories.WeatherDataRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Component
public class WeatherDataImporter {

    private final String weatherDataUrl = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private final RestTemplate restTemplate;
    private final WeatherDataRepository weatherDataRepository;

    /**
     * Constructs a new WeatherDataImporter with RestTemplate and WeatherDataRepository.
     * @param restTemplate RestTemplate used to make HTTP requests
     * @param weatherDataRepository repository used to save the imported weather data
     */
    public WeatherDataImporter(RestTemplate restTemplate, WeatherDataRepository weatherDataRepository) {
        this.restTemplate = restTemplate;
        this.weatherDataRepository = weatherDataRepository;
    }

    /**
     * Imports weather data from the website every hour at 15 minutes past the hour
     * Only gets data from the stations "Tallinn-Harku", "Pärnu", and "Tartu-Tõravere".
     */
    @Scheduled(cron = "${weather.import.cron}")// the frequency can be configured in the application.properties file at src/main/resources/application.properties
    public void importWeatherData() {
        try {
            String xmlData = restTemplate.getForObject(weatherDataUrl, String.class);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlData)));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("station");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String stationName = element.getElementsByTagName("name").item(0).getTextContent();

                    if (stationName.equals("Tallinn-Harku") || stationName.equals("Pärnu") || stationName.equals("Tartu-Tõravere")) {
                        String wmocode = element.getElementsByTagName("wmocode").item(0).getTextContent();
                        String airTemperatureString = element.getElementsByTagName("airtemperature").item(0).getTextContent();
                        Double airTemperature = airTemperatureString.isEmpty() ? 0.0 : Double.parseDouble(airTemperatureString);
                        String windSpeedString = element.getElementsByTagName("windspeed").item(0).getTextContent();
                        Double windSpeed = windSpeedString.isEmpty() ? 0.0 : Double.parseDouble(windSpeedString);
                        String phenomenon = element.getElementsByTagName("phenomenon").item(0).getTextContent();
                        String observationTimestampString = doc.getDocumentElement().getAttribute("timestamp");
                        Long observationTimestamp = observationTimestampString.isEmpty() ? 0L : Long.parseLong(observationTimestampString);

                        WeatherData weatherData = new WeatherData();
                        weatherData.setStationName(stationName);
                        weatherData.setWmocode(wmocode);
                        weatherData.setAirTemperature(airTemperature);
                        weatherData.setWindSpeed(windSpeed);
                        weatherData.setPhenomenon(phenomenon);
                        weatherData.setObservationTimestamp(observationTimestamp);

                        weatherDataRepository.save(weatherData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
