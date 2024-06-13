package com.openclassrooms.mddapi;

import com.openclassrooms.mddapi.models.Role;
import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.repositories.RoleRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MddApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MddApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner (RoleRepository roleRepository, TopicRepository topicRepository) {
		return args -> {
			if(roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}

			if(topicRepository.count() == 0)
			{
				//create dummy topics for the application
				List<Topic> topics = new ArrayList<>();

				var topic1 = Topic.builder()
						.title("PC Hardware World")
						.description("PC Hardware is an essential tool if you are interested about computer hardware and software, we provide news, unbiased reviews and guides.")
						.createdAt(LocalDateTime.now())
						.build();
				topics.add(topic1);

				var topic2 = Topic.builder()
						.title("Apple Hardware World")
						.description("We are fully dedicated on giving news for Apple fanboy :)")
						.createdAt(LocalDateTime.now())
						.build();
				topics.add(topic2);

				var topic3 = Topic.builder()
						.title("Crypto World")
						.description("Everything you need to know about Cryptoassets in the rapidly growing cryptocurrency space.")
						.createdAt(LocalDateTime.now())
						.build();
				topics.add(topic3);

				topicRepository.saveAll(topics);
			}
		};
	}

}
