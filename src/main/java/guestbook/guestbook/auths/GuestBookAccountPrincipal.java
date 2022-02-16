package guestbook.guestbook.auths;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * @author Massimo_Montefusco
 */
@ToString
public class GuestBookAccountPrincipal implements UserDetails {

    private final Account account;
    private final List<AuthGroup> authGroups;

    public GuestBookAccountPrincipal(Account account, List<AuthGroup> authGroups){
        super();
        this.account = account;
        this.authGroups = authGroups;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(null==authGroups){
            return Collections.emptySet();
        }
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        authGroups.forEach(group-> grantedAuthorities.add(new SimpleGrantedAuthority(group.getAuthGroup())));
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.account.getPassword();
    }

    @Override
    public String getUsername() {
        return this.account.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
