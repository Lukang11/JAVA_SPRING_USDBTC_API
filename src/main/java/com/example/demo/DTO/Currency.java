package com.example.demo.DTO;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String pairName;
    private float price;
    private LocalDateTime date;

    public String getPairName() {
        return pairName;
    }

    public void setPairName(String pairName) {
        this.pairName = pairName;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public float getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    @Override
    public String toString(){
        return "Currency{" +
                "id=" + id +
                ", pairName='" + pairName + '\'' +
                ", price='" + price + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
