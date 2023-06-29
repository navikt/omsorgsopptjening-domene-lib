package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka

object Topics {
    const val BARNETRYGDMOTTAKER= "barnetrygdmottaker"

    object Omsorgsopptjening {
        const val NAME = "omsorgsopptjening"
        data class Key(val ident: String)
    }

}