package com.google.android.gms.samples.vision.barcodereader;

/**
 * Created by Hamza Mounir on 01-12-16.
 */
public class Line {

    private String nom, image, price, quantity;

    public Line(String nom, String image, String price, String quantity) {
        this.image = image;
        this.nom = nom;
        this.price = price;
        this.quantity = quantity;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
