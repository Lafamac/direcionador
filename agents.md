# AGENTS.md — Sistema de Orientação para Colheitadeira de Café

## 1. Objetivo do Sistema

Este projeto é composto por dois blocos principais:

1. **Aplicativo Android**, desenvolvido no Android Studio, para rodar em uma multimídia instalada na colheitadeira de café.
2. **Firmware ESP32**, responsável por ler sensores ultrassônicos, processar os dados e responder ao Android via UDP.

O sistema tem como objetivo auxiliar o operador da colheitadeira, indicando visualmente para qual lado ele deve conduzir a máquina para manter o alinhamento correto durante a operação.

O sistema:

- recebe dados de sensores via UDP;
- interpreta o alinhamento da máquina;
- exibe orientação visual ao operador;
- aciona LEDs gráficos, setas e alertas;
- permite ajustes e calibração;
- registra parâmetros e resultados;
- prepara a base para relatórios, histórico e telemetria futura.

---

## 2. Arquitetura Geral

Fluxo técnico esperado:

```text
Sensores HC-SR04
↓
ESP32
↓
Task FreeRTOS de leitura
↓
Processamento estatístico
Média / Moda / Média móvel
↓
Resposta UDP
↓
Aplicativo Android
↓
UDPProtocol.java
↓
CentralizadorSistema.java
↓
CentralizadorLEDs.java
↓
Operador visualiza a direção de correção
```

Fluxo operacional para o operador:

```text
Colheitadeira em movimento
↓
Sensores medem distância/lado/alinhamento
↓
ESP32 calcula tendência
↓
Android recebe valor entre -6 e +6
↓
Multimídia mostra LEDs/setas
↓
Operador corrige direção da máquina
```

---

## 3. Estado Atual do Projeto

### 3.1 Android

Status atual:

- Projeto Android Studio válido;
- APK já existente;
- Layouts e recursos gráficos presentes;
- Telas principais estruturadas;
- Comunicação UDP já implementada;
- Sistema de LEDs gráficos implementado;
- Sistema de parâmetros implementado;
- Sistema de relatórios/gráficos existente;
- Projeto base recuperável e funcional.

Pontos ainda pendentes:

- Validar funcionamento real na multimídia;
- Corrigir/estabilizar thread operacional;
- Validar comunicação UDP em tempo real;
- Corrigir lifecycle Android para evitar threads órfãs;
- Garantir atualização visual apenas na UI Thread;
- Criar modo de simulação sem ESP32;
- Testar orientação real dos LEDs e setas.

---

### 3.2 ESP32

Status após as correções realizadas:

- Firmware voltou a compilar;
- Estrutura FreeRTOS foi corrigida;
- Leituras dos sensores agora usam timeout;
- Conflito com arquivo `WiFi.h` foi corrigido;
- Watchdog foi atualizado para compatibilidade com ESP32 Core 3.x;
- O firmware está pronto para o primeiro teste real com placa conectada via USB.

Pontos ainda pendentes:

- Fazer upload para o ESP32;
- Validar Monitor Serial;
- Confirmar leituras reais dos sensores;
- Confirmar conexão Wi-Fi;
- Confirmar IP utilizado pelo ESP32;
- Confirmar recebimento dos comandos UDP;
- Confirmar respostas enviadas ao Android;
- Testar estabilidade por tempo prolongado.

---

## 4. Correções Já Realizadas no ESP32

### 4.1 Correção da task FreeRTOS

Problema original:

O arquivo de sensores possuía estrutura incorreta dentro da função de task:

```cpp
VOID SETUP()
VOID LOOP()
```

Isso não é correto dentro de uma task FreeRTOS.

Correção aplicada:

A task foi convertida para a estrutura correta:

```cpp
void leituraSensores(void *pvParameters) {
    while (true) {
        // leitura contínua dos sensores
    }
}
```

Resultado esperado:

- A task passa a executar continuamente;
- A leitura dos sensores fica compatível com FreeRTOS;
- O firmware se torna mais previsível;
- Reduz risco de comportamento indefinido.

---

### 4.2 Correção do `pulseIn()`

Problema original:

As leituras dos sensores usavam:

```cpp
pulseIn(ECHOPIN, HIGH);
```

Sem timeout, se o sensor não responder, o ESP32 pode ficar travado aguardando eco.

