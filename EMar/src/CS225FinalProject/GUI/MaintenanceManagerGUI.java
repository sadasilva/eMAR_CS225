/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CS225FinalProject.GUI;

import CS225FinalProject.DataStructure.Medication;
import CS225FinalProject.DataStructure.SimulationController;
import CS225FinalProject.DataStructure.Student;
import CS225FinalProject.DataStructure.CompletedScenario;
import CS225FinalProject.DataStructure.Scenario;
import CS225FinalProject.DataStructure.Narrative;
import CS225FinalProject.DataStructure.DataIO;
import CS225FinalProject.DataStructure.User;
import CS225FinalProject.DataStructure.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.*;

import CS225FinalProject.SimulationManager;
import CS225FinalProject.printer.SimulationPrinter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.crypto.Data;

//!!KL
import java.util.*;
//KL

import static javax.swing.JOptionPane.*;
import sun.print.PageableDoc;

/**
 * 
 * @author Eric
 */
public class MaintenanceManagerGUI extends javax.swing.JFrame {

	private DefaultListModel sessionListModel;
	private DefaultListModel studentListModel;
	private DefaultListModel scenarioListModel;
        
        private int lastSelected;
        //!!JR
        private boolean hasChanged;
	//JR
	private SimulationController controller = SimulationController
			.getInstance();
        
        
        
        

