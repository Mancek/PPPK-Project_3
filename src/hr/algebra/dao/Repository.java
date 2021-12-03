/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.model.Hotel;
import hr.algebra.model.Person;
import hr.algebra.model.Reservation;
import java.util.List;

/**
 *
 * @author Manuel
 */
public interface Repository {

    int addPerson(Person data) throws Exception;
    void deletePerson(Person person) throws Exception;
    List<Person> getPeople() throws Exception;
    Person getPerson(int idPerson) throws Exception;
    void updatePerson(Person person) throws Exception;
    
    int addHotel(Hotel data) throws Exception;
    void deleteHotel(Hotel hotel) throws Exception;
    List<Hotel> getHotels() throws Exception;
    Hotel getHotel(int idHotel) throws Exception;
    void updateHotel(Hotel hotel) throws Exception;
    
    int addReservation(Reservation data) throws Exception;
    void deleteReservation(Reservation reservation) throws Exception;
    List<Reservation> getReservations() throws Exception;
    Reservation getReservation(int idReservation) throws Exception;
    void updateReservation(Reservation reservation) throws Exception;
    
    default void release() throws Exception{};
}
