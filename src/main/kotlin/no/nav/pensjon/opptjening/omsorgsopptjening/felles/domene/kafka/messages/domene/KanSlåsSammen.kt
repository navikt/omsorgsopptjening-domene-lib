package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

interface KanSlåsSammen<T> {
    fun kanSlåsSammen(other: T): Boolean
    fun slåSammen(other: T): T
    fun sorteringVedSammenslåing(): Comparator<T>

    companion object {
        inline fun <reified T : KanSlåsSammen<T>> List<T>.slåSammenLike(): List<T> {
            return if (isNotEmpty()) {
                sortedWith(this.first().sorteringVedSammenslåing()).let {
                    it.fold(emptyList()) { acc, other ->
                        if (acc.isNotEmpty()) {
                            val siste = acc.last()
                            if (siste.kanSlåsSammen(other)) {
                                acc - siste + siste.slåSammen(other)
                            } else {
                                acc + other
                            }
                        } else {
                            acc + other
                        }
                    }
                }
            } else {
                emptyList()
            }
        }
    }
}