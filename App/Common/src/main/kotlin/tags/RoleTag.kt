package tags

enum class RoleTag(val tag: String) : IMDTag<RoleTag> {
    ROLE_DEV("#ROLE/DEV"),
    ROLE_DEV_BACKEND("#ROLE/DEV/BACKEND"),
    ROLE_DEV_FRONTEND("#ROLE/DEV/FRONTEND"),
    ROLE_DEV_IOS("#ROLE/DEV/IOS"),
    ROLE_DEV_ANDROID("#ROLE/DEV/ANDROID"),
    ROLE_DEV_OPS("#ROLE/DEV/OPS"),
    ROLE_TESTER("#ROLE/TESTER"),
    ROLE_TESTER_MANUAL("#ROLE/TESTER/MANUAL"),
    ROLE_TESTER_AUTOMATION("#ROLE/TESTER/AUTOMATION"),
    ROLE_ANALYST("#ROLE/ANALYST"),
    ROLE_ANALYST_SYSTEM("#ROLE/ANALYST/SYSTEM"),
    ROLE_ANALYST_BUSINESS("#ROLE/ANALYST/BUSINESS"),
    ROLE_ENGINEER("#ROLE/ENGINEER"),
    ROLE_ENGINEER_DATA("#ROLE/ENGINEER/DATA"),
    ROLE_ENGINEER_PROMT("#ROLE/ENGINEER/PROMT"),
    ROLE_ALL("#ROLE/ALL");

    override fun tagsName(): String {
        return "Роли в команде"
    }

    override fun isOptional(): Boolean {
        return true
    }

    override fun tags(): List<RoleTag> {
        return entries
    }

    override fun tagOf(tag: String): RoleTag? {
        return tags().find { it.tag == tag }
    }

//    override fun isTag(tag: String): Boolean {
//        return tags().any(tag::equals)
//    }

    override fun tagsAsStr(): List<String> {
        return tags().map { it.tag }
    }

    companion object {
        fun tagOf(tag: String): RoleTag? {
            return entries.first().tagOf(tag)
        }
    }
}