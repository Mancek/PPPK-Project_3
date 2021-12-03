 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.Repository;
import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Hotel;
import hr.algebra.model.Person;
import hr.algebra.model.Reservation;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class HibernateRepository implements Repository {

    @Override
    public int addPerson(Person data) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // in order to use in in transaction scope, we must create new Person that with data details
            Person person = new Person(data);
            em.persist(person);
            em.getTransaction().commit();
            return person.getIdPerson();
        }
    }

    @Override
    public void deletePerson(Person person) throws Exception {
        RepositoryFactory.getRepository().getReservations().forEach(reservation -> {
        if(reservation.getPersonID() == person.getIdPerson()) {
            try {
                RepositoryFactory.getRepository().deleteReservation(reservation);
            } catch (Exception ex) {
                Logger.getLogger(HibernateRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        });
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // person comes from earlier transaction, so we must merge it into this one
            em.remove(em.contains(person) ? person : em.merge(person));
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Person> getPeople() throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            return entityManager.get().createNamedQuery(HibernateFactory.SELECT_PEOPLE).getResultList();
        }
    }

    @Override
    public Person getPerson(int idPerson) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            return em.find(Person.class, idPerson);
        }
    }

    @Override
    public void updatePerson(Person person) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // automatically persists
            em.find(Person.class, person.getIdPerson()).updateDeatils(person);
            em.getTransaction().commit();            
        }
    }
    
    @Override
    public int addHotel(Hotel data) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // in order to use in in transaction scope, we must create new Person that with data details
            Hotel hotel = new Hotel(data);
            em.persist(hotel);
            em.getTransaction().commit();
            return hotel.getIdHotel();
        }
    }

    @Override
    public void deleteHotel(Hotel hotel) throws Exception {
        RepositoryFactory.getRepository().getReservations().forEach(reservation -> {
        if(reservation.getHotelID() == hotel.getIdHotel()) {
            try {
                RepositoryFactory.getRepository().deleteReservation(reservation);
            } catch (Exception ex) {
                Logger.getLogger(HibernateRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        });
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // person comes from earlier transaction, so we must merge it into this one
            em.remove(em.contains(hotel) ? hotel : em.merge(hotel));
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Hotel> getHotels() throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            return entityManager.get().createNamedQuery(HibernateFactory.SELECT_HOTELS).getResultList();
        }
    }

    @Override
    public Hotel getHotel(int idHotel) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            return em.find(Hotel.class, idHotel);
        }
    }

    @Override
    public void updateHotel(Hotel hotel) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // automatically persists
            em.find(Hotel.class, hotel.getIdHotel()).updateDeatils(hotel);
            em.getTransaction().commit();            
        }
    }
    
    @Override
    public int addReservation(Reservation data) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // in order to use in in transaction scope, we must create new Person that with data details
            Reservation reservation = new Reservation(data);
            em.persist(reservation);
            em.getTransaction().commit();
            return reservation.getIdReservation();
        }
    }

    @Override
    public void deleteReservation(Reservation reservation) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // person comes from earlier transaction, so we must merge it into this one
            em.remove(em.contains(reservation) ? reservation : em.merge(reservation));
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Reservation> getReservations() throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            return entityManager.get().createNamedQuery(HibernateFactory.SELECT_RESERVATIONS).getResultList();
        }
    }

    @Override
    public Reservation getReservation(int idReservation) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            return em.find(Reservation.class, idReservation);
        }
    }

    @Override
    public void updateReservation(Reservation reservation) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // automatically persists
            em.find(Reservation.class, reservation.getIdReservation()).updateDeatils(reservation);
            em.getTransaction().commit();            
        }
    }

    @Override
    public void release() {
        HibernateFactory.release();
    }

}
