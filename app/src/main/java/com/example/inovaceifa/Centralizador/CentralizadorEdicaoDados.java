package com.example.inovaceifa.Centralizador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.inovaceifa.Utilities.HelperDatabaseSQL;
import com.example.inovaceifa.Utilities.Imagem;
import com.example.inovaceifa.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Activity que cuida da edição dos dados proeminentes de uma gleba já cadastrada no sistema
 *
 * @author Gustavo Henrique Tostes
 * @version 1.0 - 19/01/2023
 */
public class CentralizadorEdicaoDados extends AppCompatActivity {
    /**
     * {@link Integer} - Código para a seleção de uma imagem da galeria para a gleba.
     */
    private final int SELECT_PICTURE = 200;

    /**
     * {@link Bitmap} - Representa a imagem atual da gleba.
     */
    private Bitmap imagem_gleba;

    /**
     * {@link Bitmap} - Representa a imagem atualizada da gleba (após o usuário selecionar uma nova da galeria).
     */
    private Bitmap imagem_atualizada = null;

    /**
     * {@link CircleImageView} - Representa o campo, área, onde a imagem fica contida.
     */
    private CircleImageView imagem;

    /**
     * {@link CircleImageView} - Botão de confirmação para a edição de imagem/dados na gleba feita pelo usuário.
     */
    private CircleImageView confirm;

    /**
     * {@link CircleImageView} - Botão de cancelamento das edições de imagem/dados na gleba.
     */
    private CircleImageView cancela;

    /**
     * {@link CircleImageView} - Botão para alteração da imagem na edição da gleba.
     */
    private Button alterar_imagem;

    /**
     * {@link FloatingActionButton} - Botão de rotação da imagem selecionada pelo usuário para representar a gleba.
     */
    private FloatingActionButton edicao;

    /**
     * {@link Imagem} - Objeto para manipulações de imagem.
     */
    private final Imagem image = new Imagem();

    /**
     * {@link Operador} - Objeto da gleba "recuperado" através do banco de dados para acesso aos dados da mesma.
     */
    private Operador operador;

    /**
     * {@link HelperDatabaseSQL} - Objeto do banco de dados para a devida utilização dos métodos referentes à ele.
     */
    private HelperDatabaseSQL helper;

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centralizador_edicao_dados);

        helper = HelperDatabaseSQL.getInstance(this);

        Intent intent = getIntent();
        String numero_Gleba = (String) intent.getSerializableExtra("numeroOperador");

        iniciaComponentes();

        operador = helper.searchOperadorByNumber(numero_Gleba);

        atualizaImagens();

        configuraConfirm();

        configuraCancela();

        configuraAlterarImagem();

        configuraEdicao();
    }

    /**
     * Inicializa os componentes através do método {@link android.app.Activity#findViewById(int)}.
     */
    private void iniciaComponentes() {
        confirm = findViewById(R.id.gerEd_confirma);
        cancela = findViewById(R.id.gerEd_cancela);
        alterar_imagem = findViewById(R.id.gerEd_editarImagem);
        imagem = findViewById(R.id.gerEd_imagemGleba);
        edicao = findViewById(R.id.gerEd_fab);
    }

    /**
     * Atualiza as imagens dispostas na tela conforme presença ou não de uma imagem já previamente configurada
     * na gleba.
     */
    private void atualizaImagens() {
        imagem_gleba = image.pathToImage(operador.getImagemPath());

        if (imagem_gleba != null) {
            imagem.setImageBitmap(imagem_gleba);
        }
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorEdicaoDados#confirm}.
     */
    private void configuraConfirm() {
        confirm.setOnClickListener(v -> {
            EditText campo1 = findViewById(R.id.gerEdit_campo1);
            if (!((campo1.getText().toString()).equals(""))) {
                operador.setLocal(campo1.getText().toString());
                helper.updateLocalOperador(operador);
            }

            if (imagem_atualizada != null) {
                runOnUiThread(() -> {
                    File internalStorage = CentralizadorEdicaoDados.this.getDir("GerenciadorImages",
                            Context.MODE_PRIVATE);
                    File imageFilePath = new File(internalStorage, operador.getNumero() + ".png");
                    String imagePath = imageFilePath.toString();

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(imageFilePath);
                        imagem_atualizada.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.close();
                    } catch (Exception ex) {
                        Log.i("DATABASE", "Problem updating picture", ex);
                        imagePath = "";
                    }

                    operador.setImagemPath(imagePath);
                    helper.updateImagemOperador(operador, imagem_atualizada, CentralizadorEdicaoDados.this);
                });
            }
            finish();
        });
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorEdicaoDados#cancela}.
     */
    private void configuraCancela() {
        cancela.setOnClickListener(v -> finish());
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorEdicaoDados#alterar_imagem}.
     */
    private void configuraAlterarImagem() {
        alterar_imagem.setOnClickListener(v -> escolherImagem());
    }

    /**
     * Configura os parâmetros referentes ao {@link CentralizadorEdicaoDados#edicao}.
     */
    private void configuraEdicao() {
        edicao.setOnClickListener(v -> {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            if (imagem_atualizada != null) {
                imagem_atualizada = Bitmap.createBitmap(imagem_atualizada, 0, 0,
                        imagem_atualizada.getWidth(),imagem_atualizada.getHeight(), matrix, true);
                imagem.setImageBitmap(imagem_atualizada);
            } else {
                imagem_atualizada = Bitmap.createBitmap(imagem_gleba, 0, 0,
                        imagem_gleba.getWidth(),imagem_gleba.getHeight(), matrix, true);
                imagem.setImageBitmap(imagem_atualizada);
            }
        });
    }

    /**
     * Inicia a "Activity" para a escolha de uma imagem, na galeria do usuário.
     */
    private void escolherImagem() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                try {
                    imagem_atualizada = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Bitmap bitmapImage = imagem_atualizada;
                    int altura = (int) ( bitmapImage.getHeight() * (720.0 / bitmapImage.getWidth()) );
                    imagem_atualizada = Bitmap.createScaledBitmap(bitmapImage, 720, altura, true);
                    imagem.setImageBitmap(imagem_atualizada);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}