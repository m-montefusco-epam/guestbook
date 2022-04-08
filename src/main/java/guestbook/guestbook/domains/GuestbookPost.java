package guestbook.guestbook.domains;

import guestbook.guestbook.auths.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.nio.charset.StandardCharsets;
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

    public String getImage() {
        return  new String(imageContent, StandardCharsets.UTF_8);
    }
}
