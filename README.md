# CEIFA_App

Esse código de aplicativo se refere à uma aplicação empresarial que mostra ao usuário todos os produtos que ele tem acesso e provê acesso ao que o produto faz. Em primeiro lugar, a classe "SelecaoEquipamento" é responsável por mostrar ao usuário os produtos, com botões clicáveis, e garantir acesso ao site da empresa. A descrição dos botões são:
        
        -> "Centralizador da Colhedora" (nome de código (XML): "direcionador")
        -> "Gerenciador de Colheita"    (nome de código (XML): "gerenciador")
        -> "Monitor Animal"             (nome de código (XML): "monitor")

Relativo ao site, o acesso é dado através da logotipo da empresa (Ceifa). Quando o usário clica na logotipo, ele será redirecionado para o site (o qual usa o navegador). Agora explicarei todos os produtos (cuja parcela do código esteja inserida) e como as classes que eles precisam para funcionar. Nesse momento, esse código possui os seguintes produtos:
        
        -> Centralizador de Colhedora (Finalizado/Em constante atualização)
        -> Gerenciador de Colheita (Não atualizado nesse projeto, dado que existe um exclusivo para o mesmo)
        -> Monitor Animal (Não inicializado)
        
------------------------------------------------   Centralizador da Colhedora   -----------------------------------------------
        
Para desenvolver as partes que se referem a esse produto, usei as seguintes classes:
        -> "CentralizadorAjustes.java"
        -> "CentralizadorAjustesPrincipal.java"
        -> "CentralizadorContaPes.java" (Em desuso)
        -> "CentralizadorGrafico.java" (Em desuso)
        -> "CentralizadorLEDs.java"
        -> "CentralizadorParametros.java"
        -> "CentralizadorPrincipal.java"
        -> "CentralizadorRelatorio.java"
        -> "CentralizadorSistema.java"
        -> "CentralizadorSistema2.java" (Em desuso)

Sendo assim, todas as classes que possuem Principal em seu nome constituem apenas telas de explicação/introdução ao usuário, para que ele tenha conhecimento do que deverá ser feito em seguida.

A classe "CentralizadorAjustes.java" refere-se à uma propriedade do sistema que dá ao usuário a autonomia de selecionar os valores para os parâmetros a serem utilizados, tais como distância entre as barras, maior e menor distância medida pelos sensores. Ressalta-se que essa tela só deverá estar disponível ao usuário durante sua primeira utilização, de forma que para acessos futuros, será necessário uma senha de acesso.

        Regras acerca da digitação de dados:
        -> O usuário pode digitar apenas números
        -> O usuário não pode enviar um campo vazio (EditText)
        -> O usuário pode digitar um número entre 0 e 999
        
Se o usuário seguir todas as regras acima, ele será capaz de passar para a próxima página, a qual tem o sistema propriamente dito, além de ter os dados inseridos salvos no banco de dados. Essa função diz respeito à classe "CentralizadorSistema.java". Essa classe provê ao usuário a capacidade de mudar o tempo para o envio de informações, ou seja, de quanto em quanto tempo informações ser]ao enviadas/recebidas do microcontrolador utilizado (ESP32). Sendo assim, esse tempo pode variar entre:

        -> 1.0 segundos;
        -> 1.1 segundos;
        -> 1.2 segundos;
        -> 1.3 segundos;
        -> 1.4 segundos;
        -> 1.5 segundos;

Com isso, através do protocolo de comunicação UDP, informações são trocadas do dispositivo utilizado com o microcontrolador supracitado. Assim, o usuário é capaz de observar, através de "LEDs" coloridos o quão a máquina está descentralizada com a plantação. Para auxílio de compreensão, o sistema ainda dispõe de setas que indicam para que lado o operador deve redirecionar a máquina:

        -> Um (1) único LED verde claro aceso indica que a máquina está centralizada;
        -> LEDs verde escuro indicam que a máquina está um pouco descentralizada, exigindo leves variações de movimentação;
        -> LEDs amarelos indicam que a máquina está consideravelmente descentralizada, exigindo uma manobra um pouco mais forte que a anterior;
        -> LEDs vermelhos indicam que a máquina está muito descentralizada, exigindo que o operador manobre a máquina "bruscamente";
        -> LEDs à direita do LED central indica que a máquina está descentralizada para a direita, implicando que o operador deve leva-la para a esquerda;
        -> LEDs à esquerda do LED central indica que a máquina está descentralizada para a esquerda, implicando que o operador deve leva-la para a direita;

Além disso, o usuário, após parar a execução do sistema (com o clique no botão de Parar) pode acessar a função de relatório, que consiste em apresentar ao mesmo o "tempo" que o operador permaneceu desalinhado. Esse tempo é dado através de amostras, ou seja, não consiste em uma medida real de horas, minutos e e segundos.

As classes "CentralizadorGrafico.java", "CentralizadorContaPes.java" e "CentralizadorSistema2.java" não estão sendo usadas no momento.
