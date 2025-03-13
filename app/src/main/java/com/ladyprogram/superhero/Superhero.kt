package com.ladyprogram.superhero


class SuperheroResponse (
    val response: String,
    val results: List<Superhero>
) {

}

class Superhero (
    val id: String,
    val name: String

) {
}