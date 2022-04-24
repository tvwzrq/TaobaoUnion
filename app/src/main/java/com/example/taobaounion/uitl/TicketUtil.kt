package com.example.taobaounion.uitl

import java.util.regex.Matcher
import java.util.regex.Pattern

class TicketUtil {
    companion object{
        fun getTicket(ticketString: String):String{
            val pattern=Pattern.compile("￥\\w+￥")
            val matcher=pattern.matcher(ticketString)
            if (matcher.find()){
                return matcher.group()
            }
            return ticketString
        }


    }
}