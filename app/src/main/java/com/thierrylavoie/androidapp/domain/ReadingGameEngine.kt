package com.thierrylavoie.ludo.domain

import kotlin.random.Random

import java.io.Serializable

data class ReadingTask(
    val text: String,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int
) : Serializable

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
        ),
        ReadingTask(
            "Le lion est souvent appelé le roi de la jungle. Il a une grande crinière et il rugit très fort pour protéger son territoire.",
            "Comment appelle-t-on souvent le lion ?",
            listOf("Le roi de la jungle", "Le chat géant", "Le prince des bois", "Le chef du désert"),
            0
        ),
        ReadingTask(
            "Chaque matin, Hugo mange des céréales avec du lait. Il ajoute parfois des morceaux de banane pour avoir plus d'énergie.",
            "Que mange Hugo chaque matin ?",
            listOf("Des œufs", "Des céréales", "Du pain grillé", "Une pomme"),
            1
        ),
        ReadingTask(
            "L'astronaute Thomas Pesquet a passé plusieurs mois dans l'espace. Il a pris de magnifiques photos de la Terre depuis la station spatiale.",
            "Qu'a fait Thomas Pesquet depuis l'espace ?",
            listOf("Il a dormi tout le temps", "Il a réparé la Lune", "Il a pris des photos de la Terre", "Il a mangé des pizzas"),
            2
        ),
        ReadingTask(
            "Ma petite sœur dessine un arc-en-ciel avec ses feutres. Elle utilise du rouge, de l'orange, du jaune, du vert, du bleu et du violet.",
            "Qu'est-ce que la petite sœur dessine ?",
            listOf("Une maison", "Un chat", "Un arc-en-ciel", "Un soleil"),
            2
        ),
        ReadingTask(
            "Les baleines sont les plus grands animaux du monde. Elles vivent dans l'océan et elles respirent par un évent situé sur leur tête.",
            "Où vivent les baleines ?",
            listOf("Dans les rivières", "Dans l'océan", "Sur la plage", "Dans les lacs"),
            1
        ),
        ReadingTask(
            "Pour faire une bonne pizza, il faut de la pâte, de la sauce tomate et beaucoup de fromage. On peut aussi ajouter des champignons.",
            "Quel ingrédient est nécessaire pour la sauce de la pizza ?",
            listOf("Du chocolat", "De la tomate", "De la crème", "Du beurre"),
            1
        ),
        ReadingTask(
            "Le vélo de Nathan est bleu avec une petite sonnette argentée. Il fait toujours attention de mettre son casque avant de partir.",
            "Quelle est la couleur du vélo de Nathan ?",
            listOf("Vert", "Rouge", "Bleu", "Jaune"),
            2
        ),
        ReadingTask(
            "Les abeilles fabriquent du miel dans leur ruche. Elles butinent les fleurs pour récolter le nectar. Le miel est sucré et délicieux.",
            "Que fabriquent les abeilles ?",
            listOf("Du sucre", "Du jus", "Du miel", "Du lait"),
            2
        ),
        ReadingTask(
            "Le Père Noël habite au Pôle Nord. Il prépare des cadeaux pour tous les enfants sages avec l'aide de ses lutins et de ses rennes.",
            "Où habite le Père Noël ?",
            listOf("Au Pôle Sud", "À Paris", "Au Pôle Nord", "Dans la forêt"),
            2
        ),
        ReadingTask(
            "Le pirate Barbe-Noire cherche un trésor caché sur une île déserte. Il utilise une vieille carte et une boussole pour se diriger.",
            "Qu'utilise Barbe-Noire pour se diriger ?",
            listOf("Un GPS", "Un télescope", "Une carte et une boussole", "Les étoiles"),
            2
        ),
        ReadingTask(
            "Les fourmis sont de petits insectes très travailleurs. Elles vivent en colonie et elles peuvent porter des objets beaucoup plus lourds qu'elles.",
            "Comment vivent les fourmis ?",
            listOf("Seules", "En colonie", "Dans l'eau", "Dans les arbres"),
            1
        ),
        ReadingTask(
            "Emma va à la piscine le mercredi après-midi. Elle apprend à nager la brasse et le crawl. Elle porte un maillot de bain rose.",
            "Quand Emma va-t-elle à la piscine ?",
            listOf("Le samedi", "Le lundi", "Le mercredi", "Le vendredi"),
            2
        ),
        ReadingTask(
            "Le kangourou vit en Australie. Il transporte son bébé dans une poche sur son ventre. Il peut faire de très grands bonds.",
            "Où le kangourou transporte-t-il son bébé ?",
            listOf("Sur son dos", "Dans une poche", "Dans ses bras", "Dans un sac"),
            1
        ),
        ReadingTask(
            "Pour Halloween, Lucas s'est déguisé en fantôme. Il a mis un grand drap blanc avec deux trous pour les yeux.",
            "En quoi Lucas s'est-il déguisé ?",
            listOf("En pirate", "En dinosaure", "En fantôme", "En magicien"),
            2
        ),
        ReadingTask(
            "Le jardinier plante des graines dans la terre. Il espère que de belles tomates pousseront cet été. Il doit enlever les mauvaises herbes.",
            "Que plante le jardinier ?",
            listOf("Des cailloux", "Des graines", "Des fleurs", "Des arbres"),
            1
        )
    )

    fun getAllTasks(): List<ReadingTask> = tasks

    fun checkAnswer(task: ReadingTask, selectedIndex: Int): Boolean {
        return task.correctOptionIndex == selectedIndex
    }
}
