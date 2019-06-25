# NightClazz Reactor / Code Lab

Vous avez équipé votre maison d'un capteur de température, et vous souhaitez faire un programme
qui récupère les valeurs. 
Pour le moment nous simulerons le capteur, en émettant simplement un flux.



## Exercice 1 :

1.

> Créer un Mono ou un Flux à partir d'une valeur : https://projectreactor.io/docs/core/release/reference/#_simple_ways_to_create_a_flux_or_mono_and_subscribe_to_it

> Transformer une valeur : https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html#map-java.util.function.Function-

Lancez les tests dans `TemperatureServiceTest`.

- Implémentez la méthode `getLastTemperatureAsFloat` pour qu'elle retourne la température dans un Mono
*Le test `getLastTemperatureAsFloat_should_emit_LAST_TEMPERATURE` devrait réussir.*

- Implémentez la méthode `getLastTemperatureData` pour qu'elle souscrive au Publisher renvoyé par `getLastTemperatureAsFloat`
et transforme le résultat émis en Temperature.
 *Le test `getLastTemperatureData_should_emit_one_value` devrait réussir.*

- Complétez la méthode `getLastTemperatureData` pour qu'elle transforme la température en farenheit. 
*Le test `getLastTemperatureData_should_emit_LAST_TEMPERATURE_in_farenheit()` devrait réussir.*

> Vous aurez besoin de transformer votre température avec la méthode `toFahrenheit`

2.

- Complétez la méthode `getLastTemperatureDatas` pour qu'elle retourne un Flux de TemperatureData
*Le test `getLastTemperatureDatas_should_emit_all_temperatures` devrait réussir.*

> Les valeurs des températures sont dans LAST_TEMPERATURES

3.

- Complétez la méthode `generateTemperatureData` pour qu'elle retourne un flux infini de `TemperatureData`. Cette méthode doit émettre un élément toutes les 100ms. 

> Vous aurez besoin de l'opérateur Flux.interval() pour générer le Flux
> Vous pouvez utiliser la méthode `generateFloat()`



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



## Exercice 3 :

Dans cette exercice nous allons appeler les API précédément créé. Pour cela nous allons
utiliser WebClient ( https://www.baeldung.com/spring-5-webclient ), que nous allons initialiser
puis utiliser.

Le WebClient est initiliasé dans notre constructeur.

Pour cette exercice les tests se feront grâce à la classe. Vous devez préalablement démarré votre module API

1. Mono

Implémentez la méthode `getLastData()` pour appeler votre route 
`/temperature/last` qui renvoie un Mono

2. Flux

Implémentez la méthode `getLastDatas()` pour appeler votre route 
`/temperature/lasts` qui renvoie un Flux

3. ServerSentEvent

Implémentez la méthode `getTemperatureEvent()` pour appeler votre route `/temperature-event`
qui renvoie des ServerSentEvent. Pour ce cas, vous devrez transformer votre Flux de cette manière :
`.bodyToFlux(typeReference);`



## Exercice 4 :

Nous souhaitons souscrire aux événements provenant du module API (http://localhost:8081/temperature-event) et effectuer plusieurs opérations :
 * aggrégation de plusieurs températures en une seule
 * calculer la temperature moyenne de cette liste  
 * persister la température moyenne en base de données

1. Le module Data dispose des dépendances suivantes  

```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
    </dependency>
```

```xml
    <dependency>
        <groupId>de.flapdoodle.embed</groupId>
        <artifactId>de.flapdoodle.embed.mongo</artifactId>
        <version>2.2.0</version>
    </dependency>
```

2. Ajoutez une interface *TemperatureRepository* 
utilisez la classe docuement *Temperature* à votre disposition. 

3. Dans la classe *DataService* modifier la méthode `temperatureEventFlux()` afin qu'elle effectue les opérations suivantes :

    31. Souscrire aux événements en utilisant la méthode  `webClient.getTemperatureEvent()`
     
    32. Un mapping de type de *Flux<ServerSentEvent<String>>* vers *Temperature*
    Vous pouvez utiliser la classe *JacksonConverter*
     
    33. Un calcul de moyenne de température pour 10 températures 
    Vous pouvez utiliser la méthode `computeAverage(List<Float> floatList)`
    
    34. La création d'un document mongo 
    Vous pouvez utiliser la méthode `buildDocument(Float aFloat)`
    
    35. La persistence en base de données

4. Terminez d'implémenter la méthode *DataController#getAll()* afin qu'elle retourne tous les documents de la collection temperature

5. Testez dans un navigateur que votre implémentation fonctionne via la route http://localhost:8082/all



## Exercice 5 :

Nous souhaitons que notre module front affiche en temps réel les nouvelles moyennes de températures calculées et persistées.
Pour ce faire vous allez mettre en oeuvre l'API *Processor*.  

1. Ecrivez une classe *TemperatureProcessor* qui exposera des bean de *DirectProcessor<T>* et de *FluxSink<T>*.

    On utilisera l'implémentation *DirectProcessor*, l'API expose deux méthodes permettants sa mise en oeuvre : 
    
    - `DirectProcessor.create()` : Retourne une instance de *DirectProcessor<T>* sur la quelle il est possible d'effectuer une souscription 
    - `DirectProcessor#sink()` : Retourne une instance de *FluxSink<T>* sur la quelle permets de pousser de la donnée

2. Modifier la classe MongoListener afin que cette dernière notifie le *Processor* de chaque docuement persisté

3. Completez la méthode *DataController#getTemperatureEvent()* afin de souscrire au *Processor* 

    - Vous pouvez chaîner l'opérateur `log()` afin d'afficher des logs sur les connexions en cours

4. Exécutez le front afin de vérifier le bon fonctionnement