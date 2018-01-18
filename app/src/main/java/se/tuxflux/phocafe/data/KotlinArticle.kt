package se.tuxflux.phocafe.data

import java.util.*

class KotlinArticle {

    var id: String? = null
    var title: String? = null
    var author: String? = null
    var link: String? = null
    var pubDate: Date? = null
    var description: String? = null
    var content: String? = null
    var image: String? = null
    private var categories: MutableList<String>? = null

    fun getCategories(): List<String>? {
        return categories
    }

    fun addCategory(category: String) {
        if (categories == null)
            categories = ArrayList()
        categories!!.add(category)
    }
}