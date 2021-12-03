/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Hotel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Manuel
 */
public class HotelViewModel {
    private final Hotel hotel;

    public HotelViewModel(Hotel hotel) {
        if (hotel == null) {
            hotel = new Hotel(0, "", "", 0);
        }
        this.hotel = hotel;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public IntegerProperty getIdHotelProperty() {
        return new SimpleIntegerProperty(hotel.getIdHotel());
    }

    public StringProperty getAdressProperty() {
        return new SimpleStringProperty(hotel.getAddress());
    }

    public StringProperty getCityProperty() {
        return new SimpleStringProperty(hotel.getCity());
    }

    public IntegerProperty getStarsProperty() {
        return new SimpleIntegerProperty(hotel.getStars());
    }
}
