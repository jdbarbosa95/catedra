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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sebastianPc
 */
@Entity
@Table(name = "nota")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nota.findAll", query = "SELECT n FROM Nota n"),
    @NamedQuery(name = "Nota.findByIdNota", query = "SELECT n FROM Nota n WHERE n.idNota = :idNota"),
    @NamedQuery(name = "Nota.findByValoracion", query = "SELECT n FROM Nota n WHERE n.valoracion = :valoracion"),
    @NamedQuery(name = "Nota.findByFechaRegistro", query = "SELECT n FROM Nota n WHERE n.fechaRegistro = :fechaRegistro")})
public class Nota implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idNota")
    private Integer idNota;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Valoracion")
    private int valoracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaRegistro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @JoinColumn(name = "Estudiante_idEstudiante", referencedColumnName = "idEstudiante")
    @ManyToOne(optional = false)
    private Estudiante estudianteidEstudiante;
    @JoinColumn(name = "Periodo_idPeriodo", referencedColumnName = "idPeriodo")
    @ManyToOne(optional = false)
    private Periodo periodoidPeriodo;
    @JoinColumn(name = "Logro_idLogro", referencedColumnName = "idLogro")
    @ManyToOne(optional = false)
    private Logro logroidLogro;
    @JoinColumn(name = "Asignatura_idAsignatura", referencedColumnName = "idAsignatura")
    @ManyToOne(optional = false)
    private Asignatura asignaturaidAsignatura;

    public Nota() {
    }

    public Nota(Integer idNota) {
        this.idNota = idNota;
    }

    public Nota(Integer idNota, int valoracion, Date fechaRegistro) {
        this.idNota = idNota;
        this.valoracion = valoracion;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
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

    public Periodo getPeriodoidPeriodo() {
        return periodoidPeriodo;
    }

    public void setPeriodoidPeriodo(Periodo periodoidPeriodo) {
        this.periodoidPeriodo = periodoidPeriodo;
    }

    public Logro getLogroidLogro() {
        return logroidLogro;
    }

    public void setLogroidLogro(Logro logroidLogro) {
        this.logroidLogro = logroidLogro;
    }

    public Asignatura getAsignaturaidAsignatura() {
        return asignaturaidAsignatura;
    }

    public void setAsignaturaidAsignatura(Asignatura asignaturaidAsignatura) {
        this.asignaturaidAsignatura = asignaturaidAsignatura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNota != null ? idNota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nota)) {
            return false;
        }
        Nota other = (Nota) object;
        if ((this.idNota == null && other.idNota != null) || (this.idNota != null && !this.idNota.equals(other.idNota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.catedra.oe.Nota[ idNota=" + idNota + " ]";
    }
    
}
