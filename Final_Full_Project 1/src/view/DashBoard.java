/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.Color;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import util.DbCon;

/**
 *
 * @author USER
 */
public class DashBoard extends javax.swing.JFrame {

    DbCon con = new DbCon();
    PreparedStatement ps;
    String sql = " ";
    ResultSet rs;

    static float salesDue = 0.0f; // getDue method variable
    static float duePay = 0.00f; // DuePay method variable

    static String namePay = " "; // DuePay method variable

    static String ppName = " ";  // product table name field declear
    static String ppCode = "";

    static Float sttQuantity = 0.00f;

    static String sttName = " ";

    static Float sttDueAmount = 0.00f;

    static Float actualPrice = 0.0f;

    static String custName = " ";
    static String custEmail = " ";
    static String custPhone = " ";
    static String custAddress = " ";

    public DashBoard() {
        initComponents();

        AutoCompleteDecorator.decorate(customName);
        AutoCompleteDecorator.decorate(sName);
        AutoCompleteDecorator.decorate(payName);

        getAllSales();
        getAllProduct();

        getPurchaseComboName();
        getAllPurchase();

        getSelesComboName();
        getAllStockTable();

        todayPurchaseReport();
        monthlyPurchaseReport();

        todaySalesReport();
        monthlySalesReport();

        todayDueReport();
        monthlyDueReport();

        getSelesCustomerName(); // customer name add method

        getCardTable();   // Card Add method 

        getAllCustomer();  // customer data method

        getAllCustomerDueTable();

        getCustomerDueIncreament();

        getAllCustomerDuePay();

        getAllCustomerDueDecreament();

    }

