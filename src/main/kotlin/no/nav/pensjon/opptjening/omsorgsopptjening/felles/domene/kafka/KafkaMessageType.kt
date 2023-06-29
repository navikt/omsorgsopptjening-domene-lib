package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka

enum class KafkaMessageType {
    BARNETRYGD,
    OMSORGSGRUNNLAG,
    OPPTJENING;
    companion object {
        const val name = "MESSAGE_TYPE"
    }
}

