/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Pessoa;
import model.Vendedor;
import model.Produto;
import model.Venda;

/**
 *
 * @author Clayton Ferraz
 */
public class VendaJpaController implements Serializable {

    public VendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venda venda) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa idpessoafk = venda.getIdpessoafk();
            if (idpessoafk != null) {
                idpessoafk = em.getReference(idpessoafk.getClass(), idpessoafk.getIdpessoa());
                venda.setIdpessoafk(idpessoafk);
            }
            Vendedor idvendedorfk = venda.getIdvendedorfk();
            if (idvendedorfk != null) {
                idvendedorfk = em.getReference(idvendedorfk.getClass(), idvendedorfk.getIdvendedor());
                venda.setIdvendedorfk(idvendedorfk);
            }
            Produto idprodutofk = venda.getIdprodutofk();
            if (idprodutofk != null) {
                idprodutofk = em.getReference(idprodutofk.getClass(), idprodutofk.getIdproduto());
                venda.setIdprodutofk(idprodutofk);
            }
            em.persist(venda);
            if (idpessoafk != null) {
                idpessoafk.getVendaCollection().add(venda);
                idpessoafk = em.merge(idpessoafk);
            }
            if (idvendedorfk != null) {
                idvendedorfk.getVendaCollection().add(venda);
                idvendedorfk = em.merge(idvendedorfk);
            }
            if (idprodutofk != null) {
                idprodutofk.getVendaCollection().add(venda);
                idprodutofk = em.merge(idprodutofk);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venda venda) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venda persistentVenda = em.find(Venda.class, venda.getIdvenda());
            Pessoa idpessoafkOld = persistentVenda.getIdpessoafk();
            Pessoa idpessoafkNew = venda.getIdpessoafk();
            Vendedor idvendedorfkOld = persistentVenda.getIdvendedorfk();
            Vendedor idvendedorfkNew = venda.getIdvendedorfk();
            Produto idprodutofkOld = persistentVenda.getIdprodutofk();
            Produto idprodutofkNew = venda.getIdprodutofk();
            if (idpessoafkNew != null) {
                idpessoafkNew = em.getReference(idpessoafkNew.getClass(), idpessoafkNew.getIdpessoa());
                venda.setIdpessoafk(idpessoafkNew);
            }
            if (idvendedorfkNew != null) {
                idvendedorfkNew = em.getReference(idvendedorfkNew.getClass(), idvendedorfkNew.getIdvendedor());
                venda.setIdvendedorfk(idvendedorfkNew);
            }
            if (idprodutofkNew != null) {
                idprodutofkNew = em.getReference(idprodutofkNew.getClass(), idprodutofkNew.getIdproduto());
                venda.setIdprodutofk(idprodutofkNew);
            }
            venda = em.merge(venda);
            if (idpessoafkOld != null && !idpessoafkOld.equals(idpessoafkNew)) {
                idpessoafkOld.getVendaCollection().remove(venda);
                idpessoafkOld = em.merge(idpessoafkOld);
            }
            if (idpessoafkNew != null && !idpessoafkNew.equals(idpessoafkOld)) {
                idpessoafkNew.getVendaCollection().add(venda);
                idpessoafkNew = em.merge(idpessoafkNew);
            }
            if (idvendedorfkOld != null && !idvendedorfkOld.equals(idvendedorfkNew)) {
                idvendedorfkOld.getVendaCollection().remove(venda);
                idvendedorfkOld = em.merge(idvendedorfkOld);
            }
            if (idvendedorfkNew != null && !idvendedorfkNew.equals(idvendedorfkOld)) {
                idvendedorfkNew.getVendaCollection().add(venda);
                idvendedorfkNew = em.merge(idvendedorfkNew);
            }
            if (idprodutofkOld != null && !idprodutofkOld.equals(idprodutofkNew)) {
                idprodutofkOld.getVendaCollection().remove(venda);
                idprodutofkOld = em.merge(idprodutofkOld);
            }
            if (idprodutofkNew != null && !idprodutofkNew.equals(idprodutofkOld)) {
                idprodutofkNew.getVendaCollection().add(venda);
                idprodutofkNew = em.merge(idprodutofkNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = venda.getIdvenda();
                if (findVenda(id) == null) {
                    throw new NonexistentEntityException("The venda with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venda venda;
            try {
                venda = em.getReference(Venda.class, id);
                venda.getIdvenda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venda with id " + id + " no longer exists.", enfe);
            }
            Pessoa idpessoafk = venda.getIdpessoafk();
            if (idpessoafk != null) {
                idpessoafk.getVendaCollection().remove(venda);
                idpessoafk = em.merge(idpessoafk);
            }
            Vendedor idvendedorfk = venda.getIdvendedorfk();
            if (idvendedorfk != null) {
                idvendedorfk.getVendaCollection().remove(venda);
                idvendedorfk = em.merge(idvendedorfk);
            }
            Produto idprodutofk = venda.getIdprodutofk();
            if (idprodutofk != null) {
                idprodutofk.getVendaCollection().remove(venda);
                idprodutofk = em.merge(idprodutofk);
            }
            em.remove(venda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venda> findVendaEntities() {
        return findVendaEntities(true, -1, -1);
    }

    public List<Venda> findVendaEntities(int maxResults, int firstResult) {
        return findVendaEntities(false, maxResults, firstResult);
    }

    private List<Venda> findVendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venda.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Venda findVenda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venda.class, id);
        } finally {
            em.close();
        }
    }

    public int getVendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venda> rt = cq.from(Venda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
