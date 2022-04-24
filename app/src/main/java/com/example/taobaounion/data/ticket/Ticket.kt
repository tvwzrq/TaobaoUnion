package com.example.taobaounion.data

data class Ticket(
    val tbk_tpwd_create_response: TbkTpwdCreateResponse
)
data class TbkTpwdCreateResponse(
    val `data`: Data,
    val request_id: String
)

data class Data(
    val model: String
)