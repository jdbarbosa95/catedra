/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.eo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sebastianPc
 */
@Entity
@Table(name = "observacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observacion.findAll", query = "SELECT o FROM Observacion o"),
    @NamedQuery(name = "Observacion.findByIdObservacion", query = "SELECT o FROM Observacion o WHERE o.idObservacion = :idObservacion"),
    @NamedQuery(name = "Observacion.findByFecha", query = "SELECT o FROM Observacion o WHERE o.fecha = :fecha")})
public class Observacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idObservacion")
    private Integer idObservacion;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "Observacion")
    private String observacion;
    @Lob
    @Size(max = 65535)
    @Column(name = "Descargos")
    private String descargos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "Observador_idObservador", referencedColumnName = "idObservador")
    @ManyToOne(optional = false)
    private Observador observadoridObservador;

    public Observacion() {
    }

    public Observacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    public Observacion(Integer idObservacion, String observacion, Date fecha) {
        this.idObservacion = idObservacion;
        this.observacion = observacion;
        this.fecha = fecha;
    }

    public Integer getIdObservacion() {
        return idObservacion;
    }

    public void setIdObservacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDescargos() {
        return descargos;
    }

    public void setDescargos(String descargos) {
        this.descargos = descargos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Observador getObservadoridObservador() {
        return observadoridObservador;
    }

    public void setObservadoridObservador(Observador observadoridObservador) {
        this.observadoridObservador = observadoridObservador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idObservacion != null ? idObservacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Observacion)) {
            return false;
        }
        Observacion other = (Observacion) object;
        if ((this.idObservacion == null && other.idObservacion != null) || (this.idObservacion != null && !this.idObservacion.equals(other.idObservacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.catedra.oe.Observacion[ idObservacion=" + idObservacion + " ]";
    }
    
}
