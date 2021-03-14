package com.jeanpaulo.buscador_itunes.util

import com.jeanpaulo.buscador_itunes.util.params.Employee
import com.jeanpaulo.buscador_itunes.util.params.Person

class KotlinClass{

    fun youtubeClass(){
        val name = "Kotlin"
        val arr = arrayOf(1,2,3)
        println("Hello $name y amigos")
        println("resultado =${1+5}")
        val age = Integer.valueOf(readLine().toString())

        val result = if(age >=21) "You are allowed to drink" else "You are not allowed to drink"
        println(result)

        print("What day is today?")
        val day = readLine()
        val food = when(day){
            "Monday", "Wednesday" -> "Chicken"
            "Friday", "Tuesday" -> "Salmon"
            else -> "Bacon"
        }

        val food2 = when{
            day== "Monday" -> "Chicken"
            day == "Friday" -> "Salmon"
            else -> "Bacon"
        }

        print(food)

        val list = listOf<String>("Kotlin", "Java", "Python")
        val map = mapOf(1 to "Kotlin", 2 to "Java", 3 to "Python")

        for ((key, value) in map){
            println("$key => $value")
        }

        for(i in 1..9){
            print(i)
        }

        for(i in 1 until 9 step 2){}

        for(i in 9 downTo 1){}

        fun String.getEmotion() : String{
            return when{
                last() == '!' -> "Exciting"
                last() == '?' -> "Curious"
                last() == '.' -> "Calm"
                else -> "Unidentified"
            }
        }

        val s = "How are you?"
        println(s.getEmotion())

        val pl = Person()
        pl.age += 1
        pl.fullname

        val el = Employee(company = "XYZ")
        println("el id is: ${el.id}")

    }
}