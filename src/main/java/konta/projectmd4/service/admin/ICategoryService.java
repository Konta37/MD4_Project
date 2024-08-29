package konta.projectmd4.service.admin;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormCategory;
import konta.projectmd4.model.entity.admin.Category;

public interface ICategoryService {
    Category save(FormCategory formCategory) throws CustomException;
}
