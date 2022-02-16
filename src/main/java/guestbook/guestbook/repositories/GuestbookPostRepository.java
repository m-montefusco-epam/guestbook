package guestbook.guestbook.repositories;

import guestbook.guestbook.domains.GuestbookPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Massimo_Montefusco
 */
public interface  GuestbookPostRepository extends JpaRepository<GuestbookPost, Long> {
    List<GuestbookPost> findAllByApprovedFlagEqualsOrderByDateSubmitDesc(boolean isApproved);
}
