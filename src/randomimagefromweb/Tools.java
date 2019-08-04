/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomimagefromweb;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.w3c.dom.Node;
import sun.nio.ch.IOStatus;

/**
 *
 * @author Chaitanya V
 */
public class Tools {
    public static Image readImgFromFile(String filename) {
        File file = new File(filename);
        
        //CHECK IF MODIFIED FILE ALREADY EXISTS
        File[] files = file.getParentFile().listFiles((File dir, String name) -> name.equals(filename+"_"));
        if(files.length>0)return Toolkit.getDefaultToolkit().createImage(files[0].getAbsolutePath());
        
        
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            
            
            
            if(URLConnection.guessContentTypeFromStream(bis).contains("gif")){
                //
                System.out.println("readImgFromFile - FOUND GIF");
                //
            } else {
                System.out.println("readImgFromFile - NOT GIF");
                return Toolkit.getDefaultToolkit().createImage(filename);
            }
            bis.close();
            
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
        
        
        
        // Fix for bug when delay is 0
        try {
            
            // Get GIF reader
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            // Give it the stream to decode from
            reader.setInput(ImageIO.createImageInputStream(file));

            int numImages = reader.getNumImages(true);

            // Get 'metaFormatName'. Need first frame for that.
            IIOMetadata imageMetaData = reader.getImageMetadata(0);
            String metaFormatName = imageMetaData.getNativeMetadataFormatName();

            // Find out if GIF is bugged
            boolean foundBug = false;
            for (int i = 0; i < numImages && !foundBug; i++) {
                // Get metadata
                IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(i).getAsTree(metaFormatName);

                // Find GraphicControlExtension node
                int nNodes = root.getLength();
                for (int j = 0; j < nNodes; j++) {
                    Node node = root.item(j);
                    if (node.getNodeName().equalsIgnoreCase("GraphicControlExtension")) {
                        // Get delay value
                        String delay = ((IIOMetadataNode) node).getAttribute("delayTime");

                        // Check if delay is bugged
                        if (Integer.parseInt(delay) == 0) {
                            foundBug = true;
                        }

                        break;
                    }
                }
            }

            // Load non-bugged GIF the normal way
            Image image;
            if (!foundBug) {
                image = Toolkit.getDefaultToolkit().createImage(filename);
            } else {
                // Prepare streams for image encoding
//                ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
                FileOutputStream baoStream = new FileOutputStream(new File(filename+".fixed"));
                try (ImageOutputStream ios = ImageIO.createImageOutputStream(baoStream)) {
                    // Get GIF writer that's compatible with reader
                    ImageWriter writer = ImageIO.getImageWriter(reader);
                    // Give it the stream to encode to
                    writer.setOutput(ios);

                    writer.prepareWriteSequence(null);

                    for (int i = 0; i < numImages; i++) {
                        // Get input image
                        BufferedImage frameIn = reader.read(i);

                        // Get input metadata
                        IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(i).getAsTree(metaFormatName);

                        // Find GraphicControlExtension node
                        int nNodes = root.getLength();
                        for (int j = 0; j < nNodes; j++) {
                            Node node = root.item(j);
                            if (node.getNodeName().equalsIgnoreCase("GraphicControlExtension")) {
                                // Get delay value
                                String delay = ((IIOMetadataNode) node).getAttribute("delayTime");

                                // Check if delay is bugged
                                if (Integer.parseInt(delay) == 0) {
                                    // Overwrite with a valid delay value
                                    ((IIOMetadataNode) node).setAttribute("delayTime", "10");
                                }

                                break;
                            }
                        }

                        // Create output metadata
                        IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(frameIn), null);
                        // Copy metadata to output metadata
                        metadata.setFromTree(metadata.getNativeMetadataFormatName(), root);

                        // Create output image
                        IIOImage frameOut = new IIOImage(frameIn, null, metadata);

                        // Encode output image
                        writer.writeToSequence(frameOut, writer.getDefaultWriteParam());
                    }

                    writer.endWriteSequence();
                    writer.dispose();
                }
                
                reader.dispose();
                baoStream.close();
                
                
                // Create image using encoded data
                image = Toolkit.getDefaultToolkit().createImage(filename+".fixed");
            }

            // Trigger lazy loading of image
//            MediaTracker mt = new MediaTracker(parent);
//            mt.addImage(image, 0);
//            try {
//                mt.waitForAll();
//            } catch (InterruptedException e) {
//                image = null;
//            }
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }
            }
        };
    
    static String urlToFilename(String url){
        String filename = url.substring(Math.max(url.lastIndexOf('/'), url.length() - 100) + 1);
        int lastIndex = -1;
        if ((lastIndex = filename.lastIndexOf('.')) >= 0) {
            filename = new StringBuilder(filename).replace(lastIndex, lastIndex + 1, "-").toString();
        }

        StringBuilder sb = new StringBuilder("");
        String illegalChars = "<>:\"/\\|?*";
        for (char c : filename.toCharArray()) {
            if (illegalChars.contains("" + c)) {
                sb.append("_");
            } else {
                sb.append(c);
            }
        }
        filename = sb.toString();
        return filename;
    }
}
