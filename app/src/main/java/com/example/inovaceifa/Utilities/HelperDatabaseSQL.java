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
import com.example.inovaceifa.Centralizador.EventoProducao;
import com.example.inovaceifa.Centralizador.Operador;
import com.example.inovaceifa.Centralizador.Tempo;

import java.util.ArrayList;

/**
 * Classe referente ao Banco de Dados, utilizando SQLite.
 */
public class HelperDatabaseSQL extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CEIFA_Database3.0";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_OPERADOR = "gerenciador";
    private static final String TABLE_CENTRALIZADOR = "centralizador";
    private static final String TABLE_TEMPO = "tempo";
    private static final String TABLE_EVENTOS = "eventos_producao";

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

    private static final String KEY_CENT_ID = "centID";
    private static final String KEY_CENT_CONTROLE = "controleDados";
    private static final String KEY_CENT_ANGULO = "angulo";
    private static final String KEY_CENT_DIST_BARRA = "distanciaBarra";
    private static final String KEY_CENT_MENOR_DISTANCIA = "menorDistancia";
    private static final String KEY_CENT_MAIOR_DISTANCIA = "maiorDistancia";
    private static final String KEY_CENT_DIAMETRO = "diametro";
    private static final String KEY_CENT_TEMPO_ATT = "tempoAtt";
    private static final String KEY_CENT_HABILITA_SCROLL = "habilitaScrollTempo";
    private static final String KEY_CENT_TEMPO_FIXO = "tempoFixo";
    private static final String KEY_CENT_PATH_1 = "filePath1";
    private static final String KEY_CENT_PATH_2 = "filePath2";
    private static final String KEY_CENT_PATH_3 = "filePath3";
    private static final String KEY_CENT_PATH_4 = "filePath4";
    private static final String KEY_CENT_PATH_5 = "filePath5";
    private static final String KEY_CENT_PATH_6 = "filePath6";
    private static final String KEY_CENT_PATH_7 = "filePath7";

    private static final String KEY_EVENTO_ID = "eventoID";
    private static final String KEY_EVENTO_TIPO = "tipoEvento";
    private static final String KEY_EVENTO_HORA = "hora";
    private static final String KEY_EVENTO_DATA = "data";
    private static final String KEY_EVENTO_VALOR = "valor";
    private static final String KEY_EVENTO_OPERADOR_ID = "operadorID";

    private static final String KEY_TEMPO_ID = "tempoID";
    private static final String KEY_TEMPO_INICIO = "tempoInicio";
    private static final String KEY_TEMPO_FINAL = "tempoFinal";
    private static final String KEY_TEMPO_TOTAL = "tempoTotal";
    private static final String KEY_TEMPO_DATA = "tempoData";
    private static final String KEY_TEMPO_OPERADOR_ID = "tempoOperadorID";

    private static HelperDatabaseSQL sInstance;
    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public static synchronized HelperDatabaseSQL getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HelperDatabaseSQL(context.getApplicationContext());
        }
        return sInstance;
    }

    private HelperDatabaseSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableGerenc());
        db.execSQL(createTableCent());
        db.execSQL(createTableTempo());
        db.execSQL(createTableEventos());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTOS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPO);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPERADOR);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CENTRALIZADOR);
                
                onCreate(db);
            } catch (Exception e) {
                Log.e(TAG, "Erro Upgrade v4: " + e.getMessage());
            }
        }
    }

    private String createTableCent() {
        return "CREATE TABLE " + TABLE_CENTRALIZADOR + "(" +
                KEY_CENT_ID + " INTEGER," +
                KEY_CENT_CONTROLE + " INTEGER," +
                KEY_CENT_ANGULO + " INTEGER," +
                KEY_CENT_DIST_BARRA + " INTEGER," +
                KEY_CENT_MENOR_DISTANCIA + " INTEGER," +
                KEY_CENT_MAIOR_DISTANCIA + " INTEGER," +
                KEY_CENT_DIAMETRO + " INTEGER," +
                KEY_CENT_TEMPO_ATT + " REAL," +
                KEY_CENT_HABILITA_SCROLL + " INTEGER," +
                KEY_CENT_TEMPO_FIXO + " REAL," +
                KEY_CENT_PATH_1 + " TEXT," +
                KEY_CENT_PATH_2 + " TEXT," +
                KEY_CENT_PATH_3 + " TEXT," +
                KEY_CENT_PATH_4 + " TEXT," +
                KEY_CENT_PATH_5 + " TEXT," +
                KEY_CENT_PATH_6 + " TEXT," +
                KEY_CENT_PATH_7 + " TEXT)";
    }

    private String createTableTempo() {
        return "CREATE TABLE " + TABLE_TEMPO + "(" +
                KEY_TEMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_TEMPO_OPERADOR_ID + " INTEGER," +
                KEY_TEMPO_INICIO + " TEXT," +
                KEY_TEMPO_FINAL + " TEXT," +
                KEY_TEMPO_TOTAL + " TEXT," +
                KEY_TEMPO_DATA + " TEXT," +
                "FOREIGN KEY (" + KEY_TEMPO_OPERADOR_ID + ") REFERENCES " + TABLE_OPERADOR + "(" + KEY_OPERADOR_ID + ")" +
                ")";
    }

    private String createTableGerenc() {
        return "CREATE TABLE " + TABLE_OPERADOR + "(" +
                KEY_OPERADOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
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
                KEY_OPERADOR_VELOCIDADE + " INTEGER," +
                KEY_OPERADOR_VIBRACAO + " INTEGER," +
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
    }

    private String createTableEventos() {
        return "CREATE TABLE " + TABLE_EVENTOS + "(" +
                KEY_EVENTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_EVENTO_TIPO + " TEXT," +
                KEY_EVENTO_HORA + " TEXT," +
                KEY_EVENTO_DATA + " TEXT," +
                KEY_EVENTO_VALOR + " TEXT," +
                KEY_EVENTO_OPERADOR_ID + " TEXT" +
                ")";
    }

    @SuppressLint("Range")
    public ArrayList<Operador> listarTodasOperadors() {
        String sql = "SELECT * FROM " + TABLE_OPERADOR + " ORDER BY " + KEY_OPERADOR_NUMERO + " ASC";
        if (mReadableDB == null) mReadableDB = getReadableDatabase();
        ArrayList<Operador> lista = new ArrayList<>();
        Cursor cursor = mReadableDB.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(setAll(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public void addOperador(Operador operador) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();
        mWritableDB.insert(TABLE_OPERADOR, null, putAll(operador));
    }

    public void deleteOneOperador(Operador operador) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();
        mWritableDB.delete(TABLE_OPERADOR, KEY_OPERADOR_NUMERO + " = ?", new String[]{operador.getNumero()});
    }

    @SuppressLint("Range")
    public Operador searchOperadorByNumber(String numero) {
        if (mReadableDB == null) mReadableDB = getReadableDatabase();
        Operador operador = null;
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM " + TABLE_OPERADOR + " WHERE " + KEY_OPERADOR_NUMERO + " = ?", new String[]{numero});
        if (cursor.moveToFirst()) {
            operador = setAll(cursor);
        }
        cursor.close();
        return operador;
    }

    public Cursor searchNumber(String numero) {
        if (mReadableDB == null) mReadableDB = getReadableDatabase();
        return mReadableDB.rawQuery("SELECT * FROM " + TABLE_OPERADOR + " WHERE " + KEY_OPERADOR_NUMERO + " = ?", new String[]{numero});
    }

    public int updateParametrosCentralizador(CentralizadorParametros cent) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();
        return mWritableDB.update(TABLE_CENTRALIZADOR, putAllCent(cent), KEY_CENT_ID + "= ?", new String[]{String.valueOf(cent.getID())});
    }

    public void registrarEvento(String tipo, String valor, String operadorID) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();
        java.text.SimpleDateFormat sdfData = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        java.text.SimpleDateFormat sdfHora = new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault());
        String data = sdfData.format(new java.util.Date());
        String hora = sdfHora.format(new java.util.Date());
        ContentValues values = new ContentValues();
        values.put(KEY_EVENTO_TIPO, tipo);
        values.put(KEY_EVENTO_VALOR, valor);
        values.put(KEY_EVENTO_OPERADOR_ID, operadorID);
        values.put(KEY_EVENTO_DATA, data);
        values.put(KEY_EVENTO_HORA, hora);
        mWritableDB.insert(TABLE_EVENTOS, null, values);
    }

    @SuppressLint("Range")
    public ArrayList<EventoProducao> listarEventosFiltrados(String data, String operadorID) {
        ArrayList<EventoProducao> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_EVENTOS);
        ArrayList<String> args = new ArrayList<>();
        if (data != null || operadorID != null) {
            sql.append(" WHERE ");
            if (data != null) {
                sql.append(KEY_EVENTO_DATA).append(" = ?");
                args.add(data);
            }
            if (operadorID != null) {
                if (data != null) sql.append(" AND ");
                sql.append(KEY_EVENTO_OPERADOR_ID).append(" = ?");
                args.add(operadorID);
            }
        }
        sql.append(" ORDER BY ").append(KEY_EVENTO_ID).append(" DESC");
        if (mReadableDB == null) mReadableDB = getReadableDatabase();
        Cursor cursor = mReadableDB.rawQuery(sql.toString(), args.isEmpty() ? null : args.toArray(new String[0]));
        if (cursor.moveToFirst()) {
            do {
                EventoProducao ev = new EventoProducao();
                ev.setId(cursor.getInt(cursor.getColumnIndex(KEY_EVENTO_ID)));
                ev.setTipo(cursor.getString(cursor.getColumnIndex(KEY_EVENTO_TIPO)));
                ev.setHora(cursor.getString(cursor.getColumnIndex(KEY_EVENTO_HORA)));
                ev.setData(cursor.getString(cursor.getColumnIndex(KEY_EVENTO_DATA)));
                ev.setValor(cursor.getString(cursor.getColumnIndex(KEY_EVENTO_VALOR)));
                ev.setOperadorID(cursor.getString(cursor.getColumnIndex(KEY_EVENTO_OPERADOR_ID)));
                lista.add(ev);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    @SuppressLint("Range")
    public ArrayList<String> listarDatasUnicas() {
        ArrayList<String> datas = new ArrayList<>();
        if (mReadableDB == null) mReadableDB = getReadableDatabase();
        Cursor cursor = mReadableDB.rawQuery("SELECT DISTINCT " + KEY_EVENTO_DATA + " FROM " + TABLE_EVENTOS + " ORDER BY " + KEY_EVENTO_DATA + " DESC", null);
        if (cursor.moveToFirst()) {
            do { datas.add(cursor.getString(0)); } while (cursor.moveToNext());
        }
        cursor.close();
        return datas;
    }

    @SuppressLint("Range")
    public CentralizadorParametros searchAjustes(int id) {
        if (mReadableDB == null) mReadableDB = getReadableDatabase();
        CentralizadorParametros cent = null;
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM " + TABLE_CENTRALIZADOR + " WHERE " + KEY_CENT_ID + " = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            cent = setAllCent(cursor);
        }
        cursor.close();
        return cent;
    }

    public int updateLocalOperador(Operador op) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_OPERADOR_LOCAL, op.getLocal());
        return mWritableDB.update(TABLE_OPERADOR, v, KEY_OPERADOR_NUMERO + " = ?", new String[]{op.getNumero()});
    }

    public int updateImagemOperador(Operador op, Bitmap img, Context ctx) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_OPERADOR_IMAGEM, op.getImagemPath());
        return mWritableDB.update(TABLE_OPERADOR, v, KEY_OPERADOR_NUMERO + " = ?", new String[]{op.getNumero()});
    }

    @SuppressLint("Range")
    private Operador setAll(Cursor cursor) {
        Operador op = new Operador();
        op.setNumero(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_NUMERO)));
        op.setLocal(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_LOCAL)));
        op.setDescricao(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_DESCRICAO)));
        op.setImagemPath(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_IMAGEM)));
        op.setNumeroLedsVerdeClaros(cursor.getInt(cursor.getColumnIndex(KEY_OPERADOR_VERDE_CLARO)));
        op.setNumeroLedsVerdeEscurosEsquerda(cursor.getInt(cursor.getColumnIndex(KEY_OPERADOR_VERDE_ESCURO_ESQUERDA)));
        op.setNumeroLedsVerdeEscurosDireita(cursor.getInt(cursor.getColumnIndex(KEY_OPERADOR_VERDE_ESCURO_DIREITA)));
        op.setNumeroLedsAmarelosEsquerda(cursor.getInt(cursor.getColumnIndex(KEY_OPERADOR_AMARELO_ESQUERDA)));
        op.setNumeroLedsAmarelosDireita(cursor.getInt(cursor.getColumnIndex(KEY_OPERADOR_AMARELO_DIREITA)));
        op.setNumeroLedsVermelhosEsquerda(cursor.getInt(cursor.getColumnIndex(KEY_OPERADOR_VERMELHO_ESQUERDA)));
        op.setNumeroLedsVermelhosDireita(cursor.getInt(cursor.getColumnIndex(KEY_OPERADOR_VERMELHO_DIREITA)));
        op.setTempoDiarioTrabalhado(cursor.getInt(cursor.getColumnIndex(KEY_OPERADOR_TEMPO_DIARIO_TRABALHADO)));
        op.setDiaAtual(cursor.getString(cursor.getColumnIndex(KEY_OPERADOR_DIA_ATUAL)));
        return op;
    }

    private ContentValues putAll(Operador op) {
        ContentValues v = new ContentValues();
        v.put(KEY_OPERADOR_NUMERO, op.getNumero());
        v.put(KEY_OPERADOR_LOCAL, op.getLocal());
        v.put(KEY_OPERADOR_DESCRICAO, op.getDescricao());
        v.put(KEY_OPERADOR_IMAGEM, op.getImagemPath());
        v.put(KEY_OPERADOR_VERDE_CLARO, op.getNumeroLedsVerdeClaros());
        v.put(KEY_OPERADOR_VERDE_ESCURO_ESQUERDA, op.getNumeroLedsVerdeEscurosEsquerda());
        v.put(KEY_OPERADOR_VERDE_ESCURO_DIREITA, op.getNumeroLedsVerdeEscurosDireita());
        v.put(KEY_OPERADOR_AMARELO_ESQUERDA, op.getNumeroLedsAmarelosEsquerda());
        v.put(KEY_OPERADOR_AMARELO_DIREITA, op.getNumeroLedsAmarelosDireita());
        v.put(KEY_OPERADOR_VERMELHO_ESQUERDA, op.getNumeroLedsVermelhosEsquerda());
        v.put(KEY_OPERADOR_VERMELHO_DIREITA, op.getNumeroLedsVermelhosDireita());
        v.put(KEY_OPERADOR_TEMPO_DIARIO_TRABALHADO, op.getTempoDiarioTrabalhado());
        v.put(KEY_OPERADOR_DIA_ATUAL, op.getDiaAtual());
        return v;
    }

    @SuppressLint("Range")
    private CentralizadorParametros setAllCent(Cursor cursor) {
        CentralizadorParametros c = new CentralizadorParametros();
        c.setID(cursor.getInt(cursor.getColumnIndex(KEY_CENT_ID)));
        c.setAngulo(cursor.getInt(cursor.getColumnIndex(KEY_CENT_ANGULO)));
        c.setDistBarra(cursor.getInt(cursor.getColumnIndex(KEY_CENT_DIST_BARRA)));
        c.setDistMin(cursor.getInt(cursor.getColumnIndex(KEY_CENT_MENOR_DISTANCIA)));
        c.setDistMax(cursor.getInt(cursor.getColumnIndex(KEY_CENT_MAIOR_DISTANCIA)));
        c.setDiametroMedio(cursor.getInt(cursor.getColumnIndex(KEY_CENT_DIAMETRO)));
        c.setTempoAtt(cursor.getFloat(cursor.getColumnIndex(KEY_CENT_TEMPO_ATT)));
        c.setHabilitaScrollTempo(cursor.getInt(cursor.getColumnIndex(KEY_CENT_HABILITA_SCROLL)));
        c.setTempoFixo(cursor.getFloat(cursor.getColumnIndex(KEY_CENT_TEMPO_FIXO)));
        return c;
    }

    private ContentValues putAllCent(CentralizadorParametros c) {
        ContentValues v = new ContentValues();
        v.put(KEY_CENT_ID, c.getID());
        v.put(KEY_CENT_ANGULO, c.getAngulo());
        v.put(KEY_CENT_DIST_BARRA, c.getDistBarra());
        v.put(KEY_CENT_MENOR_DISTANCIA, c.getDistMin());
        v.put(KEY_CENT_MAIOR_DISTANCIA, c.getDistMax());
        v.put(KEY_CENT_DIAMETRO, c.getDiametroMedio());
        v.put(KEY_CENT_TEMPO_ATT, c.getTempoAtt());
        v.put(KEY_CENT_HABILITA_SCROLL, c.getHabilitaScrollTempo());
        v.put(KEY_CENT_TEMPO_FIXO, c.getTempoFixo());
        return v;
    }

    public void addCent(CentralizadorParametros cent) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();
        mWritableDB.insert(TABLE_CENTRALIZADOR, null, putAllCent(cent));
    }

    public void addTempo(Tempo tempo) {
        if (mWritableDB == null) mWritableDB = getWritableDatabase();
        mWritableDB.insert(TABLE_TEMPO, null, putAllTempo(tempo));
    }

    private ContentValues putAllTempo(Tempo tempo) {
        ContentValues values = new ContentValues();
        values.put(KEY_TEMPO_OPERADOR_ID, tempo.getOperadorID());
        values.put(KEY_TEMPO_INICIO, tempo.getTempoInicio());
        values.put(KEY_TEMPO_FINAL, tempo.getTempoFinal());
        values.put(KEY_TEMPO_TOTAL, tempo.getTempoTotal());
        values.put(KEY_TEMPO_DATA, tempo.getTempoData());
        return values;
    }
}