	/**
	 * Creates new form MaintenanceManagerGUI
	 */
	public MaintenanceManagerGUI() {
		initComponents();
                
                scenarioList.addListSelectionListener(new ListSelectionListener() {

                    
                    
                    /**
                     * this is for loading the selected scenario to the gui
                     */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(scenarioList.getSelectedIndex()>-1){
                    Scenario scenario = controller.getScenarioByName((String)scenarioList.getSelectedValue());
                    
                    while(medJTable.getRowCount()>0){
                        ((DefaultTableModel)medJTable.getModel()).removeRow(0);
                    }
                    for(Medication medication:scenario.getMedicationList()){
                        ((DefaultTableModel)medJTable.getModel()).addRow(new String[]{medication.getMedicationName(), medication.getDosage(), medication.getRouteOfMedication(), medication.getMedicationDueTimes()});
                    }
                    
                    while(documentationTable.getRowCount()>0){
                        ((DefaultTableModel)documentationTable.getModel()).removeRow(0);
                    }
                    for(Narrative medication:scenario.getNarrativeList()){
                        ((DefaultTableModel)documentationTable.getModel()).addRow(medication.getNarrativeAsArrayStrings());
                    }
                    
                    scenarioSummaryTextPane.setText(scenario.getSummary());
                    roomNumberTextField.setText(scenario.getRoom()+"");
                    patientNameTextField.setText(scenario.getPatientName());
                    diagnosisTextArea.setText(scenario.getDiagnosis());
                    allergiesTextArea.setText(scenario.getAllergies());
                    timeTextfield.setText(scenario.getTime()+"");
                    
                }
            }
        });
                
                
                
                
                rootTabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if(rootTabbedPane.getSelectedIndex()==1){
                    ((DefaultListModel)scenarioList.getModel()).removeAllElements();
                    for(Scenario scenario: controller.getScenarios())
                        ((DefaultListModel)scenarioList.getModel()).addElement(scenario.getPatientName());
                }
                if(scenarioList.getSelectedIndex()<0){
                    if(scenarioList.getModel().getSize()>0)
                        scenarioList.setSelectedIndex(0);
                    else
                        previewTabbedPane.setVisible(false);
                }
                if(rootTabbedPane.getSelectedIndex()==2){
                    currentProfessorUserNameLabel.setText(controller.getUsers().get(0).getUserName());
                    currentProfessorPasswordLabel.setText(controller.getUsers().get(0).getPassword());
                    
//                    ((DefaultListModel)scenarioList.getModel()).removeAllElements();
//                    for(Scenario scenario: controller.getScenarios())
//                        ((DefaultListModel)scenarioList.getModel()).addElement(scenario.getPatientName());
            }}
        });
                
                
                studentList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(studentList.getSelectedIndex()>-1){
                    if(!((DefaultListModel)studentList.getModel()).isEmpty()){
                        
                       
                    studentManagerControlTabbedPane.setSelectedIndex(0);
                    studentManagerControlTabbedPane.setSelectedIndex(1);
                        
                    
                    }
                }
                
            }
        });
                
                studentManagerControlTabbedPane.addChangeListener(new ChangeListener() {
 
            @Override
            public void stateChanged(ChangeEvent e) {
                if(studentManagerControlTabbedPane.getSelectedIndex()==1 && studentList.getSelectedIndex()>-1){
                    User student = null;
                    while(((DefaultTableModel) studentTable.getModel()).getRowCount()>0)
                        ((DefaultTableModel) studentTable.getModel()).removeRow(0);
                    //!!KL
                    // Ketty: Changed getRealName() to getUserName()
                    for(User user:  controller.getStudentsInClass((String)classList.getSelectedValue())){
                        if(!user.isInstructor()&& user.getUserName().equals(studentList.getSelectedValue())&&
                                user.getClassName().equals(classList.getSelectedValue())){
                            student = user;
                            break;
                        }
                   
                    }
                    //KL
                    if(student!=null){
                        int sum = 0;
                        int total = 0;
                        
                        
                        int sumC = 0;
                        int totalC = 0;
                        
                        studentManagerControlTabbedPane.setVisible(true);
                    for(CompletedScenario completedScenario:((Student)student).getCompletedScenarios()){
                        ((DefaultTableModel) studentTable.getModel()).addRow(
                                new String[]{completedScenario.getScenarioTaken().getPatientName(),
                                    completedScenario.getDateTaken(),
                                    completedScenario.getEvaluationSuggestion(),
                                    ""+(completedScenario.getScore()==null? "No score issued": completedScenario.getScore())} );
                        try{
                            sum += Integer.parseInt(completedScenario.getScore()+"");
                            total++;
                        }
                        catch(NumberFormatException n){
                            continue;
                        }
                        
                    }
                    try{
                    SimulationScoreLabel.setText("Average Simulation Score: "+ (sum/total));
                    }
                    //!!KL
                    // Ketty: Caps change in Not Available.
                    catch(Exception exception){
                         SimulationScoreLabel.setText("Average Simulation Score: "+ "Not Available");
                    }
                    //KL
                    currentStudentUserNameLabel.setText(student.getUserName());
                    currentStudentPasswordLabel.setText(student.getPassword());
                    //!!KL
                    // Ketty: Added real name label.
                    currentStudentRealNameLabel.setText(student.getRealName());
                    //KL
                    studentNameLabel.setText(student.getRealName());
                    }
                    
                }
                
                if(studentManagerControlTabbedPane.getSelectedIndex()==1 && studentList.getSelectedIndex()<0)
                    studentManagerControlTabbedPane.setSelectedIndex(0);
                else if(studentManagerControlTabbedPane.getSelectedIndex()==0 &&studentList.getSelectedIndex()>-1){
                    int selected = studentList.getSelectedIndex();
                    studentList.clearSelection();
                    studentList.setSelectedIndex(selected);
                }
                //else
                       // studentManagerControlTabbedPane.setVisible(false);
                   
            }
        });
		setTitle("Simulation Data Management");
		setVisible(false);

		setLocation((getToolkit().getScreenSize().width - getWidth()) / 2,
				(getToolkit().getScreenSize().height - getHeight()) / 2);

		loadClasses();
		loadStudentsByClass();
                rootTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
                     public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        rootTabbedPaneStateChanged(evt);
                    }
                }); //!!HERE
                //!!JR
                hasChanged = false;
                //JR
                lastSelected = 0;
	}

	private void loadClasses() {
		for (String cName : controller.getClassNames()) {
			sessionListModel.addElement((String) cName);
		}
		classList.setSelectedIndex(0);
	}

	private void loadStudentsByClass() {
            studentListModel = new DefaultListModel();
		studentList.setModel(studentListModel);
                ArrayList<User> unsortedList = new ArrayList<User>();

		for (User u : controller.getUsers()) {

			if (classList.getSelectedValue() != null
					&& !u.isInstructor()
					&& classList.getSelectedValue().toString()
							.equalsIgnoreCase(u.getClassName())) {
                            //!!KL
                            //Ketty: Changed getRealName() to getUserName()
                            //!!JK : sorted the list
                                unsortedList.add(u);
                                
				//studentListModel.addElement((String) u.getUserName());
                            //KL
			}
		}
                Collections.sort(unsortedList,
                                new Comparator<User>() {
                                    public int compare(User user1, User user2) {
                                        return user1.getUserName().compareToIgnoreCase(user2.getUserName());
                                    }
                                });

                for (User userName : unsortedList){
                    studentListModel.addElement(userName.getUserName());
                }
                //JK
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rootTabbedPane = new javax.swing.JTabbedPane();
        scenarioManagerPanel = new javax.swing.JPanel();
        scenarioScrollPanel = new javax.swing.JScrollPane();
        scenarioList = new javax.swing.JList();
        selectScenarioLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        previewTabbedPane = new javax.swing.JTabbedPane();
        marPanel = new javax.swing.JPanel();
        medScrollPane = new javax.swing.JScrollPane();
        medJTable = new javax.swing.JTable();
        roomNumLabel = new javax.swing.JLabel();
        diagnosisLabel = new javax.swing.JLabel();
        patientNameLabel = new javax.swing.JLabel();
        allergiesLabel = new javax.swing.JLabel();
        roomNumberTextField = new javax.swing.JTextField();
        allergiesScrollPane = new javax.swing.JScrollPane();
        allergiesTextArea = new javax.swing.JTextArea();
        patientNameTextField = new javax.swing.JTextField();
        diagnosisScrollPane = new javax.swing.JScrollPane();
        diagnosisTextArea = new javax.swing.JTextArea();
        addMedicationButton = new javax.swing.JButton();
        removeMedicationButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        timeTextfield = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        editHourDueButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        documentationPanel = new javax.swing.JPanel();
        documentationScrollPane = new javax.swing.JScrollPane();
        documentationTable = new javax.swing.JTable();
        addNarrativeButton = new javax.swing.JButton();
        removeAllNarrativeButton = new javax.swing.JButton();
        viewSelectedNarrativeButton = new javax.swing.JButton();
        deleteNarrativeButton = new javax.swing.JButton();
        editNarrativeButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        scenarioSummaryPanel = new javax.swing.JPanel();
        scenarioSummaryLabel = new javax.swing.JLabel();
        ScenarioScrollPane = new javax.swing.JScrollPane();
        scenarioSummaryTextPane = new javax.swing.JTextPane();
        clearSummaryButton = new javax.swing.JButton();
        saveChangesButton = new javax.swing.JButton();
        scenarioPreviewLabel = new javax.swing.JLabel();
        UndoAllChangesButton = new javax.swing.JButton();
        addScenarioButton = new javax.swing.JButton();
        removeScenarioButton = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        importExportAreaLabel = new javax.swing.JLabel();
        importScenarioButton = new javax.swing.JButton();
        exportScenarioButton = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        professorLoginManager = new javax.swing.JPanel();
        currentProfessorPasswordLabel = new javax.swing.JLabel();
        changeProffesorPasswordButton = new javax.swing.JButton();
        currentProfessorUserNameLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        studentManagerPanel = new javax.swing.JPanel();
        classScrollPane = new javax.swing.JScrollPane();
        classList = new javax.swing.JList();
        studentScrollPane = new javax.swing.JScrollPane();
        studentList = new javax.swing.JList();
        classScrollPaneLabel = new javax.swing.JLabel();
        studentScrollPaneLabel = new javax.swing.JLabel();
        //!!KL
        addClassButton = new javax.swing.JButton();
        //!!KL
        addStudentButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        //!!KL
        removeClassButton = new javax.swing.JButton();
        //!!KL
        removeStudentButton = new javax.swing.JButton();
        //!!KL
        jSeparator3 = new javax.swing.JSeparator();
        //!!KL
        removalAreaLabel = new javax.swing.JLabel();
        selectionArealabel = new javax.swing.JLabel();
        //!!KL
        addingAreaLabel = new javax.swing.JLabel();
        studentManagerControlTabbedPane = new javax.swing.JTabbedPane();
        classControlPanel = new javax.swing.JPanel();
        classControlScrollPane = new javax.swing.JScrollPane();
        //!!KL
        classControlJTable = new javax.swing.JTable();
        //!!KL
        averageClassScoreLabel = new javax.swing.JLabel();
        //!!KL
        printAllStudentRecordsButton = new javax.swing.JButton();
        studentControlPanel = new javax.swing.JPanel();
        studentControlScrollPane = new javax.swing.JScrollPane();
        //!!KL
        studentTable = new javax.swing.JTable();
        //!!KL
        viewSelectedScenarioButton = new javax.swing.JButton();
        studentNameLabel = new javax.swing.JLabel();
        SimulationScoreLabel = new javax.swing.JLabel();
        //!!KL
        changePasswordButton = new javax.swing.JButton();
        //!!KL
        changeUserNameButton = new javax.swing.JButton();
        //!!KL
        jSeparator6 = new javax.swing.JSeparator();
        LoginModLabel = new javax.swing.JLabel();
        //!!KL
        currentStudentUserNameLabel = new javax.swing.JLabel();
        //!!KL
        currentStudentPasswordLabel = new javax.swing.JLabel();
        //!!KL
        SimResultsAreaLabel = new javax.swing.JLabel();
        //!!KL
        setScenarioScoreButton = new javax.swing.JButton();
        //!!KL
        jLabel3 = new javax.swing.JLabel();
        //!!KL
        jLabel4 = new javax.swing.JLabel();
        //!!KL
        editSelectedResultSuggestionButton = new javax.swing.JButton();
        //!!KL
        deleteSelectedResultButton = new javax.swing.JButton();
        //!!KL
        printSelectedStudentRecordButton = new javax.swing.JButton();
        //!!KL
        jLabel12 = new javax.swing.JLabel();
        //!!KL
        currentStudentRealNameLabel = new javax.swing.JLabel();
        //!!KL
        changeRealNameButton = new javax.swing.JButton();
        logOutButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulation Maintenance Manager");
        setResizable(false);

        rootTabbedPane.setPreferredSize(new java.awt.Dimension(1037, 727));
        rootTabbedPane.setVerifyInputWhenFocusTarget(false);

        scenarioManagerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scenarioListModel = new DefaultListModel();
        scenarioListModel.add(0,"CS225 TESTING");
        scenarioListModel.add(0,"CS225 TESTING");
        scenarioListModel.add(0,"CS225 TESTING");
        scenarioListModel.add(0,"CS225 TESTING");
        scenarioList.setModel(scenarioListModel);
        scenarioList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scenarioList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                scenarioListValueChanged(evt);
            }
        });
        scenarioScrollPanel.setViewportView(scenarioList);

        scenarioManagerPanel.add(scenarioScrollPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 260, 76));

        selectScenarioLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        selectScenarioLabel.setText("Select Scenario to Edit");
        scenarioManagerPanel.add(selectScenarioLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jSeparator4.setPreferredSize(new java.awt.Dimension(0, 10));
        scenarioManagerPanel.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 1030, 10));

        marPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        medJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Medication", "Dose", "Route", "Hour Due"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        medJTable.setRowHeight(45);
        medJTable.getTableHeader().setReorderingAllowed(false);
        medScrollPane.setViewportView(medJTable);

        marPanel.add(medScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 31, 932, 243));

        roomNumLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        roomNumLabel.setText("Room:");
        marPanel.add(roomNumLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        diagnosisLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        diagnosisLabel.setText("Diagnosis:");
        marPanel.add(diagnosisLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, -1, -1));

        patientNameLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        patientNameLabel.setText("Name:");
        marPanel.add(patientNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, -1, -1));

        allergiesLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        allergiesLabel.setText("Allergies:");
        marPanel.add(allergiesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 320, -1, -1));

        roomNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                roomNumberTextFieldKeyTyped(evt);
            }
        });
        marPanel.add(roomNumberTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 53, -1));

        allergiesTextArea.setColumns(20);
        allergiesTextArea.setRows(5);
        allergiesTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                allergiesTextAreaKeyPressed(evt);
            }
        });
        allergiesScrollPane.setViewportView(allergiesTextArea);

        marPanel.add(allergiesScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 320, 324, 121));

        patientNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                patientNameTextFieldKeyPressed(evt);
            }
        });
        marPanel.add(patientNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 140, -1));

        diagnosisTextArea.setColumns(20);
        diagnosisTextArea.setRows(5);
        diagnosisTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                diagnosisTextAreaKeyPressed(evt);
            }
        });
        diagnosisScrollPane.setViewportView(diagnosisTextArea);

        marPanel.add(diagnosisScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 282, 121));

        addMedicationButton.setText("Add Medication");
        addMedicationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMedicationButtonActionPerformed(evt);
            }
        });
        marPanel.add(addMedicationButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 290, -1, -1));

        removeMedicationButton.setText("Remove Selected Medication");
        removeMedicationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeMedicationButtonActionPerformed(evt);
            }
        });
        marPanel.add(removeMedicationButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 290, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Time limit in min:");
        marPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(764, 289, -1, -1));
        marPanel.add(timeTextfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(864, 286, 47, -1));

        jLabel6.setText("To allow unlimited time, enter 0");
        marPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 320, -1, 10));

        editHourDueButton.setText("Edit selected Hour Due");
        editHourDueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editHourDueButtonActionPerformed(evt);
            }
        });
        marPanel.add(editHourDueButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 290, -1, -1));

        jLabel9.setText("Note: after adding a medication with its Route, you may double click on the area that is empty or with content, so you can edit it.");
        marPanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        previewTabbedPane.addTab("MAR", marPanel);

        documentationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Time", "Narrative", "Follow Up", "Initialls"
            }
        ));
        documentationTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        documentationTable.setName("sample"); // NOI18N
        documentationTable.getTableHeader().setReorderingAllowed(false);
        documentationScrollPane.setViewportView(documentationTable);
        documentationTable.getColumnModel().getColumn(2).setPreferredWidth(625);

        addNarrativeButton.setText("Add New Narrative");
        addNarrativeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNarrativeButtonActionPerformed(evt);
            }
        });

        removeAllNarrativeButton.setText("Remove All Narratives");
        removeAllNarrativeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllNarrativeButtonActionPerformed(evt);
            }
        });

        viewSelectedNarrativeButton.setText("View Selected Narrative");
        viewSelectedNarrativeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSelectedNarrativeButtonActionPerformed(evt);
            }
        });

        deleteNarrativeButton.setText("Delete Selected Narrative");
        deleteNarrativeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteNarrativeButtonActionPerformed(evt);
            }
        });

        editNarrativeButton.setText("Edit Selected Narrative");
        editNarrativeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editNarrativeButtonActionPerformed(evt);
            }
        });

        jLabel10.setText("Note: after adding a medication with its Route, you may double click on the area that is empty so you can edit  ");

        jLabel11.setText("Note: after adding a narrative, you may double click on the area that is empty or with content, so you can edit it.  ");

        javax.swing.GroupLayout documentationPanelLayout = new javax.swing.GroupLayout(documentationPanel);
        documentationPanel.setLayout(documentationPanelLayout);
        documentationPanelLayout.setHorizontalGroup(
            documentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(documentationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(documentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(documentationScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 955, Short.MAX_VALUE)
                    .addGroup(documentationPanelLayout.createSequentialGroup()
                        .addGroup(documentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(documentationPanelLayout.createSequentialGroup()
                                .addComponent(addNarrativeButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(viewSelectedNarrativeButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(editNarrativeButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(deleteNarrativeButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(removeAllNarrativeButton))
                            .addComponent(jLabel11))
                        .addGap(0, 212, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(documentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(documentationPanelLayout.createSequentialGroup()
                    .addGap(0, 221, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addGap(0, 221, Short.MAX_VALUE)))
        );
        documentationPanelLayout.setVerticalGroup(
            documentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(documentationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(12, 12, 12)
                .addComponent(documentationScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(documentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNarrativeButton)
                    .addComponent(viewSelectedNarrativeButton)
                    .addComponent(editNarrativeButton)
                    .addComponent(deleteNarrativeButton)
                    .addComponent(removeAllNarrativeButton))
                .addContainerGap(142, Short.MAX_VALUE))
            .addGroup(documentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(documentationPanelLayout.createSequentialGroup()
                    .addGap(0, 216, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addGap(0, 216, Short.MAX_VALUE)))
        );

        previewTabbedPane.addTab("Documentation", documentationPanel);

        scenarioSummaryLabel.setText("Scenario Summary");

        ScenarioScrollPane.setViewportView(scenarioSummaryTextPane);

        clearSummaryButton.setText("Clear Summary");
        clearSummaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSummaryButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout scenarioSummaryPanelLayout = new javax.swing.GroupLayout(scenarioSummaryPanel);
        scenarioSummaryPanel.setLayout(scenarioSummaryPanelLayout);
        scenarioSummaryPanelLayout.setHorizontalGroup(
            scenarioSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scenarioSummaryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scenarioSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scenarioSummaryLabel)
                    .addComponent(ScenarioScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearSummaryButton))
                .addContainerGap(488, Short.MAX_VALUE))
        );
        scenarioSummaryPanelLayout.setVerticalGroup(
            scenarioSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scenarioSummaryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scenarioSummaryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScenarioScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearSummaryButton)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        previewTabbedPane.addTab("Summary", scenarioSummaryPanel);

        scenarioManagerPanel.add(previewTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        saveChangesButton.setText("Save Changes");
        saveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChangesButtonActionPerformed(evt);
            }
        });
        scenarioManagerPanel.add(saveChangesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 626, -1, -1));

        scenarioPreviewLabel.setText("Scenario Preview");
        scenarioManagerPanel.add(scenarioPreviewLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, -1, -1));

        UndoAllChangesButton.setText("Undo All");
        UndoAllChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UndoAllChangesButtonActionPerformed(evt);
            }
        });
        scenarioManagerPanel.add(UndoAllChangesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(117, 626, -1, -1));

        addScenarioButton.setText("Add Scenario");
        addScenarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addScenarioButtonActionPerformed(evt);
            }
        });
        scenarioManagerPanel.add(addScenarioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, -1, -1));

        removeScenarioButton.setText("Remove Scenario");
        removeScenarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeScenarioButtonActionPerformed(evt);
            }
        });
        scenarioManagerPanel.add(removeScenarioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, -1, -1));

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        scenarioManagerPanel.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 0, 40, 120));

        importExportAreaLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        importExportAreaLabel.setText("Import and Export Scenario List");
        scenarioManagerPanel.add(importExportAreaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 10, -1, -1));

        importScenarioButton.setText("Import");
        importScenarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importScenarioButtonActionPerformed(evt);
            }
        });
        scenarioManagerPanel.add(importScenarioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 50, 100, 60));

        exportScenarioButton.setText("Export");
        exportScenarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportScenarioButtonActionPerformed(evt);
            }
        });
        scenarioManagerPanel.add(exportScenarioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 50, 110, 60));

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);
        scenarioManagerPanel.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, 30, 120));

        jButton1.setText("Random Scenario");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        scenarioManagerPanel.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, -1, -1));

        rootTabbedPane.addTab("Scenario Manager", scenarioManagerPanel);

        professorLoginManager.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        currentProfessorPasswordLabel.setText("CurrentPassword");
        professorLoginManager.add(currentProfessorPasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 95, -1, -1));

        changeProffesorPasswordButton.setText("Change Password");
        changeProffesorPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeProffesorPasswordButtonActionPerformed(evt);
            }
        });
        professorLoginManager.add(changeProffesorPasswordButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 150, -1));

        currentProfessorUserNameLabel.setText("CurrentUserName");
        professorLoginManager.add(currentProfessorUserNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 41, -1, -1));

        jLabel1.setText("Username:");
        professorLoginManager.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 70, -1));

        jLabel2.setText("Password:");
        professorLoginManager.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 95, -1, -1));

        rootTabbedPane.addTab("Professor Login Manager", professorLoginManager);

        studentManagerPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                studentManagerPanelPropertyChange(evt);
            }
        });
        studentManagerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sessionListModel = new DefaultListModel();
        classList.setModel(
            sessionListModel
        );
        classList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        classList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                classListValueChanged(evt);
            }
        });
        classList.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                classListFocusGained(evt);
            }
        });
        classScrollPane.setViewportView(classList);

        studentManagerPanel.add(classScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 55, 150, 120));

        studentList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        studentList.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                studentListFocusGained(evt);
            }
        });
        studentScrollPane.setViewportView(studentList);

        studentManagerPanel.add(studentScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 55, 200, 120));

        classScrollPaneLabel.setText("Class");
        studentManagerPanel.add(classScrollPaneLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        studentScrollPaneLabel.setText("Student");
        studentManagerPanel.add(studentScrollPaneLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, -1, -1));

        addClassButton.setText("Add Class");
        addClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClassButtonActionPerformed(evt);
            }
        });
        studentManagerPanel.add(addClassButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 130, -1));
        //KL

        addStudentButton.setText("Add Student");
        addStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentButtonActionPerformed(evt);
            }
        });
        studentManagerPanel.add(addStudentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 150, 130, -1));
        //KL

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        studentManagerPanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 0, 8, 180));
        studentManagerPanel.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 183, 1032, 10));

        removeClassButton.setText("Remove Class");
        removeClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeClassButtonActionPerformed(evt);
            }
        });
        studentManagerPanel.add(removeClassButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 130, -1));
        //KL

        removeStudentButton.setText("Remove Student");
        removeStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeStudentButtonActionPerformed(evt);
            }
        });
        studentManagerPanel.add(removeStudentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 150, 130, -1));
        //KL

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        studentManagerPanel.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 0, 10, 180));
        //KL

        removalAreaLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        removalAreaLabel.setForeground(new java.awt.Color(255, 0, 0));
        removalAreaLabel.setText("Removal");
        studentManagerPanel.add(removalAreaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, -1, -1));
        //KL

        selectionArealabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        selectionArealabel.setText("Selection");
        studentManagerPanel.add(selectionArealabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        addingAreaLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        addingAreaLabel.setText("Adding");
        studentManagerPanel.add(addingAreaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, -1, -1));
        //KL

        studentManagerControlTabbedPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        classControlJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Student Name", "Student Username", "Average Score", "Number of Simulations Completed"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        classControlJTable.getTableHeader().setReorderingAllowed(false);
        classControlJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                classControlJTableMouseClicked(evt);
            }
        });
        classControlScrollPane.setViewportView(classControlJTable);
        //KL

        averageClassScoreLabel.setText("Average Class Score:");

        printAllStudentRecordsButton.setText("Print All Student Records in Selected Class");
        printAllStudentRecordsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printAllStudentRecordsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout classControlPanelLayout = new javax.swing.GroupLayout(classControlPanel);
        classControlPanel.setLayout(classControlPanelLayout);
        classControlPanelLayout.setHorizontalGroup(
            classControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(classControlScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1025, Short.MAX_VALUE)
            .addGroup(classControlPanelLayout.createSequentialGroup()
                .addGroup(classControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(classControlPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(averageClassScoreLabel))
                    .addGroup(classControlPanelLayout.createSequentialGroup()
                        .addGap(363, 363, 363)
                        .addComponent(printAllStudentRecordsButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        classControlPanelLayout.setVerticalGroup(
            classControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(classControlScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(averageClassScoreLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(printAllStudentRecordsButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //KL
        //KL

        studentManagerControlTabbedPane.addTab("Class Control", classControlPanel);

        studentControlPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        studentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name of Completed Scenario", "DateTaken", "Suggestion", "Score"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentControlScrollPane.setViewportView(studentTable);
        studentTable.getColumnModel().getColumn(0).setResizable(false);
        studentTable.getColumnModel().getColumn(1).setResizable(false);
        studentTable.getColumnModel().getColumn(2).setResizable(false);
        studentTable.getColumnModel().getColumn(3).setResizable(false);
        //KL

        studentControlPanel.add(studentControlScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 1025, 222));

        viewSelectedScenarioButton.setText("View/Print Selected Scenario Input");
        viewSelectedScenarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSelectedScenarioButtonActionPerformed(evt);
            }
        });
        studentControlPanel.add(viewSelectedScenarioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 300, 210, -1));
        //KL

        studentNameLabel.setText("StudentName");
        studentControlPanel.add(studentNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(444, 0, -1, -1));

        SimulationScoreLabel.setText("AVG Simulation Score");
        studentControlPanel.add(SimulationScoreLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, -1, -1));

        changePasswordButton.setText("Change Student Password");
        changePasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePasswordButtonActionPerformed(evt);
            }
        });
        studentControlPanel.add(changePasswordButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 420, 190, -1));
        //KL

        changeUserNameButton.setText("Change Student Username");
        changeUserNameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeUserNameButtonActionPerformed(evt);
            }
        });
        studentControlPanel.add(changeUserNameButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 330, 190, -1));
        //KL

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        studentControlPanel.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 250, 50, 202));
        //KL

        LoginModLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        LoginModLabel.setText("Login Modification");
        studentControlPanel.add(LoginModLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 250, -1, -1));

        currentStudentUserNameLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        currentStudentUserNameLabel.setText("current Username");
        studentControlPanel.add(currentStudentUserNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 310, -1, -1));
        //KL

        currentStudentPasswordLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        currentStudentPasswordLabel.setText("current Password");
        studentControlPanel.add(currentStudentPasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 400, -1, -1));
        //KL

        SimResultsAreaLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        SimResultsAreaLabel.setText("Simulation Results");
        studentControlPanel.add(SimResultsAreaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, -1, -1));
        //KL

        setScenarioScoreButton.setText("Grade Scenario");
        setScenarioScoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setScenarioScoreButtonActionPerformed(evt);
            }
        });
        studentControlPanel.add(setScenarioScoreButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 210, -1));
        //KL

        jLabel3.setText("Current Student Password");
        studentControlPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 380, -1, -1));
        //KL

        jLabel4.setText("Current Student Username");
        studentControlPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 290, -1, -1));
        //KL

        editSelectedResultSuggestionButton.setText("Edit Suggestion");
        editSelectedResultSuggestionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSelectedResultSuggestionButtonActionPerformed(evt);
            }
        });
        studentControlPanel.add(editSelectedResultSuggestionButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 210, -1));
        //KL

        deleteSelectedResultButton.setText("Delete Result");
        deleteSelectedResultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedResultButtonActionPerformed(evt);
            }
        });
        studentControlPanel.add(deleteSelectedResultButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 210, -1));
        //KL

        printSelectedStudentRecordButton.setText("Print Current Student Record");
        printSelectedStudentRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedStudentRecordButtonActionPerformed(evt);
            }
        });
        studentControlPanel.add(printSelectedStudentRecordButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 330, 210, -1));
        //KL

        jLabel12.setText("Current Student Real Name");
        studentControlPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 290, -1, -1));
        //KL

        currentStudentRealNameLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        currentStudentRealNameLabel.setText("current RealName");
        studentControlPanel.add(currentStudentRealNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 310, -1, -1));
        //KL

        changeRealNameButton.setText("Change Student Name");
        changeRealNameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeRealNameButtonActionPerformed(evt);
            }
        });
        studentControlPanel.add(changeRealNameButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 330, 190, -1));
        //KL

        studentManagerControlTabbedPane.addTab("Student Control", studentControlPanel);

        studentManagerPanel.add(studentManagerControlTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 1030, 490));

        rootTabbedPane.addTab("Student Manager", studentManagerPanel);

        logOutButton.setBackground(new java.awt.Color(255, 0, 0));
        logOutButton.setText("Logout");
        logOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logOutButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(rootTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logOutButton))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewSelectedNarrativeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSelectedNarrativeButtonActionPerformed
        // TODO add your handling code here:
        if (documentationTable.getSelectedRow() < 0)
			JOptionPane.showMessageDialog(this, "Please select a note");
		else {
			JDialog t = new JDialog(this,true);
                        t.setSize(400, 300);
			t.setLocation(
					(t.getToolkit().getScreenSize().width - t.getWidth()) / 2,
					(t.getToolkit().getScreenSize().height - t.getHeight()) / 2);

			t.setTitle(patientNameTextField.getText()
					+ ": "
					+ (String) documentationTable.getValueAt(
							documentationTable.getSelectedRow(), 0)
					+ " at "
					+ (String) documentationTable.getValueAt(
							documentationTable.getSelectedRow(), 1));
			

			JTextPane t2 = new JTextPane();
			t2.setEditable(false);
			JScrollPane pane = new JScrollPane();
			pane.setViewportView(t2);
			t2.setText((String) documentationTable.getValueAt(
					documentationTable.getSelectedRow(), 2));
			t.add(pane);

			t.setVisible(true);
			t.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
    }//GEN-LAST:event_viewSelectedNarrativeButtonActionPerformed

    private void deleteNarrativeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteNarrativeButtonActionPerformed
        // TODO add your handling code here:
        
            if(documentationTable.getSelectedRow()>-1)
                ((DefaultTableModel)documentationTable.getModel()).removeRow(documentationTable.getSelectedRow());
            else if (documentationTable.getSelectedRow() < 0)
			JOptionPane.showMessageDialog(this, "Please select a note");
    }//GEN-LAST:event_deleteNarrativeButtonActionPerformed

    private void editNarrativeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editNarrativeButtonActionPerformed
        // TODO add your handling code here:
        if (documentationTable.getSelectedRow() < 0)
			JOptionPane.showMessageDialog(this, "Please select a note");
                
		else {
			final JDialog t = new JDialog(this,true);
			t.setLayout(new GridLayout(1, 2));

			t.setSize(800, 300);
			t.setLocation(
					(t.getToolkit().getScreenSize().width - t.getWidth()) / 2,
					(t.getToolkit().getScreenSize().height - t.getHeight()) / 2);
                        
                        

			t.setTitle(patientNameLabel.getText()
					+ ": "
					+ (String) documentationTable.getValueAt(
							documentationTable.getSelectedRow(), 0)
					+ " at "
					+ (String) documentationTable.getValueAt(
							documentationTable.getSelectedRow(), 1));

			JTextPane t2 = new JTextPane();
			t2.setSize(350, 300);

			final JTextPane t3 = t2;

			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			// t2.setEditable(false);
			JScrollPane pane = new JScrollPane();
			pane.setViewportView(t2);
			t2.setText((String) documentationTable.getValueAt(
					documentationTable.getSelectedRow(), 2));

			t.add(pane);

			JButton saveButton = new JButton("Save");
			saveButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					documentationTable.setValueAt((Object) t3.getText(),
							documentationTable.getSelectedRow(), 2);
					t.dispose();
				}
			});
			panel.add(saveButton);
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					t.dispose();
				}
			});
			panel.add(cancelButton);

			t.add(panel);

			t.setVisible(true);
			t.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
    }//GEN-LAST:event_editNarrativeButtonActionPerformed

    private void editHourDueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editHourDueButtonActionPerformed
        // TODO add your handling code here:
        //!!JR
        hasChanged = true;
        //JR
        
         if (medJTable.getSelectedRow() < 0)
                        //!!KL
                        // Just added a period to sentence.
			JOptionPane.showMessageDialog(this, "Please select a medication to edit.");
                        //KL
                
		else {
			final JDialog t = new JDialog(this,true);
			t.setLayout(new GridLayout(1, 2));

			t.setSize(800, 300);
			t.setLocation(
					(t.getToolkit().getScreenSize().width - t.getWidth()) / 2,
					(t.getToolkit().getScreenSize().height - t.getHeight()) / 2);
                        
                        

//			t.setTitle(patientNameLabel.getText()
//					+ ": "
//					+ (String) documentationTable.getValueAt(
//							documentationTable.getSelectedRow(), 0)
//					+ " at "
//					+ (String) documentationTable.getValueAt(
//							documentationTable.getSelectedRow(), 1));

			JTextPane t2 = new JTextPane();
			t2.setSize(350, 300);

			final JTextPane t3 = t2;

			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			// t2.setEditable(false);
			JScrollPane pane = new JScrollPane();
			pane.setViewportView(t2);
			t2.setText((String) medJTable.getValueAt(
					medJTable.getSelectedRow(), 3));

			t.add(pane);

			JButton saveButton = new JButton("Save");
			saveButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					medJTable.setValueAt((Object) t3.getText(),
					  medJTable.getSelectedRow(), 3);
					t.dispose();
				}
			});
			panel.add(saveButton);
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					t.dispose();
				}
			});
			panel.add(cancelButton);

			t.add(panel);

			t.setVisible(true);
			t.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
    
    }//GEN-LAST:event_editHourDueButtonActionPerformed

    private void clearSummaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSummaryButtonActionPerformed
        // TODO add your handling code here:
        scenarioSummaryTextPane.setText("");
    }//GEN-LAST:event_clearSummaryButtonActionPerformed

    private void deleteSelectedResultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSelectedResultButtonActionPerformed
        // TODO add your handling code here:
        
        if(studentTable.getSelectedRow()>-1){
            int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this result?",null , JOptionPane.YES_NO_OPTION);
            if(answer== JOptionPane.NO_OPTION)
                return;
            Student student = controller.getStudentByNameAndClassroom((String)studentList.getSelectedValue(), (String)classList.getSelectedValue());
            if(student!=null){
                student.getCompletedScenarios().remove(studentTable.getSelectedRow());
               
               int i = studentList.getSelectedIndex();
                studentList.clearSelection();
                studentList.setSelectedIndex(i);
                //classListValueChanged(null);
                 controller.writeUsers();
                
            }
            else
        //!!KL
        // Ketty: Tweaked both sentences.
                JOptionPane.showMessageDialog(this,"Please make sure you have the student highlighted in the selection list.",null, JOptionPane.OK_OPTION);
            
        }
        else
            JOptionPane.showMessageDialog(this,"Please select a result to remove.",null, JOptionPane.OK_OPTION);
        //KL
    }//GEN-LAST:event_deleteSelectedResultButtonActionPerformed

    private void editSelectedResultSuggestionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSelectedResultSuggestionButtonActionPerformed
        // TODO add your handling code here:
        if (studentTable.getSelectedRow() < 0)
                        //!!KL
			JOptionPane.showMessageDialog(this, "Please select a scenario suggestion to edit.");
                        //KL
		else {
   final Student student = controller.getStudentByNameAndClassroom((String)studentList.getSelectedValue(), (String)classList.getSelectedValue());

			final JDialog t = new JDialog(this,true);
			t.setLayout(new GridLayout(1, 2));

			t.setSize(800, 300);
			t.setLocation(
					(t.getToolkit().getScreenSize().width - t.getWidth()) / 2,
					(t.getToolkit().getScreenSize().height - t.getHeight()) / 2);
                        
                        

//			t.setTitle(patientNameLabel.getText()
//					+ ": "
//					+ (String) documentationTable.getValueAt(
//							documentationTable.getSelectedRow(), 0)
//					+ " at "
//					+ (String) documentationTable.getValueAt(
//							documentationTable.getSelectedRow(), 1));

			JTextPane t2 = new JTextPane();
			t2.setSize(350, 300);

			final JTextPane t3 = t2;

			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			// t2.setEditable(false);
			JScrollPane pane = new JScrollPane();
			pane.setViewportView(t2);
			t2.setText((String) studentTable.getValueAt(
					studentTable.getSelectedRow(), 2));

			t.add(pane);

			JButton saveButton = new JButton("Save");
			saveButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					studentTable.setValueAt((Object) t3.getText(),
							studentTable.getSelectedRow(), 2);
                                        student.getCompletedScenarios().get(studentTable.getSelectedRow()).setEvaluationSuggestion(t3.getText());
                                        controller.writeUsers();
					t.dispose();
				}
			});
			panel.add(saveButton);
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					t.dispose();
				}
			});
			panel.add(cancelButton);

			t.add(panel);

			t.setVisible(true);
			t.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
    }//GEN-LAST:event_editSelectedResultSuggestionButtonActionPerformed

    private void printAllStudentRecordsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printAllStudentRecordsButtonActionPerformed
        // TODO add your handling code here:
        if(classList.getSelectedIndex()>-1 && studentList.getModel().getSize()>0){
        try {
			PrinterJob job = PrinterJob.getPrinterJob();
                        int index = classList.getSelectedIndex();
                        
                        if (job.printDialog()){
                            PageFormat format = new PageFormat();
                                format.setOrientation(PageFormat.LANDSCAPE);
                            for(int i =0; i<studentList.getModel().getSize();i++){
                                studentList.setSelectedIndex(i);
                                
                                Student student = controller.getStudentByNameAndClassroom(
                                (String)studentList.getSelectedValue(),
                                (String)classList.getSelectedValue());

                                Printable printable = studentTable.getPrintable(JTable.PrintMode.FIT_WIDTH, 
                                        new MessageFormat(
                                        student.getRealName()+ " "+
                                        student.getClassName()+
                                        " Report as of "+new SimpleDateFormat("M-d-yy").format(Calendar.getInstance().getTime())), 

                                        new MessageFormat(
                                        "Completed Scenarios:"+student.getCompletedScenarios().size()+
                                        //!!KL
                                        " Average Score: "+ (student.getAverageScore()==null?"Not Available":student.getAverageScore()) + 
                                        " Page - {0}"));
                                        //KL
                                        job.setPrintable(printable, format);
                                        job.print();
                            }
                        }
                        studentList.clearSelection();
                        classControlJTable.clearSelection();
                        classList.setSelectedIndex(index);
		} catch (PrinterException ex) {
			Logger.getLogger(SimulationGUI.class.getName()).log(Level.SEVERE,
					null, ex);
		}
            
        }
        //!!KL
        else if(studentList.getModel().getSize()==0&&classList.getSelectedIndex()>-1)
            showMessageDialog(this, "There are currently no student records to print from this class.", null, OK_OPTION);
        else if(classList.getSelectedIndex()<0 && classList.getModel().getSize() > 0)
            showMessageDialog(this, "Please select a class.", null, OK_OPTION);
        //Ketty: Gives error when trying to print all records but there are none.
        else if (classList.getModel().getSize() == 0)
            showMessageDialog(this, "There are no classes to print records from.", "Error", OK_OPTION);
        //KL
    }//GEN-LAST:event_printAllStudentRecordsButtonActionPerformed

    private void printSelectedStudentRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedStudentRecordButtonActionPerformed
        // TODO add your handling code here:
        if(studentList.getSelectedIndex()>-1 && studentTable.getRowCount()>0){
            
 //           Printable printable =  new Printable() {
//
//                @Override
//                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//                   // throw new UnsupportedOperationException("Not supported yet.");
//                    if(pageIndex==0){
//                        System.out.println(pageFormat.getImageableWidth()/12);
//                    Graphics2D g2d = (Graphics2D) graphics;
//                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY()+24);
//                    g2d.setColor(Color.black);
//                    g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
//                    
//                    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
//                    
//                    g2d.drawString("Eric Santana",0,0);
//                    g2d.drawString("CS225\n",0,12);
//                    g2d.drawString("AVG Score: 100", 0, 24);
//                   // g2d.drawString("00000000000000000000000000000000000000000000000000000000000000000000000000000000", 0, 36);
//                    Printable p =  studentTable.getPrintable(JTable.PrintMode.FIT_WIDTH, null, null);
//                   
//                    g2d.translate(-pageFormat.getImageableX(), -24);
//                    p.print(g2d, pageFormat, pageIndex);
//                    
//                    return PAGE_EXISTS;
//                    }
//                    else
//                        return NO_SUCH_PAGE;
//                }
//            };   
            
        try {
			PrinterJob job = PrinterJob.getPrinterJob();
                        
			PageFormat format = new PageFormat();
			format.setOrientation(PageFormat.LANDSCAPE);
                        Student student = controller.getStudentByNameAndClassroom(
                   (String)studentList.getSelectedValue(),
                    (String)classList.getSelectedValue());
                        

                        Printable printable = studentTable.getPrintable(JTable.PrintMode.FIT_WIDTH, 
                                new MessageFormat(
                                student.getRealName()+ " "+
                                student.getClassName()+
                                " Report as of "+new SimpleDateFormat("M-d-yy").format(Calendar.getInstance().getTime())), 
                                
                                new MessageFormat(
                                "Completed Scenarios:"+student.getCompletedScenarios().size()+
                                //!!KL
                                " Average Score: "+ (student.getAverageScore()==null?"Not Available":student.getAverageScore()) + 
                                " Page - {0}"));
                                //KL
                        
			job.setPrintable(printable, format);
			if (job.printDialog())
				job.print();
			
		} catch (PrinterException ex) {
			Logger.getLogger(SimulationGUI.class.getName()).log(Level.SEVERE,
					null, ex);
		}
            
        }
        else
            showMessageDialog(this, "This student does not have any record to print.", null, OK_OPTION);
        
        
        
