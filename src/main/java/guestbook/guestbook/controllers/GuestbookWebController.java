package guestbook.guestbook.controllers;

import guestbook.guestbook.services.GuestBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Massimo_Montefusco
 */
@Controller
@RequestMapping("/")
public class GuestbookWebController {

    Logger logger = LoggerFactory.getLogger(GuestbookWebController.class);

    @Autowired
    private GuestBookService guestBookService;

    @GetMapping(value={"/", "/index"})
    public String getHomePage(Model model){
        logger.info("getHomePage start -> Model" + model);
        model.addAttribute("posts", guestBookService.getAllApprovedGuestbookPost());
        return "index";
    }

    @GetMapping(value={"/write"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getWritePage(Model model){
        logger.info("getWritePage start -> Model" + model);
        return "write";
    }

    @GetMapping(value={"/manage"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getManagePage(Model model){
        logger.info("getWritePage start -> Model" + model);
        model.addAttribute("posts", guestBookService.getAllNotApprovedGuestbookPost());
        return "manage";
    }

    @GetMapping(value="/login")
    public String getLoginPage(Model model){
        logger.info("getLoginPage start -> Model" + model);
        return "login";
    }

    @GetMapping(value="/logout-success")
    public String getLogoutPage(Model model){
        logger.info("getLogoutPage start -> Model" + model);
        return "logout";
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String postUpload(@RequestParam("image") MultipartFile image, String text, Authentication authentication, Model model) throws IOException {
        logger.info("fileUpload start -> Model: " + model);
        logger.info("fileUpload start -> text: " + text);
        logger.info("fileUpload start -> account: " + authentication.getName());
        logger.info("fileUpload start -> img orig file name: " + image.getOriginalFilename());
        guestBookService.createGuestbookPost(image, text, authentication.getName());
        model.addAttribute("posts", guestBookService.getAllApprovedGuestbookPost());
        return "index";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postUpdate(@PathVariable("id") Long idPost, @RequestParam("image") MultipartFile image, String text, Authentication authentication, Model model) throws IOException {
        guestBookService.updateGuestbookPost(image, text, idPost);
        model.addAttribute("posts", guestBookService.getAllNotApprovedGuestbookPost());
        return "manage";
    }

    @GetMapping("/editpost/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editPost(@PathVariable("id") long id, Authentication authentication, Model model) throws IOException {
        model.addAttribute("post", guestBookService.findGuestbookPostById(id).get());
        return "modify";
    }

    @GetMapping("/approvepost/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String approvePost(@PathVariable("id") long id, Authentication authentication, Model model) throws Exception {
        guestBookService.approveGuestbookPost(id);
        model.addAttribute("posts", guestBookService.getAllNotApprovedGuestbookPost());
        return "manage";
    }

    @GetMapping("/deletepost/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletePost(@PathVariable("id") long id, Authentication authentication, Model model) throws Exception {
        guestBookService.deleteGuestbookPostById(id);
        model.addAttribute("posts", guestBookService.getAllNotApprovedGuestbookPost());
        return "manage";
    }
}
