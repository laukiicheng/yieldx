package com.yieldx.models

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

// TODO: This is some dummy class
data class Finance(
    val name: String,
    val number: Int
)

@XmlRootElement(name = "Finances")
data class Finances(
    @XmlElement(name = "Finance")
    val finances: List<Finance>
)
