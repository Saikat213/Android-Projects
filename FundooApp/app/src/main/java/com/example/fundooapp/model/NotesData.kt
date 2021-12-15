package com.example.fundooapp.model

import java.io.Serializable

data class NotesData(var Title : String ?= null, var Content : String ?= null, var ID : String ?= null,
                        var Archive : Boolean ?= false) : Serializable
