package guestbook.guestbook.services;

import guestbook.guestbook.domains.GuestbookPost;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author Massimo_Montefusco
 */
public interface GuestBookService {
    GuestbookPost createGuestbookPost(MultipartFile image, String text, String accountName) throws IOException;
    List<GuestbookPost> getAllGuestbookPost();
    List<GuestbookPost> getAllApprovedGuestbookPost();
    List<GuestbookPost> getAllNotApprovedGuestbookPost();
    Optional<GuestbookPost> findGuestbookPostById(long id);
    void deleteGuestbookPost(GuestbookPost guestbookPost);
    void deleteGuestbookPostById(long id) throws Exception;
    void approveGuestbookPost(long id) throws Exception;
    void updateGuestbookPost(MultipartFile image, String text, Long idPost) throws IOException;
}
