package com.example.demo.Currency;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.DTO.Currency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.*;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Value("${api.key}")
    private String apiKey;
    @Value("${api.path}")
    private String apiPath;


    public List<Currency> getAllCurrency() {
        return currencyRepository.findAll();
    }
    public Currency addCurrency(Currency currency){
        return currencyRepository.save(currency);
    }
    public Object fetchCurrency(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime previousDay = now.minusDays(1);
        String formattedPreviousDay = previousDay.format(dtf);
        Boolean canIFetch = false;
       List<Currency> list = currencyRepository.findAll();
        for (int i = 0; i < list.size() ; i++) {
            System.out.println(list.get(i).getDate());
            System.out.println(previousDay);
            int isTheSameDay = list.get(i).getDate().getDayOfMonth();
            int isTheSameMonth = list.get(i).getDate().getMonthValue();
            int isTheSameYear = list.get(i).getDate().getYear();
            int todayDay = now.getDayOfMonth();
            int todayMonth = now.getMonthValue();
            int todayYear = now.getYear();
            if(todayDay == isTheSameDay && todayMonth == isTheSameMonth && todayYear == isTheSameYear ){
                System.out.println("Record with that day already exist");
                canIFetch = false;
            }else{
                System.out.println("fetching");
                canIFetch = true;
            }
        }
        HttpResponse response = null;
        if(canIFetch){
            System.out.println("fetched");
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
            try {
                String endpoint = String
                    .format(
                            "https://api.polygon.io/v2/aggs/ticker/X:BTCUSD/range/1/day/%s/%s?apiKey=%s",
                            formattedPreviousDay,
                            formattedPreviousDay,
                            apiKey
                    );
                 URI uri = URI.create(endpoint);
                HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
                response = client.send(request,HttpResponse.BodyHandlers.ofString());
                String jsonData = (String) response.body();
                ObjectMapper objectMapper = new ObjectMapper();

                // Parse the JSON string
                JsonNode rootNode = objectMapper.readTree(jsonData);

                // Extract values for "ticker" and "vw"
                String ticker = rootNode.path("ticker").asText();
                float vw = rootNode.path("results").get(0).path("vw").floatValue();
                Currency newCurrency = new Currency();
                newCurrency.setPairName(ticker);
                newCurrency.setPrice(vw);
                newCurrency.setDate(previousDay);
                currencyRepository.save(newCurrency);


            }catch (Exception e){
            e.printStackTrace();
            }
        }
        int responseCode = response.statusCode();

        System.out.println(responseCode);
        System.out.println(response.body());

        String returnString = String.format("apikey=%s,apiPath=%s",apiKey,apiPath);
        return (String) response.body();
    }
}
