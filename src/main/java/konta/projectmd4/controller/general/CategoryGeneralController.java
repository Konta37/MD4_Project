package konta.projectmd4.controller.general;

import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Category;
import konta.projectmd4.repository.ICategoryRepository;
import konta.projectmd4.service.admin.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/category")
public class CategoryGeneralController {
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<DataResponse<Page<Category>>> getAllCategories(@PageableDefault(page = 0, size = 2) Pageable pageable) {
        // Use the pageable object to fetch paginated data
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return new ResponseEntity<>(new DataResponse<>(categoryPage,HttpStatus.OK), HttpStatus.OK);
//        return ResponseEntity.ok().body(categoryPage);
    }

//    @PostMapping
//    public ResponseEntity<?> addCategory(@Validated @RequestBody FormCategory formCategory) throws CustomException {
//        Category savedCategory = categoryService.save(formCategory);
//        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED); // Use 201 Created status
//    }
}
