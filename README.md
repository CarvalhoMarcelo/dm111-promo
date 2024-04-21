# DM111-PROMO

**Used Operational System:**

- *Windows 11*
- *WSL2 - Kali Linux 2023.3*

---

**Used Technologies:**

- *IntelliJ IDEA Community Edition 2023.2.2*
- *Open JDK 64 bit 17.0.10*
- *Postman 10.17.04*
- *Google Cloud SDK 472.0.0*
- *Apache Maven 3.9.5*

---

Payload:

In the folder `docs` there the files `DM111-Promo.postman_collection.json` and `LocalEnvironment.postman_environment.json` 
that you can use to import to your preferred API platform or tool (I've used Postman for this project), so you will have 
all the endpoints ready and configured to use and test the APIs.

Endpoints:

If you decide to not import the Postman configured collection that I have exported, below are the endpoints to be used 
to test the API.

```
--- PROMO ---
(GET)    http://localhost:8081/dm111/promo                    (Fetch all promo - does not require token)
(GET)    http://localhost:8081/dm111/promo/{{promoId}}        {Fetch promo by Id - does not reqire token)
(GET)    http://localhost:8081/dm111/promo/users/{{userId}}   (Fetch relevant promo by userId - requires Client token)
(POST)   http://localhost:8081/dm111/promo                    (Create promo - requires Admin token)
(UPDATE) http://localhost:8081/dm111/promo/{{promoId}}        (Update promo - requires Admin token)
(DELETE) http://localhost:8081/dm111/promo/{{promoId}}        (Delete promo - requires Admin token)       

--- AUTHENTICATION ---
(GET) http://localhost:8081/dm111/promo/auth                  (Fetch Admin or Client token to use as authentication)
```

> Payload for Promo (This Promo entity is also stored in the Firebase):
```json
{
    "id": "uuid",
    "name": "Promotion name",
    "starting": "date formatted as dd/MM/yyyy",
    "expiration": "date formatted as dd/MM/yyyy",
    "products": [
        {
        "productId": "product id",
        "discount": 99 
        }
    ]
}
```

> Payload for Admin or Client Authentication (User need to be created in advanced using the DM111 project, as describe in the section "Project") 
```json
{
  "email": "admin@gmail.com",
  "password": "abc123"
}
```

**Attention:** `User`, `Product` and `SuperMarketList` must be created in advanced using the DM111 project, as describe 
in the section `"Project"`


## Project

The project was split in two repositories, the [current one](https://github.com/carvalhomarcelo/dm111-promo) 
(a.k.a DM111-PROMO) and the [DM111](https://github.com/edilsonjustiniano/dm111).
The current project will listen some notifications published by DM111 and apply some business logic based on the 
published/consumed events.

## Overview

This repository contains the solution implemented by the DM111 discipline from the Pos Graduation course Mobile and
Cloud Development by Inatel.

This project implements a web service to deal with a minimal structure of PROMOTIONS, i.e. The _CRUD_
operations for **Promo**.

To make these exposed APIs secure it was implemented an **Authentication** API as well using JWT token.

In addition to the auth API an _Interceptor_ was created and all the endpoints that required the authorization was
added behind this interceptor. It means, only authenticated users could access the resources and in some scenarios only
authenticated and authorized users were able to perform some operations.

## Integrations

The current project does have the integrations with: _Firebase DB_, _Firebase Cloud Message_, _Pub Sub_.

### Firebase

In order to reduce the cost and also reduce the risk of reach the Free tier limit of U$ 300 provided by the Google Cloud
I have decided to use Firebase as our main database solution. That decision helped us to reduce the number of resources
required to be deployed and simplify our deployment letting us move our focus on the code itself.

#### Remember
_You have to generate the service accounts from your Firebase project and paste into the right place, i.e.the folder_
_src/main/resources_

### Firebase Cloud Message

It was also implemented the Publisher to the **FCM** (_Firebase Cloud Message_) and whenever a user was created a message
is published into the FCM topic.

### Pub Sub

One more integration added to the application was the **Pub Sub** that is composed by the topic and their subscriptions.
Whenever a supermarket list has been created, updated or either deleted an event is published through this topic and the
consumer services will be aware of the change. So, the consumer applications can apply their specific logics.

## How to set up

In order to prepare the machine to either run or change the project follow the instructions detailed at
[DM111 setup instructions](DM111-setup.md)

## How to deploy to GAE

To deploy the code to **GAE** (_Google App Engine_), it is necessary first initialize the **gcloud** environment, 
please run the following command:

```
gcloud init
```

During the execution of this command, it will be necessary to authenticate at the **Google Cloud** account. After the
initialization has been completed the deployment will be straightforward you only need to execute the following command.

```
gcloud app deploy --version 1 dm111/app.yaml
```

Some data/confirmation will be requested via terminal at the end of this step and as result of the deploy the link to 
access the service via API will be there.

### Deploy multiple services at once

To deploy both services simultaneously, please run the following command:

```
gcloud app deploy --version 1 dm111/app.yaml dm111-promo/app.yaml
```
