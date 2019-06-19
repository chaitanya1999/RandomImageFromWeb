/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomimagefromweb;

import com.sun.glass.events.KeyEvent;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Chaitanya V
 */
public class frame extends javax.swing.JFrame {

    Random random = new Random();
    ArrayList<String> imgurls = new ArrayList<>();
    SwingFXImageView imgView = null;
    String USER_AGENT="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36";
    
    File curImage=null;
    
    public frame() {
        initComponents();
        imgView = new SwingFXImageView();
        getContentPane().add(imgView,BorderLayout.CENTER);
        
        SwingUtilities.invokeLater(()->{
            imgView.imageview.setFitHeight(imgView.getHeight());
            imgView.imageview.setFitWidth(imgView.getWidth());
        });
        
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }
            }
        };

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        curImageUrl = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setMaximumSize(new java.awt.Dimension(400, 300));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                formPropertyChange(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(15, 10));

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        getContentPane().add(jTextField1, java.awt.BorderLayout.PAGE_START);

        jButton1.setText(">>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, java.awt.BorderLayout.LINE_END);

        curImageUrl.setEditable(false);
        getContentPane().add(curImageUrl, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int ri = random.nextInt(imgurls.size());
        try {
            String url = imgurls.get(ri);
            curImageUrl.setText(url);
            
            String filename = url.substring(Math.max(url.lastIndexOf('/'),url.length()-100)+1);
            int lastIndex=-1;
            if((lastIndex=filename.lastIndexOf('.'))>=0)filename=new StringBuilder(filename).replace(lastIndex, lastIndex+1, "-").toString();
            filename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            File file = new File("./Images/"+filename);
            file.getParentFile().mkdirs();
            
            System.out.println("imagefile = " + filename);
            
            
            HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
            c.addRequestProperty("User-Agent", USER_AGENT);
            
            Files.copy(c.getInputStream(), file.toPath());
            Image img = new Image(new FileInputStream(curImage = file.getAbsoluteFile()));
            System.out.println("displayed");
            
            imgView.imageview.setImage(img);
            System.out.println("soft loaded");
        } catch (MalformedURLException ex) {
            Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        driver.quit();
    }//GEN-LAST:event_formWindowClosing

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        SwingUtilities.invokeLater(()->{
            imgView.imageview.setFitHeight(imgView.getHeight());
            imgView.imageview.setFitWidth(imgView.getWidth());
            if (curImage != null) {
                try {
                    Image img = new Image(new FileInputStream(curImage.getAbsoluteFile()));
                    imgView.imageview.setImage(img);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }//GEN-LAST:event_formComponentResized

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            String searchQuery = jTextField1.getText();

            System.out.println("searchquery = " + searchQuery);
            try {
                searchQuery = URLEncoder.encode(searchQuery, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                imgurls = new ArrayList<>();

                HttpURLConnection.setFollowRedirects(true);

                Document doc = Jsoup.connect("https://www.google.com/search?safe=images&tbs=itp:animated&tbm=isch&source=lnms&q=" + searchQuery)
                        .userAgent(USER_AGENT)
                        .get();

                Elements elements = doc.getElementsByClass("rg_meta");

                for (Element ele : elements) {

                    String inner = ele.text();

                    try {
                        JSONObject obj = (JSONObject) new JSONParser().parse(inner);
                        switch (((String) obj.get("ity"))) {
                            case "jpg":
                            case "gif":
                            case "png":
                            case "bmp":
                            case "jpeg": {
                                String iu = (String) obj.get("ou");
                                imgurls.add(iu);
                                System.out.println("# " + iu);
                                break;
                            }
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } catch (MalformedURLException ex) {
                Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("hard loaded. now soft loading.");

            jButton1.doClick();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_formPropertyChange

    }//GEN-LAST:event_formPropertyChange

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField curImageUrl;
    private javax.swing.JButton jButton1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
