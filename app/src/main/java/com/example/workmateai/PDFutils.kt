package com.example.workmateai.utils

import android.content.Context
import android.util.Log
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.File
import java.io.FileInputStream

object PDFUtils {
    fun extractTextFromPDF(context: Context, pdfFile: File): String? {
        return try {
            PDFBoxResourceLoader.init(context)
            val document = PDDocument.load(FileInputStream(pdfFile))
            val text = PDFTextStripper().getText(document)
            document.close()
            text
        } catch (e: Exception) {
            Log.e("PDFUtils", "Error extracting text from PDF: ${e.message}")
            null
        }
    }

    fun extractSkillsFromPDFText(pdfText: String): List<String> {
        val skills = mutableListOf<String>()
        val lines = pdfText.lines()
        var inSkillsSection = false

        for (line in lines) {
            val trimmedLine = line.trim()
            if (trimmedLine.equals("SKILLS", ignoreCase = true)) {
                inSkillsSection = true
                continue
            }
            if (inSkillsSection) {
                if (trimmedLine.matches(Regex("^[A-Z]+\\s*$")) && trimmedLine != "SKILLS") {
                    break
                }
                if (trimmedLine.contains(":")) {
                    val skillsPart = trimmedLine.split(":").getOrNull(1)?.trim() ?: continue
                    val skillItems = skillsPart.split(",")
                        .flatMap { it.split(" ") } // Split by spaces too
                        .map { it.trim() }
                        .map { it.replace("•", "") } // Remove bullet points
                        .filter { it.isNotEmpty() }
                    skills.addAll(skillItems)
                }
            }
        }
        Log.d("PDFUtils", "Extracted skills from Skills section: $skills")
        return skills
    }
}