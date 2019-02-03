# beer-application
###Tecnologias utiltizadas:

- Java 11
- Maven 3
- Spring Framework 5 com Spring boot 2
- Spring REST Docs 2
- Lombok 1.1

Obs: Para uma melhor leitura do código utilizando Lombok
recomendo que seja instalado um plugin para sua IDE.
Segue os links de como instalar este plugin para as principais
IDEs:

- https://projectlombok.org/setup/intellij
- https://projectlombok.org/setup/eclipse
- https://projectlombok.org/setup/netbeans

####Documentação
https://rauldavid.github.io/beer-application

Como Spring REST Docs a documentação é gerada a partir dos testes.
Caso algum teste falhe a documentação não é gerada para o endpoint.
Isso é muito bom pois garante a confiabilidade da documentação e da
própia aplicação. O link acima possui a última documentação gerada
pelo último build que realizei. Caso seja feita alguma alteração nos
endpoints é preciso realizar o comando `mvn package` subir a aplicação
e acessar o endpoint `/docs/index.html` para ver a documentação gerada.

####Build
#####Local

Você só precisa ter instalado o docker na sua máquina.
Execute o arquivo `build-linux.sh` se o seu SO for linux.
Obs: o build demora cerca de 8min.

Para Windows, na pasta do projeto execute os seguintes comandos:

`docker build -t beer-image .`

`docker run  -p 8080:8080 --name beer-container -t beer-image`

Caso você tenha um ambiente de desenvolvimento já configurado com
Java 11 (`java --version`) e maven 3 (`mvn --version`),
recomendo que na raiz do projeto seja feito o comando
`mvn spring-boot:run -Dspring.profiles.active=prod`.

  

