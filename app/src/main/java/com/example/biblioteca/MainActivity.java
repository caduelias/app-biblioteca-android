package com.example.biblioteca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {

    EditText edtBuscar;
    Button btnAdicionar, btnBuscar;
    RadioButton rbtTitulo, rbtAutor, rbtEditora;
    ArrayList<HashMap<String,String>> listaLivros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //verificando se tem permissao para gravar no disco
        verificarPermissao(
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnBuscar = findViewById(R.id.btnBuscar);
        edtBuscar = findViewById(R.id.edtBuscar);
        rbtAutor = findViewById(R.id.rbtAutor);
        rbtEditora = findViewById(R.id.rbtEditora);
        rbtTitulo= findViewById(R.id.rbtTitulo);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarLivros();
            }
        });

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //criando o caminho para a tela de cadastro
                Intent tela = new Intent(
                        MainActivity.this,
                        Cadastro.class);

                //abrindo a tela de cadastro
                startActivity(tela);
            }
        });
    }

    //onResumo é executado toda vez que a
    //   tela aparece (fica visivel)
    @Override
    protected void onResume() {
        super.onResume();

        //buscando todos os alunos cadastrados
        buscarLivros();
    }

    private void buscarLivros() {
        //criando as variaveis que receberao o valor da busca
        String titulo = "";
        String autor = "";
        String editora = "";

        //verifica qual item esta marcado para buscar
        if (rbtTitulo.isChecked())
            titulo = edtBuscar.getText().toString();
        else if (rbtAutor.isChecked())
            autor = edtBuscar.getText().toString();
        else if (rbtEditora.isChecked())
            editora = edtBuscar.getText().toString();

        //criando a classe responsavel pela tabela aluno
        Tabela tbBiblioteca = new Tabela(this);

        //buscando os alunos, passando o filtro da busca
        listaLivros = tbBiblioteca.buscar(titulo, autor, editora);

        //criando o adaptador de dados para exibir os dados dos
        //  alunos na listagem, usando o modelo
        ListAdapter adapter = new SimpleAdapter(this,
                listaLivros, R.layout.listview_modelo,
                new String[] {"titulo", "autor", "editora"},
                new int[] {R.id.lblTitulo, R.id.lblAutor, R.id.lblEditora});

        //adicionando na lista da tela (listView) o adaptador criador
        setListAdapter(adapter);
    }

    private void verificarPermissao(String nomePermissao) {
        //verificando se o usuario ja deu ou nao a permissao
        if (ContextCompat.checkSelfPermission(this,
                nomePermissao) != PackageManager.PERMISSION_GRANTED) {

            //verifica se o usuario ja negou a permissao
            //  e marcou a opcao de nao ver novamento
            //if (ActivityCompat.shouldShowRequestPermissionRationale(
            //                                      this, nomePermissao))

            //solicitando a permissao
            ActivityCompat.requestPermissions(this,
                    new String[] {nomePermissao}, 0);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //criando o caminho para abrir a tela de cadastro
        Intent tela = new Intent(MainActivity.this,
                Cadastro.class);

        //carregando os dados do aluno selecionado
        HashMap<String,String> livro = listaLivros.get(position);

        //criando a classe de parametros
        Bundle parans = new Bundle();

        //adicionando os dados do aluno selecionado nos parans
        parans.putString("titulo", livro.get("titulo"));
        parans.putString("descricao", livro.get("descricao"));
        parans.putString("autor", livro.get("autor"));
        parans.putString("editora", livro.get("editora"));
        parans.putString("ano", livro.get("ano"));
        parans.putString("paginas", livro.get("paginas"));

        //adicionando os parametros no caminho
        tela.putExtras(parans);

        //abrindo a tela com os parametros
        startActivity(tela);
    }

}
