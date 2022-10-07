package com.stoph.boolog;

import com.stoph.boolog.domain.member.MemberRepository;
import com.stoph.boolog.domain.posts.PostsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BoologApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoologApplication.class, args);
	}

	@Profile("local")
	@Bean
	public TestDataInit testDataInit(PostsRepository postsRepository, MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
		return new TestDataInit(postsRepository, memberRepository, passwordEncoder);
	}

}
