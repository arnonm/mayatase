/************************************************************\
 *      Copyright (C) 2016 The Infinite Kind, Limited       *
\************************************************************/

package com.moneydance.modules.features.tlvbonds;

import com.moneydance.awt.*;
import com.infinitekind.moneydance.model.*;
// import com.infinitekind.moneydance.model.Account.AccountType;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
// import java.util.Arrays;
// import java.util.ArrayList;
// import java.util.List;

/** Window used for Account List interface */

public class AccountListWindow extends JFrame implements ActionListener {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Main extension;
    private JTextArea accountListArea;
    private JList accountList;
    // private JButton clearButton;
    private JButton closeButton;
    private JButton updateButton;
    private JTextField inputArea;

    private AccountBook book;

    public AccountListWindow(Main extension) {
        super("Select Investment Account to update");
        this.extension = extension;

        accountListArea = new JTextArea();

        book = extension.getUnprotectedContext().getCurrentAccountBook();
        StringBuffer acctStr = new StringBuffer();

        if (book != null) {
            addSubAccounts(book.getRootAccount(), acctStr);
        }
        // accountListArea.setEditable(false);
        // accountListArea.setText(acctStr.toString());
        inputArea = new JTextField();

        String accountListArray[] = acctStr.toString().split("\n");

        accountList = new JList(accountListArray);
        accountList.setSelectedIndex(1);

        inputArea.setEditable(true);

        // clearButton = new JButton("Clear");
        closeButton = new JButton("Close");
        updateButton = new JButton("Update");

        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        // p.add(new JScrollPane(accountListArea),
        // AwtUtil.getConstraints(0,0,1,1,4,1,true,true));
        p.add(new JScrollPane(accountList), AwtUtil.getConstraints(0, 0, 1, 1, 4, 1, true, true));
        p.add(Box.createVerticalStrut(8), AwtUtil.getConstraints(0, 2, 0, 0, 1, 1, false, false));
        p.add(updateButton, AwtUtil.getConstraints(0, 3, 1, 0, 1, 1, false, true));
        p.add(closeButton, AwtUtil.getConstraints(1, 3, 1, 0, 1, 1, false, true));
        getContentPane().add(p);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        enableEvents(WindowEvent.WINDOW_CLOSING);
        closeButton.addActionListener(this);
        // clearButton.addActionListener(this);
        updateButton.addActionListener(this);

        PrintStream c = new PrintStream(new ConsoleStream());

        setSize(500, 400);
        AwtUtil.centerWindow(this);
    }

    public static void addSubAccounts(Account parentAcct, StringBuffer acctStr) {
        int sz = parentAcct.getSubAccountCount();
        for (int i = 0; i < sz; i++) {
            Account acct = parentAcct.getSubAccount(i);
            if (accountIsInvestment(acct)) {
                acctStr.append(acct.getFullAccountName());
                acctStr.append("\n");
            }
            addSubAccounts(acct, acctStr);
        }
    }

    private static boolean accountIsInvestment(Account account) {

        return (account.getAccountType() == Account.AccountType.INVESTMENT);
    }

    public void UpdateStocks(Account account) {
        return;
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == closeButton) {
            extension.closeConsole();
        }
        // if(src==clearButton) {
        // accountListArea.setText("");
        // }
        if (src == updateButton) {
            String msg = String.format("Index %d selected %s", accountList.getSelectedIndex(),
                    accountList.getSelectedValue());
            if (book != null) {
                Account rootaccount = book.getRootAccount();
                     = rootaccount.getAccountByName((String) accountList.getSelectedValue(),
                        Account.AccountType.INVESTMENT);
                if (account != null) {
                    UpdateStocks(account);

                }
            }
        }
    }

    public final void processEvent(AWTEvent evt) {
        if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
            extension.closeConsole();
            return;
        }
        if (evt.getID() == WindowEvent.WINDOW_OPENED) {
        }
        super.processEvent(evt);
    }

    private class ConsoleStream extends OutputStream implements Runnable {
        public void write(int b) throws IOException {
            accountListArea.append(String.valueOf((char) b));
            repaint();
        }

        public void write(byte[] b) throws IOException {
            accountListArea.append(new String(b));
            repaint();
        }

        public void run() {
            accountListArea.repaint();
        }
    }

    void goAway() {
        setVisible(false);
        dispose();
    }
}
