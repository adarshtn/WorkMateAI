package com.example.workmateai.utils

import android.util.Log

object SkillEnhancer {
    // Comprehensive embeddings: skill -> list of (related skill, similarity score)
    private val embeddings = mapOf(
        // Programming Languages
        "java" to listOf("spring" to 0.85, "hibernate" to 0.80, "jdbc" to 0.75, "javafx" to 0.70, "android" to 0.65, "junit" to 0.60),
        "kotlin" to listOf("android" to 0.90, "java" to 0.70, "coroutines" to 0.65, "spring" to 0.60),
        "c" to listOf("c++" to 0.95, "embedded" to 0.80, "linux" to 0.75, "python" to 0.60, "assembly" to 0.55),
        "c++" to listOf("c" to 0.95, "qt" to 0.75, "boost" to 0.70, "stl" to 0.65, "embedded" to 0.60),
        "c#" to listOf(".net" to 0.90, "asp.net" to 0.85, "unity" to 0.70, "sql" to 0.65, "xamarin" to 0.60),
        "python" to listOf("django" to 0.80, "flask" to 0.75, "numpy" to 0.70, "pandas" to 0.65, "tensorflow" to 0.60, "pytorch" to 0.55),
        "javascript" to listOf("react" to 0.85, "node.js" to 0.80, "typescript" to 0.75, "html" to 0.70, "css" to 0.65, "angular" to 0.60),
        "typescript" to listOf("javascript" to 0.75, "react" to 0.70, "node.js" to 0.65, "angular" to 0.60),
        "go" to listOf("docker" to 0.75, "kubernetes" to 0.70, "microservices" to 0.65),
        "rust" to listOf("systems" to 0.80, "concurrency" to 0.75, "c++" to 0.60),
        "ruby" to listOf("rails" to 0.85, "sql" to 0.65),
        "php" to listOf("laravel" to 0.80, "mysql" to 0.75, "wordpress" to 0.70),

        // Web Development
        "html" to listOf("css" to 0.90, "javascript" to 0.85, "react" to 0.70, "bootstrap" to 0.65, "xml" to 0.60),
        "css" to listOf("html" to 0.90, "sass" to 0.75, "bootstrap" to 0.70, "javascript" to 0.65, "tailwind" to 0.60),

        // Frameworks & Libraries
        "spring" to listOf("java" to 0.85, "hibernate" to 0.75, "spring boot" to 0.90, "rest" to 0.70),
        "hibernate" to listOf("java" to 0.80, "spring" to 0.75, "jdbc" to 0.70, "sql" to 0.65),
        "django" to listOf("python" to 0.80, "flask" to 0.70, "sql" to 0.65, "rest" to 0.60),
        "flask" to listOf("python" to 0.75, "django" to 0.70, "sql" to 0.60, "rest" to 0.55),
        "react" to listOf("javascript" to 0.85, "typescript" to 0.75, "html" to 0.70, "css" to 0.65, "redux" to 0.60),
        ".net" to listOf("c#" to 0.90, "asp.net" to 0.85, "sql" to 0.70, "azure" to 0.65),
        "node.js" to listOf("javascript" to 0.80, "express" to 0.75, "mongodb" to 0.70),
        "rails" to listOf("ruby" to 0.85, "sql" to 0.65),
        "laravel" to listOf("php" to 0.80, "mysql" to 0.75),

        // Tools & Platforms
        "android" to listOf("kotlin" to 0.90, "java" to 0.65, "android studio" to 0.85, "firebase" to 0.70),
        "javafx" to listOf("java" to 0.70, "ui" to 0.65),
        "mysql" to listOf("sql" to 0.90, "database" to 0.85, "php" to 0.75, "python" to 0.70, "jdbc" to 0.65),
        "firebase" to listOf("android" to 0.75, "javascript" to 0.70, "database" to 0.65, "cloud" to 0.60),
        "intellij" to listOf("java" to 0.75, "kotlin" to 0.70, "android" to 0.65, "python" to 0.60),
        "vs code" to listOf("javascript" to 0.75, "python" to 0.70, "html" to 0.65, "css" to 0.60, "typescript" to 0.55),
        "google cloud" to listOf("cloud" to 0.90, "python" to 0.65, "java" to 0.60, "kubernetes" to 0.55),
        "aws" to listOf("cloud" to 0.90, "python" to 0.70, "java" to 0.65, "docker" to 0.60),
        "azure" to listOf("cloud" to 0.85, ".net" to 0.75, "c#" to 0.70),
        "docker" to listOf("kubernetes" to 0.80, "go" to 0.75, "devops" to 0.70),
        "kubernetes" to listOf("docker" to 0.80, "cloud" to 0.75, "devops" to 0.70),
        "git" to listOf("github" to 0.85, "version control" to 0.80),

        // Databases
        "sql" to listOf("mysql" to 0.90, "postgresql" to 0.85, "database" to 0.80, "sqlite" to 0.75),
        "mongodb" to listOf("node.js" to 0.70, "database" to 0.65, "javascript" to 0.60),
        "postgresql" to listOf("sql" to 0.85, "database" to 0.80, "python" to 0.65),

        // AI/ML
        "tensorflow" to listOf("python" to 0.80, "machine learning" to 0.75, "pytorch" to 0.70),
        "pytorch" to listOf("python" to 0.75, "machine learning" to 0.70, "tensorflow" to 0.65),
        "machine learning" to listOf("python" to 0.80, "tensorflow" to 0.75, "pytorch" to 0.70, "numpy" to 0.65),
        "deep learning" to listOf("tensorflow" to 0.80, "pytorch" to 0.75, "python" to 0.70),

        // Algorithms & Data Structures
        "algorithms" to listOf("data structures" to 0.90, "python" to 0.65, "c++" to 0.60),
        "data structures" to listOf("algorithms" to 0.90, "c++" to 0.65, "java" to 0.60),

        // Cybersecurity
        "cybersecurity" to listOf("networking" to 0.75, "python" to 0.70, "linux" to 0.65),
        "networking" to listOf("cybersecurity" to 0.75, "tcp/ip" to 0.70, "linux" to 0.65),

        // Miscellaneous
        "xml" to listOf("java" to 0.65, "android" to 0.60, "html" to 0.55),
        "tkinter" to listOf("python" to 0.70, "ui" to 0.65),
        "studio" to listOf("android" to 0.85, "java" to 0.60), // Assuming "Studio" means Android Studio
        "code" to listOf("vs code" to 0.80, "javascript" to 0.65), // Assuming "code" relates to VS Code
        "platform" to listOf("cloud" to 0.70, "android" to 0.65),
        "linux" to listOf("c" to 0.75, "bash" to 0.70, "networking" to 0.65),
        "embedded" to listOf("c" to 0.80, "c++" to 0.65, "microcontrollers" to 0.70)
    )

    fun enhanceSkills(skills: List<String>, threshold: Float = 0.7f): List<String> {
        val enhancedSkills = mutableSetOf<String>()
        skills.forEach { skill ->
            val lowerSkill = skill.lowercase()
            enhancedSkills.add(lowerSkill) // Always include the original skill
            embeddings[lowerSkill]?.forEach { (relatedSkill, similarity) ->
                if (similarity >= threshold) {
                    enhancedSkills.add(relatedSkill)
                }
            }
        }
        Log.d("SkillEnhancer", "Original skills: $skills, Enhanced skills: $enhancedSkills")
        return enhancedSkills.toList()
    }
}