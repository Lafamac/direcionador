package com.example.inovaceifa.Centralizador;

/**
 * Classe contendo os parâmetros utilizados para referenciar o centralizador. Sendo assim, identifica-se os seguintes
 * parâmetros:
 *
 * <li>ID - Por Default, configura-se o mesmo como 1.</li>
 * <li>Ângulo - Definido na classe {@link CentralizadorAjustes}.</li>
 * <li>Distância entre as barras - Definido na classe {@link CentralizadorAjustes}.</li>
 * <li>Distância mínima - Definido na classe {@link CentralizadorAjustes}.</li>
 * <li>Distância máxima - Definido na classe {@link CentralizadorAjustes}.</li>
 * <li>Diâmetro Médio dos pés - Definido na classe {@link CentralizadorAjustes}.</li>
 * <li>Tempo de Atualização - Definido na classe {@link CentralizadorAjustes}.</li>
 * <li>Arquivo 1 - Definido na classe {@link CentralizadorSistema}.</li>
 * <li>Arquivo 2 - Definido na classe {@link CentralizadorSistema}.</li>
 * <li>Arquivo 3 - Definido na classe {@link CentralizadorSistema}.</li>
 * <li>Arquivo 4 - Definido na classe {@link CentralizadorSistema}.</li>
 * <li>Arquivo 5 - Definido na classe {@link CentralizadorSistema}.</li>
 * <li>Arquivo 6 - Definido na classe {@link CentralizadorSistema}.</li>
 * <li>Arquivo 7 - Definido na classe {@link CentralizadorSistema}.</li>
 *
 * @author Gustavo Henrique Tostes
 * @version 1.0
 * @since 22/09/2023
 */
public class CentralizadorParametros {

    /**
     * {@link Integer} - ID do centralizador, a ser guardado no banco de dados. Por Default, considera-se como 1.
     */
    private int ID;

    /**
     * {@link Integer} - Ângulo de inclinação das caixas de sensores.
     */
    private int angulo;

    /**
     * {@link Integer} - Distância entre as barras do interior da colhedora.
     */
    private int distBarra;

    /**
     * {@link Integer} - Menor distância medida/aceita pelos sensores.
     */
    private int distMin;

    /**
     * {@link Integer} - Maior distância medida/aceita pelos sensores
     */
    private int distMax;

    /**
     * {@link Integer} - Diâmetro médio dos pés de café onde a colheita será realizada.
     */
    private int diametroMedio;

    /**
     * {@link Float} - Tempo de atualização do sistema, em segundos.
     */
    private float tempoAtt;

    /**
     * {@link String} - Nome do primeiro arquivo contendo dados no sistema, obedecendo um limite pré-definido.
     */
    private String file1;

    /**
     * {@link String} - Nome do segundo arquivo contendo dados no sistema, obedecendo um limite pré-definido.
     */
    private String file2;

    /**
     * {@link String} - Nome do terceiro arquivo contendo dados no sistema, obedecendo um limite pré-definido.
     */
    private String file3;

    /**
     * {@link String} - Nome do quarto arquivo contendo dados no sistema, obedecendo um limite pré-definido.
     */
    private String file4;

    /**
     * {@link String} - Nome do quinto arquivo contendo dados no sistema, obedecendo um limite pré-definido.
     */
    private String file5;

    /**
     * {@link String} - Nome do sexto arquivo contendo dados no sistema, obedecendo um limite pré-definido.
     */
    private String file6;

    /**
     * {@link String} - Nome do sétimo arquivo contendo dados no sistema, obedecendo um limite pré-definido.
     */
    private String file7;

    //----------------------------------------------------------------------------------------------

    /**
     * Método Get() para o ID do sistema do centralizador (Apenas para aferição no banco de dados).
     * @return Retorna o ID do sistema do centralizador.
     */
    public int getID() {
        return ID;
    }

    /**
     * Método Set() para o ID do centralizador (Apenas para aferição no banco de dados).
     * @param ID ID do sistema do centralizador.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Método Get() para o ângulo.
     * @return Retorna o ângulo de inclinação das caixinhas contendo os sensores
     */
    public int getAngulo() {
        return angulo;
    }

    /**
     * Método Set() para o ângulo.
     * @param angulo Seta o ângulo de inclinção das caixinhas contendo os sensores.
     */
    public void setAngulo(int angulo) {
        this.angulo = angulo;
    }

    /**
     * Método Get() para a distância entre barras.
     * @return Retorna a distância entre as barras (interior) da máquina.
     */
    public int getDistBarra() {
        return distBarra;
    }

    /**
     * Método Set() para a distância entre barras.
     * @param distBarra Seta a distância entre as barras (interior) da máquina.
     */
    public void setDistBarra(int distBarra) {
        this.distBarra = distBarra;
    }

