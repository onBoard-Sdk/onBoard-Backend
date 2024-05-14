package com.onboard.server.domain.file.controller

import com.onboard.server.domain.file.controller.dto.FileUploadResponse
import com.onboard.server.domain.file.service.FileService
import com.onboard.server.global.common.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/api/v1/files")
@RestController
class FileController(
    private val fileService: FileService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun upload(@RequestPart file: MultipartFile): ApiResponse<FileUploadResponse> =
        ApiResponse.create(fileService.upload(file))
}
