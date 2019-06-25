## Exercice 2 :

Le but de cette exercice est de migrer notre controller en annotation vers une RouterFunction.

Commencez par lancer les tests d'intégration dans *TemperatureHandlerTest*. 

1. Refactorisez le code pour utiliser les router function. 

2. Créez une classe WebConfig pour définir vos RouterFunction

```java
    @Configuration
    @EnableWebFlux
    public class WebConfig implements WebFluxConfigurer {
        
    }
```

3. Créez des beans pour vos RouterFunction

> https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-fn-router-functions

Vous devez migrer 3 routes :

- @GetMapping(path = "/temperature/last")
- @GetMapping(path = "/temperature/lasts")
- @GetMapping(path = "/temperature-event",produces = MediaType.TEXT_EVENT_STREAM_VALUE)

Commencez par les tester pour vérifier que le projet fonctionne, et lancer les tests d'intégration dans `TemperatureHandlerTest`

```java
    @Bean
    public RouterFunction<?> router() {
        return route()
                .path("/...", builder ->
                        builder.nest(//
                                accept(MediaType.ALL), builder2 -> //
                                        builder2//
                                                .GET("/r1", request -> temperatureHandler.getLastTemperatureData())
                                                .GET("/r2", request -> temperatureHandler...())
                                                .build()
                        ))
                .before(this::logRequest)
                .build();
    }

    @Bean
    public RouterFunction<?> routerSSE() {
        return route()
                .GET("/r3", request -> temperatureHandler.getTemperatureData())
                .build();
    }
```

* Transformez la classe TemperatureHandler pour que vos méthodes retourne des Mono<ServerResponse>

Voici un exemple basique :

```java
    public Mono<ServerResponse> getLastTemperatureData() {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(temperatureService.getLastTemperatureData(), Temperature.class);
    }
```
Cet exemple fonctionne, mais ne gère pas le cas ou notre méthode ne renvoie aucune valeur. Voici un autre exemple :

```java
    public Mono<ServerResponse> demo2() {
        return temperatureService.getLastTemperatureData()
                .flatMap(res -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(res)))
                .switchIfEmpty(notFound().build());
    }
```

* Vous pouvez également gérer le cas où votre publisher vous renvoie un signal d'erreur :

```java
    public Mono<ServerResponse> demoWithError() {
        return temperatureService.getLastTemperatureData()
                .flatMap(res -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(res)))
                .doOnError(res->ServerResponse.status(500).build())
                .switchIfEmpty(notFound().build());
    }
```


* Le test d'intrégation doit toujours fonctionner, néanmoins vous allez devoir rajouter votre classe de configuration dans la configuration de votre test :

```java
     @RunWith(SpringRunner.class)
     @WebFluxTest(TemperatureHandler.class)
     @ContextConfiguration(classes = {
             TemperatureHandler.class,
             TemperatureService.class,
             WebConfig.class
     })
     public class TemperatureHandlerTest {
     }
 ```


4. / Améliorer vos routerFunction

Rajoutez la méthode .before() dans vos router function pour logguer toutes les requêtes entrante


