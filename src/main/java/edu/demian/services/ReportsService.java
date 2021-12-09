package edu.demian.services;

import edu.demian.dto.reports.EmployeesProjectOccupation;
import java.util.List;

public interface ReportsService {

  List<EmployeesProjectOccupation> getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis();

}
