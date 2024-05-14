package com.onboard.server.thirdparty.storage

import org.springframework.web.multipart.MultipartFile

interface ImageService {
    fun upload(file: MultipartFile): String
}
