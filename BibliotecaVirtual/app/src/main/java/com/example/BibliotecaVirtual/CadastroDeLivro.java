package com.example.BibliotecaVirtual;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroDeLivro extends AppCompatActivity {
    EditText edtAutor, edtTitulo, edtEditora;
    Button btnCancelar, btnSalvar, btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_livro);

        edtAutor = findViewById(R.id.edtAutor);
        edtTitulo = findViewById(R.id.edtTitulo);
        edtEditora = findViewById(R.id.edtEditora);

        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnExcluir = findViewById(R.id.btnExcluir);

        Intent caminhoTela = getIntent();
        if (caminhoTela != null) {
            Bundle parans = caminhoTela.getExtras();

            if (parans != null) {

                edtAutor.setText(parans.getString("autor"));
                edtTitulo.setText(parans.getString("titulo"));
                edtEditora.setText(parans.getString("editora"));

                edtAutor.setEnabled(false);
            }
        }

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAutor.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Autor é obrigatório!");
                    return;
                }
                if (edtTitulo.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Titulo é obrigatório!");
                    return;
                }
                if (edtEditora.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Editora é obrigatório!");
                    return;
                }

                try {
                    Livro livro = new Livro(
                            CadastroDeLivro.this);

                    livro.salvar(edtAutor.getText().toString(),edtTitulo.getText().toString(),
                            edtEditora.getText().toString());

                    onBackPressed();
                }
                catch (Exception e) { }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta =
                        new AlertDialog.Builder(CadastroDeLivro.this);

                alerta.setTitle("Biblioteca Virtual");

                alerta.setMessage("Deseja realmente excluir este livro?");

                alerta.setNegativeButton("Não", null);

                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Livro tbAluno = new Livro(CadastroDeLivro.this);

                        tbAluno.excluir(edtAutor.getText().toString());

                        onBackPressed();
                    }
                });

                alerta.show();
            }
        });
    }

    private void mostrarMensagem(String texto) {

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        alerta.setTitle("Biblioteca Virtual");

        alerta.setMessage(texto);

        alerta.setNeutralButton("OK", null);

        alerta.show();
    }
}