    // Home DashBoard er Calculation Start********************************
    LocalDate currentDate = LocalDate.now();
    java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);

    // purchase today report method create ***********************
    public void todayPurchaseReport() {
        sql = "select sum(purchaseTotalPrice) from purchase where purchaseDate =? ";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, sqlDate);

            rs = ps.executeQuery();

            while (rs.next()) {

                Float totalPrice = rs.getFloat("sum(purchaseTotalPrice)");

                homeTodayPurchase.setText(totalPrice + "");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Monthly Purchase report method create *********************
    public void monthlyPurchaseReport() {

        sql = "select sum(purchaseTotalPrice) from purchase where purchaseDate like ? ";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, sqlDate.toString().substring(0, 8) + "%");

            rs = ps.executeQuery();

            while (rs.next()) {

                Float totalPrice = rs.getFloat("sum(purchaseTotalPrice)");
                homeMonthlyPurchase.setText(totalPrice + " ");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // *** Sales report start *********************
    public void todaySalesReport() {
        sql = "select sum(Actual_Price) from selertable where Date =? ";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, sqlDate);

            rs = ps.executeQuery();

            while (rs.next()) {

                Float totalPrice = rs.getFloat("sum(Actual_Price)");

                homeTodaySales.setText(totalPrice + " ");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Monthly Sales report method create *********************
    public void monthlySalesReport() {

        sql = "select sum(Actual_Price) from selertable where Date like ? ";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, sqlDate.toString().substring(0, 8) + "%");

            rs = ps.executeQuery();

            while (rs.next()) {

                Float totalPrice = rs.getFloat("sum(Actual_Price)");
                homeMonthlySales.setText(totalPrice + " ");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ********* Due report start *********************
    public void todayDueReport() {
        sql = "select sum(Due_Amount) from selertable where Date =? ";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, sqlDate);

            rs = ps.executeQuery();

            while (rs.next()) {

                Float totalPrice = rs.getFloat("sum(Due_Amount)");

                homeTodayDue.setText(totalPrice + " ");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Monthly Due report method create *********************
    public void monthlyDueReport() {

        sql = "select sum(Due_Amount) from selertable where Date like ? ";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, sqlDate.toString().substring(0, 8) + "%");

            rs = ps.executeQuery();

            while (rs.next()) {

                Float totalPrice = rs.getFloat("sum(Due_Amount)");
                homeMonthlyDue.setText(totalPrice + " ");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Reset sales method create
    public void reset() {
        sName.setSelectedItem(null);
        sUnitPrice.setText(null);
        sQuantity.setText(null);
        sTotalPrice.setText(null);
        sDiscount.setText(null);
        sActualPrice1.setText(null);
        sCashReceive.setText(null);
        sDueAmount.setText(null);
        sCashReturn1.setText(null);
        sID.setText(null);
        sDate.setDate(null);
    }

    // table e data show method****************
    String[] selerColumns = {"ID", "customerName", "Seler_Name", "Unit_Price", "Quantity", "Total_Price", "Discount", "Actual_Price", "Cash_Receive", "Due_Amount", "Cash_Return", "Date"};

    public void getAllSales() {
        sql = "select * from selertable";

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(selerColumns);

        cusTable.setModel(model);

        try {

            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int Seler_ID = rs.getInt("ID");
                String customer_Name = rs.getString("customerName");
                String Seler_Name = rs.getString("Seler_Name");
                Float Unit_Price = rs.getFloat("Unit_Price");
                Float Quantity = rs.getFloat("Quantity");
                Float Total_Price = rs.getFloat("Total_Price");
                Float Discount = rs.getFloat("Discount");
                Float Actual_Price = rs.getFloat("Actual_Price");
                Float cash_Receive = rs.getFloat("Cash_Receive");
                Float dueAmount = rs.getFloat("Due_Amount");
                Float cash_Return = rs.getFloat("Cash_Return");
                java.util.Date date = rs.getDate("Date");

                model.addRow(new Object[]{Seler_ID, customer_Name, Seler_Name, Unit_Price, Quantity, Total_Price, Discount, Actual_Price, cash_Receive, dueAmount, cash_Return, date});
            }

            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // calculate total Price 
    public float getTotalPrice() {

        float unitPrice = Float.parseFloat(sUnitPrice.getText().trim());
        float quantity = Float.parseFloat(sQuantity.getText().trim());

        float totalPrice = unitPrice * quantity;

        return totalPrice;
    }

    // Actual Price method
    public float getActualPrice() {

        float totalPrice = getTotalPrice();
        float discount = Float.parseFloat(sDiscount.getText().trim());

        float discountamount = (totalPrice * discount / 100);

        float actualPrice = totalPrice - discountamount;

        return actualPrice;
    }

    // Discount method
    public float getDiscount() {
        return getTotalPrice() - getActualPrice();
    }

    // Date method
    public java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        dHome = new javax.swing.JButton();
        dSales = new javax.swing.JButton();
        dPurchase = new javax.swing.JButton();
        dStock = new javax.swing.JButton();
        dProduct = new javax.swing.JButton();
        dReport = new javax.swing.JButton();
        dCustomer = new javax.swing.JButton();
        dCustomer1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        CusDuePay = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        mainmenu = new javax.swing.JTabbedPane();
        home = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        homeTodayPurchase = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        homeTodaySales = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jPanel49 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        homeTodayDue = new javax.swing.JLabel();
        jPanel45 = new javax.swing.JPanel();
        homeMonthlyPurchase = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        homeMonthlySales = new javax.swing.JLabel();
        jPanel51 = new javax.swing.JPanel();
        homeMonthlyDue = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        cusSearchTable = new javax.swing.JTable();
        jLabel59 = new javax.swing.JLabel();
        searchId = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        sales = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        sQuantity = new javax.swing.JTextField();
        sUnitPrice = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        sDiscount = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        sTotalPrice = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cusTable = new javax.swing.JTable();
        sReset = new javax.swing.JButton();
        sSave = new javax.swing.JButton();
        sDelete = new javax.swing.JButton();
        sUpdate = new javax.swing.JButton();
        sCashReceive = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        sDueAmount = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        sDate = new com.toedter.calendar.JDateChooser();
        sName = new javax.swing.JComboBox<>();
        sActualPrice1 = new javax.swing.JTextField();
        sCashReturn1 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        sPrint = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        card = new javax.swing.JTextPane();
        jLabel56 = new javax.swing.JLabel();
        customName = new javax.swing.JComboBox<>();
        purchase = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        prID = new javax.swing.JTextField();
        prUnitPrice = new javax.swing.JTextField();
        prCompanyName = new javax.swing.JTextField();
        prQuantity = new javax.swing.JTextField();
        prTotalPrice = new javax.swing.JTextField();
        PrCashPay = new javax.swing.JTextField();
        prDueAmount = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        prTable = new javax.swing.JTable();
        prReset = new javax.swing.JButton();
        prSave = new javax.swing.JButton();
        prUpdate = new javax.swing.JButton();
        prDelete = new javax.swing.JButton();
        prName = new javax.swing.JComboBox<>();
        prDate = new com.toedter.calendar.JDateChooser();
        stock = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        stQuantity = new javax.swing.JTextField();
        stID = new javax.swing.JTextField();
        stName = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        stTable = new javax.swing.JTable();
        jPanel39 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        stReset = new javax.swing.JButton();
        stSave = new javax.swing.JButton();
        stDelete = new javax.swing.JButton();
        stUpdate = new javax.swing.JButton();
        stQuantity1 = new javax.swing.JTextField();
        product = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        pCode = new javax.swing.JTextField();
        pID = new javax.swing.JTextField();
        pName = new javax.swing.JTextField();
        pReset = new javax.swing.JButton();
        pSave = new javax.swing.JButton();
        pUpdate = new javax.swing.JButton();
        pDelete = new javax.swing.JButton();
        pCategory = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        pTable = new javax.swing.JTable();
        jPanel19 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        report = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        reFromDate = new com.toedter.calendar.JDateChooser();
        reToDate = new com.toedter.calendar.JDateChooser();
        reStock = new javax.swing.JRadioButton();
        rePurchase = new javax.swing.JRadioButton();
        reSales = new javax.swing.JRadioButton();
        reView = new javax.swing.JButton();
        jPanel41 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        jPanel43 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        reportTotal = new javax.swing.JTextPane();
        customer = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        cusDate = new com.toedter.calendar.JDateChooser();
        cusEmail = new javax.swing.JTextField();
        cusID = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        cusAddress = new javax.swing.JTextArea();
        cusName = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        customTable = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        btnCustomerSubmit = new javax.swing.JButton();
        cusPhone = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        cusDueUpdate = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        cuDueUpdate = new javax.swing.JTable();
        cusDuePay = new javax.swing.JPanel();
        jPanel53 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        payId = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        payNumber = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        payDate = new com.toedter.calendar.JDateChooser();
        jLabel73 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        payAddress = new javax.swing.JTextArea();
        payEmail = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        payDue = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        payTable = new javax.swing.JTable();
        paySubmit = new javax.swing.JButton();
        payName = new javax.swing.JComboBox<>();
        paySubmit1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(153, 205, 242));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dHome.setBackground(new java.awt.Color(153, 153, 153));
        dHome.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        dHome.setText("Home");
        dHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dHomeMouseExited(evt);
            }
        });
        dHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dHomeActionPerformed(evt);
            }
        });
        jPanel2.add(dHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 180, 50));

        dSales.setBackground(new java.awt.Color(153, 153, 153));
        dSales.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        dSales.setText("Sales");
        dSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dSalesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dSalesMouseExited(evt);
            }
        });
        dSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dSalesActionPerformed(evt);
            }
        });
        jPanel2.add(dSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 180, 50));

        dPurchase.setBackground(new java.awt.Color(153, 153, 153));
        dPurchase.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        dPurchase.setText("Purchase");
        dPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dPurchaseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dPurchaseMouseExited(evt);
            }
        });
        dPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dPurchaseActionPerformed(evt);
            }
        });
        jPanel2.add(dPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 180, 50));

        dStock.setBackground(new java.awt.Color(153, 153, 153));
        dStock.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        dStock.setText("Stock");
        dStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dStockMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dStockMouseExited(evt);
            }
        });
        dStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dStockActionPerformed(evt);
            }
        });
        jPanel2.add(dStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 180, 50));

        dProduct.setBackground(new java.awt.Color(153, 153, 153));
        dProduct.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        dProduct.setText("Product");
        dProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dProductMouseExited(evt);
            }
        });
        dProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dProductActionPerformed(evt);
            }
        });
        jPanel2.add(dProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 180, 50));

        dReport.setBackground(new java.awt.Color(153, 153, 153));
        dReport.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        dReport.setText("Report");
        dReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dReportMouseExited(evt);
            }
        });
        dReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dReportActionPerformed(evt);
            }
        });
        jPanel2.add(dReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 180, 50));

        dCustomer.setBackground(new java.awt.Color(153, 153, 153));
        dCustomer.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        dCustomer.setText("Customer Due");
        dCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dCustomerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dCustomerMouseExited(evt);
            }
        });
        dCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dCustomerActionPerformed(evt);
            }
        });
        jPanel2.add(dCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 530, 180, 50));

        dCustomer1.setBackground(new java.awt.Color(153, 153, 153));
        dCustomer1.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        dCustomer1.setText(" Customer Reg");
        dCustomer1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dCustomer1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dCustomer1MouseExited(evt);
            }
        });
        dCustomer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dCustomer1ActionPerformed(evt);
            }
        });
        jPanel2.add(dCustomer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 180, 50));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assit/mini logo.png"))); // NOI18N
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 140, 110));

        CusDuePay.setBackground(new java.awt.Color(153, 153, 153));
        CusDuePay.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        CusDuePay.setText("CusDuePay");
        CusDuePay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CusDuePayMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CusDuePayMouseExited(evt);
            }
        });
        CusDuePay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CusDuePayActionPerformed(evt);
            }
        });
        jPanel2.add(CusDuePay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 180, 50));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 660));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel46.setFont(new java.awt.Font("Times New Roman", 3, 48)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("FAMILY POINT SUPER MARKET");
        jPanel3.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 110));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1020, 110));

        jPanel4.setBackground(new java.awt.Color(102, 250, 247));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel44.setBackground(new java.awt.Color(153, 255, 204));
        jPanel44.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel46.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        homeTodayPurchase.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        homeTodayPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        homeTodayPurchase.setText("0.00");
        jPanel46.add(homeTodayPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 260, 60));

        jLabel43.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("TODAY PURCHASE");
        jPanel46.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 60));

        jPanel44.add(jPanel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 260, 140));

        jPanel47.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        homeTodaySales.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        homeTodaySales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        homeTodaySales.setText("0.00");
        jPanel47.add(homeTodaySales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 260, 60));

        jLabel44.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("TODAY SALES");
        jPanel47.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 60));

        jPanel44.add(jPanel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 260, 140));

        jPanel49.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("TODAY DUE");
        jPanel49.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 60));

        homeTodayDue.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        homeTodayDue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        homeTodayDue.setText("0.00");
        jPanel49.add(homeTodayDue, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 260, 60));

        jPanel44.add(jPanel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, 260, 140));

        jPanel45.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        homeMonthlyPurchase.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        homeMonthlyPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        homeMonthlyPurchase.setText("0.00");
        jPanel45.add(homeMonthlyPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 260, 60));

        jLabel49.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("MONTHLY PURCHASE");
        jPanel45.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 60));

        jPanel44.add(jPanel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 260, 140));

        jPanel50.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel51.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("MONTHLY  SALES");
        jPanel50.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 60));

        homeMonthlySales.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        homeMonthlySales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        homeMonthlySales.setText("0.00");
        jPanel50.add(homeMonthlySales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 260, 60));

        jPanel44.add(jPanel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, 260, 140));

        jPanel51.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        homeMonthlyDue.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        homeMonthlyDue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        homeMonthlyDue.setText("0.00");
        jPanel51.add(homeMonthlyDue, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 260, 60));

        jLabel52.setFont(new java.awt.Font("Times New Roman", 1, 26)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("MONTHLY DUE");
        jPanel51.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 60));

        jPanel44.add(jPanel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 180, 260, 140));

        cusSearchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane11.setViewportView(cusSearchTable);

        jPanel44.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 940, 160));

        jLabel59.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel59.setText("ID");
        jPanel44.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, -1));

        searchId.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel44.add(searchId, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 337, 120, 30));

        searchButton.setBackground(new java.awt.Color(204, 204, 204));
        searchButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        searchButton.setText("Search");
        searchButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchButtonMouseClicked(evt);
            }
        });
        jPanel44.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 337, 90, 30));

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeLayout.createSequentialGroup()
                .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
                .addContainerGap())
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainmenu.addTab("tab1", home);

        jPanel5.setBackground(new java.awt.Color(153, 255, 204));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(153, 255, 204));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SALES");
        jPanel6.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 50));

        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 50));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Sale ID");
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 110, 20));

        sID.setEditable(false);
        sID.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        sID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.add(sID, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 200, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setText("Product Name");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 130, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Quantity");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 110, 20));

        sQuantity.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        sQuantity.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                sQuantityFocusLost(evt);
            }
        });
        sQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sQuantityKeyReleased(evt);
            }
        });
        jPanel5.add(sQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 200, -1));

        sUnitPrice.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        sUnitPrice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                sUnitPriceFocusLost(evt);
            }
        });
        jPanel5.add(sUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 200, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setText("Unit Price");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 110, 20));

        sDiscount.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        sDiscount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                sDiscountFocusLost(evt);
            }
        });
        jPanel5.add(sDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 200, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setText("Total Price");
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 120, 20));

        sTotalPrice.setEditable(false);
        sTotalPrice.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        sTotalPrice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sTotalPrice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sTotalPriceMouseClicked(evt);
            }
        });
        jPanel5.add(sTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 200, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setText("Discount");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 110, 20));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setText("Date");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 60, 20));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setText("Actual Price");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 110, 20));

        cusTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        cusTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cusTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(cusTable);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 80, 640, 90));

        sReset.setBackground(new java.awt.Color(153, 153, 0));
        sReset.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        sReset.setText("RESET");
        sReset.setBorder(null);
        sReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sResetMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sResetMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sResetMouseExited(evt);
            }
        });
        jPanel5.add(sReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 490, 80, 30));

        sSave.setBackground(new java.awt.Color(51, 204, 0));
        sSave.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        sSave.setText("SAVE");
        sSave.setBorder(null);
        sSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sSaveMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sSaveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sSaveMouseExited(evt);
            }
        });
        jPanel5.add(sSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 490, 90, 30));

        sDelete.setBackground(new java.awt.Color(255, 51, 51));
        sDelete.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        sDelete.setText("DELETE");
        sDelete.setBorder(null);
        sDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sDeleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sDeleteMouseExited(evt);
            }
        });
        jPanel5.add(sDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 490, 90, 30));

        sUpdate.setBackground(new java.awt.Color(255, 255, 0));
        sUpdate.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        sUpdate.setText("UPDATE");
        sUpdate.setBorder(null);
        sUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sUpdateMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sUpdateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sUpdateMouseExited(evt);
            }
        });
        jPanel5.add(sUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 490, 100, 30));

        sCashReceive.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        sCashReceive.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sCashReceive.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                sCashReceiveFocusLost(evt);
            }
        });
        jPanel5.add(sCashReceive, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 200, -1));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setText("Due Amount");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 110, 20));

        sDueAmount.setEditable(false);
        sDueAmount.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        sDueAmount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.add(sDueAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 360, 200, -1));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel13.setText("Custo Name");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 130, 20));
        jPanel5.add(sDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 200, 30));

        sName.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jPanel5.add(sName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 200, -1));

        sActualPrice1.setEditable(false);
        sActualPrice1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        sActualPrice1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.add(sActualPrice1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 300, 200, -1));

        sCashReturn1.setEditable(false);
        sCashReturn1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        sCashReturn1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.add(sCashReturn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 390, 200, -1));

        jLabel41.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel41.setText("Cash Receive");
        jPanel5.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 110, 20));

        jLabel42.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Product List");
        jPanel5.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 45, 640, -1));

        jLabel45.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Customer List");
        jPanel5.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 650, 30));

        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerTableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(customerTable);

        jPanel5.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, 640, 90));

        sPrint.setBackground(new java.awt.Color(204, 204, 0));
        sPrint.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        sPrint.setText("PRINT");
        sPrint.setBorder(null);
        sPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sPrintMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sPrintMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sPrintMouseExited(evt);
            }
        });
        jPanel5.add(sPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 490, 80, 30));

        jButton1.setBackground(new java.awt.Color(102, 255, 0));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setText("Add To Card");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 490, -1, 30));

        jScrollPane7.setViewportView(card);

        jPanel5.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 300, 640, 170));

        jLabel56.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel56.setText("Cash Return");
        jPanel5.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 110, 20));

        customName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel5.add(customName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 430, 200, -1));

        javax.swing.GroupLayout salesLayout = new javax.swing.GroupLayout(sales);
        sales.setLayout(salesLayout);
        salesLayout.setHorizontalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        salesLayout.setVerticalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainmenu.addTab("tab2", sales);

        jPanel11.setBackground(new java.awt.Color(153, 255, 204));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel17.setText("Date");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 150, 30));

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel18.setText("Purchase ID");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 150, 30));

        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel19.setText("Purchase Name");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 150, 30));

        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel30.setText("Unit Price");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 150, 30));

        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel20.setText("Company Name");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 150, 30));

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel31.setText("Quantity");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 150, 30));

        jPanel26.setBackground(new java.awt.Color(153, 255, 204));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Purchase");
        jPanel26.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 1040, 60));

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 120, 30));

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 120, 30));

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 120, 30));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 120, 30));

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 120, 30));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, -1, -1));

        jPanel11.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 1050, 70));

        jPanel35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel33.setText("Due Amount");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 150, 30));

        jPanel34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel32.setText("Total Price");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 150, 30));

        jPanel33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel16.setText("Cash Pay");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, 150, 30));

        prID.setEditable(false);
        jPanel11.add(prID, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 150, 30));

        prUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                prUnitPriceFocusLost(evt);
            }
        });
        jPanel11.add(prUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, 150, 30));
        jPanel11.add(prCompanyName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 150, 30));

        prQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                prQuantityFocusLost(evt);
            }
        });
        jPanel11.add(prQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 290, 150, 30));

        prTotalPrice.setEditable(false);
        jPanel11.add(prTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, 150, 30));

        PrCashPay.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                PrCashPayFocusLost(evt);
            }
        });
        jPanel11.add(PrCashPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 370, 150, 30));

        prDueAmount.setEditable(false);
        jPanel11.add(prDueAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 410, 150, 30));

        prTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        prTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(prTable);

        jPanel11.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 90, 600, 340));

        prReset.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        prReset.setText("Reset");
        prReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prResetMouseClicked(evt);
            }
        });
        jPanel11.add(prReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 470, -1, -1));

        prSave.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        prSave.setText("Save");
        prSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prSaveMouseClicked(evt);
            }
        });
        jPanel11.add(prSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 470, 80, -1));

        prUpdate.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        prUpdate.setText("Update");
        prUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prUpdateMouseClicked(evt);
            }
        });
        jPanel11.add(prUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 470, -1, -1));

        prDelete.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        prDelete.setText("Delete");
        prDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prDeleteMouseClicked(evt);
            }
        });
        jPanel11.add(prDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 470, -1, -1));

        jPanel11.add(prName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 150, 30));
        jPanel11.add(prDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 150, 30));

        javax.swing.GroupLayout purchaseLayout = new javax.swing.GroupLayout(purchase);
        purchase.setLayout(purchaseLayout);
        purchaseLayout.setHorizontalGroup(
            purchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        purchaseLayout.setVerticalGroup(
            purchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainmenu.addTab("tab3", purchase);

        jPanel13.setBackground(new java.awt.Color(153, 255, 204));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Stock");
        jPanel13.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 80));

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel36.setText("Stock ID");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel13.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 160, 40));

        jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel34.setText("Stock Name");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel13.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 160, -1));

        jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel35.setText("Stock Quantity");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel13.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, 160, -1));

        stQuantity.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jPanel13.add(stQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 330, 200, 40));

        stID.setEditable(false);
        stID.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jPanel13.add(stID, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 200, 40));

        stName.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jPanel13.add(stName, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 200, 200, 40));

        stTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(stTable);

        jPanel13.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 140, 500, 240));

        jLabel37.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel37.setText("Stock Code");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel39Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel13.add(jPanel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, -1, -1));

        stReset.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stReset.setText("Reset");
        jPanel13.add(stReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 450, 110, 40));

        stSave.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stSave.setText("Save");
        jPanel13.add(stSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 450, 110, 40));

        stDelete.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stDelete.setText("Delete");
        jPanel13.add(stDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 450, 110, 40));

        stUpdate.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        stUpdate.setText("Update");
        jPanel13.add(stUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 450, 110, 40));

        stQuantity1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jPanel13.add(stQuantity1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, 200, 40));

        javax.swing.GroupLayout stockLayout = new javax.swing.GroupLayout(stock);
        stock.setLayout(stockLayout);
        stockLayout.setHorizontalGroup(
            stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        stockLayout.setVerticalGroup(
            stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
        );

        mainmenu.addTab("tab4", stock);

        jPanel9.setBackground(new java.awt.Color(153, 255, 204));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(153, 255, 204));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("PRODUCT");
        jPanel10.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 80));

        jPanel9.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 60));

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Product ID");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 42, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 166, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel9.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 150, 40));

        pCode.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel9.add(pCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 350, 190, 40));

        pID.setEditable(false);
        pID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel9.add(pID, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 190, 40));

        pName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel9.add(pName, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 200, 190, 40));

        pReset.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        pReset.setText("RESET");
        pReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pResetMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pResetMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pResetMouseExited(evt);
            }
        });
        jPanel9.add(pReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 440, 110, 40));

        pSave.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        pSave.setText("SAVE");
        pSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pSaveMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pSaveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pSaveMouseExited(evt);
            }
        });
        jPanel9.add(pSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 440, 110, 40));

        pUpdate.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        pUpdate.setText("UPDATE");
        pUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pUpdateMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pUpdateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pUpdateMouseExited(evt);
            }
        });
        jPanel9.add(pUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 440, 130, 40));

        pDelete.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        pDelete.setText("DELETE");
        pDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pDeleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pDeleteMouseExited(evt);
            }
        });
        jPanel9.add(pDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 440, 130, 40));

        pCategory.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        pCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fish", "Fruits", "Vegetable", "Meat", "Milk", "Personal Care", "Dettol Handwash", "Veet Normal Skin", "Fresh Toilet", "Fresh Kitchen", "CloseUp Tooth Past", "Sunsilk Hair Fall", "Dove Hair Fall", "Blue Lady", "Golden Rose", "Flormar Lips", "Nivea Cream 30", "Imperial Leather Soap", "Vaseline", "Savlon Mild Soap", " ", " ", " " }));
        jPanel9.add(pCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 280, 190, 40));

        pTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        pTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(pTable);

        jPanel9.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 110, 520, 280));

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Product ID");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Product ID");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Product ID");

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Product ID");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel9.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 150, 40));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Product Name");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 150, 40));

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("Category");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 150, 40));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Product Code");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel9.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 350, -1, -1));

        javax.swing.GroupLayout productLayout = new javax.swing.GroupLayout(product);
        product.setLayout(productLayout);
        productLayout.setHorizontalGroup(
            productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        productLayout.setVerticalGroup(
            productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
        );

        mainmenu.addTab("tab5", product);

        jPanel40.setBackground(new java.awt.Color(132, 221, 188));
        jPanel40.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel38.setBackground(new java.awt.Color(102, 102, 102));
        jLabel38.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Report");
        jPanel40.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 60));
        jPanel40.add(reFromDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 210, 30));
        jPanel40.add(reToDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 110, 220, 30));

        buttonGroup1.add(reStock);
        reStock.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        reStock.setText("STOCK");
        jPanel40.add(reStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 190, -1, -1));

        buttonGroup1.add(rePurchase);
        rePurchase.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        rePurchase.setText("PURCHASE");
        jPanel40.add(rePurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        buttonGroup1.add(reSales);
        reSales.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        reSales.setText("SALES");
        jPanel40.add(reSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 190, -1, -1));

        reView.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        reView.setText("VIEW");
        reView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        reView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reViewMouseClicked(evt);
            }
        });
        jPanel40.add(reView, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 190, 70, 30));

        jLabel40.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("TO");

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel40.add(jPanel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 90, 30));

        jLabel39.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("FROM");

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel40.add(jPanel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(reportTable);

        jPanel40.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 980, 170));

        jPanel43.setBackground(new java.awt.Color(102, 102, 102));
        jPanel43.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel40.add(jPanel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 60));

        jScrollPane8.setViewportView(reportTotal);

        jPanel40.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 980, 80));

        javax.swing.GroupLayout reportLayout = new javax.swing.GroupLayout(report);
        report.setLayout(reportLayout);
        reportLayout.setHorizontalGroup(
            reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        reportLayout.setVerticalGroup(
            reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainmenu.addTab("tab6", report);

        jPanel48.setBackground(new java.awt.Color(133, 222, 177));
        jPanel48.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Email");
        jPanel48.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 170, 30));

        jLabel48.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel48.setText("Date ");
        jPanel48.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 170, 30));

        jLabel50.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel50.setText("Customer Name");
        jPanel48.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 170, 30));

        jLabel53.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel53.setText("Address");
        jPanel48.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 170, 40));

        jLabel54.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel54.setText("Phone Number");
        jPanel48.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 170, 30));

        jLabel55.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel55.setText("Customer ID");
        jPanel48.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 170, 30));

        cusDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cusDate.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jPanel48.add(cusDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 280, -1));

        cusEmail.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cusEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel48.add(cusEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, 280, 30));

        cusID.setEditable(false);
        cusID.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cusID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel48.add(cusID, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 280, 30));

        cusAddress.setColumns(20);
        cusAddress.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        cusAddress.setRows(5);
        cusAddress.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane9.setViewportView(cusAddress);

        jPanel48.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, 280, 70));

        cusName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cusName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel48.add(cusName, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 180, 280, 30));

        customTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        customTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customTableMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(customTable);

        jPanel48.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, 480, 270));

        jButton3.setBackground(new java.awt.Color(255, 204, 0));
        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        jButton3.setText("UPDATE");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel48.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 390, 120, 50));

        btnCustomerSubmit.setBackground(new java.awt.Color(102, 204, 0));
        btnCustomerSubmit.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        btnCustomerSubmit.setText("SUBMIT");
        btnCustomerSubmit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnCustomerSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomerSubmitMouseClicked(evt);
            }
        });
        jPanel48.add(btnCustomerSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 390, 120, 50));

        cusPhone.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cusPhone.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel48.add(cusPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 300, 280, 30));

        jLabel57.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText("CUSTOMER REGISTRATION");
        jPanel48.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1050, 70));

        javax.swing.GroupLayout customerLayout = new javax.swing.GroupLayout(customer);
        customer.setLayout(customerLayout);
        customerLayout.setHorizontalGroup(
            customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerLayout.createSequentialGroup()
                .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, 1049, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        customerLayout.setVerticalGroup(
            customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainmenu.addTab("tab7", customer);

        jPanel52.setBackground(new java.awt.Color(153, 255, 204));
        jPanel52.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel60.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("CUSTOMER DUE");
        jPanel52.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 60));

        cuDueUpdate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane13.setViewportView(cuDueUpdate);

        jPanel52.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1000, 400));

        javax.swing.GroupLayout cusDueUpdateLayout = new javax.swing.GroupLayout(cusDueUpdate);
        cusDueUpdate.setLayout(cusDueUpdateLayout);
        cusDueUpdateLayout.setHorizontalGroup(
            cusDueUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cusDueUpdateLayout.setVerticalGroup(
            cusDueUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainmenu.addTab("tab8", cusDueUpdate);

        jPanel53.setBackground(new java.awt.Color(204, 255, 204));
        jPanel53.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel53MouseClicked(evt);
            }
        });
        jPanel53.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel68.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("Customer Due Pay");
        jPanel53.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 1020, 60));

        jLabel69.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("PayId");
        jPanel53.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 100, 30));

        payId.setEditable(false);
        payId.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jPanel53.add(payId, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 170, 30));

        jLabel70.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("CustomerPayName");
        jPanel53.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 230, 30));

        jLabel71.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("CustomerPhoneNumber");
        jPanel53.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 270, 30));

        payNumber.setEditable(false);
        payNumber.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jPanel53.add(payNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 220, 170, 30));

        jLabel72.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("Address");
        jPanel53.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 170, 110, 30));

        payDate.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jPanel53.add(payDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 100, 260, 30));

        jLabel73.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText("Date");
        jPanel53.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 100, 90, 30));

        payAddress.setEditable(false);
        payAddress.setColumns(20);
        payAddress.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        payAddress.setRows(5);
        payAddress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                payAddressMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(payAddress);

        jPanel53.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 150, 260, 90));

        payEmail.setEditable(false);
        payEmail.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jPanel53.add(payEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 270, 170, 30));

        jLabel74.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel74.setText("CustomerEmail");
        jPanel53.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 180, 30));

        payDue.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        payDue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                payDueFocusLost(evt);
            }
        });
        jPanel53.add(payDue, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 260, 260, 30));

        jLabel75.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel75.setText("CustomerDue");
        jPanel53.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 260, 180, 30));

        payTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane15.setViewportView(payTable);

        jPanel53.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 980, 160));

        paySubmit.setBackground(new java.awt.Color(119, 185, 84));
        paySubmit.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        paySubmit.setText("Submit");
        paySubmit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        paySubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paySubmitMouseClicked(evt);
            }
        });
        jPanel53.add(paySubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 320, 90, 40));

        payName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        payName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                payNameMouseClicked(evt);
            }
        });
        jPanel53.add(payName, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, 170, 30));

        paySubmit1.setBackground(new java.awt.Color(119, 185, 84));
        paySubmit1.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        paySubmit1.setText("Reset");
        paySubmit1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        paySubmit1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paySubmit1MouseClicked(evt);
            }
        });
        jPanel53.add(paySubmit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 320, 90, 40));

        javax.swing.GroupLayout cusDuePayLayout = new javax.swing.GroupLayout(cusDuePay);
        cusDuePay.setLayout(cusDuePayLayout);
        cusDuePayLayout.setHorizontalGroup(
            cusDuePayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cusDuePayLayout.setVerticalGroup(
            cusDuePayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainmenu.addTab("tab9", cusDuePay);

        jPanel4.add(mainmenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 1020, 580));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 1020, 550));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dHomeActionPerformed
        // TODO add your handling code here:
        mainmenu.setSelectedIndex(0);
    }//GEN-LAST:event_dHomeActionPerformed

    private void dSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dSalesActionPerformed
        // TODO add your handling code here:
        mainmenu.setSelectedIndex(1);
    }//GEN-LAST:event_dSalesActionPerformed

    private void dPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dPurchaseActionPerformed
        // TODO add your handling code here:
        mainmenu.setSelectedIndex(2);
    }//GEN-LAST:event_dPurchaseActionPerformed

    private void dStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dStockActionPerformed
        // TODO add your handling code here:
        mainmenu.setSelectedIndex(3);
        getAllStockTable();
    }//GEN-LAST:event_dStockActionPerformed

    private void dProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dProductActionPerformed
        // TODO add your handling code here:
        mainmenu.setSelectedIndex(4);
    }//GEN-LAST:event_dProductActionPerformed

    private void dReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dReportActionPerformed
        // TODO add your handling code here:
        mainmenu.setSelectedIndex(5);
    }//GEN-LAST:event_dReportActionPerformed

