package com.Instagram_clone.Instagram_clone.AwsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {

    // @Value இன்ஜெக்ஷன்கள் - இவை அப்படியே இருக்கட்டும்
    @Value("${cloud.aws.credentials.access-key}")
    private String ACCESS_KEY;

    @Value("${cloud.aws.credentials.secret-key}")
    private String SECRET_KEY;

    @Value("${cloud.aws.region.static}")
    private String REGION;

    // 👇 1. StaticCredentialsProvider Bean: இதுதான் முக்கியம்!
    @Bean
    public StaticCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider.create(
            AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)
        );
    }

    // 👇 2. S3Client Bean: ஒரே ஒரு முறை, உள்ளீட்டை ஏற்கிறது (Line 25 முதல் 38 வரை உள்ள மற்ற s3client முறைகளை நீக்க வேண்டும்)
    @Bean
    public S3Client s3client(StaticCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .region(Region.of(REGION))
                .credentialsProvider(credentialsProvider) // DI மூலம் பெறப்பட்டது
                .build();
    }

    // 👇 3. S3Presigner Bean: ஒரே ஒரு முறை, உள்ளீட்டை ஏற்கிறது (Line 40 முதல் 51 வரை உள்ள மற்ற s3Presigner முறைகளை நீக்க வேண்டும்)
    @Bean
    public S3Presigner s3Presigner(StaticCredentialsProvider credentialsProvider) {
        return S3Presigner.builder()
                .region(Region.of(REGION))
                .credentialsProvider(credentialsProvider) // DI மூலம் பெறப்பட்டது
                .build();
    }
}