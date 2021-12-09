package edu.demian.services.impl;

import edu.demian.dto.reports.EmployeesProjectOccupation;
import edu.demian.repositories.UserRepository;
import edu.demian.services.ReportsService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportsServiceImpl implements ReportsService {

  private final UserRepository userRepository;

  public ReportsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<EmployeesProjectOccupation> getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis() {
    return userRepository.getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis();
  }
}
