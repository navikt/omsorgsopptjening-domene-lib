package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

data class Person(val fnr: String, var fodselsAr: String? = null)