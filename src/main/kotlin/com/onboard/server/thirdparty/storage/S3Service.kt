package com.onboard.server.thirdparty.storage

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Component
class S3Service(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
) : ImageService {
    override fun upload(file: MultipartFile): String {
        val objectMetadata = ObjectMetadata().apply {
            contentType = file.contentType
            contentLength = file.size
        }

        val fileName = "${UUID.randomUUID()}-${file.originalFilename}"

        try {
            val request = PutObjectRequest(bucket, fileName, file.inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead)

            amazonS3.putObject(request)
        } catch (e: Exception) {
            throw RuntimeException("이미지 업로드 실패")
        }

        return amazonS3.getUrl(bucket, fileName).toString()
    }
}
