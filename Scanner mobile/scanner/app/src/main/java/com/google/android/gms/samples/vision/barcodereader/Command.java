package com.google.android.gms.samples.vision.barcodereader;

/**
 * Created by Hamza Mounir on 01-12-16.
 */
public class Command {

    private String id, date, price, payed;

    public Command(String id, String date, String price, String payed) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.payed = payed;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Command{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", price='" + price + '\'' +
                ", payed='" + payed + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }
}
