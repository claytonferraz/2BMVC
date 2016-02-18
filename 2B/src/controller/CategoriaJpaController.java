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
import model.Produto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Categoria;

/**
 *
 * @author Clayton Ferraz
 */
public class CategoriaJpaController implements Serializable {

    public CategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public CategoriaJpaController() {
       
        emf  = Persistence.createEntityManagerFactory("2BMVCPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) {
        if (categoria.getProdutoCollection() == null) {
            categoria.setProdutoCollection(new ArrayList<Produto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Produto> attachedProdutoCollection = new ArrayList<Produto>();
            for (Produto produtoCollectionProdutoToAttach : categoria.getProdutoCollection()) {
                produtoCollectionProdutoToAttach = em.getReference(produtoCollectionProdutoToAttach.getClass(), produtoCollectionProdutoToAttach.getIdproduto());
                attachedProdutoCollection.add(produtoCollectionProdutoToAttach);
            }
            categoria.setProdutoCollection(attachedProdutoCollection);
            em.persist(categoria);
            for (Produto produtoCollectionProduto : categoria.getProdutoCollection()) {
                Categoria oldIdcatFkOfProdutoCollectionProduto = produtoCollectionProduto.getIdcatFk();
                produtoCollectionProduto.setIdcatFk(categoria);
                produtoCollectionProduto = em.merge(produtoCollectionProduto);
                if (oldIdcatFkOfProdutoCollectionProduto != null) {
                    oldIdcatFkOfProdutoCollectionProduto.getProdutoCollection().remove(produtoCollectionProduto);
                    oldIdcatFkOfProdutoCollectionProduto = em.merge(oldIdcatFkOfProdutoCollectionProduto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getIdcat());
            Collection<Produto> produtoCollectionOld = persistentCategoria.getProdutoCollection();
            Collection<Produto> produtoCollectionNew = categoria.getProdutoCollection();
            List<String> illegalOrphanMessages = null;
            for (Produto produtoCollectionOldProduto : produtoCollectionOld) {
                if (!produtoCollectionNew.contains(produtoCollectionOldProduto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Produto " + produtoCollectionOldProduto + " since its idcatFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Produto> attachedProdutoCollectionNew = new ArrayList<Produto>();
            for (Produto produtoCollectionNewProdutoToAttach : produtoCollectionNew) {
                produtoCollectionNewProdutoToAttach = em.getReference(produtoCollectionNewProdutoToAttach.getClass(), produtoCollectionNewProdutoToAttach.getIdproduto());
                attachedProdutoCollectionNew.add(produtoCollectionNewProdutoToAttach);
            }
            produtoCollectionNew = attachedProdutoCollectionNew;
            categoria.setProdutoCollection(produtoCollectionNew);
            categoria = em.merge(categoria);
            for (Produto produtoCollectionNewProduto : produtoCollectionNew) {
                if (!produtoCollectionOld.contains(produtoCollectionNewProduto)) {
                    Categoria oldIdcatFkOfProdutoCollectionNewProduto = produtoCollectionNewProduto.getIdcatFk();
                    produtoCollectionNewProduto.setIdcatFk(categoria);
                    produtoCollectionNewProduto = em.merge(produtoCollectionNewProduto);
                    if (oldIdcatFkOfProdutoCollectionNewProduto != null && !oldIdcatFkOfProdutoCollectionNewProduto.equals(categoria)) {
                        oldIdcatFkOfProdutoCollectionNewProduto.getProdutoCollection().remove(produtoCollectionNewProduto);
                        oldIdcatFkOfProdutoCollectionNewProduto = em.merge(oldIdcatFkOfProdutoCollectionNewProduto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoria.getIdcat();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getIdcat();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Produto> produtoCollectionOrphanCheck = categoria.getProdutoCollection();
            for (Produto produtoCollectionOrphanCheckProduto : produtoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Produto " + produtoCollectionOrphanCheckProduto + " in its produtoCollection field has a non-nullable idcatFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
