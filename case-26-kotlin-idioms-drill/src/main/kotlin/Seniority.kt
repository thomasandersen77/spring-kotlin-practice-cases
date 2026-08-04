enum class Seniority {

    JUNIOR,
    ERFAREN,
    SENIOR,
    VETERAN;

    companion object {
        fun from(yearsOfExperience: Int): Seniority {
            require(yearsOfExperience >= 0) {
                "Erfaring kan ikke være en negativ"
            }

            return when(yearsOfExperience) {
                in 0..2 -> JUNIOR
                in 3..7 -> ERFAREN
                in 8..14 -> SENIOR
                else -> VETERAN
            }
        }
    }
}