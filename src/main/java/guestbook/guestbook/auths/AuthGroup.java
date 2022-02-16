package guestbook.guestbook.auths;

import lombok.*;

import javax.persistence.*;

/**
 * @author Massimo_Montefusco
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "auth_account_group")
public class AuthGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTH_ACCOUNT_GROUP_ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 128)
    private String name;

    @Column(name = "AUTH_GROUP", nullable = false, length = 128)
    private String authGroup;

}
