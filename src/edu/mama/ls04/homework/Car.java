package edu.mama.ls04.homework;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 使用Jackson的JSON相关注解
 */
@JsonPropertyOrder({"model", "brand", "seat", "price", "date"})
public class Car {
    //品牌
    private String brand;
    //型号
    private String model;
    //座数
    @JsonProperty("seatNum")
    private int seat;
    //上市时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date date;
    //售价
    private BigDecimal price;

    public Car() {
    }

    public Car(String brand, String model, int seat, Date date, BigDecimal price) {
        this.brand = brand;
        this.model = model;
        this.seat = seat;
        this.date = date;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
