/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.eo;

import java.io.Serializable;
import java.util.Collection;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sebastianPc
 */
@Entity
@Table(name = "estudiante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e"),
    @NamedQuery(name = "Estudiante.findByIdEstudiante", query = "SELECT e FROM Estudiante e WHERE e.idEstudiante = :idEstudiante")})
public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEstudiante")
    private Integer idEstudiante;
    @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario usuarioidUsuario;
    @JoinColumn(name = "Acudiente_idAcudiente", referencedColumnName = "idAcudiente")
    @ManyToOne(optional = false)
    private Acudiente acudienteidAcudiente;
    @JoinColumn(name = "Curso_idCurso", referencedColumnName = "idCurso")
    @ManyToOne(optional = false)
    private Curso cursoidCurso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudianteidEstudiante")
    private Collection<Observador> observadorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudianteidEstudiante")
    private Collection<Regitroasistencia> regitroasistenciaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudianteidEstudiante")
    private Collection<Nota> notaCollection;

    public Estudiante() {
    }

    public Estudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Usuario getUsuarioidUsuario() {
        return usuarioidUsuario;
    }

    public void setUsuarioidUsuario(Usuario usuarioidUsuario) {
        this.usuarioidUsuario = usuarioidUsuario;
    }

    public Acudiente getAcudienteidAcudiente() {
        return acudienteidAcudiente;
    }

    public void setAcudienteidAcudiente(Acudiente acudienteidAcudiente) {
        this.acudienteidAcudiente = acudienteidAcudiente;
    }

    public Curso getCursoidCurso() {
        return cursoidCurso;
    }

    public void setCursoidCurso(Curso cursoidCurso) {
        this.cursoidCurso = cursoidCurso;
    }

    @XmlTransient
    public Collection<Observador> getObservadorCollection() {
        return observadorCollection;
    }

    public void setObservadorCollection(Collection<Observador> observadorCollection) {
        this.observadorCollection = observadorCollection;
    }

    @XmlTransient
    public Collection<Regitroasistencia> getRegitroasistenciaCollection() {
        return regitroasistenciaCollection;
    }

    public void setRegitroasistenciaCollection(Collection<Regitroasistencia> regitroasistenciaCollection) {
        this.regitroasistenciaCollection = regitroasistenciaCollection;
    }

    @XmlTransient
    public Collection<Nota> getNotaCollection() {
        return notaCollection;
    }

    public void setNotaCollection(Collection<Nota> notaCollection) {
        this.notaCollection = notaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstudiante != null ? idEstudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.idEstudiante == null && other.idEstudiante != null) || (this.idEstudiante != null && !this.idEstudiante.equals(other.idEstudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return usuarioidUsuario.getNombres()+" "+usuarioidUsuario.getApellidos();
    }
    
}
