package com.aps.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aps.domain.Dogs;
import com.github.javafaker.Dog;
import com.github.javafaker.Faker;

@RestController
@RequestMapping("dogs")
public class DogsResources {

	private static final int QUANTIDADEREPETICOES = 20000;

	@GetMapping
	@Cacheable("dogs")
	public List<Dogs> listar() {

		Dog fakeDog = new Faker().dog();

		List<Dogs> dogs = new ArrayList<>();

		for (int i = 0; i < QUANTIDADEREPETICOES; i++) {
			Dogs dog = new Dogs(fakeDog.age(), fakeDog.breed(), fakeDog.coatLength(), fakeDog.gender(),
					fakeDog.memePhrase(), fakeDog.name(), fakeDog.size(), fakeDog.sound());
			dogs.add(dog);
		}

		return dogs;

	}

}
