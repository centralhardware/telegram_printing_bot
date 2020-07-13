package ru.AlexeyFedechkin.mutliSV.telegramBot.Core;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.javase.QRCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class QR {

    public static InputStream generateQRCodeImage(@NonNull String barcodeText) {
        log.info(String.format("generate QR code from String: %s", barcodeText));
        ByteArrayOutputStream stream = QRCode
                .from(barcodeText)
                .withSize(250, 250)
                .stream();
        return new ByteArrayInputStream(stream.toByteArray());
    }

    public static String readQRCodeImage(@NonNull InputStream inputStream) throws NotFoundException, IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);
        log.info(String.format("read QR from image: %s", result.getText()));
        return result.getText();
    }

}
