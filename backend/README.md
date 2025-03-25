# API Docs

to access the api docs, run the backend and access `http://localhost:3080/swagger.html`

## curl Commands to Test the API

1. Create a User

  ```shell
  curl -s -X POST "http://localhost:3080/api/v1/users?username=john&password=secret"
  ```

  Response: User JSON with auto-generated id.

2. Obtain a JWT (Minimal Example)

  If you have an AuthController that issues JWT upon login, for example:

  ```shell
  curl -s -X POST \
    -H "Content-Type: application/json" \
    -d '{"username":"john","password":"secret"}' \
    http://localhost:3080/api/v1/auth/login
  ```

  Response: Something like { "token": "eyJhbGci..." }

3. Create a Task (Authenticated)

  ```shell

  curl -s -X POST "http://localhost:3080/api/v1/tasks" \
    -H "Authorization: Bearer <PUT-YOUR-JWT-HERE>" \
    -H "Content-Type: application/json" \
    -d '{
      "description": "Buy groceries",
      "completed": false
  }
  ```

4. List All Tasks

  ```shell

  curl -s -X GET "http://localhost:3080/api/v1/tasks" \
    -H "Authorization: Bearer <PUT-YOUR-JWT-HERE>"
  ```

5. Update a Task Partially

  ```shell

  curl -s -X PATCH "http://localhost:3080/api/v1/tasks/123" \
    -H "Authorization: Bearer <JWT>" \
    -H "Content-Type: application/json" \
    -d '{"completed": true}'

  ```
