/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

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


// NOVO MODEL
/**
 *
 * @author Clayton Ferraz
 */
@Entity
@Table(catalog = "2binf", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produto.findAll", query = "SELECT p FROM Produto p"),
    @NamedQuery(name = "Produto.findByIdproduto", query = "SELECT p FROM Produto p WHERE p.idproduto = :idproduto"),
    @NamedQuery(name = "Produto.findByNomeproduto", query = "SELECT p FROM Produto p WHERE p.nomeproduto = :nomeproduto"),
    @NamedQuery(name = "Produto.findByQuantidade", query = "SELECT p FROM Produto p WHERE p.quantidade = :quantidade"),
    @NamedQuery(name = "Produto.findByValor", query = "SELECT p FROM Produto p WHERE p.valor = :valor")})
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idproduto;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String nomeproduto;
    @Basic(optional = false)
    @Column(nullable = false)
    private int quantidade;
    @Basic(optional = false)
    @Column(nullable = false)
    private float valor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idprodutofk")
    private Collection<Venda> vendaCollection;
    @JoinColumn(name = "idcatFk", referencedColumnName = "idcat", nullable = false)
    @ManyToOne(optional = false)
    private Categoria idcatFk;

    public Produto() {
    }

    public Produto(Integer idproduto) {
        this.idproduto = idproduto;
    }

    public Produto(Integer idproduto, String nomeproduto, int quantidade, float valor) {
        this.idproduto = idproduto;
        this.nomeproduto = nomeproduto;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Integer getIdproduto() {
        return idproduto;
    }

    public void setIdproduto(Integer idproduto) {
        this.idproduto = idproduto;
    }

    public String getNomeproduto() {
        return nomeproduto;
    }

    public void setNomeproduto(String nomeproduto) {
        this.nomeproduto = nomeproduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    @XmlTransient
    public Collection<Venda> getVendaCollection() {
        return vendaCollection;
    }

    public void setVendaCollection(Collection<Venda> vendaCollection) {
        this.vendaCollection = vendaCollection;
    }

    public Categoria getIdcatFk() {
        return idcatFk;
    }

    public void setIdcatFk(Categoria idcatFk) {
        this.idcatFk = idcatFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idproduto != null ? idproduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
        if ((this.idproduto == null && other.idproduto != null) || (this.idproduto != null && !this.idproduto.equals(other.idproduto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Produto[ idproduto=" + idproduto + " ]";
    }
    
}
