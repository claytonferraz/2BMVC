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
public class EditarPessoa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Sistema si = new Sistema();

        // Cria um pessoa
        Pessoa pes = new Pessoa();
        // aqui pesquisa pessoa 
        pes = si.ConPessoa(1);
        // altera o nome
        pes.setNome("Jose das Alfaces");
        //editar pessoa 
        si.EditPessoa(pes);
        // Cria o endereco
        Endereco end = new Endereco();
        //consulta endereço
        end = si.ConEndereco(1);
        //altera o bairro
        end.setBairro("Centro");
        //editar no banco o endereco
        si.EditEndereco(end);

    }

}
