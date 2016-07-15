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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "logro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logro.findAll", query = "SELECT l FROM Logro l"),
    @NamedQuery(name = "Logro.findByIdLogro", query = "SELECT l FROM Logro l WHERE l.idLogro = :idLogro")})
public class Logro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idLogro")
    private Integer idLogro;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "Indicador")
    private String indicador;
    @JoinColumn(name = "Asignatura_idAsignatura", referencedColumnName = "idAsignatura")
    @ManyToOne(optional = false)
    private Asignatura asignaturaidAsignatura;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "logroidLogro")
    private Collection<Nota> notaCollection;

    public Logro() {
    }

    public Logro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public Logro(Integer idLogro, String indicador) {
        this.idLogro = idLogro;
        this.indicador = indicador;
    }

    public Integer getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public Asignatura getAsignaturaidAsignatura() {
        return asignaturaidAsignatura;
    }

    public void setAsignaturaidAsignatura(Asignatura asignaturaidAsignatura) {
        this.asignaturaidAsignatura = asignaturaidAsignatura;
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
        hash += (idLogro != null ? idLogro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Logro)) {
            return false;
        }
        Logro other = (Logro) object;
        if ((this.idLogro == null && other.idLogro != null) || (this.idLogro != null && !this.idLogro.equals(other.idLogro))) {
            return false;
        }
        return true;
    }

    @Override
   public String toString() {
        return indicador;
    }
    
    
}
