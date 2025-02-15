package weather_Forecast;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WeatherForecastApp {

    private static JFrame frame;
    private static JTextField locationField;
    private static JTextArea weatherDisplay;
    private static JButton fetchButton;
    private static String apiKey ="ad16552d4e8a9de24afa5404c3284425";                       // Replace your own key

    public static String fetchWeatherData(String city){
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                response.append(line);
            }
            reader.close();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.toString());
            JSONObject mainObj = (JSONObject) jsonObject.get("main");

            double temperatureCelsius = (double) mainObj.get("temp");
            int humidity = ((Number) mainObj.get("humidity")).intValue();

            // Retrieve weather description
            JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
            JSONObject weather = (JSONObject) weatherArray.get(0);
            String description = (String) weather.get("description");
        
            return "Description: " + description + "\nTemperature: " + temperatureCelsius + "°C\nHumidity: " + humidity + "%";
        } 
        catch (Exception e) {
            return "Failed to Fetch weather data. Please check your city and API key...\nError: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        frame = new JFrame("Weather Forecast App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        frame.setLayout(new FlowLayout());

        locationField = new JTextField(15);
        fetchButton = new JButton("Fetch Weather");
        weatherDisplay = new JTextArea(10,30);
        weatherDisplay.setEditable(false);

        frame.add(new JLabel("Enter City Name"));
        frame.add(locationField);
        frame.add(fetchButton);
        frame.add(weatherDisplay);

        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String city = locationField.getText();
                String weatherInfo = fetchWeatherData(city);
                weatherDisplay.setText(weatherInfo);
            }
        });

        frame.setVisible(true);
    }
}
