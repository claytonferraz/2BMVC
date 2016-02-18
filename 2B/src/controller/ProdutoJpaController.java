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
import model.Categoria;
import model.Venda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Produto;

/**
 *
 * @author Clayton Ferraz
 */
public class ProdutoJpaController implements Serializable {

    public ProdutoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produto produto) {
        if (produto.getVendaCollection() == null) {
            produto.setVendaCollection(new ArrayList<Venda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idcatFk = produto.getIdcatFk();
            if (idcatFk != null) {
                idcatFk = em.getReference(idcatFk.getClass(), idcatFk.getIdcat());
                produto.setIdcatFk(idcatFk);
            }
            Collection<Venda> attachedVendaCollection = new ArrayList<Venda>();
            for (Venda vendaCollectionVendaToAttach : produto.getVendaCollection()) {
                vendaCollectionVendaToAttach = em.getReference(vendaCollectionVendaToAttach.getClass(), vendaCollectionVendaToAttach.getIdvenda());
                attachedVendaCollection.add(vendaCollectionVendaToAttach);
            }
            produto.setVendaCollection(attachedVendaCollection);
            em.persist(produto);
            if (idcatFk != null) {
                idcatFk.getProdutoCollection().add(produto);
                idcatFk = em.merge(idcatFk);
            }
            for (Venda vendaCollectionVenda : produto.getVendaCollection()) {
                Produto oldIdprodutofkOfVendaCollectionVenda = vendaCollectionVenda.getIdprodutofk();
                vendaCollectionVenda.setIdprodutofk(produto);
                vendaCollectionVenda = em.merge(vendaCollectionVenda);
                if (oldIdprodutofkOfVendaCollectionVenda != null) {
                    oldIdprodutofkOfVendaCollectionVenda.getVendaCollection().remove(vendaCollectionVenda);
                    oldIdprodutofkOfVendaCollectionVenda = em.merge(oldIdprodutofkOfVendaCollectionVenda);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produto produto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto persistentProduto = em.find(Produto.class, produto.getIdproduto());
            Categoria idcatFkOld = persistentProduto.getIdcatFk();
            Categoria idcatFkNew = produto.getIdcatFk();
            Collection<Venda> vendaCollectionOld = persistentProduto.getVendaCollection();
            Collection<Venda> vendaCollectionNew = produto.getVendaCollection();
            List<String> illegalOrphanMessages = null;
            for (Venda vendaCollectionOldVenda : vendaCollectionOld) {
                if (!vendaCollectionNew.contains(vendaCollectionOldVenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venda " + vendaCollectionOldVenda + " since its idprodutofk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idcatFkNew != null) {
                idcatFkNew = em.getReference(idcatFkNew.getClass(), idcatFkNew.getIdcat());
                produto.setIdcatFk(idcatFkNew);
            }
            Collection<Venda> attachedVendaCollectionNew = new ArrayList<Venda>();
            for (Venda vendaCollectionNewVendaToAttach : vendaCollectionNew) {
                vendaCollectionNewVendaToAttach = em.getReference(vendaCollectionNewVendaToAttach.getClass(), vendaCollectionNewVendaToAttach.getIdvenda());
                attachedVendaCollectionNew.add(vendaCollectionNewVendaToAttach);
            }
            vendaCollectionNew = attachedVendaCollectionNew;
            produto.setVendaCollection(vendaCollectionNew);
            produto = em.merge(produto);
            if (idcatFkOld != null && !idcatFkOld.equals(idcatFkNew)) {
                idcatFkOld.getProdutoCollection().remove(produto);
                idcatFkOld = em.merge(idcatFkOld);
            }
            if (idcatFkNew != null && !idcatFkNew.equals(idcatFkOld)) {
                idcatFkNew.getProdutoCollection().add(produto);
                idcatFkNew = em.merge(idcatFkNew);
            }
            for (Venda vendaCollectionNewVenda : vendaCollectionNew) {
                if (!vendaCollectionOld.contains(vendaCollectionNewVenda)) {
                    Produto oldIdprodutofkOfVendaCollectionNewVenda = vendaCollectionNewVenda.getIdprodutofk();
                    vendaCollectionNewVenda.setIdprodutofk(produto);
                    vendaCollectionNewVenda = em.merge(vendaCollectionNewVenda);
                    if (oldIdprodutofkOfVendaCollectionNewVenda != null && !oldIdprodutofkOfVendaCollectionNewVenda.equals(produto)) {
                        oldIdprodutofkOfVendaCollectionNewVenda.getVendaCollection().remove(vendaCollectionNewVenda);
                        oldIdprodutofkOfVendaCollectionNewVenda = em.merge(oldIdprodutofkOfVendaCollectionNewVenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produto.getIdproduto();
                if (findProduto(id) == null) {
                    throw new NonexistentEntityException("The produto with id " + id + " no longer exists.");
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
            Produto produto;
            try {
                produto = em.getReference(Produto.class, id);
                produto.getIdproduto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Venda> vendaCollectionOrphanCheck = produto.getVendaCollection();
            for (Venda vendaCollectionOrphanCheckVenda : vendaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Produto (" + produto + ") cannot be destroyed since the Venda " + vendaCollectionOrphanCheckVenda + " in its vendaCollection field has a non-nullable idprodutofk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idcatFk = produto.getIdcatFk();
            if (idcatFk != null) {
                idcatFk.getProdutoCollection().remove(produto);
                idcatFk = em.merge(idcatFk);
            }
            em.remove(produto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produto> findProdutoEntities() {
        return findProdutoEntities(true, -1, -1);
    }

    public List<Produto> findProdutoEntities(int maxResults, int firstResult) {
        return findProdutoEntities(false, maxResults, firstResult);
    }

    private List<Produto> findProdutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produto.class));
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

    public Produto findProduto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Produto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
