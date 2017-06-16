Desafio do Centro Logístico

Apresentação do desafio

A CI&T desenvolverá micro serviço para a logística de um cliente, no abastecimento de
carga de veículos de um dos centros logísticos onde o cliente atua. O abastecimento da
carga no veículo é importante para sincronizar ordem cronológica da rota de entrega.

Para tal cliente, as entregas sempre iniciam com o pacote mais leve primeiro, e o mais
pesado por último. Conforme a figura abaixo, o centro logístico em questão garante a
disponibilização da carga na ordem correta de entrega (o mais pesado embaixo e o mais
leve no topo) na zona de abastecimento (A), e cabe ao funcionário responsável pela doca, a
transferência correta, de acordo com a ordem inicial colocada.

O processo de abastecimento do veículo possui disponível uma zona de transferência (T) e
por segurança, há algumas restrições:

● Todas as zonas (zona de abastecimento [A], zona de transferência [T] e zona do
caminhão [C]) possuem espaço somente para pacotes empilhados e somente uma
pilha pode ser formada em cada zona (inclusive no caminhão);

● Somente um pacote pode ser movimentado por vez;

● Nunca um pacote mais pesado deve ser colocado sobre um pacote mais leve;

Com tais restrições, um pacote será movimentado diversas vezes para ordem correta na
colocação no veículo, conforme as setas de sentido duplo indicam.

Modelo de abastecimento na doca: carga na zona de abastecimento, e carga através do uso da zona de transferência

A API a ser construída será integrada a um terceiro sistema lúdico, auxiliando o motorista
em um passo-a-passo, para que o abastecimento do caminhão ocorra corretamente.

Objetivos da API

O terceiro sistema ainda está em concepção (teremos liberdade para propor os contratos
da API), e a integração ocorrerá em dois principais endpoints:

● Cadastro de um conjunto de cargas para um veículo: especificar ID do veículo, ID da
entrega, lista de pacotes com o identificador, e peso individuais de cada pacote.

Como exemplo abaixo:

POST /delivery

{
    “vehicle” : “123456”,
    “deliveryId” : “1234567890”
    “packages” : [
        { "id": “1”, “weight”: “14.50”},
        { "id": “2”, “weight”: “12.15”},
        { "id": “3”, “weight”: “19.50”}
    ]
}

---

201 CREATED

● Consulta de instruções para abastecimento de carga no veículo, dado um ID de
entrega e ID de veículo. A consulta deve informar os passos para movimentação da
carga entre o centro logístico, a zona de transferência e o veículo, como no exemplo
abaixo:

GET /delivery/{deliveryId}/step

[
    { "step" : 1, "packageId": 2, "from": "zona de abastecimento", "to": "zona do caminhão" },
    { "step" : 2, "packageId": 1, "from": "zona de abastecimento", "to": "zona de transferência" },
    { "step" : 3, "packageId": 2, "from": "zona do caminhão", "to": "zona de transferência" },
    { "step" : 4, "packageId": 3, "from": "zona de abastecimento", "to": "zona do caminhão" },
    { "step" : 5, "packageId": 2, "from": "zona de transferência", "to": "zona de abastecimento" },
    { "step" : 6, "packageId": 1, "from": "zona de transferência", "to": "zona de caminhão" },
    { "step" : 7, "packageId": 2, "from": "zona de abastecimento", "to": "zona de caminhão" }
]

---

200 OK

Pontos relevantes

● Os dados não podem ser perdido a cada novo redeploy

● Seu código será testado em cenários complexos, portanto não se esqueça dos
requisitos não-funcionais

● Planeje como o deploy da aplicação será feito (12factor)

● Gere uma documentação de suas APIs

● Utilize a plataforma Java para o desenvolvimento da solução do desafio

Entregáveis

● Código da API versionado no Github

● Instruções ou script para fazer build and deploy da aplicação

● Lista de requisitos não funcionais considerados

● Documentação da API

● Plano para execução do desafio: lista de tarefas, envolvendo codificação ou não, e
tempo estimado para realizá-las

Avaliação

● Os entregáveis serão avaliados individualmente quanto a completude e a clareza

● Seguindo as instruções de build e deploy, seu código será instalado em um
ambiente de teste

● Seguindo a documentação da API, testes automatizados serão escritos e executados
contra a API, com variações de cenários funcionais e não funcionais.