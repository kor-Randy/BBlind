package com.test.moon.bblind

class IdData
{

    public var name : String? = "a"
    public var sex : String? = null
    public var to : String? = null
    public var from : String? = null
    public var date : String? = null
    public var AverageAge : String? = null
    public var AverageDrink : String? = null
    public var Introduction : String? = null



    constructor(name : String, sex : String,from : String, to : String,date : String,averageAge : String,averageDrink : String,introduction : String)
    {
        this.name = name
        this.sex = sex
        this.from = from
        this.to = to
        this.date = date
        this.AverageAge = averageAge
        this.AverageDrink = averageDrink
        this.Introduction = introduction
    }
    constructor()
    {}





}