# beer-application
### Tecnologias utiltizadas

- Java 11
- Maven 3
- Spring Framework 5 com Spring boot 2
- Spring REST Docs 2
- Lombok 1.1
- JUnit 5
- MongoDB (flapdoodle testes e mLab remoto)
- Heroku
- Docker

Obs: Para uma melhor leitura do código utilizando Lombok
recomendo que seja instalado um plugin para sua IDE.
Segue os links de como instalar este plugin para as principais
IDEs:

- https://projectlombok.org/setup/intellij
- https://projectlombok.org/setup/eclipse
- https://projectlombok.org/setup/netbeans

### Documentação
https://rauldavid.github.io/beer-application

ou

https://raul-beer-application.herokuapp.com/docs/index.html

Todos os endpoints estão documentados.

Com Spring REST Docs a documentação é gerada a partir dos testes.
Caso algum teste falhe a documentação não é gerada para o endpoint.
Isso é muito bom pois garante a confiabilidade da documentação e da
própia aplicação. Caso seja feita alguma alteração local nos
endpoints é preciso realizar o comando `mvn package` subir a aplicação
e acessar o endpoint `/docs/index.html` para ver a documentação gerada.

### Build
#### Local

Você só precisa ter instalado o docker na sua máquina.
Execute o arquivo `build-linux.sh` se o seu SO for linux.

Obs: o build demora cerca de 8min.

Para Windows, na pasta do projeto execute os seguintes comandos:

`docker build -t beer-image .`

`docker run  -p 8080:8080 --name beer-container -t beer-image`

Caso você tenha um ambiente de desenvolvimento já configurado com
Java 11 (`java --version`) e maven 3 (`mvn --version`),
recomendo realizar o seguinte comando na raiz do projeto:
`mvn spring-boot:run -Dspring.profiles.active=prod`.

#### Remoto

Para fazer o build remotamente basta fazer um push na branch
`production`. A nova versão será gerada em alguns minutos 
(cerca de 3min) e será possível acessá-la
através do host: `raul-beer-application.herokuapp.com`. Onde poderá
fazer os requests disponíveis. Por exemplo: 
- https://raul-beer-application.herokuapp.com/beer-styles

#### Considerações

Já existe uma base de estilos de cerveja persistida no mongo
remoto basta consultar o link citado na sessão de como realizar o build
remotamente.

São poucos os estilos de cerveja que contém seu nome na playlist,
por isso criei um parâmetro no endpoint para não validar o nome 
se necessário:
- https://raul-beer-application.herokuapp.com/playlist-beers/beer-styles?temperature=10&checkName=false
  

