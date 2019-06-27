## Exercice 2 :

Le but de cet exercice est de migrer notre controller en annotation vers une RouterFunction.

Commencez par lancer les tests d'intégration dans *TemperatureHandlerTest*.

1. Refactorisez le code pour utiliser les router functions.

1.1. Créez une classe WebConfig pour définir vos RouterFunction

```java
    @Configuration
    @EnableWebFlux
    public class WebConfig implements WebFluxConfigurer {
        
    }
```

1.2. Créez des beans pour vos RouterFunction

> https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-fn-router-functions

Vous devez migrer 3 routes :

- @GetMapping(path = "/temperature/last")
- @GetMapping(path = "/temperature/lasts")
- @GetMapping(path = "/temperature-event",produces = MediaType.TEXT_EVENT_STREAM_VALUE)

Commencez par les tester pour vérifier que le projet fonctionne, et lancez les tests d'intégration dans `TemperatureHandlerTest`

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

2. Transformez la classe TemperatureHandler pour que vos méthodes retournent des `Mono<ServerResponse>`.

Voici un exemple basique :

```java
    public Mono<ServerResponse> getLastTemperatureData() {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(temperatureService.getLastTemperatureData(), Temperature.class);
    }
```
Cet exemple fonctionne, mais ne gère pas le cas où notre méthode ne renvoie aucune valeur. Voici un autre exemple :

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


* Le test d'intégration doit toujours fonctionner, néanmoins vous allez devoir ajouter votre classe de configuration dans la configuration de votre test :

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


3. Améliorez vos routerFunction

Ajoutez la méthode `.before()` dans vos router functions pour logguer toutes les requêtes entrantes.


