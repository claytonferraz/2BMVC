/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testes;

import controller.Sistema;
import model.Endereco;
import model.Pessoa;
import model.Tarefa;

/**
 *
 * @author Clayton Ferraz
 */
public class DeletarPessoa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Sistema si = new Sistema();

        // Cria o endereco
        Endereco end = new Endereco();
        //consulta endereço
        end = si.ConEndereco(2);
        //deletar o endereco
        si.DeletarEndereco(end.getIdendereco());
        // Cria o endereco
        Tarefa ta = new Tarefa();
        //consulta endereço
        ta = si.ConTarefa(2);
        //deletar o endereco
        si.DelatarTarefa(ta.getIdtarefa());
                
        // Cria um pessoa
        Pessoa pes = new Pessoa();
        // aqui pesquisa pessoa 
        pes = si.ConPessoa(2);
        //deletar pessoa 
        si.DeletarPessoa(pes.getIdpessoa());

    }

}
