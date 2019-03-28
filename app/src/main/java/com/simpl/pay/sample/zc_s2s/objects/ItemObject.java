package com.simpl.pay.sample.zc_s2s.objects;

public class ItemObject {
    String name;
    int quantity, amount;

    public ItemObject(String name, int quantity, int amount) {
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void addQuantity() {
        quantity++;
    }

    public void subQuantity() {
        quantity--;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAmount() {
        return amount;
    }

    public int getTotalItemPrice() {
        return amount * quantity;
    }
}