HotMart's HotChat Coding Test 
Thiago Miceli - thiagomiceli@gmail.com  

**** Instruções ****

1. Baixar o repositório do projeto em https://github.com/thiagomiceli/HotChatApp

2. No diretório da aplicação, executar o comando
	 mvn clean package embedded-glassfish:run -DskipTests=true
	 
3.  Em um novo terminal, diretório da aplicação, executar os testes automatizados com o comando:
	mvn test
	
Considerações:
 * Após executar o passo 2 a aplicação estará disponível através da url
 	http://0.0.0.0:8080/hotchat/
 	
 * Serão criados automaticamente três usuários para o propósito de testes
 	userName: usr1, password: 1
 	userName: usr2, password: 2
 	userName: usr3, password: 3
 	
 * Ao realizar o login será criado um cookie para o tratamento da autorização de acesso as páginas,
 portanto não será possível realizar testes funcionais para simuar um chat entre dois usários utilizando o mesmo browser. Para tal utilizar dois browsers diferentes ou utilizar o recurso de navegação anônima do Chrome ;)
 	