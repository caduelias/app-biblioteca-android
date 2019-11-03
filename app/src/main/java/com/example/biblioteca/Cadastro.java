package com.example.biblioteca;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Cadastro extends AppCompatActivity {

    EditText edtTitulo, edtAutor, edtEditora, edtData, edtPagina, edtDescricao;
    Button btnCancelar, btnSalvar, btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtTitulo = findViewById(R.id.edtTitulo);
        edtAutor = findViewById(R.id.edtAutor);
        edtEditora = findViewById(R.id.edtEditora);
        edtData = findViewById(R.id.edtData);
        edtPagina = findViewById(R.id.edtPagina);
        edtDescricao = findViewById(R.id.edtDescricao);

        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnExcluir = findViewById(R.id.btnExcluir);

        //capturando o caminho que foi utilizado para abrir a tela
        Intent caminhoTela = getIntent();
        if (caminhoTela != null) {
            //capturando os parametros enviados para esta tela
            Bundle parans = caminhoTela.getExtras();

            //verifica se existem parametros enviados
            if (parans != null) {
                //populando os campos da tela com os recebidos da principal
                edtTitulo.setText(parans.getString("titulo"));
                edtAutor.setText(parans.getString("autor"));
                edtEditora.setText(parans.getString("editora"));

                //desabilitando o RA, pois é nossa chave no banco
                edtTitulo.setEnabled(false);
            }
        }

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ao cancelar, chama o evento de voltar do
                //  do android para fechar a tela
                onBackPressed();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validando o Nome, RA e Cidade, para nao salvar vazio
                if (edtTitulo.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Nome é obrigatório!");
                    return;
                }
                if (edtAutor.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo RA é obrigatório!");
                    return;
                }
                if (edtEditora.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Cidade é obrigatório!");
                    return;
                }
                if (edtData.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Cidade é obrigatório!");
                    return;
                }
                if (edtPagina.getText().toString().isEmpty()) {
                    mostrarMensagem("O campo Cidade é obrigatório!");
                    return;
                }

                try {
                    Tabela tbBiblioteca = new Tabela(
                            Cadastro.this);

                    //salvado os dados do aluno
                    tbBiblioteca.salvar(edtTitulo.getText().toString(),
                            edtDescricao.getText().toString(),
                            edtAutor.getText().toString(),
                            edtEditora.getText().toString(),
                    edtData.getText().toString(),
                    edtPagina.getText().toString()

                    );

                    //fechando a tela ao salvar
                    onBackPressed();
                }
                catch (Exception e) { }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //criando a mensagem de alerta
                AlertDialog.Builder alerta =
                        new AlertDialog.Builder(Cadastro.this);

                //adicionando um titulo a mensagem
                alerta.setTitle("Biblioteca");

                //adicionando a pergunta de validacao na mensagem
                alerta.setMessage("Deseja realmente excluir este livro?");

                //criando a opcao negativa, que so fecha a mensagem
                alerta.setNegativeButton("Não", null);

                //criando a opcao positiva, que ira excluir o aluno
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Tabela tbBiblioteca = new Tabela(Cadastro.this);

                        //excluindo o aluno do banco de dados pelo RA
                        tbBiblioteca.excluir(edtTitulo.getText().toString());

                        //fechando a tela ao excluir
                        onBackPressed();
                    }
                });

                //exibindo a mensagem na tela
                alerta.show();
            }
        });
    }

    private void mostrarMensagem(String texto) {
        //criando a classe da mensagem de alerta
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        //adicionando um titulo para a mensagem
        alerta.setTitle("Biblioteca");

        //adicionando o texto recebido pelo metodo na mensagem/alerta
        alerta.setMessage(texto);

        //adicionando um botao de OK para fechar a mensagem
        alerta.setNeutralButton("OK", null);

        //exibindo a mensagem criada na tela
        alerta.show();
    }
}