Correção aplicada:

Foi adicionado timeout:

```cpp
pulseIn(ECHOPIN, HIGH, 30000);
pulseIn(ECHOPIN2, HIGH, 30000);
```

Resultado esperado:

- Se o sensor falhar, o ESP32 não congela;
- O loop continua funcionando;
- O watchdog tem mais chance de atuar corretamente;
- A comunicação UDP não fica presa por falha de sensor.

---

### 4.3 Correção do conflito `WiFi.h`

Problema original:

Existia um arquivo local chamado:

```text
WiFi.h
```

Mas o ESP32 já possui uma biblioteca oficial chamada:

```cpp
#include <WiFi.h>
```

Isso causava recursão de includes:

```text
WiFi.h local
↓
#include <WiFi.h>
↓
Arduino encontra novamente WiFi.h local
↓
loop infinito
```

Erro observado:

```text
#include nested depth 200 exceeds maximum of 200
```

Correção aplicada:

O arquivo local foi renomeado para:

```text
wifi_manager.h
```

E os includes internos passaram a usar:

```cpp
#include "wifi_manager.h"
```

Mantendo a biblioteca oficial do ESP32 como:

```cpp
#include <WiFi.h>
```

Resultado esperado:

- O erro de recursão desaparece;
- O compilador usa a biblioteca correta do ESP32;
- O projeto passa para os próximos erros reais de compilação.

---

### 4.4 Correção do Watchdog para ESP32 Core 3.x

Problema original:

O arquivo `watchdog.h` usava API antiga:

```cpp
timer = timerBegin(0, 80, true);
timerAttachInterrupt(timer, &resetModule, true);
timerAlarmWrite(timer, tempo, true);
timerAlarmEnable(timer);
```

No ESP32 Core 3.x, essa API mudou.

Erro observado:

```text
too many arguments to function 'hw_timer_t* timerBegin(uint32_t)'
timerAlarmWrite was not declared in this scope
timerAlarmEnable was not declared in this scope
```

Correção aplicada:

A função `configuraWatchdog` foi atualizada para o formato novo:

```cpp
void configuraWatchdog(int tempo) {
  timer = timerBegin(1000000);
  timerAttachInterrupt(timer, &resetModule);
  timerAlarm(timer, tempo, true, 0);
}
```

Resultado esperado:

- Compatibilidade com ESP32 Core 3.3.8;
- Firmware volta a compilar;
- Watchdog permanece funcional;
- Base pronta para teste físico.

---

### 4.5 Dependência `StackArray`

Problema encontrado:

A biblioteca `StackArray.h` não foi localizada corretamente pelo Arduino IDE.

Erro observado:

```text
StackArray.h: No such file or directory
```

Estado atual:

O projeto compilou após correções realizadas no ambiente/código.

Recomendação futura:

Substituir o uso de `StackArray` por estrutura nativa, como:

```cpp
std::vector<int>
```

ou buffer circular fixo.

Motivo:

- Reduz dependência externa;
- Evita problemas com Arduino IDE;
- Facilita manutenção;
- Melhora previsibilidade em ambiente embarcado.

---

## 5. Firmware ESP32 — Arquivos e Funções Importantes

### 5.1 `sensorzinho.ino`

Arquivo principal do firmware.

Responsável por:

- iniciar Serial;
- configurar Wi-Fi;
- iniciar tasks;
- iniciar watchdog;
- manter loop principal;
- chamar gerenciamento UDP.

---

### 5.2 `sensores.ino`

Responsável pela leitura dos sensores HC-SR04.

Função crítica:

```cpp
void leituraSensores(void *pvParameters)
```

Responsável por:

- configurar pinos TRIG/ECHO;
- emitir pulso;
- medir tempo de retorno;
- converter tempo em distância;
- filtrar leituras inválidas;
- armazenar leituras para processamento.

---

### 5.3 `wifi_manager.h`

Responsável pelas funções de rede/Wi-Fi.

Importante:

Este arquivo substituiu o antigo `WiFi.h` local para evitar conflito com a biblioteca oficial do ESP32.

---

### 5.4 `watchdog.h`

Responsável por reiniciar o módulo em caso de travamento.

Após a correção, está compatível com ESP32 Core 3.x.

---

### 5.5 Arquivos de processamento

