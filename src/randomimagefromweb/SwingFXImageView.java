package randomimagefromweb;

import com.sun.javafx.application.PlatformImpl;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JPanel;
  
/** 
 * SwingFXWebView 
 */  
public class SwingFXImageView extends JPanel {  
     
    private Stage stage;  
    public ImageView imageview;  
    private JFXPanel jfxPanel;  
  
    public SwingFXImageView(){  
        imageview = new ImageView();
        initComponents();  
    }  
     
    private void initComponents(){  
         
        jfxPanel = new JFXPanel();  
        createScene();  
         
        setLayout(new BorderLayout());  
        add(jfxPanel, BorderLayout.CENTER);           
    }     
     
    /** 
     * createScene 
     * 
     * Note: Key is that Scene needs to be created and run on "FX user thread" 
     *       NOT on the AWT-EventQueue Thread 
     * 
     */  
    private void createScene() {  
        PlatformImpl.startup(new Runnable() {  
            @Override
            public void run() {  
                 
                stage = new Stage();  
                 
                stage.setTitle("Hello Java FX");  
                stage.setResizable(true);  
   
                Group root = new Group();  
                Scene scene = new Scene(root,80,20);  
                stage.setScene(scene);  
                 
                // Set up the embedded imageview:
                imageview.setPreserveRatio(true);
                System.out.println("debug");
                
                ObservableList<Node> children = root.getChildren();
                children.add(imageview);                     
                 
                jfxPanel.setScene(scene);  
            }  
        });  
    }
}