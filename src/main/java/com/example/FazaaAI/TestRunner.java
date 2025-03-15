////package com.example.FazaaAI;
////
////import com.example.FazaaAI.entity.Crisis;
////import com.example.FazaaAI.entity.Post;
////import com.example.FazaaAI.entity.SurvivalGuide;
////import com.example.FazaaAI.service.CrisisService;
////import com.example.FazaaAI.service.PostService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.boot.CommandLineRunner;
////import org.springframework.stereotype.Component;
////
////@Component
////public class TestRunner implements CommandLineRunner {
////
////    @Autowired
////    private PostService postService;
////
////    @Autowired
////    private CrisisService crisisService;
////
////    @Override
////    public void run(String... args) {
////        // Test for Post
////        Post post = new Post();
////        post.setCity("Al Rehab City");
////        postService.generatePostContent(post, "Urgently need 5 bottles of water");
////
////        System.out.println("🟢 Generated Post Title: " + post.getTitle());
////        System.out.println("🟢 Generated Post Description: " + post.getDescription());
////
////        // Test for Crisis
////        Crisis crisis = new Crisis();
////        crisis.setCrisisType("Earthquake");
////        crisis.setCity("Cairo");
////
////        SurvivalGuide guide = crisisService.generateSurvivalGuide(crisis);
////        System.out.println("🟢 Generated Survival Guide: " + guide.getGeneratedText());
////    }
////}
//package com.example.FazaaAI;
//
//import com.example.FazaaAI.service.AIService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TestRunner implements CommandLineRunner {
//
//    private final AIService aiService;
//
//    public TestRunner(AIService aiService) {
//        this.aiService = aiService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        String prompt = "Provide a brief earthquake survival guide for Cairo.";
//        String response = aiService.generateContent(prompt);
//
//        System.out.println("✅ Clearly Generated AI Response:");
//        System.out.println(response);
//    }
//}