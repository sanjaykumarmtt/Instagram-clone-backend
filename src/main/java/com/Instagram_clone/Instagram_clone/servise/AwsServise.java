package com.Instagram_clone.Instagram_clone.servise;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Instagram_clone.Instagram_clone.Entity.Post;
import com.Instagram_clone.Instagram_clone.Entity.User;
import com.Instagram_clone.Instagram_clone.Entity.post_DTP;
import com.Instagram_clone.Instagram_clone.Entity.story;
import com.Instagram_clone.Instagram_clone.ExceptionHan.HostelException;
import com.Instagram_clone.Instagram_clone.JwtLogines.Entity.LoginEntity;
import com.Instagram_clone.Instagram_clone.JwtLogines.Repositroy.SecurityRepositry;
import com.Instagram_clone.Instagram_clone.Repository.InstRepository;
import com.Instagram_clone.Instagram_clone.Repository.Srory_Repository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class AwsServise {

	@Value("${cloud.aws.s3.bucket.name}")
	private String bucketName;

	private S3Client s3clinent;
	private final S3Presigner s3Presigner;
	private final InstRepository InstRepository;
	private final Srory_Repository storyrepo;
	private final SecurityRepositry SecurityRepositry;

	public AwsServise(S3Client s3Client, S3Presigner s3Presigner, InstRepository InstRepository,
			Srory_Repository storyrepo,SecurityRepositry SecurityRepositry) {
		this.s3clinent = s3Client;
		this.s3Presigner = s3Presigner;
		this.InstRepository = InstRepository;
		this.storyrepo = storyrepo;
		this.SecurityRepositry=SecurityRepositry;
	}

	public List<Post> get_All_postdata() throws IOException, HostelException {
		List<Post> post = InstRepository.findAll();
		List<Post> Re_post = new ArrayList<>();
		Post pos = new Post();
		if (post != null) {
			for (Post po : post) {
				String ProfilePic = oneUrlForImage(po.getUser().getProfilePic());
				String Image = oneUrlForImage(po.getImage());
				pos = po;
				pos.getUser().setProfilePic(ProfilePic);
				pos.setImage(Image);
				Re_post.add(pos);
			}
			return Re_post;
		}
		throw new HostelException("No date of this type some datas");
	}

	public Post faileupdate(post_DTP dto,MultipartFile profa) throws IOException, HostelException {
		LoginEntity lent=SecurityRepositry.getByusername(dto.getUsername());
		String poste = AwsS3fail_stor(profa);
		if (poste != null && lent != null) {
			Post po=new Post();
			User us = new User();
			us.setUsername(lent.getUsername());
			us.setProfilePic(lent.getPeofaile());
			po.setUser(us);	
			
			po.setImage(poste);
			po.setCaption(dto.getCaption());
			po.setTimestamp(dto.getTimestamp());
			return InstRepository.save(po);
		} else {
			deleteFileFromS3(poste);
			throw new HostelException("Image not uploaded please check the your image");
		}
	}
	public void Putposte(post_DTP dto,MultipartFile Image) {
		
		
	}

	public String AwsS3fail_stor(MultipartFile faile) throws IOException, HostelException {

		String faileurl = System.currentTimeMillis() + "_" + faile.getOriginalFilename();

		PutObjectResponse respon = s3clinent.putObject(PutObjectRequest.builder().bucket(bucketName).key(faileurl)
				.contentLength(faile.getSize()).contentType(faile.getContentType()).build(),
				RequestBody.fromBytes(faile.getBytes()));
		if (respon.eTag() != null) {
			return faileurl;
		}
		throw new HostelException("Image not uploaded please check the your image");
	}

	public story Story_ser(String username, MultipartFile Image)throws IOException, HostelException {
		
		LoginEntity lent=SecurityRepositry.getByusername(username);
		
		String Storeyurl= AwsS3fail_stor(Image);
	
		if (lent != null && lent != null) {
			story stoe=new story();
			stoe.setUsername(username);
			stoe.setFullname(lent.getFuallname());
			stoe.setStory_image(Storeyurl);
			return storyrepo.save(stoe);
		} else {
			deleteFileFromS3(Storeyurl);
			throw new HostelException("Image not uploaded please check the your image");
		}
	}
	public List<story> Get_Story_ser(){
		 
		List<story> sro=storyrepo.findAll();
		List<story> story=new ArrayList<>();
		story st=new story();
		for(story s:sro) {
			String sto_im=oneUrlForImage(s.getStory_image());
			st=s;
			st.setStory_image(sto_im);
			story.add(st);
		}
		return story;
	}
	
	public story Get_Story_Id(int id)throws IOException, HostelException{
		 
		Optional<story> sro=storyrepo.findById(id);
		
		story st=sro.orElseThrow(()->new HostelException("Srory not found eith is= "+id));
		String sto_image=oneUrlForImage(st.getStory_image());
		st.setStory_image(sto_image);
		
		return st;
	}


	public String oneUrlForImage(String objectKey) {

		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().getObjectRequest(getObjectRequest)
				.signatureDuration(Duration.ofMinutes(60)).build();

		try {
			PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);

			System.out.println(presignedGetObjectRequest.url().toString());
			
			return presignedGetObjectRequest.url().toString();

		} catch (Exception e) {
			System.err.println("Error generating presigned URL for " + objectKey + ": " + e.getMessage());
			return "URL generation failed: " + e.getMessage();
		}
	}

	public List<String> listAllImagesInBucket() {
		ListObjectsV2Request listRequest = ListObjectsV2Request.builder().bucket(bucketName).build();

		try {
			ListObjectsV2Response response = s3clinent.listObjectsV2(listRequest);

			List<String> presignedUrls = response.contents().stream().map(S3Object::key).filter(this::isImageFile)
					.map(key -> generatePresignedUrl(bucketName, key)) // <--- Pre-Signed URL உருவாக்குதல்
					.collect(Collectors.toList());

			return presignedUrls;

		} catch (Exception e) {
			System.err.println("Error listing and presigning objects in bucket " + bucketName + ": " + e.getMessage());
			throw new RuntimeException("Unable to list and presign images from S3", e);
		}
	}

	private String generatePresignedUrl(String bucketName, String key) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();

		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
				.signatureDuration(Duration.ofMinutes(60)) 
				.getObjectRequest(getObjectRequest).build();
		System.out.println(s3Presigner.presignGetObject(presignRequest).url().toString() + " hello");

		return s3Presigner.presignGetObject(presignRequest).url().toString();
	}

	private boolean isImageFile(String key) {

		if (key == null || key.isEmpty()) {
			return false;
		}
		String lowerCaseKey = key.toLowerCase();

		return lowerCaseKey.endsWith(".jpg") || lowerCaseKey.endsWith(".jpeg") || lowerCaseKey.endsWith(".png");
	}

	public boolean deleteFileFromS3(String objectKey) {
		DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(bucketName).key(objectKey).build(); // எந்த
		
		try {
			s3clinent.deleteObject(deleteRequest);

			System.out.println("Object " + objectKey + " deleted successfully from bucket " + bucketName);
			return true;

		} catch (S3Exception e) {
			System.err.println("S3 Delete Error: " + e.getMessage());
			return false;
		} catch (Exception e) {
			System.err.println("General Delete Error: " + e.getMessage());
			return false;
		}
	}
}
