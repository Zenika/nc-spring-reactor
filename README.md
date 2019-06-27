# NightClazz Reactor / Code Lab

Vous avez équipé votre maison d'un capteur de température, et vous souhaitez faire un programme
qui récupère ses valeurs.
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