// unit price focus lost
    private void sUnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sUnitPriceFocusLost
        // TODO add your handling code here:
        try {
            if (!sUnitPrice.getText().trim().isEmpty()) {
                // your code when the text field is not empty
            } else {
                JOptionPane.showMessageDialog(rootPane, "Unit Price cannot be Empty");
                sUnitPrice.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "An error occurred :" + e.getMessage());
        }
    }//GEN-LAST:event_sUnitPriceFocusLost

    // quantity price focus lost
    private void sQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sQuantityFocusLost
        // TODO add your handling code here:    

        sql = "select stockQuantity from stock where stockName=?";

//        sName.removeAllItems();

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, sName.getSelectedItem().toString());
            
            rs = ps.executeQuery();

            while (rs.next()) {

                float quantit = Float.parseFloat(rs.getString("stockQuantity"));
                float stock = Float.parseFloat(sQuantity.getText().toString());

                if (sUnitPrice.getText().trim().isEmpty()) {
                    sUnitPrice.requestFocus();
                } 
             else if(sQuantity.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(rootPane, "Quantity  Empty");
                     sQuantity.requestFocus();               
            }
             
                else if ( stock <=0) {
//                sTotalPrice.setText(getTotalPrice() + " ");
                    JOptionPane.showMessageDialog(rootPane, "Not Allow");
                    sQuantity.requestFocus();
                }
             
                else if (!sQuantity.getText().trim().isEmpty() && quantit <= stock ) {
//                sTotalPrice.setText(getTotalPrice() + " ");
                    JOptionPane.showMessageDialog(rootPane, "Stock Not Avalaible");
                    sQuantity.requestFocus();
                }
                else if (!sQuantity.getText().trim().isEmpty() && quantit >= stock ) {
                    sTotalPrice.setText(getTotalPrice() + " ");
                }                                 
            }

            ps.close();
            rs.close();
            con.getCon().close();
            getAllStockToSeles();
        }
            
         catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