    /**
     * Método Get() para a distância mínima a ser medida pelos sensores.
     * @return Retorna a distância mínima a ser medida pelos sensores (Ex: 30cm).
     */
    public int getDistMin() {
        return distMin;
    }

    /**
     * Método Set() para a distância mínima a ser medida pelos sensores.
     * @param distMin Seta a distância mínima a ser medida pelos sensores (Ex: 30cm).
     */
    public void setDistMin(int distMin) {
        this.distMin = distMin;
    }

    /**
     * Método Get() para a distância máxima a ser medida pelos sensores.
     * @return Retorna a distância máxima a ser medida pelos sensores (Ex: 240cm).
     */
    public int getDistMax() {
        return distMax;
    }

    /**
     * Método Set() para a distância máxima a ser medida pelos sensores.
     * @param distMax Seta a distância máxima a ser medida pelos sensores (Ex: 240cm).
     */
    public void setDistMax(int distMax) {
        this.distMax = distMax;
    }

    /**
     * Método Get() para o diâmetro médio dos pés.
     * @return Retorna o diâmetro médio dos pés de café do local onde se colherá.
     */
    public int getDiametroMedio() {
        return diametroMedio;
    }

    /**
     * Método Set() para o diâmetro médio dos pés.
     * @param diametroMedio Seta o diâmetro médio dos pés de café do local onde se colherá.
     */
    public void setDiametroMedio(int diametroMedio) {
        this.diametroMedio = diametroMedio;
    }

    /**
     * Método Get() para o tempo de atualização do sistema.
     * @return Retorna o tempo de atualização do sistema (execuções de Thread) (Ex: 1 segundo).
     */
    public float getTempoAtt() {
        return tempoAtt;
    }

    /**
     * Método Set() para o tempo de atualização do sistema.
     * @param tempoAtt Seta o tempo de atualização do sistema (execuções de Thread) (Ex: 1 segundo).
     */
    public void setTempoAtt(float tempoAtt) {
        this.tempoAtt = tempoAtt;
    }

    /**
     * Método Get() para o primeiro arquivo salvo referente ao relatório.
     * @return Retorna o nome do arquivo em questão.
     */
    public String getFile1() {
        return file1;
    }

    /**
     * Método Set() para o primeiro arquivo salvo referente ao relatório.
     * @param file1 Nome do arquivo em questão.
     */
    public void setFile1(String file1) {
        this.file1 = file1;
    }

    /**
     * Método Get() para o segundo arquivo salvo referente ao relatório.
     * @return Retorna o nome do arquivo em questão.
     */
    public String getFile2() {
        return file2;
    }

    /**
     * Método Set() para o segundo arquivo salvo referente ao relatório.
     * @param file2 Nome do arquivo em questão.
     */
    public void setFile2(String file2) {
        this.file2 = file2;
    }

    /**
     * Método Get() para o terceiro arquivo salvo referente ao relatório.
     * @return Retorna o nome do arquivo em questão.
     */
    public String getFile3() {
        return file3;
    }

    /**
     * Método Set() para o terceiro arquivo salvo referente ao relatório.
     * @param file3 Nome do arquivo em questão.
     */
    public void setFile3(String file3) {
        this.file3 = file3;
    }

    /**
     * Método Get() para o quarto arquivo salvo referente ao relatório.
     * @return Retorna o nome do arquivo em questão.
     */
    public String getFile4() {
        return file4;
    }

    /**
     * Método Set() para o quarto arquivo salvo referente ao relatório.
     * @param file4 Nome do arquivo em questão.
     */
    public void setFile4(String file4) {
        this.file4 = file4;
    }

    /**
     * Método Get() para o quinto arquivo salvo referente ao relatório.
     * @return Retorna o nome do arquivo em questão.
     */
    public String getFile5() {
        return file5;
    }

    /**
     * Método Set() para o quinto arquivo salvo referente ao relatório.
     * @param file5 Nome do arquivo em questão.
     */
    public void setFile5(String file5) {
        this.file5 = file5;
    }

    /**
     * Método Get() para o sexto arquivo salvo referente ao relatório.
     * @return Retorna o nome do arquivo em questão.
     */
    public String getFile6() {
        return file6;
    }

    /**
     * Método Set() para o sexto arquivo salvo referente ao relatório.
     * @param file6 Nome do arquivo em questão.
     */
    public void setFile6(String file6) {
        this.file6 = file6;
    }

    /**
     * Método Get() para o sétimo arquivo salvo referente ao relatório.
     * @return Retorna o nome do arquivo em questão.
     */
    public String getFile7() {
        return file7;
    }

    /**
     * Método Set() para o sétimo arquivo salvo referente ao relatório.
     * @param file7 Nome do arquivo em questão.
     */
    public void setFile7(String file7) {
        this.file7 = file7;
    }
}
