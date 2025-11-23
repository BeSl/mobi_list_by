package com.example.myapplication.usecase

data class ParsedItem(
    val name: String,
    val quantity: Int? = null
)

class ParseTextUseCase {
    fun parse(text: String): List<ParsedItem> {
        if (text.isBlank()) return emptyList()
        
        // Разделяем по запятым
        val items = text.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        
        return items.map { item ->
            // Пытаемся извлечь количество (цифру) из начала или конца строки
            val quantityRegex = Regex("(\\d+)\\s*(.+)|(.+)\\s*(\\d+)")
            val match = quantityRegex.find(item)
            
            if (match != null) {
                // Найдено количество и название
                val (qtyStart, nameStart, nameEnd, qtyEnd) = match.destructured
                if (qtyStart.isNotEmpty() && nameStart.isNotEmpty()) {
                    // Формат: "3 яблока"
                    ParsedItem(nameStart.trim(), qtyStart.toIntOrNull())
                } else if (nameEnd.isNotEmpty() && qtyEnd.isNotEmpty()) {
                    // Формат: "яблоки 5"
                    ParsedItem(nameEnd.trim(), qtyEnd.toIntOrNull())
                } else {
                    ParsedItem(item)
                }
            } else {
                // Нет количества, только название
                ParsedItem(item)
            }
        }
    }
}