//        
//        try {
//            if (sUnitPrice.getText().trim().isEmpty()) {
//                sUnitPrice.requestFocus();
//            } else if (!sQuantity.getText().trim().isEmpty()) {
//                sTotalPrice.setText(getTotalPrice() + " ");
//            } else {
//                JOptionPane.showMessageDialog(rootPane, "Quantity can not be Empty");
//                sQuantity.requestFocus();
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(rootPane, "An error :" + e.getMessage());
//        }
       
        
    }//GEN-LAST:event_sQuantityFocusLost

    // discount fosus lost
    private void sDiscountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sDiscountFocusLost
        // TODO add your handling code here:
        float actualPrice = getActualPrice();
        sActualPrice1.setText(actualPrice + " ");

    }//GEN-LAST:event_sDiscountFocusLost

    // stock method create Seles add contain to stock
    public void getAllStockToSeles() {
        sql = "update stock set stockQuantity = stockQuantity - ? where stockName=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setFloat(1, sttQuantity);
            ps.setString(2, sttName);

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            //      JOptionPane.showMessageDialog(prCompanyName, "Stock Data is  Decreament");
        } catch (SQLException ex) {
            //      JOptionPane.showMessageDialog(prCompanyName, "Stock Data is Not Decreament");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Seles combo field er method
    public void getSelesComboName() {

        sql = "select * from stock";

        sName.removeAllItems();

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String nasme = rs.getString("stockName");
                float quantit = Float.parseFloat(rs.getString("stockQuantity"));

                if (quantit <= 0) {
                    sName.addItem(nasme + "( xxx)");
                }
//                else if(){
//                
//                }
//                else if (quantit >= 0) {
//                    sName.addItem(nasme + " ( " + quantit + " )" );
//                } 
                else {

                    sName.addItem(nasme);
                }

            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    // Seles combo field er method   Customer Name add Customer table from sales table
    public void getSelesCustomerName() {

        sql = "select customerName from customer";

        customName.removeAllItems();

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("customerName");
                customName.addItem(name);
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getAllStockZero() {

        sql = "select quantity from stock where ";

    }

    // Save method create
    private void sSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sSaveMouseClicked

        sql = "insert into selertable( Seler_Name, Unit_Price, Quantity, Total_Price, Discount, Actual_Price,  Cash_Receive, Due_Amount, Cash_Return, customerName, Date ) values (?,?,?,?,?,?,?,?,?,?,?)";

        sttQuantity = Float.valueOf(sQuantity.getText());
        sttName = sName.getSelectedItem().toString();

//       Float.parseFloat(sDueAmount.getText())
        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, sttName);
            ps.setFloat(2, Float.parseFloat(sUnitPrice.getText()));
            ps.setFloat(3, sttQuantity);
            ps.setFloat(4, getTotalPrice());
            ps.setFloat(5, getDiscount());
            ps.setFloat(6, getActualPrice());
            ps.setFloat(7, Float.parseFloat(sCashReceive.getText()));
            ps.setFloat(8, getDue());
            ps.setFloat(9, Float.parseFloat(sCashReturn1.getText()));

            ps.setString(10, (String) customName.getSelectedItem());

            ps.setDate(11, convertUtilDateToSqlDate(sDate.getDate()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Successfully");

            //****************  Add to Card  Start ****************************
            DefaultTableModel model = (DefaultTableModel) customerTable.getModel();

            String customer = customName.getSelectedItem().toString();
            String productName = sName.getSelectedItem().toString();
            Float unitPrice = Float.parseFloat(sUnitPrice.getText());
            Float quantity = Float.parseFloat(sQuantity.getText());
            Float totalPrice = Float.parseFloat(sTotalPrice.getText());
            Float discount = getDiscount();
            Float actualPrice = getActualPrice();
            Float cashPay = Float.parseFloat(sCashReceive.getText());

            java.util.Date date = convertUtilDateToSqlDate(sDate.getDate());

            List<Object> productList = new ArrayList<>();

            productList.add(new Object[]{customer, productName, unitPrice, quantity, totalPrice, discount, actualPrice, cashPay, date});

            int row = model.getRowCount();

            for (Object i : productList) {
                model.insertRow(row, (Object[]) i);
            }

            //   JOptionPane.showMessageDialog(prCompanyName, "Add to Card");
            //**********************  Add to Card  end   **********************
            reset();
            getAllSales();

            getAllStockTable();
            getAllStockToSeles();

            todaySalesReport();
            monthlySalesReport();

            todayDueReport();
            monthlyDueReport();

            todayPurchaseReport();
            monthlyPurchaseReport();

            getCustomerDueIncreament();  //  customer Due Increament Table 

            getCustomerToDueName();
            getSelesComboName();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data Not Successfully");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_sSaveMouseClicked

    // sales Delete method create ******************
    private void sDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sDeleteMouseClicked
        // TODO add your handling code here:
        sql = "delete from selertable where ID=?";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(sID.getText()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Deleted");
            reset();
            getAllSales();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data not Delete");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_sDeleteMouseClicked

    // UPdate method create
    private void sUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update selertable set Seler_Name=?, Unit_price=?, Quantity=?, Total_Price=?, Discount=?, Actual_Price=? where ID=?";
        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, sName.getSelectedItem().toString());
            ps.setFloat(2, Float.parseFloat(sUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(sQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(sTotalPrice.getText().trim()));
            ps.setFloat(5, Float.parseFloat(sDiscount.getText().trim()));
            ps.setFloat(6, Float.parseFloat(sActualPrice1.getText().trim()));
            ps.setInt(7, Integer.parseInt(sID.getText().toString()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Updated");
            reset();
            getAllSales();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data not Update");

            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_sUpdateMouseClicked

    private void sCashReceiveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sCashReceiveFocusLost
        // TODO add your handling code here:
        float actualPrice = getActualPrice();
        float cashReceive = Float.parseFloat(sCashReceive.getText().trim());
        float cashReturn = actualPrice - cashReceive;

        if (cashReturn > 0) {
            sDueAmount.setText(cashReturn + " ");
            sCashReturn1.setText(00 + "");
        } else {
            sCashReturn1.setText(cashReturn + " ");
            sDueAmount.setText(00 + "");
        }
    }//GEN-LAST:event_sCashReceiveFocusLost

    //  product list clicked system create *******************************
    private void cusTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cusTableMouseClicked

        int rowIndex = cusTable.getSelectedRow();

        String Seller_ID = cusTable.getModel().getValueAt(rowIndex, 0).toString();
        String customerNam = cusTable.getModel().getValueAt(rowIndex, 1).toString();
        String Seller_Name = cusTable.getModel().getValueAt(rowIndex, 2).toString();
        String unit_Price = cusTable.getModel().getValueAt(rowIndex, 3).toString();
        String Quantity = cusTable.getModel().getValueAt(rowIndex, 4).toString();
        String Total_Price = cusTable.getModel().getValueAt(rowIndex, 5).toString();
        String Discount = cusTable.getModel().getValueAt(rowIndex, 6).toString();
        String Actual_Price = cusTable.getModel().getValueAt(rowIndex, 7).toString();
        String cash_Receive = cusTable.getModel().getValueAt(rowIndex, 8).toString();
        String dueAmount = cusTable.getModel().getValueAt(rowIndex, 9).toString();
        String cash_Return = cusTable.getModel().getValueAt(rowIndex, 10).toString();
        String date = cusTable.getModel().getValueAt(rowIndex, 11).toString();

        sID.setText(Seller_ID);
        customName.setSelectedItem(customerNam);
        sName.setSelectedItem(Seller_Name);
        sUnitPrice.setText(unit_Price);
        sQuantity.setText(Quantity);
        sTotalPrice.setText(Total_Price);
        sDiscount.setText(Discount);
        sActualPrice1.setText(Actual_Price);
        sCashReceive.setText(cash_Receive);
        sDueAmount.setText(dueAmount);
        sCashReturn1.setText(cash_Return);
        sDate.setDate(convertStringToDate(date));

    }//GEN-LAST:event_cusTableMouseClicked

    private void sResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sResetMouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, "Sales is Reset");
        reset();
    }//GEN-LAST:event_sResetMouseClicked

    private void dHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dHomeMouseEntered
        // TODO add your handling code here:
        dHome.setBackground(new Color(153, 250, 218));
    }//GEN-LAST:event_dHomeMouseEntered

    private void dHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dHomeMouseExited
        // TODO add your handling code here:
        dHome.setBackground(Color.WHITE);

    }//GEN-LAST:event_dHomeMouseExited

    private void dSalesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dSalesMouseEntered
        // TODO add your handling code here:
        dSales.setBackground(new Color(153, 250, 218));
    }//GEN-LAST:event_dSalesMouseEntered

    private void dSalesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dSalesMouseExited
        // TODO add your handling code here:
        dSales.setBackground(Color.WHITE);
    }//GEN-LAST:event_dSalesMouseExited

    private void dPurchaseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dPurchaseMouseEntered
        // TODO add your handling code here:
        dPurchase.setBackground(new Color(153, 250, 218));
    }//GEN-LAST:event_dPurchaseMouseEntered

    private void dPurchaseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dPurchaseMouseExited
        // TODO add your handling code here:
        dPurchase.setBackground(Color.WHITE);
    }//GEN-LAST:event_dPurchaseMouseExited

    private void dStockMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dStockMouseEntered
        // TODO add your handling code here:
        dStock.setBackground(new Color(153, 250, 218));
    }//GEN-LAST:event_dStockMouseEntered

    private void dStockMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dStockMouseExited
        // TODO add your handling code here:
        dStock.setBackground(Color.WHITE);
    }//GEN-LAST:event_dStockMouseExited

    private void dProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dProductMouseEntered
        // TODO add your handling code here:
        dProduct.setBackground(new Color(153, 250, 218));
    }//GEN-LAST:event_dProductMouseEntered

    private void dProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dProductMouseExited
        // TODO add your handling code here:
        dProduct.setBackground(Color.WHITE);
    }//GEN-LAST:event_dProductMouseExited

    private void dReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dReportMouseEntered
        // TODO add your handling code here:
        dReport.setBackground(new Color(153, 250, 218));

    }//GEN-LAST:event_dReportMouseEntered

    private void dReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dReportMouseExited
        // TODO add your handling code here:
        dReport.setBackground(Color.WHITE);
    }//GEN-LAST:event_dReportMouseExited

    // colponick stock table create 
    String[] stockColumns = {"stockID", "stockName", "stockQuantity", "stockCode"};

    public void getAllStockTable() {
        sql = "select * from stock";
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(stockColumns);
        stTable.setModel(model);
        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int ID = rs.getInt("stockID");
                String name = rs.getString("stockName");
                Float quantity = rs.getFloat("stockQuantity");
                String code = rs.getString("stockCode");
                model.addRow(new Object[]{ID, name, quantity, code});
            }
            ps.executeQuery();
            ps.close();
            con.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // purchase barar jonno method*************
    public void addProductToStock() {
        sql = " insert into stock (stockName, stockQuantity, stockCode) values (?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, ppName);
            ps.setFloat(2, 0.0f);
            ps.setString(3, ppCode);

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Product method Reset****************
    public void reset1() {

        pName.setText(null);
        pCategory.setSelectedItem(null);
        pCode.setText(null);

    }

    // product method create******************
    String[] productColumns = {"Product_Id", "Product_Name", "Product_Category", "Product_Code"};

    public void getAllProduct() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(productColumns);
        pTable.setModel(model);
        sql = "select * from product";

        try {
            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Product_Id");
                String pName = rs.getString("Product_Name");
                String pCategory = rs.getString("Product_Category");
                String pCode = rs.getString("Product_Code");

                model.addRow(new Object[]{id, pName, pCategory, pCode});
            }

            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // product insert play*************
    private void pSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pSaveMouseClicked
        // TODO add your handling code here:
        sql = "insert into product (Product_Name, Product_Category, Product_Code) values (?,?,?)";

        ppName = pName.getText().trim();   // variable theke data asche
        ppCode = pCode.getText().trim();
        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, pName.getText().trim());
            ps.setString(2, pCategory.getSelectedItem().toString());
            ps.setString(3, pCode.getText().trim());

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Product is Save");

            getAllProduct();  // create method theke asche
            reset1();
            getPurchaseComboName(); // pruchase method theke asche
            addProductToStock();  // data pass korar jonno method
            getSelesComboName();
            getAllStockTable();    // stock table

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Product is Not Save");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pSaveMouseClicked


    private void pTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pTableMouseClicked
        // TODO add your handling code here:
        int rowIndex = pTable.getSelectedRow();

        String id = pTable.getModel().getValueAt(rowIndex, 0).toString();
        String Name = pTable.getModel().getValueAt(rowIndex, 1).toString();
        String Category = pTable.getModel().getValueAt(rowIndex, 2).toString();
        String Code = pTable.getModel().getValueAt(rowIndex, 3).toString();

        pID.setText(id);
        pName.setText(Name);
        pCategory.setSelectedItem(Category);
        pCode.setText(Code);

    }//GEN-LAST:event_pTableMouseClicked

    private void pDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pDeleteMouseClicked
        // TODO add your handling code here:
        sql = " delete from product where Product_ID=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(pID.getText().trim()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Product is Delete");
            reset1();
            getAllProduct();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Product is Not Delete");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pDeleteMouseClicked

    private void pUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update product set  Product_Name=?, Product_Category=?, Product_Code=? where Product_Id=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, pName.getText());
            ps.setString(2, pCategory.getSelectedItem().toString());
            ps.setString(3, pCode.getText().trim());
            ps.setInt(4, Integer.parseInt(pID.getText()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Product is Update");
            getAllProduct();
            reset1();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Product is Not Update");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pUpdateMouseClicked

    private void pResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pResetMouseClicked
        // TODO add your handling code here:

        JOptionPane.showMessageDialog(rootPane, "Product is Reset");
        reset1();
    }//GEN-LAST:event_pResetMouseClicked

    private void sSaveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sSaveMouseEntered
        // TODO add your handling code here:
//        sSave.setBackground(new Color(153, 250, 2));
        sSave.setBackground(Color.GREEN);
    }//GEN-LAST:event_sSaveMouseEntered

    private void sSaveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sSaveMouseExited
        // TODO add your handling code here:
        sSave.setBackground(Color.WHITE);
    }//GEN-LAST:event_sSaveMouseExited

    private void sDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sDeleteMouseEntered
        // TODO add your handling code here:
        sDelete.setBackground(Color.red);
    }//GEN-LAST:event_sDeleteMouseEntered

    private void sDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sDeleteMouseExited
        // TODO add your handling code here:
        sDelete.setBackground(Color.WHITE);
    }//GEN-LAST:event_sDeleteMouseExited

    private void sUpdateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sUpdateMouseEntered
        // TODO add your handling code here:
        sUpdate.setBackground(Color.yellow);
    }//GEN-LAST:event_sUpdateMouseEntered

    private void sUpdateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sUpdateMouseExited
        // TODO add your handling code here:
        sUpdate.setBackground(Color.WHITE);
    }//GEN-LAST:event_sUpdateMouseExited

    private void sResetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sResetMouseEntered
        // TODO add your handling code here:
        sReset.setBackground(Color.PINK);
    }//GEN-LAST:event_sResetMouseEntered

    private void sResetMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sResetMouseExited
        // TODO add your handling code here:
        sReset.setBackground(Color.WHITE);
    }//GEN-LAST:event_sResetMouseExited

    // product method botton color change
    private void pSaveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pSaveMouseEntered
        // TODO add your handling code here:
        pSave.setBackground(new Color(153, 250, 2));

    }//GEN-LAST:event_pSaveMouseEntered

    private void pSaveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pSaveMouseExited
        // TODO add your handling code here:
        pSave.setBackground(Color.WHITE);
    }//GEN-LAST:event_pSaveMouseExited

    private void pUpdateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pUpdateMouseEntered
        // TODO add your handling code here:
        pUpdate.setBackground(Color.CYAN);
    }//GEN-LAST:event_pUpdateMouseEntered

    private void pUpdateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pUpdateMouseExited
        // TODO add your handling code here:
        pUpdate.setBackground(Color.WHITE);
    }//GEN-LAST:event_pUpdateMouseExited

    private void pDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pDeleteMouseEntered
        // TODO add your handling code here:
        pDelete.setBackground(Color.red);
    }//GEN-LAST:event_pDeleteMouseEntered

    private void pDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pDeleteMouseExited
        // TODO add your handling code here:
        pDelete.setBackground(Color.WHITE);
    }//GEN-LAST:event_pDeleteMouseExited

    private void pResetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pResetMouseEntered
        // TODO add your handling code here:
        pReset.setBackground(Color.PINK);
    }//GEN-LAST:event_pResetMouseEntered

    private void pResetMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pResetMouseExited
        // TODO add your handling code here:
        pReset.setBackground(Color.WHITE);
    }//GEN-LAST:event_pResetMouseExited

    // purchase combo field er method
    public void getPurchaseComboName() {

        sql = "select Product_Name from product";
        prName.removeAllItems();

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("Product_Name");
                prName.addItem(name);
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // purchase table calculate  ************* 
    public float getTotalPurchase() {
        float pUnitPrice = Float.parseFloat(prUnitPrice.getText().trim());
        float pQuantity = Float.parseFloat(prQuantity.getText().trim());

        float pTotalPrice = pUnitPrice * pQuantity;

        return pTotalPrice;
    }

    // purchase Table calponic table create**********
    String[] purchaseColumns = {"purchaseID", "purchaseName", "purchaseUnitPrice", "purchaseQuantity", "purchaseTotalPrice", "purchaseCashPay", "purchaseDueAmount", "purchaseDate"};

    public void getAllPurchase() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(purchaseColumns);
        prTable.setModel(model);

        sql = "select * from purchase";

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("purchaseID");
                String name = rs.getString("purchaseName");
//                String company = rs.getString("purchaseCompanyName");
                float unitPrice = rs.getFloat("purchaseUnitPrice");
                float quantity = rs.getFloat("purchaseQuantity");

                float totalPrice = rs.getFloat("purchaseTotalPrice");
                float cashPay = rs.getFloat("purchaseCashPay");
                float dueAmount = rs.getFloat("purchaseDueAmount");
                java.util.Date date = rs.getDate("purchaseDate");

                model.addRow(new Object[]{id, name, unitPrice, quantity, totalPrice, cashPay, dueAmount, date});
            }

            ps.executeQuery();
            rs.close();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // stock method create purchase add contain to stock*************
    public void getStock() {
        sql = "update stock set stockQuantity = stockQuantity + ? where stockName=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setFloat(1, Float.parseFloat(prQuantity.getText().trim()));
            ps.setString(2, prName.getSelectedItem().toString());

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(prCompanyName, "Stock Data is Increament");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(prCompanyName, "Stock Data is Not Increament");

            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // purchase method create play
    private void prSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prSaveMouseClicked
        // TODO add your handling code here:

        sql = "insert into purchase (purchaseName, purchaseQuantity, purchaseUnitPrice, purchaseTotalPrice, purchaseDate) values (?,?,?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, prName.getSelectedItem().toString());
            ps.setFloat(2, Float.parseFloat(prQuantity.getText()));
            ps.setFloat(3, Float.parseFloat(prUnitPrice.getText()));
            ps.setFloat(4, Float.parseFloat(prTotalPrice.getText()));
            ps.setDate(5, convertUtilDateToSqlDate(prDate.getDate()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(prCompanyName, "Purchase Data is Save");
            getStock();
            getAllPurchase();
            getAllStockTable();  // stock table
            prReset();
            todayPurchaseReport();
            getSelesComboName();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(prCompanyName, "Purchase Data is not Save");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_prSaveMouseClicked

    private void prUnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prUnitPriceFocusLost
        // TODO add your handling code here:
        try {
            if (!prUnitPrice.getText().trim().isEmpty()) {
                // your code when the text field is not empty
            } else {
                JOptionPane.showMessageDialog(rootPane, "Unit Price cannot be Empty");
                prUnitPrice.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "An error occurred :" + e.getMessage());
        }
    }//GEN-LAST:event_prUnitPriceFocusLost

    private void prQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prQuantityFocusLost
        // TODO add your handling code here:
        try {
            if (prUnitPrice.getText().trim().isEmpty()) {
                prUnitPrice.requestFocus();

            } else if (!prQuantity.getText().trim().isEmpty()) {
                prTotalPrice.setText(getTotalPurchase() + " ");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Quantity can not be Empty");
                prQuantity.requestFocus();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "An error :" + e.getMessage());
        }
    }//GEN-LAST:event_prQuantityFocusLost

    private void PrCashPayFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PrCashPayFocusLost
        // TODO add your handling code here:

        float prActualPrice = getTotalPurchase();
        float prCashReceive = Float.parseFloat(PrCashPay.getText().trim());
        float prCashReturn = prActualPrice - prCashReceive;
        prDueAmount.setText(prCashReturn + " ");


    }//GEN-LAST:event_PrCashPayFocusLost

    // purchase Update create
    private void prUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prUpdateMouseClicked
        // TODO add your handling code here:

        sql = "update purchase set purchaseName=?,  purchaseUnitPrice=?, purchaseQuantity=?, purchaseTotalPrice=?, purchaseCashPay=?, purchaseDueAmount=?, purchaseDate=? where purchaseID=? ";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, prName.getSelectedItem().toString());
