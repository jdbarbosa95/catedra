/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.eo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sebastianPc
 */
@Entity
@Table(name = "observador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observador.findAll", query = "SELECT o FROM Observador o"),
    @NamedQuery(name = "Observador.findByIdObservador", query = "SELECT o FROM Observador o WHERE o.idObservador = :idObservador"),
    @NamedQuery(name = "Observador.findByFechaRegistro", query = "SELECT o FROM Observador o WHERE o.fechaRegistro = :fechaRegistro")})
public class Observador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idObservador")
    private Integer idObservador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaRegistro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @JoinColumn(name = "Estudiante_idEstudiante", referencedColumnName = "idEstudiante")
    @ManyToOne(optional = false)
    private Estudiante estudianteidEstudiante;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "observadoridObservador")
    private Collection<Observacion> observacionCollection;

    public Observador() {
    }

    public Observador(Integer idObservador) {
        this.idObservador = idObservador;
    }

    public Observador(Integer idObservador, Date fechaRegistro) {
        this.idObservador = idObservador;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdObservador() {
        return idObservador;
    }

    public void setIdObservador(Integer idObservador) {
        this.idObservador = idObservador;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Estudiante getEstudianteidEstudiante() {
        return estudianteidEstudiante;
    }

    public void setEstudianteidEstudiante(Estudiante estudianteidEstudiante) {
        this.estudianteidEstudiante = estudianteidEstudiante;
    }

    @XmlTransient
    public Collection<Observacion> getObservacionCollection() {
        return observacionCollection;
    }

    public void setObservacionCollection(Collection<Observacion> observacionCollection) {
        this.observacionCollection = observacionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idObservador != null ? idObservador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Observador)) {
            return false;
        }
        Observador other = (Observador) object;
        if ((this.idObservador == null && other.idObservador != null) || (this.idObservador != null && !this.idObservador.equals(other.idObservador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.catedra.oe.Observador[ idObservador=" + idObservador + " ]";
    }
    
}
