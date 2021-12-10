package edu.demian.controllers;

import static edu.demian.jobs.EmployeesExcelReportsJob.currentReportFileEmployeesAvailable;
import static edu.demian.jobs.EmployeesExcelReportsJob.currentReportFileEmployeesWorkload;

import edu.demian.exceptions.ExcelReportGenerationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/reports")
public class ExcelReportsController {

  @GetMapping("/workload")
  public StreamingResponseBody sendEmployeesWorkloadReport(HttpServletResponse response) {
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment; filename=\"report-workload.xlsx\"");

    InputStream inputStream;
    try {
      inputStream = new FileInputStream(currentReportFileEmployeesWorkload);
    } catch (FileNotFoundException e) {
      throw new ExcelReportGenerationException("Can't upload report[workload] --" + new Date());
    }

    return getStreamingResponseBody(inputStream);
  }

  @GetMapping("available")
  public StreamingResponseBody sendEmployeesAvailableReport(HttpServletResponse response) {
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment; filename=\"report-available.xlsx\"");


    InputStream inputStream;
    try {
      inputStream = new FileInputStream(currentReportFileEmployeesAvailable);
    } catch (FileNotFoundException e) {
      throw new ExcelReportGenerationException("Can't upload report[available] --" + new Date());
    }

    return getStreamingResponseBody(inputStream);
  }

  private StreamingResponseBody getStreamingResponseBody(InputStream inputStream) {
    return outputStream -> {
      int nRead;
      byte[] data = new byte[1024];
      while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
        outputStream.write(data, 0, nRead);
      }
      inputStream.close();
    };
  }

}
