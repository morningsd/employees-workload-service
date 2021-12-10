package edu.demian.services.impl;

import edu.demian.dto.reports.EmployeesProjectReportDTO;
import edu.demian.repositories.UserRepository;
import edu.demian.services.ReportsService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportsServiceImpl implements ReportsService {

  private static final long AVAILABLE_GAP_IN_DAYS = 30L;
  private final UserRepository userRepository;

  public ReportsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<EmployeesProjectReportDTO> getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis() {
    return userRepository.getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis();
  }

  @Override
  public List<EmployeesProjectReportDTO> getDataForEmployeesAvailableWithinNext30Days() {
    Instant fromDate = Instant.now().plus(AVAILABLE_GAP_IN_DAYS, ChronoUnit.DAYS);
    return userRepository.getDataForEmployeesAvailableWithinNext30Days(fromDate);
  }
}
