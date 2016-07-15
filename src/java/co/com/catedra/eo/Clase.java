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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sebastianPc
 */
@Entity
@Table(name = "clase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clase.findAll", query = "SELECT c FROM Clase c"),
    @NamedQuery(name = "Clase.findByIdClase", query = "SELECT c FROM Clase c WHERE c.idClase = :idClase"),
    @NamedQuery(name = "Clase.findByDia", query = "SELECT c FROM Clase c WHERE c.dia = :dia"),
    @NamedQuery(name = "Clase.findByHora", query = "SELECT c FROM Clase c WHERE c.hora = :hora")})
public class Clase implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idClase")
    private Integer idClase;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Dia")
    private String dia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @JoinColumn(name = "Profesor_idProfesor", referencedColumnName = "idProfesor")
    @ManyToOne(optional = false)
    private Profesor profesoridProfesor;
    @JoinColumn(name = "Curso_idCurso", referencedColumnName = "idCurso")
    @ManyToOne(optional = false)
    private Curso cursoidCurso;
    @JoinColumn(name = "Asignatura_idAsignatura", referencedColumnName = "idAsignatura")
    @ManyToOne(optional = false)
    private Asignatura asignaturaidAsignatura;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claseidClase")
    private Collection<Regitroasistencia> regitroasistenciaCollection;

    public Clase() {
    }

    public Clase(Integer idClase) {
        this.idClase = idClase;
    }

    public Clase(Integer idClase, String dia, Date hora) {
        this.idClase = idClase;
        this.dia = dia;
        this.hora = hora;
    }

    public Integer getIdClase() {
        return idClase;
    }

    public void setIdClase(Integer idClase) {
        this.idClase = idClase;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Profesor getProfesoridProfesor() {
        return profesoridProfesor;
    }

    public void setProfesoridProfesor(Profesor profesoridProfesor) {
        this.profesoridProfesor = profesoridProfesor;
    }

    public Curso getCursoidCurso() {
        return cursoidCurso;
    }

    public void setCursoidCurso(Curso cursoidCurso) {
        this.cursoidCurso = cursoidCurso;
    }

    public Asignatura getAsignaturaidAsignatura() {
        return asignaturaidAsignatura;
    }

    public void setAsignaturaidAsignatura(Asignatura asignaturaidAsignatura) {
        this.asignaturaidAsignatura = asignaturaidAsignatura;
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
        hash += (idClase != null ? idClase.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clase)) {
            return false;
        }
        Clase other = (Clase) object;
        if ((this.idClase == null && other.idClase != null) || (this.idClase != null && !this.idClase.equals(other.idClase))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return dia +" "+asignaturaidAsignatura.getNomAsignatura();
    }
    
}
