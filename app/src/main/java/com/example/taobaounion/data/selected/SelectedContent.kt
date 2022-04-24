package com.example.taobaounion.data.selected

data class SelectedContent(
    val tbk_dg_optimus_material_response: TbkDgOptimusMaterialResponse
)
data class TbkDgOptimusMaterialResponse(
    val is_default: String,
    val request_id: String,
    val result_list: ResultList,
    val total_count: Int
)
data class ResultList(
    val map_data: List<SelectedContentItem>
)
data class SmallImages(
    val string: List<String>
)