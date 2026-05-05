package com.thierrylavoie.ludo.domain

data class AvatarItem(
    val id: String,
    val nameEn: String,
    val nameFr: String,
    val category: String, // "HAT", "GLASSES", "SHIRT"
    val price: Int,
    val icon: String // Emoji representation
)

object ShopManager {
    val items = listOf(
        // Base Avatars
        AvatarItem("base_default", "Default", "Défaut", "BASE", 0, "😶"),
        AvatarItem("base_robot", "Robot", "Robot", "BASE", 300, "🤖"),
        AvatarItem("base_alien", "Alien", "Extraterrestre", "BASE", 500, "👽"),
        AvatarItem("base_ghost", "Ghost", "Fantôme", "BASE", 400, "👻"),
        AvatarItem("base_ninja", "Ninja", "Ninja", "BASE", 600, "🥷"),
        AvatarItem("base_monster", "Monster", "Monstre", "BASE", 1500, "👹"),
        AvatarItem("base_alien_green", "Green Alien", "Alien Vert", "BASE", 800, "👽"),
        AvatarItem("base_robot_gold", "Gold Robot", "Robot d'Or", "BASE", 1200, "🤖"),
        AvatarItem("base_clown", "Clown", "Clown", "BASE", 400, "🤡"),
        AvatarItem("base_vampire", "Vampire", "Vampire", "BASE", 1100, "🧛"),
        AvatarItem("base_star", "Superstar", "Vedette", "BASE", 2500, "🌟"),

        // Hats
        AvatarItem("hat_cap", "Cap", "Casquette", "HAT", 50, "🧢"),
        AvatarItem("hat_top", "Top Hat", "Haut-de-forme", "HAT", 200, "🎩"),
        AvatarItem("hat_wizard", "Wizard Hat", "Chapeau de mage", "HAT", 500, "🧙"),
        AvatarItem("hat_crown", "Crown", "Couronne", "HAT", 1000, "👑"),
        
        // Glasses
        AvatarItem("glass_cool", "Sunglasses", "Lunettes de soleil", "GLASSES", 75, "🕶️"),
        AvatarItem("glass_nerd", "Glasses", "Lunettes", "GLASSES", 30, "👓"),
        AvatarItem("glass_goggles", "Goggles", "Masque", "GLASSES", 300, "🥽"),
        
        // Accessories
        AvatarItem("acc_medal", "Medal", "Médaille", "CHEST", 150, "🏅"),
        AvatarItem("acc_tie", "Tie", "Cravate", "CHEST", 100, "👔"),
        AvatarItem("acc_rocket", "Rocket", "Fusée", "COMPANION", 2000, "🚀"),
        AvatarItem("acc_cat", "Cat Friend", "Chat", "COMPANION", 800, "🐱"),
        AvatarItem("acc_dog", "Dog Friend", "Chien", "COMPANION", 800, "🐶"),
        AvatarItem("acc_fire", "Fire Aura", "Aura de feu", "COMPANION", 5000, "🔥")
    )

    fun getItemsByCategory(category: String) = items.filter { it.category == category }
}
