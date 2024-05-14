package com.onboard.server.thirdparty.storage

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config(
    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKey: String,
    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretKey: String,
) {
    @Bean
    fun amazonS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(
                AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey))
            )
            .withRegion(Regions.AP_NORTHEAST_2)
            .build()
    }
}
