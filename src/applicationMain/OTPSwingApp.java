package applicationMain;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class OTPSwingApp {

    private static final SecureRandom RNG = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    private static String generateOTP(int length) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(RNG.nextInt(10));
        }
        return otp.toString();
    }

    private static void copyToClipboard(String text) {
        try {
            StringSelection selection = new StringSelection(text);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        } catch (Exception e) {
            // ignore clipboard errors
        }
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("One-Time Password");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(320, 140));

        JPanel content = new JPanel(new BorderLayout(10, 10));

        JLabel otpLabel = new JLabel("", SwingConstants.CENTER);
        otpLabel.setFont(otpLabel.getFont().deriveFont(24f));
        content.add(otpLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton resetBtn = new JButton("Reset OTP");
        buttonPanel.add(resetBtn);
        content.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(content);
        frame.pack();
        frame.setLocationRelativeTo(null);

        // initial OTP
        Runnable setOtp = () -> {
            String otp = generateOTP(OTP_LENGTH);
            otpLabel.setText(otp);
            copyToClipboard(otp);
        };

        setOtp.run();

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setOtp.run();
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
}
