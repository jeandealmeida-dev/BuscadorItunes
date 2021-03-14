package com.jeanpaulo.buscador_itunes.util.params

open class Person(
    var firstName: String = "John",
    var lastName: String = "Smith",
    var age: Int = 28
) {
    val fullname: String
        get() = "$firstName $lastName"

    constructor(year: Int) : this() {
        age += year
    }

    open val id: String

    init {
        //run before any secondary constructor
        id = fullname + age
    }

}

class Employee(company: String) : Person() {
    override val id = fullname + company + age
}

//OTHER WAY

abstract class Person2(
    var firstName: String = "John",
    var lastName: String = "Smith",
    var age: Int = 28
) {
    val fullname: String
        get() = "$firstName $lastName"

    constructor(year: Int) : this() {
        age += year
    }

    abstract val id: String

}

class Employee2(company: String) : Person() , PersonActions{
    override val id = fullname + company + age
    override fun wearClothes() {
        println("Employee wears suits and tie")
    }
}

class Student (school: String) : Person(), PersonActions{
    override val id = fullname + school + age
    override fun wearClothes() {
        println("Students wears uniform")
    }
}

interface PersonActions{
    fun wearClothes()
}