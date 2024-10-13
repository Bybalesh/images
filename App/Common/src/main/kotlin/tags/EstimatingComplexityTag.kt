package tags

enum class EstimatingComplexityTag(val tag: String, weight: Int) {
    COMPLEXITY_BASIC("#COMPLEXITY/BASIC",5),
    COMPLEXITY_MEDIUM("#COMPLEXITY/MEDIUM",10),
    COMPLEXITY_ADVANCED("#COMPLEXITY/ADVANCED",20),
}