package guestbook.guestbook.auths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Massimo_Montefusco
 */
@Service
public class GuestBookAccountDetailsService  implements UserDetailsService {

    final Logger logger = LoggerFactory.getLogger(GuestBookAccountDetailsService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthGroupRepository authGroupRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("accountName="+username);
        Account account = null;
        List<AuthGroup> authGroups = null;
        try {
            account = accountRepository.findAllByName(username);
            if (null == account) {
                logger.error("cannot find accountName=" + username);
                throw new UsernameNotFoundException("cannot find accountName: " + username);
            }
            logger.info("account="+account);
            authGroups = authGroupRepository.findAllByName(username);
            //authGroups = authGroupRepository.findByAccount(account);
        }catch(Exception ex){
            logger.error("==>MESSAGE:" + ex.getMessage());
            logger.error("==>CAUSE:" + ex.getCause());
            logger.error("==>" + ex);
        }
        logger.info("account=" +account + " authGroups=" + authGroups);
        return new GuestBookAccountPrincipal(account,authGroups);
    }
}
