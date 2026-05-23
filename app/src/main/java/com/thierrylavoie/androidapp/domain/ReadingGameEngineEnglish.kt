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
        ),
        ReadingTask(
            "The Great Wall of China is the longest structure ever built by humans. It stretches across northern China and was built to protect the country from invasions.",
            "Why was the Great Wall of China built?",
            listOf("To attract tourists", "To store food", "To protect the country", "To create a road"),
            2
        ),
        ReadingTask(
            "Mars is often called the 'Red Planet' because of the iron oxide on its surface, which gives it a reddish appearance. It is the fourth planet from the Sun.",
            "Why does Mars look red?",
            listOf("Because it's hot", "Because of iron oxide", "Because of red plants", "Because of its atmosphere"),
            1
        ),
        ReadingTask(
            "The cheetah is the fastest land animal in the world. It can reach speeds of up to 70 miles per hour in short bursts covering distances up to 1,500 feet.",
            "Which animal is the fastest on land?",
            listOf("Lion", "Leopard", "Cheetah", "Tiger"),
            2
        ),
        ReadingTask(
            "Alexander Graham Bell is credited with inventing the first practical telephone in 1876. His first words on the telephone were to his assistant, Mr. Watson.",
            "What did Alexander Graham Bell invent?",
            listOf("The lightbulb", "The telephone", "The radio", "The airplane"),
            1
        ),
        ReadingTask(
            "Mount Everest is the highest mountain in the world above sea level. It is located in the Himalayas on the border between Nepal and China.",
            "In which mountain range is Mount Everest located?",
            listOf("The Andes", "The Alps", "The Himalayas", "The Rockies"),
            2
        ),
        ReadingTask(
            "Sharks are a group of fish known for their cartilaginous skeletons and multiple rows of sharp teeth. They have been in the oceans for over 400 million years.",
            "How long have sharks been in the oceans?",
            listOf("100 million years", "200 million years", "300 million years", "400 million years"),
            3
        ),
        ReadingTask(
            "The Olympic Games are a major international sporting event held every four years. They feature thousands of athletes from around the world competing in various sports.",
            "How often are the Olympic Games held?",
            listOf("Every year", "Every two years", "Every four years", "Every five years"),
            2
        ),
        ReadingTask(
            "Dolphins are highly intelligent marine mammals. They use echolocation to find food and communicate with each other using a variety of clicks and whistles.",
            "What do dolphins use to find food?",
            listOf("Their eyes only", "Echolocation", "Their sense of smell", "Electricity"),
            1
        ),
        ReadingTask(
            "The Eiffel Tower was completed in 1889 for the World's Fair in Paris. It was originally intended to be a temporary structure but became a global cultural icon.",
            "In which city is the Eiffel Tower located?",
            listOf("London", "New York", "Paris", "Berlin"),
            2
        ),
        ReadingTask(
            "The Sahara is the largest hot desert in the world. It covers much of North Africa and is known for its sand dunes and very high temperatures during the day.",
            "Which part of Africa does the Sahara cover?",
            listOf("South Africa", "East Africa", "West Africa", "North Africa"),
            3
        )
    )

    fun getAllTasks(): List<ReadingTask> = tasks

    fun checkAnswer(task: ReadingTask, selectedIndex: Int): Boolean {
        return task.correctOptionIndex == selectedIndex
    }
}
