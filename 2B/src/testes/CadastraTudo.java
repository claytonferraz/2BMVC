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
public class CadastraTudo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Sistema si = new Sistema();
        Endereco end = new Endereco();
        end.setBairro("Jardim America");
        end.setRua("Avenida Curitiba");
        end.setUf("RO");
        // Cria uma nova pessoa
        Pessoa pes = new Pessoa();
        // Cadastra os dados da pessoa
        pes.setCpf("12345678911");
        pes.setNome("Jose das Couves");
        //Classe que cadastra no banco
        // Cadastro uma nova pessoa
        si.CadPessoa(pes);
        //Crio a relação pessoa endereco fk        
        end.setIdpessoafk(pes);
        //cadastro endereco
        si.CadEndereco(end);
        //cadastro de tarefa
        Tarefa ta = new Tarefa();
        ta.setData("04/02/2016");
        ta.setDescricao("Aula 2B - Top do Goiás");
        ta.setIdpessoa(pes);

        // cadastrar uma tarefa
        si.CadTarefa(ta);

    }

}
