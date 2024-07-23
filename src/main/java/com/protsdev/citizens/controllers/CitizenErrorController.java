package com.protsdev.citizens.controllers;

// import static
// org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
// import static
// org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.web.ErrorProperties;
// import
// org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
// import org.springframework.boot.web.servlet.error.ErrorAttributes;
// import org.springframework.boot.web.servlet.error.ErrorController;
// import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.context.request.ServletWebRequest;

// import jakarta.servlet.http.HttpServletRequest;

// @Controller
// @RequestMapping("/error")
// public class CitizenErrorController implements ErrorController {
// public class CitizenErrorController extends BasicErrorController {

// @RequestMapping("/error")
// @ResponseBody
// public String handleError(HttpServletRequest request) {
// Integer statusCode = (Integer)
// request.getAttribute("javax.servlet.error.status_code");
// Exception exception = (Exception)
// request.getAttribute("javax.servlet.error.exception");
// return String.format("<html><body><h2>Error Page</h2><div>Status code:
// <b>%s</b></div>"
// + "<div>Exception Message: <b>%s</b></div><body></html>",
// statusCode, exception == null ? "N/A" : exception.getMessage());
// }

// @Override
// public String getErrorPath() {
// return "/error";
// }

// @Autowired
// private ErrorAttributes errorAttributes;

// public CitizenErrorController(ErrorAttributes et, ErrorProperties ep) {
// super(et, ep);
// }

// @GetMapping("/error")
// @ResponseBody
// public ResponseEntity<?> error(HttpServletRequest request) {
// ServletWebRequest servletWebRequest = new ServletWebRequest(request);
// Map<String, Object> errorAttributes =
// this.errorAttributes.getErrorAttributes(servletWebRequest, null);

// return ResponseEntity.ok(HalModelBuilder
// .emptyHalModel()
// .embed(errorAttributes)
// .link(linkTo(methodOn(CitizenErrorController.class).error(null)).withSelfRel())
// .build());
// }

// @Override
// @GetMapping("/error")
// // @ResponseBody
// public ResponseEntity<?> error(HttpServletRequest request) {
// ServletWebRequest servletWebRequest = new ServletWebRequest(request);
// Map<String, Object> errorAttributes =
// this.errorAttributes.getErrorAttributes(servletWebRequest, null);

// return ResponseEntity.ok(HalModelBuilder
// .emptyHalModel()
// .embed(errorAttributes)
// .link(linkTo(methodOn(CitizenErrorController.class).error(null)).withSelfRel())
// .build());
// }
// }
