package com.example.fundooapp.model

import java.io.Serializable

data class NotesData(var Title : String ?= null, var Content : String ?= null, var Archive : String ?= null,
                     var ID : String ?= null) : Serializable
