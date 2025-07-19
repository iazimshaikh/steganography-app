import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Arrays;

public class SteganographyApp extends JFrame {
    private BufferedImage image;
    private JTextField messageField;
    private JTextField keyField;
    private JLabel statusLabel;

    public SteganographyApp() {
        setTitle("Digital Steganography");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton selectImageButton = new JButton("Select Image");
        JButton encodeButton = new JButton("Encode Message");
        JButton decodeButton = new JButton("Decode Message");
        messageField = new JTextField(20);
        keyField = new JTextField(20);
        statusLabel = new JLabel("");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(selectImageButton);
        panel.add(new JLabel("Enter message:"));
        panel.add(messageField);
        panel.add(new JLabel("Enter secret key:"));
        panel.add(keyField);
        panel.add(encodeButton);
        panel.add(decodeButton);
        panel.add(statusLabel);

        add(panel);

        selectImageButton.addActionListener(e -> selectImage());
        encodeButton.addActionListener(e -> encodeMessage());
        decodeButton.addActionListener(e -> decodeMessage());
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                image = ImageIO.read(selectedFile);
                statusLabel.setText("Image selected: " + selectedFile.getName());
            } catch (Exception e) {
                statusLabel.setText("Error loading image.");
            }
        }
    }

    private void encodeMessage() {
        if (image == null) {
            statusLabel.setText("No image selected.");
            return;
        }
        String message = messageField.getText();
        String key = keyField.getText();

        if (message.isEmpty() || key.isEmpty()) {
            statusLabel.setText("Message and key cannot be empty.");
            return;
        }

        String finalMessage = key + ":" + message;
        byte[] messageBytes = finalMessage.getBytes();

        if (messageBytes.length * 8 > image.getWidth() * image.getHeight()) {
            statusLabel.setText("Message is too long to encode.");
            return;
        }

        encodeMessageInImage(messageBytes);
        statusLabel.setText("Message encoded into image successfully!");

        try {

            File outputfile = new File("encoded_image.png");
            ImageIO.write(image, "png", outputfile);
            statusLabel.setText("Message encoded and saved as encoded_image.png");
        } catch (Exception e) {
            statusLabel.setText("Error saving encoded image.");
        }
    }

    private void decodeMessage() {
        if (image == null) {
            statusLabel.setText("No image selected.");
            return;
        }
        String key = keyField.getText();
        if (key.isEmpty()) {
            statusLabel.setText("Secret key cannot be empty.");
            return;
        }

        String hiddenMessage = decodeMessageFromImage();
        if (hiddenMessage.startsWith(key + ":")) {
            statusLabel.setText("Decoded message: " + hiddenMessage.substring(key.length() + 1));
        } else {
            statusLabel.setText("Incorrect key.");
        }
    }


    private void encodeMessageInImage(byte[] messageBytes) {
        int messageIndex = 0;
        int bitIndex = 0;
        outerLoop:
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                if (messageIndex < messageBytes.length) {
                    int messageBit = (messageBytes[messageIndex] >> (7 - bitIndex)) & 1;
                    b = (b & 0xFE) | messageBit;
                    bitIndex++;

                    if (bitIndex == 8) {
                        bitIndex = 0;
                        messageIndex++;
                    }

                    int newPixel = (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, newPixel);
                } else {
                    break outerLoop;
                }
            }
        }
    }

    private String decodeMessageFromImage() {
        byte[] messageBytes = new byte[image.getWidth() * image.getHeight() / 8];
        int messageIndex = 0;
        int bitIndex = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int b = pixel & 0xFF;
                int messageBit = b & 1;

                messageBytes[messageIndex] = (byte) ((messageBytes[messageIndex] << 1) | messageBit);
                bitIndex++;

                if (bitIndex == 8) {
                    bitIndex = 0;
                    messageIndex++;
                }

                if (messageIndex >= messageBytes.length) {
                    break;
                }
            }
        }
        return new String(messageBytes).trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SteganographyApp app = new SteganographyApp();
            app.setVisible(true);
        });
    }
}