O firmware possui lógica relacionada a:

- média;
- moda;
- média móvel;
- pilhas/listas de leituras;
- cálculo do valor final enviado ao Android.

Essas rotinas precisam ser validadas depois que o ESP32 estiver rodando fisicamente.

---

## 6. Protocolo UDP

### 6.1 Porta

A porta utilizada entre Android e ESP32 é:

```text
1234
```

---

### 6.2 Fluxo esperado

```text
Android envia comando UDP
↓
ESP32 recebe comando
↓
ESP32 interpreta comando
↓
ESP32 calcula ou recupera valor
↓
ESP32 envia resposta UDP
↓
Android lê resposta
↓
Android atualiza LEDs/setas
```

---

### 6.3 Comandos esperados

| Comando enviado pelo Android | Resposta esperada do ESP32 |
|---|---|
| `iniciar:Media` | valor numérico entre -6 e +6 |
| `iniciar:Moda` | valor numérico entre -6 e +6 |
| `iniciar:Movel` | valor numérico entre -6 e +6 |
| `params:...` | confirmação ou atualização de parâmetros |
| `restart` | reinicialização/confirmação |

---

### 6.4 Valores de resposta

O Android espera valores semelhantes a:

```text
-6 -5 -4 -3 -2 -1 0 1 2 3 4 5 6
```

Interpretação geral:

```text
0      → máquina centralizada
positivo → correção para um lado
negativo → correção para o outro lado
```

A direção exata precisa ser validada fisicamente:

```text
valor positivo significa ir para esquerda ou direita?
valor negativo significa ir para esquerda ou direita?
```

Esse ponto é crítico para segurança operacional.

---

## 7. Aplicativo Android — Arquivos Importantes

### 7.1 `Inicial.java`

Ponto inicial do app.

Responsável por:

- inicialização da aplicação;
- primeira navegação;
- preparação do ambiente.

---

### 7.2 `CentralizadorPrincipal.java`

Tela principal do sistema.

Responsável por:

- navegação principal;
- acesso a sistema;
- acesso a relatórios;
- acesso a parâmetros;
- fluxo inicial do operador.

---

### 7.3 `CentralizadorSistema.java`

Arquivo mais crítico do Android neste momento.

Responsável pela tela operacional da colheitadeira.

Problema principal:

A versão atual parece ter perdido parte da lógica UDP contínua que existia em `CentralizadorSistema2.java`.

Correção necessária:

- comparar com `CentralizadorSistema2.java`;
- reintegrar a leitura UDP;
- garantir loop operacional;
- atualizar LEDs com segurança;
- parar threads ao sair da tela.

---

### 7.4 `CentralizadorSistema2.java`

Versão antiga/experimental.

Importante porque contém lógica funcional que pode ser reaproveitada.

Deve ser usado como referência para recuperar:

- envio de comandos UDP;
- leitura de resposta;
- atualização periódica;
- chamada de LEDs.

---

### 7.5 `UDPProtocol.java`

Coração da comunicação Android ↔ ESP32.

Precisa garantir:

- envio confiável;
- recebimento confiável;
- timeout;
- fechamento de socket;
- tratamento de erro;
- retorno síncrono.

Correção recomendada:

Criar método único:

```java
String enviarEReceber(String comando, int porta, String ipDestino)
```

Fluxo recomendado:

```text
abrir socket
↓
setar timeout
↓
enviar pacote
↓
aguardar resposta
↓
validar resposta
↓
fechar socket
↓
retornar resultado
```

---

### 7.6 `CentralizadorLEDs.java`

Controla os LEDs/setas da interface.

Responsável por:

- interpretar valor recebido;
- acender LEDs;
- indicar direção;
- mostrar centralização;
- alertar desalinhamento.

Precisa ser validado com valores simulados:

```text
-6, -3, -1, 0, 1, 3, 6
```

---

### 7.7 `CentralizadorParametros.java`

Responsável por parâmetros de calibração.

Pode enviar ao ESP32 dados como:

- ângulo;
- distância da barra;
- distância máxima;
- distância mínima;
- diâmetro;
- método de cálculo.

Precisa ser validado com o protocolo `params`.

---

## 8. Problemas Identificados no Android

### 8.1 Comunicação UDP instável

Arquivos:

```text
UDPProtocol.java
CentralizadorSistema.java
```

