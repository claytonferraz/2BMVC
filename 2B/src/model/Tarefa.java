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
@Table(name = "tarefa", catalog = "2binfo", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarefa.findAll", query = "SELECT t FROM Tarefa t"),
    @NamedQuery(name = "Tarefa.findByIdtarefa", query = "SELECT t FROM Tarefa t WHERE t.idtarefa = :idtarefa"),
    @NamedQuery(name = "Tarefa.findByDescricao", query = "SELECT t FROM Tarefa t WHERE t.descricao = :descricao"),
    @NamedQuery(name = "Tarefa.findByData", query = "SELECT t FROM Tarefa t WHERE t.data = :data")})
public class Tarefa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtarefa")
    private Integer idtarefa;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "data")
    private String data;
    @JoinColumn(name = "idpessoa", referencedColumnName = "idpessoa")
    @ManyToOne(optional = false)
    private Pessoa idpessoa;

    public Tarefa() {
    }

    public Tarefa(Integer idtarefa) {
        this.idtarefa = idtarefa;
    }

    public Tarefa(Integer idtarefa, String descricao, String data) {
        this.idtarefa = idtarefa;
        this.descricao = descricao;
        this.data = data;
    }

    public Integer getIdtarefa() {
        return idtarefa;
    }

    public void setIdtarefa(Integer idtarefa) {
        this.idtarefa = idtarefa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Pessoa getIdpessoa() {
        return idpessoa;
    }

    public void setIdpessoa(Pessoa idpessoa) {
        this.idpessoa = idpessoa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtarefa != null ? idtarefa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarefa)) {
            return false;
        }
        Tarefa other = (Tarefa) object;
        if ((this.idtarefa == null && other.idtarefa != null) || (this.idtarefa != null && !this.idtarefa.equals(other.idtarefa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Tarefa[ idtarefa=" + idtarefa + " ]";
    }
    
}