//        Student student = controller.getStudentByNameAndClassroom(
//                  (String)studentList.getSelectedValue(),
//                    (String)classList.getSelectedValue());
//        SimulationPrinter.printStudentRecord(student, studentTable.getModel(), studentTable.getColumnModel());
    }//GEN-LAST:event_printSelectedStudentRecordButtonActionPerformed

    
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        controller.addRandomizedScenario();
        ArrayList<Scenario> temp = controller.getScenarios();
        scenarioListModel.addElement(temp.get(temp.size()-1).getPatientName());
    }//GEN-LAST:event_jButton1ActionPerformed

//!!KL
// Added button to change student's real name.
private void changeRealNameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeRealNameButtonActionPerformed
// TODO add your handling code here:
    String realNamePattern = "[a-zA-Z \\-?]*";
    
    if(studentManagerControlTabbedPane.getSelectedIndex()==1 && studentList.getSelectedIndex()>-1){
                    User student = null;
                    for(User user:  controller.getStudentsInClass((String)classList.getSelectedValue())){
                        //Ketty: Changed getRealName() to getUserName()
                        if(!user.isInstructor()&& user.getUserName().equals(studentList.getSelectedValue())&&
                                user.getClassName().equals(classList.getSelectedValue())){
                            student = user;
                            break;
                        }
                   
                    }
                    if(student!=null){
                    
                    String newRealName = JOptionPane.showInputDialog("Enter a new name:");
                    if(newRealName!= null){
                        if(!newRealName.equals("") && newRealName.matches(realNamePattern)) {
                        student.setRealName(newRealName);
                        }
                        //!!KL
                        else if (!newRealName.equals("") && !newRealName.matches(realNamePattern))
                        showMessageDialog(this, "The Real Name field only allows letters, spaces and dashes.", "Error", OK_OPTION);
                        //KL
                        else{
                            showMessageDialog(this,"Please enter an appropriate name.",null, OK_OPTION);
                        }
                        
                        controller.writeUsers();
                        studentManagerControlTabbedPane.setSelectedIndex(0);
                        studentManagerControlTabbedPane.setSelectedIndex(1);
                        
                    }                 
                }
    }
}//GEN-LAST:event_changeRealNameButtonActionPerformed

