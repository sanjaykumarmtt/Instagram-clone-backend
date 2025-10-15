package com.Instagram_clone.Instagram_clone.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Instagram_clone.Instagram_clone.Entity.Post;
import com.Instagram_clone.Instagram_clone.Entity.post_DTP;
import com.Instagram_clone.Instagram_clone.Entity.story;
import com.Instagram_clone.Instagram_clone.ExceptionHan.HostelException;
import com.Instagram_clone.Instagram_clone.servise.AwsServise;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api")
public class Inst_Controller {

	private final AwsServise s3Service;

	private static final String BASE_S3_URL = "https://s3.amazonaws.com/";

	public Inst_Controller(AwsServise s3Service) {
		this.s3Service = s3Service;
	}

	@PostMapping("/Post_data_file")
	public ResponseEntity<Post> uploadFile(@RequestPart("poste") MultipartFile prof,
			@RequestPart("postdata") post_DTP dto) throws HostelException {
		try {
			return ResponseEntity.ok(s3Service.faileupdate(dto, prof));

		} catch (Exception e) {
			System.out.println("❌ சர்வர் பிழை: " + e.getMessage());
			throw new HostelException("❌ சர்வர் பிழை: " + e.getMessage());
		}
	}

	@GetMapping("/Tocken-Chane")
	public ResponseEntity<Boolean> TockenChane() {

		return ResponseEntity.ok(true);
	}

	@GetMapping("/Get_All_post_data")
	public ResponseEntity<List<Post>> GetUpdatae() throws HostelException {
		try {
			return ResponseEntity.ok(s3Service.get_All_postdata());
		} catch (Exception e) {
			throw new HostelException("❌ சர்வர் பிழை: " + e.getMessage());
		}
	}

	@PostMapping("/Set_Story_data/{username}")
	public ResponseEntity<String> FileUpdatae( @PathVariable("username") String username,@RequestPart("storyFile") MultipartFile storyFile)
			throws IOException, HostelException {
		
//		System.out.println(storyFile.getOriginalFilename());
//		System.out.println(username);
		s3Service.Story_ser(username, storyFile);
		return ResponseEntity.ok("ok");

	}

	@GetMapping("/Get_All_Story_data")
	public ResponseEntity<List<story>> Get_Story_ser() {
		return ResponseEntity.ok(s3Service.Get_Story_ser());
	}

	@GetMapping("/Get_one_Story_data/{id}")
	public ResponseEntity<story> Get_All_Story_onedata(@PathVariable int id) throws HostelException, IOException {
		return ResponseEntity.ok(s3Service.Get_Story_Id(id));
	}

	@GetMapping("/stream-oneimage")
	public ResponseEntity<String> streamImageToFrontend(@RequestParam String key) {

		try {
			// இது Pre-Signed URL பட்டியலைத் திருப்பி அனுப்பும்
			String imageUrls = s3Service.oneUrlForImage(key);
			return new ResponseEntity<>(imageUrls, HttpStatus.OK);

		} catch (Exception e) {
			System.err.println("Error streaming image: " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/images/list")
	public ResponseEntity<List<String>> getAllImages() {

		try {
			List<String> imageUrls = s3Service.listAllImagesInBucket();
			return new ResponseEntity<>(imageUrls, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/s3/delete/{objectKey}")
	public String deleteObject(@PathVariable String objectKey) {
		try {
			String decodedKey = java.net.URLDecoder.decode(objectKey, "UTF-8");

			boolean deleted = s3Service.deleteFileFromS3(decodedKey);

			if (deleted) {
				return "Successfully deleted object: " + decodedKey;
			} else {
				return "Failed to delete object: " + decodedKey + ". Check server logs for details.";
			}
		} catch (Exception e) {
			return "Error processing request: " + e.getMessage();
		}
	}
}