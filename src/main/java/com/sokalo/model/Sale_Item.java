package com.sokalo.model;

public class Sale_Item {

    private int saleID;
    private int itemID;
    private int quantitySold;
    private double sellingPriceAtSale;

    public Sale_Item(int saleID, int itemID, int quantitySold, double sellingPriceAtSale) {
        this.saleID = saleID;
        this.itemID = itemID;
        this.quantitySold = quantitySold;
        this.sellingPriceAtSale = sellingPriceAtSale;
    }

    // --- Getters and Setters ---

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getSellingPriceAtSale() {
        return sellingPriceAtSale;
    }

    public void setSellingPriceAtSale(double sellingPriceAtSale) {
        this.sellingPriceAtSale = sellingPriceAtSale;
    }
}