private void studentManagerPanelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_studentManagerPanelPropertyChange
// TODO add your handling code here:
}//GEN-LAST:event_studentManagerPanelPropertyChange


//!!KL
private void classControlJTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_classControlJTableMouseClicked
// TODO add your handling code here:
    if (evt.getClickCount() == 2) {
        System.out.println("Yes, you doubleclicked me."); //Test
        studentList.setSelectedIndex(classControlJTable.getSelectedRow());
        studentManagerControlTabbedPane.setSelectedIndex(1);
    }
}//GEN-LAST:event_classControlJTableMouseClicked
//KL


//!!JR
    private void roomNumberTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roomNumberTextFieldKeyTyped
        // TODO add your handling code here:
        hasChanged = true;
    }//GEN-LAST:event_roomNumberTextFieldKeyTyped

    private void patientNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_patientNameTextFieldKeyPressed
        // TODO add your handling code here:
        hasChanged = true;
    }//GEN-LAST:event_patientNameTextFieldKeyPressed

    private void diagnosisTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diagnosisTextAreaKeyPressed
        // TODO add your handling code here:
        hasChanged = true;
    }//GEN-LAST:event_diagnosisTextAreaKeyPressed

    private void allergiesTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_allergiesTextAreaKeyPressed
        // TODO add your handling code here:
        hasChanged = true; 
    }//GEN-LAST:event_allergiesTextAreaKeyPressed
