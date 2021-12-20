package com.example.experiments.model.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

public class DecorItem implements Item {
    private String name;
    private String category;
    private Double price;
    private Manufacturer manufacturer;

    private static Logger log = LoggerFactory.getLogger(DecorItem.class);

    public DecorItem() {
        this.category = "Decor";
        log.info("Decor Bean Instantiated!");
    }

    @Autowired
    public DecorItem(String name, Double price, Manufacturer manufacturer) {
        this();
        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "DecorItem{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", manufacturer=" + manufacturer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DecorItem decorItem = (DecorItem) o;
        return Objects.equals(name, decorItem.name) && Objects.equals(category, decorItem.category) && Objects.equals(price, decorItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, price);
    }

    public String getName() {
        return this.name;
    }
    public String getCategory() {
        return this.category;
    }
    public Double getPrice() {
        return this.price;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
}
