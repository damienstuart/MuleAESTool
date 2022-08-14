package com.devmetrix.dstuart;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Color;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class MuleAESTool {

	private JFrame frmMuleAesTool;
	private JTextField txtClearText;
	private JTextField txtEncryptedText;
	private JPasswordField txtKey;
	private JLabel lblMessage;
	private JButton btnEncrypt;
	private JButton btnDecrypt;
	private JCheckBox chkboxUseRandomIV;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MuleAESTool window = new MuleAESTool();
					window.frmMuleAesTool.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MuleAESTool() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMuleAesTool = new JFrame();
		frmMuleAesTool.getContentPane().setFocusTraversalPolicyProvider(true);
		frmMuleAesTool.setResizable(false);
		frmMuleAesTool.setTitle("MuleAESTool");
		frmMuleAesTool.setBounds(100, 100, 620, 205);
		frmMuleAesTool.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMuleAesTool.getContentPane().setLayout(null);
		
		char pwEchoChar;
		
		ImageIcon programIcon = new ImageIcon(getClass().getResource("/res/MuleAES-logo.png"));
		frmMuleAesTool.setIconImage(programIcon.getImage());
	
		MuleAES aes = new MuleAES();
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setMnemonic('Q');
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmMuleAesTool.dispose();
			}
		});
		btnQuit.setBounds(493, 122, 105, 29);
		frmMuleAesTool.getContentPane().add(btnQuit);
		
		JLabel lblClearText = new JLabel("Clear Text:");
		lblClearText.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClearText.setBounds(5, 45, 105, 16);
		frmMuleAesTool.getContentPane().add(lblClearText);
		
		JLabel lblEncryptedText = new JLabel("Encrypted Text:");
		lblEncryptedText.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEncryptedText.setBounds(5, 78, 105, 16);
		frmMuleAesTool.getContentPane().add(lblEncryptedText);
		
		txtClearText = new JTextField();
		txtClearText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					btnEncrypt.doClick();
			}
		});
		txtClearText.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				checkBtnStates();
			}
			public void removeUpdate(DocumentEvent e) {
				checkBtnStates();
			}
			public void insertUpdate(DocumentEvent e) {
				checkBtnStates();
			}
		});
		txtClearText.setBounds(111, 39, 370, 29);
		frmMuleAesTool.getContentPane().add(txtClearText);
		txtClearText.setColumns(10);
		
		txtEncryptedText = new JTextField();
		txtEncryptedText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					btnDecrypt.doClick();
			}
		});
		txtEncryptedText.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				checkBtnStates();
			}
			public void removeUpdate(DocumentEvent e) {
				checkBtnStates();
			}
			public void insertUpdate(DocumentEvent e) {
				checkBtnStates();
			}
		});
		txtEncryptedText.setColumns(10);
		txtEncryptedText.setBounds(111, 72, 370, 29);
		frmMuleAesTool.getContentPane().add(txtEncryptedText);
		
		JLabel lblKey = new JLabel("Key:");
		lblKey.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKey.setBounds(5, 12, 105, 16);
		frmMuleAesTool.getContentPane().add(lblKey);
		
		txtKey = new JPasswordField();
		txtKey.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				checkKeyField();
			}
		});
		txtKey.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				checkBtnStates();
				checkKeyField();
			}
			public void removeUpdate(DocumentEvent e) {
				checkBtnStates();
				checkKeyField();
			}
			public void insertUpdate(DocumentEvent e) {
				checkBtnStates();
				checkKeyField();
			}
		});
		txtKey.setColumns(10);
		txtKey.setBounds(111, 6, 185, 29);
		pwEchoChar = txtKey.getEchoChar();
		frmMuleAesTool.getContentPane().add(txtKey);
		
		btnEncrypt = new JButton("Encrypt");
		btnEncrypt.setToolTipText("Encrypt the Clear Text");
		btnEncrypt.setMnemonic('E');
		Image e_img = new ImageIcon(getClass().getResource("/res/encrypt.png")).getImage();
		btnEncrypt.setIcon(new ImageIcon(e_img));
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					txtEncryptedText.setText(aes.encrypt(new String(txtKey.getPassword()), txtClearText.getText(), chkboxUseRandomIV.isSelected()));
					txtEncryptedText.requestFocusInWindow();
					txtEncryptedText.selectAll();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frmMuleAesTool, e1.toString(), "Encryption Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnEncrypt.setBounds(495, 39, 100, 29);
		frmMuleAesTool.getContentPane().add(btnEncrypt);
		
		btnDecrypt = new JButton("Decrypt");
		btnDecrypt.setToolTipText("Decrypt the Encrypted Text");
		btnDecrypt.setMnemonic('D');
		Image d_img = new ImageIcon(getClass().getResource("/res/decrypt.png")).getImage();
		btnDecrypt.setIcon(new ImageIcon(d_img));
		btnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					txtClearText.setText(aes.decrypt(new String(txtKey.getPassword()), txtEncryptedText.getText(), chkboxUseRandomIV.isSelected()));
					txtClearText.requestFocusInWindow();
					txtClearText.selectAll();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frmMuleAesTool, e1.toString(), "Decryption Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDecrypt.setBounds(495, 72, 100, 29);
		frmMuleAesTool.getContentPane().add(btnDecrypt);
		
		JButton btnClearAll = new JButton("Clear All");
		btnClearAll.setToolTipText("Clear all fields");
		btnClearAll.setMnemonic('C');
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtClearText.setText("");
				txtEncryptedText.setText("");
				txtKey.setText("");
				txtKey.requestFocusInWindow();
			}
		});
		btnClearAll.setBounds(14, 122, 94, 29);
		frmMuleAesTool.getContentPane().add(btnClearAll);
		
		lblMessage = new JLabel("");
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage.setForeground(Color.RED);
		lblMessage.setBounds(125, 142, 316, 26);
		frmMuleAesTool.getContentPane().add(lblMessage);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 107, 608, 12);
		frmMuleAesTool.getContentPane().add(separator);
		
		JCheckBox chckbxShowKey = new JCheckBox("Show Key");
		chckbxShowKey.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		chckbxShowKey.setToolTipText("Make the key text visible");
		chckbxShowKey.setMnemonic('K');
		chckbxShowKey.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chckbxShowKey.isSelected())
					txtKey.setEchoChar((char)0);
				else
					txtKey.setEchoChar(pwEchoChar);
			}
		});
		chckbxShowKey.setBounds(300, 9, 99, 23);
		frmMuleAesTool.getContentPane().add(chckbxShowKey);
		
		chkboxUseRandomIV = new JCheckBox("Use Random IV");
		chkboxUseRandomIV.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		chkboxUseRandomIV.setSelected(true);
		chkboxUseRandomIV.setToolTipText("Use a random Initialization Vector when encrypting (recommended)");
		chkboxUseRandomIV.setMnemonic('I');
		chkboxUseRandomIV.setBounds(493, 9, 110, 23);
		frmMuleAesTool.getContentPane().add(chkboxUseRandomIV);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(484, 0, 12, 110);
		frmMuleAesTool.getContentPane().add(separator_1);
		frmMuleAesTool.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtKey, txtClearText, btnEncrypt, txtEncryptedText, btnDecrypt, btnClearAll, btnQuit}));
		
		checkBtnStates();
	}
	
	private void checkBtnStates() {
		btnEncrypt.setEnabled(txtClearText.getText().length() > 0 && txtKey.getPassword().length == 16);
		btnDecrypt.setEnabled(txtEncryptedText.getText().length() > 0 && txtKey.getPassword().length == 16);
	}
	
	private void checkKeyField() {
		int klen = txtKey.getPassword().length;
		if (klen == 0 || klen == 16)
			lblMessage.setText("");
		else
			lblMessage.setText("Key must be exactly 16 characters");
	}
}
