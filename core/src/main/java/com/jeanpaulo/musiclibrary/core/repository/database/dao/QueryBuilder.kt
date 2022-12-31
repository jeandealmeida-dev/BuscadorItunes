package com.jeanpaulo.musiclibrary.core.repository.database.dao

class QueryBuilder {

    //private lateinit var query

//    fun select(table: String) : QueryBuilder{
//        query = SELECT.format(table)
//    }


    companion object {
        const val SELECT = "SELECT %s"
        const val FROM = "FROM %s"
        const val WHERE = "WHERE %s"
    }


}