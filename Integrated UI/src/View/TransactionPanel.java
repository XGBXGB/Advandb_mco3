package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import Model.Column;
import Model.ComboBoxConstants;
import Model.Constants;

public class TransactionPanel extends JPanel{
	
	private static final String SELECT_ALL = "Select All";
	private static int id = 1;
	private JPanel queriesHolder;
	private JTextArea queryDisplayer;
	private JPanel panelTemp;
	private JButton btnRemove;
	private JButton btnAdd;
	private JPanel bottomPanel;
	private JComboBox areaOptions;
	private JComboBox acOptions;
	private ButtonGroup group;
	
	private static final int CUST_HEIGHT = 110;
	
	public TransactionPanel(){
		this.setName("T" + id);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(this.getSize().width, (int) (this.getSize().height*0.9) ));
		this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		panelTemp = new JPanel();
		panelTemp.setLayout(new BorderLayout());
		panelTemp.setBackground(Color.lightGray);
		
		panelTemp.add(infoPanel(), BorderLayout.NORTH);
		panelTemp.add(createResultsPanel(), BorderLayout.CENTER);
		
		this.add(panelTemp, BorderLayout.CENTER);
		this.id++;
	}
	
	public JPanel createResultsPanel(){
		JPanel resultsPane = new JPanel();
		resultsPane.setLayout(new BorderLayout());
		resultsPane.setBorder(BorderFactory.createEmptyBorder(10,2,3,2));
		
		resultsPane.add(new JScrollPane(createJTable(null)), BorderLayout.CENTER);
		
		if (id > 2){
			btnRemove = new JButton("Remove");
			btnRemove.addActionListener(new ButtonListener());
			JPanel btnHolder = new JPanel();
			btnHolder.setLayout(new FlowLayout(FlowLayout.RIGHT));
			btnHolder.add(btnRemove);
			resultsPane.add(btnHolder, BorderLayout.SOUTH);
		}
		return resultsPane;
	}
	
	public JPanel infoPanel(){
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(500,CUST_HEIGHT));
		infoPanel.setLayout(new RelativeLayout(RelativeLayout.X_AXIS));
		
		infoPanel.add(createLeftControl(), new Float(1));
		infoPanel.add(createRightControl(), new Float(2));
		return infoPanel;
	}
	
	public JPanel createLeftControl(){
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension (100,CUST_HEIGHT));
		leftPanel.setLayout(new BorderLayout());
		
		queriesHolder = new JPanel();
		queriesHolder.setLayout(new BoxLayout(queriesHolder, BoxLayout.Y_AXIS));
	    JScrollPane scroll = new JScrollPane(queriesHolder);
	    scroll.setPreferredSize(new Dimension(50,50));
	    
	    group = new ButtonGroup();
	    addQueryChoice(Constants.QUERY_1);
	    addQueryChoice(Constants.QUERY_2);
	    addQueryChoice(Constants.QUERY_3);
	    addQueryChoice(Constants.QUERY_4);
	    addQueryChoice(Constants.QUERY_5);
	    addQueryChoice(Constants.QUERY_6);
	    addQueryChoice(Constants.QUERY_7);
	    
	    leftPanel.add(scroll, BorderLayout.CENTER);
	    return leftPanel;
	}
	
	public void addQueryChoice(String text) {
		JRadioButton button = new JRadioButton(text);
		group.add(button);
	//	cb.addItemListener(new checkBoxListener());
		button.setBorder(BorderFactory.createEmptyBorder(1,5,1,5));
		queriesHolder.add(button);
		queriesHolder.revalidate();
		queriesHolder.repaint();
	}
	
	public JTable createJTable(ResultSet rs) {
		if ( rs != null ){
			JTable table = new JTable();
			DefaultTableModel dataModel = new DefaultTableModel();
			table.setModel(dataModel);
			
			try {
				ResultSetMetaData mdata = rs.getMetaData();
				int colCount = mdata.getColumnCount();		
				String[] colNames = getColumnNames(colCount, mdata);
				dataModel.setColumnIdentifiers(colNames);
				while (rs.next()) {
					String[] rowData = new String[colCount];
					for (int i = 1; i <= colCount; i++) {
						rowData[i - 1] = rs.getString(i);
					}
					dataModel.addRow(rowData);
				}
			} catch (SQLException e) {}
			
			return table;
		}
		else {
			JTable table = new JTable();
			return table;
		}
	}
	
	public String[] getColumnNames(int colCount, ResultSetMetaData mdata) throws SQLException {
		String[] colNames = new String[colCount];
		for (int i = 1; i <= colCount; i++) {
			String col = mdata.getColumnName(i);
			colNames[i-1] = col;
		}
		return colNames;
	}
	
	private void updateRowHeights(JTable table) {
		try {
			for (int row = 0; row < table.getRowCount(); row++) {
				int rowHeight = table.getRowHeight();

				for (int column = 0; column < table.getColumnCount(); column++) {
					Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
					rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
				}

				table.setRowHeight(row, rowHeight);
			}
		} catch (ClassCastException e) {}
	}
	
	public void updateTable(ResultSet rs) {
		panelTemp.removeAll();
		JTable table = createJTable(rs);
		JScrollPane pane = new JScrollPane(table);
		updateRowHeights(table);
		panelTemp.add(pane, BorderLayout.CENTER);
		panelTemp.revalidate();
		panelTemp.repaint();
	}
	
	public void setQuery(String query){
		queryDisplayer.setText(queryDisplayer.getText() + "\n\n" + query);
	}
	
	public JPanel createRightControl() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(100, CUST_HEIGHT));
		panel.setLayout(new BorderLayout());
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		JScrollPane scroll = new JScrollPane(bottomPanel);
	    scroll.setPreferredSize(new Dimension(50,50));
	    
		panel.add(scroll, BorderLayout.CENTER);
		
		JPanel addBtnContainer = new JPanel();
		addBtnContainer.setLayout(new RelativeLayout(RelativeLayout.X_AXIS));
		
		JLabel la = new JLabel("Area:   ");
		la.setHorizontalAlignment(SwingConstants.RIGHT);
		addBtnContainer.add(la, new Float(1));
		
		areaOptions = new JComboBox(new String[] {"Marinduque", "Palawan", "Both"});
		areaOptions.setSelectedIndex(0);
		addBtnContainer.add(areaOptions, new Float(1.5));
		
		JLabel lab = new JLabel("Abort or Commit:   ");
		lab.setHorizontalAlignment(SwingConstants.RIGHT);
		addBtnContainer.add(lab, new Float(2));
		
		acOptions = new JComboBox(new String[] {"Abort", "Commit"});
		acOptions.setSelectedIndex(0);
		addBtnContainer.add(acOptions, new Float(1));
		
		JLabel l = new JLabel("Filtering Options   ");
		l.setHorizontalAlignment(SwingConstants.RIGHT);
		addBtnContainer.add(l, new Float(2));
		btnAdd = new JButton("+");
		btnAdd.setPreferredSize(new Dimension(50,20));
		btnAdd.addActionListener(new ButtonListener());
		addBtnContainer.add(btnAdd, new Float(1));
		panel.add(addBtnContainer, BorderLayout.NORTH);
		
		return panel;
	}
	
	private void addFilteringOption(){
		ArrayList<Column> columns = ComboBoxConstants.OPTIONS_QUERY;
	    JPanel filterOption = new JPanel();
	    filterOption.setLayout(new RelativeLayout(RelativeLayout.X_AXIS));
	    if (bottomPanel.getComponents().length != 0){
	    	JComboBox opList = new JComboBox(new String[] {"AND" , "OR"});
		    opList.setSelectedIndex(0);
		    filterOption.add(opList, new Float(1));
	    }
	    
	    ArrayList<String> cols = new ArrayList<String>();
	    for (Column c: columns){
	    	cols.add(c.getName());
	    }
	    JComboBox colList = new JComboBox(cols.toArray());
	    colList.setSelectedIndex(0);
	    filterOption.add(colList, new Float(2));
	    
	    JComboBox funcList = new JComboBox(getFunctions());
	    colList.setSelectedIndex(0);
	    filterOption.add(funcList, new Float(1));
	    
	    JTextField text = new JTextField(10);
	    filterOption.add(text, new Float(2));
	    
	    JButton btnRemove = new JButton("-");
	    btnRemove.addActionListener(new ButtonListener());
	    filterOption.add(btnRemove, new Float (1));
	    
	    bottomPanel.add(filterOption);
	    bottomPanel.revalidate();
	    bottomPanel.repaint();
	}
	
	private void removeFilteringOption(JPanel panel){
		if (bottomPanel.getComponentZOrder(panel) == 0 && bottomPanel.getComponentCount() > 1){
			((JPanel)bottomPanel.getComponent(1)).remove(((JPanel)bottomPanel.getComponent(1)).getComponent(0));
		}
		bottomPanel.remove(panel);
		bottomPanel.revalidate();
		bottomPanel.repaint();
	}
	
	private String[] getFunctions(){
		ArrayList<String> funcList = new ArrayList<String>();
		funcList.add("=");
		funcList.add(">");
		funcList.add("<");
		funcList.add(">=");
		funcList.add("<=");
		funcList.add("IS");
		return funcList.stream().toArray(String[]::new);
	}
	
	public int getQuerySelected() {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
            	switch(button.getText()){
            		case Constants.QUERY_1: return 1;
            		case Constants.QUERY_2: return 2;
            		case Constants.QUERY_3: return 3;
            		case Constants.QUERY_4: return 4;
            		case Constants.QUERY_5: return 5;
            		case Constants.QUERY_6: return 6;
            		case Constants.QUERY_7: return 7;
            	}
            }
        }
        return -1;
	}
	
	public String getArea(){
		return this.areaOptions.getSelectedItem().toString();
	}
	
	public boolean getBooleanAbort(){
		if (this.acOptions.getSelectedItem().equals("Abort"))
			return true;
		return false;
	}
	
	public class ButtonListener implements ActionListener{
	    @Override
		public void actionPerformed(ActionEvent e) {
	    	JButton button = (JButton) e.getSource();
	    	if (button.getText().equals("Remove")){
	    		MainFrame.deleteTransactionPanel((JPanel)button.getParent().getParent().getParent().getParent());
	    	}
	    	else if (button == btnAdd){
	    		addFilteringOption();
	    	}
	    	else {
	    		JPanel panel = (JPanel)button.getParent();
	    		removeFilteringOption(panel);
	    	}
		}
	}

}
