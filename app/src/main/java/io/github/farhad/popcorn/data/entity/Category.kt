package io.github.farhad.popcorn.data.entity

/**
 * used to differentiate [MovieEntity] in database when synchronized with the remote api call
 */
enum class Category(var value: String) {
    UPCOMING("upcoming") {
        override fun toString(): String {
            return "upcoming"
        }
    },
    TRENDING("trending") {
        override fun toString(): String {
            return "trending"
        }
    };

    companion object {
        fun from(findValue: String): Category = Category.values().first { it.value == findValue }
    }
}