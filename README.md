## Exercice 5 :

Nous souhaitons que notre module front affiche en temps réel les nouvelles moyennes de températures persistées.
Pour ce faire vous allez mettre en oeuvre l'API *Processor*.  

1. Ecrivez une classe *TemperatureProcessor* qui exposera des bean de *DirectProcessor<T>* et de *FluxSink<T>*.

    On utilisera l'implémentation *DirectProcessor*, l'API expose deux méthodes permettants sa mise en oeuvre : 
    
    - `DirectProcessor.create()` : Retourne une instance de *DirectProcessor<T>* sur la quelle il est possible d'effectuer une souscription 
    - `DirectProcessor#sink()` : Retourne une instance de *FluxSink<T>* sur la quelle permets de pousser de la donnée

2. Modifier la classe MongoListener afin que cette dernière notifie le *Processor* de chaque docuement persisté

3. Completez la méthode *DataController#getTemperatureEvent()* afin de souscrire au *Processor* 

    > Vous pouvez chaîner l'opérateur `log()` afin d'afficher des logs sur les connexions en cours
    
    > Utilisez la méthode `toServerSentEvent()`

4. Exécutez le front afin de vérifier le bon fonctionnement