Problema:

O app pode estar usando fluxo assíncrono inseguro:

```java
UDP.enviarMensagem();
recebido = UDP.getMensagem_recebida();
```

Risco:

- resposta vazia;
- resposta antiga;
- perda de pacote;
- travamento;
- atualização incorreta.

Correção:

Criar fluxo síncrono:

```text
enviar comando
↓
aguardar resposta
↓
validar resposta
↓
retornar resposta
```

---

### 8.2 Falta de timeout no Android

Arquivo:

```text
UDPProtocol.java
```

Problema:

Se `DatagramSocket.receive()` ficar sem timeout, o Android pode travar aguardando resposta.

Correção:

```java
socket.setSoTimeout(1000);
```

---

### 8.3 Thread operacional incompleta

Arquivo:

```text
CentralizadorSistema.java
```

Problema:

A thread operacional pode estar apenas dormindo ou não executando corretamente a leitura UDP contínua.

Correção:

Reintegrar lógica de `CentralizadorSistema2.java`.

---

### 8.4 Atualização visual fora da UI Thread

Problema:

No Android, alterações visuais fora da thread principal podem causar erro.

Risco:

```text
CalledFromWrongThreadException
```

Correção:

Toda atualização de interface deve usar:

```java
runOnUiThread(() -> {
    // atualizar LEDs, textos, setas
});
```

---

### 8.5 Threads órfãs

Problema:

Ao sair da tela operacional, a thread pode continuar rodando.

Risco:

- consumo excessivo;
- múltiplas threads duplicadas;
- UDP rodando em segundo plano;
- travamento;
- comportamento inconsistente.

Correção:

Implementar parada segura em:

```java
onPause()
onStop()
onDestroy()
```

---

### 8.6 Falta de modo simulado

Problema:

Hoje o teste depende muito do ESP32 real.

Correção:

Criar modo simulado no Android para gerar valores:

```text
-6 até +6
```

Objetivo:

- testar LEDs;
- testar layout;
- testar orientação;
- testar setas;
- testar alarmes;
- validar direção antes do campo.

---

## 9. Plano de Correção Atualizado

## ETAPA 1 — Correção inicial do ESP32 — CONCLUÍDA

Status:

```text
CONCLUÍDA
```

Itens realizados:

- Corrigida task FreeRTOS;
- Removida estrutura incorreta `VOID SETUP()` / `VOID LOOP()`;
- Adicionado timeout ao `pulseIn()`;
- Corrigido conflito `WiFi.h`;
- Corrigido watchdog para ESP32 Core 3.x;
- Firmware voltou a compilar.

---

## ETAPA 2 — Primeiro teste físico do ESP32

Status:

```text
PENDENTE
```

Objetivo:

Validar o firmware na placa real.

Passos:

1. Conectar ESP32 via USB;
2. Selecionar placa correta no Arduino IDE;
3. Selecionar porta correta;
4. Clicar em Upload;
5. Se travar em `Connecting...`, segurar botão BOOT;
6. Abrir Monitor Serial;
7. Ajustar velocidade para `115200`;
8. Observar mensagens de inicialização.

Validar no Monitor Serial:

- ESP32 iniciou;
- Não reinicia sozinho;
- Wi-Fi conectou;
- IP apareceu;
- Sensores exibem distância;
- Leituras mudam ao aproximar/afastar objeto;
- Não aparecem mensagens repetidas de erro;
- O sistema permanece ligado por vários minutos.

Resultado esperado:

```text
ESP32 ligado, lendo sensores e sem travar.
```

---

## ETAPA 3 — Validar sensores HC-SR04

Status:

```text
PENDENTE
```

Objetivo:

Confirmar que os sensores estão lendo corretamente.

Passos:

1. Abrir Monitor Serial;
2. Aproximar um objeto do Sensor A;
3. Ver se valor do Sensor A muda;
4. Aproximar um objeto do Sensor B;
5. Ver se valor do Sensor B muda;
6. Testar sem objeto;
7. Verificar se não trava quando não há eco.

Critérios de aprovação:

- Sensor A mostra valores coerentes;
- Sensor B mostra valores coerentes;
- Sem eco, o sistema não congela;
- Leituras inválidas são ignoradas;
- Distâncias acima do limite não poluem o processamento.

---

