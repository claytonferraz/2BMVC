/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import model.Categoria;
import model.Endereco;
import model.Pessoa;
import model.Tarefa;

/**
 *
 * @author Clayton Ferraz
 */
public class Sistema {
    
    public void CadPessoa(Pessoa pe) {
        PessoaJpaController pessoa = new PessoaJpaController();
        pessoa.create(pe);
    }
    
    public void CadEndereco(Endereco end) {
        EnderecoJpaController endereco = new EnderecoJpaController();
        endereco.create(end);
    }
    
    public void CadTarefa(Tarefa ta) {
        TarefaJpaController tarefa = new TarefaJpaController();
        tarefa.create(ta);
    }
    public void CadCategoria(Categoria  cat) {
        CategoriaJpaController categoria = new CategoriaJpaController();
        categoria.create(cat);
    }
    
    public Endereco ConEndereco(Integer id) {
        EnderecoJpaController endjpa = new EnderecoJpaController();
        Endereco endcon;
        endcon = endjpa.findEndereco(id);
        return endcon;
    }
    
    public Pessoa ConPessoa(Integer id) {
        PessoaJpaController pejpa = new PessoaJpaController();
        Pessoa pescon;
        pescon = pejpa.findPessoa(id);
        return pescon;
    }
    
    public Tarefa ConTarefa(Integer id) {
        TarefaJpaController tajpa = new TarefaJpaController();
        Tarefa tarcon;
        tarcon = tajpa.findTarefa(id);
        return tarcon;
    }
    
    public void EditEndereco(Endereco end) throws Exception {
        EnderecoJpaController endereco = new EnderecoJpaController();
        endereco.edit(end);
    }
    
    public void EditPessoa(Pessoa pe) throws Exception {
        PessoaJpaController pessoa = new PessoaJpaController();
        pessoa.edit(pe);
    }
    
    public void EditTarefa(Tarefa ta) throws Exception {
        TarefaJpaController tarefa = new TarefaJpaController();
        tarefa.edit(ta);
    }
    
    public void DeletarEndereco(Integer id) throws NonexistentEntityException {
        EnderecoJpaController endjpa = new EnderecoJpaController();
        endjpa.destroy(id);
        
    }
    
    public void DeletarPessoa(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        PessoaJpaController pessoa = new PessoaJpaController();
        pessoa.destroy(id);
    }

    public void DelatarTarefa(Integer id) throws NonexistentEntityException {
        TarefaJpaController tarefa = new TarefaJpaController();
        tarefa.destroy(id);
    }
}
