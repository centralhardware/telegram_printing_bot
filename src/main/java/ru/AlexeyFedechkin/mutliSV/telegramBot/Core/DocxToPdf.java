package ru.AlexeyFedechkin.mutliSV.telegramBot.Core;

import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import lombok.extern.slf4j.Slf4j;
import ooo.connector.BootstrapSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class DocxToPdf {

    private static final Logger log = LoggerFactory.getLogger(DocxToPdf.class);

    public static File convert(InputStream stream) throws Exception, BootstrapException, IOException {
        XComponentContext xContext = BootstrapSocketConnector.bootstrap("/usr/bin/soffice");
        XMultiComponentFactory xMCF = xContext.getServiceManager();
        Object oDesktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);
        XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, oDesktop);
        XComponentLoader xCompLoader = UnoRuntime.queryInterface(XComponentLoader.class, xDesktop);
        PropertyValue[] propertyValues = new PropertyValue[0];

        File tempFile = File.createTempFile("docx_to_pdf_converter", ".docx");
        tempFile.deleteOnExit();
        OutputStream outStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        stream.close();
        outStream.close();

        XComponent xComp = xCompLoader.loadComponentFromURL(
                String.format("file:///%s", tempFile.getAbsolutePath()), "_blank", 0, propertyValues);

        XStorable xStorable = UnoRuntime
                .queryInterface(XStorable.class, xComp);
        propertyValues = new PropertyValue[2];
        propertyValues[0] = new PropertyValue();
        propertyValues[0].Name = "Overwrite";
        propertyValues[0].Value = Boolean.TRUE;
        propertyValues[1] = new PropertyValue();
        propertyValues[1].Name = "FilterName";
        propertyValues[1].Value = "writer_pdf_Export";

        File result = File.createTempFile("pdf", ".pdf");
        result.deleteOnExit();
        xStorable.storeToURL(String.format("file:///%s", result.getAbsolutePath()), propertyValues);

        log.info("converted docx to pdf");
        return result;
    }
}
