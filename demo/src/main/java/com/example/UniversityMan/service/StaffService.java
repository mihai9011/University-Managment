package com.example.UniversityMan.service;

import com.example.UniversityMan.model.Department;
import com.example.UniversityMan.model.Staff;
import com.example.UniversityMan.model.Teacher;
import com.example.UniversityMan.repository.GenericRepository;
import org.springframework.stereotype.Service;

@Service
public class StaffService extends CrudService<Staff> {
	private final GenericRepository<Department> departments;

	public StaffService(GenericRepository<Staff> staff, GenericRepository<Department> departments) {
		super(staff);
		this.departments = departments;
	}

	@Override
	public Staff create(Staff staff) {
		Department department = staff instanceof Teacher teacher ? require(departments, teacher.getDepartmentId()) : null;
		Staff saved = super.create(staff);
		if (department != null) {
			department.getTeachers().add((Teacher) saved);
		}
		return saved;
	}

	@Override
	protected void beforeDelete(Staff staff) {
		ensureEmpty(staff.getAssignments(), "Lehrzuweisungen");
		if (staff instanceof Teacher teacher) {
			require(departments, teacher.getDepartmentId()).getTeachers().remove(teacher);
		}
	}
}
