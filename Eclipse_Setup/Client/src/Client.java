import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

    private static final long serialVersionUID = 1L;
    private static JButton displayMatrixBtn, newMatrixBtn, multiplyByMatrixBtn, showResultBtn;
    private static JPanel contentPane;
    private static Matrix matrixA;
    private static Matrix matrixB;
    private static Matrix matrixC;
    private static FabriqueInterface fab;

    /**
     * Create the frame.
     */
    public Client() {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	
                    Registry rgsty = LocateRegistry.getRegistry("localhost", 1099);
                    fab = (FabriqueInterface) rgsty.lookup("MyFabrique");

                    setVisible(true);
                    setTitle("Welcome Client");
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    setIconImage(new ImageIcon(this.getClass().getResource("/myAppIcon.png")).getImage());
                    setSize(400, 180);
                    setResizable(false);
                    setLocationRelativeTo(null);

                    contentPane = new JPanel();
                    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
                    setContentPane(contentPane);
                    contentPane.setLayout(null);

                    // New Matrix Button
                    newMatrixBtn = new JButton("New Matrix");
                    newMatrixBtn.setBounds(10, 25, 175, 35);
                    newMatrixBtn.setIcon(new ImageIcon(this.getClass().getResource("/plusIcon.png")));
                    newMatrixBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            getDimensions();
                        }
                    });
                    contentPane.add(newMatrixBtn);

                    // Multiply Matrix Button
                    multiplyByMatrixBtn = new JButton("Multiply By Matrix");
                    multiplyByMatrixBtn.setBounds(10, 85, 175, 35);
                    multiplyByMatrixBtn.setIcon(new ImageIcon(this.getClass().getResource("/multiplyIcon.png")));
                    multiplyByMatrixBtn.setEnabled(false);
                    multiplyByMatrixBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            getOtherDimensions();
                        }
                    });
                    contentPane.add(multiplyByMatrixBtn);

                    // Display Matrix Button
                    displayMatrixBtn = new JButton("Display Matrix");
                    displayMatrixBtn.setBounds(195, 25, 175, 35);
                    displayMatrixBtn.setIcon(new ImageIcon(this.getClass().getResource("/displayIcon.png")));
                    displayMatrixBtn.setEnabled(false);
                    displayMatrixBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            displayMatrixGui(matrixA);
                        }
                    });
                    contentPane.add(displayMatrixBtn);

                    // Show Matrix Button (Multiplication Result)
                    showResultBtn = new JButton("Show Result");
                    showResultBtn.setBounds(195, 85, 175, 35);
                    showResultBtn.setIcon(new ImageIcon(this.getClass().getResource("/goalIcon.png")));
                    showResultBtn.setEnabled(false);
                    showResultBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            showMultiplicationResult();
                        }
                    });
                    contentPane.add(showResultBtn);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        });


    }

    public static void fakeMain() {

    }

    // Show Multiplication Result
    private void showMultiplicationResult() {
        try {
            RMIInterface mul = (RMIInterface) fab.newMultiplication(matrixA, matrixB);
            matrixC = mul.multiply(matrixA, matrixB);
            displayMatrixGui(matrixC);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Something wrong happened! Please try again !", "Failure", JOptionPane.ERROR_MESSAGE, new ImageIcon(this.getClass().getResource("/sadIcon.png")));
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get Matrix Dimensions 
    private void getDimensions() {

        // Check If Matrix is already initialised , if true => Fill Matrix Process 
        if (matrixA != null) {
            setMatrixElements(matrixA);
            return;
        }

        // Field Text
        final int min = 1, max = 5;
        JTextField rowField = new JTextField(5);
        JTextField colField = new JTextField(5);
        ImageIcon warningIcon = new ImageIcon(this.getClass().getResource("/warningIcon.png"));
        rowField.setDocument(new JTextFieldLimit(2));
        colField.setDocument(new JTextFieldLimit(2));

        // Design Structure   
        JPanel choosePanel[] = new JPanel[2];
        choosePanel[0] = new JPanel();
        choosePanel[1] = new JPanel();
        choosePanel[0].add(new JLabel("Enter Dimensions"));
        choosePanel[1].add(new JLabel("Rows:"));
        choosePanel[1].add(rowField);
        choosePanel[1].add(Box.createHorizontalStrut(15));
        choosePanel[1].add(new JLabel("Cols:"));
        choosePanel[1].add(colField);

        int option = JOptionPane.showConfirmDialog(this, choosePanel, "Fill Matrix", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Click Cancel or Exit
        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) return;

        // Get input field text
        String rowStr = rowField.getText();
        String colStr = colField.getText();

        // Check if provided text is integer or not
        if (!isInt(rowStr) || !isInt(colStr)) {
            JOptionPane.showMessageDialog(this, "Params should be integer !", "Failure", JOptionPane.WARNING_MESSAGE, warningIcon);
            return;
        }

        // Convert String to Integer
        int colInt = convertStringToInt(colStr);
        int rowInt = convertStringToInt(rowStr);

        // Check if the provided integer is between the given range
        if (!intInterval(rowInt, min, max) || !intInterval(colInt, min, max)) {
            JOptionPane.showMessageDialog(this, "Cols And Rows should be between " + min + " and " + max + " !", "Failure", JOptionPane.WARNING_MESSAGE, warningIcon);
            return;
        }

        // Create Matrix Object
        matrixA = new Matrix(rowInt, colInt);

        // Fill Matrix Process
        setMatrixElements(matrixA);

    }


    //setting matrix's elements
    private void setMatrixElements(Matrix matrix) {

        // Create Generate Button 
        JButton generateBtn = new JButton("Generate");
        generateBtn.setBounds(200, 115, 175, 35);
        generateBtn.setIcon(new ImageIcon(this.getClass().getResource("/generateIcon.png")));

        // Design Matrix
        final int rows = matrix.getRows(), cols = matrix.getColumns();
        JPanel choosePanel[] = new JPanel[rows + 2];
        choosePanel[0] = new JPanel();
        choosePanel[0].add(generateBtn);
        choosePanel[choosePanel.length - 1] = new JPanel();
        choosePanel[choosePanel.length - 1].add(new JLabel("Consider empty fields as zeros"));
        JTextField[][] inputFields = new JTextField[rows][cols];

        // Draw Matrix
        for (int i = 1; i <= rows; i++) {
            choosePanel[i] = new JPanel();
            for (int j = 0; j < cols; j++) {
                JTextField textField = new JTextField(3);
                textField.setDocument(new JTextFieldLimit(2));
                inputFields[i - 1][j] = textField;
                choosePanel[i].add(inputFields[i - 1][j]);
                if (j < cols - 1) choosePanel[i].add(Box.createHorizontalStrut(15));
            }
        }

        // Fill Input fields with random numbers
        generateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillInputFields(inputFields, rows, cols);
            }
        });

        // Show The GUI
        int option = JOptionPane.showConfirmDialog(this, choosePanel, "Fill Matrix", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If Cancel button or Close button is clicked, we call the garbage collector to destroy the instance
        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            displayMatrixBtn.setEnabled(false);
            multiplyByMatrixBtn.setEnabled(false);
            showResultBtn.setEnabled(false);

            if (matrixA != null) matrixA = null;
            if (matrixB != null) matrixB = null;

            System.gc();
            return;
        }

        // Grab All Data From Text Input Fields
        checkInputFieldsValue(inputFields, rows, cols);
        getInputFieldsValue(inputFields, matrixA, rows, cols);
        displayMatrixBtn.setEnabled(true);
        multiplyByMatrixBtn.setEnabled(true);

    }


    // Get Multiply By Matrix Dimensions 
    private void getOtherDimensions() {

        // Check If Matrix is already initialised , if true => Fill Matrix Process 
        if (matrixB != null) {
            setMultiplyByMatrixElements(matrixB);
            return;
        }

        // Field Text
        final int min = 1, max = 5;
        JTextField rowField = new JTextField(5);
        JTextField colField = new JTextField(5);
        ImageIcon warningIcon = new ImageIcon(this.getClass().getResource("/warningIcon.png"));
        rowField.setDocument(new JTextFieldLimit(2));
        rowField.setText(convertIntToString(matrixA.getColumns()));
        rowField.setEditable(false);
        colField.setDocument(new JTextFieldLimit(2));

        // Design Structure   
        JPanel choosePanel[] = new JPanel[2];
        choosePanel[0] = new JPanel();
        choosePanel[1] = new JPanel();
        choosePanel[0].add(new JLabel("Enter Dimensions"));
        choosePanel[1].add(new JLabel("Rows:"));
        choosePanel[1].add(rowField);
        choosePanel[1].add(Box.createHorizontalStrut(15));
        choosePanel[1].add(new JLabel("Cols:"));
        choosePanel[1].add(colField);

        int option = JOptionPane.showConfirmDialog(this, choosePanel, "Fill Matrix", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Click Cancel or Exit
        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) return;

        // Get input field text
        String colStr = colField.getText();

        // Check if provided text is integer or not
        if (!isInt(colStr)) {
            JOptionPane.showMessageDialog(this, "Params should be integer !", "Failure", JOptionPane.WARNING_MESSAGE, warningIcon);
            return;
        }

        // Convert String to Integer
        int colInt = convertStringToInt(colStr);

        // Check if the provided integer is between the given range
        if (!intInterval(colInt, min, max)) {
            JOptionPane.showMessageDialog(this, "Cols should be between " + min + " and " + max + " !", "Failure", JOptionPane.WARNING_MESSAGE, warningIcon);
            return;
        }

        // Create Matrix Object      
        matrixB = new Matrix(matrixA.getColumns(), colInt);

        // Fill Matrix Process
        setMultiplyByMatrixElements(matrixB);

    }

    // Setting Multiply Bymatrix's elements
    private void setMultiplyByMatrixElements(Matrix matrix) {

        // Create Generate Button 
        JButton generateBtn = new JButton("Generate");
        generateBtn.setBounds(200, 115, 175, 35);
        generateBtn.setIcon(new ImageIcon(this.getClass().getResource("/generateIcon.png")));

        // Design Matrix
        final int rows = matrix.getRows(), cols = matrix.getColumns();
        JPanel choosePanel[] = new JPanel[rows + 2];
        choosePanel[0] = new JPanel();
        choosePanel[0].add(generateBtn);
        choosePanel[choosePanel.length - 1] = new JPanel();
        choosePanel[choosePanel.length - 1].add(new JLabel("Consider empty fields as zeros"));
        JTextField[][] inputFields = new JTextField[rows][cols];

        // Draw Matrix
        for (int i = 1; i <= rows; i++) {
            choosePanel[i] = new JPanel();
            for (int j = 0; j < cols; j++) {
                JTextField textField = new JTextField(3);
                textField.setDocument(new JTextFieldLimit(2));
                inputFields[i - 1][j] = textField;
                choosePanel[i].add(inputFields[i - 1][j]);
                if (j < cols - 1) choosePanel[i].add(Box.createHorizontalStrut(15));
            }
        }

        // Fill Input fields with random numbers
        generateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillInputFields(inputFields, rows, cols);
            }
        });

        // Show The GUI
        int option = JOptionPane.showConfirmDialog(this, choosePanel, "Fill Matrix", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If Cancel button or Close button is clicked, we call the garbage collector to destroy the instance
        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            showResultBtn.setEnabled(false);
            if (matrixB != null) matrixB = null;

            System.gc();
            return;
        }

        // Grab All Data From Text Input Fields
        checkInputFieldsValue(inputFields, rows, cols);
        getInputFieldsValue(inputFields, matrixB, rows, cols);
        displayMatrixGui(matrixB);
        showResultBtn.setEnabled(true);

    }

    // Display Matrix GUI
    private void displayMatrixGui(Matrix matrix) {
        JPanel choosePanel[] = new JPanel[matrix.getRows()];
        for (int i = 0; i < matrix.getRows(); i++) {
            choosePanel[i] = new JPanel();
            for (int j = 0; j < matrix.getColumns(); j++) {
                choosePanel[i].add(new JLabel(convertIntToString(matrix.getMatrix()[i][j])));
                if (j < matrix.getColumns() - 1) choosePanel[i].add(Box.createHorizontalStrut(15));
            }
        }
        JOptionPane.showMessageDialog(this, choosePanel, "Display Matrix", JOptionPane.PLAIN_MESSAGE, null);
    }


    // Fill Input Fields With Random Integers
    private void getInputFieldsValue(JTextField[][] inputFields, Matrix matrix, int rows, int columns) {
        int tempMatrix[][] = matrix.getMatrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                tempMatrix[i][j] = convertStringToInt(inputFields[i][j].getText());
        }
        matrix.setMatrix(tempMatrix);
    }

    // For setting spaced fields as zeros
    private void checkInputFieldsValue(JTextField[][] inputFields, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                if (!isInt(inputFields[i][j].getText())) inputFields[i][j].setText("0");
        }
    }

    // Fill Input Fields With Random Integers
    private void fillInputFields(JTextField[][] inputFields, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                inputFields[i][j].setText(convertIntToString(generateRandomInt(-5, 5)));
        }
    }

    // Convert Integer To String
    private String convertIntToString(int number) {
        return String.valueOf(number);
    }

    // Generate Random Integer
    private int generateRandomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    // Check if the given string is an integer integer
    private boolean isInt(String str) {
        Pattern pattern = Pattern.compile("^([+-]?[0-9]\\d*|0)$");
        return pattern.matcher(str).matches();
    }

    // Convert String to integer
    @SuppressWarnings({
        "finally"
    })
    private int convertStringToInt(String str) {
        int number = 0;
        try {
            number = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } finally {
            return number;
        }
    }

    // Test if number between the given range
    private boolean intInterval(int number, int min, int max) {
        return number >= min && number <= max;
    }

}