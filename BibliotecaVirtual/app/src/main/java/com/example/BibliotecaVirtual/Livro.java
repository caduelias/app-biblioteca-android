package com.example.BibliotecaVirtual;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

public class Livro {
    public Livro(Context context) {

        BancoDeDados.getInstance().abrirBanco(context);

        String sql = "CREATE TABLE IF NOT EXISTS livros (id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", autor TEXT" +
                ", titulo TEXT" +
                ", editora TEXT" +
                ", ano INTERGER" +
                ", numero_paginas INTERGER)";

        BancoDeDados.getInstance().executarSQL(sql);

        BancoDeDados.getInstance().adicionarNovaColuna("livros",
                "gazin", "TEXT", "");
    }

    private String addAspas(String texto) {
        return "'" + texto + "'";
    }

    public void salvar(String autor, String titulo, String editora) {
        ArrayList<HashMap<String,String>> lista = buscar(autor, "", "");
        System.out.println(lista);

        if (lista.size() > 0) {
            alterar(autor, titulo, editora);
        }
        else {
            inserir(autor, titulo, editora);
        }
    }

    private void inserir(String autor, String titulo, String editora) {
        System.out.println(autor);
        System.out.println(titulo);
        System.out.println(editora);
        String sql = "INSERT INTO livros (autor, titulo, editora) VALUES (" + addAspas(autor) + ", " + addAspas(titulo) + ", " + addAspas(editora) + ")";

        BancoDeDados.getInstance().executarSQL(sql);

    }

    private void alterar(String autor, String titulo, String editora) {
        System.out.println(autor);
        System.out.println(titulo);
        System.out.println(editora);

        String sql = "UPDATE livros SET " +
                " autor = " + addAspas(autor) + ", " +
                " titulo = " + addAspas(titulo) + ", " +
                " editora = " + addAspas(editora) + "" +
                "WHERE autor = " + addAspas(autor);

        BancoDeDados.getInstance().executarSQL(sql);
        System.out.println("Atualizado!");

    }

    public void excluir(String autor) {

        String sql = "DELETE FROM livros WHERE autor = " + addAspas(autor);

        BancoDeDados.getInstance().executarSQL(sql);
    }

    public ArrayList<HashMap<String, String>> buscar(String autor, String titulo, String editora) {
        try {
            String condicaoSQL = "";
            String operadorSQL = "";

            if (autor.isEmpty() == false) {
                condicaoSQL = "UPPER(autor) LIKE UPPER(" +
                        addAspas("%" + autor + "%") + ")";
                operadorSQL = " AND ";
            }
            if (titulo.isEmpty() == false) {
                condicaoSQL += operadorSQL + "titulo = " + addAspas(titulo);
                operadorSQL = " AND ";
            }
            if (editora.isEmpty() == false) {
                condicaoSQL += operadorSQL + "UPPER(editora) LIKE UPPER(" +
                        addAspas("%" + editora + "%") + ")";
                operadorSQL = " AND ";
            }

            Cursor cursor = BancoDeDados.getInstance().getBancoDados().query(
                    "livros", //nome da tabela
                    new String[]{"autor", "titulo", "editora"}, //campos retornados na busca
                    condicaoSQL, //condicao do WHERE da busca
                    null, //argumentos do WHERE, caso exista
                    null, //clausula GROUP BY
                    null, //clausula HAVING
                    "autor", //ORDER BY, ordenacao da busca
                    null //limite de registros
            );
            System.out.println(retornarLista(cursor) + "teste");
            return retornarLista(cursor);
        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());
            return new ArrayList<HashMap<String, String>>();
        }
    }
        private ArrayList<HashMap<String, String>> retornarLista(Cursor cursor) {
            try {
                ArrayList<HashMap<String, String>> listaRetorno =
                        new ArrayList<HashMap<String, String>>();

                int campoAutor = cursor.getColumnIndex("autor");
                int campoTitulo = cursor.getColumnIndex("titulo");
                int campoEditora = cursor.getColumnIndex("editora");

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    for (int i = 0; i < cursor.getCount(); i++) {
                        HashMap<String,String> item = new HashMap<>();

                        item.put("autor", cursor.getString(campoAutor));
                        item.put("titulo", cursor.getString(campoTitulo));
                        item.put("editora", cursor.getString(campoEditora));

                        listaRetorno.add(item);

                        cursor.moveToNext();
                    }
                }

                return listaRetorno;
            }
            catch (Exception e) {
                System.out.println("Erro ao montar lista: " + e.getMessage());
                return new ArrayList<HashMap<String, String>>();
            }
        }
}
