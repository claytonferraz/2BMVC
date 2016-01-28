/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Endereco;
import model.Pessoa;


/**
 *
 * @author Clayton Ferraz
 */
public class Sistema {

      
    
    
    public void CadEndereco (Endereco end){
        
        EnderecoJpaController endereco = new EnderecoJpaController();
        endereco.create(end);
    }
    
    public void EditEndereco (Endereco end) throws Exception{
        EnderecoJpaController endereco = new EnderecoJpaController();
        endereco.edit(end);
    }
    public void CadPessoa (Pessoa pe){
        
        PessoaJpaController pessoa = new PessoaJpaController();
        pessoa.create(pe);
    }
    
    public Endereco ConEndereco (Integer id){
        EnderecoJpaController endjpa = new EnderecoJpaController();
        Endereco endcon;        
        endcon = endjpa.findEndereco(id);
        return endcon;
    }
    public Pessoa ConPessoa (Integer id){
        PessoaJpaController pejpa = new PessoaJpaController();
        Pessoa pescon;        
        pescon = pejpa.findPessoa(id);
        return pescon;
    }
    
    
    
    
    
    
}
