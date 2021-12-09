package edu.demian.jobs;

import edu.demian.dto.reports.EmployeesProjectOccupation;
import edu.demian.exceptions.ExcelReportGenerationException;
import edu.demian.services.ReportsService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmployeesExcelReportsJob {

  private static final int HEADER_ROW_NUMBER = 0;

  private static final int DATA_START_ROW_NUMBER = 2;

  private static final String XLSX_EXTENSION = ".xlsx";

  private static final String SHEET_NAME = "Employees workload with occupation";

  private static final String[] COLUMNS = {"full_name", "department", "project", "occupation"};

  private final List<AutoCloseable> resources = new ArrayList<>();

  public static File currentReportFile;

  private final ReportsService reportsService;

  public EmployeesExcelReportsJob(ReportsService reportsService) {
    this.reportsService = reportsService;
  }

  @Scheduled(cron = "*/30 * * * * *")
  public void getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis() {
    System.out.println("Hello, world");
    List<EmployeesProjectOccupation> data =
        reportsService.getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis();
    try {
      Workbook workbook = initWorkbook();
      Sheet sheet = initSheet(workbook);
      fillWorkbookWithData(data, workbook, sheet);
    } catch (IOException e) {
      throw new ExcelReportGenerationException(
          "Can't create excel file to generate report --" + new Date());
    }
    closeResources();
    System.out.println("END");
  }

  private Workbook initWorkbook() throws IOException {
    Workbook workbook = new XSSFWorkbook();
    resources.add(workbook);
    return workbook;
  }

  private Sheet initSheet(Workbook workbook) {
    Sheet sheet = workbook.createSheet(SHEET_NAME);

    for (int i = 0; i < COLUMNS.length; i++) {
      sheet.setColumnWidth(i, 4000);
    }

    Row header = sheet.createRow(HEADER_ROW_NUMBER);
    createHeader(header);

    return sheet;
  }

  private static void createHeader(Row header) {
    for (int i = 0; i < COLUMNS.length; i++) {
      Cell headerCell = header.createCell(i);
      headerCell.setCellValue(COLUMNS[i]);
    }
  }

  private void fillWorkbookWithData(
      List<? extends EmployeesProjectOccupation> data, Workbook workbook, Sheet sheet)
      throws IOException {
    int rowNumber = data.size() + DATA_START_ROW_NUMBER;
    Row row;
    int ctr = 0;
    for (int rowCtr = DATA_START_ROW_NUMBER; rowCtr < rowNumber; rowCtr++) {
      EmployeesProjectOccupation emp = data.get(rowCtr - DATA_START_ROW_NUMBER);
      row = sheet.createRow(rowCtr);


      ctr = createNewCell(row, ctr, emp.getFirstName() + " " + emp.getLastName());
      ctr = createNewCell(row, ctr, emp.getDepartmentName());
      ctr = createNewCell(row, ctr, emp.getProjectName());
      ctr = createNewCell(row, ctr, String.valueOf(emp.getOccupation()));

      ctr = 0;
    }

    String fileName = UUID.randomUUID().toString();

    currentReportFile = new File(fileName + XLSX_EXTENSION);

    FileOutputStream outputStream = new FileOutputStream(currentReportFile);
    resources.add(outputStream);
    workbook.write(outputStream);
  }

  private int createNewCell(Row row, int ctr, String data) {
    Cell cell = row.createCell(ctr++);
    cell.setCellValue(data);
    return ctr;
  }

  private void closeResources() {
    for (Iterator<AutoCloseable> iter = resources.listIterator(); iter.hasNext(); ) {
      AutoCloseable resource = iter.next();
      try {
        resource.close();
      } catch (Exception e) {
        /* do nothing */
      }
      iter.remove();
    }
  }
}
