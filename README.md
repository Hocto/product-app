REST Stock Application

Requirements:
- User is able to add new item to stock. Each item contains name, price and quantity.
- Items might be additionally updated, deleted or sold.
- Possibility to report sold items and current stock levels.
- Provide REST endpoints for user operations is enough (UI not needed).
- All operations can be public, no need to focus on authentication level.
- Containerize application with Docker
- Keep the solution small and clean.

Steps to initialize:
- ./gradlew build -x test
- docker image build -t product-app .
- docker-compose up

Application will be ready on 8080 port. 
You can see the API doc here(http://localhost:8080/swagger-ui/)