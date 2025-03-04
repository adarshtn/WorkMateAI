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
            PDFBoxResourceLoader.init(context) // Required for Android
            val document = PDDocument.load(FileInputStream(pdfFile))
            val text = PDFTextStripper().getText(document)
            document.close()
            text
        } catch (e: Exception) {
            Log.e("PDFUtils", "Error extracting text from PDF: ${e.message}")
            null
        }
    }
}
