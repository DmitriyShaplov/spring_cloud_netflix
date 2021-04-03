# spring_cloud_netflix
Проект для ознакомления с экосистемой микросервисов Spring Cloud Netflix

Имеется:  
**_eureka-service_** - (EurekaService) в качестве Registry - ведет учет сервисов
остальные подключаются посредствам EurekaClient;
**_gallery-service_** - реактивное приложение на Spring5 (WebFlux), общается
с БД - MongoBD.
**_user-service_** - общается с gallery-service средствами FeignClient,
RestTemplate, WebClient. В случае выхода из строя БД или gallery-service
используется библиотека Hystrix.