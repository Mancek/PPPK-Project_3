/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Reservation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Manuel
 */
public class ReservationViewModel {
    private final Reservation reservation;

    public ReservationViewModel(Reservation reservation) {
        if (reservation == null) {
            reservation = new Reservation(0, 0, 0, "", "");
        }
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public IntegerProperty getIdReservationProperty() {
        return new SimpleIntegerProperty(reservation.getIdReservation());
    }
    
    public IntegerProperty getHotelIdProperty() {
        return new SimpleIntegerProperty(reservation.getHotelID());
    }
    
    public IntegerProperty getPersonIdProperty() {
        return new SimpleIntegerProperty(reservation.getPersonID());
    }

    public StringProperty getFromDateProperty() {
        return new SimpleStringProperty(reservation.getFromDate());
    }

    public StringProperty getToDateProperty() {
        return new SimpleStringProperty(reservation.getToDate());
    }
}
