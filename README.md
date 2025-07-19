# 🕵️‍♂️ Digital Steganography App

A simple Java Swing application for encoding and decoding hidden messages inside images using basic steganography techniques.

## 📌 Features

- 🔐 Embed secret messages inside PNG images.
- 🔑 Protect messages with a secret key.
- 🖼️ Decode and verify hidden messages using the correct key.
- 🧠 Built with basic LSB (Least Significant Bit) image manipulation.
- 🧪 Easy-to-use GUI via Java Swing.

## 🖥️ Screenshots

> <img width="733" height="525" alt="home" src="https://github.com/user-attachments/assets/5e2e8d11-90b8-4ff4-a895-3cbbe8aa708e" />


## 🚀 How It Works

The app hides a secret message inside the least significant bits (LSBs) of the image’s pixels. A secret key is used to prepend to the message, helping ensure only those with the correct key can decode it successfully.

## 🛠️ Getting Started

### Requirements

- Java JDK 8 or higher

### Running the App

1. Clone the repository:

   ```bash
   git clone https://github.com/iazimshaikh/steganography-app.git
   cd digital-steganography
   ```

2. Compile the Java file:
   
   ```bash
   javac SteganographyApp.java
   ```

4. Run the application:
   
   ```bash
   java SteganographyApp
   ```

## 💡 Usage
1. Click "Select Image" and choose a PNG image.
2. Enter the message you want to hide.
3. Enter a secret key.
4. Click "Encode Message" to embed the message and save the image as encoded_image.png.
5. To decode:
  - Load the encoded image via "Select Image".
  - Enter the same secret key.
  - Click "Decode Message".

## ⚙️ How Encoding Works
- The app stores each bit of the message into the least significant bit of each pixel’s blue component.
- The message is saved in the format:
```bash
key:message
``` 
This allows the decoding logic to verify the key before displaying the message.

## 📂 Output
- Encoded image will be saved in the current working directory as:
  - encoded_image.png

## ⚠️ Limitations
- Works only with PNG images (lossless format recommended).
- Maximum message length is constrained by the image’s pixel count.
- No encryption—only key-based validation.

## 📃 License
This project is licensed under the MIT License. See the LICENSE file for details.
