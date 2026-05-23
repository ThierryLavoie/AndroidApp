package com.thierrylavoie.ludo.domain

data class AvatarItem(
    val id: String,
    val nameEn: String,
    val nameFr: String,
    val category: String, // "BASE", "HAT", "GLASSES", "CHEST", "COMPANION"
    val price: Int,
    val icon: String // Emoji representation
)

object ShopManager {
    val items = listOf(
        // Base Avatars (Animaux "nus" sans accessoires intégrés)
        AvatarItem("base_default", "Neutral", "Neutre", "BASE", 0, "😶"),
        AvatarItem("base_cat", "Cat", "Chat", "BASE", 300, "🐱"),
        AvatarItem("base_dog", "Dog", "Chien", "BASE", 300, "🐶"),
        AvatarItem("base_rabbit", "Rabbit", "Lapin", "BASE", 400, "🐰"),
        AvatarItem("base_fox", "Fox", "Renard", "BASE", 600, "🦊"),
        AvatarItem("base_bear", "Bear", "Ours", "BASE", 500, "🐻"),
        AvatarItem("base_panda", "Panda", "Panda", "BASE", 800, "🐼"),
        AvatarItem("base_lion", "Lion", "Lion", "BASE", 1200, "🦁"),
        AvatarItem("base_tiger", "Tiger", "Tigre", "BASE", 1100, "🐯"),
        AvatarItem("base_frog", "Frog", "Grenouille", "BASE", 200, "🐸"),
        AvatarItem("base_monkey", "Monkey", "Singe", "BASE", 450, "🐵"),
        AvatarItem("base_chick", "Chick", "Poussin", "BASE", 250, "🐥"),
        AvatarItem("base_penguin", "Penguin", "Manchot", "BASE", 700, "🐧"),

        // Hats (Chapeaux)
        AvatarItem("hat_cap", "Cap", "Casquette", "HAT", 50, "🧢"),
        AvatarItem("hat_top", "Top Hat", "Haut-de-forme", "HAT", 200, "🎩"),
        AvatarItem("hat_wizard", "Wizard Hat", "Chapeau de mage", "HAT", 500, "🧙"),
        AvatarItem("hat_crown", "Crown", "Couronne", "HAT", 1000, "👑"),
        AvatarItem("hat_pirate", "Pirate Hat", "Chapeau pirate", "HAT", 450, "🏴‍☠️"),
        AvatarItem("hat_chef", "Chef Hat", "Toque de chef", "HAT", 300, "👨‍🍳"),
        AvatarItem("hat_santa", "Santa Hat", "Chapeau de Noël", "HAT", 100, "🎅"),
        AvatarItem("hat_helm", "Knight Helmet", "Casque de chevalier", "HAT", 1200, "🪖"),
        
        // Glasses (Lunettes)
        AvatarItem("glass_cool", "Sunglasses", "Lunettes de soleil", "GLASSES", 75, "🕶️"),
        AvatarItem("glass_nerd", "Glasses", "Lunettes", "GLASSES", 30, "👓"),
        AvatarItem("glass_goggles", "Goggles", "Masque", "GLASSES", 300, "🥽"),
        AvatarItem("glass_monocle", "Monocle", "Monocle", "GLASSES", 800, "🧐"),
        AvatarItem("glass_mask", "Hero Mask", "Masque de héros", "GLASSES", 600, "🎭"),
        
        // Chest Accessories (Torse/Médailles)
        AvatarItem("acc_medal", "Medal", "Médaille", "CHEST", 150, "🏅"),
        AvatarItem("acc_tie", "Tie", "Cravate", "CHEST", 100, "👔"),
        AvatarItem("acc_scarf", "Scarf", "Écharpe", "CHEST", 120, "🧣"),
        AvatarItem("acc_backpack", "Backpack", "Sac à dos", "CHEST", 250, "🎒"),
        AvatarItem("acc_cape", "Hero Cape", "Cape", "CHEST", 1500, "🚩"),
        
        // Companions (Petits animaux et effets)
        AvatarItem("acc_rocket", "Rocket", "Fusée", "COMPANION", 2000, "🚀"),
        AvatarItem("acc_fire", "Fire Aura", "Aura de feu", "COMPANION", 5000, "🔥"),
        AvatarItem("acc_dragon", "Baby Dragon", "Bébé dragon", "COMPANION", 10000, "🐲"),
        AvatarItem("acc_robot_mini", "Mini Bot", "Mini Robot", "COMPANION", 4500, "🤖"),
        AvatarItem("acc_unicorn", "Unicorn", "Licorne", "COMPANION", 7500, "🦄"),
        AvatarItem("acc_owl", "Wise Owl", "Chouette", "COMPANION", 3500, "🦉"),
        AvatarItem("acc_ghost_mini", "Tiny Ghost", "Petit fantôme", "COMPANION", 1500, "👻"),
        AvatarItem("acc_bee", "Friendly Bee", "Abeille", "COMPANION", 900, "🐝")
    )

    fun getItemsByCategory(category: String) = items.filter { it.category == category }
}
