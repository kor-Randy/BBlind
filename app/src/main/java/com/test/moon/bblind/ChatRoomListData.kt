package com.test.moon.bblind

class ChatRoomListData {
    var Subway: String? = null
    var PersonNum: String? = null
    var MeetDate: String? = null
    var ChatRoomNum: String? = null
    var LastMsg : String? = null

    var ManMsg : String? = "1"

    var WomanMsg : String? = "1"

    constructor(subway : String, personnum : String, meetdate : String,chatroomnum : String,lastmsg : String)
    {

        this.Subway = subway
        this.PersonNum = personnum
        this.MeetDate = meetdate
        this.ChatRoomNum = chatroomnum
        this.LastMsg = lastmsg
        this.ManMsg = "1"
        this.WomanMsg="1"

    }
    constructor()
    {}

}
