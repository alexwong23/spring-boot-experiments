package com.example.experiments.model.Item;

import java.util.Objects;

public class DecorItem implements Item {
    private String name;
    private String category;
    private Double price;

    public DecorItem() {
        this.category = "Decor";
    }

    public DecorItem(String name, Double price) {
        this();
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "\nItem Name: " + this.name +
                "\nItem Category: " + this.category +
                "\nItem Price: " + this.price;
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
}
