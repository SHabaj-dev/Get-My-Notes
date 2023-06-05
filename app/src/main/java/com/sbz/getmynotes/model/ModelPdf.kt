package com.sbz.getmynotes.model

data class ModelPdf(
    var uid: String = "",
    var id: String = "",
    var topic: String = "",
    var subjectId: String = "",
    var url: String = "",
    var timestamp: Long = 0,
    var viewCount: Int = 0,
    var downloadsCount: Int = 0
)