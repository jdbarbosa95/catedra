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
@Table(name = "regitroasistencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Regitroasistencia.findAll", query = "SELECT r FROM Regitroasistencia r"),
    @NamedQuery(name = "Regitroasistencia.findByIdRegitroAsistencia", query = "SELECT r FROM Regitroasistencia r WHERE r.idRegitroAsistencia = :idRegitroAsistencia"),
    @NamedQuery(name = "Regitroasistencia.findByAsistio", query = "SELECT r FROM Regitroasistencia r WHERE r.asistio = :asistio"),
    @NamedQuery(name = "Regitroasistencia.findByFecha", query = "SELECT r FROM Regitroasistencia r WHERE r.fecha = :fecha")})
public class Regitroasistencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRegitroAsistencia")
    private Integer idRegitroAsistencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "Asistio")
    private String asistio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "Justificacion_idJustificacion", referencedColumnName = "idJustificacion")
    @ManyToOne
    private Justificacion justificacionidJustificacion;
    @JoinColumn(name = "Clase_idClase", referencedColumnName = "idClase")
    @ManyToOne(optional = false)
    private Clase claseidClase;
    @JoinColumn(name = "Estudiante_idEstudiante", referencedColumnName = "idEstudiante")
    @ManyToOne(optional = false)
    private Estudiante estudianteidEstudiante;

    public Regitroasistencia() {
    }

    public Regitroasistencia(Integer idRegitroAsistencia) {
        this.idRegitroAsistencia = idRegitroAsistencia;
    }

    public Regitroasistencia(Integer idRegitroAsistencia, String asistio, Date fecha) {
        this.idRegitroAsistencia = idRegitroAsistencia;
        this.asistio = asistio;
        this.fecha = fecha;
    }

    public Integer getIdRegitroAsistencia() {
        return idRegitroAsistencia;
    }

    public void setIdRegitroAsistencia(Integer idRegitroAsistencia) {
        this.idRegitroAsistencia = idRegitroAsistencia;
    }

    public String getAsistio() {
        return asistio;
    }

    public void setAsistio(String asistio) {
        this.asistio = asistio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Justificacion getJustificacionidJustificacion() {
        return justificacionidJustificacion;
    }

    public void setJustificacionidJustificacion(Justificacion justificacionidJustificacion) {
        this.justificacionidJustificacion = justificacionidJustificacion;
    }

    public Clase getClaseidClase() {
        return claseidClase;
    }

    public void setClaseidClase(Clase claseidClase) {
        this.claseidClase = claseidClase;
    }

    public Estudiante getEstudianteidEstudiante() {
        return estudianteidEstudiante;
    }

    public void setEstudianteidEstudiante(Estudiante estudianteidEstudiante) {
        this.estudianteidEstudiante = estudianteidEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRegitroAsistencia != null ? idRegitroAsistencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Regitroasistencia)) {
            return false;
        }
        Regitroasistencia other = (Regitroasistencia) object;
        if ((this.idRegitroAsistencia == null && other.idRegitroAsistencia != null) || (this.idRegitroAsistencia != null && !this.idRegitroAsistencia.equals(other.idRegitroAsistencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.catedra.oe.Regitroasistencia[ idRegitroAsistencia=" + idRegitroAsistencia + " ]";
    }
    
}
