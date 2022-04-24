package com.example.taobaounion.data.onsell

data class OnSellContent(
    val tbk_dg_optimus_material_response: TbkDgOptimusMaterialResponse
)

data class TbkDgOptimusMaterialResponse(
    val is_default: String,
    val request_id: String,
    val result_list: ResultList
)

data class ResultList(
    val map_data: List<OnSellContentItem>
)


data class SmallImages(
    val string: List<String>
)