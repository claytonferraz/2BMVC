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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Clayton Ferraz
 */
@Entity
@Table(catalog = "2binf", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categoria.findAll", query = "SELECT c FROM Categoria c"),
    @NamedQuery(name = "Categoria.findByIdcat", query = "SELECT c FROM Categoria c WHERE c.idcat = :idcat"),
    @NamedQuery(name = "Categoria.findByNomecat", query = "SELECT c FROM Categoria c WHERE c.nomecat = :nomecat"),
    @NamedQuery(name = "Categoria.findByDescricao", query = "SELECT c FROM Categoria c WHERE c.descricao = :descricao")})
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idcat;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String nomecat;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcatFk")
    private Collection<Produto> produtoCollection;

    public Categoria() {
    }

    public Categoria(Integer idcat) {
        this.idcat = idcat;
    }

    public Categoria(Integer idcat, String nomecat, String descricao) {
        this.idcat = idcat;
        this.nomecat = nomecat;
        this.descricao = descricao;
    }

    public Integer getIdcat() {
        return idcat;
    }

    public void setIdcat(Integer idcat) {
        this.idcat = idcat;
    }

    public String getNomecat() {
        return nomecat;
    }

    public void setNomecat(String nomecat) {
        this.nomecat = nomecat;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<Produto> getProdutoCollection() {
        return produtoCollection;
    }

    public void setProdutoCollection(Collection<Produto> produtoCollection) {
        this.produtoCollection = produtoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcat != null ? idcat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.idcat == null && other.idcat != null) || (this.idcat != null && !this.idcat.equals(other.idcat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Categoria[ idcat=" + idcat + " ]";
    }
    
}
