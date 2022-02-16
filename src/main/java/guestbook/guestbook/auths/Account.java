package guestbook.guestbook.auths;


import guestbook.guestbook.domains.GuestbookPost;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;

import java.util.List;

/**
 * @author Massimo_Montefusco
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID", nullable = false)
    private Long id;

    @Column(name = "NAME", length = 128)
    private String name;

    @Column(name = "PASSWORD", length = 256)
    private String password;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<GuestbookPost> guestbookPosts = new ArrayList<>();

    @OneToMany(mappedBy = "name", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AuthGroup> authAccountGroups = new ArrayList<>();

}
