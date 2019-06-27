## Exercice 3 :

Dans cet exercice nous allons appeler les API précédemment créées. Pour cela nous allons
utiliser WebClient ( https://www.baeldung.com/spring-5-webclient ), que nous allons initialiser
puis utiliser.

Le WebClient est initialisé dans notre constructeur.

Pour cet exercice les tests se feront grâce à la classe. Vous devez préalablement démarrer votre module API

1. Mono

Implémentez la méthode `getLastData()` pour appeler votre route
`/temperature/last` qui renvoie un Mono

2. Flux

Implémentez la méthode `getLastDatas()` pour appeler votre route 
`/temperature/lasts` qui renvoie un Flux

3. ServerSentEvent

Implémentez la méthode `getTemperatureEvent()` pour appeler votre route `/temperature-event`
qui renvoie des `ServerSentEvent`. Pour ce cas, vous devrez transformer votre Flux de cette manière :
`.bodyToFlux(typeReference);`