/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author msrik
 */
@Entity
@Table(name = "CONFIGURACIONPC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configuracionpc.findAll", query = "SELECT c FROM Configuracionpc c"),
    @NamedQuery(name = "Configuracionpc.findByIdconfiguracion", query = "SELECT c FROM Configuracionpc c WHERE c.idconfiguracion = :idconfiguracion"),
    @NamedQuery(name = "Configuracionpc.findByTipocpu", query = "SELECT c FROM Configuracionpc c WHERE c.tipocpu = :tipocpu"),
    @NamedQuery(name = "Configuracionpc.findByVelocidadcpu", query = "SELECT c FROM Configuracionpc c WHERE c.velocidadcpu = :velocidadcpu"),
    @NamedQuery(name = "Configuracionpc.findByCapacidadram", query = "SELECT c FROM Configuracionpc c WHERE c.capacidadram = :capacidadram"),
    @NamedQuery(name = "Configuracionpc.findByCapacidaddd", query = "SELECT c FROM Configuracionpc c WHERE c.capacidaddd = :capacidaddd"),
    @NamedQuery(name = "Configuracionpc.findByVelocidadtarjetagrafica", query = "SELECT c FROM Configuracionpc c WHERE c.velocidadtarjetagrafica = :velocidadtarjetagrafica"),
    @NamedQuery(name = "Configuracionpc.findByMemoriatarjetagrafica", query = "SELECT c FROM Configuracionpc c WHERE c.memoriatarjetagrafica = :memoriatarjetagrafica"),
    @NamedQuery(name = "Configuracionpc.findByPrecio", query = "SELECT c FROM Configuracionpc c WHERE c.precio = :precio")})
public class Configuracionpc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDCONFIGURACION")
    private Integer idconfiguracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "TIPOCPU")
    private String tipocpu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VELOCIDADCPU")
    private int velocidadcpu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAPACIDADRAM")
    private int capacidadram;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAPACIDADDD")
    private int capacidaddd;
    @Column(name = "VELOCIDADTARJETAGRAFICA")
    private Integer velocidadtarjetagrafica;
    @Column(name = "MEMORIATARJETAGRAFICA")
    private Integer memoriatarjetagrafica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRECIO")
    private float precio;

    public Configuracionpc() {
    }

    public Configuracionpc(Integer idconfiguracion) {
        this.idconfiguracion = idconfiguracion;
    }

    public Configuracionpc(Integer idconfiguracion, String tipocpu, int velocidadcpu, int capacidadram, int capacidaddd, float precio) {
        this.idconfiguracion = idconfiguracion;
        this.tipocpu = tipocpu;
        this.velocidadcpu = velocidadcpu;
        this.capacidadram = capacidadram;
        this.capacidaddd = capacidaddd;
        this.precio = precio;
    }

    public Integer getIdconfiguracion() {
        return idconfiguracion;
    }

    public void setIdconfiguracion(Integer idconfiguracion) {
        this.idconfiguracion = idconfiguracion;
    }

    public String getTipocpu() {
        return tipocpu;
    }

    public void setTipocpu(String tipocpu) {
        this.tipocpu = tipocpu;
    }

    public int getVelocidadcpu() {
        return velocidadcpu;
    }

    public void setVelocidadcpu(int velocidadcpu) {
        this.velocidadcpu = velocidadcpu;
    }

    public int getCapacidadram() {
        return capacidadram;
    }

    public void setCapacidadram(int capacidadram) {
        this.capacidadram = capacidadram;
    }

    public int getCapacidaddd() {
        return capacidaddd;
    }

    public void setCapacidaddd(int capacidaddd) {
        this.capacidaddd = capacidaddd;
    }

    public Integer getVelocidadtarjetagrafica() {
        return velocidadtarjetagrafica;
    }

    public void setVelocidadtarjetagrafica(Integer velocidadtarjetagrafica) {
        this.velocidadtarjetagrafica = velocidadtarjetagrafica;
    }

    public Integer getMemoriatarjetagrafica() {
        return memoriatarjetagrafica;
    }

    public void setMemoriatarjetagrafica(Integer memoriatarjetagrafica) {
        this.memoriatarjetagrafica = memoriatarjetagrafica;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idconfiguracion != null ? idconfiguracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuracionpc)) {
            return false;
        }
        Configuracionpc other = (Configuracionpc) object;
        if ((this.idconfiguracion == null && other.idconfiguracion != null) || (this.idconfiguracion != null && !this.idconfiguracion.equals(other.idconfiguracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dominio.Configuracionpc[ idconfiguracion=" + idconfiguracion + " ]";
    }
    
}
