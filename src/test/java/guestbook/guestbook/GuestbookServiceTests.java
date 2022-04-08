package guestbook.guestbook;

import guestbook.guestbook.auths.Account;
import guestbook.guestbook.auths.AccountRepository;
import guestbook.guestbook.domains.GuestbookPost;
import guestbook.guestbook.repositories.GuestbookPostRepository;
import guestbook.guestbook.services.GuestBookServiceImpl;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class GuestbookServiceTests {

	@Autowired
	GuestBookServiceImpl guestBookService;

	@Autowired
	private GuestbookPostRepository guestbookPostRepository;

	@Autowired
	private AccountRepository accountRepository;

	private final String accountName = "JoeTester";
	private final String accountPassword = "pwd";
	private final String text = "You may say I'm a dreamer but I'm not the only one I hope someday you'll join us and the world will be as one (Imagine Beatles)";
	private final String mpName = "multipart";
	private final String mpOrigFileName = "myfile.jpg";
	private final String mpContentType = "image/jpeg";
	private final byte[] mpContent = "Test Data".getBytes();

	public void setup(){
		System.out.println("XXX> setup()");
		Account account = createAccount();
		accountRepository.save(account);
	}

	public void cleanTables(){
		System.out.println("XXX> cleanPosts() 1 " + guestbookPostRepository.findAll().size());
		accountRepository.deleteAll();
		guestbookPostRepository.deleteAll();
		System.out.println("XXX> cleanPosts() 2 " + guestbookPostRepository.findAll().size());
	}

	@Test
	void createGuestbookPost() throws IOException {
		cleanTables();
		setup();
		MockMultipartFile multipartFile = createMockMultipartFile();
		GuestbookPost result = guestBookService.createGuestbookPost(multipartFile, text, accountName);
		Assertions.assertArrayEquals(Base64.encodeBase64(multipartFile.getBytes()), result.getImageContent());
		Assertions.assertEquals(text,result.getPostText());
		Assertions.assertEquals(mpOrigFileName,result.getPostImageLabel());
		Assertions.assertEquals(9L,result.getSize());
		Assertions.assertEquals(false,result.getApprovedFlag());
	}

	@Test
	void getAllGuestbookPost() throws IOException {
		cleanTables();
		setup();
		guestBookService.createGuestbookPost(createMockMultipartFile(), text, accountName);

		List<GuestbookPost> results = guestBookService.getAllGuestbookPost();
		Assertions.assertEquals(1, results.size());
	}

	@Test
	void getAllApprovedGuestbookPost() {
		cleanTables();
		setup();
		GuestbookPost post1 = createGBPostWithAccount(accountRepository.findAllByName(accountName));
		GuestbookPost post2 = createGBPostWithAccount(accountRepository.findAllByName(accountName));
		post1.setApprovedFlag(true);
		accountRepository.findAllByName(accountName);
		guestbookPostRepository.save(post1);
		guestbookPostRepository.save(post2);
		List<GuestbookPost> results = guestBookService.getAllApprovedGuestbookPost();
		Assertions.assertEquals(1, results.size());
	}

	@Test
	void getAllNotApprovedGuestbookPost() {
		cleanTables();
		setup();
		GuestbookPost post1 = createGBPostWithAccount(accountRepository.findAllByName(accountName));
		GuestbookPost post2 = createGBPostWithAccount(accountRepository.findAllByName(accountName));
		post1.setApprovedFlag(true);
		accountRepository.findAllByName(accountName);
		guestbookPostRepository.save(post1);
		guestbookPostRepository.save(post2);
		List<GuestbookPost> results = guestBookService.getAllNotApprovedGuestbookPost();
		Assertions.assertEquals(1, results.size());
	}

	@Test
	void findGuestbookPostById() {
		cleanTables();
		setup();
		GuestbookPost post1 = createGBPostWithAccount(accountRepository.findAllByName(accountName));
		GuestbookPost savedPost = guestbookPostRepository.save(post1);
		Long postId = savedPost.getId();
		Optional<GuestbookPost> optResult = guestBookService.findGuestbookPostById(postId);
		Assertions.assertTrue(optResult.isPresent());
		GuestbookPost result = optResult.get();
		Assertions.assertEquals(postId, result.getId());
	}

	@Test
	void deleteGuestbookPost() {
		cleanTables();
		setup();
		GuestbookPost post1 = createGBPostWithAccount(accountRepository.findAllByName(accountName));
		guestbookPostRepository.save(post1);
		List<GuestbookPost> results = guestbookPostRepository.findAll();
		Assertions.assertEquals(1, results.size());
		guestBookService.deleteGuestbookPost(results.get(0));
		results = guestbookPostRepository.findAll();
		Assertions.assertEquals(0, results.size());
	}

	@Test
	void deleteGuestbookPostById() throws Exception {
		cleanTables();
		setup();
		GuestbookPost post1 = createGBPostWithAccount(accountRepository.findAllByName(accountName));
		guestbookPostRepository.save(post1);
		List<GuestbookPost> results = guestbookPostRepository.findAll();
		Assertions.assertEquals(1, results.size());
		Long resultId = results.get(0).getId();
		guestBookService.deleteGuestbookPostById(resultId);
		results = guestbookPostRepository.findAll();
		Assertions.assertEquals(0, results.size());
	}

	@Test
	void approveGuestbookPost() throws Exception {
		cleanTables();
		setup();
		GuestbookPost post1 = createGBPostWithAccount(accountRepository.findAllByName(accountName));
		GuestbookPost savedPost = guestbookPostRepository.save(post1);
		guestBookService.approveGuestbookPost(savedPost.getId());
		List<GuestbookPost> results = guestBookService.getAllApprovedGuestbookPost();
		Assertions.assertEquals(1, results.size());
	}

	@Test
	void updateGuestbookPost() throws Exception {
		cleanTables();
		setup();
		MockMultipartFile multipartFile1 = createMockMultipartFile();
		GuestbookPost guestPost1 = guestBookService.createGuestbookPost(multipartFile1, text, accountName);
		Long guestPostId = guestPost1.getId();
		byte[] newContent = "New Test Data".getBytes();
		MockMultipartFile multipartFile2 = new MockMultipartFile(mpName, "hellpwrold.jpg", mpContentType, mpContent);
		guestPost1.setImageContent(newContent);
		guestBookService.updateGuestbookPost(multipartFile2, "Hey Judeeeeeee!!!!", guestPostId );
		Optional<GuestbookPost> optResult = guestBookService.findGuestbookPostById(guestPostId);
		Assertions.assertTrue(optResult.isPresent());
		GuestbookPost result = optResult.get();
		Assertions.assertArrayEquals(Base64.encodeBase64(multipartFile2.getBytes()), result.getImageContent());
		Assertions.assertEquals("Hey Judeeeeeee!!!!",result.getPostText());
		Assertions.assertEquals("hellpwrold.jpg",result.getPostImageLabel());
		Assertions.assertEquals(9L,result.getSize());
		Assertions.assertEquals(false,result.getApprovedFlag());
	}

	private GuestbookPost createGBPostWithAccount(Account account) {
		GuestbookPost guestbookPost = new GuestbookPost();
		guestbookPost.setPostText(text);
		guestbookPost.setPostImageLabel(mpOrigFileName);
		guestbookPost.setImageContent(mpContent);
		guestbookPost.setSize(9L);
		guestbookPost.setAccount(account);
		guestbookPost.setApprovedFlag(false);
		guestbookPost.setDateSubmit(Instant.now());
		guestbookPost.setDateApprove(null);
		return guestbookPost;
	}

	private Account createAccount() {
		Account account = new Account();
		account.setAuthAccountGroups(new ArrayList<>());
		account.setName(accountName);
		account.setGuestbookPosts(new ArrayList<>());
		account.setPassword(accountPassword);
		return account;
	}

	private MockMultipartFile createMockMultipartFile() {
		return new MockMultipartFile(mpName, mpOrigFileName, mpContentType, mpContent);
	}
}
