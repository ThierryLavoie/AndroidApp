package com.thierrylavoie.ludo.domain

import kotlin.random.Random

class ReadingGameEngineEnglish(
    private val random: Random = Random.Default
) {
    private val tasks = listOf(
        ReadingTask(
            "The Great Pyramid of Giza was built for the Pharaoh Khufu. It is one of the Seven Wonders of the Ancient World and took about 20 years to build.",
            "Who was the Great Pyramid of Giza built for?",
            listOf("Pharaoh Tutankhamun", "Pharaoh Khufu", "Pharaoh Ramses", "Pharaoh Akhenaten"),
            1
        ),
        ReadingTask(
            "A smartphone is a portable device that combines a mobile phone and a computer. It allows users to browse the internet, take photos, and run various apps.",
            "What can you do with a smartphone according to the text?",
            listOf("Fly a plane", "Cook a meal", "Browse the internet", "Drive a car"),
            2
        ),
        ReadingTask(
            "Penguins are flightless birds that live mostly in the Southern Hemisphere. They are excellent swimmers and spend about half of their lives on land and half in the ocean.",
            "Where do penguins spend about half of their lives?",
            listOf("In the air", "In trees", "In the ocean", "In the desert"),
            2
        ),
        ReadingTask(
            "Basketball was invented in 1891 by Dr. James Naismith. He wanted to create a game that could be played indoors during the cold winter months.",
            "In what year was basketball invented?",
            listOf("1881", "1891", "1901", "1911"),
            1
        ),
        ReadingTask(
            "The Grand Canyon is a massive canyon in Arizona, carved by the Colorado River over millions of years. It is known for its size and its colorful landscape.",
            "Which river carved the Grand Canyon?",
            listOf("Mississippi River", "Nile River", "Colorado River", "Amazon River"),
            2
        ),
        ReadingTask(
            "Photosynthesis is the process by which green plants use sunlight to make food from carbon dioxide and water. Oxygen is released as a byproduct.",
            "What do plants release during photosynthesis?",
            listOf("Carbon dioxide", "Nitrogen", "Oxygen", "Helium"),
            2
        ),
        ReadingTask(
            "The Apollo 11 mission was the first to land humans on the Moon. Neil Armstrong became the first person to walk on the lunar surface in 1969, followed by Buzz Aldrin.",
            "Who was the first person to walk on the Moon?",
            listOf("Buzz Aldrin", "Neil Armstrong", "Yuri Gagarin", "John Glenn"),
            1
        ),
        ReadingTask(
            "The piano is a musical instrument played using a keyboard. It has 88 keys and is widely used in many styles of music, from classical to jazz.",
            "How many keys does a standard piano have?",
            listOf("66", "77", "88", "99"),
            2
        ),
        ReadingTask(
            "The Amazon Rainforest is the largest tropical rainforest in the world. It is home to millions of species of plants and animals and is often called the 'lungs of the Earth'.",
            "What is the Amazon Rainforest often called?",
            listOf("The heart of the Earth", "The lungs of the Earth", "The eyes of the Earth", "The skin of the Earth"),
            1
        ),
        ReadingTask(
            "Honeybees play a vital role in our ecosystem by pollinating plants. They live in colonies called hives and communicate with each other through a 'waggle dance'.",
            "How do honeybees communicate with each other?",
            listOf("By singing", "By dancing", "By biting", "By jumping"),
            1
        )
    )

    fun getAllTasks(): List<ReadingTask> = tasks

    fun checkAnswer(task: ReadingTask, selectedIndex: Int): Boolean {
        return task.correctOptionIndex == selectedIndex
    }
}