//JR
    //KL
	private void classListValueChanged(javax.swing.event.ListSelectionEvent evt) {
		// change students based on selected class
		loadStudentsByClass();
                while( ((DefaultTableModel)classControlJTable.getModel()).getRowCount()>0)
               ((DefaultTableModel)classControlJTable.getModel()).removeRow(0);
               
                Integer sumAVG =null;
                int total = controller.getStudentsInClass((String)classList.getSelectedValue()).size();
               for(User user:controller.getStudentsInClass((String)classList.getSelectedValue())){
                   ((DefaultTableModel)classControlJTable.getModel()).addRow(new Object[]{
                       user.getRealName(),
                       //!!KL
                       //Added username column to table.
                       user.getUserName(),
                       //KL
                      (((Student)user).getAverageScore() != null ? ((Student)user).getAverageScore():"Not Available"),
                       new Integer( ((Student)user).getCompletedScenarios().size())});
                   if(((Student)user).getAverageScore()!=null){
                      if(sumAVG==null)
                          sumAVG = 0;
                       sumAVG += ((Student)user).getAverageScore();
                       
                   }
                   else if(((Student)user).getAverageScore()==null)
                           total --;
                   
               }
               if(!controller.getStudentsInClass((String)classList.getSelectedValue()).isEmpty() && sumAVG!=null && total>0){
                   averageClassScoreLabel.setText("Average Class Score: "+ sumAVG/total);
               }
               else if(sumAVG ==null)
                  averageClassScoreLabel.setText("Average Class Score: "+ "Not Available"); 
               else
                  averageClassScoreLabel.setText("Average Class Score: "+ "Not Available");

               
	}

	private void viewSelectedScenarioButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
            
            if(studentTable.getSelectedRow()>-1){
            
            Student student = controller.getStudentByNameAndClassroom((String)studentList.getSelectedValue(), (String)classList.getSelectedValue());
            if(student!=null){
                SimulationGUI preview = new SimulationGUI(student.getCompletedScenarios().get(studentTable.getSelectedRow()).getScenarioTaken(),
                        
                        student.getCompletedScenarios().get(studentTable.getSelectedRow()).getStudentInput());
                
            }
            //!!KL
            else
                JOptionPane.showMessageDialog(this,"Please make sure you have the student highlighted in the selection list.",null, JOptionPane.OK_OPTION);
            
        }
        else
            JOptionPane.showMessageDialog(this,"Please select a result to view/print.",null, JOptionPane.OK_OPTION);
            //KL	
	}

    public JTable getStudentTable() {
        return studentTable;
    }
        

	private void setScenarioScoreButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
             if(studentTable.getSelectedRow()>-1){
           
                 int index = studentTable.getSelectedRow();
            Student student = controller.getStudentByNameAndClassroom((String)studentList.getSelectedValue(), (String)classList.getSelectedValue());
            if(student!=null){
                try{
                //!!KL
                // Ketty: Changed sentences in all four message dialogs.  
                String o = JOptionPane.showInputDialog(this,"Please enter the numerical score for this result:");
   
                if(o!=null){
                int score = Integer.parseInt(o);
                student.getCompletedScenarios().get(studentTable.getSelectedRow()).setScore(score);
                controller.writeUsers();
                int i = studentList.getSelectedIndex();
                studentList.clearSelection();
                studentList.setSelectedIndex(i);
                studentTable.setRowSelectionInterval(index, index);
                }
                
                }
                catch(NumberFormatException e){
                    showMessageDialog(this, "Please enter a whole number only. Setting the score is cancelled.","Error", OK_OPTION);
                    return;
                }
                
               // classListValueChanged(null);
                
            }
            else
                JOptionPane.showMessageDialog(this,"Please make sure you have the student highlighted in the selection list.",null, JOptionPane.OK_OPTION);
        }
        else
            JOptionPane.showMessageDialog(this,"Please select a result to grade.",null, JOptionPane.OK_OPTION);
	}
        //KL
        
	private void ViewScenarioSuggestionButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
            if (studentTable.getSelectedRow() < 0)
                //!!KL
			JOptionPane.showMessageDialog(this, "Please select a completed scenario from the list.");
                //KL
		else {
			JDialog t = new JDialog(this,true);
                        t.setSize(400, 300);
			t.setLocation(
					(t.getToolkit().getScreenSize().width - t.getWidth()) / 2,
					(t.getToolkit().getScreenSize().height - t.getHeight()) / 2);

			t.setTitle((String) studentTable.getValueAt(
					studentTable.getSelectedRow(), 0)
					+ " at "+ (String) studentTable.getValueAt(
					studentTable.getSelectedRow(), 1));
			

			JTextPane t2 = new JTextPane();
			t2.setEditable(false);
			JScrollPane pane = new JScrollPane();
			pane.setViewportView(t2);
			t2.setText((String) studentTable.getValueAt(
					studentTable.getSelectedRow(), 2));
			t.add(pane);

			t.setVisible(true);
			t.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
	}

	private void changePasswordButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
            if(studentManagerControlTabbedPane.getSelectedIndex()==1 && studentList.getSelectedIndex()>-1){
                    User student = null;
                    
                    //!!KL
                    
                    for(User user:  controller.getStudentsInClass((String)classList.getSelectedValue())){
                        //Ketty: Changed getRealName() to getUserName()
                        if(!user.isInstructor()&& user.getUserName().equals(studentList.getSelectedValue())&&
                                user.getClassName().equals(classList.getSelectedValue())){
                            student = user;
                            break;
                        }
                   
                    }
                    
                    if(student!=null){
                    // Ketty: Adjusted both message dialogs.
                    String newUsername = JOptionPane.showInputDialog("Enter a new password:");
                    if(newUsername!= null){
                        if(!newUsername.equals(""))
                        student.setPassword(newUsername);
                        else{
                            showMessageDialog(this,"Please enter an appropriate password.",null, OK_OPTION);
                        }
                        controller.writeUsers();
                        studentManagerControlTabbedPane.setSelectedIndex(0);
                        studentManagerControlTabbedPane.setSelectedIndex(1);
                    }
                    //KL
                        
                    }
                }
            
	}

	private void addNarrativeButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		DateFormat dayFormat = new SimpleDateFormat("HH:mma");
		((DefaultTableModel) documentationTable.getModel())
				.addRow(new String[] {
						dateFormat.format(Calendar.getInstance(
								TimeZone.getDefault()).getTime()),
						dayFormat.format(Calendar.getInstance(
								TimeZone.getDefault()).getTime()),
						"Temperature:"
                                        + "\nPulse:"
                        + "\nResp:\n"
                        + "BP:\n"
                        + "O2 Sat:\n"
                        + "Pain Scale:\n"
                        + "FSBS:\n"
                        + "Site:\n"
                        + "Related Diagnosis/Reason for medication:\n" });
		// documentationTable.isfo
                documentationTable.setRowSelectionInterval(
					documentationTable.getRowCount() - 1,
					documentationTable.getRowCount() - 1);
                editNarrativeButtonActionPerformed(null);
	}        

	private void removeNarrativeButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void removeAllNarrativeButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
            while(((DefaultTableModel)documentationTable.getModel()).getRowCount()>0)
            ((DefaultTableModel)documentationTable.getModel()).removeRow(0);
	}
