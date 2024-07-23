package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.protsdev.citizens.storage.StorageFileNotFoundException;
import com.protsdev.citizens.storage.StorageService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    private StorageService storageService;

    // @GetMapping
    // public ResponseEntity<?> listUploadedFiles() throws IOException {

    // List<String> urls = storageService.loadAll().map(
    // path -> MvcUriComponentsBuilder.fromMethodName(
    // FileUploadController.class,
    // "serveFile",
    // path.getFileName().toString()).build()
    // .toUri()
    // .toString())
    // .toList();

    // return ResponseEntity.ok(CollectionModel.of(urls)
    // .add(linkTo(methodOn(FileUploadController.class).listUploadedFiles()).withSelfRel()));
    // }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        // download
        // return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        // "attachment; filename=\"" + file.getFilename() + "\"").body(file);

        // show
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,
                "image/jpeg").body(file);
    }

    /*
     * Upload
     */
    // @PostMapping
    // public String handleFileUpload(@RequestParam("file") MultipartFile file,
    // RedirectAttributes redirectAttributes) {

    // if (file != null) {
    // storageService.store(file);
    // redirectAttributes.addFlashAttribute("message", "You successfully uploaded "
    // + file.getOriginalFilename() + "!");
    // }

    // return "redirect:/api/files";
    // }

}
