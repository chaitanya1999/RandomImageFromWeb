/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomimagefromweb;

import com.sun.glass.events.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javax.swing.Timer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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

    ArrayList<String> imgurls = new ArrayList<>();
    int imageIndex=0;
    
    
    ImageView imageView = null;
    String USER_AGENT="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36";
    
    boolean loadingYet = false;    
    boolean imageDebug = true;
    
    File curImage=null;
    static HashSet<String> downloadedUrls = new HashSet<>();
    
    static class ImageView extends JLabel{
        public double sx=1.0,sy=1.0;
        
        @Override
        public void paint(Graphics g){
            Graphics2D gg = (Graphics2D) g;
            
            if(sx<sy)sy=sx;
            else sx=sy;
            
            gg.scale(sx, sy);
            super.paint(g);
        }
        
        @Override
        public ImageIcon getIcon(){
            return (ImageIcon)super.getIcon();
        }
    }
    
    
//    Timer timer = new Timer(100, new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (curImage != null && !loadingYet) {
//                loadImageInImageView();
//                timer.stop();
//            }
//        }
//    });
//    
    public frame() {
        initComponents();
        imageView = new ImageView();
        imgContainer.setLayout(new BorderLayout());
        imgContainer.add(imageView,BorderLayout.CENTER);
        
        imageView.setHorizontalAlignment(JLabel.LEFT);
        imageView.setVerticalAlignment(JLabel.TOP);
        

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, Tools.trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        
        if(imageDebug){
            curImage = new File("Images/18186421-gif");
//            try {
//                URL url = frame.class.getClassLoader().getResource("res/mario.gif");
//                curImage = new File(url.toURI());
//            } catch (URISyntaxException ex) {
//                Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
            loadImageInImageView();
            System.out.println("debug image");
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
        imgContainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Random Image From Web");
        setAlwaysOnTop(true);
        setMaximumSize(new java.awt.Dimension(400, 300));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
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
        curImageUrl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                curImageUrlMouseClicked(evt);
            }
        });
        getContentPane().add(curImageUrl, java.awt.BorderLayout.PAGE_END);
        getContentPane().add(imgContainer, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        imageIndex = (imageIndex+1)%imgurls.size();
            loadingYet=true;
            String url = imgurls.get(imageIndex);
            curImageUrl.setText(url);
            
            String filename = Tools.urlToFilename(url);
            
            File file = new File("./Images/"+filename);
            file.getParentFile().mkdirs();
            
            System.out.println("imagefile = " + filename);
            
        try {
            //CHANGE HERE USE URL INSTEAD OF FILENAME IN HASH SET RIGHT,!!XD :p
            if(!downloadedUrls.contains(url)){
                HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
                c.addRequestProperty("User-Agent", USER_AGENT);
                Files.copy(c.getInputStream(), file.toPath());
                downloadedUrls.add(url);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        curImage = file;
        loadImageInImageView();
        System.out.println("displayed, soft loaded");
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        driver.quit();
    }//GEN-LAST:event_formWindowClosing

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
//        timer.restart();
        setImageAndRescaleView(imageView.getIcon());
    }//GEN-LAST:event_formComponentResized

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            loadingYet=true;
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

                Document doc = Jsoup.connect("https://www.google.com/search?tbs=itp:animated&tbm=isch&source=lnms&q=" + searchQuery)
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

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        
    }//GEN-LAST:event_formMouseReleased

    private void curImageUrlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_curImageUrlMouseClicked
        System.out.println(curImageUrl.getText());
    }//GEN-LAST:event_curImageUrlMouseClicked

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
    private javax.swing.JPanel imgContainer;
    private javax.swing.JButton jButton1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    private void loadImageInImageView() {
        ImageIcon ic = null;
//            if(!curImage.getAbsolutePath().equals(imageView.imagePath))
//                ic = new ImageIcon(Tools.readImgFromFile(curImage.getAbsolutePath()));
        ic = new ImageIcon(Toolkit.getDefaultToolkit().createImage(curImage.getAbsolutePath()));
        setImageAndRescaleView(ic);
        loadingYet = false;
    }
    
    private void setImageAndRescaleView(ImageIcon ic){
        int width = imgContainer.getWidth();
        int height = imgContainer.getHeight();

        double sx = (width * 1.0) / ic.getIconWidth();
        double sy = (height * 1.0) / ic.getIconHeight();

        imageView.sx = sx;
        imageView.sy = sy;

        imageView.setIcon(ic);
    }
}
