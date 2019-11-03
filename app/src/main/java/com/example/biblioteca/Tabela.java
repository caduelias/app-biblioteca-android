package com.example.biblioteca;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Tabela {

    public Tabela(Context context) {
        //abrindo a conexao com o banco de dados
        Banco.getInstance().abrirBanco(context);

        //SQL para criar a tabela no banco de dados, caso ela ainda nao exista
        String sql = "CREATE TABLE IF NOT EXISTS tbBiblioteca (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descricao TEXT, autor TEXT, editora TEXT, ano DATE, paginas INTEGER )";

        //executando o SQL no banco de dados
        Banco.getInstance().executarSQL(sql);
    }

    private String addAspas(String texto) {
        //adicionando Aspas Simples antes e depois do texto
        return "'" + texto + "'";
    }

    public void salvar(String titulo, String descricao, String autor, String editora, String data, String paginas) {
        //buscando no banco pelo RA passado por parametro
        ArrayList<HashMap<String,String>> lista = buscar(  "", titulo, "");

        //se encontrou um aluno com este RA, chama o alterar, senao insere
        if (lista.size() > 0) {
            alterar(titulo, descricao, autor, editora, data, paginas);
        }
        else {
            inserir(titulo, descricao, autor, editora, data, paginas);
        }
    }

    private void inserir(String titulo, String descricao, String autor, String editora, String ano, String paginas) {
        //montando SQL para inserir o aluno na tabela
        String sql = "INSERT INTO tbBiblioteca (titulo, descricao, autor, editora, ano, paginas) VALUES (" +
                addAspas(titulo) + ", " + addAspas(descricao) + ", " + addAspas(autor) +  ", " + addAspas(editora) +   ", " + addAspas(ano) +  ", " + addAspas(paginas) + ")";

        //executando o SQL de INSERT no banco de dados
        Banco.getInstance().executarSQL(sql);
    }

    private void alterar(String titulo, String descricao, String autor, String editora, String ano, String paginas) {
        //montando SQL para editar o aluno na tabela
        String sql = "UPDATE tbBiblioteca SET " +
                " titulo = " + addAspas(titulo) + ", " +
                " descricao = " + addAspas(descricao) + " " +
                " autor = " + addAspas(autor) + " " +
                " editora = " + addAspas(editora) + " " +
                " ano = " + addAspas(ano) + " " +
                " paginas = " + addAspas(paginas) + " " +
                "WHERE titulo = " + addAspas(titulo);

        //executando o SQL de UPDATE no banco de dados
        Banco.getInstance().executarSQL(sql);
    }

    public void excluir(String titulo) {
        //montando SQL para excluir o aluno na tabela
        String sql = "DELETE FROM tbBiblioteca WHERE titulo = " + addAspas(titulo);

        //executando o SQL de DELETE no banco de dados
        Banco.getInstance().executarSQL(sql);
    }

    public ArrayList<HashMap<String, String>> buscar(String titulo, String autor, String editora) {
        try {
            String condicaoSQL = "";
            String operadorSQL = "";

            //montando a condicao do SQL de acordo com os parametros recebidos
            if (titulo.isEmpty() == false) {
                condicaoSQL = "UPPER(titulo) LIKE UPPER(" +
                        addAspas("%" + titulo + "%") + ")";
                operadorSQL = " AND ";
            }
            if (autor.isEmpty() == false) {
                condicaoSQL += operadorSQL + "autor = " + addAspas(autor);
                operadorSQL = " AND ";
            }
            if (editora.isEmpty() == false) {
                condicaoSQL += operadorSQL + "UPPER(editora) LIKE UPPER(" +
                        addAspas("%" + editora + "%") + ")";
                operadorSQL = " AND ";
            }

            //criando uma busca usando o metodo QUERY do banco de dados
            Cursor cursor = Banco.getInstance().getBancoDados().query(
                    "tbBiblioteca", //nome da tabela
                    new String[] {"titulo", "descricao", "autor", "editora", "ano", "paginas"}, //campos retornados na busca
                    condicaoSQL, //condicao do WHERE da busca
                    null, //argumentos do WHERE, caso exista
                    null, //clausula GROUP BY
                    null, //clausula HAVING
                    "titulo", //ORDER BY, ordenacao da busca
                    null //limite de registros
            );

            //retornando a lista (array) com os registros do banco de dados
            return retornarLista(cursor);
        }
        catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());
            //caso de erro, retorna uma lista vazia
            return new ArrayList<HashMap<String, String>>();
        }
    }

    private ArrayList<HashMap<String, String>> retornarLista(Cursor cursor) {
        try {
            //criando a lista que sera retornada pelo metodo
            ArrayList<HashMap<String, String>> listaRetorno =
                    new ArrayList<HashMap<String, String>>();

            //pegando o indice de cada coluna do banco
            int campoTitulo = cursor.getColumnIndex("titulo");
            int campoDescricao = cursor.getColumnIndex("descricao");
            int campoAutor = cursor.getColumnIndex("autor");
            int campoEditora = cursor.getColumnIndex("editora");
            int campoAno = cursor.getColumnIndex("ano");
            int campoPaginas = cursor.getColumnIndex("paginas");

            //se existem dados retornados do banco de dados
            if (cursor.getCount() > 0) {
                //move o cursor para a primeira posicao
                cursor.moveToFirst();

                //FOR para rodas todos os itens do cursor (registros do banco)
                for (int i = 0; i < cursor.getCount(); i++) {
                    HashMap<String,String> item = new HashMap<>();
                    //adicionando o valor do banco (cursor) no item HASH
                    item.put("titulo", cursor.getString(campoTitulo));
                    item.put("descricao", cursor.getString(campoDescricao));
                    item.put("autor", cursor.getString(campoAutor));
                    item.put("editora", cursor.getString(campoEditora));
                    item.put("ano", cursor.getString(campoAno));
                    item.put("paginas", cursor.getString(campoPaginas));


                    //adicionando o item na lista de retorno
                    listaRetorno.add(item);

                    //movendo o cursor para o proximo registro
                    cursor.moveToNext();
                }
            }

            return listaRetorno;
        }
        catch (Exception e) {
            System.out.println("Erro ao montar lista: " + e.getMessage());
            //caso de erro, retorna uma lista vazia
            return new ArrayList<HashMap<String, String>>();
        }
    }
}
