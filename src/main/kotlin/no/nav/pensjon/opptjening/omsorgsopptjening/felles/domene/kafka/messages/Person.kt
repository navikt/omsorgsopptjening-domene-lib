package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

class Person(val fnr: String, var fodselsAr: String? = null) {
    override fun equals(other: Any?) = this === other || (other is Person && this.fnr == other.fnr)
    override fun hashCode() = fnr.hashCode()
}