//            ps.setString(2, prCompanyName.getText());
            ps.setFloat(3, Float.parseFloat(prUnitPrice.getText()));
            ps.setFloat(4, Float.parseFloat(prQuantity.getText()));
            ps.setFloat(5, Float.parseFloat(prTotalPrice.getText()));
            ps.setFloat(6, Float.parseFloat(PrCashPay.getText()));
            ps.setFloat(7, Float.parseFloat(prDueAmount.getText()));
            ps.setDate(8, convertUtilDateToSqlDate(prDate.getDate()));
            ps.setInt(9, Integer.parseInt(prID.getText()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(prCompanyName, "Purchase Data is Update");
            getAllPurchase();
            prReset();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(prCompanyName, "Purchase Data is not Update");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_prUpdateMouseClicked

    // Date method create **************
    public java.util.Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.err.println("Parsing failed: " + e.getMessage());
            return null; // Handle the exception by returning null
        }
    }

    // ********* Table to field Data pass******

    private void prTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prTableMouseClicked
        // TODO add your handling code here:
        int row = prTable.getSelectedRow();

        String id = prTable.getModel().getValueAt(row, 0).toString();
        String name = prTable.getModel().getValueAt(row, 1).toString();
        //    String companyName = prTable.getModel().getValueAt(row, 2).toString();

        String unitPrice = prTable.getModel().getValueAt(row, 2).toString();
        String quantity = prTable.getModel().getValueAt(row, 3).toString();
        String totalPrice = prTable.getModel().getValueAt(row, 4).toString();
        String cashPay = prTable.getModel().getValueAt(row, 5).toString();
        String dueAmount = prTable.getModel().getValueAt(row, 6).toString();
        String date = prTable.getModel().getValueAt(row, 7).toString();

        prID.setText(id);
        prName.setSelectedItem(name);
        //    prCompanyName.setText(companyName);
        prUnitPrice.setText(quantity);
        prQuantity.setText(unitPrice);
        prTotalPrice.setText(totalPrice);
        PrCashPay.setText(cashPay);
        prDueAmount.setText(dueAmount);
        prDate.setDate(convertStringToDate(date));

    }//GEN-LAST:event_prTableMouseClicked

    private void prDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prDeleteMouseClicked
        // TODO add your handling code here:

        sql = "delete from purchase where purchaseID=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(prID.getText()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(prCompanyName, "Purchase Data is Delete");
            getAllPurchase();
            prReset();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(prCompanyName, "Purchase Data is Not Delete");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_prDeleteMouseClicked

    // **** Reset method create *************
    public void prReset() {

        prID.setText(null);
        prName.setSelectedItem(null);
        prCompanyName.setText(null);
        prUnitPrice.setText(null);
        prQuantity.setText(null);
        prTotalPrice.setText(null);
        PrCashPay.setText(null);
        prDueAmount.setText(null);
        prDate.setDate(null);

    }

    // purchase Table reset method create*****************
    private void prResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prResetMouseClicked
        // TODO add your handling code here:
        prReset();

    }//GEN-LAST:event_prResetMouseClicked

    // report Stock method create play**************
    public void getStockReportByDate(java.util.Date fromDate, java.util.Date toDate) {

        String[] columnNames = {"Stock Product Name", "Stock Product Quantity"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        reportTable.setModel(model);

        sql = "select * from stock ";

        try {
            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("stockName");
                Float quantity = rs.getFloat("stockQuantity");

                model.addRow(new Object[]{name, quantity});
            }

            ps.executeQuery();
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // report purchase method create*************
    public void getPurchaseReportByDate(java.util.Date fromDate, java.util.Date toDate) {

        String[] columnNames = {"Purchase Name", " Purchase Unit Price", "Quantity", "Total Price", "Purchase Cash Pay", "Purchase Due Amount"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        reportTable.setModel(model);

        sql = "select * from purchase where purchaseDate between ? and ? ";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("purchaseName");
                Float unitPrice = rs.getFloat("purchaseUnitPrice");
                Float quantity = rs.getFloat("purchaseQuantity");
                Float total = rs.getFloat("purchaseTotalPrice");
                Float cashPay = rs.getFloat("purchaseCashPay");
                Float due = rs.getFloat("purchaseDueAmount");

                model.addRow(new Object[]{name, unitPrice, quantity, total, cashPay, due});
            }

            ps.executeQuery();
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Report sales method create play***************
    public void getSalesReportByDate(java.util.Date fromDate, java.util.Date toDate) {

        String[] columnNames = {"Product Name", "Unit Price", "Quantity", "Discount", "Total Price", "Due Amount"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        reportTable.setModel(model);

        sql = "select * from selertable where Date between ? and ? ";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("Seler_Name");
                Float unitPrice = rs.getFloat("Unit_Price");
                Float quantity = rs.getFloat("Quantity");
                Float discount = rs.getFloat("Discount");
                Float actualPrice = rs.getFloat("Actual_Price");

                model.addRow(new Object[]{name, unitPrice, quantity, discount, actualPrice});
            }

            ps.executeQuery();
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // report view method create ******************
    private void reViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reViewMouseClicked

        if (rePurchase.isSelected()) {
            getPurchaseReportByDate(reFromDate.getDate(), reToDate.getDate());
        } else if (reSales.isSelected()) {
            getSalesReportByDate(reFromDate.getDate(), reToDate.getDate());
        } else if (reStock.isSelected()) {
            getStockReportByDate(reFromDate.getDate(), reToDate.getDate());
        } else {
            System.out.println(" not ok");
        }

    }//GEN-LAST:event_reViewMouseClicked


    private void customerTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerTableMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_customerTableMouseClicked

// Print total price from cuatomer table **************    
    public float getActualTotalPrice() {

        float ff = 0.0f;

        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        int row = model.getRowCount();

        for (int i = 0; i < row; i++) {
            float aPrice = Float.valueOf(customerTable.getModel().getValueAt(i, 6).toString());
            ff += aPrice;

        }
        return ff;
    }

    // cash Pay method create ****************************   
    public float cashPay() {

        float ff = 0.0f;

        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        int row = model.getRowCount();

        for (int i = 0; i < row; i++) {
            float aPrice = Float.valueOf(customerTable.getModel().getValueAt(i, 7).toString());
            ff += aPrice;
        }
        return ff;
    }

    // Customer k Tk back method create ****************************   
    public float balance() {
        Float balance = 0.0f;

        if (getActualTotalPrice() < cashPay()) {
            balance = cashPay() - getActualTotalPrice();
        } else {
            float bala = getActualTotalPrice() - cashPay();
            balance = bala * (-1);
        }
        return balance;
    }

// Print method create start *************************************************    
    private void sPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sPrintMouseClicked

        try {

            card.setText(card.getText() + "\t\t\t\t F, P, S, M \n");
            card.setText(card.getText() + "\t\t\t Mohammadpur, Dhaka-1207, Bangladesh \n");
            card.setText(card.getText() + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            card.setText(card.getText() + "Cust Name " + "    P Name" + "\t  Unit Price" + "\t  Quantity" + "\tTotal Price" + "\tDiscount" + "\tActual Price" + "\tCash Pay" + "\t  Date" + "\n");
            card.setText(card.getText() + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

            DefaultTableModel model = (DefaultTableModel) customerTable.getModel();  //  Data from customer table ********

            // get table product details
            for (int i = 0; i < model.getRowCount(); i++) {

                String customerName = model.getValueAt(i, 0).toString();
                String name = model.getValueAt(i, 1).toString();
                String unitPrice = model.getValueAt(i, 2).toString();
                String quantity = model.getValueAt(i, 3).toString();
                String totalPrice = model.getValueAt(i, 4).toString();
                String discount = model.getValueAt(i, 5).toString();
                String actualPrice = model.getValueAt(i, 6).toString();
                String cashPay = model.getValueAt(i, 7).toString();
                String date = model.getValueAt(i, 8).toString();

                card.setText(card.getText() + customerName + "\t     " + name + "\t   " + unitPrice + "\t " + quantity + "\t" + totalPrice + "\t" + discount + "\t" + actualPrice + "\t" + cashPay + "\t" + date + "\n");

            }

            card.setText(card.getText() + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

            card.setText(card.getText() + "\tTotal Price\t       :\t" + getActualTotalPrice() + "\n");
            card.setText(card.getText() + "\tCash Pay\t       :\t" + cashPay() + "\n");
            card.setText(card.getText() + "\tBalance Back      :\t" + balance() + "\n");

            card.setText(card.getText() + "----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

            card.setText(card.getText() + "\t\t\t\t Thank you so much \n\n");

            card.print();

        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_sPrintMouseClicked


    private void sPrintMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sPrintMouseEntered
        // TODO add your handling code here:
        sPrint.setBackground(Color.orange);

    }//GEN-LAST:event_sPrintMouseEntered

    private void sPrintMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sPrintMouseExited
        // TODO add your handling code here:
        sPrint.setBackground(Color.WHITE);

    }//GEN-LAST:event_sPrintMouseExited

    // ********* Card method Play ******************
    public void getCardTable() {

        String[] columns = {"Customer Name", "Product Name", "Unit Price", "Quantity", "Total Price", "Discount", "Actual Price", "Cash Pay", "Date"};

        DefaultTableModel cardAdd = new DefaultTableModel();
        cardAdd.setColumnIdentifiers(columns);
        customerTable.setModel(cardAdd);

    }

    // **** Add to Card method create *************
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked

//        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
//
//        String productName = sName.getSelectedItem().toString();
//        Float unitPrice = Float.parseFloat(sUnitPrice.getText());
//        Float quantity = Float.parseFloat(sQuantity.getText());
//        Float totalPrice = Float.parseFloat(sTotalPrice.getText());
//        Float discount = getDiscount();
//        actualPrice = getActualPrice();
//        Float cashPay = Float.parseFloat(sCashReceive.getText());
//
//        java.util.Date date = convertUtilDateToSqlDate(sDate.getDate());
//
//        List<Object> productList = new ArrayList<>();
//
//        productList.add(new Object[]{productName, unitPrice, quantity, totalPrice, discount, actualPrice, cashPay, date});
//
//        int row = model.getRowCount();
//
//        for (Object i : productList) {
//            model.insertRow(row, (Object[]) i);
//        }
    }//GEN-LAST:event_jButton1MouseClicked


    private void dCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dCustomerActionPerformed
        mainmenu.setSelectedIndex(7);
        getAllCustomerDueTable();

    }//GEN-LAST:event_dCustomerActionPerformed

    private void dCustomerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dCustomerMouseEntered
        // TODO add your handling code here:
        dCustomer.setBackground(new Color(153, 250, 218));
    }//GEN-LAST:event_dCustomerMouseEntered

    private void dCustomerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dCustomerMouseExited
        // TODO add your handling code here:
        dCustomer.setBackground(Color.WHITE);
    }//GEN-LAST:event_dCustomerMouseExited

    // customer Default table create *****************************************
    String[] customerColumns = {"customer_id", "customerName", "address", "phoneNumber", "email", "date"};

    public void getAllCustomer() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(customerColumns);
        customTable.setModel(model);
        sql = "select * from customer";
        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int custId = rs.getInt("customer_id");
                String custName = rs.getString("customerName");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phoneNumber");
                String email = rs.getString("email");
                java.util.Date date = rs.getDate("date");
                model.addRow(new Object[]{custId, custName, address, phoneNumber, email, date});
            }
            rs.close();
            ps.close();
            con.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // customer table create ***********************************************
    private void btnCustomerSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerSubmitMouseClicked

        sql = "insert into customer ( customerName, address, phoneNumber, email, date) values (?,?,?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, cusName.getText().toString());
            ps.setString(2, cusAddress.getText().toString());
            ps.setString(3, cusPhone.getText().toString());
            ps.setString(4, cusEmail.getText().toString());
            ps.setDate(5, convertUtilDateToSqlDate(cusDate.getDate()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();
            JOptionPane.showMessageDialog(prCompanyName, "Customer Data is Save");

            getAllCustomer();
            addCustomerToDue();  // getCustomerToDue method
            getAllCustomerDueTable();  // 

            getSelesCustomerName();

            getCustomerToDueName();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(prCompanyName, "Customer Data is not Save");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCustomerSubmitMouseClicked

// customer table data from customer field***********************   

    private void customTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customTableMouseClicked

        int rowIndex = customTable.getSelectedRow();

        String customer_ID = customTable.getModel().getValueAt(rowIndex, 0).toString();
        String customerName = customTable.getModel().getValueAt(rowIndex, 1).toString();
        String address = customTable.getModel().getValueAt(rowIndex, 2).toString();
        String phoneNumber = customTable.getModel().getValueAt(rowIndex, 3).toString();
        String email = customTable.getModel().getValueAt(rowIndex, 4).toString();
        String date = customTable.getModel().getValueAt(rowIndex, 5).toString();

        cusID.setText(customer_ID);
        cusName.setText(customerName);
        cusAddress.setText(address);
        cusPhone.setText(phoneNumber);
        cusEmail.setText(email);
        sDate.setDate(convertStringToDate(date));
    }//GEN-LAST:event_customTableMouseClicked

    private void dCustomer1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dCustomer1MouseEntered
        dCustomer1.setBackground(new Color(153, 250, 218));
    }//GEN-LAST:event_dCustomer1MouseEntered

    private void dCustomer1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dCustomer1MouseExited
        dCustomer1.setBackground(Color.WHITE);
    }//GEN-LAST:event_dCustomer1MouseExited

    private void dCustomer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dCustomer1ActionPerformed
        mainmenu.setSelectedIndex(6);
    }//GEN-LAST:event_dCustomer1ActionPerformed

    private void CusDuePayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CusDuePayActionPerformed
        mainmenu.setSelectedIndex(8);
        getCustomerToDueName();
        getAllCustomerDuePay();
    }//GEN-LAST:event_CusDuePayActionPerformed

    
     public void resetPay() {
        payName.setSelectedItem(null);
        payNumber.setText(null);
        payId.setText(null);
        payEmail.setText(null);
        payDate.setDate(null);
        payAddress.setText(null);
        payDue.setText(null);
        
    }
    
    
    // Customer Due Pay method create start ***********************************   
    private void paySubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paySubmitMouseClicked

        sql = "insert into customerduepay ( customerDueDate, customerName, customerPhoneNumber, customerEmail, customerDueAddress, customerDuePay) values (?,?,?,?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setDate(1, convertUtilDateToSqlDate(payDate.getDate()));

            ps.setString(2, payName.getSelectedItem().toString());
            ps.setString(3, payNumber.getText().toString());
            ps.setString(4, payEmail.getText().toString());
            ps.setString(5, payAddress.getText().toString());
            ps.setString(6, payDue.getText().toString());

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            getAllCustomerDueDecreament();
            getAllCustomerDuePay();
            
            resetPay();

            JOptionPane.showMessageDialog(prCompanyName, "Due Paid");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(prCompanyName, "Due not Paid");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_paySubmitMouseClicked

    private void payNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payNameMouseClicked

    }//GEN-LAST:event_payNameMouseClicked

