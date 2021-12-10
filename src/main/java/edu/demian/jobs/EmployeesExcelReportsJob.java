package edu.demian.jobs;

import edu.demian.dto.reports.EmployeesProjectReportDTO;
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

  private static final String[] WORKLOAD_COLUMNS = {"full_name", "department", "project", "occupation"};
  private static final String[] AVAILABLE_COLUMNS = {"full_name", "department", "project", "position_end_date"};

  private final List<AutoCloseable> resources = new ArrayList<>();

  public static File currentReportFileEmployeesWorkload;
  public static File currentReportFileEmployeesAvailable;

  private final ReportsService reportsService;

  public EmployeesExcelReportsJob(ReportsService reportsService) {
    this.reportsService = reportsService;
  }

  @Scheduled(cron = "*/30 * * * * *")
  public void createExcelReportEmployeesAvailable() {
    System.out.println("START2");
    List<EmployeesProjectReportDTO> dataEmployeesAvailable =
        reportsService.getDataForEmployeesAvailableWithinNext30Days();

    try {
      Workbook workbookEmployeesAvailable = initWorkbook();
      Sheet sheet = initSheet(workbookEmployeesAvailable, AVAILABLE_COLUMNS);
      fillAvailableWorkbookWithData(dataEmployeesAvailable, workbookEmployeesAvailable, sheet);
    } catch (IOException e) {
      throw new ExcelReportGenerationException(
          "Can't create excel file to generate report[employees-available] --" + new Date());
    }
    closeResources();
    System.out.println("END2");
  }


  @Scheduled(cron = "*/30 * * * * *")
  public void createExcelReportEmployeesWorkload() {
    System.out.println("START");
    List<EmployeesProjectReportDTO> dataEmployeesWorkload =
        reportsService.getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis();

    try {
      Workbook workbookEmployeesWorkload = initWorkbook();
      Sheet sheet = initSheet(workbookEmployeesWorkload, WORKLOAD_COLUMNS);
      fillWorkloadWorkbookWithData(dataEmployeesWorkload, workbookEmployeesWorkload, sheet);
    } catch (IOException e) {
      throw new ExcelReportGenerationException(
          "Can't create excel file to generate report[employees-workload] --" + new Date());
    }
    closeResources();
    System.out.println("END");
  }

  private Workbook initWorkbook() throws IOException {
    Workbook workbook = new XSSFWorkbook();
    resources.add(workbook);
    return workbook;
  }

  private Sheet initSheet(Workbook workbook, String[] columns) {
    Sheet sheet = workbook.createSheet(SHEET_NAME);

    for (int i = 0; i < columns.length; i++) {
      sheet.setColumnWidth(i, 4000);
    }

    Row header = sheet.createRow(HEADER_ROW_NUMBER);
    createHeader(header, columns);

    return sheet;
  }

  private static void createHeader(Row header, String[] columns) {
    for (int i = 0; i < columns.length; i++) {
      Cell headerCell = header.createCell(i);
      headerCell.setCellValue(columns[i]);
    }
  }

  private void fillAvailableWorkbookWithData(
      final List<? extends EmployeesProjectReportDTO> data, final Workbook workbook,
      final Sheet sheet)
      throws IOException {
    int rowNumber = data.size() + DATA_START_ROW_NUMBER;
    Row row;
    for (int rowCtr = DATA_START_ROW_NUMBER; rowCtr < rowNumber; rowCtr++) {
      EmployeesProjectReportDTO emp = data.get(rowCtr - DATA_START_ROW_NUMBER);
      row = sheet.createRow(rowCtr);
      fillRowWithAvailableData(row, emp);
    }

    writeWorkbookToFile(workbook, currentReportFileEmployeesAvailable);
  }

  private void fillWorkloadWorkbookWithData(
      final List<? extends EmployeesProjectReportDTO> data, final Workbook workbook,
      final Sheet sheet)
      throws IOException {
    int rowNumber = data.size() + DATA_START_ROW_NUMBER;
    Row row;
    for (int rowCtr = DATA_START_ROW_NUMBER; rowCtr < rowNumber; rowCtr++) {
      EmployeesProjectReportDTO emp = data.get(rowCtr - DATA_START_ROW_NUMBER);
      row = sheet.createRow(rowCtr);
      fillRowWithWorkloadData(row, emp);
    }

    writeWorkbookToFile(workbook, currentReportFileEmployeesWorkload);
  }

  private void fillRowWithAvailableData(Row row, EmployeesProjectReportDTO emp) {
    int ctr = 0;
    createNewCell(row, ctr++, emp.getFirstName() + " " + emp.getLastName());
    createNewCell(row, ctr++, emp.getDepartmentName());
    createNewCell(row, ctr++, emp.getProjectName());
    createNewCell(row, ctr, emp.getPositionEndDate().toString());
  }

  private void fillRowWithWorkloadData(Row row, EmployeesProjectReportDTO emp) {
    int ctr = 0;
    createNewCell(row, ctr++, emp.getFirstName() + " " + emp.getLastName());
    createNewCell(row, ctr++, emp.getDepartmentName());
    createNewCell(row, ctr++, emp.getProjectName());
    createNewCell(row, ctr, String.valueOf(emp.getOccupation()));
  }

  private void writeWorkbookToFile(Workbook workbook, File file) throws IOException {
    String fileName = UUID.randomUUID().toString();

    file = new File(fileName + XLSX_EXTENSION);

    FileOutputStream outputStream = new FileOutputStream(file);
    resources.add(outputStream);
    workbook.write(outputStream);
  }

  private void createNewCell(Row row, int ctr, String data) {
    Cell cell = row.createCell(ctr);
    cell.setCellValue(data);
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
