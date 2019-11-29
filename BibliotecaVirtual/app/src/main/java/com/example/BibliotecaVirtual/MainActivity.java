package com.example.BibliotecaVirtual;

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

    EditText                            edtBuscar;
    Button                              btnAdicionar;
    Button                              btnBuscar;
    RadioButton                         radioAutor;
    RadioButton                         radioTitulo;
    RadioButton                         radioEditora;
    ArrayList<HashMap<String,String>>   listarLivros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verificarPermissao(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        btnAdicionar        = findViewById(R.id.btnAdicionar);
        btnBuscar           = findViewById(R.id.btnBuscar);
        edtBuscar         = findViewById(R.id.edtBuscar);
        radioEditora        = findViewById(R.id.radioEditora);
        radioAutor          = findViewById(R.id.radioAutor);
        radioTitulo         = findViewById(R.id.radioTitulo);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarLivros();
            }
        });

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tela = new Intent(MainActivity.this , CadastroDeLivro.class);

                startActivity(tela);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
       buscarLivros();
    }

    private void buscarLivros() {
        String autor         = "";
        String titulo        = "";
        String editora       = "";

        if(radioAutor.isChecked()){
            autor = edtBuscar.getText().toString();
        }else if (radioTitulo.isChecked()) {
            titulo = edtBuscar.getText().toString();
        }else if (radioEditora.isChecked()) {
            editora = edtBuscar.getText().toString();
        }else {

        }

        Livro livro = new Livro (this);

        listarLivros = livro.buscar(autor,titulo,editora);

        ListAdapter adapter = new SimpleAdapter(this
                                                ,listarLivros
                                                ,R.layout.listview_modelo
                                                ,new String[] {"titulo" , "autor", "editora"}
                                                ,new int[] {R.id.lblTitulo, R.id.lblAutor, R.id.lblEditora}
                                                );

        setListAdapter(adapter);
    }

    private void verificarPermissao(String nomePermissao) {
        if(ContextCompat.checkSelfPermission(this,nomePermissao) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[] {nomePermissao},0);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent tela = new Intent(MainActivity.this,CadastroDeLivro.class);

        HashMap<String,String> livro = listarLivros.get(position);

        Bundle parans = new Bundle();
        parans.putString("titulo",livro.get("titulo"));
        parans.putString("autor",livro.get("autor"));
        parans.putString("editora",livro.get("editora"));

        tela.putExtras(parans);

        startActivity(tela);
    }
}