// dara show due table*************************************    
    String[] customerDuePay = {"payId", "payName", "payNumber", "payAddress", "payEmail", "payDate", "payDue"};

    public void getAllCustomerDuePay() {

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(customerDuePay);
        payTable.setModel(model);

        sql = "select * from customerduepay";

        try {
            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                int cuId = rs.getInt("duePayId");
                String custName = rs.getString("customerName");
                String cuphoneNumber = rs.getString("customerPhoneNumber");
                String cuaddress = rs.getString("customerDueAddress");
                String cuemail = rs.getString("customerEmail");
                java.util.Date cudate = rs.getDate("customerDueDate");
                Float cudue = rs.getFloat("customerDuePay");

                model.addRow(new Object[]{cuId, custName, cuphoneNumber, cuaddress, cuemail, cudate, cudue});
            }
            rs.close();
            ps.close();
            con.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// Pay table e combo box to field data pass**************************************    
    private void payAddressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payAddressMouseClicked

        sql = "select * from customer where customerName =? ";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, payName.getSelectedItem().toString());

            rs = ps.executeQuery();

            while (rs.next()) {
                String address = rs.getString("address"); // Get the first (and only) column's value
                String phone = rs.getString("phoneNumber");
                String emai = rs.getString("email");

                payAddress.setText(address + " ");
                payNumber.setText(phone + " ");
                payEmail.setText(emai + " ");

            }
            rs.close();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_payAddressMouseClicked

