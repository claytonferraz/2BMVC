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
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Pessoa;
import model.Tarefa;

/**
 *
 * @author Clayton Ferraz
 */
public class TarefaJpaController implements Serializable {

    public TarefaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    
    public TarefaJpaController() {
       
        emf  = Persistence.createEntityManagerFactory("2BMVCPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarefa tarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa idpessoa = tarefa.getIdpessoa();
            if (idpessoa != null) {
                idpessoa = em.getReference(idpessoa.getClass(), idpessoa.getIdpessoa());
                tarefa.setIdpessoa(idpessoa);
            }
            em.persist(tarefa);
            if (idpessoa != null) {
                idpessoa.getTarefaCollection().add(tarefa);
                idpessoa = em.merge(idpessoa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarefa tarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa persistentTarefa = em.find(Tarefa.class, tarefa.getIdtarefa());
            Pessoa idpessoaOld = persistentTarefa.getIdpessoa();
            Pessoa idpessoaNew = tarefa.getIdpessoa();
            if (idpessoaNew != null) {
                idpessoaNew = em.getReference(idpessoaNew.getClass(), idpessoaNew.getIdpessoa());
                tarefa.setIdpessoa(idpessoaNew);
            }
            tarefa = em.merge(tarefa);
            if (idpessoaOld != null && !idpessoaOld.equals(idpessoaNew)) {
                idpessoaOld.getTarefaCollection().remove(tarefa);
                idpessoaOld = em.merge(idpessoaOld);
            }
            if (idpessoaNew != null && !idpessoaNew.equals(idpessoaOld)) {
                idpessoaNew.getTarefaCollection().add(tarefa);
                idpessoaNew = em.merge(idpessoaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarefa.getIdtarefa();
                if (findTarefa(id) == null) {
                    throw new NonexistentEntityException("The tarefa with id " + id + " no longer exists.");
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
            Tarefa tarefa;
            try {
                tarefa = em.getReference(Tarefa.class, id);
                tarefa.getIdtarefa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarefa with id " + id + " no longer exists.", enfe);
            }
            Pessoa idpessoa = tarefa.getIdpessoa();
            if (idpessoa != null) {
                idpessoa.getTarefaCollection().remove(tarefa);
                idpessoa = em.merge(idpessoa);
            }
            em.remove(tarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarefa> findTarefaEntities() {
        return findTarefaEntities(true, -1, -1);
    }

    public List<Tarefa> findTarefaEntities(int maxResults, int firstResult) {
        return findTarefaEntities(false, maxResults, firstResult);
    }

    private List<Tarefa> findTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarefa.class));
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

    public Tarefa findTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarefa> rt = cq.from(Tarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
