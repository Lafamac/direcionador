package com.example.inovaceifa.Utilities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.inovaceifa.Centralizador.CentralizadorParametros;
import com.example.inovaceifa.Centralizador.Operador;
import com.example.inovaceifa.Centralizador.Tempo;

import java.util.ArrayList;

/**
 * Classe referente ao Banco de Dados, utilizando SQLite. A classe contém tudo que é pertinente às
 * ações do banco de dados, tais quais adicionar conteúdo, remover conteúdo, atualizar conteúdo, etc.
 * Observação: Quando colunas forem acrescentadas/removidas do SQLite, a versão anterior do aplicativo,
 * instalada no dispositivo do usuário, deve ser desinstalada e, em alguns casos, o cachê deve
 * ser limpo.
 *
 * @version 1.0 - 20/12/2022
 * @author Gustavo Henrique Tostes
 */
public class HelperDatabaseSQL extends SQLiteOpenHelper {

    //Informações inerentes ao banco de dados, tais quais seu nome e sua versão.
    private static final String DATABASE_NAME = "CEIFA_Database3.0";
    private static final int DATABASE_VERSION = 1;

    //Tabelas presentes no banco de dados.
    private static final String TABLE_OPERADOR = "gerenciador";
    private static final String TABLE_CENTRALIZADOR = "centralizador";
    private static final String TABLE_TEMPO = "tempo";

    //Colunas referentes às operadors cadastradas, contendo os tópicos necessários para caracterizácao
    private static final String KEY_OPERADOR_ID = "_id";
    private static final String KEY_OPERADOR_NUMERO = "numero";
    private static final String KEY_OPERADOR_LOCAL = "local";
    private static final String KEY_OPERADOR_IMAGEM = "imagemPath";
    private static final String KEY_OPERADOR_DESCRICAO = "descricao";
    private static final String KEY_OPERADOR_COORDENADAS = "coordenadas";
    private static final String KEY_OPERADOR_CULTIVAR = "cultivar";
    private static final String KEY_OPERADOR_ESPACAMENTO_PLANTAS = "espPlantas";
    private static final String KEY_OPERADOR_ESPACAMENTO_RUAS = "espRuas";
    private static final String KEY_OPERADOR_ALTURA_PLANTAS = "altPlantas";
    private static final String KEY_OPERADOR_NUMERO_RAMOS = "numeroRamos";
    private static final String KEY_OPERADOR_PORCENTAGEM_CEREJA = "porcentagemCereja";
    private static final String KEY_OPERADOR_PORCENTAGEM_VERDE = "porcentagemVerde";
    private static final String KEY_OPERADOR_RENDA = "renda";
    private static final String KEY_OPERADOR_MODELO = "modelo";
    private static final String KEY_OPERADOR_CARGA = "cargaPendente";
    private static final String KEY_OPERADOR_PLANTAS_HA = "plantasHa";
    private static final String KEY_OPERADOR_PRODUTIVIDADE = "produtividade";
    private static final String KEY_OPERADOR_LITROS_METRO = "litrosMetro";
    private static final String KEY_OPERADOR_PES = "pesContados";
    private static final String KEY_OPERADOR_PORCENTAGEM_CEREJA_PONTA = "porcentagemCerejaPonta";
    private static final String KEY_OPERADOR_PORCENTAGEM_CEREJA_SAIA = "porcentagemCerejaSaia";
    private static final String KEY_OPERADOR_PORCENTAGEM_VERDE_PONTA = "porcentagemVerdePonta";
    private static final String KEY_OPERADOR_PORCENTAGEM_VERDE_SAIA = "porcentagemVerdeSaia";
    private static final String KEY_OPERADOR_FORCA_CEREJA_PONTA = "forcaMediaCerejaPonta";
    private static final String KEY_OPERADOR_FORCA_VERDE_PONTA = "forcaMediaVerdePonta";
    private static final String KEY_OPERADOR_FORCA_CEREJA_SAIA = "forcaMediaCerejaSaia";
    private static final String KEY_OPERADOR_FORCA_VERDE_SAIA = "forcaMediaVerdeSaia";
    private static final String KEY_OPERADOR_FORCA_CEREJA = "forcaMediaCereja";
    private static final String KEY_OPERADOR_FORCA_VERDE = "forcaMediaVerde";
    private static final String KEY_OPERADOR_TIPO_COLHEITA = "tipoColheita";
    private static final String KEY_OPERADOR_QUANDO_COLHER = "quandoColher";
    private static final String KEY_OPERADOR_VELOCIDADE = "velocidade";
    private static final String KEY_OPERADOR_VIBRACAO = "vibracao";
    private static final String KEY_OPERADOR_FREIO = "freio";
    private static final String KEY_OPERADOR_VERDE_CLARO = "VerdeClaro";
    private static final String KEY_OPERADOR_VERDE_ESCURO_ESQUERDA = "VEEquerda";
    private static final String KEY_OPERADOR_VERDE_ESCURO_DIREITA = "VEDireita";
    private static final String KEY_OPERADOR_AMARELO_ESQUERDA = "AmareloEsquerda";
    private static final String KEY_OPERADOR_AMARELO_DIREITA = "AmareloDireita";
    private static final String KEY_OPERADOR_VERMELHO_ESQUERDA = "VermelhoEsquerda";
    private static final String KEY_OPERADOR_VERMELHO_DIREITA = "VermelhoDireita";
    private static final String KEY_OPERADOR_TEMPO_DIARIO_TRABALHADO = "TempoTrabalhado";
    private static final String KEY_OPERADOR_DIA_ATUAL = "diaAtual";

