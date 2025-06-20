package com.hackthon.renegados;

import com.hackthon.renegados.model.Usuario;
import com.hackthon.renegados.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RenegadosApplication {

	public static void main(String[] args) {
		SpringApplication.run(RenegadosApplication.class, args);
	}


	@Bean
	public CommandLineRunner commandLineRunner(UsuarioRepository usuarioRepository) {
		return args -> {

			if (usuarioRepository.count() > 0) {
				return;
			}

			usuarioRepository.save(new Usuario(
					null,
					"user",
					"admin",
					new BCryptPasswordEncoder().encode("admin"),
					"ADMIN"
			));

			usuarioRepository.save(new Usuario(
					null,
					"professor",
					"professor",
					new BCryptPasswordEncoder().encode("professor"),
					"PROF"
			));

			usuarioRepository.save(new Usuario(
					null,
					"aluno",
					"aluno",
					new BCryptPasswordEncoder().encode("aluno"),
					"ALUNO"
			));
		};
	}
}