//  Search table ceate ************************************    
    String[] searchColumns = {"customerDueName", "customerPhoneNumber", "customerAddress", "customerEmail", "customerDue", "customerDate"};

    private void searchButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchButtonMouseClicked

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(searchColumns);
        cusSearchTable.setModel(model);

        sql = "select * from customerdueupdate where customerDueId =?";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(searchId.getText()));

            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("customerDueName");
                String phon = rs.getString("customerPhoneNumber");
                String addres = rs.getString("customerAddress");
                String emaill = rs.getString("customerEmail");
                Float due = rs.getFloat("customerDue");
                java.util.Date dat = rs.getDate("customerDate");

                model.addRow(new Object[]{name, phon, addres, emaill, due, dat});
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_searchButtonMouseClicked

    private void CusDuePayMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CusDuePayMouseEntered
        // TODO add your handling code here:
        CusDuePay.setBackground(new Color(153, 250, 218));
    }//GEN-LAST:event_CusDuePayMouseEntered

    private void CusDuePayMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CusDuePayMouseExited
        // TODO add your handling code here:
        CusDuePay.setBackground(Color.WHITE);
    }//GEN-LAST:event_CusDuePayMouseExited

    private void sQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sQuantityKeyReleased
        // TODO add your handling code here:

//          sql = "select stockQuantity from stock where stockName=?";
//
//        sName.removeAllItems();
//
//        try {
//            ps = con.getCon().prepareStatement(sql);
//            ps.setString(1, sName.getSelectedItem().toString());
//            rs = ps.executeQuery();
//          
//                
//            
//            while (rs.next()) {
//               
//                float quantit = Float.parseFloat(rs.getString("stockQuantity"));
//                float stock = Float.parseFloat(sQuantity.getText().toString());
//                
//                if(quantit < stock ){
//               JOptionPane.showMessageDialog(rootPane, "Stock Not Avalaible");
//               sQuantity.requestFocus();
//                }
//                else{
//               
//               
//                }
//               
//            }
//            
//            ps.close();
//            rs.close();
//            con.getCon().close();
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        

    }//GEN-LAST:event_sQuantityKeyReleased

    private void sTotalPriceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sTotalPriceMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_sTotalPriceMouseClicked

    
    
    
    private void payDueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_payDueFocusLost
        // TODO add your handling code here:
        
          sql = "select * from customerdueupdate where customerDueName=?";

