package konta.projectmd4.controller.admin;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormCategory;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.admin.Category;
import konta.projectmd4.repository.admin.ICategoryRepository;
import konta.projectmd4.service.admin.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/category")
public class CategoryController {
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<DataResponse<Page<Category>>> getAllCategories(@PageableDefault(page = 0, size = 2) Pageable pageable) {
        // Use the pageable object to fetch paginated data
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        return new ResponseEntity<>(new DataResponse<>(categoryPage,HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse<Category>> addCategory(@Validated @RequestBody FormCategory formCategory) throws CustomException {
        Category savedCategory = categoryService.save(formCategory);
//        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED); // Use 201 Created status
        return new ResponseEntity<>(new DataResponse<>(savedCategory,HttpStatus.CREATED), HttpStatus.CREATED);
    }
}
