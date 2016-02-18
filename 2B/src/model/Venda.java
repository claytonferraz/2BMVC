/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Clayton Ferraz
 */
@Entity
@Table(catalog = "2binf", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venda.findAll", query = "SELECT v FROM Venda v"),
    @NamedQuery(name = "Venda.findByIdvenda", query = "SELECT v FROM Venda v WHERE v.idvenda = :idvenda"),
    @NamedQuery(name = "Venda.findByValortotal", query = "SELECT v FROM Venda v WHERE v.valortotal = :valortotal"),
    @NamedQuery(name = "Venda.findByDataventa", query = "SELECT v FROM Venda v WHERE v.dataventa = :dataventa")})
public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idvenda;
    @Basic(optional = false)
    @Column(nullable = false)
    private float valortotal;
    @Basic(optional = false)
    @Column(nullable = false)
    private float dataventa;
    @JoinColumn(name = "idpessoafk", referencedColumnName = "idpessoa", nullable = false)
    @ManyToOne(optional = false)
    private Pessoa idpessoafk;
    @JoinColumn(name = "idvendedorfk", referencedColumnName = "idvendedor", nullable = false)
    @ManyToOne(optional = false)
    private Vendedor idvendedorfk;
    @JoinColumn(name = "idprodutofk", referencedColumnName = "idcatFk", nullable = false)
    @ManyToOne(optional = false)
    private Produto idprodutofk;

    public Venda() {
    }

    public Venda(Integer idvenda) {
        this.idvenda = idvenda;
    }

    public Venda(Integer idvenda, float valortotal, float dataventa) {
        this.idvenda = idvenda;
        this.valortotal = valortotal;
        this.dataventa = dataventa;
    }

    public Integer getIdvenda() {
        return idvenda;
    }

    public void setIdvenda(Integer idvenda) {
        this.idvenda = idvenda;
    }

    public float getValortotal() {
        return valortotal;
    }

    public void setValortotal(float valortotal) {
        this.valortotal = valortotal;
    }

    public float getDataventa() {
        return dataventa;
    }

    public void setDataventa(float dataventa) {
        this.dataventa = dataventa;
    }

    public Pessoa getIdpessoafk() {
        return idpessoafk;
    }

    public void setIdpessoafk(Pessoa idpessoafk) {
        this.idpessoafk = idpessoafk;
    }

    public Vendedor getIdvendedorfk() {
        return idvendedorfk;
    }

    public void setIdvendedorfk(Vendedor idvendedorfk) {
        this.idvendedorfk = idvendedorfk;
    }

    public Produto getIdprodutofk() {
        return idprodutofk;
    }

    public void setIdprodutofk(Produto idprodutofk) {
        this.idprodutofk = idprodutofk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idvenda != null ? idvenda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venda)) {
            return false;
        }
        Venda other = (Venda) object;
        if ((this.idvenda == null && other.idvenda != null) || (this.idvenda != null && !this.idvenda.equals(other.idvenda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Venda[ idvenda=" + idvenda + " ]";
    }
    
}