## ETAPA 4 — Validar Wi-Fi/IP do ESP32

Status:

```text
PENDENTE
```

Objetivo:

Garantir que Android e ESP32 estarão na mesma rede.

Validar:

- Nome da rede;
- IP do ESP32;
- Gateway;
- Se o IP final `.200` está sendo aplicado;
- Se o Android/multimídia está na mesma faixa de rede.

Exemplo esperado:

```text
Android: 192.168.0.X
ESP32:   192.168.0.200
Porta:   1234
```

Critério de aprovação:

```text
Android consegue alcançar o IP do ESP32.
```

---

## ETAPA 5 — Validar UDP do ESP32 sem Android

Status:

```text
PENDENTE
```

Objetivo:

Testar se o ESP32 recebe e responde UDP antes de envolver o app.

Opções de teste:

- Usar aplicativo de teste UDP no celular;
- Usar ferramenta UDP no computador;
- Criar sketch/app simples de teste;
- Usar o próprio Android depois que o firmware estiver validado.

Comandos a testar:

```text
iniciar:Media
iniciar:Moda
iniciar:Movel
params:...
restart
```

Critérios de aprovação:

- ESP32 recebe comando;
- Mostra comando no Serial;
- Responde pacote UDP;
- Resposta é numérica quando esperado;
- Não trava com comando inválido.

---

## ETAPA 6 — Corrigir `UDPProtocol.java` no Android

Status:

```text
PENDENTE
```

Objetivo:

Garantir comunicação estável no app.

Implementar:

- método `enviarEReceber`;
- timeout com `setSoTimeout(1000)`;
- tratamento de exceções;
- fechamento seguro de socket;
- retorno padronizado.

Retornos recomendados:

```text
OK
TIMEOUT
ERRO
VALOR_INVALIDO
```

ou objeto de estado equivalente.

Critérios de aprovação:

- Sem travamento quando ESP32 está desligado;
- Sem travamento quando Wi-Fi cai;
- Sem resposta antiga sendo reutilizada;
- Cada comando retorna sua própria resposta;
- Socket é fechado após uso.

---

## ETAPA 7 — Corrigir `CentralizadorSistema.java`

Status:

```text
PENDENTE
```

Objetivo:

Restaurar o funcionamento operacional da tela principal da colheitadeira.

Ações:

1. Comparar `CentralizadorSistema.java` com `CentralizadorSistema2.java`;
2. Identificar lógica UDP existente no `Sistema2`;
3. Reintegrar na tela atual;
4. Criar loop de atualização controlado;
5. Parar loop ao sair da tela;
6. Atualizar interface apenas na UI Thread.

Fluxo desejado:

```text
iniciar tela
↓
iniciar thread operacional
↓
enviar comando ao ESP32
↓
receber valor
↓
validar valor
↓
atualizar LEDs
↓
aguardar intervalo
↓
repetir
```

Critérios de aprovação:

- Tela operacional atualiza continuamente;
- Não trava sem ESP32;
- Para corretamente ao sair;
- Não cria múltiplas threads;
- Não atualiza interface fora da UI Thread.

---

## ETAPA 8 — Criar modo simulado Android

Status:

```text
PENDENTE
```

Objetivo:

Permitir testes sem ESP32 e sem colheitadeira.

Implementar modo que gere valores:

```text
-6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6
```

Possíveis botões:

```text
Esquerda
Centro
Direita
Automático
```

Usos:

- testar LEDs;
- testar setas;
- testar layout;
- testar alarmes;
- validar sentido de correção;
- treinar operador;
- depurar sem hardware.

Critérios de aprovação:

- LEDs respondem corretamente a todos os valores;
- Valor 0 mostra centralização;
- Valores extremos mostram alerta forte;
- Direção visual está clara.

---

## ETAPA 9 — Validar `CentralizadorLEDs.java`

Status:

```text
PENDENTE
```

Objetivo:

Garantir que os LEDs representam corretamente a situação da máquina.

Testar valores:

```text
-6
-3
-1
0
1
3
6
```

Validar:

- lado correto;
- quantidade de LEDs;
- cores/imagens;
- setas;
- estado centralizado;
- estado sem conexão;
- estado valor inválido.

Atenção crítica:

A direção deve ser validada fisicamente.

Pergunta obrigatória de validação:

