package com.onboard.server.domain.file.service

import com.onboard.server.domain.file.controller.dto.FileUploadResponse
import com.onboard.server.thirdparty.storage.ImageService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileService(
    private val imageService: ImageService
) {
    fun upload(file: MultipartFile): FileUploadResponse {
        val url = imageService.upload(file)
        return FileUploadResponse(url)
    }
}
