package edu.demian.services;

import edu.demian.dto.reports.EmployeesProjectReportDTO;
import java.time.Instant;
import java.util.List;

public interface ReportsService {

  List<EmployeesProjectReportDTO> getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis();

  List<EmployeesProjectReportDTO> getDataForEmployeesAvailableWithinNext30Days();

}
