package guestbook.guestbook.services;

import guestbook.guestbook.auths.Account;
import guestbook.guestbook.auths.AccountRepository;
import guestbook.guestbook.domains.GuestbookPost;
import guestbook.guestbook.repositories.GuestbookPostRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author Massimo_Montefusco
 */
@Service
public class GuestBookServiceImpl implements GuestBookService{

    Logger logger = LoggerFactory.getLogger(GuestBookServiceImpl.class);

    @Autowired
    private GuestbookPostRepository guestbookPostRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public GuestbookPost createGuestbookPost(MultipartFile image, String text, String accountName) throws IOException {
        Account account = accountRepository.findAllByName(accountName);
        GuestbookPost guestbookPost = new GuestbookPost();
        guestbookPost.setPostText(text);
        guestbookPost.setPostImageLabel(image.getOriginalFilename());
        guestbookPost.setImageContent(Base64.encodeBase64(image.getBytes()));
        guestbookPost.setSize(image.getSize());
        guestbookPost.setAccount(account);
        guestbookPost.setApprovedFlag(false);
        guestbookPost.setDateSubmit(Instant.now());
        guestbookPost.setDateApprove(null);

        return guestbookPostRepository.save(guestbookPost);
    }

    @Override
    public List<GuestbookPost> getAllGuestbookPost() {
        return guestbookPostRepository.findAll();
    }

    @Override
    public List<GuestbookPost> getAllApprovedGuestbookPost() {
        return guestbookPostRepository.findAllByApprovedFlagEqualsOrderByDateSubmitDesc(true);
    }

    @Override
    public List<GuestbookPost> getAllNotApprovedGuestbookPost() {
        return guestbookPostRepository.findAllByApprovedFlagEqualsOrderByDateSubmitDesc(false);
    }

    @Override
    public Optional<GuestbookPost> findGuestbookPostById(long id) {
        return guestbookPostRepository.findById(id);
    }

    @Override
    public void deleteGuestbookPost(GuestbookPost guestbookPost) {
        guestbookPostRepository.delete(guestbookPost);
    }

    @Override
    public void deleteGuestbookPostById(long id) throws Exception {
        GuestbookPost guestbookPost = guestbookPostRepository.findById(id).orElseThrow(()->new Exception("No post for id="+id));
        guestbookPostRepository.delete(guestbookPost);
    }

    @Override
    public void approveGuestbookPost(long id) throws Exception {
        GuestbookPost guestbookPost = guestbookPostRepository.findById(id).orElseThrow(()->new Exception("No post for id="+id));
        guestbookPost.setApprovedFlag(true);
        guestbookPostRepository.save(guestbookPost);
    }

    @Override
    public void updateGuestbookPost(MultipartFile image, String text, Long idPost) throws IOException {
        Optional<GuestbookPost> guestbookPost = guestbookPostRepository.findById(idPost);
        if(guestbookPost.isPresent()) {
            GuestbookPost guestbook = guestbookPost.get();
            if(image != null && image.getBytes() != null) {
                guestbook.setPostImageLabel(image.getOriginalFilename());
                guestbook.setImageContent(Base64.encodeBase64(image.getBytes()));
                guestbook.setSize(image.getSize());
            }
            guestbook.setPostText(text);

            logger.info("original idPost=" + idPost);
            logger.info("read idPost=" + guestbook.getId());
            guestbookPostRepository.save(guestbook);
        }
    }
}
