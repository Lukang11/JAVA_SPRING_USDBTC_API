package com.example.demo.Currency;

import com.example.demo.DTO.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public List<Currency> getAllCurrency(){
        return currencyService.getAllCurrency();
    }
    @PostMapping
    public Currency addCurrency(@RequestBody Currency currency){
        return currencyService.addCurrency(currency);
    }
    @GetMapping("/fetch")
    public Object fetchCurrency(){
        return currencyService.fetchCurrency();
    }

}
