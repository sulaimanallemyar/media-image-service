package com.example.image_resizer.utils;

import io.minio.MinioClient;

public class MinioConnector {

    public static MinioClient connect() {
        MinioClient minioConnect = MinioClient
            .builder()
            .endpoint(Configuration.MINIO_ENDPOINT)
            .credentials(Configuration.MINIO_ACCESS_KEY, Configuration.MINIO_SECRET_KEY)
            .build();

        return minioConnect;
    }
}
