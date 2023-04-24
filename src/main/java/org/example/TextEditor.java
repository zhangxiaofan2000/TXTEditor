package org.example;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TextEditor extends JFrame {

    private JTextArea textArea;
    private JLabel statusLabel;
    private FindAndReplaceDialog findAndReplaceDialog;
    private JButton prevButton, nextButton;
    private int currentPage = 1;
    private int linesPerPage = 10;
    private int pageSize = 500; // 每页显示的字数

    private JLabel pageLabel;
    private int totalPage;

    public TextEditor() {
        setTitle("文本编辑器");
        setSize(800, 600);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setLineWrap(true); // 自动换行
        textArea.setWrapStyleWord(true);

        Font font = new Font("宋体", Font.PLAIN, 20);
        textArea.setFont(font);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);


        statusLabel = new JLabel("字数: 0");
        add(statusLabel, "South");

        // 添加翻页按钮
        JButton prevButton = new JButton("上一页");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    currentPage--;
                    textArea.setCaretPosition((currentPage - 1) * pageSize);
                    updatePageLabel();
                }
            }
        });

        JButton nextButton = new JButton("下一页");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage < totalPage) {
                    currentPage++;
                    textArea.setCaretPosition((currentPage - 1) * pageSize);
                    updatePageLabel();
                }
            }
        });


        // 添加下方的页码标签
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pageLabel = new JLabel("第" + currentPage + "页/共" + totalPage + "页");
        statusPanel.add(prevButton);
        statusPanel.add(nextButton);
        statusPanel.add(pageLabel);
        add(statusPanel, BorderLayout.SOUTH);


        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStatusLabel();
                updatePageLabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStatusLabel();
                updatePageLabel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStatusLabel();
                updatePageLabel();
            }



        });

        JMenuBar menuBar = new JMenuBar();
        Font menufont = new Font("宋体", Font.PLAIN, 16);
        menuBar.setFont(menufont);

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

                        updateStatusLabel();
                        updatePageLabel();

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


        JMenu editMenu = new JMenu("编辑");
        JMenuItem findItem = new JMenuItem("查找和替换...");
        findItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (findAndReplaceDialog == null) {
                    findAndReplaceDialog = new FindAndReplaceDialog(TextEditor.this, textArea);
                } else {
                    findAndReplaceDialog.setVisible(true);
                }
            }
        });
        editMenu.add(findItem);





        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);





    }

    private void updatePageLabel() {
        int charCount = textArea.getText().length();
        totalPage = (int) Math.ceil((double) charCount / pageSize);
        currentPage = Math.min(currentPage, totalPage);
        pageLabel.setText("第" + currentPage + "页/共" + totalPage + "页");
    }


    private void updateStatusLabel() {
        String text = textArea.getText();
        int wordCount = text.isEmpty() ? 0 : text.trim().split("\\s+").length;
        int charCount = text.length();

        statusLabel.setText("字数: " + charCount);
    }



    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        editor.setVisible(true);
    }
}

