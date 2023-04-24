package org.example;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FindAndReplaceDialog extends JDialog {

    private JTextArea textArea; // 编辑器的文本区域
    private JTextField findField; // 查找输入框
    private JTextField replaceField; // 替换输入框
    private JRadioButton wholeRadio; // 全部范围单选按钮
    private JRadioButton selectionRadio; // 选择范围单选按钮
    private JButton findNextButton; // 查找下一个按钮
    private JButton replaceButton; // 替换按钮
    private JButton replaceAllButton; // 全部替换按钮

    public FindAndReplaceDialog(JFrame parent, JTextArea textArea) {
        super(parent, "查找和替换", true);
        this.textArea = textArea;
        initComponents();
    }

    private void initComponents() {
        // 创建组件
        JLabel findLabel = new JLabel("查找:");
        findField = new JTextField(20);
        JLabel replaceLabel = new JLabel("替换为:");
        replaceField = new JTextField(20);
        wholeRadio = new JRadioButton("全部范围");
        selectionRadio = new JRadioButton("选择范围");
        ButtonGroup scopeGroup = new ButtonGroup();
        scopeGroup.add(wholeRadio);
        scopeGroup.add(selectionRadio);
        wholeRadio.setSelected(true);
        findNextButton = new JButton("查找下一个");
        replaceButton = new JButton("替换");
        replaceAllButton = new JButton("全部替换");

        // 布局组件
        JPanel contentPane = new JPanel(new BorderLayout());
        JPanel inputPane = new JPanel(new FlowLayout());
        JPanel scopePane = new JPanel(new FlowLayout());
        JPanel buttonPane = new JPanel(new FlowLayout());
        inputPane.add(findLabel);
        inputPane.add(findField);
        inputPane.add(replaceLabel);
        inputPane.add(replaceField);
        scopePane.add(wholeRadio);
        scopePane.add(selectionRadio);
        buttonPane.add(findNextButton);
        buttonPane.add(replaceButton);
        buttonPane.add(replaceAllButton);
        contentPane.add(inputPane, BorderLayout.NORTH);
        contentPane.add(scopePane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        setContentPane(contentPane);

        // 添加事件处理
        findNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                String target = findField.getText();
                int index = -1;
                if (wholeRadio.isSelected()) {
                    int start = textArea.getSelectionEnd(); // 从选择的文本结束位置开始搜索
                    index = text.indexOf(target, start);
                    textArea.setSelectionEnd(index + target.length());

                } else if (selectionRadio.isSelected()) {
                    int start = textArea.getSelectionStart();
                    int end = textArea.getSelectionEnd();

                    index = text.indexOf(target, start);
                    end = end > start ? end : start;
                    if (index >= end) {
                        index = -1;
                    }
                }
                if (index != -1) {
                    textArea.setSelectionStart(index);
                    textArea.setSelectionEnd(index + target.length());
                } else {
                    JOptionPane.showMessageDialog(FindAndReplaceDialog.this,
                            "已经到文件末尾，没有找到匹配的文本", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        replaceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                String target = findField.getText();
                String replacement = replaceField.getText();
                int index = -1;
                if (wholeRadio.isSelected()) {
                    index = text.indexOf(target);
                } else if (selectionRadio.isSelected()) {
                    int start = textArea.getSelectionStart();
                    int end = textArea.getSelectionEnd();
                    index = text.indexOf(target, start);
                }
                if (index != -1) {
                    textArea.replaceRange(replacement, index, index + target.length());
                    textArea.setSelectionStart(index + replacement.length());
                    textArea.setSelectionEnd(index + replacement.length());
                } else {
                    JOptionPane.showMessageDialog(FindAndReplaceDialog.this,
                            "没有找到匹配的文本", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        replaceAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                String target = findField.getText();
                String replacement = replaceField.getText();
                int count = 0;
                int index = -1;
                while ((index = text.indexOf(target, index + 1)) != -1) {
                    textArea.replaceRange(replacement, index, index + target.length());
                    index += replacement.length() - target.length();
                    count++;
                }
                JOptionPane.showMessageDialog(FindAndReplaceDialog.this,
                        "替换了 " + count + " 处匹配的文本", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 显示对话框
        pack();
        setLocationRelativeTo(textArea);
        setVisible(true);
    }
}

