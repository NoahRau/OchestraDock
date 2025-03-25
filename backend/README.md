# API Docs

to access the api docs, run the backend and access `http://localhost:3080/swagger.html`

## Todo

- Token Refresh endpoint
- improve Logging
- UI
- ?

## Test the api

Open the Open Api docs
1. Create a new User
2. Note down the ID
3. Login into the Account
4. Copy the JWT Token
5. Place it in the Autorize Button in the top right corner
6. Now you should be able to create/delete/update/list the tasks

## Show the MongoDB Stuff

to login into the mongodb run the following commands

```bash
docker exec -ti mongodb mongosh
use admin
db.auth("app", "test1234")
use todoapp
show collections
db.users.find().pretty()
db.tasks.find().pretty()
db.users.getIndexes()
```

## Linting in IntelliJ

Go to Editor > Code Style > Kotlin, then select "Imports" and choose "Use single name import".

