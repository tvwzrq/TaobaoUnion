package com.example.taobaounion.data.search

data class SearchContent(
val tbk_dg_material_optional_response: TbkDgMaterialOptionalResponse
)

data class TbkDgMaterialOptionalResponse(
    val request_id: String,
    val result_list: ResultList,
    val total_results: Int
)

data class ResultList(
    val map_data: List<SearchContentItem>
)

data class SmallImages(
    val string: List<String>
)

