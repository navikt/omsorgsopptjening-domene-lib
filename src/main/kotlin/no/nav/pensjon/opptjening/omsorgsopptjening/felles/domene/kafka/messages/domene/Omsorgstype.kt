package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

enum class Omsorgstype {
    DELT_BARNETRYGD,
    FULL_BARNETRYGD,
    USIKKER_BARNETRYGD, //ikke nok data til å avgjøre om det er delt eller full, kan være tilfellet på gamle saker fra Infotrygd
    HJELPESTØNAD_FORHØYET_SATS_3,
    HJELPESTØNAD_FORHØYET_SATS_4;
}