private void rootTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {                                            
        // TODO add your handling code here:
        if(lastSelected == 0 && hasChanged)
        {
            //!!JR
            hasChanged = false;
            rootTabbedPane.setSelectedIndex(0);
            int returnVal = JOptionPane.showConfirmDialog(this,"Save changes to Scenarios?","Confirm Save",JOptionPane.YES_NO_OPTION);
            if(returnVal == JOptionPane.YES_OPTION){
                saveChangesButtonActionPerformed(null);
        }
            //System.out.println("yup");
            //JR
        }
        lastSelected = rootTabbedPane.getSelectedIndex();
    }     
	private void importScenarioButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
            if(showConfirmDialog(this, "Are you sure you want to import a list of scenarios from a file?\n"
                    + "By doing so, it replaces your current list of scenarios.\n"
                    + "You may backup your list by exporting your list by clicking export. ", "Import Verification", YES_NO_OPTION) == YES_OPTION){
            DataIO io = new DataIO();
            
            JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter(".LST Files","lst");
            if(JFileChooser.APPROVE_OPTION==fileChooser.showOpenDialog(this)){
                
                if(filter.accept(fileChooser.getSelectedFile()))
                {
                    ArrayList<Scenario> scenarios = io.loadScenarioList(fileChooser.getSelectedFile().getAbsolutePath());
                    if(scenarios!= null){
                        for(Scenario s : scenarios)
                        {
                            controller.getScenarios().add(s);
                            controller.writeScenarios();
                            rootTabbedPane.setSelectedIndex(0);
                            rootTabbedPane.setSelectedIndex(1);
                        }
                    if(scenarioList.getModel().getSize()>0)
                        scenarioList.setSelectedIndex(0);
                    }
                    else
                        JOptionPane.showMessageDialog(this, "There were no Scenarios in this file.");
                }
                else
                    JOptionPane.showMessageDialog(this, "Incorrect file type.  Only .lst files are accepted.");
            }
            }
	}

	private void exportScenarioButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
            DataIO io = new DataIO();
            
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showSaveDialog(this);
            if(JFileChooser.APPROVE_OPTION==returnVal){
                if(fileChooser.getSelectedFile().exists())
                {
                    int n = JOptionPane.showConfirmDialog(this,fileChooser.getSelectedFile().getName() + " already exists, would you like to overwrite?","Overwrite Confirmation" ,JOptionPane.YES_NO_OPTION);
                    if(n == JOptionPane.YES_OPTION){
                        io.writeScenarioList(fileChooser.getSelectedFile().getAbsolutePath()+".lst",controller.getScenarios());
                    }
                }
                else
                    io.writeScenarioList(fileChooser.getSelectedFile().getAbsolutePath()+".lst",controller.getScenarios());
            }
                
	}

	private void UndoAllChangesButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
            int num = scenarioList.getSelectedIndex();
            scenarioList.clearSelection();
            scenarioList.setSelectedIndex(num);
	}

	private void sortByNameButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void sortByScoreButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

//!!KL
// Deleted removeAll button. Commented out its code.
//KL
        
