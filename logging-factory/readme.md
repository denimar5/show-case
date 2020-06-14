Esse projeto tem como objetivo capturar todo request e response e enviar para o Logstach.
Para usa-lo siga o passo no seu projeto:
1) 
    <dependency>
			<groupId>com.logging.factory</groupId>
			<artifactId>logging-factory</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
    
 1.1) Nesse projeto, vá até  a pasta resrouces e altere o IP e porta do Logstash no qual sua ingra ELK está instalado.
  No caso de applicações com conte
 spring:
  application:
    name: suaAplicacao
  logstash:
    url: 10.01.187:5000
    
 2)Ou, se sua aplicação residirá eum uma imagem kubernetes ou Docker. Crie essas variaveis de ambiente {SUA_APLICACAO} no seu aquivo docker.file 
 spring:
  application:
    name: {SUA_APLICACAO}
  logstash:
    url: {IP_PORTA_LOGSTASH}