//        sName.removeAllItems();

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, payName.getSelectedItem().toString());
            
            rs = ps.executeQuery();

            while (rs.next()) {

                float customerDue = Float.parseFloat(rs.getString("customerDue"));
                float payAmount = Float.parseFloat(payDue.getText().toString());

                if (payDue.getText().trim().isEmpty()) {
                    payDue.requestFocus();
                } 
//             else if(sQuantity.getText().trim().isEmpty()) {
//                    JOptionPane.showMessageDialog(rootPane, "Quantity  Empty");
//                     sQuantity.requestFocus();               
//            }
             
                else if ( payAmount <=0) {
//                sTotalPrice.setText(getTotalPrice() + " ");
                    JOptionPane.showMessageDialog(rootPane, "Not Allow");
                    payDue.requestFocus();
                }
             
                else if (!payDue.getText().trim().isEmpty() && customerDue < payAmount ) {
//                sTotalPrice.setText(getTotalPrice() + " ");
                    JOptionPane.showMessageDialog(rootPane, "Bounce of Due");
                    payDue.requestFocus();
                }
//                else if (!sQuantity.getText().trim().isEmpty() && quantit >= stock ) {
//                    sTotalPrice.setText(getTotalPrice() + " ");
//                }    



            }

            ps.close();
            rs.close();
            con.getCon().close();
//            getAllStockToSeles();
        }
            
         catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_payDueFocusLost

    private void jPanel53MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel53MouseClicked
        // TODO add your handling code here:
               sql = "select * from customerdueupdate where customerDueName=?";

//        sName.removeAllItems();

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, payName.getSelectedItem().toString());
            
            rs = ps.executeQuery();

            while (rs.next()) {

                float customerDue = Float.parseFloat(rs.getString("customerDue"));
                float payAmount = Float.parseFloat(payDue.getText().toString());

                if (payDue.getText().trim().isEmpty()) {
                    payDue.requestFocus();
                } 
//             else if(sQuantity.getText().trim().isEmpty()) {
//                    JOptionPane.showMessageDialog(rootPane, "Quantity  Empty");
//                     sQuantity.requestFocus();               
//            }
             
                else if ( payAmount <=0) {
//                sTotalPrice.setText(getTotalPrice() + " ");
                    JOptionPane.showMessageDialog(rootPane, "Not Allow");
                    payDue.requestFocus();
                }
             
                else if (!payDue.getText().trim().isEmpty() && customerDue < payAmount ) {
//                sTotalPrice.setText(getTotalPrice() + " ");
                    JOptionPane.showMessageDialog(rootPane, "Bounce of Due");
                    payDue.requestFocus();
                }
//                else if (!sQuantity.getText().trim().isEmpty() && quantit >= stock ) {
//                    sTotalPrice.setText(getTotalPrice() + " ");
//                }    
            }

            ps.close();
            rs.close();
            con.getCon().close();
//            getAllStockToSeles();
        }
            
         catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jPanel53MouseClicked

    private void paySubmit1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paySubmit1MouseClicked
       resetPay();
    }//GEN-LAST:event_paySubmit1MouseClicked

    // Customer  barar jonno method*************
    public void addCustomerToDue() {
        sql = " insert into customerdueupdate (customerDueName, customerPhoneNumber, customerAddress, customerEmail, customerDate, customerDue) values (?,?,?,?,?,?)";
        custName = cusName.getText();
        custPhone = cusPhone.getText();
        custAddress = cusAddress.getText();
        custEmail = cusEmail.getText();
        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, custName);
            ps.setString(2, custPhone);
            ps.setString(3, custAddress);
            ps.setString(4, custEmail);
            ps.setDate(5, convertUtilDateToSqlDate(cusDate.getDate()));
            ps.setFloat(6, 0.0f);

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// Default Customer Due Table create ************************
    String[] customerDueColumn = {"customerDueId", "customerDueName", "customerPhoneNumber", "customerAddress", "customerEmail", "customerDate", "customerDue"};

    public void getAllCustomerDueTable() {

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(customerDueColumn);
        cuDueUpdate.setModel(model);

        sql = "select * from customerdueupdate";

        try {
            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                
                float due = Float.parseFloat(rs.getString("customerDue"));
                
                if(due > 0.00f){
                int cuId = rs.getInt("customerDueId");
                String custName = rs.getString("customerDueName");
                String cuphoneNumber = rs.getString("customerPhoneNumber");
                String cuaddress = rs.getString("customerAddress");
                String cuemail = rs.getString("customerEmail");
                java.util.Date cudate = rs.getDate("customerDate");
                Float cudue = rs.getFloat("customerDue");

                model.addRow(new Object[]{cuId, custName, cuphoneNumber, cuaddress, cuemail, cudate, cudue});
                }

            }
            rs.close();
            ps.close();
            con.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // customerDue Update table method create ********************  
    public float getDuePay() {
        if (payDue.getText().isEmpty()) {
            payDue.setText("0");
            return duePay;
        } else {
            duePay = Float.parseFloat(payDue.getText());
            return duePay;
        }
    }

    // Customer Due Decreament method create Data from Customer Table**************************
    public void getAllCustomerDueDecreament() {
        sql = "update customerdueupdate set customerDue = customerDue - ? where customerDueName=?";

        namePay = (String) payName.getSelectedItem(); // Retrieve the selected item as a String

        if (namePay != null) {
            try {
                ps = con.getCon().prepareStatement(sql);

                ps.setFloat(1, getDuePay());
                ps.setString(2, namePay);

                ps.executeUpdate();
                ps.close();
                con.getCon().close();
            } catch (SQLException ex) {
                Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Selected item is null or empty. Handle this case.");
        }
    }

    // customerDue Update table method create ********************  
    public float getDue() {
        if (sDueAmount.getText().isEmpty()) {
            sDueAmount.setText("0");
            return salesDue;
        } else {
            salesDue = Float.parseFloat(sDueAmount.getText());
            return salesDue;
        }
    }

    // stock method create purchase add contain to stock*************
    public void getCustomerDueIncreament() {
        sql = "update customerdueupdate set  customerDue = customerDue + ? where customerDueName=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setFloat(1, getDue());
            ps.setString(2, customName.getSelectedItem().toString());

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Due Pay combo field er method   Customer Name add Customer table from sales table
    public void getCustomerToDueName() {

        sql = "select customerName from customer";

        payName.removeAllItems();

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("customerName");
                payName.addItem(name);
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CusDuePay;
    private javax.swing.JTextField PrCashPay;
    private javax.swing.JButton btnCustomerSubmit;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextPane card;
    private javax.swing.JTable cuDueUpdate;
    private javax.swing.JTextArea cusAddress;
    private com.toedter.calendar.JDateChooser cusDate;
    private javax.swing.JPanel cusDuePay;
    private javax.swing.JPanel cusDueUpdate;
    private javax.swing.JTextField cusEmail;
    private javax.swing.JTextField cusID;
    private javax.swing.JTextField cusName;
    private javax.swing.JTextField cusPhone;
    private javax.swing.JTable cusSearchTable;
    private javax.swing.JTable cusTable;
    private javax.swing.JComboBox<String> customName;
    private javax.swing.JTable customTable;
    private javax.swing.JPanel customer;
    private javax.swing.JTable customerTable;
    private javax.swing.JButton dCustomer;
    private javax.swing.JButton dCustomer1;
    private javax.swing.JButton dHome;
    private javax.swing.JButton dProduct;
    private javax.swing.JButton dPurchase;
    private javax.swing.JButton dReport;
    private javax.swing.JButton dSales;
    private javax.swing.JButton dStock;
    private javax.swing.JPanel home;
    private javax.swing.JLabel homeMonthlyDue;
    private javax.swing.JLabel homeMonthlyPurchase;
    private javax.swing.JLabel homeMonthlySales;
    private javax.swing.JLabel homeTodayDue;
    private javax.swing.JLabel homeTodayPurchase;
    private javax.swing.JLabel homeTodaySales;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane mainmenu;
    private javax.swing.JComboBox<String> pCategory;
    private javax.swing.JTextField pCode;
    private javax.swing.JButton pDelete;
    private javax.swing.JTextField pID;
    private javax.swing.JTextField pName;
    private javax.swing.JButton pReset;
    private javax.swing.JButton pSave;
    private javax.swing.JTable pTable;
    private javax.swing.JButton pUpdate;
    private javax.swing.JTextArea payAddress;
    private com.toedter.calendar.JDateChooser payDate;
    private javax.swing.JTextField payDue;
    private javax.swing.JTextField payEmail;
    private javax.swing.JTextField payId;
    private javax.swing.JComboBox<String> payName;
    private javax.swing.JTextField payNumber;
    private javax.swing.JButton paySubmit;
    private javax.swing.JButton paySubmit1;
    private javax.swing.JTable payTable;
    private javax.swing.JTextField prCompanyName;
    private com.toedter.calendar.JDateChooser prDate;
    private javax.swing.JButton prDelete;
    private javax.swing.JTextField prDueAmount;
    private javax.swing.JTextField prID;
    private javax.swing.JComboBox<String> prName;
    private javax.swing.JTextField prQuantity;
    private javax.swing.JButton prReset;
    private javax.swing.JButton prSave;
    private javax.swing.JTable prTable;
    private javax.swing.JTextField prTotalPrice;
    private javax.swing.JTextField prUnitPrice;
    private javax.swing.JButton prUpdate;
    private javax.swing.JPanel product;
    private javax.swing.JPanel purchase;
    private com.toedter.calendar.JDateChooser reFromDate;
    private javax.swing.JRadioButton rePurchase;
    private javax.swing.JRadioButton reSales;
    private javax.swing.JRadioButton reStock;
    private com.toedter.calendar.JDateChooser reToDate;
    private javax.swing.JButton reView;
    private javax.swing.JPanel report;
    private javax.swing.JTable reportTable;
    private javax.swing.JTextPane reportTotal;
    private javax.swing.JTextField sActualPrice1;
    private javax.swing.JTextField sCashReceive;
    private javax.swing.JTextField sCashReturn1;
    private com.toedter.calendar.JDateChooser sDate;
    private javax.swing.JButton sDelete;
    private javax.swing.JTextField sDiscount;
    private javax.swing.JTextField sDueAmount;
    private javax.swing.JTextField sID;
    private javax.swing.JComboBox<String> sName;
    private javax.swing.JButton sPrint;
    private javax.swing.JTextField sQuantity;
    private javax.swing.JButton sReset;
    private javax.swing.JButton sSave;
    private javax.swing.JTextField sTotalPrice;
    private javax.swing.JTextField sUnitPrice;
    private javax.swing.JButton sUpdate;
    private javax.swing.JPanel sales;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchId;
    private javax.swing.JButton stDelete;
    private javax.swing.JTextField stID;
    private javax.swing.JTextField stName;
    private javax.swing.JTextField stQuantity;
    private javax.swing.JTextField stQuantity1;
    private javax.swing.JButton stReset;
    private javax.swing.JButton stSave;
    private javax.swing.JTable stTable;
    private javax.swing.JButton stUpdate;
    private javax.swing.JPanel stock;
    // End of variables declaration//GEN-END:variables
}
