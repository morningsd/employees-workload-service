package edu.demian.repositories;

import edu.demian.dto.reports.EmployeesProjectOccupation;
import edu.demian.entities.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  @Query(
      value =
          "SELECT u.* FROM usr u JOIN user_project up ON u.id = up.user_id WHERE up.project_id = ?1",
      nativeQuery = true)
  List<User> findUsersByUserProjectsProjectId(UUID projectId);

  @Query(
      value =
          "select u.* from usr u left join user_project on user_project.user_id = u.id and user_project.position_end_date > ?1 where user_project.project_id is null",
      nativeQuery = true)
  List<User> findUsersAvailableWithinNextCoupleOfDays(Instant fromData);

  @Query(
      value =
          "select new edu.demian.dto.reports.EmployeesProjectOccupation(u.firstName, u.lastName, d.name, p.name, up.workingHours) from UserProject up left join up.user u left join u.department d left join up.project p"
  )
  List<EmployeesProjectOccupation> getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis();
}
