/*package com.example.inovaceifa.Utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelHandler {

    public static void main(String[] args) {
        try {
            // Step 1: Read Data from Excel
            Workbook wb = new XSSFWorkbook("your_excel_file.xlsx");
            Sheet sheet = wb.getSheetAt(0); // Assuming data is in the first sheet

            // Step 2: Process Data
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (Row row : sheet) {
                Cell xCell = row.getCell(0); // Assuming X values are in the first column
                Cell yCell = row.getCell(1); // Assuming Y values are in the second column

                if (xCell != null && yCell != null) {
                    double xValue = xCell.getNumericCellValue();
                    double yValue = yCell.getNumericCellValue();
                    dataset.addValue(yValue, "Data", Double.toString(xValue)); // Add data to the dataset
                }
            }

            // Step 3: Create Graphics
            JFreeChart chart = ChartFactory.createLineChart(
                    "Your Chart Title", // chart title
                    "X Axis Label", // domain axis label
                    "Y Axis Label", // range axis label
                    dataset // data
            );

            // Step 4: Save the Chart as an Image
            FileOutputStream out = new FileOutputStream("chart.png");
            ChartUtilities.writeChartAsPNG(out, chart, 800, 600);

            // Optionally, you can embed the image back into the Excel file (not shown here)

            // Close the workbook and output stream
            wb.close();
            out.close();

            System.out.println("Chart created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/