    //Colunas referentes aos parâmetros do gerenciador de Colheita
    private static final String KEY_CENT_ID = "centID";
    private static final String KEY_CENT_CONTROLE = "controleDados";
    private static final String KEY_CENT_ANGULO = "angulo";
    private static final String KEY_CENT_DIST_BARRA = "distanciaBarra";
    private static final String KEY_CENT_MENOR_DISTANCIA = "menorDistancia";
    private static final String KEY_CENT_MAIOR_DISTANCIA = "maiorDistancia";
    private static final String KEY_CENT_DIAMETRO = "diametro";
    private static final String KEY_CENT_TEMPO_ATT = "tempoAtt";
    private static final String KEY_CENT_PATH_1 = "filePath1";
    private static final String KEY_CENT_PATH_2 = "filePath2";
    private static final String KEY_CENT_PATH_3 = "filePath3";
    private static final String KEY_CENT_PATH_4 = "filePath4";
    private static final String KEY_CENT_PATH_5 = "filePath5";
    private static final String KEY_CENT_PATH_6 = "filePath6";
    private static final String KEY_CENT_PATH_7 = "filePath7";


    //Trabela TempoDeTrabalho
    private static final String KEY_TEMPO_ID = "tempoID";
    private static final String KEY_TEMPO_INICIO = "tempoInicio";
    private static final String KEY_TEMPO_FINAL = "tempoFinal";
    private static final String KEY_TEMPO_TOTAL = "tempoTotal";
    private static final String KEY_TEMPO_DATA = "tempoData";
    private static final String KEY_TEMPO_OPERADOR_ID = "tempoOperadorID";



    //----------------------------------------------------------------------------------------------
    private static HelperDatabaseSQL sInstance;
    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    /**
     * Método referente ao "hábito" de se criar o objeto do banco de dados apenas uma vez. Isso é
     * feito dado o fator de que o banco de dados pode ser utilizado durante toda a execução do
     * programa, economizando recursos do dispositivo.
     *
     * @param context "Contexto" da aplicação a ser utilizado (aplicativo)
     * @return Retorna a instância atual, ou a nova que foi criado (caso ainda não tenha sido criado)
     */
    public static synchronized HelperDatabaseSQL getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HelperDatabaseSQL(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Construtor da classe em questão.
     *
     * @param context "Contexto" da aplicação a ser utilizado (aplicativo)
     */
    private HelperDatabaseSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Configura para que o banco de dados possa ser usado de forma "externa".
     *
     * @param db Objeto do banco de dados.
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * Criação da tabela do banco de dados. Até o momento, todos os objetos são Strings
     *
     * @param db Objeto do banco de dados.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Criação da última versão da tabela do gerenciador de colheita.
        db.execSQL(createTableGerenc());

        //Criação da última versão da tabela do direcionador da colhedora.
        db.execSQL(createTableCent());


        //Criação da última versão da tabela DE Tempo do gerenciador.
        db.execSQL(createTableTempo());
    }

