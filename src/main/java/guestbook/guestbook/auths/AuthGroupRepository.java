package guestbook.guestbook.auths;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * @author Massimo_Montefusco
 */
public interface AuthGroupRepository extends JpaRepository<AuthGroup, Long> {
    List<AuthGroup>  findAllByName(String name);
}
