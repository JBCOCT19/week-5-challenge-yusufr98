
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired JobRepository jobRepository;

    @Override
    public void run(String ... strings) throws Exception {
        Job job;
        job = new Job("Junior Web Developer","Looking for entry-level web developer who is comfortable with HTML, CSS, JS, Bootstrap and React.", "11/23/19", "John Doe","301-123-4567", "https://miro.medium.com/max/3960/0*HICLyAdNSIyT0ODU.jpg");
        jobRepository.save(job);

        job = new Job("Project Manager", "Looking for an experienced project manager who can provide great leadership and direction to a small development team.","11/15/19","Jane Smith","240-789-4561", "");
        jobRepository.save(job);

        job = new Job("Seasonal Retail Employee","Temporary customer service opening for the holiday season. Must work weekends and evening shifts.","11/01/19","Target", "1-800-951-3578","https://nmgprod.s3.amazonaws.com/media/files/36/66/3666729b49ec130782ca74655d80e96d/cover_image.jpg.640x360_q85_crop.jpg");
        jobRepository.save(job);
    }
}
