**Quarkus 3.14.1**
<br>
**JAVA 21**

**CORE DEPENDENCIES:**
<br>
implementation("io.quarkus:quarkus-security")
<br>
implementation("io.quarkiverse.amazonservices:quarkus-amazon-dynamodb-enhanced:2.16.2")


To run it:
<br>
**1)** Need Docker
<br><br>
**2)** Run command [ **docker run -p 8000:8000 amazon/dynamodb-local**  ]
<br><br>
**3)** Find file [ **dynamodbconfig_security.json** ]. File is located in [ **src/main/resources** ]
<br><br>
**4)** Run command on this file [ **aws dynamodb create-table --cli-input-json file://dynamodbconfig_security.json --endpoint-url http://localhost:8000** ]
<br><br>
**5)** Verify if table has been created with command [ **aws dynamodb scan --table-name Table --endpoint-url http://localhost:8000** ]
<br><br>
**6)** Run application [ **port is set to 9083** ]
<br><br>
**7)** Create user with endpoint  [ **POST http://localhost:9083/hello/save** ]
<br><br>
**8)** Verify if user has been created with command [ **aws dynamodb scan --table-name Table --endpoint-url http://localhost:8000** ]. You should see user with **email: "jakub@gmail.com"** and **roles: ["admin"]**.
<br><br>
**9)** Check if you have access to [ **GET http://localhost:9083/hello** ]. You should have access and you should see "hello" response.
<br><br>
**10)** Go to [ **/security/TestAugmentor.kt** ] and switch from **B** version of augment function to **A** version.
<br><br>
**11)** Hit [ **GET http://localhost:9083/hello** ] once again and now you should see [ **ContextNotActiveException** ]
