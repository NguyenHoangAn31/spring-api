package vn.aptech.backendapi.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.aptech.backendapi.dto.DepartmentDto;
import vn.aptech.backendapi.service.Department.DepartmentService;
import vn.aptech.backendapi.service.File.FileService;

@RestController
@RequestMapping(value = "/api/department")
public class DepartmentController {

    @Autowired
    private FileService fileService;

    @Autowired
    private DepartmentService departmentService;

    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DepartmentDto>> findAll() {
        List<DepartmentDto> result = departmentService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentDto> findById(@PathVariable("id") int id) {
        Optional<DepartmentDto> result = departmentService.findById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) throws IOException {
        Optional<DepartmentDto> result = departmentService.findById(id);
        boolean status = result.get().isStatus();
        if (status == true) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Department is Active");
        }
        String pathIcon = result.get().getIcon();
        boolean deleted = departmentService.deleteById(id);
        if (deleted) {
            if (pathIcon != null) {
                fileService.deleteFile("department", pathIcon);
            }
        }
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    // @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<DepartmentDto> Create(@RequestParam("icon") MultipartFile photo,
            @RequestParam("department") String department) throws IOException {

        // xử lý DepartmentDto
        ObjectMapper objectMapper = new ObjectMapper();
        DepartmentDto departmentDto = objectMapper.readValue(department, DepartmentDto.class);
        // xử lý hình ảnh

        departmentDto.setIcon(fileService.uploadFile("department", photo));
        DepartmentDto result = departmentService.save(departmentDto);
        if (result != null) {
            return ResponseEntity.ok(result); // Return the created SlotDto
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentDto> updateTutorial(@PathVariable("id") int id,
            @RequestParam(name = "icon", required = false) MultipartFile photo,
            @RequestParam("department") String department) throws IOException {

        System.out.println("photo : " + photo);
        System.out.println("department : " + department);

        // xử lý DepartmentDto
        ObjectMapper objectMapper = new ObjectMapper();
        DepartmentDto departmentDto = objectMapper.readValue(department,
                DepartmentDto.class);
        // xử lý hình ảnh
        if (photo != null) {
            // nếu ảnh tồn tại thì xóa
            if (departmentDto.getIcon() != null) {
                fileService.deleteFile("department", departmentDto.getIcon());
            }
            departmentDto.setIcon(fileService.uploadFile("department", photo));

        }
        departmentDto.setId(id);
        DepartmentDto updatedDepartment = departmentService.save(departmentDto);
        if (updatedDepartment != null) {

            return ResponseEntity.ok(updatedDepartment);
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping(value = "/changestatus/{id}/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeStatusDepartment(@PathVariable("id") int id,@PathVariable("status") int status) {
        boolean changed = departmentService.changeStatus(id,status);
        if (changed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}