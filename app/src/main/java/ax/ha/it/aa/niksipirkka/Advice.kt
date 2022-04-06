package ax.ha.it.aa.niksipirkka

class Advice(
    private var author: String,
    private var content: String,
    private var category: String
) {
    fun getAuthor() : String {
        return author
    }
    fun getContent() : String {
        return content
    }
    fun getCategory() : String {
        return category
    }
}