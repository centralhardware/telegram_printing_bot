package ru.AlexeyFedechkin.mutliSV.telegramBot.Core;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.NonNull;
import net.glxn.qrgen.javase.QRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.AlexeyFedechkin.mutliSV.telegramBot.Core.Cups.Cups;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class QR {

    private static final Logger log = LoggerFactory.getLogger(Cups.class);

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
