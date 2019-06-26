## Exercice 6 :

Nous avons rajouté un capteur d'humidité dans notre API et nous souhaitons récupérer en même temps la dernière valeur émise de
notre température et de l'humidité. 
Dans le cas où le capteur d'humidité est défaillant, nous souhaitons qu'il renvoie la valeur 0.
Vous aurez besoin de l'opérateur `zipWith` et `onErrorReturn` pour cette exercice.


Lancez les tests dans `DataServiceTest`
# 1 
Implémentez la méthode `public Flux<Tuple2<Temperature, Integer>> getTemperatureAndHumidity()` :
- appeler le flux webClient.getTemperatureEvent()
- Convertir le résultat en Température
- zip le résultat avec le résultat de webClient.getHumidity()

> Vérifiez en lançant le test public void getTemperatureAndHumidity_should_return_both_values()

# 2
Modifiez la méthode pour que dans le cas où une erreur se produit quand on récupère
l'humidité, le flux renvoie la valeur 0

>  Vérifiez en lançant le reste des tests