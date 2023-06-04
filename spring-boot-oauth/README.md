## OAuth 2.0: Authorization Code Flow in Spring Boot
https://curity.io/resources/learn/oauth-code-flow/
https://medium.com/javarevisited/oauth-2-0-authorization-code-flow-in-spring-boot-d8ff393f316d

### Prepare
	cd C:\opt
	mkdir spring-boot-oauth
	
### Run
	Start PostgreSQL Docker container:
	docker-compose up

	Start Authorization Server
	
	Start Resource Server
	
	Check the authorization server capabilities:
	curl -X GET http://localhost:9000/.well-known/oauth-authorization-server | jq .

	Redirection to the authorization server - log in
	http://localhost:9000/oauth2/authorize?response_type=code&scope=articles.read%20articles.write&client_id=articles-client&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/articles-client-oidc
	(Authorization Server presents the login page)
	Submit user credentials
	(Authorization Server serves the consent page)
	Submit consent
	(Redirected to the client app url)
	
	Simulate acquiring the access token using Postman:
		grant_type: authorization_code
		redirect_uri: http://127.0.0.1:8080/login/oauth2/code/articles-client-oidc
		code: 4ZYvXIPq7ctSPCi0vIbUW4LeEqTifLsY61GVgeZzW4M2_PSOr91btJ-MAYOZ2BLTU5WJS3IH3tNl_aqUjOid4NnxwlX4hruNX00mxbAWdYRU-XdM2yVrimeugZqqcSc8
	Retrieve the articles from the resource server using Postman:
	http://localhost:8090/articles
	Under Authorization tab, enter the access token.
	Send

### Using cURL - Bash -TODO
	1. Redirection to the authorization server - log in
	   Cookies is saved to file cookies.txt. _csrf hidden value is exported to CSRF variable.
	   
	export CSRF=$(curl -c cookies.txt  "http://localhost:9000/oauth2/authorize?response_type=code&scope=articles.read%20articles.write&client_id=articles-client&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/articles-client-oidc" | grep -oP '.*<input\s+name=\"_csrf\"\s+type=\"hidden\"\s+value=\"\K[0-9a-z\-]+')
	
	2. Submit credential to authorization server. 
	Supply the -L or --location option in order to enable curl to follow HTTP redirects.
	
	curl -b cookies.txt -L -X POST "http://localhost:9000/login?username=username&password=password&_csrf=$CSRF" 
	
	Simulate acquiring the access token using curl
	Git Bash:
	export CODE=<code>
	
	export TOKEN=$(curl -H 'Authorization:Basic YXJ0aWNsZXMtY2xpZW50OnNlY3JldA==' \
	-X POST "http://localhost:9000/oauth2/token?grant_type=authorization_code&code=$CODE&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/articles-client-oidc" \
	| jq -r '.access_token')

	curl localhost:8090/articles \
	-H "Authorization: Bearer $TOKEN" | jq .

### Stop PostgreSQL Docker container
	docker-compose down