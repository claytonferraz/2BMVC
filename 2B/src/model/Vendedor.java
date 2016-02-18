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
    @NamedQuery(name = "Vendedor.findAll", query = "SELECT v FROM Vendedor v"),
    @NamedQuery(name = "Vendedor.findByIdvendedor", query = "SELECT v FROM Vendedor v WHERE v.idvendedor = :idvendedor"),
    @NamedQuery(name = "Vendedor.findByNomevendedor", query = "SELECT v FROM Vendedor v WHERE v.nomevendedor = :nomevendedor"),
    @NamedQuery(name = "Vendedor.findByTelefone", query = "SELECT v FROM Vendedor v WHERE v.telefone = :telefone")})
public class Vendedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idvendedor;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String nomevendedor;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String telefone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idvendedorfk")
    private Collection<Venda> vendaCollection;

    public Vendedor() {
    }

    public Vendedor(Integer idvendedor) {
        this.idvendedor = idvendedor;
    }

    public Vendedor(Integer idvendedor, String nomevendedor, String telefone) {
        this.idvendedor = idvendedor;
        this.nomevendedor = nomevendedor;
        this.telefone = telefone;
    }

    public Integer getIdvendedor() {
        return idvendedor;
    }

    public void setIdvendedor(Integer idvendedor) {
        this.idvendedor = idvendedor;
    }

    public String getNomevendedor() {
        return nomevendedor;
    }

    public void setNomevendedor(String nomevendedor) {
        this.nomevendedor = nomevendedor;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @XmlTransient
    public Collection<Venda> getVendaCollection() {
        return vendaCollection;
    }

    public void setVendaCollection(Collection<Venda> vendaCollection) {
        this.vendaCollection = vendaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idvendedor != null ? idvendedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vendedor)) {
            return false;
        }
        Vendedor other = (Vendedor) object;
        if ((this.idvendedor == null && other.idvendedor != null) || (this.idvendedor != null && !this.idvendedor.equals(other.idvendedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Vendedor[ idvendedor=" + idvendedor + " ]";
    }
    
}
