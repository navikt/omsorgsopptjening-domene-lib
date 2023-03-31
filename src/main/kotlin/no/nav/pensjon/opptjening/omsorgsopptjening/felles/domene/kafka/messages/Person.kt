package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

class Person(val fnr: String, var fodselsAr: String? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Person) return false
        if(this.fnr === other.fnr) return true
        return false
    }

    override fun hashCode(): Int {
        return fnr.hashCode()
    }
}

