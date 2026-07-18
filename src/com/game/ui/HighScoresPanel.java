package com.game.ui;

import com.game.database.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;

public class HighScoresPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public HighScoresPanel(MainFrame frame){

        setLayout(new BorderLayout());
        setBackground(new Color(20,40,60));

        JLabel title = new JLabel("HIGH SCORES",SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,34));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        add(title,BorderLayout.NORTH);

        String[] columns = {
                "Rank",
                "Player",
                "Score",
                "Level"
        };

        model = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(40);
        table.setFont(new Font("Arial",Font.BOLD,16));
        table.setBackground(new Color(35,35,35));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(40,70,100));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial",Font.BOLD,18));
        header.setBorder(BorderFactory.createLineBorder(new Color(55, 80, 110)));

        table.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column){

                Component c =
                        super.getTableCellRendererComponent(
                                table,value,isSelected,hasFocus,row,column);

                setHorizontalAlignment(CENTER);

                if(row==0){

                    c.setBackground(new Color(255,215,0));
                    c.setForeground(Color.BLACK);

                }
                else if(row==1){

                    c.setBackground(new Color(192,192,192));
                    c.setForeground(Color.BLACK);

                }
                else if(row==2){

                    c.setBackground(new Color(205,127,50));
                    c.setForeground(Color.BLACK);

                }
                else{

                    c.setBackground(new Color(90,100,100));
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(20,40,60));
        scroll.setBorder(BorderFactory.createEmptyBorder(15,30,15,30));

        add(scroll,BorderLayout.CENTER);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(30,40,70), 20));

        JButton backBtn = new JButton("Back To Menu");
        backBtn.setPreferredSize(new Dimension(200,40));
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        backBtn.setBackground(new Color(40, 70, 100));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200), 2));

        backBtn.addActionListener(e->frame.showPage("MENU"));

        JPanel south = new JPanel();
        south.setBackground(new Color(20,40,60));
        south.add(backBtn);

        add(south,BorderLayout.SOUTH);
        refreshTable();
    }

    public void refreshTable() {

        model.setRowCount(0);

        try {

            ResultSet rs = DatabaseManager.getHighScores();

            int rank = 1;

            while (rs.next()) {

                model.addRow(new Object[]{
                        rank,
                        rs.getString("username"),
                        rs.getInt("bestScore"),
                        rs.getInt("current_level")
                });

                rank++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
