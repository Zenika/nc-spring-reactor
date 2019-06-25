## Exercice 4 :

Nous souhaitons souscrire aux événements provenant du module API (http://localhost:8081/temperature-event) et effectuer plusieurs opérations :
 * transformer un Flux de temperature en un Flux de liste de températures 
 * calculer la temperature moyenne de cette liste  
 * persister la température moyenne en base de données
 * exposer les moyennes de temperatures stockées en base via un *Controller*  

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

> Uutilisez la classe document *Temperature* à votre disposition. 

3. Dans la classe *DataService* modifier la méthode `temperatureEventFlux()` afin qu'elle effectue les opérations suivantes :

    31. Souscrire aux événements de `webClient.getTemperatureEvent()`
     
    32. Un mapping de type de *Flux<ServerSentEvent<String>>* vers *Temperature*
    > Vous pouvez utiliser la classe *JacksonConverter*
     
    33. Un calcul de moyenne de température pour 10 températures 
    > Vous pouvez utiliser la méthode `computeAverage(List<Float> floatList)`
    
    34. La création d'un document mongo temperature (moyenne de temperature des 10 températures précédentes)
    > Vous pouvez utiliser la méthode `buildDocument(Float aFloat)`
    
    35. La persistence en base de données
    
    36. Terminez d'implémenter la méthode *DataController#getAll()* afin qu'elle retourne tous les documents de la collection temperature
    > Testez dans un navigateur que votre implémentation fonctionne via la route http://localhost:8082/all

5. Terminez d'implémenter la méthode *DataController#getWithLimit()*  afin qu'elle retourne le nombre de document passé en argument.

> Utilisez l'opérateur `handle()` avec un compteur afin de déclencher manuellement le `onComplete()` du flux.

> Testez dans un navigateur que votre implémentation fonctionne via la route http://localhost:8082/limit/x
