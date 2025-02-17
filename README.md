`
# Payment Application.

### About 

This app provides 3 endpoints to:

- Set webhook `webhook/set`
- Add new payment `payment/add`
- View all payments `payment/all`

### Prerequisites
- Install Docker 
- Postman - Please use Postman. This app hasn't been tested using curl yet. There maybe issues with the self-signed certificate and curl.

### Build
This app is built with Java 17 with Spring Boot version 3.4.2 with a mysql database. 

### How the app solves the problem

- A https request is made over a secure network with (first name, last name, zipcode and card number).
- In the backend, the data is decrypted via the handshake.
- `Jasypt` is used to encrypt the card number before its stored in the database. For a production app a KMS (like AWS KMS) will be used instead.
- The the encrypted card number is then stored in the database table.
- When new payments are created, if a webhook is registered, it would get called, and up to 5 retries will be made. If all 5 retries are unsuccessful, the webhook operation terminates gracefully. A warning message is shown in the spring boot logs (Failed to send webhook). 
- If the webhook succeeds, the logs will show a success message as well as which url was used for the webhook.
- There's only one webhook url for this app. Initially its not set. Once you set it, you can change it.
- I didnt have enough time to setup OpenAPI documenation or unit and integration tests unfortunately.

### Setup 
- Clone repository 
- Navigate to repository
- Run the docker containers for this app in terminal for the mysql database:

        docker-compose up 
- Then in a seperate terminal, run the app (this will take a few minutes):
        
        ./gradlew build
        ./gradlew bootRun
- Open Postman and make requests from there.

### API Documentation

#### 1 - Add webhook via API

- Make a https `Post` request to `https://localhost:8080/webhook/set`
- In `headers` set `key` to `Content-Type` and `value` to `application/json`
- In the `body` send a raw json data that specifies the URL for your webhook:

```
{
  "url": "https://webhook.site/..."
}
```

The returned response will be something like: 
```
Webhook URL set successfully
```

To improve this app, I would use a more conventional response, that's in the form of JSON with the data that we need to use.

Note: if you miss this step. The app will still function (allow payments to be created). It just wont call the webhooks. You will get a message printed in your sprint boot logs that says (No webhook URL is set) when you create new payments


#### 2 - Add a payment

- Make a https `Post` request to `https://localhost:8080/payment/add`
- In `headers` set `key` to `Content-Type` and `value` to `application/json`
- In the `body` send a raw json data with the payment details. For example:

```
{
  "firstName": "John",
  "lastName": "Doe",
  "zipCode": "90201",
  "cardNumber": "1111111111111111"
}
```
Note: `cardNumber` must must be 16 digits long. Numbers only.

Response example:
```
Payment saved successfully. ID: 3 
```
To improve this app, I would use a more conventional response, that's in the form of JSON with the data that we need to use (eg: a response like `{status: success, id: 3 }`)

#### 2 - View all payments
- Make a https `get` request to `https://localhost:8080/payment/all`
Response example:
```
[
    {
        "id": 1,
        "firstName": "cee",
        "lastName": "has",
        "zipCode": "12345",
        "cardNumber": "5GcC4MxjhINYodqdnKd2fLRhLFVK44bGkkUMRtzhSccoZ0hmDrxrVKyyLtAcZ472Wi2aD1BBVOfdAVlxHJRg0A=="
    }
]
```
This endpoint is not part of the requirement. I wanted to add for ease of testing.

### Gotchas

Since this is not a production app:

- The app uses a self-signed certificate. Postman will warn you about this. But just disrigard it.
- Request must all be made through `https`. `http` requests will not be redirected for the purpose of this app.
- Everytime you run or restart the app, the database will reset all the data because `hibernate` is used by default in `application.properties` which does the job. For real world apps I would use migrations.
- I didnt have enough time to add OpenAPI documentaion or write integration and unit testing.
- If I had more time, i would abstract the code further, and focus on improving performance.
- Currently, when creating a payment via a POST request, the response is only returned after the webhook either succeeds or reaches 5 failed attempts. Theres a 2 second wait time in between retries. Once 5 failures occur, the webhook stops retrying. In a production environment, a non-blocking approach could be more suitable—returning the payment response immediately while processing the webhook asynchronously. The webhook can then retry up to 5 times independently. While Spring Boot provides libraries for non-blocking operations, this challenge uses a synchronous flow for simplicity. But of course it depends on our usecase and business requirements. Its just a nice observation.`