/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testes;

import controller.Sistema;
import model.Endereco;
import model.Pessoa;

/**
 *
 * @author Clayton Ferraz
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Sistema si = new Sistema();

        Endereco end = new Endereco();
        end = si.ConEndereco(2);
        System.out.println(end.getRua());
        System.out.println(end.getIdendereco());

//        end.setBairro("Jardim America");
//        end.setRua("Avenida Curitiba");
//        end.setUf("RO");
//        // Cria uma nova pessoa
//        Pessoa pes = new Pessoa(); 
//        // Cadastra os dados da pessoa
//        pes.setCpf("12345678911");
//        pes.setNome("Jose das Couves");
//        
        //Classe que cadastra no banco
        //Cadastro uma nova pessoa
        //si.CadPessoa(pes);
        //Crio a relação pessoa endereco fk
//        Pessoa pessoacon = new Pessoa();
//        pessoacon = si.ConPessoa(2);
//        //end.setIdpessoafk(pessoacon);
        //cadastro endereco
        // si.CadEndereco(end);
//        si.EditEndereco(end);
//        
    }

}
