package guestbook.guestbook.auths;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Massimo_Montefusco
 */
public interface AccountRepository  extends JpaRepository<Account, Long> {
    Account findAllByName(String name);
}
