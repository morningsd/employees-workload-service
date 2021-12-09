package edu.demian.controllers;

import static edu.demian.jobs.EmployeesExcelReportsJob.currentReportFile;

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

  @GetMapping("/occupation")
  public StreamingResponseBody sendEmployeesOccupationReport(HttpServletResponse response) {
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment; filename=\"report.xlsx\"");

    InputStream inputStream;
    try {
      inputStream = new FileInputStream(currentReportFile);
    } catch (FileNotFoundException e) {
      throw new ExcelReportGenerationException("Can't upload report --" + new Date());
    }

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
