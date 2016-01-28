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
import model.Endereco;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Pessoa;
import model.Tarefa;

/**
 *
 * @author Clayton Ferraz
 */
public class PessoaJpaController implements Serializable {

    public PessoaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    
    
    public PessoaJpaController() {
       
        emf  = Persistence.createEntityManagerFactory("2BMVCPU");
    }
    
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoa pessoa) {
        if (pessoa.getEnderecoCollection() == null) {
            pessoa.setEnderecoCollection(new ArrayList<Endereco>());
        }
        if (pessoa.getTarefaCollection() == null) {
            pessoa.setTarefaCollection(new ArrayList<Tarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Endereco> attachedEnderecoCollection = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionEnderecoToAttach : pessoa.getEnderecoCollection()) {
                enderecoCollectionEnderecoToAttach = em.getReference(enderecoCollectionEnderecoToAttach.getClass(), enderecoCollectionEnderecoToAttach.getIdendereco());
                attachedEnderecoCollection.add(enderecoCollectionEnderecoToAttach);
            }
            pessoa.setEnderecoCollection(attachedEnderecoCollection);
            Collection<Tarefa> attachedTarefaCollection = new ArrayList<Tarefa>();
            for (Tarefa tarefaCollectionTarefaToAttach : pessoa.getTarefaCollection()) {
                tarefaCollectionTarefaToAttach = em.getReference(tarefaCollectionTarefaToAttach.getClass(), tarefaCollectionTarefaToAttach.getIdtarefa());
                attachedTarefaCollection.add(tarefaCollectionTarefaToAttach);
            }
            pessoa.setTarefaCollection(attachedTarefaCollection);
            em.persist(pessoa);
            for (Endereco enderecoCollectionEndereco : pessoa.getEnderecoCollection()) {
                Pessoa oldIdpessoafkOfEnderecoCollectionEndereco = enderecoCollectionEndereco.getIdpessoafk();
                enderecoCollectionEndereco.setIdpessoafk(pessoa);
                enderecoCollectionEndereco = em.merge(enderecoCollectionEndereco);
                if (oldIdpessoafkOfEnderecoCollectionEndereco != null) {
                    oldIdpessoafkOfEnderecoCollectionEndereco.getEnderecoCollection().remove(enderecoCollectionEndereco);
                    oldIdpessoafkOfEnderecoCollectionEndereco = em.merge(oldIdpessoafkOfEnderecoCollectionEndereco);
                }
            }
            for (Tarefa tarefaCollectionTarefa : pessoa.getTarefaCollection()) {
                Pessoa oldIdpessoaOfTarefaCollectionTarefa = tarefaCollectionTarefa.getIdpessoa();
                tarefaCollectionTarefa.setIdpessoa(pessoa);
                tarefaCollectionTarefa = em.merge(tarefaCollectionTarefa);
                if (oldIdpessoaOfTarefaCollectionTarefa != null) {
                    oldIdpessoaOfTarefaCollectionTarefa.getTarefaCollection().remove(tarefaCollectionTarefa);
                    oldIdpessoaOfTarefaCollectionTarefa = em.merge(oldIdpessoaOfTarefaCollectionTarefa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoa pessoa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa persistentPessoa = em.find(Pessoa.class, pessoa.getIdpessoa());
            Collection<Endereco> enderecoCollectionOld = persistentPessoa.getEnderecoCollection();
            Collection<Endereco> enderecoCollectionNew = pessoa.getEnderecoCollection();
            Collection<Tarefa> tarefaCollectionOld = persistentPessoa.getTarefaCollection();
            Collection<Tarefa> tarefaCollectionNew = pessoa.getTarefaCollection();
            List<String> illegalOrphanMessages = null;
            for (Endereco enderecoCollectionOldEndereco : enderecoCollectionOld) {
                if (!enderecoCollectionNew.contains(enderecoCollectionOldEndereco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Endereco " + enderecoCollectionOldEndereco + " since its idpessoafk field is not nullable.");
                }
            }
            for (Tarefa tarefaCollectionOldTarefa : tarefaCollectionOld) {
                if (!tarefaCollectionNew.contains(tarefaCollectionOldTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarefa " + tarefaCollectionOldTarefa + " since its idpessoa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Endereco> attachedEnderecoCollectionNew = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionNewEnderecoToAttach : enderecoCollectionNew) {
                enderecoCollectionNewEnderecoToAttach = em.getReference(enderecoCollectionNewEnderecoToAttach.getClass(), enderecoCollectionNewEnderecoToAttach.getIdendereco());
                attachedEnderecoCollectionNew.add(enderecoCollectionNewEnderecoToAttach);
            }
            enderecoCollectionNew = attachedEnderecoCollectionNew;
            pessoa.setEnderecoCollection(enderecoCollectionNew);
            Collection<Tarefa> attachedTarefaCollectionNew = new ArrayList<Tarefa>();
            for (Tarefa tarefaCollectionNewTarefaToAttach : tarefaCollectionNew) {
                tarefaCollectionNewTarefaToAttach = em.getReference(tarefaCollectionNewTarefaToAttach.getClass(), tarefaCollectionNewTarefaToAttach.getIdtarefa());
                attachedTarefaCollectionNew.add(tarefaCollectionNewTarefaToAttach);
            }
            tarefaCollectionNew = attachedTarefaCollectionNew;
            pessoa.setTarefaCollection(tarefaCollectionNew);
            pessoa = em.merge(pessoa);
            for (Endereco enderecoCollectionNewEndereco : enderecoCollectionNew) {
                if (!enderecoCollectionOld.contains(enderecoCollectionNewEndereco)) {
                    Pessoa oldIdpessoafkOfEnderecoCollectionNewEndereco = enderecoCollectionNewEndereco.getIdpessoafk();
                    enderecoCollectionNewEndereco.setIdpessoafk(pessoa);
                    enderecoCollectionNewEndereco = em.merge(enderecoCollectionNewEndereco);
                    if (oldIdpessoafkOfEnderecoCollectionNewEndereco != null && !oldIdpessoafkOfEnderecoCollectionNewEndereco.equals(pessoa)) {
                        oldIdpessoafkOfEnderecoCollectionNewEndereco.getEnderecoCollection().remove(enderecoCollectionNewEndereco);
                        oldIdpessoafkOfEnderecoCollectionNewEndereco = em.merge(oldIdpessoafkOfEnderecoCollectionNewEndereco);
                    }
                }
            }
            for (Tarefa tarefaCollectionNewTarefa : tarefaCollectionNew) {
                if (!tarefaCollectionOld.contains(tarefaCollectionNewTarefa)) {
                    Pessoa oldIdpessoaOfTarefaCollectionNewTarefa = tarefaCollectionNewTarefa.getIdpessoa();
                    tarefaCollectionNewTarefa.setIdpessoa(pessoa);
                    tarefaCollectionNewTarefa = em.merge(tarefaCollectionNewTarefa);
                    if (oldIdpessoaOfTarefaCollectionNewTarefa != null && !oldIdpessoaOfTarefaCollectionNewTarefa.equals(pessoa)) {
                        oldIdpessoaOfTarefaCollectionNewTarefa.getTarefaCollection().remove(tarefaCollectionNewTarefa);
                        oldIdpessoaOfTarefaCollectionNewTarefa = em.merge(oldIdpessoaOfTarefaCollectionNewTarefa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoa.getIdpessoa();
                if (findPessoa(id) == null) {
                    throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.");
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
            Pessoa pessoa;
            try {
                pessoa = em.getReference(Pessoa.class, id);
                pessoa.getIdpessoa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Endereco> enderecoCollectionOrphanCheck = pessoa.getEnderecoCollection();
            for (Endereco enderecoCollectionOrphanCheckEndereco : enderecoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the Endereco " + enderecoCollectionOrphanCheckEndereco + " in its enderecoCollection field has a non-nullable idpessoafk field.");
            }
            Collection<Tarefa> tarefaCollectionOrphanCheck = pessoa.getTarefaCollection();
            for (Tarefa tarefaCollectionOrphanCheckTarefa : tarefaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the Tarefa " + tarefaCollectionOrphanCheckTarefa + " in its tarefaCollection field has a non-nullable idpessoa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoa> findPessoaEntities() {
        return findPessoaEntities(true, -1, -1);
    }

    public List<Pessoa> findPessoaEntities(int maxResults, int firstResult) {
        return findPessoaEntities(false, maxResults, firstResult);
    }

    private List<Pessoa> findPessoaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoa.class));
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

    public Pessoa findPessoa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoa.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoa> rt = cq.from(Pessoa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
