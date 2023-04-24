package org.example;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TextEditor extends JFrame {

    private JTextArea textArea;
    private JLabel statusLabel;

    public TextEditor() {
        setTitle("文本编辑器");
        setSize(500, 500);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        add(new JScrollPane(textArea));

        statusLabel = new JLabel("字数: 0");
        add(statusLabel, "South");

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStatusLabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStatusLabel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStatusLabel();
            }

            private void updateStatusLabel() {
                String text = textArea.getText();
                int wordCount = text.isEmpty() ? 0 : text.trim().split("\\s+").length;
                statusLabel.setText("字数: " + wordCount);
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem openMenuItem = new JMenuItem("打开");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("文本文件", "txt");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(TextEditor.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                        textArea.read(reader, null);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(TextEditor.this,
                                "无法打开文件: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JMenuItem saveMenuItem = new JMenuItem("保存");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("文本文件", "txt");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showSaveDialog(TextEditor.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                        textArea.write(writer);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(TextEditor.this,
                                "无法保存文件: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);





    }

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        editor.setVisible(true);
    }
}

