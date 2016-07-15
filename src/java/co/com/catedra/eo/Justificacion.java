/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.eo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sebastianPc
 */
@Entity
@Table(name = "justificacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Justificacion.findAll", query = "SELECT j FROM Justificacion j"),
    @NamedQuery(name = "Justificacion.findByIdJustificacion", query = "SELECT j FROM Justificacion j WHERE j.idJustificacion = :idJustificacion")})
public class Justificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idJustificacion")
    private Integer idJustificacion;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "Descargo")
    private String descargo;
    @OneToMany(mappedBy = "justificacionidJustificacion")
    private Collection<Regitroasistencia> regitroasistenciaCollection;

    public Justificacion() {
    }

    public Justificacion(Integer idJustificacion) {
        this.idJustificacion = idJustificacion;
    }

    public Justificacion(Integer idJustificacion, String descargo) {
        this.idJustificacion = idJustificacion;
        this.descargo = descargo;
    }

    public Integer getIdJustificacion() {
        return idJustificacion;
    }

    public void setIdJustificacion(Integer idJustificacion) {
        this.idJustificacion = idJustificacion;
    }

    public String getDescargo() {
        return descargo;
    }

    public void setDescargo(String descargo) {
        this.descargo = descargo;
    }

    @XmlTransient
    public Collection<Regitroasistencia> getRegitroasistenciaCollection() {
        return regitroasistenciaCollection;
    }

    public void setRegitroasistenciaCollection(Collection<Regitroasistencia> regitroasistenciaCollection) {
        this.regitroasistenciaCollection = regitroasistenciaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idJustificacion != null ? idJustificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Justificacion)) {
            return false;
        }
        Justificacion other = (Justificacion) object;
        if ((this.idJustificacion == null && other.idJustificacion != null) || (this.idJustificacion != null && !this.idJustificacion.equals(other.idJustificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.catedra.oe.Justificacion[ idJustificacion=" + idJustificacion + " ]";
    }
    
}
