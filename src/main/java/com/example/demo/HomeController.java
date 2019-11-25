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
import java.util.ArrayList;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    CloudinaryConfig cloudc;

    Boolean success = false;

    @RequestMapping("/")
    public String listJobs(Model model){
        model.addAttribute("jobs", jobRepository.findAll());
        model.addAttribute("success", success);
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/add")
    public String addJob(Model model){
        success=false;
        model.addAttribute("job", new Job());
        model.addAttribute("success", success);
        return "add";
    }
    @PostMapping("/search")
    public String search(Model model, @RequestParam("search") String s){
        success=false;
        String[] array = s.split(" ");
        if(array.length>1){
            ArrayList<Job> list = new ArrayList<>();
            ArrayList<Job> cleanlist = new ArrayList<>();
            for(String str: array){
                list.addAll(jobRepository.findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(str,str,str));
            }
            for(Job j: list){
                if(!cleanlist.contains(j)){
                    cleanlist.add(j);
                }
            }
            model.addAttribute("jobs",cleanlist);
        }
        else {
            model.addAttribute("jobs", jobRepository.findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(s, s, s));
        }
        model.addAttribute("success", success);
        return "search";
    }
    @PostMapping("/process")
    public String processForm(@Valid @ModelAttribute Job job, @RequestParam("file")MultipartFile file){
        if(!file.isEmpty()){
            try {
                Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                job.setUrl(uploadResult.get("url").toString());
                jobRepository.save(job);
                success=true;
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/add";
            }
        }
        else{
            success=true;
            jobRepository.save(job);
        }
        return "redirect:/";
    }
    @RequestMapping("/show/{id}")
    public String showJob(@PathVariable("id") long id, Model model){
        success=false;
        model.addAttribute("job", jobRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateJob(@PathVariable("id") long id, Model model){
        success=false;
        model.addAttribute("job", jobRepository.findById(id).get());
        return "add";
    }

    @RequestMapping("/delete/{id}")
    public String delJob(@PathVariable("id") long id){
        success=false;
        jobRepository.deleteById(id);
        return "redirect:/";
    }
}