```text
Quando o sensor indica valor positivo, o operador deve levar a máquina para qual lado?
```

Essa lógica não pode ser assumida sem teste.

---

## ETAPA 10 — Teste Android ↔ ESP32

Status:

```text
PENDENTE
```

Objetivo:

Validar comunicação real entre app e firmware.

Passos:

1. Ligar ESP32;
2. Confirmar IP no Monitor Serial;
3. Abrir app Android;
4. Entrar na tela operacional;
5. Enviar comando `iniciar:Media`;
6. Confirmar no Serial que o ESP32 recebeu;
7. Confirmar no app que LEDs atualizaram;
8. Repetir com `Moda` e `Movel`;
9. Desligar ESP32 para testar timeout;
10. Ligar novamente para testar recuperação.

Critérios de aprovação:

- Android envia comando;
- ESP32 recebe;
- ESP32 responde;
- Android atualiza tela;
- Timeout não trava;
- Reconexão é possível.

---

## ETAPA 11 — Teste de bancada

Status:

```text
PENDENTE
```

Objetivo:

Simular operação sem levar para o campo.

Montagem:

- ESP32 ligado;
- sensores posicionados;
- objetos simulando troncos/linhas;
- Android/multimídia conectada;
- rede Wi-Fi igual à real.

Testar:

- estabilidade por 30 minutos;
- movimentação de objetos;
- perda de sensor;
- perda de Wi-Fi;
- religamento do ESP32;
- mudança de parâmetros;
- troca de método de cálculo;
- atraso na resposta;
- atualização dos LEDs.

Critérios de aprovação:

- Sem travamentos;
- Sem reinícios inesperados;
- LEDs respondem rapidamente;
- App não congela;
- ESP32 continua enviando respostas;
- Falhas são tratadas.

---

## ETAPA 12 — Teste na multimídia

Status:

```text
PENDENTE
```

Objetivo:

Validar o app no hardware real onde será usado.

Testar:

- instalação do APK;
- resolução da tela;
- toque;
- brilho;
- desempenho;
- permissões de rede;
- conexão Wi-Fi;
- estabilidade com tela ligada;
- comportamento ao bloquear/desbloquear;
- comportamento após reiniciar multimídia.

Critérios de aprovação:

- App abre corretamente;
- Tela operacional aparece bem;
- Não há cortes no layout;
- Botões são usáveis;
- LEDs são visíveis;
- Comunicação UDP funciona.

---

## ETAPA 13 — Teste em colheitadeira parada

Status:

```text
PENDENTE
```

Objetivo:

Validar ambiente real sem operação em movimento.

Testar:

- alimentação elétrica;
- ruído eletromagnético;
- vibração leve;
- fixação dos sensores;
- distância real dos sensores;
- posição de instalação;
- Wi-Fi no ambiente da máquina;
- estabilidade do ESP32;
- leitura com máquina ligada.

Critérios de aprovação:

- Sensores leem valores coerentes;
- ESP32 não reinicia;
- App permanece conectado;
- LEDs respondem;
- Cabos e alimentação não causam falhas.

---

## ETAPA 14 — Teste em campo com operação assistida

Status:

```text
PENDENTE
```

Objetivo:

Validar o sistema em operação real.

Atenção:

Este teste deve ser feito com cuidado, com operador ciente de que o sistema ainda está em validação.

Validar:

- sentido correto da orientação;
- atraso entre sensor e tela;
- estabilidade em vibração;
- leituras falsas;
- poeira/sujeira;
- reflexo nos sensores;
- sol na tela;
- comportamento em curvas;
- comportamento em desalinhamento real.

Critérios de aprovação:

- Operador entende a indicação;
- Direção indicada está correta;
- Sistema não induz erro;
- App não trava;
- ESP32 não perde conexão;
- Resposta é rápida o suficiente.

---

## 10. Prioridade Atual Real

A prioridade mudou depois que o ESP32 voltou a compilar.

Antes, o foco era recuperar o firmware.

Agora, a prioridade é:

1. Fazer upload e teste físico do ESP32;
2. Confirmar sensores no Monitor Serial;
3. Confirmar Wi-Fi/IP;
4. Confirmar UDP;
5. Corrigir `UDPProtocol.java`;
6. Corrigir `CentralizadorSistema.java`;
7. Validar `CentralizadorLEDs.java`;
8. Criar modo simulado;
9. Testar Android ↔ ESP32;
10. Testar na multimídia;
11. Testar em bancada;
12. Testar em campo.

