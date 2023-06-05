package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

class PersonMedFødselsår(
    val fnr: String,
    val fodselsAr: Int
) {
    override fun equals(other: Any?) = this === other || (other is PersonMedFødselsår && this.fnr == other.fnr)
    override fun hashCode() = fnr.hashCode()

    fun alder(aarstall: Int): Int {
        return aarstall - fodselsAr
    }
}

