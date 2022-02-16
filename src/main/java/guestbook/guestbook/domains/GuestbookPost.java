package guestbook.guestbook.domains;

import guestbook.guestbook.auths.Account;
import lombok.*;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.time.Instant;

/**
 * @author Massimo_Montefusco
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "guestbook_post")
public class GuestbookPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "approved_flag")
    private Boolean approvedFlag;

    @Column(name = "date_submit")
    private Instant dateSubmit;

    @Column(name = "date_approve")
    private Instant dateApprove;

    @Lob
    @Column(name = "image_content")
    private byte[] imageContent;

    @Column(name = "post_image_label")
    private String postImageLabel;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "size")
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_account_id")
    private Account account;

    public String getImage() throws UnsupportedEncodingException {
        return  new String(imageContent, "UTF-8");
    }
}