Prioridade técnica máxima no Android:

```text
CentralizadorSistema.java
UDPProtocol.java
CentralizadorLEDs.java
```

Prioridade técnica máxima no ESP32:

```text
sensores.ino
wifi_manager.h
watchdog.h
processamento estatístico
UDP
```

---

## 11. Checklist de Validação

### ESP32

- [x] Firmware compila
- [x] Task FreeRTOS corrigida
- [x] `pulseIn()` com timeout
- [x] Conflito `WiFi.h` corrigido
- [x] Watchdog compatível com Core 3.x
- [ ] Upload realizado no ESP32
- [ ] Monitor Serial validado
- [ ] Sensor A validado
- [ ] Sensor B validado
- [ ] Wi-Fi validado
- [ ] IP validado
- [ ] UDP validado
- [ ] Teste de estabilidade realizado

---

### Android

- [x] Projeto base existente
- [x] APK existente
- [x] Telas principais existentes
- [x] LEDs existentes
- [x] UDP implementado
- [ ] `UDPProtocol.java` revisado
- [ ] Timeout UDP implementado
- [ ] `CentralizadorSistema.java` corrigido
- [ ] Thread operacional estabilizada
- [ ] UI Thread protegida
- [ ] Modo simulado criado
- [ ] LEDs validados
- [ ] Teste com ESP32 realizado
- [ ] Teste em multimídia realizado

---

### Campo

- [ ] Teste de bancada
- [ ] Teste com colheitadeira parada
- [ ] Teste com colheitadeira em operação
- [ ] Validação do sentido de correção
- [ ] Validação de estabilidade
- [ ] Validação com operador

---

## 12. Melhorias Futuras

### 12.1 Substituir StackArray

Trocar por:

- `std::vector<int>`;
- buffer circular fixo;
- fila com tamanho limitado.

Objetivo:

- reduzir dependência externa;
- melhorar estabilidade;
- evitar estouro de memória.

---

### 12.2 Criar logs técnicos no Android

Criar classe:

```text
SystemLog
```

Registrar:

- comandos enviados;
- respostas recebidas;
- timeouts;
- falhas;
- reconexões;
- valores dos sensores;
- estado visual exibido.

---

### 12.3 Criar logs técnicos no ESP32

Registrar no Serial:

- início do firmware;
- IP;
- comando recebido;
- resposta enviada;
- leituras dos sensores;
- erros de sensor;
- reinícios por watchdog.

---

### 12.4 Reconexão automática

Se ESP32 desconectar:

```text
mostrar alerta
tentar reconectar
não travar app
não reutilizar valor antigo sem aviso
```

---

### 12.5 Tela de diagnóstico

Criar tela técnica no Android com:

- IP do ESP32;
- último comando;
- última resposta;
- latência;
- status da conexão;
- valores brutos dos sensores;
- versão do firmware;
- versão do app.

---

### 12.6 Modo calibração

Criar fluxo para calibrar:

- distância mínima;
- distância máxima;
- distância da barra;
- ângulo;
- sensibilidade;
- método estatístico.

---

### 12.7 Telemetria/Firebase

Futuro possível:

- backup de dados;
- sincronização;
- histórico;
- relatórios remotos;
- manutenção preventiva.

---

### 12.8 Atualização OTA do ESP32

Futuro possível:

- atualizar firmware sem cabo USB;
- facilitar manutenção em campo;
- reduzir deslocamento técnico.

---

## 13. Conclusão Técnica

O projeto é recuperável e possui boa base técnica.

Ele não precisa ser refeito do zero.

O núcleo já existe:

```text
ESP32
↓
UDP
↓
Android
↓
LEDs
↓
Operador
```

O firmware do ESP32 já passou pela primeira estabilização e voltou a compilar.

O próximo foco é validar o ESP32 fisicamente e, em seguida, estabilizar o Android na comunicação real.

O ponto mais crítico agora é garantir que:

```text
o valor calculado pelo ESP32
chegue corretamente ao Android
e seja exibido corretamente ao operador
```

A partir disso, o projeto pode avançar para bancada, multimídia e campo com muito mais segurança.
