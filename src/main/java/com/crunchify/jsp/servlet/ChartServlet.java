/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crunchify.jsp.servlet;

import edu.co.sergio.mundo.dao.DepartamentoDAO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class ChartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        JFreeChart chart = getChart();
        int width = 500;
        int height = 350;
        ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);

    }

    public JFreeChart getChart() {

        DefaultPieDataset dataset = new DefaultPieDataset();
        //Crear la capa de servicios que se enlace con el DAO
//                dataset.setValue("Ford", 23.3);
//		dataset.setValue("Chevy", 32.4);
//		dataset.setValue("Yugo", 44.2);

        boolean legend = true;
        boolean tooltips = false;
        boolean urls = false;

        ArrayList resultado = new ArrayList();
        DepartamentoDAO dao = new DepartamentoDAO();
        try {
            resultado = dao.consulta1();
        } catch (SQLException ex) {
            Logger.getLogger(ChartServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        int sumatoria = 0;
        for (int i = 0; i < resultado.size(); i++) {
            sumatoria = (Integer) resultado.get(i + 1) + sumatoria;
        }

        for (int i = 0; i < resultado.size(); i++) {
            String nom_Proy = (String) resultado.get(i);
            int total = (Integer) resultado.get(i + 1);
            double porcentaje = total / sumatoria;
            dataset.setValue(nom_Proy, porcentaje);
        }

        JFreeChart chart = ChartFactory.createPieChart("Recursos por Proyecto", dataset, legend, tooltips, urls);

        chart.setBorderPaint(Color.GREEN);
        chart.setBorderStroke(new BasicStroke(5.0f));
        chart.setBorderVisible(true);

        return chart;

    }

}