    /**
     * Método "inerente" à execução do código, sendo uma execução interna. Analisa se a versão
     * antiga, salva no dispositivo, é igual à mais recente. Caso não seja, "exclui" a versão
     * antiga e a substitui pela nova.
     *
     * @param db Objeto do banco de dados
     * @param oldVersion Versão antiga do banco de dados
     * @param newVersion Versão nova (atual) do banco de dados
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            if (oldVersion == 1) {
                switch (newVersion) {
                    case 2:
                        System.out.println("Versão anterior: " + oldVersion);
                        System.out.println("Versão atual: " + newVersion);
                }
            }
        }

        /*
            Modelo para atualização de tabelas (para cada nova coluna):
            db.execSQL("ALTER TABLE " + nome da tabela + " ADD COLUMN " + key da nova coluna +
            tipo dos dados (Exemplo: " TEXT");
         */
    }

    /**
     * Método para a criação de uma tabela referente aos dados de parametrização do centralizador
     * da colhedora. A tabela criada é referente à última versão.
     * @return String contendo o comando a ser utilizado para a criação de uma nova tabela.
     */
    private String createTableCent() {
        return ("CREATE TABLE " + TABLE_CENTRALIZADOR + "("+
                KEY_CENT_ID + " INTEGER," +
                KEY_CENT_CONTROLE + " INTEGER," +
                KEY_CENT_ANGULO + " INTEGER," +
                KEY_CENT_DIST_BARRA + " INTEGER," +
                KEY_CENT_MENOR_DISTANCIA + " INTEGER," +
                KEY_CENT_MAIOR_DISTANCIA + " INTEGER," +
                KEY_CENT_DIAMETRO + " INTEGER," +
                KEY_CENT_TEMPO_ATT + " REAL," +
                KEY_CENT_PATH_1 + " TEXT," +
                KEY_CENT_PATH_2 + " TEXT," +
                KEY_CENT_PATH_3 + " TEXT," +
                KEY_CENT_PATH_4 + " TEXT," +
                KEY_CENT_PATH_5 + " TEXT," +
                KEY_CENT_PATH_6 + " TEXT," +
                KEY_CENT_PATH_7 + " TEXT)");
    }


    private String createTableTempo() {
        return "CREATE TABLE " + TABLE_TEMPO + "(" +
                KEY_TEMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // ID único
                KEY_TEMPO_OPERADOR_ID+ " INTEGER," +                       // Chave estrangeira do operador
                KEY_TEMPO_INICIO + " TEXT," +                         // Horário de início (YYYY-MM-DD HH:mm:ss)
                KEY_TEMPO_FINAL + " TEXT," +
                KEY_TEMPO_TOTAL + " TEXT," +    // Horário de término (YYYY-MM-DD HH:mm:ss)
                KEY_TEMPO_DATA + " TEXT," +                           // Data do registro (YYYY-MM-DD)
                "FOREIGN KEY (" + KEY_TEMPO_OPERADOR_ID + ") REFERENCES " + TABLE_OPERADOR + "(" + KEY_OPERADOR_ID + ")" +
                ")";
    }

    /**
     * Método para a criação de uma tabela referente aos dados de das gelbas do gerenciador
     * de colheita. A tabela criada é referente à última versão.
     * @return String contendo o comando a ser utilizado para a criação de uma nova tabela.
     */
    private String createTableGerenc() {
        return "CREATE TABLE " + TABLE_OPERADOR + "(" +
                KEY_OPERADOR_NUMERO + " TEXT," +
                KEY_OPERADOR_LOCAL + " TEXT," +
                KEY_OPERADOR_IMAGEM + " TEXT," +
                KEY_OPERADOR_DESCRICAO + " TEXT," +
                KEY_OPERADOR_CULTIVAR + " TEXT," +
                KEY_OPERADOR_ESPACAMENTO_PLANTAS + " REAL," +
                KEY_OPERADOR_ESPACAMENTO_RUAS + " REAL," +
                KEY_OPERADOR_ALTURA_PLANTAS + " REAL," +
                KEY_OPERADOR_NUMERO_RAMOS + " INTEGER," +
                KEY_OPERADOR_RENDA + " INTEGER," +
                KEY_OPERADOR_MODELO + " TEXT," +
                KEY_OPERADOR_CARGA + " REAL," +
                KEY_OPERADOR_PLANTAS_HA + " INTEGER," +
                KEY_OPERADOR_PRODUTIVIDADE + " INTEGER," +
                KEY_OPERADOR_LITROS_METRO + " REAL," +
                KEY_OPERADOR_PORCENTAGEM_CEREJA + " INTEGER," +
                KEY_OPERADOR_PORCENTAGEM_VERDE + " INTEGER," +
                KEY_OPERADOR_PORCENTAGEM_CEREJA_PONTA + " INTEGER," +
                KEY_OPERADOR_PORCENTAGEM_CEREJA_SAIA + " INTEGER," +
                KEY_OPERADOR_PORCENTAGEM_VERDE_PONTA + " INTEGER," +
                KEY_OPERADOR_PORCENTAGEM_VERDE_SAIA + " INTEGER," +
                KEY_OPERADOR_FORCA_CEREJA_PONTA + " REAL," +
                KEY_OPERADOR_FORCA_VERDE_PONTA + " REAL," +
                KEY_OPERADOR_FORCA_CEREJA_SAIA + " REAL," +
                KEY_OPERADOR_FORCA_VERDE_SAIA + " REAL," +
                KEY_OPERADOR_FORCA_CEREJA + " REAL," +
                KEY_OPERADOR_FORCA_VERDE + " REAL," +
                KEY_OPERADOR_TIPO_COLHEITA + " TEXT," +
                KEY_OPERADOR_QUANDO_COLHER + " TEXT," +
                KEY_OPERADOR_VIBRACAO + " INTEGER," +
                KEY_OPERADOR_VELOCIDADE + " INTEGER," +
                KEY_OPERADOR_FREIO + " INTEGER," +
                KEY_OPERADOR_COORDENADAS + " TEXT," +
                KEY_OPERADOR_PES + " INTEGER," +
                KEY_OPERADOR_VERDE_CLARO + " INTEGER," +
                KEY_OPERADOR_VERDE_ESCURO_ESQUERDA + " INTEGER," +
                KEY_OPERADOR_VERDE_ESCURO_DIREITA + " INTEGER," +
                KEY_OPERADOR_AMARELO_ESQUERDA + " INTEGER," +
                KEY_OPERADOR_AMARELO_DIREITA + " INTEGER," +
                KEY_OPERADOR_VERMELHO_ESQUERDA + " INTEGER," +
                KEY_OPERADOR_VERMELHO_DIREITA + " INTEGER," +
                KEY_OPERADOR_TEMPO_DIARIO_TRABALHADO + " INTEGER," +
                KEY_OPERADOR_DIA_ATUAL + " TEXT" +
                ")";
        //NAO ESQUECER DA VIRGULA, menos no ultimo
    }

    /**
     * Listar todas as operadors para utilização de yma listagem, por exemplo, na tela
     * @return ArrayList com todas as operadors cadastradas no banco de dados
     */
    @SuppressLint("Range")
    public ArrayList<Operador> listarTodasOperadors() {
        String sql = "select * from " + TABLE_OPERADOR + " ORDER BY " + KEY_OPERADOR_NUMERO +
                " ASC ";

        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }

        ArrayList<Operador> operadorssss = new ArrayList<>();
        Cursor cursor = mReadableDB.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Operador nova_operador = setAll(cursor);
                operadorssss.add(nova_operador);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return operadorssss;
    }

    /**
     * Método para adicionar operadors à sua respectiva tabela no banco de dados, com todas as
     * características pertinentes.
     *
     * @param operador Objeto do tipo Operador, contendo as informações da mesma.
     * @return ID da Operador registrada; -1 se a inserção não for feita corretamente.
     */
    public void addOperador(Operador operador) {
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        //long operadorId = -1;
        ContentValues values = putAll(operador);

        mWritableDB.insert(TABLE_OPERADOR, null, values);
    }

    /**
     * Deleta TODAS as operadors presentes no banco de dados.
     */
    public void deleteAllOperadors() {
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }
        mWritableDB.beginTransaction();

        try{
            //Pode-se usar:
            //db.execSQL("DELETE FROM " + TABLE_OPERADOR);
            mWritableDB.delete(TABLE_OPERADOR, null, null);
            mWritableDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Erro ao apagar todas as operadors");
        } finally {
            mWritableDB.endTransaction();
        }
    }

    /**
     * Deleta apenas uma operador (uma linha do banco de dados).
     *
     * @param operador Objeto da operador a ser deletada.
     */
    public void deleteOneOperador(Operador operador) {
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }
        mWritableDB.beginTransaction();

        try {
            //db.delete(TABLE_OPERADOR, operador.numero + "= ?", null);
            mWritableDB.delete(TABLE_OPERADOR, KEY_OPERADOR_NUMERO + " = ?", new String[]{operador.getNumero()});
            mWritableDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Erro ao apagar a Operador desejada");
        } finally {
            mWritableDB.endTransaction();
        }
    }

    /**
     * Busca das operadors, a partir da sua posição.
     *
     * @param position Posição da operador.
     * @return Operador na posição requisitada.
     */
    @SuppressLint("Range")
    public Operador query(int position) {
        String query = "SELECT * FROM " + TABLE_OPERADOR + " ORDER BY " + KEY_OPERADOR_NUMERO +
                " ASC " + "LIMIT " + position + ",1";

        Cursor cursor = null;
        Operador nova_operador = new Operador();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();

            nova_operador = setAll(cursor);

        } catch (Exception e) {
            Log.d(TAG, "Não foi possível encontrar as operadors");
        } finally {
            cursor.close();
            return nova_operador;
        }
    }

    /**
     * Conta quantas entradas há no banco de dados.
     *
     * @return Número de entradas no banco de dados.
     */
    public long count() {
        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB, TABLE_OPERADOR);
    }

    /**
     * Procura uma operador a partir do número da mesma.
     * @param number Número que caracteriza a operador.
     * @return Cursor com todas as operadors encontradas com o número.
     */
    public Cursor searchNumber(String number) {
        String[] columns = new String[]{KEY_OPERADOR_NUMERO};
        String selection = KEY_OPERADOR_NUMERO + " LIKE ?";
        number = "%" + number + "%";
        String[] selectionArgs = new String[]{number};

        Cursor cursor = null;

        //String query = "SELECT * FROM " + TABLE_OPERADOR + " WHERE " + KEY_OPERADOR_NUMERO + " = " + "number";

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.query(TABLE_OPERADOR, columns, selection, selectionArgs, null, null, null);
            //cursor = mReadableDB.rawQuery(query, null);
        } catch (Exception e) {
            Log.d(TAG, "Não foi possível achar o número");
        }

        return cursor;
    }

    /**
     * Procura uma operador a partir do seu número. Diferente do método "searchNumber(String number),
     * dado que o mesmo procura apenas a coluna de números, enquanto esse "armazena" todas as
     * colunas.
     *
     * @param numero Número a ser pesquisado no banco de dados.
     * @return Um objeto do tipo Operador, contendo todas as informações obtidas a partir do número da
     * operador.
     */
    @SuppressLint("Range")
    public Operador searchOperadorByNumber(String numero) {
        Operador operador = null;

        String queryOperador = "SELECT * FROM " + TABLE_OPERADOR + " WHERE " + KEY_OPERADOR_NUMERO + " = '" + numero + "'";

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            Cursor cursor = mReadableDB.rawQuery(queryOperador, null);

            cursor.moveToFirst();

            if(numero.equals(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_NUMERO)))) {
                operador = setAll(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Não foi possível achar a operador em questão");
        }

        return operador;
    }

    /**
     * Atualização do local de uma operador.
     *
     * @param operador Objeto da Operador a ser atualizada
     * @return 1 caso o Update tenha sido realizado // 0 caso o Update não tenha sido realizado
     */
    public int updateLocalOperador(Operador operador){
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_OPERADOR_LOCAL, operador.getLocal());

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf(operador.getNumero())});
    }

    /**
     * Atualização da descrição de uma operador.
     *
     * @param operador Objeto da Operador a ser atualizada
     * @return 1 caso o Update tenha sido realizado // 0 caso o Update não tenha sido realizado
     */
    public int updateDescricaoOperador(Operador operador){
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_OPERADOR_DESCRICAO, operador.getDescricao());

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf(operador.getNumero())});
    }

    public int updateLedsOperador(Operador operador){
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_OPERADOR_VERDE_CLARO, operador.getNumeroLedsVerdeClaros());
        values.put(KEY_OPERADOR_VERDE_ESCURO_ESQUERDA, operador.getNumeroLedsVerdeEscurosEsquerda());
        values.put(KEY_OPERADOR_VERDE_ESCURO_DIREITA, operador.getNumeroLedsVerdeEscurosDireita());
        values.put(KEY_OPERADOR_AMARELO_ESQUERDA, operador.getNumeroLedsAmarelosEsquerda());
        values.put(KEY_OPERADOR_AMARELO_DIREITA, operador.getNumeroLedsAmarelosDireita());
        values.put(KEY_OPERADOR_VERMELHO_ESQUERDA, operador.getNumeroLedsVermelhosEsquerda());
        values.put(KEY_OPERADOR_VERMELHO_DIREITA, operador.getNumeroLedsVermelhosDireita());

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf(operador.getNumero())});
    }

    public int updateTempoTrabalhadoOperador(Operador operador){
        if(mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_OPERADOR_TEMPO_DIARIO_TRABALHADO, operador.getTempoDiarioTrabalhado());

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf((operador.getNumero()))});
    }

    public int updateDiaAtual(Operador operador){
        if(mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_OPERADOR_DIA_ATUAL, operador.getDiaAtual());

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf((operador.getNumero()))});
    }

    /**
     * Atualização do caminho (path) para a imagem de uma operador.
     *
     * @param operador Objeto da Operador a ser atualizada
     * @return 1 caso o Update tenha sido realizado // 0 caso o Update não tenha sido realizado
     */
    public int updateImagemOperador(Operador operador, Bitmap imagem, Context context) {


        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_OPERADOR_IMAGEM, operador.getImagemPath());

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf(operador.getNumero())});

        //===========================================
        /*if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_OPERADOR_IMAGEM, operador.getImagemPath());

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf(operador.getNumero())});*/
    }

    /**
     * Atualização de todos os parâmetros que descrevem a operador: Local, descrição, (FUTURAMENTE)
     * foto.
     *
     * @param operador Objeto da operador a ser atualizada.
     * @return 1 caso o Update tenha sido realizado // 0 caso o Update não tenha sido realizado
     */
    public int updateCaracteristicasOperador(Operador operador){
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_OPERADOR_LOCAL, operador.getLocal());
        values.put(KEY_OPERADOR_DESCRICAO, operador.getDescricao());
        values.put(KEY_OPERADOR_IMAGEM, operador.getImagemPath());

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf(operador.getNumero())});
    }

    /**
     * Atualização dos dados referentes aos resultados, após realizar todas as medições de força de
     * desprendimento. Atualiza os parâmetros: Tipo de Colheita, Quando a colheita deve ser realizada,
     * Vibração necessária, Velocidade necessária.
     * @param operador Objeto da operador a ser atualizada;
     * @return 1 caso o Update tenha sido realizado // 0 caso o Update não tenha sido realizado.
     */
    public int updateResultadosOperador (Operador operador) {
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();

        //AQUI FICAM OS RESULTADOS DA SEGUINTE FORMA:
        //values.put(*KEY*, operador.get*O que precisar*());

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf(operador.getNumero())});
    }

    /**
     * Atualização de TUDO referente à operador. Tanto as características que definem a operador
     * (descrição, foto e local) como os dados da lavoura.
     *
     * @param operador Objeto da operador a ser atualizada.
     * @return 1 caso o Update tenha sido realizado // 0 caso o Update não tenha sido realizado.
     */
    public int updateTudoOperador(Operador operador){
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = putAll(operador);

        return mWritableDB.update(TABLE_OPERADOR, values, KEY_OPERADOR_NUMERO + "= ?",
                new String[]{String.valueOf(operador.getNumero())});
    }

    /**
     * Seta todos os parâmetros referentes à uma operador em um objeto.
     * @param cursor Cursor do banco de dados, o qual "movimenta" através das colunas.
     * @return Objeto da operador "configurada".
     */
    @SuppressLint("Range")
    private Operador setAll(Cursor cursor) {
        Operador nova_operador = new Operador();
        nova_operador.setNumero(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_NUMERO)));
        nova_operador.setLocal(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_LOCAL)));
        nova_operador.setDescricao(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_DESCRICAO)));
        nova_operador.setImagemPath(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_IMAGEM)));
        nova_operador.setNumeroLedsVerdeClaros(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_VERDE_CLARO))));
        nova_operador.setNumeroLedsVerdeEscurosEsquerda(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_VERDE_ESCURO_ESQUERDA))));
        nova_operador.setNumeroLedsVerdeEscurosDireita(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_VERDE_ESCURO_DIREITA))));
        nova_operador.setNumeroLedsAmarelosEsquerda(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_AMARELO_ESQUERDA))));
        nova_operador.setNumeroLedsAmarelosDireita(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_AMARELO_DIREITA))));
        nova_operador.setNumeroLedsVermelhosEsquerda(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_VERMELHO_ESQUERDA))));
        nova_operador.setNumeroLedsVermelhosDireita(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_VERMELHO_DIREITA))));
        nova_operador.setTempoDiarioTrabalhado(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_TEMPO_DIARIO_TRABALHADO))));
        nova_operador.setDiaAtual(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_DIA_ATUAL)));

        return nova_operador;
    }
    @SuppressLint("Range")
    private Tempo setAllTempo(Cursor cursor) {
        Tempo novoTempo = new Tempo();

        // Preenchendo os atributos do objeto `Tempo`
        novoTempo.setTempoID(cursor.getInt(cursor.getColumnIndex(KEY_TEMPO_ID)));
        novoTempo.setOperadorID(cursor.getInt(cursor.getColumnIndex(KEY_OPERADOR_ID)));
        novoTempo.setTempoInicio(cursor.getString(cursor.getColumnIndex(KEY_TEMPO_INICIO)));
        novoTempo.setTempoFinal(cursor.getString(cursor.getColumnIndex(KEY_TEMPO_FINAL)));
        novoTempo.setTempoTotal(cursor.getString(cursor.getColumnIndex(KEY_TEMPO_TOTAL)));
        novoTempo.setTempoData(cursor.getString(cursor.getColumnIndex(KEY_TEMPO_DATA)));

        return novoTempo;
    }


    /**
     * Coloca todos os parâmetros referentes à uma operador em uma variável a ser utilizada para realizar
     * um update ou mesmo uma inserção.
     * @param operador Objeto da operador contendo os valores "analisados".
     * @return Objeto do tipo ContentValues.
     */
    private ContentValues putAll(Operador operador) {
        ContentValues values = new ContentValues();

        values.put(KEY_OPERADOR_NUMERO, operador.getNumero());
        values.put(KEY_OPERADOR_LOCAL, operador.getLocal());
        values.put(KEY_OPERADOR_DESCRICAO, operador.getDescricao());
        values.put(KEY_OPERADOR_IMAGEM, operador.getImagemPath());
        values.put(KEY_OPERADOR_VERDE_CLARO, operador.getNumeroLedsVerdeClaros());
        values.put(KEY_OPERADOR_VERDE_ESCURO_ESQUERDA, operador.getNumeroLedsVerdeEscurosEsquerda());
        values.put(KEY_OPERADOR_VERDE_ESCURO_DIREITA, operador.getNumeroLedsVerdeEscurosDireita());
        values.put(KEY_OPERADOR_AMARELO_ESQUERDA, operador.getNumeroLedsAmarelosEsquerda());
        values.put(KEY_OPERADOR_AMARELO_DIREITA, operador.getNumeroLedsAmarelosDireita());
        values.put(KEY_OPERADOR_VERMELHO_ESQUERDA, operador.getNumeroLedsVermelhosEsquerda());
        values.put(KEY_OPERADOR_VERMELHO_DIREITA, operador.getNumeroLedsVermelhosDireita());
        values.put(KEY_OPERADOR_TEMPO_DIARIO_TRABALHADO, operador.getTempoDiarioTrabalhado());
        values.put(KEY_OPERADOR_DIA_ATUAL, operador.getDiaAtual());

        return values;
    }
    private ContentValues putAllTempo(Tempo tempo) {
        ContentValues values = new ContentValues();

        values.put(KEY_TEMPO_ID, tempo.getTempoID());
        values.put(KEY_TEMPO_INICIO, tempo.getTempoInicio().toString()); // Converte para string, caso seja um objeto DateTime
        values.put(KEY_TEMPO_FINAL, tempo.getTempoFinal().toString());
        values.put(KEY_TEMPO_TOTAL, tempo.getTempoTotal().toString()); // Converte para string
        values.put(KEY_TEMPO_DATA, tempo.getTempoData());
        values.put(KEY_OPERADOR_ID, tempo.getOperadorID()); // Chave estrangeira associada ao operador

        return values;
    }


    //----------------------------------------------------------------------------------------------
    //                                  Centralizador da Colhedora
    //----------------------------------------------------------------------------------------------

    public void addCent(CentralizadorParametros cent) {
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        //long operadorId = -1;
        ContentValues values = putAllCent(cent);

        long a = mWritableDB.insert(TABLE_CENTRALIZADOR, null, values);

        Log.d("centralizadorDebug", ""+a);
    }

    /**
     * Procura pelos dados contidos dos ajustes do Gerenciador, baseado no ID em questão
     * (Por Default: ID = 1).
     * @param id ID dos dados armazenados (Default: ID = 1).
     * @return Retorna um objeto do tipo "CentralizadorAjustesPadrao", contendo todos os dados armazenados.
     */
    @SuppressLint("Range")
    public CentralizadorParametros searchAjustes(int id) {
        CentralizadorParametros cent = null;

        String queryOperador = "SELECT * FROM " + TABLE_CENTRALIZADOR + " WHERE " + KEY_CENT_ID + " = '" + id + "'";

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            Cursor cursor = mReadableDB.rawQuery(queryOperador, null);

            cursor.moveToFirst();

            if (id == cursor.getInt(cursor.getColumnIndex(KEY_CENT_ID))) {
                cent = setAllCent(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Não foi possível achar a operador em questão");
        }

        return cent;
    }

    /**
     * Seta todos os parâmetros referentes ao centralizador da colhedora
     * @param cursor Cursor do banco de dados, o qual "movimenta" através das colunas.
     * @return Objeto dos ajustes do centralizador "configurado".
     */
    @SuppressLint("Range")
    private CentralizadorParametros setAllCent(Cursor cursor) {
        CentralizadorParametros cent = new CentralizadorParametros();

        cent.setID(cursor.getInt(cursor.getColumnIndex(KEY_CENT_ID)));

        cent.setAngulo(cursor.getInt(cursor.getColumnIndex(KEY_CENT_ANGULO)));
        cent.setDistBarra(cursor.getInt(cursor.getColumnIndex(KEY_CENT_DIST_BARRA)));
        cent.setDistMin(cursor.getInt(cursor.getColumnIndex(KEY_CENT_MENOR_DISTANCIA)));
        cent.setDistMax(cursor.getInt(cursor.getColumnIndex(KEY_CENT_MAIOR_DISTANCIA)));
        cent.setDiametroMedio(cursor.getInt(cursor.getColumnIndex(KEY_CENT_DIAMETRO)));
        cent.setTempoAtt(cursor.getFloat(cursor.getColumnIndex(KEY_CENT_TEMPO_ATT)));

        //cent.setDia(cursor.getString(cursor.getColumnIndex(KEY_CENT_DIA_INICIO)));
        //cent.setDiaFinal(cursor.getString(cursor.getColumnIndex(KEY_CENT_DIA_FIM)));

        cent.setFile1(cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_1)));
        cent.setFile2(cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_2)));
        cent.setFile3(cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_3)));
        cent.setFile4(cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_4)));
        cent.setFile5(cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_5)));
        cent.setFile6(cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_6)));
        cent.setFile7(cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_7)));

        return cent;
    }


    private ContentValues putAllCent(CentralizadorParametros cent) {
        ContentValues values = new ContentValues();

        values.put(KEY_CENT_ID, cent.getID());

        Log.d("centralizadorDebug", ""+cent.getID());

        values.put(KEY_CENT_ANGULO, cent.getAngulo());
        values.put(KEY_CENT_DIST_BARRA, cent.getDistBarra());
        values.put(KEY_CENT_MENOR_DISTANCIA, cent.getDistMin());
        values.put(KEY_CENT_MAIOR_DISTANCIA, cent.getDistMax());
        values.put(KEY_CENT_DIAMETRO, cent.getDiametroMedio());
        values.put(KEY_CENT_TEMPO_ATT, cent.getTempoAtt());

        values.put(KEY_CENT_PATH_1, cent.getFile1());
        values.put(KEY_CENT_PATH_2, cent.getFile2());
        values.put(KEY_CENT_PATH_3, cent.getFile3());
        values.put(KEY_CENT_PATH_4, cent.getFile4());
        values.put(KEY_CENT_PATH_5, cent.getFile5());
        values.put(KEY_CENT_PATH_6, cent.getFile6());
        values.put(KEY_CENT_PATH_7, cent.getFile7());


        return values;
    }

    /**
     * Atualização dos dados referentes aos parâmetros do centralizador da colhedora, após o usuário
     * setar todos eles. Atualiza os parâmetros: Ângulo, Distância entre as barras da máquina, Menor
     * distância medida pelo sensor (Ex: 30cm), Maior distância medida pelo sensor (Ex: 240cm),
     * Diâmetro médio dos pés de café, Tempo de atualização do aplicativo.
     * @param cent Objeto dos ajsutes do centralizador da colhedora.
     * @return 1 caso o Update tenha sido realizado // 0 caso o Update não tenha sido realizado.
     */
    public int updateParametrosCentralizador(CentralizadorParametros cent) {
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_CENT_ANGULO, cent.getAngulo());
        values.put(KEY_CENT_DIST_BARRA, cent.getDistBarra());
        values.put(KEY_CENT_MENOR_DISTANCIA, cent.getDistMin());
        values.put(KEY_CENT_MAIOR_DISTANCIA, cent.getDistMax());
        values.put(KEY_CENT_DIAMETRO, cent.getDiametroMedio());
        values.put(KEY_CENT_TEMPO_ATT, cent.getTempoAtt());

        return mWritableDB.update(TABLE_CENTRALIZADOR, values, KEY_CENT_ID + "= ?",
                new String[]{String.valueOf(cent.getID())});
    }

    /**
     * Atualiza os arquivos do Centralizador que marca os dados referentes a um dia específico.
     * @param file {@link String} contendo o nome do arquivo utilizado.
     * @param num {@link Integer} contendo o número que marca qual KEY será utilizada.
     * @param ID {@link Integer} contendo o ID do centralizador (por Default: 1).
     * @return 1 caso o Update tenha sido realizado // 0 caso o Update não tenha sido realizado.
     */
    public int updateFilesCentralizador(String file, int num, int ID) {
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();

        if (num == 0) {
            values.put(KEY_CENT_PATH_1, file);
        } else if (num == 1) {
            values.put(KEY_CENT_PATH_2, file);
        } else if (num == 2) {
            values.put(KEY_CENT_PATH_3, file);
        } else if (num == 3) {
            values.put(KEY_CENT_PATH_4, file);
        } else if (num == 4) {
            values.put(KEY_CENT_PATH_5, file);
        } else if (num == 5) {
            values.put(KEY_CENT_PATH_6, file);
        } else if (num == 6) {
            values.put(KEY_CENT_PATH_7, file);
        }
        //values.put(KEY_CENT_DIA_INICIO, cent.getDia());
        //values.put(KEY_CENT_DIA_FIM, cent.getDiaFinal());

        return mWritableDB.update(TABLE_CENTRALIZADOR, values, KEY_CENT_ID + "= ?",
                new String[]{String.valueOf(ID)});
    }

    /**
     * Atualiza o último arquivo da lista, sobrescrevendo o primeiro arquivo inserido (mais antigo).
     * @param file {@link String} contendo o nome do arquivo utilizado.
     * @param ID {@link Integer} contendo o ID do centralizador (por Default: 1).
     * @return 1 caso o Update tenha sido realizado // 0 caso o Update não tenha sido realizado.
     */
    @SuppressLint("Range")
    public int updateLastFile(String file, int ID) {
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();

        String queryTable = "SELECT * FROM " + TABLE_CENTRALIZADOR + " WHERE " + KEY_CENT_ID + " = '" + ID + "'";

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            Cursor cursor = mReadableDB.rawQuery(queryTable, null);

            cursor.moveToFirst();

            if (ID == cursor.getInt(cursor.getColumnIndex(KEY_CENT_ID))) {
                values.put(KEY_CENT_PATH_1, cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_2)));
                values.put(KEY_CENT_PATH_2, cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_3)));
                values.put(KEY_CENT_PATH_3, cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_4)));
                values.put(KEY_CENT_PATH_4, cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_5)));
                values.put(KEY_CENT_PATH_5, cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_6)));
                values.put(KEY_CENT_PATH_6, cursor.getString(cursor.getColumnIndex(KEY_CENT_PATH_7)));
                values.put(KEY_CENT_PATH_7, file);

                return mWritableDB.update(TABLE_CENTRALIZADOR, values, KEY_CENT_ID + "= ?",
                        new String[]{String.valueOf(ID)});
            }

            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Não foi possível achar a operador em questão");
        }

        return 0;
    }


    //------------------------------------------------------------------------------------*//
    public void addTempo(Tempo tempo) {
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        //long operadorId = -1;
        ContentValues values = putAllTempo(tempo);

        mWritableDB.insert(TABLE_OPERADOR, null, values);
    }









}


