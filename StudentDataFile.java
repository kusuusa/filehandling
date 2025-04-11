import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class StudentDataFile extends JFrame {
    private static final String FILE_NAME = "students.txt"; // File to store student data

    private JTextField idField, nameField;
    private JTextArea outputArea;

    public StudentDataFile() {
        setTitle("Student File Manager");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> insertStudent());
        inputPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(e -> deleteStudent());
        inputPanel.add(deleteButton);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to insert student into file
    private void insertStudent() {
        String id = idField.getText().trim(); // Keep ID as string to allow leading zeros
        String name = nameField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            outputArea.append("ID and Name cannot be empty.\n");
            return;
        }

        if (studentExists(id)) {
            outputArea.append("Student ID " + id + " already exists.\n");
            return;
        }

        // Append student data to file
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(id + "," + name + "\n");
            outputArea.append("Student " + name + " (ID: " + id + ") added successfully\n");
        } catch (IOException e) {
            outputArea.append("Error writing to file.\n");
        }
    }

    // Method to check if a student ID already exists
    private boolean studentExists(String id) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            outputArea.append("Error reading file.\n");
        }
        return false;
    }

    // Method to delete a student from the file
    private void deleteStudent() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            outputArea.append("Please enter an ID to delete.\n");
            return;
        }

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            outputArea.append("No student records found.\n");
            return;
        }

        java.util.List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            outputArea.append("Error reading file.\n");
            return;
        }

        // Rewrite the file without the deleted student
        if (found) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
                for (String line : lines) {
                    writer.println(line);
                }
                outputArea.append("Student with ID " + id + " deleted successfully\n");
            } catch (IOException e) {
                outputArea.append("Error writing to file.\n");
            }
        } else {
            outputArea.append("No student found with ID " + id + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentDataFile::new);
    }
}