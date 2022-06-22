package com.example.restaurantapi.service;

import com.example.restaurantapi.dto.report.OrderReportDto;
import com.example.restaurantapi.model.Order;
import com.example.restaurantapi.model.Report;
import com.example.restaurantapi.model.ReportType;
import com.example.restaurantapi.model.Restaurant;
import com.example.restaurantapi.repository.OrderRepository;
import com.example.restaurantapi.repository.ReportRepository;
import com.example.restaurantapi.repository.RestaurantRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ReportService {
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final ReportRepository reportRepository;

    public ServiceReturn orderReports(int restaurantId) {
        ServiceReturn ret = new ServiceReturn();
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty())
            return ServiceReturn.returnError("Can't find restaurant with given id", 0, restaurantId);

        List<Order> orderList = orderRepository.findAllByRestaurant(restaurantOptional.get());
        if (orderList.isEmpty())
            return  ServiceReturn.returnError("Can't find orders for restaurant with id", 0, restaurantId);

        List<OrderReportDto> orderReportDtos = orderList.stream()
                .map(OrderReportDto::of)
                .collect(Collectors.toList());
        createReportPdf(restaurantOptional.get(), ReportType.ORDER, orderReportDtos);
        ret.setValue(orderReportDtos);
        ret.setStatus(1);
        return ret;
    }

    //TODO tresc i format PDF
    private void createReportPdf(Restaurant restaurant, ReportType type, List<OrderReportDto> reportContent) {

        String formattedDate = new SimpleDateFormat("yyyy_MM_dd").format(new Date().getTime());
        String reportName = restaurant.getName() + "_" + type.name() + "_" + formattedDate + ".pdf";
        Font headerFont = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(reportName));
            Chunk space = new Chunk("\n");
            document.open();
            document.add(new Paragraph("Report name: " + reportName));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(restaurant.getName()));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            //Create table
            PdfPTable table = new PdfPTable(4);
            addTableHeader(table);
            addRows(table, reportContent);
            //create sum
            addRowSum(table, reportContent);
            document.add(table);

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }

        document.close();

    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Lp.","Order no.", "Customer city", "Order price")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<OrderReportDto> reportContent) {
        int lp =1;
        for (OrderReportDto x : reportContent) {
            table.addCell(Integer.toString(lp));
            table.addCell(Integer.toString(x.getOrderNo()));
            table.addCell(x.getCustomerCity());
            table.addCell(Double.toString(x.getPrice()));
            lp++;
        }
    }

    private void addRowSum(PdfPTable table, List<OrderReportDto> reportContent) {
        double total = reportContent.stream()
                .mapToDouble(x -> x.getPrice()).sum();
        table.addCell("Total:");
        table.addCell("");
        table.addCell("");
        table.addCell(Double.toString(total));
        String a = "";
        a.length();
    }




}
