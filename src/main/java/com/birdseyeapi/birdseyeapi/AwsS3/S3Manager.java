package com.birdseyeapi.birdseyeapi.AwsS3;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.utils.IoUtils;

@Component
public class S3Manager {
    private final S3Client S3;

    public S3Manager() {
        S3 = S3Client.builder()
            .region(Region.AP_NORTHEAST_1)
            .build();
    }

    public List<S3Object> listObjects(String bucketName, String prefix) {
        ListObjectsRequest listObjects = ListObjectsRequest
            .builder()
            .bucket(bucketName)
            .prefix(prefix)
            .build();

        ListObjectsResponse res = S3.listObjects(listObjects);
        List<S3Object> objects = res.contents();
        return objects;
    }

    public String getJsonObject(String bucketName, String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest
            .builder()
            .bucket(bucketName)
            .key(key)
            .build();

        ResponseInputStream<GetObjectResponse> stream = S3.getObject(getObjectRequest);
        String json = IoUtils.toUtf8String(stream);
        return json;
    }
}
