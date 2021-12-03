/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.dao.sql.HibernateFactory;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Manuel
 */
@Entity
@Table(name = "Reservation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = HibernateFactory.SELECT_RESERVATIONS, query = "SELECT r FROM Reservation r")
})
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDReservation")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idReservation;
    @Basic(optional = false)
    @Column(name = "HotelID")
    private int hotelID;
    @Basic(optional = false)
    @Column(name = "PersonID")
    private int personID;
    @Basic(optional = false)
    @Column(name = "FromDate")
    private String fromDate;
    @Basic(optional = false)
    @Column(name = "ToDate")
    private String toDate;

    public Reservation(Integer idReservation, int hotelID, int personID, String fromDate, String toDate) {
        this.idReservation = idReservation;
        this.hotelID = hotelID;
        this.personID = personID;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Reservation() {
    }

    
    public Reservation(Reservation data) {
        updateDeatils(data);
    }

    public Integer getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Integer idReservation) {
        this.idReservation = idReservation;
    }

    public int getHotelID() {
        return hotelID;
    }

    public void setHotelID(int hotelID) {
        this.hotelID = hotelID;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReservation != null ? idReservation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.idReservation == null && other.idReservation != null) || (this.idReservation != null && !this.idReservation.equals(other.idReservation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.Reservation[ idReservation=" + idReservation + " ]";
    }
    
    public void updateDeatils(Reservation data) {
        this.hotelID = data.getHotelID();
        this.personID = data.getPersonID();
        this.fromDate = data.getFromDate();
        this.toDate = data.getToDate();
    }
    
    
}
