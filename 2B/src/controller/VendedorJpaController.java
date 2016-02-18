/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Venda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Vendedor;

/**
 *
 * @author Clayton Ferraz
 */
public class VendedorJpaController implements Serializable {

    public VendedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vendedor vendedor) {
        if (vendedor.getVendaCollection() == null) {
            vendedor.setVendaCollection(new ArrayList<Venda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Venda> attachedVendaCollection = new ArrayList<Venda>();
            for (Venda vendaCollectionVendaToAttach : vendedor.getVendaCollection()) {
                vendaCollectionVendaToAttach = em.getReference(vendaCollectionVendaToAttach.getClass(), vendaCollectionVendaToAttach.getIdvenda());
                attachedVendaCollection.add(vendaCollectionVendaToAttach);
            }
            vendedor.setVendaCollection(attachedVendaCollection);
            em.persist(vendedor);
            for (Venda vendaCollectionVenda : vendedor.getVendaCollection()) {
                Vendedor oldIdvendedorfkOfVendaCollectionVenda = vendaCollectionVenda.getIdvendedorfk();
                vendaCollectionVenda.setIdvendedorfk(vendedor);
                vendaCollectionVenda = em.merge(vendaCollectionVenda);
                if (oldIdvendedorfkOfVendaCollectionVenda != null) {
                    oldIdvendedorfkOfVendaCollectionVenda.getVendaCollection().remove(vendaCollectionVenda);
                    oldIdvendedorfkOfVendaCollectionVenda = em.merge(oldIdvendedorfkOfVendaCollectionVenda);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vendedor vendedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vendedor persistentVendedor = em.find(Vendedor.class, vendedor.getIdvendedor());
            Collection<Venda> vendaCollectionOld = persistentVendedor.getVendaCollection();
            Collection<Venda> vendaCollectionNew = vendedor.getVendaCollection();
            List<String> illegalOrphanMessages = null;
            for (Venda vendaCollectionOldVenda : vendaCollectionOld) {
                if (!vendaCollectionNew.contains(vendaCollectionOldVenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venda " + vendaCollectionOldVenda + " since its idvendedorfk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Venda> attachedVendaCollectionNew = new ArrayList<Venda>();
            for (Venda vendaCollectionNewVendaToAttach : vendaCollectionNew) {
                vendaCollectionNewVendaToAttach = em.getReference(vendaCollectionNewVendaToAttach.getClass(), vendaCollectionNewVendaToAttach.getIdvenda());
                attachedVendaCollectionNew.add(vendaCollectionNewVendaToAttach);
            }
            vendaCollectionNew = attachedVendaCollectionNew;
            vendedor.setVendaCollection(vendaCollectionNew);
            vendedor = em.merge(vendedor);
            for (Venda vendaCollectionNewVenda : vendaCollectionNew) {
                if (!vendaCollectionOld.contains(vendaCollectionNewVenda)) {
                    Vendedor oldIdvendedorfkOfVendaCollectionNewVenda = vendaCollectionNewVenda.getIdvendedorfk();
                    vendaCollectionNewVenda.setIdvendedorfk(vendedor);
                    vendaCollectionNewVenda = em.merge(vendaCollectionNewVenda);
                    if (oldIdvendedorfkOfVendaCollectionNewVenda != null && !oldIdvendedorfkOfVendaCollectionNewVenda.equals(vendedor)) {
                        oldIdvendedorfkOfVendaCollectionNewVenda.getVendaCollection().remove(vendaCollectionNewVenda);
                        oldIdvendedorfkOfVendaCollectionNewVenda = em.merge(oldIdvendedorfkOfVendaCollectionNewVenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vendedor.getIdvendedor();
                if (findVendedor(id) == null) {
                    throw new NonexistentEntityException("The vendedor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vendedor vendedor;
            try {
                vendedor = em.getReference(Vendedor.class, id);
                vendedor.getIdvendedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vendedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Venda> vendaCollectionOrphanCheck = vendedor.getVendaCollection();
            for (Venda vendaCollectionOrphanCheckVenda : vendaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vendedor (" + vendedor + ") cannot be destroyed since the Venda " + vendaCollectionOrphanCheckVenda + " in its vendaCollection field has a non-nullable idvendedorfk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(vendedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vendedor> findVendedorEntities() {
        return findVendedorEntities(true, -1, -1);
    }

    public List<Vendedor> findVendedorEntities(int maxResults, int firstResult) {
        return findVendedorEntities(false, maxResults, firstResult);
    }

    private List<Vendedor> findVendedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vendedor.class));
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

    public Vendedor findVendedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vendedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getVendedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vendedor> rt = cq.from(Vendedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
