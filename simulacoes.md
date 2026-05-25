# Guia de Testes e Simulações

Este arquivo descreve o passo a passo para validar cada funcionalidade do sistema, tanto em ambiente simulado quanto com o hardware real.

## ETAPA 1: Testes em Modo Simulado (Sem Hardware)
*Objetivo: Validar a lógica visual, interface e persistência no banco de dados.*

### Teste 1.1: Comportamento Visual (LEDs e Setas)
1. Abra o aplicativo.
2. Navegue até **Utilizar Sistema**.
3. Selecione qualquer **Operador**.
4. Ative a chave **Simular** na parte inferior.
5. Clique no botão **Iniciar**.
6. **O que observar:**
   - Os blocos de LED centrais devem acender e mudar de posição suavemente.
   - Quando os LEDs se deslocarem para a **DIREITA**, a seta da **ESQUERDA** deve acender (indicando correção).
   - A cor da seta deve mudar conforme a intensidade do desalinhamento: **Verde** (leve), **Amarela** (médio), **Vermelha** (crítico).

### Teste 1.2: Registro de Produção (Banco de Dados)
1. Com a simulação ainda rodando, clique no botão **Marcar Pé** exatamente **3 vezes**.
2. Clique em **Pausar**.
3. Clique em **Voltar**.
4. Entre na **Área do Administrador** (Senha: `159357`).
5. Clique em **Relatórios**.
6. **O que observar:**
   - Devem existir 3 novos registros na lista.
   - Verifique se o nome do operador e o horário batem com o teste que você acabou de fazer.

### Teste 1.3: Validação de Filtros
1. Na tela de Relatórios, clique no filtro **Operador**.
2. Selecione o operador usado no teste anterior.
3. Verifique se a lista mostra apenas os dados dele.
4. Clique em **Limpar Filtros**.
5. Verifique se a lista completa volta a aparecer.

---

## ETAPA 2: Testes com ESP32 (Hardware Real)
*Objetivo: Validar comunicação de rede, latência e robustez.*

### Teste 2.1: Sincronização de Parâmetros
1. Conecte o Tablet ao Wi-Fi do ESP32.
2. Vá em **Área do Admin** -> **Ajustes**.
3. Mude o valor de **Distância entre Barras** e clique em **Avançar**.
4. **O que observar:**
   - Verifique no console (Logcat) do Android Studio se a mensagem `enviar Mensagem params:...` foi disparada com sucesso.

### Teste 2.2: Resistência a Falhas (Timeout)
1. Com o app em execução na tela operacional, **desligue a alimentação do ESP32**.
2. Clique em **Iniciar**.
3. **O que observar:**
   - O aplicativo **não pode travar**.
   - O sistema deve tentar a conexão por 1 segundo e retornar ao estado de espera, permitindo que você clique em outros botões.

### Teste 2.3: Controle de Tempo Operacional
1. Vá em **Ajustes** e desmarque a opção **"Liberar Scroll para Operador"**.
2. Defina o **Tempo Fixo** como `1.5`.
3. Salve e vá para a tela de **Utilizar Sistema**.
4. **O que observar:**
   - A barra de rolagem (SeekBar) no topo da tela deve estar **cinza e bloqueada**.
   - O texto deve indicar fixamente `1.5 segundos`.
