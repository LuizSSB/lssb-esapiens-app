
# Teste eSapiens
App Android relativo teste para a vaga de dev Android/backend na empresa eSapiens.

O app apresenta uma listagem de 20 pessoas aleatórias e permite que o usuário selecione uma delas para visualizar uma tela de detalhes.

Preview:

![Preview (listagem)](https://i.imgur.com/eRVii3S.png)
![Preview (detalhe)](https://i.imgur.com/V2vdboL.png)

## Características
 - Dispositivos: smartphones Android (pode ser executado em tablets, mas layouts não foram otimizados);
 - Android: API 19+;
 - Linguagem: Kotlin
 - Orientações: retrato e paisagem;
 - Dependências não-*padrão*:
	 - Android Architecture Components - Lifecycle: 1.1.1;
	 - Android Architecture Components - Room, 1.1.1;
	 - Glide, 4.8.0;
	 - Kodein, 6.0.1;
	 - Kotlin Annotation Processing Tool;
	 - Kotlin Coroutines, 1.0.1;
	 - Retrofit, 2.5.0;
 - Nenhum erro ou alerta de compilação.

Devido a API REST do exercício não oferecer qualquer tipo de paginação, decidiu-se que o app apenas mostra os registros retornados a cada requisição/atualização. Nesse momento, os registros anteriores são perdidos.

Possui view inicial de carregamento, caso o app não tenha *cacheado* registros anteriormente. 

Ao detectar que a conexão com a internet foi reestabelecida, requisita novos registros para API REST. Observação: embora tenha sido implementado suporte a dispositivos com API 23-, a validação dessa feature falha; ressalta-se, contudo, que tal validação foi conduzida apenas via emulador, o qual não parece ser totalmente responsivo a estes eventos, i.e. em dispositivos reais, há chance maior de que funcione como esperado).

Código segue padrão MVVM possibilitado pelos Android Architecture Components, com activities, fragments, view models e repositories. Também contém adaptação de `NetworkBoundResource`, para indicar o carregamento de itens

## Pacotes
### `br.com.luizssb.esapienschallenge`
Pacote raiz, contém classes relativas a aplicação, como um todo.
- `ChallengeApplication`: subclasse de `Application`, usada pelo Kodein.
- `ChallengeGlideModule`: subclasse de `AppGlideModule` (inútil?).
- `Constants`: classe estática repositório de constantes.

### `br.com.luizssb.esapienschallenge.data`
Classe e interface relacionadas ao consumo de dados pelo Room.
- `ChallengeDatabase`: especificação da base interna para cache dos registros consultados.
- `PersonDao`: data access object para a entidade Person, com consulta, inclusão em massa e deleção em massa.

### `br.com.luizssb.esapienschallenge.dependencies`
Classe, funções e anotação para injeção de dependências.
- `DependencyManager`: classe estática para carregar o container inicial do Kodein.
- `DependencyModuleLoader`: annotation para sinalizar uma função responsável por carregar módulo de dependências do Kodein (não processada).
- *DependencyModules.kt*: arquivo com funções de carregamento dos módulos de dependência.
- *Utils.kt*: arquivo com funções de injeção de view model em activity/fragment, com injeção do container do Kodein e outros parâmetros.

### `br.com.luizssb.esapienschallenge.model`
Classes relativas aos modelos de dados do app.
- `Person`: entidade que é requisitada para a API REST e cacheada.
- `Resource`: representa um recurso que pode ser de alguma maneira adquirido, com ou sem erro.

### `br.com.luizssb.esapienschallenge.network`
Classes e interface detecção de conectividade com a internet.
- `ConnectivityManagerProxy`: interface para representar um objeto proxy para o serviço `ConnectivityManager`do Android
- `NougatPlusConnectivityManagerProxy`: implementação de `ConnectivityManagerProxy` para dispositivos com API24+.
- `ReachabilityReceiver`: broadcast receiver para notificações *android.net.conn.CONNECTIVITY_CHANGE* e implementação de `ConnectivityManagerProxy` para dispositivos com API23-.

### `br.com.luizssb.esapienschallenge.repository`
Classes para atuarem como repositório de aquisição de dados.
- `NetworkBoundResource`: classe abstrata para dados que podem ser requisitados a API REST ou ao banco de dados.
- `PersonRepository`: repositório para adquirir e acessar os registros consultados.

### `br.com.luizssb.esapienschallenge.service`
Classes e interface para consumo da API REST.
- `ApiResponse`: resposta para uma requisição da API REST, contendo os dados retornados ou o erro ocorrido
- `ChallengeRestAPI`: API Kotlin para consumir a API REST.
- `PersonService`: classe para consumir os serviços relativos a pessoas (o único do teste).
- `RetrofitClientInstance`: classe estática para instanciar e configurar o Retrofit e poder consumir a API REST.

### `br.com.luizssb.esapienschallenge.ui`
Classes e extensões relacionados a UI.
- `BaseAppActivity`: ativitidade abstrata raiz de todas as outras do app; usada para mudar a cor da barra de status apenas.
- `InjectionDependentViewModel`: classe abstrata raiz de view models que requerem injeção de dependências.

### `br.com.luizssb.esapienschallenge.ui.extension`
Extensões úteis para componentes de UI.
- *Activity.kt*: extensões para activities.
- *ImageView.kt*: extensões para image views.

### `br.com.luizssb.esapienschallenge.ui.item`
- `PersonGridCell`: célula de grid view para exibir uma pessoa.

### `br.com.luizssb.esapienschallenge.ui.main`
Classes relativas a página principal (listagem).
- `MainActivity`: launch activity; faz nada.
- `MainFragment`: fragment com listagem de pessoas.
- `MainViewModel`: view model para a listagem de pessoas.
- `PeopleAdapter`: adapter para configurar as células de pessoas.

### `br.com.luizssb.esapienschallenge.ui.profile`
Classes relativas a página de perfil.
- `ProfileActivity`: activity para carregar a página; configura detalhes visuais menores.
- `ProfileFragment`: fragment para exibir o perfil de uma pessoa.
- `ProfileViewModel`: view model para o perfil da pessoa (essencialmente inútil, mas fica aí, por questão de padrão).

### TODO

 - Testes de unidade.
 - Testes de UI.
 - Especialização de layouts e dimen para dispositivos grandes.
 - Integrar status bar com toolbars (provavelmente implica em gambiarra).

-- Luiz Soares dos Santos Baglie (luizssb.biz {at} gmail {dot} com)