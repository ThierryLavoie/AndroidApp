package com.thierrylavoie.ludo.domain

import kotlin.random.Random

data class ReadingTask(
    val text: String,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int
)

class ReadingGameEngine(
    private val random: Random = Random.Default
) {
    private val tasks = listOf(
        ReadingTask(
            "Le frère d'Annabelle mange beaucoup. Il demande une deuxième portion à chaque repas. Il est un glouton!",
            "Qu'est-ce que demande le frère d'Annabelle à chaque repas ?",
            listOf("Un dessert", "Une deuxième portion", "Du lait", "Une fourchette"),
            1
        ),
        ReadingTask(
            "Julie adore les fleurs. Dans son jardin, elle a planté des roses rouges et des tournesols jaunes. Elle les arrose tous les matins.",
            "De quelle couleur sont les roses dans le jardin de Julie ?",
            listOf("Jaunes", "Bleues", "Rouges", "Blanches"),
            2
        ),
        ReadingTask(
            "Le petit chat noir de Thomas est monté sur le toit. Il a peur de descendre. Thomas appelle les pompiers pour l'aider.",
            "Pourquoi Thomas appelle-t-il les pompiers ?",
            listOf("Pour éteindre un feu", "Pour son chat noir", "Pour son chien", "Pour réparer le toit"),
            1
        ),
        ReadingTask(
            "L'hiver est arrivé. La neige tombe doucement sur la forêt. Les ours dorment profondément dans leurs grottes.",
            "Où dorment les ours pendant l'hiver ?",
            listOf("Dans les arbres", "Dans les maisons", "Dans leurs grottes", "Dans la neige"),
            2
        ),
        ReadingTask(
            "Marie prépare un gâteau au chocolat pour l'anniversaire de son père. Elle mélange de la farine, des œufs et beaucoup de cacao.",
            "Pour qui Marie prépare-t-elle un gâteau ?",
            listOf("Pour son frère", "Pour son père", "Pour son ami", "Pour son chat"),
            1
        ),
        ReadingTask(
            "Paul va à la bibliothèque tous les samedis. Il aime lire des bandes dessinées et des livres d'aventure. Aujourd'hui, il a emprunté trois livres.",
            "Quand Paul va-t-il à la bibliothèque ?",
            listOf("Le lundi", "Le mercredi", "Le samedi", "Le dimanche"),
            2
        ),
        ReadingTask(
            "Sophie a un petit chien qui s'appelle Filou. Filou est très joueur et il aime courir après les balles dans le parc. Sophie lui donne une friandise quand il revient.",
            "Comment s'appelle le chien de Sophie ?",
            listOf("Médor", "Filou", "Rex", "Ballon"),
            1
        ),
        ReadingTask(
            "Le soleil brille très fort aujourd'hui. Il fait très chaud dehors. Lucas met son chapeau et sa crème solaire avant d'aller jouer au football.",
            "À quel sport Lucas va-t-il jouer ?",
            listOf("Au tennis", "Au basket", "Au football", "À la pétanque"),
            2
        ),
        ReadingTask(
            "Dans la classe de Madame Lefebvre, il y a vingt élèves. Aujourd'hui, ils apprennent les noms des planètes. Marc a posé une question sur Mars.",
            "Sur quelle planète Marc a-t-il posé une question ?",
            listOf("La Terre", "Vénus", "Mars", "Jupiter"),
            2
        ),
        ReadingTask(
            "Maman a acheté des pommes, des bananes et des oranges au marché. Elle veut faire une salade de fruits pour le dessert de ce soir.",
            "Qu'est-ce que maman veut préparer ?",
            listOf("Une tarte", "Une salade de fruits", "Un gâteau", "Une compote"),
            1
        ),
        ReadingTask(
            "Le train pour Paris part à huit heures du matin. Lucie est déjà sur le quai avec sa valise rouge. Elle est très impatiente de voir la Tour Eiffel.",
            "De quelle couleur est la valise de Lucie ?",
            listOf("Bleue", "Verte", "Rouge", "Noire"),
            2
        ),
        ReadingTask(
            "Les lapins mangent souvent des carottes et de la laitue. Ils ont de longues oreilles et ils peuvent sauter très haut. Ils vivent dans des terriers.",
            "Que mangent souvent les lapins ?",
            listOf("Du fromage", "Des carottes", "Du pain", "Des insectes"),
            1
        ),
        ReadingTask(
            "Papa répare son vélo dans le garage. Il a besoin d'une clé et d'un tournevis. La chaîne était cassée, mais maintenant tout fonctionne.",
            "Où papa répare-t-il son vélo ?",
            listOf("Dans le jardin", "Dans la cuisine", "Dans le garage", "Dans la rue"),
            2
        ),
        ReadingTask(
            "Le boulanger prépare du pain frais tous les matins. Ça sent très bon dans toute la rue. Les gens font la queue pour acheter des croissants.",
            "Qu'est-ce que les gens achètent en faisant la queue ?",
            listOf("Des baguettes", "Des gâteaux", "Des bonbons", "Des croissants"),
            3
        ),
        ReadingTask(
            "Il pleut beaucoup aujourd'hui. Léa a pris son parapluie bleu et ses bottes de pluie jaunes. Elle aime sauter dans les flaques d'eau.",
            "De quelle couleur sont les bottes de Léa ?",
            listOf("Bleues", "Jaunes", "Vertes", "Rouges"),
            1
        )
    )

    fun getAllTasks(): List<ReadingTask> = tasks

    fun checkAnswer(task: ReadingTask, selectedIndex: Int): Boolean {
        return task.correctOptionIndex == selectedIndex
    }
}
