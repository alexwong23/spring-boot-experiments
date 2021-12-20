package com.example.experiments.model.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class ConsumableItem implements Item {
    private String name;
    private String category;
    private Double price;
    private Manufacturer manufacturer;

    private static Logger log = LoggerFactory.getLogger(ConsumableItem.class);

    public ConsumableItem() {
        this.category = "Consumable";
        log.info("Consumable Bean Instantiated!");
    }

    @Autowired // NOTE : Constructor Injection - items.xml file
    public ConsumableItem(String name, Double price, Manufacturer manufacturer) {
        this(); // constructor chaining
        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "ConsumableItem{" +
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
        ConsumableItem that = (ConsumableItem) o;
        return Objects.equals(name, that.name) && Objects.equals(category, that.category) && Objects.equals(price, that.price);
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

