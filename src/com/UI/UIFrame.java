package com.UI;

import javax.swing.*;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UIFrame extends JFrame implements ActionListener {

    private JButton cnv;
    private JButton din;
    private JButton save;
    private JButton cancel;

    private JTextField invoiceDate;
    private JTextField customerName;

    private JTable invoicesItems;
    private JTable invoicesTables;
    private String[] itcols={"No.","Date","Customer","Total"};
    private String[] iicols={"No.","Item Name","Item Price","Count","Item Total"};
    private String[][] itdata = {
            {"","","",""},
            {"","","",""},
            {"","","",""}
    };
    private String[][] iidata = {
            {"","","","",""},
            {"","","","",""},
            {"","","","",""}
    };

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem loadFile;
    private JMenuItem saveFile;



    private static int createCount=0;
    private int Total_Price;
    private final int  Mobile      = 3200;
    private final int  Cover       = 10;
    private final int  Headphones  = 130;
    private final int  Laptop      = 4000;
    private final int  Mouse       = 35;
    private JPanel panel1;
    private JButton button1;


    public UIFrame() {
        super("Design Preview[NewJFrame]");
        /** Creating Objects **/

        //Buttons
        cnv    = new JButton("Create New Invoice");
        din    = new JButton("Delete Invoice");
        save   = new JButton("Save");
        cancel = new JButton("Cancel");

        //Texts Field
        invoiceDate  = new JTextField(30);
        customerName = new JTextField(30);

        //Tables
        invoicesItems  = new JTable(iidata,iicols);
        invoicesTables = new JTable(itdata,itcols);

        //Menu
        menuBar= new JMenuBar();
        loadFile = new JMenuItem("Load File");
        loadFile.setAccelerator(KeyStroke.getKeyStroke('L', KeyEvent.CTRL_DOWN_MASK));
        loadFile.addActionListener(this);
        loadFile.setActionCommand("load");
        saveFile = new JMenuItem("Save File");
        saveFile.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
        saveFile.addActionListener(this);
        saveFile.setActionCommand("save");

        fileMenu= new JMenu("File");
        fileMenu.add(loadFile);
        fileMenu.add(saveFile);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);



        JPanel panel1 = new JPanel();
        JPanel panel = new JPanel();
        panel1.setBounds(0,0,100,100);
        panel.setBounds(100,0,100,100);
        add(panel1);
        add(panel);
        setLayout(new GridLayout(1,2));
        /*panel1.setLayout(new FlowLayout());
        panel.setLayout(new FlowLayout());*/


        //Section 1
        panel1.add(new JLabel("Invoice Table"));
        panel1.add(new JScrollPane(invoicesTables));

        //Section 2
        cnv.addActionListener(this);
        cnv.setActionCommand("btn_cnv");
        panel1.add(cnv);
        din.addActionListener(this);
        din.setActionCommand("btn_din");
        panel1.add(din);

        //Section 3
        panel.add(new JLabel("Invoice Number  "+ createCount++ + "\n"));
        panel.add(new JLabel("Invoice Date    "));
        panel.add(invoiceDate);
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Customer Name   "));
        panel.add(customerName);
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Invoice Total   "+Total_Price + "\n"));

        //Section 4
        panel.add(new JLabel("Invoice Items"));
        panel.add(new JScrollPane(invoicesItems));

        //Section 5
        save.addActionListener(this);
        save.setActionCommand("btn_save");
        panel.add(save);
        cancel.addActionListener(this);
        cancel.setActionCommand("btn_cancel");
        panel.add(cancel);



        setSize(100,100);
        setLocation(200,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "load":
                openFile();
                break;
            case "save":
                saveInFile();
                break;
            case "btn_save":
                saveItems();
                break;
            case "btn_cancel":
                cancelItems();
                break;
            case "btn_cnv":
                createInvoice();
                break;
            case "btn_din":
                deleteInvoice();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + e.getActionCommand());
        }

    }


    private void deleteInvoice() {
        ((DefaultTableModel) invoicesTables.getModel()).removeRow(invoicesTables.getRowCount());
    }

    private void createInvoice() {
        ((DefaultTableModel) invoicesTables.getModel()).insertRow(invoicesTables.getRowCount(),itdata);
    }

    private void cancelItems() {
        invoicesItems.setRowSelectionAllowed(true);
        invoicesItems.setEnabled(true);
    }

    private void saveItems() {
        invoicesItems.setRowSelectionAllowed(false);
        invoicesItems.setEnabled(false);
        JOptionPane.showMessageDialog(null,"Saved","Table Items",JOptionPane.INFORMATION_MESSAGE);
    }


    private void saveInFile() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();
            FileOutputStream file = null;

            OutputStreamWriter fos = null;
            try {file = new FileOutputStream(path+".csv");
                fos = new OutputStreamWriter(file,"UTF-8");
                for(int i=0;i<invoicesTables.getColumnCount();i++)
                {
                    fos.write(invoicesTables.getColumnName(i));
                    fos.write(',');
                }

                for(int i=0;i<invoicesTables.getRowCount();i++) {
                    fos.write(0x0d);
                    for(int j=0;j<invoicesTables.getColumnCount();j++) {
                            fos.write(String.valueOf(invoicesTables.getValueAt(i,j)));
                        fos.write(',');
                    }
                }
                fos.write(0x0d);
                for(int i=0;i<invoicesItems.getColumnCount();i++)
                {
                    fos.write(invoicesItems.getColumnName(i));
                    fos.write(',');
                }
                for(int i=0;i<invoicesItems.getRowCount();i++) {
                    fos.write(0x0d);
                    for(int j=0;j<invoicesItems.getColumnCount();j++) {
                            fos.write(String.valueOf(invoicesItems.getValueAt(i,j)));
                        fos.write(',');
                    }
                }

            }
            catch (FileNotFoundException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();
            } finally {try {fos.close();} catch (IOException ex) {ex.printStackTrace();}}
        }
        JOptionPane.showMessageDialog(null,"file saved successfully","",JOptionPane.INFORMATION_MESSAGE);
    }

    private void openFile() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            String path = fc.getSelectedFile().getPath();
            FileInputStream fis= null;
            try{
                fis = new FileInputStream(path);
                int size= fis.available();
                byte[] b= new byte[size];
                fis.read(b);

            } catch (FileNotFoundException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
            finally {try {fis.close();} catch (IOException ex) {ex.printStackTrace();}}
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
