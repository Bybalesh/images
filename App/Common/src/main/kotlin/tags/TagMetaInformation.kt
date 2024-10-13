package tags

data class TagMetaInformation<E>(val tagEnum: E, val description: CharSequence, val optional: Boolean) where E : Enum<E>