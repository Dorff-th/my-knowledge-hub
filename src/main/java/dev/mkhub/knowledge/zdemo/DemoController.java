package dev.mkhub.knowledge.zdemo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/zdemo")
public class DemoController {

    @GetMapping("/file-example1")
    public String fileExample1() {
        return "zdemo/example/file_example1.html";

    }

    @GetMapping("/file-example2")
    public String fileExample2() {
        return "zdemo/example/file_example2.html";

    }

    @GetMapping("/file-example2-gpt")
    public String fileExample2Gpt() {
        return "zdemo/example/file_example2_gpt.html";

    }


    @GetMapping("/file-example3")
    public String fileExample3() {
        return "zdemo/example/file_example3.html";

    }

    @PostMapping("/file-example2")
    public String fileExamplePost(@RequestParam("attachments") List<MultipartFile> attachments, Model model) {
        log.debug("\n");
        log.debug("attchmens size :" + attachments.size());
        if(attachments !=null && !attachments.isEmpty()) {
            attachments.forEach(attachment->{
                log.debug(attachment.getOriginalFilename());
                log.debug("" + attachment.getSize());
                log.debug("\n");
            });
        }

        model.addAttribute("attachments", attachments);

        return "zdemo/example/file_example_post_result";

    }

    //첨부파일 드로그앤 드랍 (GPT제공)
    @GetMapping("file-example10-gpt")
    public String fileExample10Gpt() {
        return "zdemo/example/file_example10_gpt";

    }

    //첨부파일 드로그앤 드랍 (GPT제공)
    @GetMapping("file-example11-gpt")
    public String fileExample11Gpt() {
        return "zdemo/example/file_example11_gpt";

    }
}