/*	private void removeAllButtonActionPerformed(java.awt.event.ActionEvent evt) {
		int n = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to remove all student and class data?",
				"Confirm Removal", JOptionPane.YES_NO_OPTION);

		if (n == JOptionPane.YES_OPTION) {
			int x = JOptionPane
					.showConfirmDialog(
							this,
							"Are you sure you want to remove all student and class data?",
							"Confirm Removal", JOptionPane.YES_NO_OPTION);
		}

		if (n == JOptionPane.YES_OPTION) {
			controller.removeAllStudents();
			controller.writeUsers();
			loadStudentsByClass();
		}
	}*/

	private void changeProffesorPasswordButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
            String password = JOptionPane.showInputDialog(this, "Enter a new password");
            if(password!=null){
                controller.getUsers().get(0).setPassword(password);
                controller.writeUsers();
                rootTabbedPane.setSelectedIndex(1);
                rootTabbedPane.setSelectedIndex(2);
                
                
            }
	}

	private void changeProffessorUsernameButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void logOutButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (JOptionPane.showConfirmDialog(this,
				"Are you sure you want to log out?", null,
				JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
			// System.exit(0);

			controller.writeClassNames();
			controller.writeScenarios();
			controller.writeUsers();
			SimulationManager.state = SimulationManager.LOGIN_STATE;
                        studentList.clearSelection();
                        classList.clearSelection();

			setVisible(false);
		}
	}

	private void removeScenarioButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
            
            if(scenarioList.getSelectedIndex()>-1){
		int n = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete this scenario?",
				"Confirm Removal", JOptionPane.YES_NO_OPTION);

		if (n == JOptionPane.YES_OPTION) {
			String scenario = (String) scenarioListModel.get(scenarioList
					.getSelectedIndex());// use this string wisely!!! :)
			scenarioListModel.remove(scenarioList.getSelectedIndex());
			if (scenarioListModel.getSize() == 0) {
				//previewTabbedPane.setVisible(false);
			}
                        controller.removeScenarioByName(scenario);
                        if(((DefaultListModel)scenarioList.getModel()).size()>0)
                            scenarioList.setSelectedIndex(0);
		}
            }
            else if(((DefaultListModel)scenarioList.getModel()).size()==0){
                showMessageDialog(this, "There is no Scenario to remove", null, OK_OPTION);
                
            }
            else{
                showMessageDialog(this, "Please select a Scenario to remove", null, OK_OPTION);
            }
                
                
	}

	private void addScenarioButtonActionPerformed(java.awt.event.ActionEvent evt) {

		String scenarioName = JOptionPane.showInputDialog(this,"Enter a scenario name.");
		if (scenarioName.length() != 0) {
                        controller.getScenarios().add(new Scenario(scenarioName));
                        controller.writeScenarios();
			scenarioListModel.addElement(scenarioName);
		}
                else
                    JOptionPane.showMessageDialog(this,"Must enter a name.");
		scenarioList.setSelectedIndex(scenarioListModel.getSize() - 1);
		if (!previewTabbedPane.isVisible()) {
			previewTabbedPane.setVisible(true);
		}
		// TODO implement adding a new scenario to the DataStructure
	}

        /**
         * completed
         * @author Eric
         * @param evt 
         */
	private void saveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {

		String selectedScenarioName = (String) scenarioList.getSelectedValue();
                //!!JR
                int position = scenarioList.getSelectedIndex();
                scenarioListModel.remove(scenarioList.getSelectedIndex());
                scenarioListModel.insertElementAt(patientNameTextField.getText(),position);
		//JR
		if (selectedScenarioName != null) {
                        
                    
                        
                    
			Scenario scenario = controller
					.getScenarioByName(selectedScenarioName);
                        try{
                            int time = Integer.parseInt(timeTextfield.getText());
                            if(!(time<0))
                            scenario.setTime(time);
                            else{
                                JOptionPane.showMessageDialog(this, "Please enter a number that is not negative");
                                return;
                            }
                        }
                        catch(NumberFormatException e){
                            JOptionPane.showMessageDialog(this, "Please only enter numbers for a time limit");
                            return;
                        }
                        try{
                            scenario.setRoom(Integer.parseInt(roomNumberTextField.getText()));
                        }
                        catch(NumberFormatException e){
                            JOptionPane.showMessageDialog(this, "Please enter only numbers for room number");
                            return;
                        }
                        

			// templates for setters when recording/editing a scenario.
			//scenario.setAge(ag );
			scenario.setAllergies(allergiesTextArea.getText());
			scenario.setSummary(scenarioSummaryTextPane.getText());
			
			// scenario.setPatientName(selectedScenarioName);
			scenario.getPatientRecord().setName( patientNameTextField.getText());
                        
                        ArrayList<Medication> medications = new ArrayList<Medication>();
                        int num = 0;
                        while( num< ((DefaultTableModel)medJTable.getModel()).getRowCount()){
                            String medi[] = new String[]{
                                (String)((DefaultTableModel)medJTable.getModel()).getValueAt(num, 0),
                                (String)((DefaultTableModel)medJTable.getModel()).getValueAt(num, 1),
                                (String)((DefaultTableModel)medJTable.getModel()).getValueAt(num, 2),
                                (String)((DefaultTableModel)medJTable.getModel()).getValueAt(num, 3)
                            };
                            medications.add(new Medication(medi[0],medi[1],medi[2],medi[3]));
                            num++;
                        }

			num=0;
			scenario.setMedicationList(medications);
                        
                        ArrayList<Narrative> narratives = new ArrayList<Narrative>();
                         num = 0;
                        while( num< ((DefaultTableModel)documentationTable.getModel()).getRowCount()){
                            String medi[] = new String[]{
                                (String)((DefaultTableModel)documentationTable.getModel()).getValueAt(num, 0),
                                (String)((DefaultTableModel)documentationTable.getModel()).getValueAt(num, 1),
                                (String)((DefaultTableModel)documentationTable.getModel()).getValueAt(num, 2),
                                (String)((DefaultTableModel)documentationTable.getModel()).getValueAt(num, 3),
                                (String)((DefaultTableModel)documentationTable.getModel()).getValueAt(num, 4)
                            };
                            narratives.add(new Narrative(medi[0],medi[1],medi[2],medi[3],medi[4]));
                            num++;
                        }

                        
                        
			scenario.setDiagnosis(diagnosisTextArea.getText());
			scenario.setNarrativeList(narratives);
                        controller.writeScenarios();
                        
                        //!!JR
                        //int i = scenarioList.getSelectedIndex();
                        //rootTabbedPane.setSelectedIndex(0);
                        //rootTabbedPane.setSelectedIndex(1);
                        scenarioList.setSelectedIndex(position);
                        //EJR
                      
                        
                        
		}
                //!!JR
                hasChanged = false;
                //JR
	}

	private void removeMedicationButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
            //!!JR
            hasChanged = true;
            //JR

            
            if(medJTable.getSelectedRow()>-1){
		((DefaultTableModel) medJTable.getModel()).removeRow(medJTable
				.getSelectedRow());
            }
            //!!JR
            else {
                showMessageDialog(this, "Please select a medication to remove", "Removing Error", OK_OPTION);
	}
            //JR
	}

	private void addMedicationButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
                //!!JR
                hasChanged = true;
		String route  = (String) JOptionPane.showInputDialog(this,
				"Please select the route of medication:",
				"Route of medication selection", JOptionPane.QUESTION_MESSAGE,
				null, new Object[] { "AD","AS","AU","Buccal","GT","ID","IM","Inhalation","IV","IVP","Nasal","NGT",
                                        "OD","OS","OU","PEG","PO","PR","SC","SL","Topical", "Transdermal", "Vaginal" }, "AD");
		if (route != null) {
			((DefaultTableModel) medJTable.getModel()).addRow(new Object[] {
					"", "", route });
		}
                //JR
	}

	private void scenarioListValueChanged(
			javax.swing.event.ListSelectionEvent evt) {
		if (scenarioList.getSelectedIndex() >= 0) {
			updateScenarioPreview((String) scenarioListModel.get(scenarioList
					.getSelectedIndex()));
		}
	}

	private void studentListFocusGained(java.awt.event.FocusEvent evt) {
		studentManagerControlTabbedPane.setSelectedIndex(1);
	}

	private void classListFocusGained(java.awt.event.FocusEvent evt) {
		studentList.clearSelection();
		studentManagerControlTabbedPane.setSelectedIndex(0);
                
                int loc = classList.getSelectedIndex();
                classList.clearSelection();
                classList.setSelectedIndex(loc);
                
               
	}
        
    //!!KL    
        
	private void removeStudentButtonActionPerformed(
			java.awt.event.ActionEvent evt) {

                
		//if (studentList.getSelectedIndex() != -1) {
            if (classControlJTable.getSelectedRow() != -1){
                    //Ketty: Added temp student object to obtain real name for use in JOptionPane window.
                    // Edited second message dialog.
                   studentList.setSelectedIndex(classControlJTable.getSelectedRow());
                   
                    
                   Student student1 = controller.getStudentByNameAndClassroom((String)studentList.getSelectedValue(), (String)classList.getSelectedValue());
                    
                   if (student1 != null) {
                       if(JOptionPane.showConfirmDialog(this, "Are you sure you want to remove "+student1.getRealName()+
                               "?", null, JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION){
                    
                      if (student1.getCompletedScenarios().size() > 0) {
                        if (JOptionPane.showConfirmDialog(this, "Would you like to print " + student1.getRealName() 
                            + "'s student record before removal?", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                            try {
			PrinterJob job = PrinterJob.getPrinterJob();
                        
			PageFormat format = new PageFormat();
			format.setOrientation(PageFormat.LANDSCAPE);
                        Student student = controller.getStudentByNameAndClassroom(
                                         (String)studentList.getSelectedValue(),
                                         (String)classList.getSelectedValue());
                        

                        Printable printable = studentTable.getPrintable(JTable.PrintMode.FIT_WIDTH, 
                                new MessageFormat(
                                student.getRealName()+ " "+
                                student.getClassName()+
                                " Report as of "+new SimpleDateFormat("M-d-yy").format(Calendar.getInstance().getTime())), 
                                
                                new MessageFormat(
                                "Completed Scenarios:"+student.getCompletedScenarios().size()+
                                //!!KL
                                " Average Score: "+ (student.getAverageScore()==null?"Not Available":student.getAverageScore()) + 
                                " Page - {0}"));
                                //KL
                        
			job.setPrintable(printable, format);
			if (job.printDialog())
				job.print();

                            }
		 catch (PrinterException ex) {
			Logger.getLogger(SimulationGUI.class.getName()).log(Level.SEVERE,
					null, ex);
		}
                            
                         } 
                       }
                      
                      String name = ((String)studentList.getSelectedValue());
                      controller.removeStudentByNameAndClassroom(name,(String) classList.getSelectedValue());
                      studentList.clearSelection();
                      studentManagerControlTabbedPane.setSelectedIndex(0);
                      classListValueChanged(null);
                    }
                  }
                }
                       
                else
                    JOptionPane.showMessageDialog(this, "Please select a student to remove.", "Student Removal", JOptionPane.OK_OPTION); 
       }     
        //KL
        
        //!!KL
        //Ketty: Added !classList.isSelectionEmpty() and "No classes currently available" dialog box.
	private void addStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {

            if(classList.getModel().getSize()>0 && !classList.isSelectionEmpty()){
              AddStudentDialog t =  new AddStudentDialog(this,true); 
              
           }
           else if (classList.getModel().getSize() < 0)
               showMessageDialog(this, "No classes are currently available. To add a student, first create a class.", "No Class Error", OK_OPTION);
           else
               showMessageDialog(this, "Please select which class you wish to add a student to.", null, OK_OPTION);
	}
        //KL
        
	private void addClassButtonActionPerformed(java.awt.event.ActionEvent evt) {

		// duplicate class name found should be handled
		// this code will only prompt for re-entry once.

		String className = JOptionPane.showInputDialog(this,
				"Enter the class name:");

                if(className!=null){
		if (!className.equals("") && controller.addClass(className)) {
			sessionListModel.addElement(className);
			controller.writeClassNames();
		} else {
			// class name exists - prompt re-entry.
			 showMessageDialog(this,
					"Invalid or already existing class name.","Error", OK_OPTION);
		}
                }
	}

        //!!KL
        // Added option of printing class's student records before deleting class.
	private void removeClassButtonActionPerformed(ActionEvent evt) {

            if(classList.getSelectedIndex()>-1){
		int n = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete "+ (String)classList.getSelectedValue() +" and its students?",
				"Confirm Removal", JOptionPane.YES_NO_OPTION);

		if (n == JOptionPane.YES_OPTION) {
			if (controller.removeClass((String) classList.getSelectedValue())
					&& classList.getSelectedIndex() > -1) {
                            
                            if (JOptionPane.showConfirmDialog(this, "Would you like to print this class's student records before removal?",
                                    null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                try {
			PrinterJob job = PrinterJob.getPrinterJob();
                        int index = classList.getSelectedIndex();
                        
                        if (job.printDialog()){
                            PageFormat format = new PageFormat();
                                format.setOrientation(PageFormat.LANDSCAPE);
                            for(int i =0; i<studentList.getModel().getSize();i++){
                                studentList.setSelectedIndex(i);
                                
                                Student student = controller.getStudentByNameAndClassroom(
                                (String)studentList.getSelectedValue(),
                                (String)classList.getSelectedValue());

                                Printable printable = studentTable.getPrintable(JTable.PrintMode.FIT_WIDTH, 
                                        new MessageFormat(
                                        student.getRealName()+ " "+
                                        student.getClassName()+
                                        " Report as of "+new SimpleDateFormat("M-d-yy").format(Calendar.getInstance().getTime())), 

                                        new MessageFormat(
                                        "Completed Scenarios:"+student.getCompletedScenarios().size()+
                                        //!!KL
                                        " Average Score: "+ (student.getAverageScore()==null?"Not Available":student.getAverageScore()) + 
                                        " Page - {0}"));
                                        //KL
                                        job.setPrintable(printable, format);
                                        job.print();
                            }
                        }
                        studentList.clearSelection();
                        classControlJTable.clearSelection();
                        classList.setSelectedIndex(index);
		} catch (PrinterException ex) {
			Logger.getLogger(SimulationGUI.class.getName()).log(Level.SEVERE,
					null, ex);
		}
                            }
				sessionListModel.remove(classList.getSelectedIndex());
                                for(User u:controller.getUsers()){
                                    if(!u.isInstructor())
                                    if(u.getClassName().equals((String)classList.getSelectedValue()))
                                        controller.getUsers().remove(u);
                                }
                                controller.writeUsers();
				controller.writeClassNames();

		}}
            }
            else
                JOptionPane.showMessageDialog(this, "Please select a class to remove.", null, JOptionPane.OK_OPTION);
	}
        //KL
        
        /**
         * this needs to validate if there is another username instance before changing username
         * @param evt 
         */
        
        //!!KL
	private void changeUserNameButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
            // Ketty: Added userNamePattern. Indicates which characters are allowed to be inputted.
            String userNamePattern = "[a-zA-Z0-9\\.?\\_?]*";
            
            if(studentManagerControlTabbedPane.getSelectedIndex()==1 && studentList.getSelectedIndex()>-1){
                    User student = null;
                    for(User user:  controller.getStudentsInClass((String)classList.getSelectedValue())){
                        // Ketty: Changed getRealName() to getUserName()
                        if(!user.isInstructor()&& user.getUserName().equals(studentList.getSelectedValue())&&
                                user.getClassName().equals(classList.getSelectedValue())){
                            student = user;
                            break;
                        }
                   
                    }
                    if(student!=null){
//                    for(CompletedScenario completedScenario:((Student)student).getCompletedScenarios()){
//                        ((DefaultTableModel) studentTable.getModel()).addRow(
//                                new String[]{completedScenario.getScenarioTaken().getPatientName(),
//                                    completedScenario.getDateTaken(),
//                                    completedScenario.getEvaluationSuggestion(),
//                                    ""+(completedScenario.getScore()==0? "A score hasn't been issued yet": completedScenario.getScore())} );
//                        
//                    }
                    
                    // Ketty: Edited the following three dialog messages and added a fourth one.
                    // Also added "newUsername.matches..." in following if statement.
                    String newUsername = JOptionPane.showInputDialog("Enter a new username:");
                   
                    //!!KL
                    if(newUsername!= null){
                        if(controller.isUserNameAvailable(newUsername)){
                        
                        
                        if(!newUsername.equals("") && newUsername.matches(userNamePattern))
                            student.setUserName(newUsername);
                        
                        else if (!newUsername.equals("") && !newUsername.matches(userNamePattern))
                        showMessageDialog(this, "The Username field only allows letters, numbers, periods and underscores.", "Error", OK_OPTION);
                        else{
                            showMessageDialog(this,"Please enter an appropriate username.",null, OK_OPTION);
                        }
                        controller.writeUsers();
                        int sIndex = studentList.getSelectedIndex();
                        int cIndex = classList.getSelectedIndex();
                        classList.clearSelection();
                        classList.setSelectedIndex(cIndex);
                        studentList.setSelectedIndex(sIndex);
                        
                        }
                        else
                            showMessageDialog(this,"Sorry, this username is already in use.",null,OK_OPTION);
                    }
                    

                    //KL
                    }
                }
            
	}
       //KL
        
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/*
		 * Set the Nimbus look and feel
		 */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(
					MaintenanceManagerGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(
					MaintenanceManagerGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(
					MaintenanceManagerGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(
					MaintenanceManagerGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		File n = new File(System.getProperty("user.dir"));

		/*
		 * Create and display the form
		 */
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				new MaintenanceManagerGUI().setVisible(true);
			}
		});
	}

	/**
	 * Load all resources for this GUI.
	 */
	public void loadResources() {
	}

	/**
	 * This method updates the Scenario preview(the componentes of
	 * previewTabbedPane) by reloading the data stru
	 * 
	 * @param string
	 *            name of the scenario to load to the preview from data
	 *            structure.
	 */
	private void updateScenarioPreview(String string) {
		// scenarioListModel.addElement("Awsome");
	}

	@Override
	public void setVisible(boolean b) {
		// TODO implement loading Datastructer into this GUI.
		if (b) {
			loadResources();
                        rootTabbedPane.setSelectedIndex(1);
                       rootTabbedPane.setSelectedIndex(0);
                       studentManagerControlTabbedPane.setSelectedIndex(0);
                       studentManagerControlTabbedPane.setSelectedIndex(1);
                       studentManagerControlTabbedPane.setSelectedIndex(0);
                       
                       if(classList.getModel().getSize()>0)
                           classList.setSelectedIndex(0);
                       if(scenarioList.getModel().getSize()>0)
                           scenarioList.setSelectedIndex(0);
                       
                       int loc = classList.getSelectedIndex();
                       classList.clearSelection();
                       classList.setSelectedIndex(loc);
                       
		}
		super.setVisible(b);
	}
        /**
 *
 * @author Eric Santana
 */
    private class AddStudentDialog extends javax.swing.JDialog {

        /**
        * Creates new form AddStudentDialog
        */
        
        //!!KL
        // Ketty: Commented out any classListS; we are not using a class list within the AddStudentDialog anymore.
        //KL
        
        public AddStudentDialog(java.awt.Frame parent, boolean modal) {
            super(parent, modal);
            initComponents();
           // classListS.setListData(((DefaultListModel)classList.getModel()).toArray());
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setLocation(
					(getToolkit().getScreenSize().width - this.getWidth()) / 2,
					(getToolkit().getScreenSize().height - this.getHeight()) / 2);
            setVisible(true);
        }

        /**
        * This method is called from within the constructor to initialize the form.
        * WARNING: Do NOT modify this code. The content of this method is always
        * regenerated by the Form Editor.
        */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        //!!KL
        private void initComponents() {

            // Ketty: Commented out jLabel4, jScrollPanel and classListS.
            // Anything having to do with them is also commented out.
            
            jLabel1 = new javax.swing.JLabel();
            studentRealNameTextField = new javax.swing.JTextField();
            jLabel2 = new javax.swing.JLabel();
            studentUserNameTextField = new javax.swing.JTextField();
            jLabel3 = new javax.swing.JLabel();
            studentPasswordTextField = new javax.swing.JTextField();
           // jLabel4 = new javax.swing.JLabel();
           // jScrollPane1 = new javax.swing.JScrollPane();
           // classListS = new javax.swing.JList();
            addStudentButton = new javax.swing.JButton();
            cancelButton = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Add Student");
            setResizable(false);

            jLabel1.setText("Student's Real Name:");

            jLabel2.setText("Desired Username:");

            jLabel3.setText("Desired Password:");

            /*jLabel4.setText("Class:");

            classListS.setModel(new javax.swing.AbstractListModel() {
                String[] strings = { };
                public int getSize() { return strings.length; }
                public Object getElementAt(int i) { return strings[i]; }
            });
            classListS.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jScrollPane1.setViewportView(classListS);*/

            addStudentButton.setText("Add Student");
            addStudentButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addStudentButtonActionPerformed(evt);
                }
            });

            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cancelButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(studentUserNameTextField)
                                .addComponent(studentRealNameTextField)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                              //  .addComponent(jLabel4)
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(addStudentButton)
                                    .addGap(18, 18, 18)
                                    .addComponent(cancelButton)
                                    .addGap(0, 0, Short.MAX_VALUE))
                               // .addComponent(jScrollPane1)
                                .addComponent(studentPasswordTextField))))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(studentRealNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(studentUserNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(studentPasswordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      //  .addComponent(jLabel4)
                      //  .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addStudentButton)
                        .addComponent(cancelButton))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            pack();
        }// </editor-fold>

        //!!KL
        private void addStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {
            String realNamePattern = "[a-zA-Z \\-?]*";
            String userNamePattern = "[a-zA-Z0-9\\.?\\_?]*";
            
            if(!studentRealNameTextField.getText().isEmpty()&& studentRealNameTextField.getText().matches(realNamePattern) &&
               !studentUserNameTextField.getText().isEmpty()&& studentUserNameTextField.getText().matches(userNamePattern) &&
               !studentPasswordTextField.getText().isEmpty() &&
              // Ketty: Edited classListS to select from classList instead. Anywhere you see a line with classList used to be classListS.
                classList.getSelectedIndex()>-1)
            {
                boolean isUsernameAvailable = controller.isUserNameAvailable(studentUserNameTextField.getText());
                if(isUsernameAvailable){
                    controller.addStudent(
                            studentUserNameTextField.getText(),
                            studentRealNameTextField.getText(), 
                            studentPasswordTextField.getText(),
                            // classListS
                            (String)classList.getSelectedValue());
                    controller.writeUsers();
                    // classListS
                    int classIndex = classList.getSelectedIndex();
                    classList.clearSelection();
                    classList.setSelectedIndex(classIndex);
                    
                    dispose();
                }
                else{
                    showMessageDialog(this, "The username provided is not available. Please provide another.","Username Not Available", OK_OPTION);
                }   
            }
            
            // Ketty: Different dialogs for different errors.
            
            else if (studentRealNameTextField.getText().isEmpty() ||
                     studentUserNameTextField.getText().isEmpty() ||
                     studentPasswordTextField.getText().isEmpty()
                    )
                showMessageDialog(this, "Please fill in all required fields in order to add a student.","Error", OK_OPTION);
            else if (!studentRealNameTextField.getText().matches(realNamePattern))
                showMessageDialog(this, "The Real Name field only allows letters, spaces and dashes.", "Error", OK_OPTION);
            else if (!studentUserNameTextField.getText().matches(userNamePattern))
                showMessageDialog(this, "The Username field only allows letters, numbers, periods and underscores.", "Error", OK_OPTION);
        }
        //KL

        private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
            dispose();
        }
        // Variables declaration - do not modify
        
        //!!KL
        /* Ketty: Modified to remove jScrollPanel list of classes.
           Classes should be selected in the class selection field only from now on. */
        
        private javax.swing.JButton addStudentButton;
        private javax.swing.JButton cancelButton;
       // private javax.swing.JList classListS;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
       // private javax.swing.JLabel jLabel4;
       // private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTextField studentPasswordTextField;
        private javax.swing.JTextField studentRealNameTextField;
        private javax.swing.JTextField studentUserNameTextField;
        // End of variables declaration
        
        //KL
    }
        

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LoginModLabel;
    private javax.swing.JScrollPane ScenarioScrollPane;
    private javax.swing.JLabel SimResultsAreaLabel;
    private javax.swing.JLabel SimulationScoreLabel;
    private javax.swing.JButton UndoAllChangesButton;
    private javax.swing.JButton addClassButton;
    private javax.swing.JButton addMedicationButton;
    private javax.swing.JButton addNarrativeButton;
    private javax.swing.JButton addScenarioButton;
    private javax.swing.JButton addStudentButton;
    private javax.swing.JLabel addingAreaLabel;
    private javax.swing.JLabel allergiesLabel;
    private javax.swing.JScrollPane allergiesScrollPane;
    private javax.swing.JTextArea allergiesTextArea;
    private javax.swing.JLabel averageClassScoreLabel;
    private javax.swing.JButton changePasswordButton;
    private javax.swing.JButton changeProffesorPasswordButton;
    private javax.swing.JButton changeRealNameButton;
    private javax.swing.JButton changeUserNameButton;
    private javax.swing.JTable classControlJTable;
    private javax.swing.JPanel classControlPanel;
    private javax.swing.JScrollPane classControlScrollPane;
    private javax.swing.JList classList;
    private javax.swing.JScrollPane classScrollPane;
    private javax.swing.JLabel classScrollPaneLabel;
    private javax.swing.JButton clearSummaryButton;
    private javax.swing.JLabel currentProfessorPasswordLabel;
    private javax.swing.JLabel currentProfessorUserNameLabel;
    private javax.swing.JLabel currentStudentPasswordLabel;
    private javax.swing.JLabel currentStudentRealNameLabel;
    private javax.swing.JLabel currentStudentUserNameLabel;
    private javax.swing.JButton deleteNarrativeButton;
    private javax.swing.JButton deleteSelectedResultButton;
    private javax.swing.JLabel diagnosisLabel;
    private javax.swing.JScrollPane diagnosisScrollPane;
    private javax.swing.JTextArea diagnosisTextArea;
    private javax.swing.JPanel documentationPanel;
    private javax.swing.JScrollPane documentationScrollPane;
    private javax.swing.JTable documentationTable;
    private javax.swing.JButton editHourDueButton;
    private javax.swing.JButton editNarrativeButton;
    private javax.swing.JButton editSelectedResultSuggestionButton;
    private javax.swing.JButton exportScenarioButton;
    private javax.swing.JLabel importExportAreaLabel;
    private javax.swing.JButton importScenarioButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JButton logOutButton;
    private javax.swing.JPanel marPanel;
    private javax.swing.JTable medJTable;
    private javax.swing.JScrollPane medScrollPane;
    private javax.swing.JLabel patientNameLabel;
    private javax.swing.JTextField patientNameTextField;
    private javax.swing.JTabbedPane previewTabbedPane;
    private javax.swing.JButton printAllStudentRecordsButton;
    private javax.swing.JButton printSelectedStudentRecordButton;
    private javax.swing.JPanel professorLoginManager;
    private javax.swing.JLabel removalAreaLabel;
    private javax.swing.JButton removeAllNarrativeButton;
    private javax.swing.JButton removeClassButton;
    private javax.swing.JButton removeMedicationButton;
    private javax.swing.JButton removeScenarioButton;
    private javax.swing.JButton removeStudentButton;
    private javax.swing.JLabel roomNumLabel;
    private javax.swing.JTextField roomNumberTextField;
    private javax.swing.JTabbedPane rootTabbedPane;
    private javax.swing.JButton saveChangesButton;
    private javax.swing.JList scenarioList;
    private javax.swing.JPanel scenarioManagerPanel;
    private javax.swing.JLabel scenarioPreviewLabel;
    private javax.swing.JScrollPane scenarioScrollPanel;
    private javax.swing.JLabel scenarioSummaryLabel;
    private javax.swing.JPanel scenarioSummaryPanel;
    private javax.swing.JTextPane scenarioSummaryTextPane;
    private javax.swing.JLabel selectScenarioLabel;
    private javax.swing.JLabel selectionArealabel;
    private javax.swing.JButton setScenarioScoreButton;
    private javax.swing.JPanel studentControlPanel;
    private javax.swing.JScrollPane studentControlScrollPane;
    private javax.swing.JList studentList;
    private javax.swing.JTabbedPane studentManagerControlTabbedPane;
    private javax.swing.JPanel studentManagerPanel;
    private javax.swing.JLabel studentNameLabel;
    private javax.swing.JScrollPane studentScrollPane;
    private javax.swing.JLabel studentScrollPaneLabel;
    private javax.swing.JTable studentTable;
    private javax.swing.JTextField timeTextfield;
    private javax.swing.JButton viewSelectedNarrativeButton;
    private javax.swing.JButton viewSelectedScenarioButton;
    // End of variables declaration//GEN-END:variables
}
