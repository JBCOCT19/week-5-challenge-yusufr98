package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listJobs(Model model){
        model.addAttribute("jobs", jobRepository.findAll());
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/add")
    public String addJob(Model model){
        model.addAttribute("job", new Job());
        return "add";
    }
    @PostMapping("/search")
    public String search(Model model, @RequestParam("search") String s){
        model.addAttribute("jobs", jobRepository.findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(s,s,s));
        return "index";
    }
    @PostMapping("/process")
    public String processForm(@Valid @ModelAttribute Job job, @RequestParam("file")MultipartFile file){
        if(!file.isEmpty()){
            try {
                Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                job.setUrl(uploadResult.get("url").toString());
                jobRepository.save(job);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/add";
            }
        }
        else{
            jobRepository.save(job);
        }
        return "redirect:/";
    }
    @RequestMapping("/show/{id}")
    public String showJob(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateJob(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobRepository.findById(id).get());
        return "add";
    }

    @RequestMapping("/delete/{id}")
    public String delJob(@PathVariable("id") long id){
        jobRepository.deleteById(id);
        return "redirect:/";
    }
}
