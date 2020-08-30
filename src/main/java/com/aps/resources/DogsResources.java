package com.aps.resources;

import com.aps.DogsService;
import com.aps.domain.Dogs;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dogs")
public class DogsResources {

  @Autowired
  DogsService dogsService;

  @GetMapping
  @Cacheable("dogs")
  public List<Dogs> listar() {

    return dogsService.generateDogsList();

  }

  @GetMapping("/banco")
  @Cacheable("dogs")
  public ResponseEntity<Resource> baixarBancoDados() throws IOException {

    File file = dogsService.criarBancoDadosJDBC();
    file.deleteOnExit();

    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

    HttpHeaders header = new HttpHeaders();
    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=banco.db");

    return ResponseEntity.ok().headers(header).contentLength(file.length())
        .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

  }
}
